package vn.edu.crud.menu;

import vn.edu.crud.dao.CustomerDAO;
import vn.edu.crud.dao.OrderDAO;
import vn.edu.crud.dao.PaymentDAO;
import vn.edu.crud.model.Customer;
import vn.edu.crud.model.Order;
import vn.edu.crud.model.Payment;
import vn.edu.crud.util.DBBase;

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

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
                    case 1:  taoDonHang(); break;
                    case 2:  themHoaVaoDon(); break;
                    case 3:  thanhToanDon(); break;
                    case 4:  capNhatTrangThai(); break;
                    case 5:  xoaChiTiet(); break;
                    case 6:  xoaDon(); break;
                    case 7:  inBang(dao.donHangGiaCao(new BigDecimal("500000"))); break;
                    case 8:  inDsDon(dao.donChuaGiaoHomNay()); break;
                    case 9:  System.out.println("Doanh thu hôm nay: " + dao.doanhThuHomNay() + " VND"); break;
                    case 10: inBang(dao.khachDaTungDatHang()); break;
                    case 11: inBang(dao.khachChuaTungDatHang()); break;
                    case 12: inBang(dao.khachNhieuDonNhat()); break;
                    case 13: inBang(dao.nhanVienCoDonHang()); break;
                    case 14: inBang(dao.nhanVienNhieuDonNhat()); break;
                    case 15: inBang(dao.tongChiTieuTheoKhach()); break;
                    case 16:
                        System.out.print("Nhập mã khách: ");
                        inDsDon(dao.donCuaKhach(sc.nextLine().trim()));   // ← ĐÃ SỬA
                        break;
                    case 17:
                        System.out.print("Nhập mã đơn: ");
                        inDonHangDayDu(sc.nextLine().trim());
                        break;
                    case 18: inBang(dao.donCoNhieuLoaiHoa(3)); break;
                    case 19: inBang(dao.thongKeDonTheoTrangThai()); break;
                    case 0:  System.out.println("Quay lại menu chính."); break;
                    default: System.out.println("⚠️ Chọn sai, vui lòng chọn lại!");
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
                    if (newId == null) continue;
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

            System.out.print("Mã hoa: ");
            String flowerId = sc.nextLine().trim();
            if (flowerId.isEmpty()) {
                System.out.println("❌ Mã hoa không được để trống!");
                return;
            }

            System.out.print("Số lượng: ");
            int quantity;
            try {
                quantity = Integer.parseInt(sc.nextLine().trim());
                if (quantity <= 0) {
                    System.out.println("❌ Số lượng phải > 0!");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Số lượng phải là SỐ nguyên!");
                return;
            }

            System.out.print("Đơn giá tại thời điểm bán: ");
            BigDecimal price;
            try {
                price = new BigDecimal(sc.nextLine().trim());
                if (price.compareTo(BigDecimal.ZERO) < 0) {
                    System.out.println("❌ Đơn giá không được âm!");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Đơn giá không hợp lệ!");
                return;
            }

            if (dao.hoaDaCoTrongDon(orderId, flowerId)) {
                System.out.println("⚠️ Hoa đã có trong đơn, cộng thêm số lượng...");
                if (dao.congThemSoLuong(orderId, flowerId, quantity)) {
                    System.out.println("✅ Đã cập nhật số lượng!");
                } else {
                    System.out.println("❌ Cập nhật thất bại!");
                }
            } else {
                if (dao.themChiTiet(orderId, flowerId, quantity, price)) {
                    System.out.println("✅ Thêm hoa vào đơn thành công!");
                } else {
                    System.out.println("❌ Thêm thất bại! Kiểm tra mã hoa có tồn tại không.");
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Lỗi khi thêm hoa: " + e.getMessage());
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
            if (method.isEmpty()) method = "TIEN_MAT";

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
                String tenHoa        = String.valueOf(row[0]);
                int soLuong          = ((Number) row[1]).intValue();
                BigDecimal donGia    = (BigDecimal) row[2];
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
                    case 1: status = "CHO_XAC_NHAN"; break;
                    case 2: status = "DANG_XU_LY"; break;
                    case 3: status = "DANG_GIAO"; break;
                    case 4: status = "HOAN_THANH"; break;
                    case 5: status = "DA_HUY"; break;
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
                if (dao.xoaChiTiet(orderId, flowerId)) {
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
                if (dao.xoaDon(orderId)) {
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
}