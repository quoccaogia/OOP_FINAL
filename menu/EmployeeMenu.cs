using System;
using System.Collections.Generic;
using System.Globalization;
using vn.edu.crud.model;
using vn.edu.crud.service;
using vn.edu.crud.util;

namespace vn.edu.crud.menu;

public class EmployeeMenu
{
    private readonly EmployeeService _employeeService = new();
    private readonly CultureInfo _vnCulture = new("vi-VN");

    public void HienThi()
    {
        int chon;
        do
        {
            Console.WriteLine("\n╔══════════════════════════════════════╗");
            Console.WriteLine("║          QUẢN LÝ NHÂN VIÊN           ║");
            Console.WriteLine("╠══════════════════════════════════════╣");
            Console.WriteLine("║  1. Xem tất cả                       ║");
            Console.WriteLine("║  2. Tìm kiếm nhân viên (LINQ)        ║");
            Console.WriteLine("║  3. Tìm theo mã                      ║");
            Console.WriteLine("║  4. Thêm mới                         ║");
            Console.WriteLine("║  5. Sửa (Nhập Mã NV)                 ║");
            Console.WriteLine("║  6. Xóa (Nhập Mã NV)                 ║");
            Console.WriteLine("║  ── BÁO CÁO ──                       ║");
            Console.WriteLine("║  7. NV đang phụ trách đơn            ║");
            Console.WriteLine("║  8. NV nhiều đơn nhất                ║");
            Console.WriteLine("║  9. Tính lương tháng + Hoa hồng      ║");
            Console.WriteLine("║  0. Quay lại                         ║");
            Console.WriteLine("╚══════════════════════════════════════╝");
            Console.Write("Chọn: ");

            if (!int.TryParse(Console.ReadLine()?.Trim(), out chon)) chon = -1;

            switch (chon)
            {
                case 1: InDanhSach(_employeeService.FindAll()); break;
                case 2: TimKiemNhanVienLambda(); break;
                case 3: TimTheoMa(); break;
                case 4: ThemNhanVien(); break;
                case 5: SuaNhanVien(); break;
                case 6: XoaNhanVien(); break;
                case 7: InBangHelper.InBang(_employeeService.NhanVienCoDonHang(), ["Mã NV", "Tên NV", "Chức Vụ", "SĐT", "Lương"]); break;
                case 8: InBangHelper.InBang(_employeeService.NhanVienNhieuDonNhat(), ["Mã NV", "Tên NV", "Số Lượng Đơn"]); break;
                case 9: TinhLuongNhanVien(); break;
                case 0: break;
                default: Console.WriteLine("Chọn sai!"); break;
            }
        } while (chon != 0);
    }

    private void TimKiemNhanVienLambda()
    {
        Console.WriteLine("\n--- TÌM KIẾM NHÂN VIÊN (LINQ & LAMBDA) ---");
        Console.Write("Nhập mã hoặc tên nhân viên cần tìm: ");
        string tuKhoa = Console.ReadLine()?.Trim() ?? "";

        var ketQua = _employeeService.SearchEmployeesByLambda(tuKhoa);

        if (ketQua.Count == 0)
        {
            Console.WriteLine($"❌ Không tìm thấy nhân viên nào khớp với từ khóa: \"{tuKhoa}\"");
        }
        else
        {
            Console.WriteLine($"✅ Tìm thấy {ketQua.Count} nhân viên phù hợp:");
            InDanhSach(ketQua);
        }
    }

    private void ThemNhanVien()
    {
        string id = _employeeService.SinhMaNVMoi();
        Console.WriteLine($"Mã NV tự sinh: {id}");

        string ten = NhapLieu.BatBuoc("Tên NV", "VD: Nguyễn Văn A");

        var dsRole = EmployeeRole.AllRoles;
        Console.WriteLine("\n--- CHỌN CHỨC VỤ ---");
        for (int i = 0; i < dsRole.Length; i++)
        {
            Console.WriteLine($"  {i + 1}. {dsRole[i].Label,-15} (Lương tháng: {dsRole[i].LuongCoBan.ToString("C0", _vnCulture)})");
        }

        EmployeeRole? roleChon = null;
        while (roleChon == null)
        {
            Console.Write($"Nhập số chọn (1-{dsRole.Length}): ");
            if (int.TryParse(Console.ReadLine()?.Trim(), out int chonRole) && chonRole >= 1 && chonRole <= dsRole.Length)
            {
                roleChon = dsRole[chonRole - 1];
            }
            else
            {
                Console.WriteLine("Vui lòng chọn đúng số!");
            }
        }

        string sdt = NhapLieu.TuyChon("SĐT", "VD: 0901234567");

        decimal luong = roleChon.LuongCoBan;
        while (true)
        {
            Console.Write($"Lương tháng (Enter = mặc định {roleChon.LuongCoBan.ToString("C0", _vnCulture)}, VD: 6000000): ");
            string s = Console.ReadLine()?.Trim() ?? "";
            if (string.IsNullOrEmpty(s)) break;
            if (decimal.TryParse(s, out decimal v) && v > 0)
            {
                luong = v;
                break;
            }
            Console.WriteLine("⚠️ Lương phải là số lớn hơn 0.");
        }

        var nv = new Employee(id, ten, roleChon.Key, sdt, luong);
        if (_employeeService.Insert(nv))
            Console.WriteLine("✅ Thêm nhân viên thành công!");
        else
            Console.WriteLine("❌ Thêm thất bại!");
    }

    private void TinhLuongNhanVien()
    {
        Console.Write("Nhập Mã NV cần tính lương (VD: NV02): ");
        string id = Console.ReadLine()?.Trim().ToUpper() ?? "";

        var nv = _employeeService.FindById(id);
        if (nv == null)
        {
            Console.WriteLine($"❌ Không tìm thấy nhân viên có mã: {id}");
            return;
        }

        decimal luongCung = nv.Salary;
        decimal doanhThu = _employeeService.TinhDoanhThuNhanVien(id);
        decimal tienHoaHong = doanhThu * 0.02m;
        decimal tongLinh = luongCung + tienHoaHong;

        Console.WriteLine("\n--- BẢNG TÍNH LƯƠNG THÁNG ---");
        Console.WriteLine($"Mã NV      : {nv.EmployeeId}");
        Console.WriteLine($"Tên NV     : {nv.Name}");
        Console.WriteLine($"Chức vụ    : {EmployeeRole.GetLabelFromString(nv.Role)}");
        Console.WriteLine("--------------------------------------");
        Console.WriteLine($"Lương cứng : {luongCung.ToString("C0", _vnCulture)}");
        Console.WriteLine($"Doanh thu  : {doanhThu.ToString("C0", _vnCulture)}");
        Console.WriteLine($"Hoa hồng   : {tienHoaHong.ToString("C0", _vnCulture)} (2% doanh thu)");
        Console.WriteLine("--------------------------------------");
        Console.WriteLine($"TỔNG LĨNH  : {tongLinh.ToString("C0", _vnCulture)}");
    }

    private void TimTheoMa()
    {
        Console.Write("Nhập Mã NV cần tìm (VD: NV01): ");
        string id = Console.ReadLine()?.Trim().ToUpper() ?? "";
        var nv = _employeeService.FindById(id);
        if (nv == null)
        {
            Console.WriteLine($"❌ Không tìm thấy nhân viên có mã: {id}");
        }
        else
        {
            Console.WriteLine("✅ Thông tin nhân viên:");
            string roleHienThi = EmployeeRole.GetLabelFromString(nv.Role);
            var rows = new List<object[]> {
                new object[] { nv.EmployeeId, nv.Name, roleHienThi, nv.Phone, nv.Salary }
            };
            InBangHelper.InBang(rows, ["Mã NV", "Tên NV", "Chức Vụ", "SĐT", "Lương"]);
        }
    }

    private void SuaNhanVien()
    {
        Console.Write("Nhập Mã NV cần sửa (VD: NV01): ");
        string id = Console.ReadLine()?.Trim().ToUpper() ?? "";
        var nv = _employeeService.FindById(id);
        if (nv == null) { Console.WriteLine("❌ Không tìm thấy!"); return; }

        Console.WriteLine("\n--- THÔNG TIN HIỆN TẠI ---");
        Console.WriteLine($"Tên: {nv.Name} | Chức vụ: {EmployeeRole.GetLabelFromString(nv.Role)} | Lương: {nv.Salary.ToString("C0", _vnCulture)}");

        Console.Write("Tên mới (Enter = giữ nguyên, VD: Nguyễn Văn A): ");
        string ten = Console.ReadLine()?.Trim() ?? "";
        if (!string.IsNullOrEmpty(ten)) nv.Name = ten;

        Console.Write("Bạn có muốn đổi chức vụ không? (y/n): ");
        if (Console.ReadLine()?.Trim().ToLower() == "y")
        {
            var dsRole = EmployeeRole.AllRoles;
            for (int i = 0; i < dsRole.Length; i++)
            {
                Console.WriteLine($"  {i + 1}. {dsRole[i].Label}");
            }
            Console.Write("Chọn số: ");
            if (int.TryParse(Console.ReadLine()?.Trim(), out int rChon) && rChon >= 1 && rChon <= dsRole.Length)
            {
                nv.Role = dsRole[rChon - 1].Key;
            }
        }

        Console.Write("SĐT mới (Enter = giữ nguyên): ");
        string sdt = Console.ReadLine()?.Trim() ?? "";
        if (!string.IsNullOrEmpty(sdt)) nv.Phone = sdt;

        Console.Write("Lương mới (Enter = giữ nguyên): ");
        string luongStr = Console.ReadLine()?.Trim() ?? "";
        if (!string.IsNullOrEmpty(luongStr) && decimal.TryParse(luongStr, out decimal luong) && luong > 0)
        {
            nv.Salary = luong;
        }

        if (_employeeService.Update(nv))
            Console.WriteLine("✅ Sửa thành công!");
        else
            Console.WriteLine("❌ Sửa thất bại!");
    }

    private void XoaNhanVien()
    {
        Console.Write("Nhập Mã NV cần xóa (VD: NV01): ");
        string id = Console.ReadLine()?.Trim().ToUpper() ?? "";

        var nv = _employeeService.FindById(id);
        if (nv == null) { Console.WriteLine("❌ Không tìm thấy!"); return; }

        if (_employeeService.KiemTraNhanVienCoDon(id))
        {
            Console.WriteLine("\n❌ KHÔNG THỂ XÓA! Nhân viên này đã phụ trách đơn hàng trong hệ thống.");
            return;
        }

        Console.Write($"Bạn có chắc muốn xóa nhân viên {nv.Name} ({id})? (y/n): ");
        if (Console.ReadLine()?.Trim().ToLower() == "y")
        {
            if (_employeeService.Delete(id))
                Console.WriteLine("✅ Đã xóa!");
            else
                Console.WriteLine("❌ Xóa thất bại!");
        }
    }

    private void InDanhSach(List<Employee> list)
    {
        if (list.Count == 0) { Console.WriteLine("Không có dữ liệu."); return; }
        var rows = list.Select(nv => new object[] {
            nv.EmployeeId, nv.Name, EmployeeRole.GetLabelFromString(nv.Role), nv.Phone, nv.Salary
        }).ToList();
        InBangHelper.InBang(rows, ["Mã NV", "Tên NV", "Chức Vụ", "SĐT", "Lương"]);
    }

    private void InBang(List<object[]> list, string[]? headers = null)
    {
        InBangHelper.InBang(list, headers);
    }
}
