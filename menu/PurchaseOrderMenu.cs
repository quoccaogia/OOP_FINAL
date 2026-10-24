using System;
using System.Collections.Generic;
using System.Globalization;
using vn.edu.crud.model;
using vn.edu.crud.service;
using vn.edu.crud.util;

namespace vn.edu.crud.menu;

public class PurchaseOrderMenu
{
    private readonly PurchaseOrderService _poService = new();
    private readonly FlowerBatchService _batchService = new();
    private readonly SupplierService _supplierService = new();
    private readonly EmployeeService _employeeService = new();
    private readonly FlowerService _flowerService = new();
    private readonly CultureInfo _vnCulture = new("vi-VN");

    public void HienThi()
    {
        int chon;
        do
        {
            Console.WriteLine("\n╔══════════════════════════════════════╗");
            Console.WriteLine("║    QUẢN LÝ PHIẾU NHẬP HÀNG           ║");
            Console.WriteLine("╠══════════════════════════════════════╣");
            Console.WriteLine("║  ── THAO TÁC ──                      ║");
            Console.WriteLine("║  1. Xem tất cả                       ║");
            Console.WriteLine("║  2. Tìm theo mã (LINQ)               ║");
            Console.WriteLine("║  3. Tạo Phiếu Nhập Mới (Nhập Hoa)    ║");
            Console.WriteLine("║  4. Xóa phiếu nhập                   ║");
            Console.WriteLine("║  ── XỬ LÝ DỞ DANG ──                 ║");
            Console.WriteLine("║  5. Chốt phiếu nhập dở dang          ║");
            Console.WriteLine("║  ── BÁO CÁO ──                       ║");
            Console.WriteLine("║  6. Tổng tiền nhập theo NCC (LINQ)   ║");
            Console.WriteLine("║  7. Phiếu nhập theo NCC (LINQ)       ║");
            Console.WriteLine("║  8. Tất cả phiếu nhập (kèm tên)      ║");
            Console.WriteLine("║  0. Quay lại                         ║");
            Console.WriteLine("╚══════════════════════════════════════╝");
            Console.Write("Chọn: ");

            if (!int.TryParse(Console.ReadLine()?.Trim(), out chon)) chon = -1;

            switch (chon)
            {
                case 1: InBangHelper.InBang(_poService.FindAll(), ["Mã PN", "Mã NCC", "Mã NV", "Ngày Nhập", "Tổng Tiền", "Trạng Thái", "Ghi Chú"]); break;
                case 2: TimTheoMa(); break;
                case 3: TaoPhieuNhapMoi(); break;
                case 4: XoaPhieu(); break;
                case 5: ChotPhieuDoDang(); break;
                case 6: TongNhapTheoNCCLambda(); break;
                case 7: PhieuNhapTheoNCCLambda(); break;
                case 8: InBangHelper.InBang(_poService.TatCaPhieuNhap(), ["Mã PN", "Nhà Cung Cấp", "Nhân Viên Nhập", "Ngày Nhập", "Tổng Tiền", "Trạng Thái", "Ghi Chú"]); break;
                case 0: break;
                default: Console.WriteLine("Chọn sai!"); break;
            }
        } while (chon != 0);
    }

    private void TimTheoMa()
    {
        Console.Write("Nhập mã phiếu nhập (VD: PN001): ");
        string ma = Console.ReadLine()?.Trim().ToUpper() ?? "";
        var ds = _poService.TimPhieuTheoMaLambda(ma);
        if (ds.Count == 0)
        {
            Console.WriteLine($"❌ Không tìm thấy phiếu {ma}!");
            return;
        }
        InDsPhieu(ds);
    }

    private void TaoPhieuNhapMoi()
    {
        string poId = _poService.SinhMaPNMoi();
        Console.WriteLine($"\n--- TẠO PHIẾU NHẬP MỚI ({poId}) ---");

        var dsNCC = _supplierService.FindAll();
        if (dsNCC.Count == 0)
        {
            Console.WriteLine("❌ Chưa có Nhà cung cấp nào trong hệ thống!");
            return;
        }
        Console.WriteLine("\n--- CHỌN NHÀ CUNG CẤP ---");
        for (int i = 0; i < dsNCC.Count; i++)
        {
            Console.WriteLine($"  {i + 1}. {dsNCC[i].Name,-25} (Mã: {dsNCC[i].SupplierId})");
        }
        string supId = "";
        while (string.IsNullOrEmpty(supId))
        {
            Console.Write($"Chọn NCC (1-{dsNCC.Count} hoặc nhập Mã NCC): ");
            string nhapNCC = Console.ReadLine()?.Trim() ?? "";
            if (int.TryParse(nhapNCC, out int idx) && idx >= 1 && idx <= dsNCC.Count)
                supId = dsNCC[idx - 1].SupplierId;
            else if (_supplierService.FindById(nhapNCC.ToUpper()) != null)
                supId = nhapNCC.ToUpper();
            else
                Console.WriteLine("⚠️ NCC không hợp lệ!");
        }

        var dsNV = _employeeService.FindAll();
        if (dsNV.Count == 0)
        {
            Console.WriteLine("❌ Chưa có Nhân viên nào trong hệ thống!");
            return;
        }
        Console.WriteLine("\n--- CHỌN NHÂN VIÊN NHẬP HÀNG ---");
        for (int i = 0; i < dsNV.Count; i++)
        {
            Console.WriteLine($"  {i + 1}. {dsNV[i].Name,-20} (Mã: {dsNV[i].EmployeeId})");
        }
        string empId = "";
        while (string.IsNullOrEmpty(empId))
        {
            Console.Write($"Chọn NV (1-{dsNV.Count} hoặc nhập Mã NV): ");
            string nhapNV = Console.ReadLine()?.Trim() ?? "";
            if (int.TryParse(nhapNV, out int idx) && idx >= 1 && idx <= dsNV.Count)
                empId = dsNV[idx - 1].EmployeeId;
            else if (_employeeService.FindById(nhapNV.ToUpper()) != null)
                empId = nhapNV.ToUpper();
            else
                Console.WriteLine("⚠️ Nhân viên không hợp lệ!");
        }

        DateTime date = DateTime.Now;
        if (!_poService.Insert(poId, supId, empId, date, 0m, "CHO_XU_LY", "Khởi tạo phiếu"))
        {
            Console.WriteLine("❌ Lỗi khởi tạo phiếu nhập!");
            return;
        }

        Console.WriteLine($"✅ Đã tạo phiếu {poId}. Bắt đầu nhập hoa vào lô:");
        decimal tongTien = 0;

        while (true)
        {
            var dsHoa = _flowerService.FindAll();
            Console.WriteLine("\n--- CHỌN LOẠI HOA NHẬP ---");
            for (int i = 0; i < dsHoa.Count; i++)
            {
                Console.WriteLine($"  {i + 1}. {dsHoa[i].Name,-25} (Mã: {dsHoa[i].FlowerId} - Tồn: {dsHoa[i].StockQuantity})");
            }
            Console.WriteLine("  Enter để kết thúc nhập hoa");
            Console.Write($"Chọn hoa (1-{dsHoa.Count} hoặc Mã Hoa): ");
            string nhapHoa = Console.ReadLine()?.Trim() ?? "";
            if (string.IsNullOrEmpty(nhapHoa)) break;

            Flower? hoa = null;
            if (int.TryParse(nhapHoa, out int idxHoa) && idxHoa >= 1 && idxHoa <= dsHoa.Count)
                hoa = dsHoa[idxHoa - 1];
            else
                hoa = _flowerService.TimTheoMa(nhapHoa.ToUpper());

            if (hoa == null)
            {
                Console.WriteLine("❌ Hoa không hợp lệ!");
                continue;
            }

            string fId = hoa.FlowerId;
            int sl = NhapLieu.SoNguyen("Số lượng nhập", "VD: 100", 1, 100_000);
            decimal giaVon = NhapLieu.Tien("Giá vốn mỗi bông", "VD: 3000", false);
            int hanDungNgay = NhapLieu.SoNguyen("Hạn sử dụng (số ngày kể từ hôm nay)", "VD: 7", 1, 365);
            DateTime hanDung = DateTime.Today.AddDays(hanDungNgay);

            string batchId = _batchService.SinhMaLoMoi();
            if (_batchService.Insert(batchId, poId, fId, sl, sl, giaVon, date, hanDung, "AVAILABLE", "Nhập theo " + poId))
            {
                _flowerService.CongTonKho(fId, sl);
                decimal thanhTien = sl * giaVon;
                tongTien += thanhTien;
                Console.WriteLine($"✅ Đã tạo lô {batchId}: {sl} bông {hoa.Name} (Thành tiền: {thanhTien.ToString("C0", _vnCulture)})");
            }
        }

        // Chốt phiếu nhập
        _poService.Update(poId, tongTien, "HOAN_THANH", "Hoàn tất nhập kho");
        Console.WriteLine($"\n🎉 Hoàn thành phiếu nhập {poId}! Tổng tiền nhập: {tongTien.ToString("C0", _vnCulture)}");
    }

    private void XoaPhieu()
    {
        Console.Write("Nhập Mã PN cần xóa: ");
        string poId = Console.ReadLine()?.Trim().ToUpper() ?? "";
        if (_poService.DemLoCuaPhieu(poId) > 0)
        {
            Console.WriteLine("❌ Phiếu này đã phát sinh các lô hoa trong kho, không thể xóa trực tiếp!");
            return;
        }

        if (_poService.Delete(poId))
            Console.WriteLine("✅ Đã xóa phiếu nhập!");
        else
            Console.WriteLine("❌ Xóa thất bại!");
    }

    private void ChotPhieuDoDang()
    {
        var ds = _poService.PhieuChoXuLy();
        if (ds.Count == 0) { Console.WriteLine("Không có phiếu nào đang chờ xử lý."); return; }
        Console.WriteLine("--- DANH SÁCH PHIẾU CHỜ XỬ LÝ ---");
        InBangHelper.InBang(ds, ["Mã PN", "Ngày Nhập", "Nhà Cung Cấp", "Số Lô"]);
        Console.Write("Nhập mã phiếu muốn chốt: ");
        string poId = Console.ReadLine()?.Trim().ToUpper() ?? "";
        if (_poService.ChotPhieu(poId))
            Console.WriteLine($"✅ Đã chốt thành công phiếu {poId} sang HOAN_THANH!");
        else
            Console.WriteLine("❌ Không thể chốt phiếu (có thể phiếu chưa có lô hoa nào hoặc sai mã).");
    }

    private void TongNhapTheoNCCLambda()
    {
        Console.WriteLine("\n--- TỔNG NHẬP THEO TỪNG NHÀ CUNG CẤP (LINQ GROUPBY & SUM) ---");
        var dict = _poService.TongNhapTheoNCCLambda();
        if (dict.Count == 0) { Console.WriteLine("Chưa có dữ liệu nhập hàng."); return; }
        var rows = dict.Select(pair => new object[] { pair.Key, pair.Value }).ToList();
        InBangHelper.InBang(rows, ["Mã NCC", "Tổng Tiền Nhập"]);
    }

    private void PhieuNhapTheoNCCLambda()
    {
        Console.Write("Nhập Mã NCC: ");
        string nccId = Console.ReadLine()?.Trim().ToUpper() ?? "";
        var ds = _poService.PhieuCuaNCCLambda(nccId);
        Console.WriteLine($"\n--- DANH SÁCH PHIẾU NHẬP CỦA NCC {nccId} (LINQ) ---");
        InDsPhieu(ds);
    }

    private void InDsPhieu(List<PurchaseOrder> list)
    {
        if (list.Count == 0) { Console.WriteLine("Không có dữ liệu."); return; }
        var rows = list.Select(po => new object[] {
            po.PurchaseOrderId, po.SupplierId, po.EmployeeId, (object?)po.ImportDate ?? "", po.TotalAmount, po.Status, po.Note ?? ""
        }).ToList();
        InBangHelper.InBang(rows, ["Mã PN", "Mã NCC", "Mã NV", "Ngày Nhập", "Tổng Tiền", "Trạng Thái", "Ghi Chú"]);
    }

    private void InBang(List<object[]> list, string[]? headers = null)
    {
        InBangHelper.InBang(list, headers);
    }
}
