using System;
using System.Collections.Generic;
using vn.edu.crud.model;
using vn.edu.crud.service;
using vn.edu.crud.util;

namespace vn.edu.crud.menu;

public class CareScheduleMenu
{
    private readonly CareScheduleService _careService = new();
    private readonly FlowerService _flowerService = new();
    private readonly FlowerBatchService _batchService = new();
    private readonly EmployeeService _employeeService = new();

    public void HienThi()
    {
        int chon;
        do
        {
            Console.WriteLine("\n╔══════════════════════════════════════╗");
            Console.WriteLine("║        QUẢN LÝ CHĂM SÓC HOA          ║");
            Console.WriteLine("╠══════════════════════════════════════╣");
            Console.WriteLine("║  1. Bảng tình trạng chăm sóc         ║");
            Console.WriteLine("║  2. Cập nhật tình trạng              ║");
            Console.WriteLine("║  3. Sửa ghi chú                      ║");
            Console.WriteLine("║  4. Xóa bản ghi                      ║");
            Console.WriteLine("║  ── TRA CỨU ──                       ║");
            Console.WriteLine("║  5. Lịch sử theo loài hoa (LINQ)     ║");
            Console.WriteLine("║  6. Lịch sử theo nhân viên (LINQ)    ║");
            Console.WriteLine("║  0. Quay lại                         ║");
            Console.WriteLine("╚══════════════════════════════════════╝");
            Console.Write("Chọn: ");

            if (!int.TryParse(Console.ReadLine()?.Trim(), out chon)) chon = -1;

            switch (chon)
            {
                case 1: BangTinhTrang(); break;
                case 2: CapNhatTinhTrang(); break;
                case 3: SuaGhiChu(); break;
                case 4: XoaBanGhi(); break;
                case 5: LichSuTheoHoa(); break;
                case 6: LichSuTheoNhanVien(); break;
                case 0: break;
                default: Console.WriteLine("Chọn sai!"); break;
            }
        } while (chon != 0);
    }

    private void BangTinhTrang()
    {
        var ds = _careService.TrangThaiHienTai();
        if (ds.Count == 0) { Console.WriteLine("Chưa có dữ liệu loài hoa."); return; }
        var rows = ds.Select(r => new object[] {
            r[0]?.ToString() ?? "",
            r[1]?.ToString() ?? "",
            r[3]?.ToString() ?? "0",
            r[4]?.ToString() ?? "—",
            r[5] != null ? Convert.ToDateTime(r[5]).ToString("dd/MM/yyyy HH:mm") : "Chưa chăm",
            r[6]?.ToString() ?? "—",
            r[7]?.ToString() ?? "—",
            r[8]?.ToString() ?? ""
        }).ToList();
        InBangHelper.InBang(rows, ["Mã Hoa", "Tên Hoa", "Tồn", "Mã CS", "Lần Chăm Cuối", "Độ Tươi", "Nước", "Ghi Chú"]);
    }

    private void CapNhatTinhTrang()
    {
        Console.WriteLine("\n--- CẬP NHẬT TÌNH TRẠNG CHĂM SÓC HOA ---");
        var dsHoa = _flowerService.FindAll();
        if (dsHoa.Count == 0)
        {
            Console.WriteLine("❌ Chưa có hoa nào trong hệ thống!");
            return;
        }

        Console.WriteLine("\nDANH SÁCH HOA:");
        for (int i = 0; i < dsHoa.Count; i++)
        {
            Console.WriteLine($"  {i + 1}. {dsHoa[i].Name,-25} (Mã: {dsHoa[i].FlowerId} - Tồn: {dsHoa[i].StockQuantity})");
        }
        Console.Write($"Chọn hoa cần chăm sóc (1-{dsHoa.Count} hoặc Mã Hoa): ");
        string nhapHoa = Console.ReadLine()?.Trim() ?? "";
        Flower? hoa = null;
        if (int.TryParse(nhapHoa, out int idxHoa) && idxHoa >= 1 && idxHoa <= dsHoa.Count)
            hoa = dsHoa[idxHoa - 1];
        else
            hoa = _flowerService.TimTheoMa(nhapHoa.ToUpper());

        if (hoa == null)
        {
            Console.WriteLine("❌ Không tìm thấy hoa!");
            return;
        }
        string flowerId = hoa.FlowerId;

        var dsNV = _employeeService.FindAll();
        if (dsNV.Count == 0)
        {
            Console.WriteLine("❌ Chưa có nhân viên nào trong hệ thống!");
            return;
        }
        Console.WriteLine("\nDANH SÁCH NHÂN VIÊN:");
        for (int i = 0; i < dsNV.Count; i++)
        {
            Console.WriteLine($"  {i + 1}. {dsNV[i].Name,-20} (Mã: {dsNV[i].EmployeeId})");
        }
        Console.Write($"Chọn nhân viên thực hiện (1-{dsNV.Count} hoặc Mã NV): ");
        string nhapNV = Console.ReadLine()?.Trim() ?? "";
        string empId = "";
        if (int.TryParse(nhapNV, out int idxNV) && idxNV >= 1 && idxNV <= dsNV.Count)
            empId = dsNV[idxNV - 1].EmployeeId;
        else if (_employeeService.FindById(nhapNV.ToUpper()) != null)
            empId = nhapNV.ToUpper();
        else
        {
            Console.WriteLine("❌ Không tìm thấy nhân viên!");
            return;
        }

        Console.WriteLine("Độ tươi: 1. TƯƠI | 2. HƠI HÉO | 3. HÉO | 4. HỎNG");
        Console.Write("Chọn: ");
        string fresh = Console.ReadLine()?.Trim() switch
        {
            "2" => "HƠI HÉO",
            "3" => "HÉO",
            "4" => "HỎNG",
            _ => "TƯƠI"
        };

        Console.WriteLine("Tình trạng nước: 1. ĐỦ | 2. THIẾU");
        Console.Write("Chọn: ");
        string water = Console.ReadLine()?.Trim() switch
        {
            "2" => "THIẾU",
            _ => "ĐỦ"
        };

        Console.Write("Hành động thực hiện (VD: Tưới nước, Thay nước, Tỉa lá): ");
        string action = Console.ReadLine()?.Trim() ?? "Tưới nước";
        Console.Write("Ghi chú bổ sung: ");
        string note = Console.ReadLine()?.Trim() ?? "";

        string csId = _careService.SinhMaCSMoi();
        if (_careService.Insert(csId, flowerId, empId, DateTime.Now, fresh, water, action, note))
        {
            Console.WriteLine($"✅ Đã ghi nhận lịch chăm sóc {csId} cho hoa {hoa.Name}!");
        }
    }

    private void SuaGhiChu()
    {
        Console.Write("Nhập Mã CS cần sửa ghi chú: ");
        string csId = Console.ReadLine()?.Trim().ToUpper() ?? "";
        Console.Write("Ghi chú mới: ");
        string note = Console.ReadLine()?.Trim() ?? "";

        if (_careService.CapNhatGhiChu(csId, note))
            Console.WriteLine("✅ Cập nhật ghi chú thành công!");
        else
            Console.WriteLine("❌ Không tìm thấy mã hoặc sửa thất bại!");
    }

    private void XoaBanGhi()
    {
        Console.Write("Nhập Mã CS cần xóa: ");
        string csId = Console.ReadLine()?.Trim().ToUpper() ?? "";
        if (_careService.Delete(csId))
            Console.WriteLine("✅ Đã xóa bản ghi chăm sóc!");
        else
            Console.WriteLine("❌ Xóa thất bại!");
    }

    private void LichSuTheoHoa()
    {
        Console.Write("Nhập Mã Hoa: ");
        string flowerId = Console.ReadLine()?.Trim().ToUpper() ?? "";
        var ds = _careService.LichSuTheoHoaLambda(flowerId);
        Console.WriteLine($"\n--- LỊCH SỬ CHĂM SÓC HOA {flowerId} (LINQ) ---");
        if (ds.Count == 0) { Console.WriteLine("Chưa có lịch sử chăm sóc cho hoa này."); return; }
        var rows = ds.Select(c => new object[] {
            c.CareId, c.EmployeeId, (object?)c.CheckDate ?? "", c.FreshnessLevel, c.WaterStatus, c.ActionTaken ?? "", c.Note ?? ""
        }).ToList();
        InBangHelper.InBang(rows, ["Mã CS", "Mã NV", "Thời Gian", "Độ Tươi", "Nước", "Hành Động", "Ghi Chú"]);
    }

    private void LichSuTheoNhanVien()
    {
        Console.Write("Nhập Mã Nhân Viên: ");
        string empId = Console.ReadLine()?.Trim().ToUpper() ?? "";
        var ds = _careService.LichSuTheoNhanVienLambda(empId);
        Console.WriteLine($"\n--- LỊCH SỬ CHĂM SÓC CỦA NHÂN VIÊN {empId} (LINQ) ---");
        if (ds.Count == 0) { Console.WriteLine("Nhân viên này chưa thực hiện chăm sóc hoa nào."); return; }
        var rows = ds.Select(c => new object[] {
            c.CareId, c.FlowerId, (object?)c.CheckDate ?? "", c.FreshnessLevel, c.WaterStatus, c.ActionTaken ?? "", c.Note ?? ""
        }).ToList();
        InBangHelper.InBang(rows, ["Mã CS", "Mã Hoa", "Thời Gian", "Độ Tươi", "Nước", "Hành Động", "Ghi Chú"]);
    }
}
