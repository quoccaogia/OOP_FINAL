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
        Console.WriteLine("\n--- THÊM HOA VÀO ĐƠN ---");
        var dsDon = _orderService.FindAll();
        if (dsDon.Count == 0)
        {
            Console.WriteLine("⚠️ Chưa có đơn hàng nào!");
            return;
        }

        Console.WriteLine("\nDANH SÁCH ĐƠN HÀNG HIỆN CÓ:");
        Console.WriteLine($"{"STT",-4} | {"Mã Đơn",-8} | {"Khách Hàng",-22} | {"Trạng Thái",-18}");
        Console.WriteLine(new string('-', 58));
        for (int i = 0; i < dsDon.Count; i++)
        {
            var o = dsDon[i];
            var kh = _customerService.FindById(o.CustomerId);
            string tenKh = kh?.Name ?? "Khách vãng lai";
            if (tenKh.Length > 20) tenKh = tenKh[..17] + "...";
            Console.WriteLine($"{i + 1,-4} | {o.OrderId,-8} | {tenKh,-22} | {DichTrangThai(o.OrderStatus),-18}");
        }

        Console.Write("\nNhập STT đơn hoặc Mã Đơn (0 để hủy): ");
        string nhapDon = Console.ReadLine()?.Trim() ?? "";
        if (nhapDon == "0" || string.IsNullOrEmpty(nhapDon)) return;

        Order? don = null;
        if (int.TryParse(nhapDon, out int sttDon) && sttDon >= 1 && sttDon <= dsDon.Count)
        {
            don = dsDon[sttDon - 1];
        }
        else
        {
            don = _orderService.TimTheoMa(nhapDon.ToUpper());
        }

        if (don == null)
        {
            Console.WriteLine("❌ Đơn hàng không tồn tại!");
            return;
        }

        string orderId = don.OrderId;
        string trangThaiDon = don.OrderStatus?.Trim() ?? "";

        bool isHopLe = trangThaiDon.Equals("DANG_CHUAN_BI", StringComparison.OrdinalIgnoreCase)
                    || trangThaiDon.Equals("CHO_XU_LY", StringComparison.OrdinalIgnoreCase)
                    || trangThaiDon.Equals("CHO_XAC_NHAN", StringComparison.OrdinalIgnoreCase);

        if (!isHopLe)
        {
            Console.WriteLine($"\n❌ TỪ CHỐI THÊM HOA! Đơn {orderId} đang ở trạng thái [{DichTrangThai(trangThaiDon)}]. Chỉ thêm hoa khi đơn đang 'Đang chuẩn bị hoa'.");
            return;
        }

        if (_paymentService.TinhDaTra(orderId) > 0)
        {
            Console.WriteLine($"\n❌ TỪ CHỐI THÊM HOA! Đơn đã thu {_paymentService.TinhDaTra(orderId).ToString("C0", _vnCulture)} nên không thể thêm/sửa hoa.");
            return;
        }

        while (true)
        {
            var dsHoa = _flowerService.FindAll().Where(f => f.StockQuantity > 0 && f.UnitPrice > 0).ToList();
            if (dsHoa.Count == 0)
            {
                Console.WriteLine("⚠️ Không còn hoa nào có sẵn trong kho!");
                break;
            }

            Console.WriteLine("\nDANH SÁCH HOA CÒN TRONG KHO:");
            Console.WriteLine($"{"STT",-4} | {"Mã Hoa",-8} | {"Tên Hoa",-25} | {"Đơn Giá",-14} | {"Tồn",-6}");
            Console.WriteLine(new string('-', 64));
            for (int i = 0; i < dsHoa.Count; i++)
            {
                var f = dsHoa[i];
                string ten = f.Name;
                if (ten.Length > 23) ten = ten[..20] + "...";
                Console.WriteLine($"{i + 1,-4} | {f.FlowerId,-8} | {ten,-25} | {f.UnitPrice.ToString("C0", _vnCulture),-14} | {f.StockQuantity,-6}");
            }

            Console.Write("\nNhập STT hoa hoặc Mã Hoa muốn thêm (0 để kết thúc): ");
            string nhapHoa = Console.ReadLine()?.Trim() ?? "";
            if (nhapHoa == "0" || string.IsNullOrEmpty(nhapHoa)) break;

            Flower? hoa = null;
            if (int.TryParse(nhapHoa, out int sttHoa) && sttHoa >= 1 && sttHoa <= dsHoa.Count)
            {
                hoa = dsHoa[sttHoa - 1];
            }
            else
            {
                hoa = _flowerService.TimTheoMa(nhapHoa.ToUpper());
            }

            if (hoa == null)
            {
                Console.WriteLine("❌ Không tìm thấy hoa!");
                continue;
            }

            string flowerId = hoa.FlowerId;
            int soLuong = NhapLieu.SoNguyen($"Số lượng mua (1-{hoa.StockQuantity})", "VD: 5", 1, hoa.StockQuantity);

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

            Console.WriteLine($"✅ Đã thêm {hoa.Name} x{soLuong} vào đơn {orderId}!");

            Console.Write("Thêm hoa khác vào đơn? (c/K, Enter = c): ");
            if (Console.ReadLine()?.Trim().ToLower() == "k") break;
        }
    }

    private void ThanhToanDon()
    {
        Console.WriteLine("\n--- THANH TOÁN ĐƠN HÀNG ---");
        var dsChuaTT = _paymentService.LayDanhSachDonChuaThanhToan();
        if (dsChuaTT.Count == 0)
        {
            Console.WriteLine("✅ Tuyệt vời! Hiện tại không có đơn hàng nào nợ tiền.");
            return;
        }

        Console.WriteLine("\nDANH SÁCH ĐƠN HÀNG CHƯA THU ĐỦ TIỀN:");
        Console.WriteLine($"{"STT",-4} | {"Mã Đơn",-8} | {"Khách Hàng",-22} | {"Trạng Thái",-18} | {"Tổng Tiền",-14} | {"Đã Trả",-14}");
        Console.WriteLine(new string('-', 88));
        for (int i = 0; i < dsChuaTT.Count; i++)
        {
            var r = dsChuaTT[i];
            string mDon = r[0]?.ToString() ?? "";
            string tenKh = r[2]?.ToString() ?? "";
            if (tenKh.Length > 20) tenKh = tenKh[..17] + "...";
            string st = r[1]?.ToString() ?? "";
            decimal tong = r[3] != null ? Convert.ToDecimal(r[3]) : 0;
            decimal tra = r[4] != null ? Convert.ToDecimal(r[4]) : 0;
            Console.WriteLine($"{i + 1,-4} | {mDon,-8} | {tenKh,-22} | {DichTrangThai(st),-18} | {tong.ToString("C0", _vnCulture),-14} | {tra.ToString("C0", _vnCulture),-14}");
        }

        Console.Write("\n👉 Nhập STT đơn hoặc Mã Đơn cần thanh toán (0 để hủy): ");
        string nhap = Console.ReadLine()?.Trim() ?? "";
        if (nhap == "0" || string.IsNullOrEmpty(nhap)) return;

        string orderId = "";
        if (int.TryParse(nhap, out int stt) && stt >= 1 && stt <= dsChuaTT.Count)
        {
            orderId = dsChuaTT[stt - 1][0]?.ToString() ?? "";
        }
        else
        {
            orderId = nhap.ToUpper();
        }

        var don = _orderService.TimTheoMa(orderId);
        if (don == null)
        {
            Console.WriteLine($"❌ Đơn hàng {orderId} không tồn tại!");
            return;
        }

        decimal tongTien = _paymentService.TinhTongTienDon(orderId);
        decimal daTra = _paymentService.TinhDaTra(orderId);
        decimal conLai = tongTien - daTra;
        decimal tienCoc = _paymentService.TinhTienCoc(orderId);

        Console.WriteLine($"\n--- CHI TIẾT THANH TOÁN ĐƠN {orderId} ---");
        Console.WriteLine($"Tổng tiền đơn: {tongTien.ToString("C0", _vnCulture)} | Đã trả: {daTra.ToString("C0", _vnCulture)} | Còn nợ: {conLai.ToString("C0", _vnCulture)}");

        if (conLai <= 0)
        {
            Console.WriteLine("✅ Đơn này đã được thanh toán đầy đủ!");
            return;
        }

        decimal amount = 0;
        if (daTra == 0)
        {
            Console.WriteLine("\nChọn hình thức thanh toán:");
            Console.WriteLine($"  1. Trả đủ tiền cọc (20%) : {tienCoc.ToString("C0", _vnCulture)}");
            Console.WriteLine($"  2. Trả hết cả tiền đơn   : {tongTien.ToString("C0", _vnCulture)}");
            Console.WriteLine("  0. Hủy thao tác");
            Console.Write("Nhập số chọn (1 hoặc 2): ");
            string ht = Console.ReadLine()?.Trim() ?? "";
            if (ht == "1") amount = tienCoc;
            else if (ht == "2") amount = tongTien;
            else { Console.WriteLine("Đã hủy thao tác."); return; }
        }
        else
        {
            Console.WriteLine($"Đơn đã thanh toán một phần. Thanh toán nốt số tiền còn lại: {conLai.ToString("C0", _vnCulture)}");
            Console.Write("Xác nhận khách trả hết? (y/n): ");
            if (Console.ReadLine()?.Trim().ToLower() != "y") { Console.WriteLine("Đã hủy."); return; }
            amount = conLai;
        }

        Console.Write("Phương thức (1. TIEN_MAT, 2. CHUYEN_KHOAN, 3. MOMO, Enter = TIEN_MAT): ");
        string pt = Console.ReadLine()?.Trim() switch
        {
            "2" => "CHUYEN_KHOAN",
            "3" => "MOMO",
            _ => "TIEN_MAT"
        };

        var p = new Payment
        {
            PaymentId = _paymentService.SinhMaThanhToan(),
            OrderId = orderId,
            Amount = amount,
            PaymentMethod = pt,
            PaymentDate = DateTime.Now,
            PaymentStatus = "THANH_TOAN_THANH_CONG"
        };

        if (_paymentService.TaoThanhToan(p))
        {
            decimal daTraSau = daTra + amount;
            Console.WriteLine($"✅ Thanh toán thành công {amount.ToString("C0", _vnCulture)} cho đơn {orderId} (Mã TT: {p.PaymentId})!");
            Console.WriteLine($"Đã trả: {daTraSau.ToString("C0", _vnCulture)} / {tongTien.ToString("C0", _vnCulture)}");
            if (daTraSau >= tongTien)
            {
                Console.WriteLine("🎉 Khách đã trả hết đơn. Khi giao hoa xong hãy cập nhật trạng thái sang HOÀN THÀNH.");
            }
            else
            {
                Console.WriteLine("👉 Đã thu đủ cọc 20%. Khi giao hoa xong thu nốt phần còn lại.");
            }
        }
        else
        {
            Console.WriteLine("❌ Thanh toán thất bại!");
        }
    }

    private void CapNhatTrangThai()
    {
        Console.WriteLine("\n--- CẬP NHẬT TRẠNG THÁI ĐƠN HÀNG ---");
        var dsDon = _orderService.FindAll();
        var dsChuaXong = dsDon.Where(o => !string.Equals(o.OrderStatus, "HOAN_THANH", StringComparison.OrdinalIgnoreCase)
                                       && !string.Equals(o.OrderStatus, "DA_HUY", StringComparison.OrdinalIgnoreCase)
                                       && !string.Equals(o.OrderStatus, "GIAO_THAT_BAI", StringComparison.OrdinalIgnoreCase)).ToList();

        if (dsChuaXong.Count == 0)
        {
            Console.WriteLine("⚠️ Hiện không có đơn hàng nào đang chờ xử lý.");
            return;
        }

        Console.WriteLine("\nDANH SÁCH ĐƠN HÀNG ĐANG XỬ LÝ:");
        Console.WriteLine($"{"STT",-4} | {"Mã Đơn",-8} | {"Khách Hàng",-22} | {"Trạng Thái",-18}");
        Console.WriteLine(new string('-', 58));
        for (int i = 0; i < dsChuaXong.Count; i++)
        {
            var o = dsChuaXong[i];
            var kh = _customerService.FindById(o.CustomerId);
            string tenKh = kh?.Name ?? "Khách vãng lai";
            if (tenKh.Length > 20) tenKh = tenKh[..17] + "...";
            Console.WriteLine($"{i + 1,-4} | {o.OrderId,-8} | {tenKh,-22} | {DichTrangThai(o.OrderStatus),-18}");
        }

        Console.Write("\nNhập STT đơn hoặc Mã Đơn cần cập nhật (0 để hủy): ");
        string nhap = Console.ReadLine()?.Trim() ?? "";
        if (nhap == "0" || string.IsNullOrEmpty(nhap)) return;

        Order? don = null;
        if (int.TryParse(nhap, out int stt) && stt >= 1 && stt <= dsChuaXong.Count)
        {
            don = dsChuaXong[stt - 1];
        }
        else
        {
            don = _orderService.TimTheoMa(nhap.ToUpper());
        }

        if (don == null)
        {
            Console.WriteLine("❌ Không tìm thấy đơn hàng!");
            return;
        }

        string orderId = don.OrderId;
        Console.WriteLine($"\nĐơn: {orderId} | Trạng thái hiện tại: {DichTrangThai(don.OrderStatus)} ({don.OrderStatus})");
        Console.WriteLine("1. DANG_CHUAN_BI (Đang chuẩn bị hoa)");
        Console.WriteLine("2. DANG_GIAO (Đang giao hàng)");
        Console.WriteLine("3. HOAN_THANH (Hoàn thành)");
        Console.WriteLine("4. GIAO_THAT_BAI (Giao thất bại - không liên lạc được)");
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
                Console.WriteLine("💰 Đã ghi nhận hoàn tiền cho khách (hoặc hủy thu nếu có) và trả hoa về kho.");
            }
            else
            {
                Console.WriteLine("📦 Đã hủy đơn và hoàn trả toàn bộ hoa về kho!");
            }
        }

        if (_orderService.CapNhatTrangThai(orderId, moi))
            Console.WriteLine($"✅ Cập nhật trạng thái thành công: {DichTrangThai(moi)}");
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
