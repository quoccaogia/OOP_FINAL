package vn.edu.crud.menu;

import vn.edu.crud.dao.CustomerDAO;
import vn.edu.crud.dao.OrderDAO;
import vn.edu.crud.model.Customer;
import vn.edu.crud.model.Order;

import java.util.List;
import java.util.Scanner;

public class CustomerMenu {
    private final Scanner sc = new Scanner(System.in);
    private final CustomerDAO dao = new CustomerDAO();
    private final OrderDAO orderDAO = new OrderDAO();

    public void hienThi() {
        int chon;
        do {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║          QUẢN LÝ KHÁCH HÀNG          ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  1. Xem tất cả danh sách             ║");
            System.out.println("║  2. Nghiệp vụ (Tiếp đón khách)       ║");
            System.out.println("║  ── BÁO CÁO ──                       ║");
            System.out.println("║  6. Khách đã từng đặt hàng           ║");
            System.out.println("║  7. Khách chưa từng đặt hàng         ║");
            System.out.println("║  8. Khách nhiều đơn nhất             ║");
            System.out.println("║  9. Tổng chi tiêu theo khách         ║");
            System.out.println("║ 10. Đơn hàng của 1 khách (Nhập SĐT)  ║");
            System.out.println("║  0. Quay lại                         ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("Chọn: ");

            try { chon = Integer.parseInt(sc.nextLine()); }
            catch (NumberFormatException e) { chon = -1; }

            switch (chon) {
                case 1: inDanhSach(dao.findAll()); break;
                case 2: tiepDonKhachHang(); break;
                case 6: inBang(orderDAO.khachDaTungDatHang()); break;
                case 7: inBang(orderDAO.khachChuaTungDatHang()); break;
                case 8: inBang(orderDAO.khachNhieuDonNhat()); break;
                case 9: inBang(orderDAO.tongChiTieuTheoKhach()); break;
                case 10: xemDonHangBangSdt(); break;
                case 0: break;
                default: System.out.println("Chọn sai!");
            }
        } while (chon != 0);
    }

    // =====================================================
    // CHỨC NĂNG 2: NGHIỆP VỤ TIẾP ĐÓN KHÁCH HÀNG
    // =====================================================
    private void tiepDonKhachHang() {
        System.out.println("\n--- TIẾP ĐÓN KHÁCH HÀNG ---");
        System.out.print("Nhập SĐT khách hàng: ");
        String sdt = dao.chuanHoaSdt(sc.nextLine());

        List<Customer> ketQua = dao.timTheoSdt(sdt);

        if (ketQua.isEmpty()) {
            System.out.println("❌ Không tìm thấy khách hàng nào dùng SĐT này.");
            System.out.print("👉 Tạo tài khoản khách hàng mới? (y/n): ");
            if (sc.nextLine().equalsIgnoreCase("y")) {
                themKhachMoi(sdt);
            }
        } else {
            System.out.println("✅ Tìm thấy khách hàng:");
            inDanhSach(ketQua);

            Customer kh = ketQua.get(0);
            System.out.print("👉 Bạn có muốn cập nhật (sửa) thông tin khách hàng này không? (y/n): ");

            if (sc.nextLine().equalsIgnoreCase("y")) {
                suaKhach(kh);
            }
        }
    }

    // =====================================================
    // CHỨC NĂNG 10: XEM ĐƠN HÀNG CỦA 1 KHÁCH (CHỈ NHẬP SĐT)
    // =====================================================
    private void xemDonHangBangSdt() {
        System.out.println("\n--- XEM ĐƠN HÀNG THEO SĐT ---");
        System.out.print("Nhập SĐT khách hàng: ");
        String sdt = dao.chuanHoaSdt(sc.nextLine());

        List<Customer> ds = dao.timTheoSdt(sdt);
        if (ds.isEmpty()) {
            System.out.println("❌ Không tìm thấy khách hàng nào dùng SĐT này.");
            return;
        }

        Customer khachChon = ds.get(0);
        System.out.println("✅ Khách hàng: " + khachChon.getName() + " (Mã: " + khachChon.getCustomerId() + ")");
        System.out.println("--- DANH SÁCH ĐƠN HÀNG ---");

        List<Order> donHang = orderDAO.donCuaKhach(khachChon.getCustomerId());
        if (donHang.isEmpty()) {
            System.out.println("Khách hàng này chưa có đơn hàng nào.");
        } else {
            System.out.printf("%-10s | %-20s | %-15s%n", "Mã Đơn", "Ngày Đặt", "Trạng Thái");
            System.out.println("-".repeat(55));
            for (Order o : donHang) {
                System.out.printf("%-10s | %-20s | %-15s%n", o.getOrderId(), o.getOrderDate(), o.getOrderStatus());
            }
        }
    }

    // =====================================================
    // CÁC HÀM HỖ TRỢ
    // =====================================================
    private void themKhachMoi(String sdtDaNhap) {
        String id = dao.sinhMaKhachMoi();
        System.out.println("Mã KH tự sinh: " + id);

        System.out.print("Tên KH: ");
        String ten = sc.nextLine();
        System.out.print("Địa chỉ: ");
        String diaChi = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();

        Customer kh = new Customer(id, ten, sdtDaNhap, diaChi, email);
        if (dao.insert(kh)) System.out.println("✅ Thêm khách hàng thành công!");
    }

    private void suaKhach(Customer kh) {
        System.out.println("\n--- SỬA THÔNG TIN KHÁCH HÀNG ---");
        System.out.print("Tên mới (bỏ trống để giữ nguyên): ");
        String ten = sc.nextLine();
        if (!ten.isEmpty()) kh.setName(ten);

        System.out.print("Địa chỉ mới (bỏ trống để giữ nguyên): ");
        String diaChi = sc.nextLine();
        if (!diaChi.isEmpty()) kh.setAddress(diaChi);

        System.out.print("Email mới (bỏ trống để giữ nguyên): ");
        String email = sc.nextLine();
        if (!email.isEmpty()) kh.setEmail(email);

        if (dao.update(kh)) System.out.println("✅ Sửa thành công!");
        else System.out.println("❌ Sửa thất bại!");
    }

    // =====================================================
    // HELPER IN BẢNG
    // =====================================================
    private void inDanhSach(List<Customer> list) {
        if (list.isEmpty()) { System.out.println("Không có dữ liệu."); return; }
        System.out.printf("%-8s | %-25s | %-15s | %-20s | %-25s%n", "Mã KH", "Tên KH", "SĐT", "Địa Chỉ", "Email");
        System.out.println("-".repeat(100));
        for (Customer c : list) {
            System.out.printf("%-8s | %-25s | %-15s | %-20s | %-25s%n",
                    c.getCustomerId(), c.getName(), c.getPhone(), c.getAddress(), c.getEmail());
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