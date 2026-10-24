using System;
using System.Collections.Generic;
using System.Globalization;
using vn.edu.crud.model;
using vn.edu.crud.service;
using vn.edu.crud.util;

namespace vn.edu.crud.menu;

public class PaymentMenu
{
    private readonly PaymentService _paymentService = new();
    private readonly OrderService _orderService = new();
    private readonly CultureInfo _vnCulture = new("vi-VN");

    public void HienThi()
    {
        int chon = -1;
        do
        {
            try
            {
                Console.WriteLine("\n╔══════════════════════════════════════╗");
                Console.WriteLine("║       QUẢN LÝ THANH TOÁN             ║");
                Console.WriteLine("╠══════════════════════════════════════╣");
                Console.WriteLine("║  1. Xem tất cả                       ║");
                Console.WriteLine("║  2. Theo phương thức (LINQ)          ║");
                Console.WriteLine("║  3. Doanh thu hôm nay (LINQ)         ║");
                Console.WriteLine("║  4. Hủy giao dịch / Hoàn tiền        ║");
                Console.WriteLine("║  5. Đơn chưa thanh toán đủ           ║");
                Console.WriteLine("║  0. Quay lại                         ║");
                Console.WriteLine("╚══════════════════════════════════════╝");
                Console.Write("Chọn: ");

                if (!int.TryParse(Console.ReadLine()?.Trim(), out chon)) chon = -1;

                switch (chon)
                {
                    case 1: XemTatCa(); break;
                    case 2: LocTheoPhuongThuc(); break;
                    case 3: DoanhThuHomNay(); break;
                    case 4: HuyHoanGiaoDich(); break;
                    case 5: DonChuaThanhToanDu(); break;
                    case 0: Console.WriteLine("Quay lại menu chính."); break;
                    default: Console.WriteLine("⚠️ Chọn sai! Vui lòng chọn số từ 0 đến 5."); break;
                }
            }
            catch (Exception ex)
            {
                AnToan.BaoLoi(ex, "số trong menu 0-5.");
                chon = -1;
            }
        } while (chon != 0);
    }

    private void XemTatCa()
    {
        var ds = _paymentService.LayDanhSachThanhToan();
        if (ds.Count == 0) { Console.WriteLine("Chưa có giao dịch thanh toán nào."); return; }
        var rows = ds.Select(p => new object[] {
            p.PaymentId, p.OrderId, p.Amount, p.PaymentMethod, (object?)p.PaymentDate ?? "", p.PaymentStatus
        }).ToList();
        InBangHelper.InBang(rows, ["Mã GD", "Mã Đơn", "Số Tiền", "Phương Thức", "Thời Gian", "Trạng Thái"]);
    }

    private void LocTheoPhuongThuc()
    {
        Console.WriteLine("\n--- LỌC THANH TOÁN THEO PHƯƠNG THỨC (LINQ) ---");
        Console.WriteLine("1. TIEN_MAT | 2. CHUYEN_KHOAN | 3. THE");
        Console.Write("Chọn phương thức: ");
        string pt = Console.ReadLine()?.Trim() switch
        {
            "1" => "TIEN_MAT",
            "2" => "CHUYEN_KHOAN",
            "3" => "THE",
            var s => s ?? ""
        };

        var ds = _paymentService.TheoPhuongThucLambda(pt);
        Console.WriteLine($"✅ Tìm thấy {ds.Count} giao dịch qua phương thức {pt}:");
        if (ds.Count > 0)
        {
            var rows = ds.Select(p => new object[] {
                p.PaymentId, p.OrderId, p.Amount, (object?)p.PaymentDate ?? "", p.PaymentStatus
            }).ToList();
            InBangHelper.InBang(rows, ["Mã GD", "Mã Đơn", "Số Tiền", "Thời Gian", "Trạng Thái"]);
        }
    }

    private void DoanhThuHomNay()
    {
        decimal dt = _paymentService.DoanhThuHomNayLambda();
        Console.WriteLine($"\n💰 TỔNG DOANH THU HÔM NAY (LINQ): {dt.ToString("C0", _vnCulture)}");
    }

    private void HuyHoanGiaoDich()
    {
        Console.Write("Nhập Mã Thanh Toán (VD: P001): ");
        string id = Console.ReadLine()?.Trim().ToUpper() ?? "";
        string? st = _paymentService.LayTrangThaiThanhToan(id);
        if (st == null)
        {
            Console.WriteLine($"❌ Không tìm thấy giao dịch {id}!");
            return;
        }

        Console.WriteLine($"Trạng thái hiện tại của GD {id}: {st}");
        Console.Write("Thao tác: 1. Hủy giao dịch (DA_HUY) | 2. Hoàn tiền khách (DA_HOAN_TIEN): ");
        string c = Console.ReadLine()?.Trim() ?? "";
        if (c == "1")
        {
            if (_paymentService.XoaThanhToan(id))
                Console.WriteLine("✅ Đã chuyển trạng thái giao dịch thành DA_HUY!");
        }
        else if (c == "2")
        {
            if (_paymentService.HoanTien(id))
                Console.WriteLine("✅ Đã hoàn tiền và chuyển trạng thái thành DA_HOAN_TIEN!");
        }
    }

    private void DonChuaThanhToanDu()
    {
        var ds = _paymentService.LayDanhSachDonChuaThanhToan();
        if (ds.Count == 0) { Console.WriteLine("Tất cả đơn hàng đã được thanh toán đủ!"); return; }
        var rows = ds.Select(r => {
            decimal total = r[3] == null ? 0 : Convert.ToDecimal(r[3]);
            decimal paid = r[4] == null ? 0 : Convert.ToDecimal(r[4]);
            decimal debt = total - paid;
            return new object[] { r[0] ?? "", r[1] ?? "", r[2] ?? "", total, paid, debt };
        }).ToList();
        InBangHelper.InBang(rows, ["Mã Đơn", "Trạng Thái", "Khách Hàng", "Tổng Tiền", "Đã Trả", "Còn Nợ"]);
    }
}
