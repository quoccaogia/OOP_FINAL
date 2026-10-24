using System;
using System.Collections.Generic;
using System.Globalization;
using vn.edu.crud.model;
using vn.edu.crud.service;
using vn.edu.crud.util;

namespace vn.edu.crud.menu;

public class FlowerBatchMenu
{
    private readonly FlowerBatchService _batchService = new();
    private readonly FlowerService _flowerService = new();
    private readonly CultureInfo _vnCulture = new("vi-VN");

    public void HienThi()
    {
        int chon;
        do
        {
            Console.WriteLine("\n╔══════════════════════════════════════╗");
            Console.WriteLine("║           QUẢN LÝ LÔ HOA             ║");
            Console.WriteLine("╠══════════════════════════════════════╣");
            Console.WriteLine("║  1. Xem tất cả lô hoa                ║");
            Console.WriteLine("║  2. Cập nhật trạng thái lô           ║");
            Console.WriteLine("║  3. Thanh lý/Hủy lô hoa (Hết hạn)    ║");
            Console.WriteLine("║  6. Đồng bộ tồn kho từ lô hoa        ║");
            Console.WriteLine("║  ── BÁO CÁO ──                       ║");
            Console.WriteLine("║  4. Lô hoa còn hàng (LINQ)           ║");
            Console.WriteLine("║  5. Lô hoa sắp hết hạn (<= 3 ngày)   ║");
            Console.WriteLine("║  7. Lô nhập quá 7 ngày (LINQ)        ║");
            Console.WriteLine("║  0. Quay lại                         ║");
            Console.WriteLine("╚══════════════════════════════════════╝");
            Console.Write("Chọn: ");

            if (!int.TryParse(Console.ReadLine()?.Trim(), out chon)) chon = -1;

            switch (chon)
            {
                case 1: XemTatCa(); break;
                case 2: CapNhatTrangThai(); break;
                case 3: ThanhLyLoHoa(); break;
                case 4: LoConHang(); break;
                case 5: LoSapHetHan(3); break;
                case 6: DongBoTonKho(); break;
                case 7: LoNhapQua7Ngay(); break;
                case 0: break;
                default: Console.WriteLine("Chọn sai!"); break;
            }
        } while (chon != 0);
    }

    private void XemTatCa()
    {
        var list = _batchService.FindAll();
        if (list.Count == 0) { Console.WriteLine("Không có dữ liệu."); return; }
        var rows = list.Select(r => new object[] {
            r[0] ?? "", // Mã Lô
            r[1] ?? "", // Mã PN
            r[4] ?? "", // Tên Hoa
            r[2] ?? "", // Nhà Cung Cấp
            r[5] ?? 0,  // SL Nhập
            r[6] ?? 0,  // SL Còn
            r[7] ?? 0,  // Giá Vốn
            (object?)r[9] ?? "", // Hạn SD
            r[10] ?? "" // Trạng Thái
        }).ToList();
        InBangHelper.InBang(rows, ["Mã Lô", "Mã PN", "Tên Hoa", "Nhà Cung Cấp", "SL Nhập", "SL Còn", "Giá Vốn", "Hạn SD", "Trạng Thái"]);
    }

    private string? ChonLoHoa()
    {
        var list = _batchService.FindAll();
        if (list.Count == 0)
        {
            Console.WriteLine("Chưa có lô hoa nào.");
            return null;
        }

        Console.WriteLine("\nDANH SÁCH LÔ HOA HIỆN CÓ:");
        Console.WriteLine($"{"STT",-4} | {"Mã Lô",-8} | {"Tên Hoa",-22} | {"SL Còn",-8} | {"Hạn SD",-12} | {"Trạng Thái",-12}");
        Console.WriteLine(new string('-', 72));
        for (int i = 0; i < list.Count; i++)
        {
            var r = list[i];
            string mLo = r[0]?.ToString() ?? "";
            string tenHoa = r[4]?.ToString() ?? "";
            if (tenHoa.Length > 20) tenHoa = tenHoa[..17] + "...";
            int slCon = r[6] != null ? Convert.ToInt32(r[6]) : 0;
            string hanSd = r[9] != null ? Convert.ToDateTime(r[9]).ToString("dd/MM/yyyy") : "—";
            string st = r[10]?.ToString() ?? "";
            Console.WriteLine($"{i + 1,-4} | {mLo,-8} | {tenHoa,-22} | {slCon,-8} | {hanSd,-12} | {st,-12}");
        }

        Console.Write("\n👉 Nhập STT lô hoặc Mã Lô (0 để hủy): ");
        string nhap = Console.ReadLine()?.Trim() ?? "";
        if (nhap == "0" || string.IsNullOrEmpty(nhap)) return null;

        if (int.TryParse(nhap, out int stt) && stt >= 1 && stt <= list.Count)
            return list[stt - 1][0]?.ToString();

        return nhap.ToUpper();
    }

    private void CapNhatTrangThai()
    {
        Console.WriteLine("\n--- CẬP NHẬT TRẠNG THÁI LÔ HOA ---");
        string? batchId = ChonLoHoa();
        if (string.IsNullOrEmpty(batchId)) return;

        Console.WriteLine("1. AVAILABLE (Đang bán) | 2. SOLD_OUT (Hết hàng) | 3. EXPIRED (Hỏng/Hết hạn)");
        Console.Write("Chọn: ");
        string moi = Console.ReadLine()?.Trim() switch
        {
            "1" => "AVAILABLE",
            "2" => "SOLD_OUT",
            "3" => "EXPIRED",
            _ => ""
        };

        if (string.IsNullOrEmpty(moi)) { Console.WriteLine("Đã hủy."); return; }

        if (_batchService.DoiTrangThaiLo(batchId, moi))
            Console.WriteLine($"✅ Đã đổi trạng thái lô {batchId} thành {moi} và tự động cân đối tồn kho!");
        else
            Console.WriteLine("❌ Đổi trạng thái thất bại!");
    }

    private void ThanhLyLoHoa()
    {
        Console.WriteLine("\n--- THANH LÝ / HỦY LÔ HOA ---");
        string? batchId = ChonLoHoa();
        if (string.IsNullOrEmpty(batchId)) return;

        Console.Write($"Bạn có chắc chắn muốn thanh lý toàn bộ số hoa còn lại trong lô {batchId}? (y/n): ");
        if (Console.ReadLine()?.Trim().ToLower() == "y")
        {
            if (_batchService.ThanhLyLo(batchId))
                Console.WriteLine($"✅ Đã thanh lý lô {batchId}, chuyển thành EXPIRED và trừ tồn kho tương ứng!");
            else
                Console.WriteLine("❌ Thanh lý thất bại!");
        }
    }

    private void LoConHang()
    {
        Console.WriteLine("\n--- LÔ HOA CÒN HÀNG (LINQ AVAILABLE & QTY > 0) ---");
        var ds = _batchService.LoConHangLambda();
        InDsLo(ds);
    }

    private void LoSapHetHan(int soNgay)
    {
        Console.WriteLine($"\n--- LÔ HOA SẮP HẾT HẠN (<= {soNgay} NGÀY, LINQ ORDERBY EXPIRY) ---");
        var ds = _batchService.LoSapHetHanLambda(soNgay);
        InDsLo(ds);
    }

    private void DongBoTonKho()
    {
        int row = _flowerService.DongBoTonKhoTuLo();
        Console.WriteLine($"✅ Đã đồng bộ tồn kho thành công cho {row} dòng hoa từ các lô AVAILABLE!");
    }

    private void LoNhapQua7Ngay()
    {
        Console.WriteLine("\n--- LÔ HOA NHẬP QUÁ 7 NGÀY (LINQ) ---");
        var ds = _batchService.LoNhapQua7NgayLambda();
        InDsLo(ds);
    }

    private void InDsLo(List<FlowerBatch> list)
    {
        if (list.Count == 0) { Console.WriteLine("Không có dữ liệu."); return; }
        var rows = list.Select(b => new object[] {
            b.BatchId, b.PurchaseOrderId, b.FlowerId, b.QuantityImported, b.QuantityRemaining,
            b.UnitCost, (object?)b.ImportDate ?? "", (object?)b.ExpiryDate ?? "", b.Status
        }).ToList();
        InBangHelper.InBang(rows, ["Mã Lô", "Mã PN", "Mã Hoa", "SL Nhập", "SL Còn", "Giá Vốn", "Ngày Nhập", "Hạn Dùng", "Trạng Thái"]);
    }

    private void InBang(List<object[]> list, string[]? headers = null)
    {
        InBangHelper.InBang(list, headers);
    }
}
