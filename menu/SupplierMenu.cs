using System;
using System.Collections.Generic;
using vn.edu.crud.model;
using vn.edu.crud.service;
using vn.edu.crud.util;

namespace vn.edu.crud.menu;

public class SupplierMenu
{
    private readonly SupplierService _supplierService = new();
    private readonly PurchaseOrderService _poService = new();

    public void HienThi()
    {
        int chon;
        do
        {
            Console.WriteLine("\n╔══════════════════════════════════════╗");
            Console.WriteLine("║         QUẢN LÝ NHÀ CUNG CẤP         ║");
            Console.WriteLine("╠══════════════════════════════════════╣");
            Console.WriteLine("║  1. Xem tất cả danh sách             ║");
            Console.WriteLine("║  2. Nghiệp vụ (Tiếp nhận NCC)        ║");
            Console.WriteLine("║  3. Tìm kiếm nhà cung cấp (LINQ)     ║");
            Console.WriteLine("║  4. Xóa nhà cung cấp                 ║");
            Console.WriteLine("║  ── BÁO CÁO ──                       ║");
            Console.WriteLine("║  6. Tổng tiền nhập theo NCC          ║");
            Console.WriteLine("║  7. NCC đang cung cấp hoa            ║");
            Console.WriteLine("║  8. NCC cung cấp nhiều loại hoa nhất ║");
            Console.WriteLine("║  9. Thống kê loại hoa theo NCC       ║");
            Console.WriteLine("║  0. Quay lại                         ║");
            Console.WriteLine("╚══════════════════════════════════════╝");
            Console.Write("Chọn: ");

            if (!int.TryParse(Console.ReadLine()?.Trim(), out chon)) chon = -1;

            switch (chon)
            {
                case 1: InDanhSach(_supplierService.FindAll()); break;
                case 2: TiepNhanNhaCungCap(); break;
                case 3: TimKiemNhaCungCapLambda(); break;
                case 4: XoaNhaCungCap(); break;
                case 6: InBangHelper.InBang(_supplierService.TongNhapTheoNCC(), ["Mã NCC", "Tên NCC", "Tổng Tiền Nhập"]); break;
                case 7: InBangHelper.InBang(_supplierService.NccDangCungCapHoa(), ["Mã NCC", "Tên NCC", "SĐT", "Địa Chỉ", "Người Đại Diện"]); break;
                case 8: InBangHelper.InBang(_supplierService.NccNhieuLoaiHoaNhat(), ["Mã NCC", "Tên NCC", "Số Loài Hoa"]); break;
                case 9: InBangHelper.InBang(_supplierService.ThongKeLoaiHoaTheoNCC(), ["Mã NCC", "Tên NCC", "Số Loài Hoa"]); break;
                case 0: break;
                default: Console.WriteLine("Chọn sai!"); break;
            }
        } while (chon != 0);
    }

    private void TimKiemNhaCungCapLambda()
    {
        Console.WriteLine("\n--- TÌM KIẾM NHÀ CUNG CẤP (LINQ & LAMBDA) ---");
        Console.Write("Nhập tên nhà cung cấp cần tìm: ");
        string tuKhoa = Console.ReadLine()?.Trim() ?? "";

        var ketQua = _supplierService.SearchSuppliersByLambda(tuKhoa);

        if (ketQua.Count == 0)
        {
            Console.WriteLine($"❌ Không tìm thấy nhà cung cấp nào có tên khớp với: \"{tuKhoa}\"");
        }
        else
        {
            Console.WriteLine($"✅ Tìm thấy {ketQua.Count} nhà cung cấp phù hợp:");
            InDanhSach(ketQua);
        }
    }

    private void TiepNhanNhaCungCap()
    {
        Console.WriteLine("\n--- TIẾP NHẬN NHÀ CUNG CẤP ---");
        Console.Write("Nhập SĐT nhà cung cấp (VD: 0263388899, có thể nhập một phần): ");
        string sdt = NhapLieu.ChuanHoaSdt(Console.ReadLine());

        var ketQua = _supplierService.TimTheoSdt(sdt);

        if (ketQua.Count == 0)
        {
            Console.WriteLine("❌ Không tìm thấy NCC nào dùng SĐT này.");
            Console.Write("👉 Tạo hồ sơ nhà cung cấp mới? (y/n): ");
            if (Console.ReadLine()?.Trim().ToLower() == "y")
            {
                ThemNhaCungCapMoi(sdt);
            }
        }
        else
        {
            Console.WriteLine("✅ Tìm thấy nhà cung cấp:");
            InDanhSach(ketQua);

            var ncc = ketQua[0];
            Console.Write("👉 Bạn có muốn cập nhật (sửa) thông tin NCC này không? (y/n): ");
            if (Console.ReadLine()?.Trim().ToLower() == "y")
            {
                SuaNhaCungCap(ncc);
            }
        }
    }

    private void XoaNhaCungCap()
    {
        Console.WriteLine("\n--- XÓA NHÀ CUNG CẤP ---");
        Console.Write("Nhập Mã NCC cần xóa (VD: NCC01): ");
        string id = Console.ReadLine()?.Trim().ToUpper() ?? "";

        var ncc = _supplierService.FindById(id);
        if (ncc == null)
        {
            Console.WriteLine($"❌ Không tìm thấy NCC có mã: {id}");
            return;
        }

        Console.WriteLine("\n--- THÔNG TIN NHÀ CUNG CẤP ---");
        Console.WriteLine($"Tên NCC : {ncc.Name}");
        Console.WriteLine($"SĐT     : {ncc.Phone}");

        if (_poService.PhieuNhapCuaNCC(id).Count > 0)
        {
            Console.WriteLine("\n❌ KHÔNG THỂ XÓA! NCC này đã có Phiếu Nhập Hàng trong hệ thống.");
            return;
        }
        if (_supplierService.CoHoa(id))
        {
            Console.WriteLine("\n❌ KHÔNG THỂ XÓA! NCC này đang cung cấp loại hoa trong danh mục.");
            Console.WriteLine("👉 Chuyển các loại hoa đó sang NCC khác (menu Hoa → Sửa) rồi hãy xóa.");
            return;
        }

        Console.Write("\nBạn có chắc chắn muốn xóa NCC này? (y/n): ");
        if (Console.ReadLine()?.Trim().ToLower() == "y")
        {
            Console.WriteLine(_supplierService.Delete(id) ? "✅ Đã xóa!" : "❌ Thất bại!");
        }
        else
        {
            Console.WriteLine("Đã hủy thao tác.");
        }
    }

    private void ThemNhaCungCapMoi(string sdtDaNhap)
    {
        string id = _supplierService.SinhMaNCCMoi();
        Console.WriteLine($"Mã NCC tự sinh: {id}");

        string sdt = (string.IsNullOrEmpty(sdtDaNhap) || !NhapLieu.LaSdtNhaCungCap(sdtDaNhap))
            ? NhapLieu.SdtNhaCungCap("SĐT NCC")
            : sdtDaNhap;
        string ten = NhapLieu.BatBuoc("Tên NCC", "VD: Vựa Hoa Đà Lạt");
        string diaChi = NhapLieu.TuyChon("Địa chỉ", "VD: Đà Lạt, Lâm Đồng");
        string daiDien = NhapLieu.TuyChon("Người đại diện", "VD: Ông Bảy");

        var ncc = new Supplier(id, ten, sdt, diaChi, daiDien);
        if (_supplierService.Insert(ncc))
            Console.WriteLine("✅ Thêm nhà cung cấp thành công!");
    }

    private void SuaNhaCungCap(Supplier ncc)
    {
        Console.WriteLine("\n--- SỬA THÔNG TIN NHÀ CUNG CẤP ---");
        Console.Write("Tên mới (Enter = giữ nguyên, VD: Vựa Hoa Đà Lạt): ");
        string ten = Console.ReadLine()?.Trim() ?? "";
        if (!string.IsNullOrEmpty(ten)) ncc.Name = ten;

        Console.Write("Địa chỉ mới (Enter = giữ nguyên, VD: Đà Lạt, Lâm Đồng): ");
        string diaChi = Console.ReadLine()?.Trim() ?? "";
        if (!string.IsNullOrEmpty(diaChi)) ncc.Address = diaChi;

        Console.Write("Người đại diện mới (Enter = giữ nguyên, VD: Ông Bảy): ");
        string daiDien = Console.ReadLine()?.Trim() ?? "";
        if (!string.IsNullOrEmpty(daiDien)) ncc.Representative = daiDien;

        if (_supplierService.Update(ncc))
            Console.WriteLine("✅ Sửa thành công!");
    }

    private void InDanhSach(List<Supplier> list)
    {
        if (list.Count == 0) { Console.WriteLine("Không có dữ liệu."); return; }
        var rows = list.Select(s => new object[] {
            s.SupplierId, s.Name, s.Phone, s.Address ?? "", s.Representative ?? ""
        }).ToList();
        InBangHelper.InBang(rows, ["Mã NCC", "Tên NCC", "SĐT", "Địa Chỉ", "Người Đại Diện"]);
    }

    private void InBang(List<object[]> list, string[]? headers = null)
    {
        InBangHelper.InBang(list, headers);
    }
}
