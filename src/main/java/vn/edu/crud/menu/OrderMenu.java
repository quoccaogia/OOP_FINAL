package vn.edu.crud.menu;

import java.util.ArrayList;
import vn.edu.crud.dao.CustomerDAO;
import vn.edu.crud.dao.OrderDAO;
import vn.edu.crud.dao.PaymentDAO;
import vn.edu.crud.model.Customer;
import vn.edu.crud.model.Order;
import vn.edu.crud.model.Payment;
import vn.edu.crud.util.DBBase;

import vn.edu.crud.dao.FlowerDAO;
import vn.edu.crud.model.Flower;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class OrderMenu {

    private final Scanner sc = new Scanner(System.in);
    private final OrderDAO dao = new OrderDAO();
    private final PaymentDAO paymentDAO = new PaymentDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final FlowerDAO flowerDAO = new FlowerDAO();

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // =====================================================
    // MENU CHÍNH
    // =====================================================
    public void hienThi() {
        int chon = -1;
        do {
            try {
                System.out.println("\n╔══════════════════════════════════════╗");
                System.out.println("║        QUẢN LÝ ĐƠN HÀNG              ║");
                System.out.println("╠══════════════════════════════════════╣");
                System.out.println("║  ── THAO TÁC ──                      ║");
                System.out.println("║  1. Tạo đơn hàng mới                 ║");
                System.out.println("║  2. Thêm hoa vào đơn                 ║");
                System.out.println("║  3. Thanh toán đơn hàng              ║");
                System.out.println("║  4. Cập nhật trạng thái              ║");
                System.out.println("║  5. Xóa chi tiết hoa khỏi đơn        ║");
                System.out.println("║  6. Xóa đơn hàng                     ║");
                System.out.println("║  ── BÁO CÁO ──                       ║");
                System.out.println("║  7. Hóa đơn > 500.000                ║");
                System.out.println("║  8. Đơn chưa giao hôm nay            ║");
                System.out.println("║  9. Doanh thu hôm nay                ║");
                System.out.println("║ 10. Khách đã từng đặt hàng           ║");
                System.out.println("║ 11. Khách chưa từng đặt hàng         ║");
                System.out.println("║ 12. Khách nhiều đơn nhất             ║");
                System.out.println("║ 13. NV đang phụ trách đơn            ║");
                System.out.println("║ 14. NV nhiều đơn nhất                ║");
                System.out.println("║ 15. Tổng chi tiêu theo khách         ║");
                System.out.println("║ 16. Đơn hàng của 1 khách             ║");
                System.out.println("║ 17. Chi tiết 1 đơn hàng              ║");
                System.out.println("║ 18. Đơn có >= 3 loại hoa             ║");
                System.out.println("║ 19. Đơn theo trạng thái              ║");
                System.out.println("║  0. Quay lại                         ║");
                System.out.println("╚══════════════════════════════════════╝");
                System.out.print("Chọn: ");

                chon = Integer.parseInt(sc.nextLine().trim());

                switch (chon) {
                    case 1:
                        taoDonHang();
                        break;
                    case 2:
                        themHoaVaoDon();
                        break;
                    case 3:
                        thanhToanDon();
                        break;
                    case 4:
                        capNhatTrangThai();
                        break;
                    case 5:
                        xoaChiTiet();
                        break;
                    case 6:
                        xoaDon();
                        break;
                    case 7:
                        inBang(dao.donHangGiaCao(new BigDecimal("500000")));
                        break;
                    case 8:
                        inDsDon(dao.donChuaGiaoHomNay());
                        break;
                    case 9:
                        System.out.println("Doanh thu hôm nay: " + dao.doanhThuHomNay() + " VND");
                        break;
                    case 10:
                        inBang(dao.khachDaTungDatHang());
                        break;
                    case 11:
                        inBang(dao.khachChuaTungDatHang());
                        break;
                    case 12:
                        inBang(dao.khachNhieuDonNhat());
                        break;
                    case 13:
                        inBang(dao.nhanVienCoDonHang());
                        break;
                    case 14:
                        inBang(dao.nhanVienNhieuDonNhat());
                        break;
                    case 15:
                        inBang(dao.tongChiTieuTheoKhach());
                        break;
                    case 16:
                        System.out.print("Nhập mã khách: ");
                        inDsDon(dao.donCuaKhach(sc.nextLine().trim())); // ← ĐÃ SỬA
                        break;
                    case 17: {
                        Order donChon = chonDonTuDanhSach();
                        if (donChon != null) {
                            inDonHangDayDu(donChon.getOrderId());
                        }
                        break;
                    }
                    case 18:
                        inBang(dao.donCoNhieuLoaiHoa(3));
                        break;
                    case 19:
                        inBang(dao.thongKeDonTheoTrangThai());
                        break;
                    case 0:
                        System.out.println("Quay lại menu chính.");
                        break;
                    default:
                        System.out.println("⚠️ Chọn sai, vui lòng chọn lại!");
                }
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Vui lòng nhập SỐ, không nhập chữ!");
                chon = -1;
            } catch (Exception e) {
                System.out.println("❌ Lỗi không mong muốn: " + e.getMessage());
                e.printStackTrace();
                chon = -1;
            }
        } while (chon != 0);
    }

    // =====================================================
    // 1. TẠO ĐƠN HÀNG
    // =====================================================
    private void taoDonHang() {
        try {
            System.out.println("\n--- TẠO ĐƠN HÀNG MỚI ---");

            Order o = new Order();
            o.setOrderId(dao.sinhMaDonMoi());
            System.out.println("Mã đơn tự sinh: " + o.getOrderId());

            // ============ CHỌN / TẠO KHÁCH HÀNG ============
            while (true) {
                System.out.print("Mã khách hàng (Enter để tạo khách mới): ");
                String customerId = sc.nextLine().trim();

                if (customerId.isEmpty()) {
                    String newId = taoKhachHangMoi();
                    if (newId == null)
                        continue;
                    o.setCustomerId(newId);
                    break;
                }

                if (khachTonTai(customerId)) {
                    o.setCustomerId(customerId);
                    System.out.println("→ Đã chọn khách: " + customerId);
                    break;
                } else {
                    System.out.println("⚠️ Khách '" + customerId + "' chưa có trong hệ thống.");
                    System.out.print("   Tạo khách mới với mã này (y/n)? ");
                    if (sc.nextLine().equalsIgnoreCase("y")) {
                        String newId = nhapThongTinKhach(customerId);
                        if (newId != null) {
                            o.setCustomerId(newId);
                            break;
                        }
                    } else {
                        System.out.println("→ Nhập lại mã khách hàng khác.");
                    }
                }
            }

            // ============ VALIDATE NHÂN VIÊN ============
            while (true) {
                System.out.print("Mã nhân viên phụ trách: ");
                String employeeId = sc.nextLine().trim();

                if (employeeId.isEmpty()) {
                    System.out.println("❌ Không được để trống!");
                    continue;
                }
                if (!nhanVienTonTai(employeeId)) {
                    System.out.println("❌ Nhân viên '" + employeeId + "' không tồn tại!");
                    continue;
                }
                o.setEmployeeId(employeeId);
                break;
            }

            // ============ NGÀY ĐẶT ============
            o.setOrderDate(LocalDateTime.now());
            System.out.println("Ngày đặt: " + o.getOrderDate().format(FMT));

            // ============ NGÀY GIAO ============
            System.out.print("Ngày giao (yyyy-MM-dd HH:mm:ss, Enter = +2h): ");
            String s = sc.nextLine().trim();

            if (s.isEmpty()) {
                o.setDeliveryDate(o.getOrderDate().plusHours(2));
                System.out.println("→ Dùng mặc định: " + o.getDeliveryDate().format(FMT));
            } else {
                try {
                    o.setDeliveryDate(LocalDateTime.parse(s, FMT));
                } catch (Exception e) {
                    System.out.println("❌ Ngày giờ không hợp lệ!");
                    System.out.println("   Format đúng: yyyy-MM-dd HH:mm:ss");
                    System.out.println("   Ví dụ: 2026-10-26 14:00:00");
                    o.setDeliveryDate(o.getOrderDate().plusHours(2));
                    System.out.println("→ Dùng mặc định: " + o.getDeliveryDate().format(FMT));
                }
            }

            // ============ ĐỊA CHỈ GIAO ============
            System.out.print("Địa chỉ giao (Enter = lấy địa chỉ khách): ");
            String addr = sc.nextLine().trim();
            if (addr.isEmpty()) {
                addr = layDiaChiKhach(o.getCustomerId());
                System.out.println("→ Dùng địa chỉ khách: " + addr);
            }
            o.setDeliveryAddress(addr);

            o.setOrderStatus("CHO_XAC_NHAN");

            // ============ INSERT ============
            System.out.println("\n>>> Đang tạo đơn...");
            if (dao.taoDonHang(o)) {
                System.out.println("✅ Tạo đơn thành công! Mã đơn: " + o.getOrderId());
            } else {
                System.out.println("❌ Tạo đơn thất bại! Xem lỗi SQL ở trên console.");
            }
        } catch (Exception e) {
            System.out.println("❌ Lỗi khi tạo đơn: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // =====================================================
    // 2. THÊM HOA VÀO ĐƠN
    // =====================================================
    private void themHoaVaoDon() {
        try {
            System.out.println("\n--- THÊM HOA VÀO ĐƠN ---");

            // ============ BƯỚC A: CHỌN ĐƠN ============
            List<Order> dsDon = dao.findAll();
            if (dsDon.isEmpty()) {
                System.out.println("⚠️ Chưa có đơn hàng nào!");
                return;
            }

            System.out.println("\n╔═══════════════════════════════════════════════════════════════════════╗");
            System.out.println("║                     DANH SÁCH ĐƠN HÀNG HIỆN CÓ                        ║");
            System.out.println("╠════╦══════════╦════════════════════════════╦════════════════╦═════════╣");
            System.out.printf("║ %-2s ║ %-8s ║ %-26s ║ %-14s ║ %-7s ║%n",
                    "STT", "Mã đơn", "Khách hàng", "Trạng thái", "Số hoa");
            System.out.println("╠════╬══════════╬════════════════════════════╬════════════════╬═════════╣");

            int sttDon = 1;
            for (Order o : dsDon) {
                System.out.printf("║ %-2d ║ %-8s ║ %-26s ║ %-14s ║ %-7d ║%n",
                        sttDon++,
                        o.getOrderId(),
                        catChuoi(layTenKhach(o.getCustomerId()), 26),
                        catChuoi(o.getOrderStatus(), 14),
                        demSoLoaiHoaTrongDon(o.getOrderId()));
            }
            System.out.println("╚════╩══════════╩════════════════════════════╩════════════════╩═════════╝");
            System.out.println("  0. Hủy thao tác");

            System.out.print("\nNhập STT đơn muốn thêm hoa (0 để hủy): ");
            int chonDon;
            try {
                chonDon = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("❌ Phải nhập số!");
                return;
            }
            if (chonDon == 0) {
                System.out.println("→ Đã hủy.");
                return;
            }
            if (chonDon < 1 || chonDon > dsDon.size()) {
                System.out.println("❌ STT không hợp lệ!");
                return;
            }

            Order donChon = dsDon.get(chonDon - 1);
            String orderId = donChon.getOrderId();
            System.out.println("→ Bạn chọn đơn: " + orderId
                    + " (khách: " + layTenKhach(donChon.getCustomerId()) + ")");

            // =====================================================
            // BƯỚC B: VÒNG LẶP THÊM NHIỀU HOA
            // =====================================================
            int soLanThem = 0;

            while (true) {
                System.out.println("\n┌──────────────────────────────────────────────────┐");
                System.out.println("│   THÊM HOA VÀO ĐƠN " + orderId + " (lần " + (soLanThem + 1) + ")");
                System.out.println("└──────────────────────────────────────────────────┘");

                // ==== LẤY HOA CÒN HÀNG ====
                List<Flower> dsHoa = new ArrayList<>();
                for (Flower f : flowerDAO.findAll()) {
                    if (f.getStockQuantity() > 0)
                        dsHoa.add(f);
                }
                dsHoa.sort((a, b) -> a.getFlowerId().compareTo(b.getFlowerId()));

                if (dsHoa.isEmpty()) {
                    System.out.println("⚠️ Không còn hoa nào trong kho!");
                    break;
                }

                // ==== IN BẢNG HOA ====
                System.out.println("╔════╦═════════╦════════════════════════════╦══════════════╦═════════╗");
                System.out.printf("║ %-2s ║ %-7s ║ %-26s ║ %-12s ║ %-7s ║%n",
                        "STT", "Mã hoa", "Tên hoa", "Đơn giá", "Tồn");
                System.out.println("╠════╬═════════╬════════════════════════════╬══════════════╬═════════╣");

                int sttHoa = 1;
                for (Flower f : dsHoa) {
                    System.out.printf("║ %-2d ║ %-7s ║ %-26s ║ %-12s ║ %-7d ║%n",
                            sttHoa++,
                            f.getFlowerId(),
                            catChuoi(f.getName(), 26),
                            f.getUnitPrice() + "đ",
                            f.getStockQuantity());
                }
                System.out.println("╚════╩═════════╩════════════════════════════╩══════════════╩═════════╝");
                System.out.println("  0. Thoát chế độ thêm hoa");

                // ==== CHỌN HOA ====
                System.out.print("\nNhập STT hoa muốn thêm (0 để thoát): ");
                int chonHoa;
                try {
                    chonHoa = Integer.parseInt(sc.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("❌ Phải nhập số!");
                    continue;
                }

                // Bấm 0 → thoát vòng lặp
                if (chonHoa == 0) {
                    System.out.println("→ Đã thoát chế độ thêm hoa.");
                    break;
                }

                if (chonHoa < 1 || chonHoa > dsHoa.size()) {
                    System.out.println("❌ STT không hợp lệ!");
                    continue;
                }

                Flower hoaChon = dsHoa.get(chonHoa - 1);
                System.out.println("→ Bạn chọn: " + hoaChon.getName()
                        + " (tồn: " + hoaChon.getStockQuantity() + ")");

                // ==== NHẬP SỐ LƯỢNG ====
                int quantity = -1;
                boolean huyHoa = false;

                while (true) {
                    System.out.print("Số lượng (tối đa " + hoaChon.getStockQuantity() + ", 0 để hủy): ");
                    try {
                        int sl = Integer.parseInt(sc.nextLine().trim());
                        if (sl == 0) {
                            huyHoa = true;
                            break;
                        }
                        if (sl < 0) {
                            System.out.println("❌ Phải >= 0!");
                            continue;
                        }
                        if (sl > hoaChon.getStockQuantity()) {
                            System.out.println("❌ Vượt tồn kho! Chỉ còn " + hoaChon.getStockQuantity() + ".");
                            continue;
                        }
                        quantity = sl;
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("❌ Phải nhập số nguyên!");
                    }
                }

                if (huyHoa) {
                    System.out.println("→ Đã hủy chọn hoa này, quay lại danh sách.");
                    continue;
                }

                // ==== ĐƠN GIÁ ====
                BigDecimal price = hoaChon.getUnitPrice();

                // ==== THÊM VÀO ĐƠN ====
                String flowerId = hoaChon.getFlowerId();
                boolean ok;

                if (dao.hoaDaCoTrongDon(orderId, flowerId)) {
                    System.out.println("⚠️ Hoa đã có trong đơn, cộng thêm số lượng...");
                    ok = dao.congThemSoLuong(orderId, flowerId, quantity);
                } else {
                    ok = dao.themChiTiet(orderId, flowerId, quantity, price);
                }

                if (ok) {
                    soLanThem++;
                    dao.truSoLuong(flowerId, quantity);
                    System.out.println("✅ Đã thêm " + hoaChon.getName()
                            + " x" + quantity + " vào đơn!");
                } else {
                    System.out.println("❌ Thêm thất bại!");
                }

                // ==== HỎI CÓ THÊM TIẾP KHÔNG ====
                System.out.print("\nThêm hoa khác vào đơn? (c/K, Enter = c): ");
                String tiepTuc = sc.nextLine().trim();
                if (tiepTuc.equalsIgnoreCase("k")) {
                    System.out.println("→ Đã thoát chế độ thêm hoa.");
                    break;
                }
            }

            System.out.println("\n✅ Đã thêm " + soLanThem + " loại hoa vào đơn " + orderId);

        } catch (Exception e) {
            System.out.println("❌ Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // =====================================================
    // 3. THANH TOÁN ĐƠN HÀNG
    // =====================================================
    private void thanhToanDon() {
        try {
            System.out.println("\n--- THANH TOÁN ĐƠN HÀNG ---");

            System.out.print("Mã đơn hàng: ");
            String orderId = sc.nextLine().trim();

            if (orderId.isEmpty()) {
                System.out.println("❌ Mã đơn không được để trống!");
                return;
            }
            if (!dao.donTonTai(orderId)) {
                System.out.println("❌ Đơn '" + orderId + "' không tồn tại!");
                return;
            }

            BigDecimal tongTien = paymentDAO.tinhTongTienDon(orderId);
            System.out.println("Tổng tiền đơn: " + tongTien + " VND");

            if (tongTien.compareTo(BigDecimal.ZERO) == 0) {
                System.out.println("⚠️ Đơn chưa có chi tiết hoa, không thể thanh toán!");
                return;
            }

            System.out.print("Số tiền thanh toán (Enter = thanh toán hết): ");
            String s = sc.nextLine().trim();
            BigDecimal amount;
            try {
                amount = s.isEmpty() ? tongTien : new BigDecimal(s);
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    System.out.println("❌ Số tiền phải > 0!");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Số tiền không hợp lệ!");
                return;
            }

            System.out.print("Phương thức (TIEN_MAT/CHUYEN_KHOAN/MOMO/VNPAY): ");
            String method = sc.nextLine().trim().toUpperCase();
            if (method.isEmpty())
                method = "TIEN_MAT";

            Payment p = new Payment();
            p.setPaymentId(paymentDAO.sinhMaThanhToan());
            p.setOrderId(orderId);
            p.setAmount(amount);
            p.setPaymentMethod(method);
            p.setPaymentDate(LocalDateTime.now());
            p.setPaymentStatus("THANH_TOAN_THANH_CONG");

            if (paymentDAO.taoThanhToan(p)) {
                System.out.println("✅ Thanh toán thành công! Mã GD: " + p.getPaymentId());
            } else {
                System.out.println("❌ Thanh toán thất bại!");
            }
        } catch (Exception e) {
            System.out.println("❌ Lỗi khi thanh toán: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // =====================================================
    // 17. IN ĐƠN HÀNG ĐẦY ĐỦ (thông tin + chi tiết + tổng tiền)
    // =====================================================
    private void inDonHangDayDu(String orderId) {
        try {
            // ----- Phần 1: Thông tin đơn hàng -----
            List<Object[]> info = dao.thongTinDayDuDonHang(orderId);

            if (info.isEmpty()) {
                System.out.println("❌ Không tìm thấy đơn '" + orderId + "'!");
                return;
            }

            Object[] row = info.get(0);
            System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║              ĐƠN HÀNG: " + orderId);
            System.out.println("╠══════════════════════════════════════════════════════════════╣");
            System.out.println("║ Khách hàng:   " + row[1] + " (" + row[2] + ")");
            System.out.println("║ Nhân viên:    " + row[3]);
            System.out.println("║ Ngày đặt:     " + row[4]);
            System.out.println("║ Ngày giao:    " + row[5]);
            System.out.println("║ Địa chỉ:      " + row[6]);
            System.out.println("║ Trạng thái:   " + row[7]);
            System.out.println("╚══════════════════════════════════════════════════════════════╝");

            // ----- Phần 2: Chi tiết hoa -----
            inChiTietDon(orderId);

        } catch (Exception e) {
            System.out.println("❌ Lỗi khi in đơn hàng: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // =====================================================
    // IN CHI TIẾT HOA CỦA ĐƠN
    // =====================================================
    private void inChiTietDon(String orderId) {
        try {
            List<Object[]> chiTiet = dao.chiTietDonHang(orderId);

            if (chiTiet == null || chiTiet.isEmpty()) {
                System.out.println("⚠️ Đơn '" + orderId + "' chưa có chi tiết hoa nào!");
                System.out.println("→ Vào chức năng [2] để thêm hoa vào đơn.");
                return;
            }

            System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║           CHI TIẾT ĐƠN HÀNG: " + orderId);
            System.out.println("╚══════════════════════════════════════════════════════════════╝");

            System.out.printf("%-3s | %-35s | %-6s | %-12s | %-15s%n",
                    "STT", "Tên hoa", "SL", "Đơn giá", "Thành tiền");
            System.out.println("-".repeat(85));

            BigDecimal tongTien = BigDecimal.ZERO;
            int stt = 1;

            for (Object[] row : chiTiet) {
                String tenHoa = String.valueOf(row[0]);
                int soLuong = ((Number) row[1]).intValue();
                BigDecimal donGia = (BigDecimal) row[2];
                BigDecimal thanhTien = donGia.multiply(BigDecimal.valueOf(soLuong));

                tongTien = tongTien.add(thanhTien);

                System.out.printf("%-3d | %-35s | %-6d | %-12s | %-15s%n",
                        stt++, tenHoa, soLuong, donGia, thanhTien);
            }

            System.out.println("-".repeat(85));
            System.out.printf("%-57s TỔNG TIỀN: %s VND%n", "", tongTien);

        } catch (Exception e) {
            System.out.println("❌ Lỗi khi in chi tiết: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // =====================================================
    // 4. CẬP NHẬT TRẠNG THÁI
    // =====================================================
    private void capNhatTrangThai() {
        try {
            System.out.print("Mã đơn hàng: ");
            String orderId = sc.nextLine().trim();

            if (orderId.isEmpty()) {
                System.out.println("❌ Mã đơn không được để trống!");
                return;
            }
            if (!dao.donTonTai(orderId)) {
                System.out.println("❌ Đơn '" + orderId + "' không tồn tại!");
                return;
            }

            System.out.println("Trạng thái mới:");
            System.out.println("  1. CHO_XAC_NHAN");
            System.out.println("  2. DANG_XU_LY");
            System.out.println("  3. DANG_GIAO");
            System.out.println("  4. HOAN_THANH");
            System.out.println("  5. DA_HUY");
            System.out.print("Chọn: ");

            String status;
            try {
                int c = Integer.parseInt(sc.nextLine().trim());
                switch (c) {
                    case 1:
                        status = "CHO_XAC_NHAN";
                        break;
                    case 2:
                        status = "DANG_XU_LY";
                        break;
                    case 3:
                        status = "DANG_GIAO";
                        break;
                    case 4:
                        status = "HOAN_THANH";
                        break;
                    case 5:
                        status = "DA_HUY";
                        break;
                    default:
                        System.out.println("⚠️ Chọn sai!");
                        return;
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Phải nhập số!");
                return;
            }

            if (dao.capNhatTrangThai(orderId, status)) {
                System.out.println("✅ Cập nhật thành công!");
            } else {
                System.out.println("❌ Cập nhật thất bại!");
            }
        } catch (Exception e) {
            System.out.println("❌ Lỗi khi cập nhật: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // =====================================================
    // 5. XÓA CHI TIẾT HOA KHỎI ĐƠN
    // =====================================================
    private void xoaChiTiet() {
        try {
            System.out.println("\n--- XÓA CHI TIẾT HOA KHỎI ĐƠN ---");

            System.out.print("Mã đơn: ");
            String orderId = sc.nextLine().trim();
            if (orderId.isEmpty()) {
                System.out.println("❌ Mã đơn không được để trống!");
                return;
            }
            if (!dao.donTonTai(orderId)) {
                System.out.println("❌ Đơn '" + orderId + "' không tồn tại!");
                return;
            }
            if (dao.donDaCoThanhToan(orderId)) {
                System.out.println("❌ Đơn đã có thanh toán, không thể xóa!");
                return;
            }
            String trangThai = dao.layTrangThaiDon(orderId);
            if ("HOAN_THANH".equals(trangThai) || "DA_HUY".equals(trangThai)) {
                System.out.println("❌ Đơn đã hoàn thành hoặc đã hủy, không thể xóa hoa!");
                return;
            }
            System.out.print("Mã hoa: ");
            String flowerId = sc.nextLine().trim();
            if (flowerId.isEmpty()) {
                System.out.println("❌ Mã hoa không được để trống!");
                return;
            }

            if (!dao.hoaDaCoTrongDon(orderId, flowerId)) {
                System.out.println("⚠️ Hoa này không có trong đơn!");
                return;
            }

            System.out.print("Xác nhận xóa (y/n): ");
            if (sc.nextLine().equalsIgnoreCase("y")) {
                int soLuong = dao.laySoLuongTrongDon(orderId, flowerId);
                if (dao.xoaChiTiet(orderId, flowerId)) {
                    dao.congTonKho(flowerId, soLuong);
                    System.out.println("✅ Đã xóa chi tiết!");
                } else {
                    System.out.println("❌ Xóa thất bại!");
                }
            } else {
                System.out.println("Đã hủy thao tác.");
            }
        } catch (Exception e) {
            System.out.println("❌ Lỗi khi xóa chi tiết: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // =====================================================
    // 6. XÓA ĐƠN HÀNG
    // =====================================================
    private void xoaDon() {
        try {
            System.out.println("\n--- XÓA ĐƠN HÀNG ---");

            System.out.print("Mã đơn cần xóa: ");
            String orderId = sc.nextLine().trim();

            if (orderId.isEmpty()) {
                System.out.println("❌ Mã đơn không được để trống!");
                return;
            }
            if (!dao.donTonTai(orderId)) {
                System.out.println("❌ Đơn '" + orderId + "' không tồn tại!");
                return;
            }
            if (dao.donDaCoThanhToan(orderId)) {
                System.out.println("❌ Đơn đã có thanh toán, không thể xóa!");
                return;
            }

            System.out.print("Xác nhận xóa đơn " + orderId + " (y/n): ");
            if (sc.nextLine().equalsIgnoreCase("y")) {
                //doc so hoa trong don truoc khi xoa
                List<Object[]> dsHoa = dao.layHoaTrongDon(orderId);
                if (dao.xoaDon(orderId)) {
                    for (Object[] dong : dsHoa) {
                        String maHoa = (String) dong[0];
                        int soLuong = (Integer) dong[1];
                        dao.congTonKho(maHoa, soLuong);
                    }
                    System.out.println("✅ Đã xóa đơn và chi tiết!");
                } else {
                    System.out.println("❌ Xóa thất bại!");
                }

            } else {
                System.out.println("Đã hủy thao tác.");
            }
        } catch (Exception e) {
            System.out.println("❌ Lỗi khi xóa đơn: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // =====================================================
    // HIỂN THỊ DANH SÁCH ĐƠN (List<Order>)
    // =====================================================
    private void inDsDon(List<Order> list) {
        if (list == null || list.isEmpty()) {
            System.out.println("(Không có dữ liệu)");
            return;
        }
        System.out.printf("%-8s | %-8s | %-6s | %-20s | %-14s%n",
                "Mã đơn", "Mã KH", "Mã NV", "Ngày đặt", "Trạng thái");
        System.out.println("-".repeat(75));
        for (Order o : list) {
            System.out.printf("%-8s | %-8s | %-6s | %-20s | %-14s%n",
                    o.getOrderId(),
                    o.getCustomerId(),
                    o.getEmployeeId(),
                    o.getOrderDate(),
                    o.getOrderStatus());
        }
    }

    // =====================================================
    // HELPER — VALIDATE
    // =====================================================
    private boolean khachTonTai(String id) {
        try {
            String sql = "SELECT 1 FROM Customer WHERE customer_id = ?";
            return !DBBase.query(sql, id).isEmpty();
        } catch (Exception e) {
            System.out.println("❌ Lỗi kiểm tra khách: " + e.getMessage());
            return false;
        }
    }

    private boolean nhanVienTonTai(String id) {
        try {
            String sql = "SELECT 1 FROM Employee WHERE employee_id = ?";
            return !DBBase.query(sql, id).isEmpty();
        } catch (Exception e) {
            System.out.println("❌ Lỗi kiểm tra nhân viên: " + e.getMessage());
            return false;
        }
    }

    // =====================================================
    // HELPER — IN BẢNG
    // =====================================================
    private void inBang(List<?> list) {
        try {
            if (list == null || list.isEmpty()) {
                System.out.println("(Không có dữ liệu)");
                return;
            }
            for (Object o : list) {
                if (o instanceof Object[]) {
                    StringBuilder sb = new StringBuilder();
                    for (Object cell : (Object[]) o) {
                        sb.append(cell).append(" | ");
                    }
                    System.out.println(sb);
                } else {
                    System.out.println(o);
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Lỗi khi in dữ liệu: " + e.getMessage());
        }
    }

    // =====================================================
    // HELPER — KHÁCH HÀNG
    // =====================================================

    private String taoKhachHangMoi() {
        String newId = customerDAO.sinhMaKhachMoi();
        System.out.println("\n--- TẠO KHÁCH HÀNG MỚI ---");
        System.out.println("Mã khách tự sinh: " + newId);
        System.out.print("Xác nhận tạo khách mới (y/n): ");
        if (!sc.nextLine().equalsIgnoreCase("y")) {
            System.out.println("→ Đã hủy.");
            return null;
        }
        return nhapThongTinKhach(newId);
    }

    private String nhapThongTinKhach(String customerId) {
        try {
            System.out.println("\n--- NHẬP THÔNG TIN KHÁCH HÀNG ---");
            System.out.println("Mã khách: " + customerId);

            System.out.print("Tên khách (bắt buộc): ");
            String name = sc.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("❌ Tên không được để trống! Hủy tạo khách.");
                return null;
            }

            System.out.print("SĐT: ");
            String phone = sc.nextLine().trim();

            System.out.print("Địa chỉ: ");
            String address = sc.nextLine().trim();

            System.out.print("Email: ");
            String email = sc.nextLine().trim();

            Customer c = new Customer();
            c.setCustomerId(customerId);
            c.setName(name);
            c.setPhone(phone);
            c.setAddress(address);
            c.setEmail(email);

            if (customerDAO.insert(c)) {
                System.out.println("✅ Tạo khách hàng thành công!");
                return customerId;
            } else {
                System.out.println("❌ Tạo khách hàng thất bại!");
                return null;
            }
        } catch (Exception e) {
            System.out.println("❌ Lỗi khi tạo khách: " + e.getMessage());
            return null;
        }
    }

    private String layDiaChiKhach(String customerId) {
        try {
            Customer c = customerDAO.findById(customerId);
            if (c != null && c.getAddress() != null && !c.getAddress().isEmpty()) {
                return c.getAddress();
            }
        } catch (Exception e) {
            // bỏ qua
        }
        return "Chưa xác định";
    }

    private String layTenKhach(String customerId) {
        try {
            Customer c = customerDAO.findById(customerId);
            if (c != null && c.getName() != null)
                return c.getName();
        } catch (Exception ignored) {
        }
        return customerId;
    }

    private int demSoLoaiHoaTrongDon(String orderId) {
        try {
            List<Object[]> ct = dao.chiTietDonHang(orderId);
            return ct == null ? 0 : ct.size();
        } catch (Exception e) {
            return 0;
        }
    }

    private String catChuoi(String s, int maxLen) {
        if (s == null)
            return "";
        if (s.length() <= maxLen)
            return s;
        return s.substring(0, maxLen - 1) + "…";
    }

    private Order chonDonTuDanhSach() {
        List<Order> dsDon = dao.findAll();
        if (dsDon.isEmpty()) {
            System.out.println("⚠️ Chưa có đơn hàng nào trong hệ thống!");
            return null;
        }

        System.out.println("\n╔═════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                          DANH SÁCH ĐƠN HÀNG HIỆN CÓ                          ║");
        System.out.println("╠════╦══════════╦════════════════════════════╦═══════════════════╦══════════╣");
        System.out.printf("║ %-2s ║ %-8s ║ %-26s ║ %-17s ║ %-8s ║%n",
                "STT", "Mã đơn", "Khách hàng", "SĐT", "Trạng thái");
        System.out.println("╠════╬══════════╬════════════════════════════╬═══════════════════╬══════════╣");

        int stt = 1;
        for (Order o : dsDon) {
            String tenKH = layTenKhach(o.getCustomerId());
            String sdtKH = laySdtKhach(o.getCustomerId());
            System.out.printf("║ %-2d ║ %-8s ║ %-26s ║ %-17s ║ %-8s ║%n",
                    stt++,
                    o.getOrderId(),
                    catChuoi(tenKH, 26),
                    catChuoi(sdtKH, 17),
                    catChuoi(o.getOrderStatus(), 8));
        }
        System.out.println("╚════╩══════════╩════════════════════════════╩═══════════════════╩══════════╝");
        System.out.println("  0. Hủy thao tác");

        System.out.print("\nNhập STT đơn muốn xem (0 để hủy): ");
        int chon;
        try {
            chon = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("❌ Phải nhập số!");
            return null;
        }

        if (chon == 0) {
            System.out.println("→ Đã hủy.");
            return null;
        }
        if (chon < 1 || chon > dsDon.size()) {
            System.out.println("❌ STT không hợp lệ!");
            return null;
        }

        return dsDon.get(chon - 1);
    }

    // Lấy SĐT khách
    private String laySdtKhach(String customerId) {
        try {
            Customer c = customerDAO.findById(customerId);
            if (c != null && c.getPhone() != null)
                return c.getPhone();
        } catch (Exception ignored) {
        }
        return "";
    }
}