package vn.edu.crud.menu;

import vn.edu.crud.dao.CustomerDAO;
import vn.edu.crud.dao.EmployeeDAO;
import vn.edu.crud.dao.FlowerDAO;
import vn.edu.crud.dao.OrderDAO;
import vn.edu.crud.dao.PaymentDAO;
import vn.edu.crud.model.Customer;
import vn.edu.crud.model.Employee;
import vn.edu.crud.model.Flower;
import vn.edu.crud.model.Order;
import vn.edu.crud.model.Payment;
import vn.edu.crud.util.DBBase;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OrderMenu {

    private final Scanner sc = new Scanner(System.in);
    private final OrderDAO orderDAO = new OrderDAO();
    private final PaymentDAO paymentDAO = new PaymentDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final EmployeeDAO employeeDAO = new EmployeeDAO();
    private final FlowerDAO flowerDAO = new FlowerDAO();

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // =====================================================
    // MENU CHÍNH (Kết hợp cả 2)
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
                System.out.println("║  4. Cập nhật trạng thái đơn          ║");
                System.out.println("║  5. Xóa chi tiết hoa khỏi đơn        ║");
                System.out.println("║  6. Xóa đơn hàng                     ║");
                System.out.println("║  7. Xem chi tiết 1 đơn               ║");
                System.out.println("║  ── BÁO CÁO ──                       ║");
                System.out.println("║  8. Đơn hàng giá trị cao (> 500k)    ║");
                System.out.println("║  9. Đơn chưa giao hôm nay            ║");
                System.out.println("║ 10. Doanh thu hôm nay                ║");
                System.out.println("║ 11. Đơn hàng của 1 khách (Nhập SĐT)  ║");
                System.out.println("║ 12. Thống kê đơn theo trạng thái     ║");
                System.out.println("║ 13. Đơn có nhiều loại hoa (>= 3)     ║");
                System.out.println("║ 14. Khách đã từng đặt hàng           ║");
                System.out.println("║ 15. Khách chưa từng đặt hàng         ║");
                System.out.println("║ 16. Khách nhiều đơn nhất             ║");
                System.out.println("║ 17. NV nhiều đơn nhất                ║");
                System.out.println("║ 18. Tổng chi tiêu theo khách         ║");
                System.out.println("║  0. Quay lại                         ║");
                System.out.println("╚══════════════════════════════════════╝");
                System.out.print("Chọn: ");

                chon = Integer.parseInt(sc.nextLine().trim());

                switch (chon) {
                    case 1: taoDonHang(); break;
                    case 2: themHoaVaoDon(); break;
                    case 3: thanhToanDon(); break;
                    case 4: capNhatTrangThai(); break;
                    case 5: xoaChiTiet(); break;
                    case 6: xoaDon(); break;
                    case 7: xemChiTietDon(); break;
                    case 8: inBang(orderDAO.donHangGiaCao(new BigDecimal("500000"))); break;
                    case 9: inDsDon(orderDAO.donChuaGiaoHomNay()); break;
                    case 10: System.out.println("Doanh thu hôm nay: " + orderDAO.doanhThuHomNay() + " VND"); break;
                    case 11: xemDonCuaKhach(); break;
                    case 12: inBang(orderDAO.thongKeDonTheoTrangThai()); break;
                    case 13: inBang(orderDAO.donCoNhieuLoaiHoa(3)); break;
                    case 14: inBang(orderDAO.khachDaTungDatHang()); break;
                    case 15: inBang(orderDAO.khachChuaTungDatHang()); break;
                    case 16: inBang(orderDAO.khachNhieuDonNhat()); break;
                    case 17: inBang(orderDAO.nhanVienNhieuDonNhat()); break;
                    case 18: inBang(orderDAO.tongChiTieuTheoKhach()); break;
                    case 0: System.out.println("Quay lại menu chính."); break;
                    default: System.out.println("⚠️ Chọn sai, vui lòng chọn lại!");
                }
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Vui lòng nhập SỐ, không nhập chữ!");
                chon = -1;
            } catch (Exception e) {
                System.out.println("❌ Lỗi không mong muốn: " + e.getMessage());
                chon = -1;
            }
        } while (chon != 0);
    }

    // =====================================================
    // 1. TẠO ĐƠN HÀNG (Gộp V1 & V2)
    // =====================================================
    private void taoDonHang() {
        try {
            System.out.println("\n--- TẠO ĐƠN HÀNG MỚI ---");

            Order o = new Order();
            o.setOrderId(orderDAO.sinhMaDonMoi());
            System.out.println("Mã đơn tự sinh: " + o.getOrderId());

            // 1. XỬ LÝ KHÁCH HÀNG BẰNG SĐT (Lấy từ bản 1)
            System.out.print("Nhập SĐT khách hàng (Bấm Enter để tạo khách mới): ");
            String sdtTho = sc.nextLine().trim();
            String sdt = customerDAO.chuanHoaSdt(sdtTho);

            if (!sdt.isEmpty()) {
                List<Customer> dsKH = customerDAO.timTheoSdt(sdt);
                if (dsKH.isEmpty()) {
                    System.out.println("❌ Không tìm thấy khách hàng nào dùng SĐT: " + sdt);
                    System.out.print("👉 Tạo khách hàng mới với SĐT này? (y/n): ");
                    if (sc.nextLine().equalsIgnoreCase("y")) {
                        o.setCustomerId(taoKhachHangNhanh(sdt));
                    } else {
                        System.out.println("Đã hủy tạo đơn hàng.");
                        return;
                    }
                } else {
                    Customer kh = dsKH.get(0);
                    o.setCustomerId(kh.getCustomerId());
                    System.out.println("✅ Khách hàng: " + kh.getName() + " (Mã: " + kh.getCustomerId() + ")");
                }
            } else {
                o.setCustomerId(taoKhachHangNhanh(""));
            }

            if (o.getCustomerId() == null) {
                System.out.println("❌ Tạo đơn hàng thất bại (Chưa có khách hàng).");
                return;
            }

            // 2. CHỌN NHÂN VIÊN TỪ DANH SÁCH (Lấy từ bản 1)
            List<Employee> dsNV = employeeDAO.findAll();
            if (dsNV.isEmpty()) {
                System.out.println("❌ Hệ thống chưa có nhân viên nào!");
                return;
            }
            System.out.println("\n--- CHỌN NHÂN VIÊN PHỤ TRÁCH ---");
            for (int i = 0; i < dsNV.size(); i++) {
                Employee nv = dsNV.get(i);
                System.out.printf("  %d. %-20s (Mã: %s - %s)\n", (i + 1), nv.getName(), nv.getEmployeeId(), nv.getRole());
            }

            String employeeId = null;
            while (employeeId == null) {
                System.out.print("Nhập số chọn nhân viên (1-" + dsNV.size() + "): ");
                try {
                    int chonNV = Integer.parseInt(sc.nextLine());
                    if (chonNV >= 1 && chonNV <= dsNV.size()) {
                        employeeId = dsNV.get(chonNV - 1).getEmployeeId();
                    } else { System.out.println("⚠️ Vui lòng chọn đúng số!"); }
                } catch (NumberFormatException e) { System.out.println("⚠️ Vui lòng nhập số!"); }
            }
            o.setEmployeeId(employeeId);

            // 3. NGÀY ĐẶT & NGÀY GIAO (Từ bản 2)
            o.setOrderDate(LocalDateTime.now());
            System.out.println("Ngày đặt: " + o.getOrderDate().format(FMT));

            System.out.print("Ngày giao (yyyy-MM-dd HH:mm:ss, Enter = +2h): ");
            String dateStr = sc.nextLine().trim();
            if (dateStr.isEmpty()) {
                o.setDeliveryDate(o.getOrderDate().plusHours(2));
                System.out.println("→ Dùng mặc định: " + o.getDeliveryDate().format(FMT));
            } else {
                try {
                    // Cho phép nhập thiếu giây
                    if (dateStr.length() == 16) dateStr += ":00";
                    o.setDeliveryDate(LocalDateTime.parse(dateStr.replace(" ", "T")));
                } catch (DateTimeParseException e) {
                    System.out.println("❌ Ngày giờ không hợp lệ! Lấy mặc định +2h.");
                    o.setDeliveryDate(o.getOrderDate().plusHours(2));
                }
            }

            // 4. ĐỊA CHỈ GIAO (Từ bản 2)
            System.out.print("Địa chỉ giao (Enter = lấy địa chỉ khách): ");
            String addr = sc.nextLine().trim();
            if (addr.isEmpty()) {
                addr = layDiaChiKhach(o.getCustomerId());
                System.out.println("→ Dùng địa chỉ khách: " + addr);
            }
            o.setDeliveryAddress(addr);

            // 5. TRẠNG THÁI MẶC ĐỊNH
            o.setOrderStatus("CHO_XAC_NHAN");

            System.out.println("\n>>> Đang tạo đơn...");
            if (orderDAO.taoDonHang(o)) {
                System.out.println("✅ Tạo đơn thành công! Mã đơn: " + o.getOrderId());
                System.out.println("👉 Hãy vào chức năng [2] để thêm hoa vào đơn.");
            } else {
                System.out.println("❌ Tạo đơn thất bại!");
            }
        } catch (Exception e) {
            System.out.println("❌ Lỗi khi tạo đơn: " + e.getMessage());
        }
    }

    // =====================================================
    // 2. THÊM HOA VÀO ĐƠN (Lấy từ bản 2)
    // =====================================================
    private void themHoaVaoDon() {
        try {
            System.out.println("\n--- THÊM HOA VÀO ĐƠN ---");

            List<Order> dsDon = orderDAO.findAll();
            if (dsDon.isEmpty()) {
                System.out.println("⚠️ Chưa có đơn hàng nào!");
                return;
            }

            System.out.println("\n╔═══════════════════════════════════════════════════════════════════════╗");
            System.out.println("║                     DANH SÁCH ĐƠN HÀNG HIỆN CÓ                        ║");
            System.out.println("╠════╦══════════╦════════════════════════════╦════════════════╦═════════╣");
            System.out.printf("║ %-2s ║ %-8s ║ %-26s ║ %-14s ║ %-7s ║%n", "STT", "Mã đơn", "Khách hàng", "Trạng thái", "Số hoa");
            System.out.println("╠════╬══════════╬════════════════════════════╬════════════════╬═════════╣");

            int sttDon = 1;
            for (Order o : dsDon) {
                System.out.printf("║ %-2d ║ %-8s ║ %-26s ║ %-14s ║ %-7d ║%n",
                        sttDon++, o.getOrderId(), catChuoi(layTenKhach(o.getCustomerId()), 26),
                        catChuoi(o.getOrderStatus(), 14), demSoLoaiHoaTrongDon(o.getOrderId()));
            }
            System.out.println("╚════╩══════════╩════════════════════════════╩════════════════╩═════════╝");
            System.out.println("  0. Hủy thao tác");

            System.out.print("\nNhập STT đơn muốn thêm hoa (0 để hủy): ");
            int chonDon = Integer.parseInt(sc.nextLine().trim());
            if (chonDon == 0) return;
            if (chonDon < 1 || chonDon > dsDon.size()) {
                System.out.println("❌ STT không hợp lệ!");
                return;
            }

            Order donChon = dsDon.get(chonDon - 1);
            String orderId = donChon.getOrderId();

            int soLanThem = 0;
            while (true) {
                List<Flower> dsHoa = new ArrayList<>();
                for (Flower f : flowerDAO.findAll()) {
                    if (f.getStockQuantity() > 0) dsHoa.add(f);
                }
                if (dsHoa.isEmpty()) {
                    System.out.println("⚠️ Không còn hoa nào trong kho!");
                    break;
                }

                System.out.println("\n╔════╦═════════╦════════════════════════════╦══════════════╦═════════╗");
                System.out.printf("║ %-2s ║ %-7s ║ %-26s ║ %-12s ║ %-7s ║%n", "STT", "Mã hoa", "Tên hoa", "Đơn giá", "Tồn");
                System.out.println("╠════╬═════════╬════════════════════════════╬══════════════╬═════════╣");
                int sttHoa = 1;
                for (Flower f : dsHoa) {
                    System.out.printf("║ %-2d ║ %-7s ║ %-26s ║ %-12s ║ %-7d ║%n",
                            sttHoa++, f.getFlowerId(), catChuoi(f.getName(), 26), f.getUnitPrice() + "đ", f.getStockQuantity());
                }
                System.out.println("╚════╩═════════╩════════════════════════════╩══════════════╩═════════╝");
                System.out.println("  0. Thoát chế độ thêm hoa");

                System.out.print("\nNhập STT hoa muốn thêm (0 để thoát): ");
                int chonHoa = Integer.parseInt(sc.nextLine().trim());
                if (chonHoa == 0) break;
                if (chonHoa < 1 || chonHoa > dsHoa.size()) {
                    System.out.println("❌ STT không hợp lệ!"); continue;
                }

                Flower hoaChon = dsHoa.get(chonHoa - 1);
                int quantity = -1;
                while (true) {
                    System.out.print("Số lượng (tối đa " + hoaChon.getStockQuantity() + ", 0 để hủy): ");
                    int sl = Integer.parseInt(sc.nextLine().trim());
                    if (sl == 0) break;
                    if (sl > hoaChon.getStockQuantity()) {
                        System.out.println("❌ Vượt tồn kho!"); continue;
                    }
                    quantity = sl; break;
                }

                if (quantity <= 0) continue;

                String flowerId = hoaChon.getFlowerId();
                boolean ok = orderDAO.hoaDaCoTrongDon(orderId, flowerId)
                        ? orderDAO.congThemSoLuong(orderId, flowerId, quantity)
                        : orderDAO.themChiTiet(orderId, flowerId, quantity, hoaChon.getUnitPrice());

                if (ok) {
                    soLanThem++;
                    // Lưu ý: Cần hàm trừ tồn kho trong FlowerDAO hoặc OrderDAO của bạn
                    // Nếu chưa có, hãy bỏ qua dòng dưới hoặc viết thêm vào DAO
                    // flowerDAO.truSoLuong(flowerId, quantity);
                    System.out.println("✅ Đã thêm " + hoaChon.getName() + " x" + quantity + " vào đơn!");
                } else {
                    System.out.println("❌ Thêm thất bại!");
                }

                System.out.print("\nThêm hoa khác vào đơn? (c/K, Enter = c): ");
                if (sc.nextLine().trim().equalsIgnoreCase("k")) break;
            }
        } catch (Exception e) {
            System.out.println("❌ Lỗi: " + e.getMessage());
        }
    }

    // =====================================================
    // 3. THANH TOÁN ĐƠN HÀNG (Lấy từ bản 2)
    // =====================================================
    private void thanhToanDon() {
        try {
            System.out.println("\n--- THANH TOÁN ĐƠN HÀNG ---");
            System.out.print("Mã đơn hàng: ");
            String orderId = sc.nextLine().trim();

            if (!orderDAO.donTonTai(orderId)) {
                System.out.println("❌ Đơn không tồn tại!"); return;
            }

            BigDecimal tongTien = paymentDAO.tinhTongTienDon(orderId);
            System.out.println("Tổng tiền đơn: " + tongTien + " VND");
            if (tongTien.compareTo(BigDecimal.ZERO) == 0) {
                System.out.println("⚠️ Đơn chưa có chi tiết hoa, không thể thanh toán!"); return;
            }

            System.out.print("Số tiền thanh toán (Enter = thanh toán hết): ");
            String s = sc.nextLine().trim();
            BigDecimal amount = s.isEmpty() ? tongTien : new BigDecimal(s);

            System.out.print("Phương thức (TIEN_MAT/CHUYEN_KHOAN/MOMO): ");
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
        }
    }

    // =====================================================
    // 7. XEM CHI TIẾT ĐƠN (Lấy từ bản 1)
    // =====================================================
    private void xemChiTietDon() {
        System.out.print("Nhập Mã đơn cần xem (VD: ORD001): ");
        String orderId = sc.nextLine().toUpperCase();

        if (!orderDAO.donTonTai(orderId)) {
            System.out.println("❌ Không tìm thấy đơn hàng!"); return;
        }

        List<Object[]> thongTin = orderDAO.thongTinDayDuDonHang(orderId);
        if (!thongTin.isEmpty()) {
            Object[] o = thongTin.get(0);
            System.out.println("\n--- THÔNG TIN ĐƠN HÀNG " + orderId + " ---");
            System.out.println("Khách hàng : " + o[1] + " (SĐT: " + o[2] + ")");
            System.out.println("Nhân viên  : " + o[3]);
            System.out.println("Ngày đặt   : " + o[4]);
            System.out.println("Ngày giao  : " + o[5]);
            System.out.println("Địa chỉ    : " + o[6]);
            System.out.println("Trạng thái : " + o[7]);
        }

        System.out.println("\n--- CHI TIẾT CÁC MẶT HÀNG ---");
        List<Object[]> chiTiet = orderDAO.chiTietDonHang(orderId);
        if (chiTiet.isEmpty()) {
            System.out.println("(Chưa có mặt hàng nào)");
        } else {
            System.out.printf("%-25s | %-10s | %-15s%n", "Tên Hoa", "Số Lượng", "Đơn Giá");
            System.out.println("-".repeat(55));
            for (Object[] row : chiTiet) {
                System.out.printf("%-25s | %-10s | %-15s%n", row[0], row[1], row[2]);
            }
        }
    }

    // =====================================================
    // 4. CẬP NHẬT TRẠNG THÁI (Gộp 2 bản)
    // =====================================================
    private void capNhatTrangThai() {
        System.out.print("Nhập Mã đơn cần cập nhật: ");
        String orderId = sc.nextLine().toUpperCase();

        if (!orderDAO.donTonTai(orderId)) {
            System.out.println("❌ Không tìm thấy đơn!"); return;
        }

        System.out.println("Chọn trạng thái mới:");
        System.out.println("  1. CHO_XAC_NHAN");
        System.out.println("  2. DANG_XU_LY");
        System.out.println("  3. DANG_GIAO");
        System.out.println("  4. HOAN_THANH");
        System.out.println("  5. DA_HUY");
        System.out.print("Nhập số chọn: ");

        String[] statuses = {"CHO_XAC_NHAN", "DANG_XU_LY", "DANG_GIAO", "HOAN_THANH", "DA_HUY"};
        try {
            int chon = Integer.parseInt(sc.nextLine());
            if (chon >= 1 && chon <= 5) {
                if (orderDAO.capNhatTrangThai(orderId, statuses[chon - 1])) {
                    System.out.println("✅ Cập nhật thành công!");
                }
            } else {
                System.out.println("⚠️ Chọn sai!");
            }
        } catch (Exception e) { System.out.println("❌ Lỗi nhập liệu!"); }
    }

    // =====================================================
    // 5. XÓA CHI TIẾT HOA KHỎI ĐƠN (Lấy từ bản 2)
    // =====================================================
    private void xoaChiTiet() {
        try {
            System.out.print("Mã đơn: ");
            String orderId = sc.nextLine().trim();
            if (!orderDAO.donTonTai(orderId)) { System.out.println("❌ Đơn không tồn tại!"); return; }
            if (orderDAO.donDaCoThanhToan(orderId)) { System.out.println("❌ Đơn đã thanh toán, không thể xóa!"); return; }

            System.out.print("Mã hoa cần xóa: ");
            String flowerId = sc.nextLine().trim();
            if (!orderDAO.hoaDaCoTrongDon(orderId, flowerId)) {
                System.out.println("⚠️ Hoa này không có trong đơn!"); return;
            }

            System.out.print("Xác nhận xóa (y/n): ");
            if (sc.nextLine().equalsIgnoreCase("y")) {
                if (orderDAO.xoaChiTiet(orderId, flowerId)) {
                    System.out.println("✅ Đã xóa chi tiết hoa khỏi đơn!");
                } else {
                    System.out.println("❌ Xóa thất bại!");
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Lỗi: " + e.getMessage());
        }
    }

    // =====================================================
    // 6. XÓA ĐƠN HÀNG (Lấy từ bản 2)
    // =====================================================
    private void xoaDon() {
        System.out.print("Nhập Mã đơn cần xóa: ");
        String orderId = sc.nextLine().toUpperCase();

        if (!orderDAO.donTonTai(orderId)) {
            System.out.println("❌ Không tìm thấy đơn!"); return;
        }
        if (orderDAO.donDaCoThanhToan(orderId)) {
            System.out.println("❌ KHÔNG THỂ XÓA! Đơn hàng này đã được thanh toán."); return;
        }

        System.out.print("Bạn có chắc chắn muốn xóa đơn hàng này? (y/n): ");
        if (sc.nextLine().equalsIgnoreCase("y")) {
            if (orderDAO.xoaDon(orderId)) System.out.println("✅ Đã xóa đơn!");
            else System.out.println("❌ Xóa thất bại!");
        }
    }

    // =====================================================
    // HÀM HỖ TRỢ (GOM TỪ CẢ 2 BẢN)
    // =====================================================

    // Tạo khách nhanh bằng SĐT (Bản 1)
    private String taoKhachHangNhanh(String sdtDaNhap) {
        String id = customerDAO.sinhMaKhachMoi();
        System.out.println("\n--- TẠO KHÁCH HÀNG MỚI (Mã: " + id + ") ---");

        String sdt = sdtDaNhap;
        if (sdt == null || sdt.isEmpty()) {
            System.out.print("SĐT khách hàng: ");
            sdt = customerDAO.chuanHoaSdt(sc.nextLine());
        }

        System.out.print("Tên khách hàng: "); String ten = sc.nextLine();
        System.out.print("Địa chỉ: ");        String diaChi = sc.nextLine();
        System.out.print("Email: ");          String email = sc.nextLine();

        Customer khMoi = new Customer(id, ten, sdt, diaChi, email);
        return customerDAO.insert(khMoi) ? id : null;
    }

    // Xem đơn của khách qua SĐT (Bản 1)
    private void xemDonCuaKhach() {
        System.out.print("Nhập SĐT khách hàng: ");
        String sdt = customerDAO.chuanHoaSdt(sc.nextLine());

        List<Customer> dsKH = customerDAO.timTheoSdt(sdt);
        if (dsKH.isEmpty()) {
            System.out.println("❌ Không tìm thấy khách hàng!"); return;
        }

        Customer kh = dsKH.get(0);
        System.out.println("--- ĐƠN HÀNG CỦA: " + kh.getName() + " ---");
        inDsDon(orderDAO.donCuaKhach(kh.getCustomerId()));
    }

    // Lấy thông tin khách (Bản 2)
    private String layDiaChiKhach(String customerId) {
        try {
            Customer c = customerDAO.findById(customerId);
            if (c != null && c.getAddress() != null) return c.getAddress();
        } catch (Exception ignored) {}
        return "Chưa xác định";
    }

    private String layTenKhach(String customerId) {
        try {
            Customer c = customerDAO.findById(customerId);
            if (c != null && c.getName() != null) return c.getName();
        } catch (Exception ignored) {}
        return customerId;
    }

    private int demSoLoaiHoaTrongDon(String orderId) {
        try {
            List<Object[]> ct = orderDAO.chiTietDonHang(orderId);
            return ct == null ? 0 : ct.size();
        } catch (Exception e) { return 0; }
    }

    private String catChuoi(String s, int maxLen) {
        if (s == null) return "";
        if (s.length() <= maxLen) return s;
        return s.substring(0, maxLen - 1) + "…";
    }

    // In danh sách đơn (Bản 1)
    private void inDsDon(List<Order> list) {
        if (list == null || list.isEmpty()) { System.out.println("Không có dữ liệu."); return; }
        System.out.printf("%-10s | %-15s | %-20s | %-15s%n", "Mã Đơn", "Mã KH", "Ngày Đặt", "Trạng Thái");
        System.out.println("-".repeat(70));
        for (Order o : list) {
            System.out.printf("%-10s | %-15s | %-20s | %-15s%n",
                    o.getOrderId(), o.getCustomerId(), o.getOrderDate().toString().replace("T", " "), o.getOrderStatus());
        }
    }

    // In bảng báo cáo (Bản 1)
    private void inBang(List<?> list) {
        if (list == null || list.isEmpty()) { System.out.println("Không có dữ liệu."); return; }
        for (Object o : list) {
            if (o instanceof Object[]) {
                StringBuilder sb = new StringBuilder();
                for (Object cell : (Object[]) o) sb.append(cell).append(" | ");
                System.out.println(sb);
            } else {
                System.out.println(o);
            }
        }
    }
}