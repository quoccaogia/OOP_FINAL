using System;
using System.Collections.Generic;
using System.Globalization;
using vn.edu.crud.model;
using vn.edu.crud.service;
using vn.edu.crud.util;

namespace vn.edu.crud.menu;

public class OrderMenu
{
    private readonly OrderService _orderService = new();
    private readonly PaymentService _paymentService = new();
    private readonly CustomerService _customerService = new();
    private readonly EmployeeService _employeeService = new();
    private readonly FlowerService _flowerService = new();
    private readonly FlowerBatchService _batchService = new();
    private readonly CultureInfo _vnCulture = new("vi-VN");

    public void HienThi()
    {
        int chon = -1;
        do
        {
            try
            {
                Console.WriteLine("\n╔══════════════════════════════════════╗");
                Console.WriteLine("║        QUẢN LÝ ĐƠN HÀNG              ║");
                Console.WriteLine("╠══════════════════════════════════════╣");
                Console.WriteLine("║  ── THAO TÁC ──                      ║");
                Console.WriteLine("║  1. Tạo đơn hàng mới                 ║");
                Console.WriteLine("║  2. Thêm hoa vào đơn                 ║");
                Console.WriteLine("║  3. Thanh toán đơn hàng              ║");
                Console.WriteLine("║  4. Cập nhật trạng thái đơn          ║");
                Console.WriteLine("║  5. Xóa chi tiết hoa khỏi đơn        ║");
                Console.WriteLine("║  6. Xóa đơn hàng                     ║");
                Console.WriteLine("║  7. Xem chi tiết 1 đơn               ║");
                Console.WriteLine("║  ── BÁO CÁO ──                       ║");
                Console.WriteLine("║  8. Đơn hàng giá trị cao (> 500k)    ║");
                Console.WriteLine("║  9. Đơn chưa giao hôm nay (LINQ)     ║");
                Console.WriteLine("║ 10. Doanh thu hôm nay                ║");
                Console.WriteLine("║ 11. Đơn hàng của 1 khách (Nhập SĐT)  ║");
                Console.WriteLine("║ 12. Thống kê đơn theo trạng thái     ║");
                Console.WriteLine("║ 13. Đơn có nhiều loại hoa (>= 3)     ║");
                Console.WriteLine("║ 14. Khách đã từng đặt hàng           ║");
                Console.WriteLine("║ 15. Khách chưa từng đặt hàng         ║");
                Console.WriteLine("║ 16. Khách nhiều đơn nhất             ║");
                Console.WriteLine("║ 17. NV nhiều đơn nhất                ║");
                Console.WriteLine("║ 18. Tổng chi tiêu theo khách         ║");
                Console.WriteLine("║ 19. Sửa thông tin đơn (địa chỉ/ngày) ║");
                Console.WriteLine("║ 20. Tìm kiếm đơn (LINQ Lambda)       ║");
                Console.WriteLine("║ 21. Lọc đơn theo trạng thái (LINQ)   ║");
                Console.WriteLine("║ 22. Đơn hàng mới nhất (LINQ)         ║");
                Console.WriteLine("║  0. Quay lại                         ║");
                Console.WriteLine("╚══════════════════════════════════════╝");
                Console.Write("Chọn: ");

                if (!int.TryParse(Console.ReadLine()?.Trim(), out chon)) chon = -1;

                switch (chon)
                {
                    case 1: TaoDonHang(); break;
                    case 2: ThemHoaVaoDon(); break;
                    case 3: ThanhToanDon(); break;
                    case 4: CapNhatTrangThai(); break;
                    case 5: XoaChiTiet(); break;
                    case 6: XoaDon(); break;
                    case 7: XemChiTietDon(); break;
                    case 8: InBangHelper.InBang(_orderService.DonHangGiaCao(500_000m), ["Mã Đơn", "Mã KH", "Mã NV", "Ngày Đặt", "Tổng Tiền"]); break;
                    case 9: InDsDon(_orderService.DonChuaGiaoHomNayLambda()); break;
                    case 10: Console.WriteLine($"Doanh thu hôm nay: {_orderService.DoanhThuHomNay().ToString("C0", _vnCulture)}"); break;
                    case 11: XemDonCuaKhach(); break;
                    case 12: InBangHelper.InBang(_orderService.ThongKeDonTheoTrangThai(), ["Trạng Thái", "Số Lượng Đơn"]); break;
                    case 13: InBangHelper.InBang(_orderService.DonCoNhieuLoaiHoa(3), ["Mã Đơn", "Số Loài Hoa"]); break;
                    case 14: InBangHelper.InBang(_orderService.KhachDaTungDatHang(), ["Mã KH", "Tên KH", "SĐT", "Địa Chỉ", "Email"]); break;
                    case 15: InBangHelper.InBang(_orderService.KhachChuaTungDatHang(), ["Mã KH", "Tên KH", "SĐT", "Địa Chỉ", "Email"]); break;
                    case 16: InBangHelper.InBang(_orderService.KhachNhieuDonNhat(), ["Mã KH", "Tên KH", "Số Lượng Đơn"]); break;
                    case 17: InBangHelper.InBang(_orderService.NhanVienNhieuDonNhat(), ["Mã NV", "Tên NV", "Số Lượng Đơn"]); break;
                    case 18: InBangHelper.InBang(_orderService.TongChiTieuTheoKhach(), ["Mã KH", "Tên KH", "Tổng Chi Tiêu"]); break;
                    case 19: SuaThongTinDon(); break;
                    case 20: TimKiemDonHangLambda(); break;
                    case 21: LocDonTheoTrangThaiLambda(); break;
                    case 22: DonHangMoiNhatLambda(); break;
                    case 0: Console.WriteLine("Quay lại menu chính."); break;
                    default: Console.WriteLine("⚠️ Chọn sai, vui lòng chọn lại!"); break;
                }
            }
            catch (Exception ex)
            {
                AnToan.BaoLoi(ex, "số trong menu 0-22.");
                chon = -1;
            }
        } while (chon != 0);
    }

    private string DichTrangThai(string? status)
    {
        return status switch
        {
            "DANG_CHUAN_BI" or "CHO_XU_LY" or "CHO_XAC_NHAN" => "Đang chuẩn bị hoa",
            "DANG_GIAO" => "Đang giao hàng",
            "HOAN_THANH" => "Hoàn thành",
            "GIAO_THAT_BAI" => "Giao thất bại",
            "DA_HUY" => "Đã hủy",
            _ => status ?? "Không rõ"
        };
    }

    private void TaoDonHang()
    {
        try
        {
            Console.WriteLine("\n--- TẠO ĐƠN HÀNG MỚI ---");
            var o = new Order
            {
                OrderId = _orderService.SinhMaDonMoi()
            };
            Console.WriteLine($"Mã đơn tự sinh: {o.OrderId}");

            Console.WriteLine("(Bấm Enter nếu là khách mới, chưa có SĐT)");
            string sdt = NhapLieu.SdtKhach("SĐT khách hàng", false);

            if (!string.IsNullOrEmpty(sdt))
            {
                var dsKH = _customerService.TimTheoSdt(sdt);
                if (dsKH.Count == 0)
                {
                    Console.WriteLine($"❌ Không tìm thấy khách hàng nào dùng SĐT: {sdt}");
                    Console.Write("👉 Tạo khách hàng mới với SĐT này? (y/n): ");
                    if (Console.ReadLine()?.Trim().ToLower() == "y")
                    {
                        o.CustomerId = TaoKhachHangNhanh(sdt);
                    }
                    else
                    {
                        Console.WriteLine("Đã hủy tạo đơn hàng.");
                        return;
                    }
                }
                else
                {
                    var kh = dsKH[0];
                    o.CustomerId = kh.CustomerId;
                    Console.WriteLine($"✅ Khách hàng: {kh.Name} (Mã: {kh.CustomerId})");
                }
            }
            else
            {
                o.CustomerId = TaoKhachHangNhanh("");
            }

            if (string.IsNullOrEmpty(o.CustomerId))
            {
                Console.WriteLine("❌ Tạo đơn hàng thất bại (Chưa có khách hàng).");
                return;
            }

            var dsNV = _employeeService.FindAll();
            if (dsNV.Count == 0)
            {
                Console.WriteLine("❌ Hệ thống chưa có nhân viên nào!");
                return;
            }
            Console.WriteLine("\n--- CHỌN NHÂN VIÊN PHỤ TRÁCH ---");
            for (int i = 0; i < dsNV.Count; i++)
            {
                var nv = dsNV[i];
                Console.WriteLine($"  {i + 1}. {nv.Name,-20} (Mã: {nv.EmployeeId} - {EmployeeRole.GetLabelFromString(nv.Role)})");
            }

            string? employeeId = null;
            while (employeeId == null)
            {
                Console.Write($"Nhập số chọn nhân viên (1-{dsNV.Count}): ");
                if (int.TryParse(Console.ReadLine()?.Trim(), out int chonNV) && chonNV >= 1 && chonNV <= dsNV.Count)
                {
                    employeeId = dsNV[chonNV - 1].EmployeeId;
                }
                else
                {
                    Console.WriteLine("⚠️ Vui lòng chọn đúng số!");
                }
            }
            o.EmployeeId = employeeId;
            o.OrderDate = DateTime.Now;
            Console.WriteLine($"Ngày đặt: {o.OrderDate:yyyy-MM-dd HH:mm:ss}");

            var ngayMacDinh = o.OrderDate.AddHours(2);
            while (true)
            {
                Console.Write("Ngày giao (yyyy-MM-dd HH:mm:ss, Enter = +2h): ");
                string dateStr = Console.ReadLine()?.Trim() ?? "";
                if (string.IsNullOrEmpty(dateStr))
                {
                    o.DeliveryDate = ngayMacDinh;
                    Console.WriteLine($"→ Dùng mặc định: {ngayMacDinh:yyyy-MM-dd HH:mm:ss}");
                    break;
                }
                if (DateTime.TryParse(dateStr, out DateTime moi))
                {
                    if (moi < o.OrderDate)
                    {
                        Console.WriteLine($"❌ Ngày giao không được trước ngày đặt ({o.OrderDate:yyyy-MM-dd HH:mm:ss})!");
                        continue;
                    }
                    o.DeliveryDate = moi;
                    break;
                }
                Console.WriteLine("❌ Ngày giờ không hợp lệ! Ví dụ: 2026-10-25 15:00:00");
            }

            Console.Write("Địa chỉ giao (Enter = lấy địa chỉ khách, VD: 123 Lê Lợi, Quận 1): ");
            string addr = Console.ReadLine()?.Trim() ?? "";
            if (string.IsNullOrEmpty(addr))
            {
                var kh = _customerService.FindById(o.CustomerId);
                addr = kh?.Address ?? "Tại quầy";
                Console.WriteLine($"→ Dùng địa chỉ khách: {addr}");
            }
            o.DeliveryAddress = addr;
            o.OrderStatus = "DANG_CHUAN_BI";

            Console.WriteLine("\n>>> Đang tạo đơn...");
            if (_orderService.TaoDonHang(o))
            {
                Console.WriteLine($"✅ Tạo đơn thành công! Mã đơn: {o.OrderId}");
                Console.WriteLine("👉 Hãy vào chức năng [2] để thêm hoa vào đơn.");
            }
        }
        catch (Exception ex)
        {
            AnToan.BaoLoi(ex, "nhập đúng thông tin khách hàng và ngày giờ giao.");
        }
    }

    private string TaoKhachHangNhanh(string sdtSan)
    {
        string id = _customerService.SinhMaKhachMoi();
        string sdt = sdtSan;
        if (string.IsNullOrEmpty(sdt))
        {
            sdt = NhapLieu.SdtKhach("SĐT khách hàng", true);
        }
        string ten = NhapLieu.BatBuoc("Tên khách hàng", "VD: Nguyễn Văn A");
        string diaChi = NhapLieu.TuyChon("Địa chỉ", "VD: Quận 1, TP.HCM");
        string email = NhapLieu.Email("Email", false);

        var kh = new Customer(id, ten, sdt, diaChi, email);
        if (_customerService.Insert(kh))
        {
            Console.WriteLine($"✅ Đã tạo nhanh tài khoản cho khách: {ten} ({id})");
            return id;
        }
        return "";
    }

    private void ThemHoaVaoDon()
    {
        Console.Write("Nhập Mã Đơn (VD: ORD001): ");
        string orderId = Console.ReadLine()?.Trim().ToUpper() ?? "";
        var don = _orderService.TimTheoMa(orderId);
        if (don == null)
        {
            Console.WriteLine($"❌ Đơn hàng {orderId} không tồn tại!");
            return;
        }

        Console.Write("Nhập Mã Hoa (VD: FL01): ");
        string flowerId = Console.ReadLine()?.Trim().ToUpper() ?? "";
        var hoa = _flowerService.TimTheoMa(flowerId);
        if (hoa == null)
        {
            Console.WriteLine($"❌ Hoa {flowerId} không tồn tại!");
            return;
        }

        int soLuong = NhapLieu.SoNguyen("Số lượng mua", "VD: 5", 1, 100_000);
        if (hoa.StockQuantity < soLuong)
        {
            Console.WriteLine($"❌ Tồn kho không đủ! Hiện còn {hoa.StockQuantity} bông.");
            return;
        }

        if (_orderService.HoaDaCoTrongDon(orderId, flowerId))
        {
            _orderService.CongThemSoLuong(orderId, flowerId, soLuong);
        }
        else
        {
            _orderService.ThemChiTiet(orderId, flowerId, soLuong, hoa.UnitPrice);
        }

        _orderService.TruSoLuong(flowerId, soLuong);
        _batchService.XuatKho(flowerId, soLuong);

        Console.WriteLine($"✅ Đã thêm {soLuong} bông {hoa.Name} vào đơn {orderId}!");
    }

    private void ThanhToanDon()
    {
        Console.Write("Nhập Mã Đơn cần thanh toán (VD: ORD001): ");
        string orderId = Console.ReadLine()?.Trim().ToUpper() ?? "";
        var don = _orderService.TimTheoMa(orderId);
        if (don == null)
        {
            Console.WriteLine($"❌ Đơn hàng {orderId} không tồn tại!");
            return;
        }

        decimal tongTien = _paymentService.TinhTongTienDon(orderId);
        decimal daTra = _paymentService.TinhDaTra(orderId);
        decimal conLai = tongTien - daTra;

        Console.WriteLine($"Tổng tiền đơn: {tongTien.ToString("C0", _vnCulture)} | Đã trả: {daTra.ToString("C0", _vnCulture)} | Còn nợ: {conLai.ToString("C0", _vnCulture)}");
        if (conLai <= 0)
        {
            Console.WriteLine("✅ Đơn này đã được thanh toán đầy đủ!");
            return;
        }

        decimal soTien = NhapLieu.Tien("Số tiền thanh toán", $"VD: {conLai}", false);
        if (soTien > conLai)
        {
            Console.WriteLine($"⚠️ Số tiền vượt quá số nợ còn lại ({conLai.ToString("C0", _vnCulture)}). Đặt lại bằng {conLai.ToString("C0", _vnCulture)}.");
            soTien = conLai;
        }

        Console.Write("Phương thức (1. TIEN_MAT, 2. CHUYEN_KHOAN, 3. THE): ");
        string pt = Console.ReadLine()?.Trim() switch
        {
            "2" => "CHUYEN_KHOAN",
            "3" => "THE",
            _ => "TIEN_MAT"
        };

        var p = new Payment
        {
            PaymentId = _paymentService.SinhMaThanhToan(),
            OrderId = orderId,
            Amount = soTien,
            PaymentMethod = pt,
            PaymentDate = DateTime.Now,
            PaymentStatus = "THANH_TOAN_THANH_CONG"
        };

        if (_paymentService.TaoThanhToan(p))
        {
            Console.WriteLine($"✅ Đã thanh toán {soTien.ToString("C0", _vnCulture)} cho đơn {orderId} (Mã TT: {p.PaymentId})!");
        }
    }

    private void CapNhatTrangThai()
    {
        Console.Write("Nhập Mã Đơn cần cập nhật: ");
        string orderId = Console.ReadLine()?.Trim().ToUpper() ?? "";
        var don = _orderService.TimTheoMa(orderId);
        if (don == null)
        {
            Console.WriteLine($"❌ Không tìm thấy đơn {orderId}!");
            return;
        }

        Console.WriteLine($"Trạng thái hiện tại: {DichTrangThai(don.OrderStatus)} ({don.OrderStatus})");
        Console.WriteLine("1. DANG_CHUAN_BI (Đang chuẩn bị hoa)");
        Console.WriteLine("2. DANG_GIAO (Đang giao hàng)");
        Console.WriteLine("3. HOAN_THANH (Hoàn thành)");
        Console.WriteLine("4. GIAO_THAT_BAI (Giao thất bại)");
        Console.WriteLine("5. DA_HUY (Đã hủy)");
        Console.Write("Chọn: ");

        string moi = Console.ReadLine()?.Trim() switch
        {
            "1" => "DANG_CHUAN_BI",
            "2" => "DANG_GIAO",
            "3" => "HOAN_THANH",
            "4" => "GIAO_THAT_BAI",
            "5" => "DA_HUY",
            _ => ""
        };

        if (string.IsNullOrEmpty(moi)) { Console.WriteLine("Đã hủy."); return; }

        if (moi == "DA_HUY" || moi == "GIAO_THAT_BAI")
        {
            // Trả lại kho
            var dsHoa = _orderService.LayHoaTrongDon(orderId);
            foreach (var r in dsHoa)
            {
                string fId = r[0]?.ToString() ?? "";
                int qty = r[1] == null ? 0 : Convert.ToInt32(r[1]);
                _orderService.CongTonKho(fId, qty);
                _batchService.NhapLaiKho(fId, qty);
            }
            if (moi == "GIAO_THAT_BAI")
            {
                _paymentService.HoanTienToanBo(orderId);
            }
        }

        if (_orderService.CapNhatTrangThai(orderId, moi))
            Console.WriteLine($"✅ Cập nhật trạng thái thành: {DichTrangThai(moi)}");
    }

    private void XoaChiTiet()
    {
        Console.Write("Nhập Mã Đơn: ");
        string orderId = Console.ReadLine()?.Trim().ToUpper() ?? "";
        Console.Write("Nhập Mã Hoa cần xóa khỏi đơn: ");
        string flowerId = Console.ReadLine()?.Trim().ToUpper() ?? "";

        int sl = _orderService.LaySoLuongTrongDon(orderId, flowerId);
        if (sl <= 0)
        {
            Console.WriteLine("❌ Hoa này không có trong đơn!");
            return;
        }

        if (_orderService.XoaChiTiet(orderId, flowerId))
        {
            _orderService.CongTonKho(flowerId, sl);
            _batchService.NhapLaiKho(flowerId, sl);
            Console.WriteLine($"✅ Đã xóa hoa {flowerId} khỏi đơn {orderId} và cộng lại {sl} bông vào kho!");
        }
    }

    private void XoaDon()
    {
        Console.Write("Nhập Mã Đơn cần xóa: ");
        string orderId = Console.ReadLine()?.Trim().ToUpper() ?? "";
        var don = _orderService.TimTheoMa(orderId);
        if (don == null)
        {
            Console.WriteLine($"❌ Không tìm thấy đơn {orderId}!");
            return;
        }

        if (_orderService.DonDaCoThanhToan(orderId))
        {
            Console.WriteLine("❌ Đơn này đã có giao dịch thanh toán thành công, không được xóa trực tiếp!");
            return;
        }

        // Hoàn lại kho trước khi xóa
        var dsHoa = _orderService.LayHoaTrongDon(orderId);
        foreach (var r in dsHoa)
        {
            string fId = r[0]?.ToString() ?? "";
            int qty = r[1] == null ? 0 : Convert.ToInt32(r[1]);
            _orderService.CongTonKho(fId, qty);
            _batchService.NhapLaiKho(fId, qty);
        }

        if (_orderService.XoaDon(orderId))
            Console.WriteLine($"✅ Đã xóa hoàn toàn đơn {orderId}!");
    }

    private void XemChiTietDon()
    {
        Console.Write("Nhập Mã Đơn: ");
        string orderId = Console.ReadLine()?.Trim().ToUpper() ?? "";
        var tt = _orderService.ThongTinDayDuDonHang(orderId);
        if (tt.Count == 0)
        {
            Console.WriteLine($"❌ Không tìm thấy đơn {orderId}!");
            return;
        }

        var r = tt[0];
        Console.WriteLine("\n╔══════════════════════════════════════════════════════╗");
        Console.WriteLine($"║ THÔNG TIN ĐƠN HÀNG: {r[0],-32} ║");
        Console.WriteLine("╠══════════════════════════════════════════════════════╣");
        Console.WriteLine($"  Khách hàng  : {r[1]} (SĐT: {r[2]})");
        Console.WriteLine($"  NV phụ trách: {r[3]}");
        Console.WriteLine($"  Ngày đặt    : {r[4]}");
        Console.WriteLine($"  Ngày giao   : {r[5]}");
        Console.WriteLine($"  Địa chỉ giao: {r[6]}");
        Console.WriteLine($"  Trạng thái  : {DichTrangThai(r[7]?.ToString())}");
        Console.WriteLine("------------------------------------------------------");
        Console.WriteLine("  DANH SÁCH HOA TRONG ĐƠN:");
        var chiTiet = _orderService.ChiTietDonHang(orderId);
        decimal tong = 0;
        foreach (var ct in chiTiet)
        {
            string ten = ct[0]?.ToString() ?? "";
            int sl = ct[1] == null ? 0 : Convert.ToInt32(ct[1]);
            decimal gia = ct[2] == null ? 0 : Convert.ToDecimal(ct[2]);
            decimal thanhTien = sl * gia;
            tong += thanhTien;
            Console.WriteLine($"   - {ten,-25} x {sl,3} = {thanhTien.ToString("C0", _vnCulture)}");
        }
        Console.WriteLine("------------------------------------------------------");
        Console.WriteLine($"  TỔNG CỘNG TIỀN ĐƠN: {tong.ToString("C0", _vnCulture)}");
        decimal daTra = _paymentService.TinhDaTra(orderId);
        Console.WriteLine($"  ĐÃ THANH TOÁN     : {daTra.ToString("C0", _vnCulture)}");
        Console.WriteLine($"  CÒN LẠI           : {(tong - daTra).ToString("C0", _vnCulture)}");
        Console.WriteLine("╚══════════════════════════════════════════════════════╝");
    }

    private void XemDonCuaKhach()
    {
        Console.Write("Nhập SĐT khách hàng: ");
        string sdt = NhapLieu.ChuanHoaSdt(Console.ReadLine());
        var dsKH = _customerService.TimTheoSdt(sdt);
        if (dsKH.Count == 0)
        {
            Console.WriteLine("❌ Không tìm thấy khách hàng!");
            return;
        }
        var kh = dsKH[0];
        Console.WriteLine($"✅ Đơn hàng của khách {kh.Name} ({kh.CustomerId}):");
        InDsDon(_orderService.DonCuaKhach(kh.CustomerId));
    }

    private void SuaThongTinDon()
    {
        Console.Write("Nhập Mã Đơn: ");
        string orderId = Console.ReadLine()?.Trim().ToUpper() ?? "";
        var don = _orderService.TimTheoMa(orderId);
        if (don == null) { Console.WriteLine("❌ Không tìm thấy đơn!"); return; }

        Console.Write("Địa chỉ giao mới (Enter = giữ nguyên): ");
        string addr = Console.ReadLine()?.Trim() ?? "";
        if (string.IsNullOrEmpty(addr)) addr = don.DeliveryAddress;

        DateTime ngayGiao = don.DeliveryDate ?? DateTime.Now.AddHours(2);
        Console.Write("Ngày giao mới (yyyy-MM-dd HH:mm:ss, Enter = giữ nguyên): ");
        string dStr = Console.ReadLine()?.Trim() ?? "";
        if (!string.IsNullOrEmpty(dStr) && DateTime.TryParse(dStr, out DateTime ngMoi))
        {
            ngayGiao = ngMoi;
        }

        if (_orderService.CapNhatThongTin(orderId, addr, ngayGiao, don.EmployeeId))
            Console.WriteLine("✅ Cập nhật thông tin giao hàng thành công!");
    }

    private void TimKiemDonHangLambda()
    {
        Console.WriteLine("\n--- TÌM KIẾM ĐƠN HÀNG (LINQ & LAMBDA) ---");
        Console.Write("Nhập từ khóa (Mã đơn, Mã KH hoặc Mã NV): ");
        string kw = Console.ReadLine()?.Trim() ?? "";

        var ds = _orderService.SearchOrdersByLambda(kw);
        if (ds.Count == 0)
        {
            Console.WriteLine($"❌ Không tìm thấy đơn nào khớp với \"{kw}\"");
        }
        else
        {
            Console.WriteLine($"✅ Tìm thấy {ds.Count} đơn hàng khớp:");
            InDsDon(ds);
        }
    }

    private void LocDonTheoTrangThaiLambda()
    {
        Console.WriteLine("\n--- LỌC ĐƠN HÀNG THEO TRẠNG THÁI (LINQ) ---");
        Console.WriteLine("1. DANG_CHUAN_BI | 2. DANG_GIAO | 3. HOAN_THANH | 4. GIAO_THAT_BAI | 5. DA_HUY");
        Console.Write("Chọn trạng thái: ");
        string st = Console.ReadLine()?.Trim() switch
        {
            "1" => "DANG_CHUAN_BI",
            "2" => "DANG_GIAO",
            "3" => "HOAN_THANH",
            "4" => "GIAO_THAT_BAI",
            "5" => "DA_HUY",
            var s => s ?? ""
        };

        var ds = _orderService.LocDonTheoTrangThaiLambda(st);
        Console.WriteLine($"✅ Tìm thấy {ds.Count} đơn trạng thái {st}:");
        InDsDon(ds);
    }

    private void DonHangMoiNhatLambda()
    {
        Console.WriteLine("\n--- ĐƠN HÀNG MỚI NHẤT (LINQ ORDERBYDESCENDING) ---");
        var ds = _orderService.DonHangMoiNhatLambda();
        InDsDon(ds);
    }

    private void InDsDon(List<Order> list)
    {
        if (list.Count == 0) { Console.WriteLine("Không có dữ liệu."); return; }
        var rows = list.Select(o => new object[] {
            o.OrderId, o.CustomerId, o.EmployeeId, o.OrderDate, o.DeliveryDate ?? (object)"", DichTrangThai(o.OrderStatus)
        }).ToList();
        InBangHelper.InBang(rows, ["Mã Đơn", "Mã KH", "Mã NV", "Ngày Đặt", "Ngày Giao", "Trạng Thái"]);
    }

    private void InBang(List<object[]> list, string[]? headers = null)
    {
        InBangHelper.InBang(list, headers);
    }
}
