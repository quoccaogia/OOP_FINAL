package vn.edu.crud.menu;

import vn.edu.crud.dao.CustomerDAO;
import vn.edu.crud.dao.OrderDAO;
import vn.edu.crud.model.Customer;

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
            System.out.println("║       QUẢN LÝ KHÁCH HÀNG             ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  ── THAO TÁC ──                      ║");
            System.out.println("║  1. Xem tất cả                       ║");
            System.out.println("║  2. Tìm theo mã                      ║");
            System.out.println("║  3. Thêm mới                         ║");
            System.out.println("║  4. Sửa                              ║");
            System.out.println("║  5. Xóa                              ║");
            System.out.println("║  ── BÁO CÁO ──                       ║");
            System.out.println("║  6. Khách đã từng đặt hàng           ║");
            System.out.println("║  7. Khách chưa từng đặt hàng         ║");
            System.out.println("║  8. Khách nhiều đơn nhất             ║");
            System.out.println("║  9. Tổng chi tiêu theo khách         ║");
            System.out.println("║ 10. Đơn hàng của 1 khách             ║");
            System.out.println("║  0. Quay lại                         ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("Chọn: ");

            try { chon = Integer.parseInt(sc.nextLine()); }
            catch (NumberFormatException e) { chon = -1; }

            switch (chon) {
                case 1: inDsKhach(dao.findAll()); break;
                case 2:
                    System.out.print("Nhập mã: ");
                    Customer c = dao.findById(sc.nextLine());
                    System.out.println(c != null ? c : "Không tìm thấy!");
                    break;
                case 3: themKhach(); break;
                case 4: suaKhach(); break;
                case 5: xoaKhach(); break;
                case 6: inBang(orderDAO.khachDaTungDatHang()); break;
                case 7: inBang(orderDAO.khachChuaTungDatHang()); break;
                case 8: inBang(orderDAO.khachNhieuDonNhat()); break;
                case 9: inBang(orderDAO.tongChiTieuTheoKhach()); break;
                case 10:
                    System.out.print("Nhập mã khách: ");
                    inBang(orderDAO.donCuaKhach(sc.nextLine()));
                    break;
                case 0: break;
                default: System.out.println("Chọn sai!");
            }
        } while (chon != 0);
    }

    private void themKhach() {
        Customer c = new Customer();
        c.setCustomerId(dao.sinhMaKhachMoi());
        System.out.println("Mã tự sinh: " + c.getCustomerId());
        System.out.print("Tên: ");     c.setName(sc.nextLine());
        System.out.print("SĐT: ");     c.setPhone(sc.nextLine());
        System.out.print("Địa chỉ: "); c.setAddress(sc.nextLine());
        System.out.print("Email: ");   c.setEmail(sc.nextLine());
        System.out.println(dao.insert(c) ? "✅ Thêm thành công!" : "❌ Thất bại!");
    }

    private void suaKhach() {
        System.out.print("Mã KH cần sửa: ");
        Customer c = dao.findById(sc.nextLine());
        if (c == null) { System.out.println("❌ Không tìm thấy!"); return; }

        System.out.print("Tên mới (" + c.getName() + "): ");
        String s = sc.nextLine(); if (!s.isEmpty()) c.setName(s);

        System.out.print("SĐT mới (" + c.getPhone() + "): ");
        s = sc.nextLine(); if (!s.isEmpty()) c.setPhone(s);

        System.out.print("Địa chỉ mới (" + c.getAddress() + "): ");
        s = sc.nextLine(); if (!s.isEmpty()) c.setAddress(s);

        System.out.print("Email mới (" + c.getEmail() + "): ");
        s = sc.nextLine(); if (!s.isEmpty()) c.setEmail(s);

        System.out.println(dao.update(c) ? "✅ Sửa thành công!" : "❌ Thất bại!");
    }

    private void xoaKhach() {
        System.out.print("Mã KH cần xóa: ");
        String id = sc.nextLine();
        System.out.print("Xác nhận (y/n): ");
        if (sc.nextLine().equalsIgnoreCase("y")) {
            System.out.println(dao.delete(id) ? "✅ Đã xóa!" : "❌ Thất bại!");
        }
    }

    private void inDsKhach(List<Customer> list) {
        if (list.isEmpty()) { System.out.println("Không có dữ liệu."); return; }
        System.out.printf("%-8s | %-25s | %-15s | %-30s | %-25s%n",
                "Mã", "Tên", "SĐT", "Địa chỉ", "Email");
        System.out.println("-".repeat(110));
        for (Customer c : list) {
            System.out.printf("%-8s | %-25s | %-15s | %-30s | %-25s%n",
                    c.getCustomerId(), c.getName(), c.getPhone(),
                    c.getAddress(), c.getEmail());
        }
    }

    private void inBang(List<?> list) {
        if (list.isEmpty()) { System.out.println("Không có dữ liệu."); return; }
        for (Object o : list) {
            if (o instanceof Object[]) {
                StringBuilder sb = new StringBuilder();
                for (Object cell : (Object[]) o) sb.append(cell).append(" | ");
                System.out.println(sb);
            } else System.out.println(o);
        }
    }
}