package vn.edu.crud.menu;

import vn.edu.crud.dao.CustomerDAO;
import vn.edu.crud.dao.EmployeeDAO;
import vn.edu.crud.dao.FlowerDAO;
import vn.edu.crud.dao.OrderDAO;
import vn.edu.crud.model.Customer;
import vn.edu.crud.model.Employee;
import vn.edu.crud.model.Flower;
import vn.edu.crud.model.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class OrderMenu {
    private final Scanner sc = new Scanner(System.in);
    private final OrderDAO orderDAO = new OrderDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final EmployeeDAO employeeDAO = new EmployeeDAO();
    private final FlowerDAO flowerDAO = new FlowerDAO();

    public void hienThi() {
        int chon;
        do {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║            QUẢN LÝ ĐƠN HÀNG          ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  ── THAO TÁC ──                      ║");
            System.out.println("║  1. Xem tất cả đơn hàng              ║");
            System.out.println("║  2. Tạo đơn hàng mới                 ║");
            System.out.println("║  3. Xem chi tiết 1 đơn               ║");
            System.out.println("║  4. Cập nhật trạng thái đơn          ║");
            System.out.println("║  5. Xóa đơn hàng                     ║");
            System.out.println("║  ── BÁO CÁO ──                       ║");
            System.out.println("║  6. Đơn hàng giá trị cao (> 500k)    ║");
            System.out.println("║  7. Đơn chưa giao hôm nay            ║");
            System.out.println("║  8. Doanh thu hôm nay                ║");
            System.out.println("║  9. Đơn hàng của 1 khách (Nhập SĐT)  ║");
            System.out.println("║ 10. Thống kê đơn theo trạng thái     ║");
            System.out.println("║ 11. Đơn có nhiều loại hoa (>= 3)     ║");
            System.out.println("║  0. Quay lại                         ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("Chọn: ");

            try { chon = Integer.parseInt(sc.nextLine()); }
            catch (NumberFormatException e) { chon = -1; }

            switch (chon) {
                case 1: inDanhSachDon(orderDAO.findAll()); break;
                case 2: taoDonHangMoi(); break;
                case 3: xemChiTietDon(); break;
                case 4: capNhatTrangThaiDon(); break;
                case 5: xoaDonHang(); break;
                case 6: inBang(orderDAO.donHangGiaCao(new BigDecimal("500000"))); break;
                case 7: inDanhSachDon(orderDAO.donChuaGiaoHomNay()); break;
                case 8: System.out.println("Doanh thu hôm nay: " + orderDAO.doanhThuHomNay() + " VNĐ"); break;
                case 9: xemDonCuaKhach(); break;
                case 10: inBang(orderDAO.thongKeDonTheoTrangThai()); break;
                case 11: inBang(orderDAO.donCoNhieuLoaiHoa(3)); break;
                case 0: break;
                default: System.out.println("Chọn sai!");
            }
        } while (chon != 0);
    }

    // =====================================================
    // CÁC HÀM THAO TÁC CƠ BẢN
    // =====================================================
    private void xemChiTietDon() {
        System.out.print("Nhập Mã đơn cần xem (VD: ORD001): ");
        String orderId = sc.nextLine().toUpperCase();

        if (!orderDAO.donTonTai(orderId)) {
            System.out.println("❌ Không tìm thấy đơn hàng!");
            return;
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

    private void capNhatTrangThaiDon() {
        System.out.print("Nhập Mã đơn cần cập nhật: ");
        String orderId = sc.nextLine().toUpperCase();

        if (!orderDAO.donTonTai(orderId)) {
            System.out.println("❌ Không tìm thấy đơn!");
            return;
        }

        System.out.println("Chọn trạng thái mới:");
        System.out.println("  1. CHO_XU_LY");
        System.out.println("  2. DANG_GIAO");
        System.out.println("  3. HOAN_THANH");
        System.out.println("  4. DA_HUY");
        System.out.print("Nhập số chọn: ");

        String[] statuses = {"CHO_XU_LY", "DANG_GIAO", "HOAN_THANH", "DA_HUY"};
        try {
            int chon = Integer.parseInt(sc.nextLine());
            if (chon >= 1 && chon <= 4) {
                if (orderDAO.capNhatTrangThai(orderId, statuses[chon - 1])) {
                    System.out.println("✅ Cập nhật thành công!");
                }
            }
        } catch (Exception e) { System.out.println("Lỗi nhập liệu!"); }
    }

    private void xoaDonHang() {
        System.out.print("Nhập Mã đơn cần xóa: ");
        String orderId = sc.nextLine().toUpperCase();

        if (!orderDAO.donTonTai(orderId)) {
            System.out.println("❌ Không tìm thấy đơn!");
            return;
        }

        if (orderDAO.donDaCoThanhToan(orderId)) {
            System.out.println("❌ KHÔNG THỂ XÓA! Đơn hàng này đã được thanh toán (có dữ liệu trong bảng Payment).");
            return;
        }

        System.out.print("Bạn có chắc chắn muốn xóa đơn hàng này? (y/n): ");
        if (sc.nextLine().equalsIgnoreCase("y")) {
            if (orderDAO.xoaDon(orderId)) System.out.println("✅ Đã xóa đơn!");
            else System.out.println("❌ Xóa thất bại!");
        }
    }

    // =====================================================
    // HÀM FULL: TẠO ĐƠN HÀNG MỚI (NÂNG CẤP)
    // =====================================================
    private void taoDonHangMoi() {
        System.out.println("\n--- TẠO ĐƠN HÀNG MỚI ---");
        String orderId = orderDAO.sinhMaDonMoi();
        System.out.println("Mã đơn tự sinh: " + orderId);

        // 1. XỬ LÝ KHÁCH HÀNG BẰNG SĐT
        String customerId = null;
        System.out.print("Nhập SĐT khách hàng (Bấm Enter để tạo khách mới): ");
        String sdtTho = sc.nextLine();
        String sdt = customerDAO.chuanHoaSdt(sdtTho);

        if (!sdt.isEmpty()) {
            List<Customer> dsKH = customerDAO.timTheoSdt(sdt);
            if (dsKH.isEmpty()) {
                System.out.println("❌ Không tìm thấy khách hàng nào dùng SĐT: " + sdt);
                System.out.print("👉 Tạo khách hàng mới với SĐT này? (y/n): ");
                if (sc.nextLine().equalsIgnoreCase("y")) {
                    customerId = taoKhachHangNhanh(sdt);
                } else {
                    System.out.println("Đã hủy tạo đơn hàng.");
                    return;
                }
            } else {
                Customer kh = dsKH.get(0);
                customerId = kh.getCustomerId();
                System.out.println("✅ Khách hàng: " + kh.getName() + " (Mã: " + customerId + ")");
            }
        } else {
            customerId = taoKhachHangNhanh("");
        }

        if (customerId == null) {
            System.out.println("❌ Tạo đơn hàng thất bại (Chưa có khách hàng).");
            return;
        }

        // 2. CHỌN NHÂN VIÊN PHỤ TRÁCH (CHỌN TỪ DANH SÁCH)
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

        // 3. NHẬP THÔNG TIN GIAO HÀNG
        System.out.print("Ngày giao (yyyy-MM-dd HH:mm, Enter để lấy ngày mai): ");
        String dateStr = sc.nextLine();
        LocalDateTime deliveryDate = LocalDateTime.now().plusDays(1);
        if (!dateStr.isEmpty()) {
            try { deliveryDate = LocalDateTime.parse(dateStr.replace(" ", "T")); }
            catch (DateTimeParseException e) { System.out.println("⚠️ Sai định dạng ngày. Lấy ngày mai."); }
        }

        System.out.print("Địa chỉ giao hàng: ");
        String address = sc.nextLine();

        Order order = new Order(orderId, customerId, employeeId, LocalDateTime.now(), deliveryDate, address, "CHO_XU_LY");

        if (!orderDAO.taoDonHang(order)) {
            System.out.println("❌ Tạo đơn thất bại!");
            return;
        }
        System.out.println("✅ Đã tạo đơn hàng gốc. Tiếp tục thêm hoa...");

        // 4. THÊM HOA VÀO ĐƠN
        while (true) {
            System.out.print("\nNhập Mã hoa cần thêm (Enter để kết thúc): ");
            String flowerId = sc.nextLine().toUpperCase();

            if (flowerId.isEmpty()) break;

            Flower flower = flowerDAO.timTheoMa(flowerId);
            if (flower == null) {
                System.out.println("❌ Không tìm thấy hoa!");
                continue;
            }

            System.out.println("👉 " + flower.getName() + " | Giá: " + flower.getUnitPrice());
            System.out.print("Nhập số lượng: ");
            int quantity = 0;
            try {
                quantity = Integer.parseInt(sc.nextLine());
                if (quantity <= 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Số lượng không hợp lệ.");
                continue;
            }

            if (orderDAO.hoaDaCoTrongDon(orderId, flowerId)) {
                orderDAO.congThemSoLuong(orderId, flowerId, quantity);
                System.out.println("✅ Đã cộng thêm " + quantity + " " + flower.getName());
            } else {
                orderDAO.themChiTiet(orderId, flowerId, quantity, flower.getUnitPrice());
                System.out.println("✅ Đã thêm " + quantity + " " + flower.getName());
            }
        }
        System.out.println("\n🎉 TẠO ĐƠN HÀNG THÀNH CÔNG! Mã đơn: " + orderId);
    }

    // =====================================================
    // HÀM HỖ TRỢ KHÁC
    // =====================================================
    private String taoKhachHangNhanh(String sdtDaNhap) {
        String id = customerDAO.sinhMaKhachMoi();
        System.out.println("\n--- TẠO KHÁCH HÀNG MỚI (Mã: " + id + ") ---");

        String sdt = sdtDaNhap;
        if (sdt.isEmpty()) {
            System.out.print("SĐT khách hàng: ");
            sdt = customerDAO.chuanHoaSdt(sc.nextLine());
        }

        System.out.print("Tên khách hàng: "); String ten = sc.nextLine();
        System.out.print("Địa chỉ: ");        String diaChi = sc.nextLine();
        System.out.print("Email: ");          String email = sc.nextLine();

        Customer khMoi = new Customer(id, ten, sdt, diaChi, email);
        return customerDAO.insert(khMoi) ? id : null;
    }

    private void xemDonCuaKhach() {
        System.out.print("Nhập SĐT khách hàng: ");
        String sdt = customerDAO.chuanHoaSdt(sc.nextLine());

        List<Customer> dsKH = customerDAO.timTheoSdt(sdt);
        if (dsKH.isEmpty()) {
            System.out.println("❌ Không tìm thấy khách hàng!");
            return;
        }

        Customer kh = dsKH.get(0);
        System.out.println("--- ĐƠN HÀNG CỦA: " + kh.getName() + " ---");
        inDanhSachDon(orderDAO.donCuaKhach(kh.getCustomerId()));
    }

    private void inDanhSachDon(List<Order> list) {
        if (list.isEmpty()) { System.out.println("Không có dữ liệu."); return; }
        System.out.printf("%-10s | %-15s | %-20s | %-15s%n", "Mã Đơn", "Mã KH", "Ngày Đặt", "Trạng Thái");
        System.out.println("-".repeat(70));
        for (Order o : list) {
            System.out.printf("%-10s | %-15s | %-20s | %-15s%n",
                    o.getOrderId(), o.getCustomerId(), o.getOrderDate().toString().replace("T", " "), o.getOrderStatus());
        }
    }

    private void inBang(List<Object[]> list) {
        if (list.isEmpty()) { System.out.println("Không có dữ liệu."); return; }
        for (Object[] row : list) {
            StringBuilder sb = new StringBuilder();
            for (Object o : row) sb.append(o).append(" | ");
            System.out.println(sb);
        }
    }
}