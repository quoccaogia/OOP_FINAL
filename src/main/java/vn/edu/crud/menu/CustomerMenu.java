package vn.edu.crud.menu;

import vn.edu.crud.dao.CustomerDAO;
import vn.edu.crud.model.Customer;

import java.util.List;
import java.util.Scanner;

public class CustomerMenu {

    private final Scanner sc;
    private final CustomerDAO dao = new CustomerDAO();

    public CustomerMenu(Scanner sc) { this.sc = sc; }

    public void run() {
        while (true) {
            printMenu();
            int c = readInt("Chọn: ");
            switch (c) {
                case 1 -> showAll();
                case 2 -> searchById();
                case 3 -> addCustomer();
                case 4 -> updateCustomer();
                case 5 -> deleteCustomer();
                case 0 -> { return; }
                default -> System.out.println("❌ Lựa chọn không hợp lệ!");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n┌─── QUẢN LÝ KHÁCH HÀNG ────────────────┐");
        System.out.println("│  1. Xem tất cả khách hàng              │");
        System.out.println("│  2. Tìm khách theo mã                  │");
        System.out.println("│  3. Thêm khách hàng                    │");
        System.out.println("│  4. Cập nhật khách hàng                │");
        System.out.println("│  5. Xoá khách hàng                     │");
        System.out.println("│  0. Quay lại                           │");
        System.out.println("└────────────────────────────────────────┘");
    }

    private void showAll() {
        System.out.println("\n=== TẤT CẢ KHÁCH HÀNG ===");
        printList(dao.findAll());
    }

    private void searchById() {
        System.out.print("Nhập mã khách: ");
        String id = sc.nextLine();
        Customer c = dao.findById(id);
        if (c == null) System.out.println("❌ Không tìm thấy!");
        else printOne(c);
    }

    private void addCustomer() {
        System.out.println("\n=== THÊM KHÁCH HÀNG ===");
        Customer c = new Customer();
        System.out.print("Mã KH: ");     c.setCustomerId(sc.nextLine());
        System.out.print("Tên: ");       c.setName(sc.nextLine());
        System.out.print("SĐT: ");       c.setPhone(sc.nextLine());
        System.out.print("Địa chỉ: ");   c.setAddress(sc.nextLine());
        System.out.print("Email: ");     c.setEmail(sc.nextLine());

        System.out.println(dao.insert(c) ? "✅ Thêm thành công!" : "❌ Thất bại!");
    }

    private void updateCustomer() {
        System.out.print("Nhập mã khách cần sửa: ");
        String id = sc.nextLine();
        Customer c = dao.findById(id);
        if (c == null) { System.out.println("❌ Không tìm thấy!"); return; }

        System.out.print("Tên mới (" + c.getName() + "): ");
        String v = sc.nextLine(); if (!v.isEmpty()) c.setName(v);
        System.out.print("SĐT mới (" + c.getPhone() + "): ");
        v = sc.nextLine(); if (!v.isEmpty()) c.setPhone(v);
        System.out.print("Địa chỉ mới (" + c.getAddress() + "): ");
        v = sc.nextLine(); if (!v.isEmpty()) c.setAddress(v);
        System.out.print("Email mới (" + c.getEmail() + "): ");
        v = sc.nextLine(); if (!v.isEmpty()) c.setEmail(v);

        System.out.println(dao.update(c) ? "✅ Cập nhật OK!" : "❌ Thất bại!");
    }

    private void deleteCustomer() {
        System.out.print("Nhập mã khách cần xoá: ");
        String id = sc.nextLine();
        System.out.print("Xác nhận xoá (y/n): ");
        if (!sc.nextLine().equalsIgnoreCase("y")) return;

        System.out.println(dao.delete(id) ? "✅ Xoá OK!" : "❌ Thất bại!");
    }

    private void printList(List<Customer> list) {
        if (list.isEmpty()) { System.out.println("  (Trống)"); return; }
        System.out.printf("  %-8s | %-25s | %-15s | %-25s%n",
                "Mã", "Tên", "SĐT", "Địa chỉ");
        System.out.println("  " + "─".repeat(80));
        for (Customer c : list) printOne(c);
        System.out.println("→ Tổng: " + list.size());
    }

    private void printOne(Customer c) {
        System.out.printf("  %-8s | %-25s | %-15s | %-25s%n",
                c.getCustomerId(), c.getName(),
                c.getPhone(), c.getAddress());
    }

    private int readInt(String prompt) {
        System.out.print(prompt);
        while (!sc.hasNextInt()) sc.next();
        int n = sc.nextInt(); sc.nextLine();
        return n;
    }
}