package vn.edu.crud.menu;

import java.util.Scanner;

public class SupplierMenu {
    private final Scanner sc;
    public SupplierMenu(Scanner sc) { this.sc = sc; }

    public void run() {
        while (true) {
            System.out.println("\n┌─── QUẢN LÝ NHÀ CUNG CẤP ──────────┐");
            System.out.println("│  1. Xem tất cả NCC                │");
            System.out.println("│  2. Tìm NCC theo mã               │");
            System.out.println("│  3. NCC cung cấp nhiều hoa nhất   │");
            System.out.println("│  0. Quay lại                      │");
            System.out.println("└───────────────────────────────────┘");
            System.out.print("Chọn: ");
            int c = sc.nextInt(); sc.nextLine();
            if (c == 0) return;
            System.out.println("⏳ Chức năng đang phát triển...");
        }
    }
}