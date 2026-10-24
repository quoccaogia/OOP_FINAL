using System;
using System.Collections.Generic;
using vn.edu.crud.model;
using vn.edu.crud.service;
using vn.edu.crud.util;

namespace vn.edu.crud.menu;

public class CustomerMenu
{
    private readonly CustomerService _customerService = new();
    private readonly OrderService _orderService = new();

    public void HienThi()
    {
        int chon;
        do
        {
            Console.WriteLine("\n╔══════════════════════════════════════╗");
            Console.WriteLine("║          QUẢN LÝ KHÁCH HÀNG          ║");
            Console.WriteLine("╠══════════════════════════════════════╣");
            Console.WriteLine("║  1. Xem tất cả danh sách             ║");
            Console.WriteLine("║  2. Nghiệp vụ (Tiếp đón khách)       ║");
            Console.WriteLine("║  3. Tìm kiếm khách hàng (LINQ)       ║");
            Console.WriteLine("║  ── BÁO CÁO ──                       ║");
            Console.WriteLine("║  6. Khách đã từng đặt hàng           ║");
            Console.WriteLine("║  7. Khách chưa từng đặt hàng         ║");
            Console.WriteLine("║  8. Khách nhiều đơn nhất             ║");
            Console.WriteLine("║  9. Tổng chi tiêu theo khách         ║");
            Console.WriteLine("║ 10. Đơn hàng của 1 khách (Nhập SĐT)  ║");
            Console.WriteLine("║  0. Quay lại                         ║");
            Console.WriteLine("╚══════════════════════════════════════╝");
            Console.Write("Chọn: ");

            if (!int.TryParse(Console.ReadLine()?.Trim(), out chon)) chon = -1;

            switch (chon)
            {
                case 1: InDanhSach(_customerService.FindAll()); break;
                case 2: TiepDonKhachHang(); break;
                case 3: TimKiemKhachHangLambda(); break;
                case 6: InBangHelper.InBang(_orderService.KhachDaTungDatHang(), ["Mã KH", "Tên KH", "SĐT", "Địa Chỉ", "Email"]); break;
                case 7: InBangHelper.InBang(_orderService.KhachChuaTungDatHang(), ["Mã KH", "Tên KH", "SĐT", "Địa Chỉ", "Email"]); break;
                case 8: InBangHelper.InBang(_orderService.KhachNhieuDonNhat(), ["Mã KH", "Tên KH", "Số Lượng Đơn"]); break;
                case 9: InBangHelper.InBang(_orderService.TongChiTieuTheoKhach(), ["Mã KH", "Tên KH", "Tổng Chi Tiêu"]); break;
                case 10: XemDonHangBangSdt(); break;
                case 0: break;
                default: Console.WriteLine("Chọn sai!"); break;
            }
        } while (chon != 0);
    }

    private void TiepDonKhachHang()
    {
        Console.WriteLine("\n--- TIẾP ĐÓN KHÁCH HÀNG ---");
        Console.Write("Nhập SĐT khách hàng (VD: 0901234567, có thể nhập một phần): ");
        string sdt = NhapLieu.ChuanHoaSdt(Console.ReadLine());

        var ketQua = _customerService.TimTheoSdt(sdt);

        if (ketQua.Count == 0)
        {
            Console.WriteLine("❌ Không tìm thấy khách hàng nào dùng SĐT này.");
            Console.Write("👉 Tạo tài khoản khách hàng mới? (y/n): ");
            if (Console.ReadLine()?.Trim().ToLower() == "y")
            {
                ThemKhachMoi(sdt);
            }
        }
        else
        {
            Console.WriteLine("✅ Tìm thấy khách hàng:");
            InDanhSach(ketQua);

            var kh = ketQua[0];
            Console.Write("👉 Bạn có muốn cập nhật (sửa) thông tin khách hàng này không? (y/n): ");
            if (Console.ReadLine()?.Trim().ToLower() == "y")
            {
                SuaKhach(kh);
            }
        }
    }

    private void XemDonHangBangSdt()
    {
        Console.WriteLine("\n--- XEM ĐƠN HÀNG THEO SĐT ---");
        Console.Write("Nhập SĐT khách hàng (VD: 0901234567, có thể nhập một phần): ");
        string sdt = NhapLieu.ChuanHoaSdt(Console.ReadLine());

        var ds = _customerService.TimTheoSdt(sdt);
        if (ds.Count == 0)
        {
            Console.WriteLine("❌ Không tìm thấy khách hàng nào dùng SĐT này.");
            return;
        }

        var khachChon = ds[0];
        Console.WriteLine($"✅ Khách hàng: {khachChon.Name} (Mã: {khachChon.CustomerId})");
        Console.WriteLine("--- DANH SÁCH ĐƠN HÀNG ---");

        var donHang = _orderService.DonCuaKhach(khachChon.CustomerId);
        if (donHang.Count == 0)
        {
            Console.WriteLine("Khách hàng này chưa có đơn hàng nào.");
        }
        else
        {
            var rows = donHang.Select(o => new object[] { o.OrderId, o.OrderDate, o.OrderStatus }).ToList();
            InBangHelper.InBang(rows, ["Mã Đơn", "Ngày Đặt", "Trạng Thái"]);
        }
    }

    private void ThemKhachMoi(string sdtDaNhap)
    {
        string id = _customerService.SinhMaKhachMoi();
        Console.WriteLine($"Mã KH tự sinh: {id}");

        string sdt = sdtDaNhap;
        if (string.IsNullOrEmpty(sdt) || !NhapLieu.LaSdtKhach(sdt))
        {
            sdt = NhapLieu.SdtKhach("SĐT khách hàng", true);
        }
        string ten = NhapLieu.BatBuoc("Tên KH", "VD: Nguyễn Văn A");
        string diaChi = NhapLieu.TuyChon("Địa chỉ", "VD: Quận 1, TP.HCM");
        string email = NhapLieu.Email("Email", false);

        var kh = new Customer(id, ten, sdt, diaChi, email);
        if (_customerService.Insert(kh))
            Console.WriteLine("✅ Thêm khách hàng thành công!");
    }

    private void SuaKhach(Customer kh)
    {
        Console.WriteLine("\n--- SỬA THÔNG TIN KHÁCH HÀNG ---");
        Console.Write("Tên mới (Enter = giữ nguyên, VD: Nguyễn Văn A): ");
        string ten = Console.ReadLine()?.Trim() ?? "";
        if (!string.IsNullOrEmpty(ten)) kh.Name = ten;

        Console.Write("Địa chỉ mới (Enter = giữ nguyên, VD: Quận 1, TP.HCM): ");
        string diaChi = Console.ReadLine()?.Trim() ?? "";
        if (!string.IsNullOrEmpty(diaChi)) kh.Address = diaChi;

        while (true)
        {
            Console.Write("Email mới (Enter = giữ nguyên, VD: ten@gmail.com): ");
            string email = Console.ReadLine()?.Trim() ?? "";
            if (string.IsNullOrEmpty(email)) break;
            if (NhapLieu.LaEmail(email))
            {
                kh.Email = email;
                break;
            }
            Console.WriteLine("⚠️ Email không hợp lệ, phải có dạng ten@domain.com.");
        }

        if (_customerService.Update(kh))
            Console.WriteLine("✅ Sửa thành công!");
        else
            Console.WriteLine("❌ Sửa thất bại!");
    }

    private void TimKiemKhachHangLambda()
    {
        Console.WriteLine("\n--- TÌM KIẾM KHÁCH HÀNG BẰNG LINQ & LAMBDA ---");
        Console.Write("Nhập từ khóa cần tìm (Tên hoặc Số điện thoại): ");
        string tuKhoa = Console.ReadLine()?.Trim() ?? "";

        var ketQua = _customerService.FilterCustomersByLambda(tuKhoa);

        if (ketQua.Count == 0)
        {
            Console.WriteLine($"❌ Không tìm thấy khách hàng nào khớp với từ khóa: \"{tuKhoa}\"");
        }
        else
        {
            Console.WriteLine($"✅ Tìm thấy {ketQua.Count} kết quả khớp:");
            InDanhSach(ketQua);
        }
    }

    private void InDanhSach(List<Customer> list)
    {
        if (list.Count == 0) { Console.WriteLine("Không có dữ liệu."); return; }
        var rows = list.Select(c => new object[] {
            c.CustomerId, c.Name, c.Phone, c.Address ?? "", c.Email ?? ""
        }).ToList();
        InBangHelper.InBang(rows, ["Mã KH", "Tên KH", "SĐT", "Địa Chỉ", "Email"]);
    }

    private void InBang(List<object[]> list, string[]? headers = null)
    {
        InBangHelper.InBang(list, headers);
    }
}
