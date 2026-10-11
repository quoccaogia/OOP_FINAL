package vn.edu.crud.menu;

import java.util.Scanner;

public class OrderMenu {
    private final Scanner sc;
    public OrderMenu(Scanner sc) { this.sc = sc; }

    public void run() {
        while (true) {
            System.out.println("\n┌─── QUẢN LÝ ĐƠN HÀNG ──────────────┐");
            System.out.println("│  1. Xem tất cả đơn hàng           │");
            System.out.println("│  2. Tìm đơn theo mã               │");
            System.out.println("│  3. Đơn hàng giá trị cao (>500k)  │");
            System.out.println("│  4. Đơn chưa giao hôm nay         │");
            System.out.println("│  5. Chi tiết 1 đơn hàng           │");
            System.out.println("│  0. Quay lại                      │");
            System.out.println("└───────────────────────────────────┘");
            System.out.print("Chọn: ");
            int c = sc.nextInt(); sc.nextLine();
            if (c == 0) return;
            System.out.println("⏳ Chức năng đang phát triển...");
        }
    }
}