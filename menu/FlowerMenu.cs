using System;
using System.Collections.Generic;
using System.Globalization;
using vn.edu.crud.model;
using vn.edu.crud.service;
using vn.edu.crud.util;

namespace vn.edu.crud.menu;

public class FlowerMenu
{
    private readonly FlowerService _flowerService = new();
    private readonly FlowerBatchService _batchService = new();
    private readonly SupplierService _supplierService = new();
    private readonly CultureInfo _vnCulture = new("vi-VN");

    public void HienThi()
    {
        int chon;
        do
        {
            Console.WriteLine("\n╔══════════════════════════════════════╗");
            Console.WriteLine("║          QUẢN LÝ HOA                 ║");
            Console.WriteLine("╠══════════════════════════════════════╣");
            Console.WriteLine("║  ── THAO TÁC ──                      ║");
            Console.WriteLine("║  1. Xem tất cả                       ║");
            Console.WriteLine("║  2. Tìm theo mã                      ║");
            Console.WriteLine("║  3. Thêm mới                         ║");
            Console.WriteLine("║  4. Sửa                              ║");
            Console.WriteLine("║  5. Xóa                              ║");
            Console.WriteLine("║  ── BÁO CÁO ──                       ║");
            Console.WriteLine("║  6. Hoa tồn < 10 (LINQ)              ║");
            Console.WriteLine("║  7. Tìm hoa theo tên / danh mục      ║");
            Console.WriteLine("║  8. Hoa theo khoảng giá (LINQ)       ║");
            Console.WriteLine("║  9. Hoa theo màu                     ║");
            Console.WriteLine("║ 10. Hoa bán chạy nhất                ║");
            Console.WriteLine("║ 11. Hoa bán chậm nhất                ║");
            Console.WriteLine("║ 12. Hoa giá giảm dần (LINQ)          ║");
            Console.WriteLine("║ 13. Tổng bán theo từng loại          ║");
            Console.WriteLine("║ 14. Hoa chưa từng bán                ║");
            Console.WriteLine("║ 15. Lô hoa tồn > 7 ngày              ║");
            Console.WriteLine("║  0. Quay lại                         ║");
            Console.WriteLine("╚══════════════════════════════════════╝");
            Console.Write("Chọn: ");

            if (!int.TryParse(Console.ReadLine()?.Trim(), out chon)) chon = -1;

            try
            {
                XuLyChon(chon);
            }
            catch (Exception ex)
            {
                AnToan.BaoLoi(ex, "số trong menu 0-15. VD: 1 = xem danh sách, 3 = thêm hoa");
            }
        } while (chon != 0);
    }

    private void XuLyChon(int chon)
    {
        switch (chon)
        {
            case 1:
                var tatCa = _flowerService.FindAll();
                InDsHoa(tatCa);
                int tongTon = _flowerService.TinhTongTonKhoLambda(tatCa);
                Console.WriteLine($"👉 Tổng cộng: {tatCa.Count} loài hoa trong hệ thống — tổng tồn kho: {tongTon} bông.");
                break;
            case 2:
                Console.Write("Mã hoa (VD: FL01): ");
                string maHoa = Console.ReadLine()?.Trim().ToUpper() ?? "";
                var f = _flowerService.TimTheoMa(maHoa);
                if (f != null)
                {
                    Console.WriteLine("✅ Thông tin hoa:");
                    InDsHoa([f]);
                }
                else
                {
                    Console.WriteLine($"❌ Không tìm thấy hoa có mã: {maHoa}");
                }
                break;
            case 3: ThemHoa(); break;
            case 4: SuaHoa(); break;
            case 5: XoaHoa(); break;
            case 6: InDsHoa(_flowerService.HoaSapHetLambda(10)); break;
            case 7:
                Console.Write("Từ khóa tìm kiếm (Tên hoặc Danh mục): ");
                InDsHoa(_flowerService.SearchFlowersByLambda(Console.ReadLine() ?? ""));
                break;
            case 8:
                decimal min = NhapLieu.Tien("Giá min", "VD: 5000", true);
                decimal max = NhapLieu.Tien("Giá max", "VD: 150000", true);
                if (min > max) (min, max) = (max, min);
                InDsHoa(_flowerService.TimTheoKhoangGiaLambda(min, max));
                break;
            case 9:
                Console.Write("Màu (VD: Đỏ): ");
                InDsHoa(_flowerService.TimTheoMau(Console.ReadLine()?.Trim() ?? ""));
                break;
            case 10: InBangHelper.InBang(_flowerService.HoaBanChay(), ["Mã Hoa", "Tên Hoa", "Tổng Đã Bán"]); break;
            case 11: InBangHelper.InBang(_flowerService.HoaBanCham(), ["Mã Hoa", "Tên Hoa", "Tổng Đã Bán"]); break;
            case 12: InDsHoa(_flowerService.HoaGiaGiamDanLambda()); break;
            case 13: InBangHelper.InBang(_flowerService.TongBanTheoHoa(), ["Mã Hoa", "Tên Hoa", "Tổng Đã Bán"]); break;
            case 14: InDsHoa(_flowerService.HoaChuaBanDuoc()); break;
            case 15: InBangHelper.InBang(_flowerService.LoHoaTonLau(7), ["Mã Hoa", "Tên Hoa", "Mã Lô", "Ngày Nhập", "Số Ngày Tồn"]); break;
            case 0: break;
            default: Console.WriteLine("Chọn sai! Nhập số từ 0 đến 15."); break;
        }
    }

    private void ThemHoa()
    {
        try
        {
            string id = _flowerService.SinhMaHoaMoi();
            Console.WriteLine($"Mã tự sinh: {id}");
            string name = NhapLieu.BatBuoc("Tên hoa", "VD: Hồng Đỏ Đà Lạt");
            string cat = NhapLieu.BatBuoc("Danh mục", "VD: Hồng");
            decimal price = NhapLieu.Tien("Đơn giá bán", "VD: 5000", false);
            int stock = NhapLieu.SoNguyen("Tồn kho ban đầu", "VD: 100", 0, 1_000_000);
            string color = NhapLieu.BatBuoc("Màu sắc", "VD: Đỏ");
            string supId = NhapMaNCC(null);

            Console.WriteLine(_flowerService.Insert(id, name, cat, price, stock, color, supId)
                ? "✅ Thêm thành công!"
                : "❌ Thêm thất bại (có thể trùng mã hoa hoặc mã NCC không hợp lệ).");
        }
        catch (Exception ex)
        {
            AnToan.BaoLoi(ex, "tên/danh mục/màu là chữ, giá là số > 0, tồn kho là số ≥ 0, mã NCC dạng NCC01");
        }
    }

    private void SuaHoa()
    {
        try
        {
            Console.Write("Mã hoa cần sửa (VD: FL01): ");
            string id = Console.ReadLine()?.Trim().ToUpper() ?? "";
            var cu = _flowerService.TimTheoMa(id);
            if (cu == null)
            {
                Console.WriteLine($"❌ Không tìm thấy hoa mã {id}!");
                return;
            }
            if (_batchService.CoLo(id))
            {
                Console.WriteLine("⚠️ Hoa này đang có lô — tồn kho được tính theo số còn trong lô.");
                Console.WriteLine("   Sửa 'Tồn kho' ở đây sẽ làm lệch với lô (nên sửa qua nhập hàng / thanh lý lô).");
            }
            string name = NhapLieu.BatBuoc("Tên mới", "VD: Hồng Đỏ Đà Lạt");
            string cat = NhapLieu.BatBuoc("Danh mục mới", "VD: Hồng");
            decimal price = NhapLieu.Tien("Đơn giá mới", "VD: 5000", false);
            int stock = NhapLieu.SoNguyen("Tồn kho mới", "VD: 100", 0, 1_000_000);
            string color = NhapLieu.BatBuoc("Màu mới", "VD: Đỏ");
            string supId = NhapMaNCC(cu.SupplierId);

            Console.WriteLine(_flowerService.Update(id, name, cat, price, stock, color, supId)
                ? "✅ Sửa thành công!"
                : "❌ Sửa thất bại (mã hoa không tồn tại hoặc dữ liệu không hợp lệ).");
        }
        catch (Exception ex)
        {
            AnToan.BaoLoi(ex, "mã hoa dạng FL01, giá là số > 0, tồn kho là số ≥ 0, mã NCC dạng NCC01");
        }
    }

    private void XoaHoa()
    {
        Console.Write("Nhập Mã hoa cần xóa (VD: FL01): ");
        string id = Console.ReadLine()?.Trim().ToUpper() ?? "";
        var f = _flowerService.TimTheoMa(id);
        if (f == null) { Console.WriteLine("❌ Không tìm thấy!"); return; }

        Console.Write($"Bạn có chắc muốn xóa hoa {f.Name} ({id})? (y/n): ");
        if (Console.ReadLine()?.Trim().ToLower() == "y")
        {
            if (_flowerService.Delete(id))
                Console.WriteLine("✅ Đã xóa!");
            else
                Console.WriteLine("❌ Xóa thất bại (hoa này có thể đang gắn với đơn hàng hoặc lô hoa).");
        }
    }

    private string NhapMaNCC(string? hienTai)
    {
        while (true)
        {
            string goiY = string.IsNullOrEmpty(hienTai)
                ? "VD: NCC01, Enter = chưa có"
                : $"Enter = giữ nguyên {hienTai}, VD: NCC01";
            Console.Write($"Mã NCC ({goiY}): ");
            string ma = Console.ReadLine()?.Trim().ToUpper() ?? "";
            if (string.IsNullOrEmpty(ma)) return hienTai ?? "";
            if (_supplierService.FindById(ma) != null) return ma;
            Console.WriteLine($"⚠️ Mã NCC '{ma}' không tồn tại. Vui lòng nhập đúng hoặc Enter để bỏ qua.");
        }
    }

    private void InDsHoa(List<Flower> list)
    {
        if (list.Count == 0) { Console.WriteLine("Không có dữ liệu."); return; }
        var rows = list.Select(f => new object[] {
            f.FlowerId, f.Name, f.Category, f.UnitPrice, f.StockQuantity, f.Color, f.SupplierId
        }).ToList();
        InBangHelper.InBang(rows, ["Mã Hoa", "Tên Hoa", "Danh Mục", "Giá Bán", "Tồn Kho", "Màu", "Mã NCC"]);
    }

    private void InBang(List<object[]> list, string[]? headers = null)
    {
        InBangHelper.InBang(list, headers);
    }
}
