package vn.edu.crud.menu;

import java.util.Scanner;

public class EmployeeMenu {
    private final Scanner sc;
    public EmployeeMenu(Scanner sc) { this.sc = sc; }

    public void run() {
        while (true) {
            System.out.println("\n┌─── QUẢN LÝ NHÂN VIÊN ─────────────┐");
            System.out.println("│  1. Xem tất cả nhân viên          │");
            System.out.println("│  2. Tìm NV theo mã                │");
            System.out.println("│  3. NV phụ trách nhiều đơn nhất   │");
            System.out.println("│  0. Quay lại                      │");
            System.out.println("└───────────────────────────────────┘");
            System.out.print("Chọn: ");
            int c = sc.nextInt(); sc.nextLine();
            if (c == 0) return;
            System.out.println("⏳ Chức năng đang phát triển...");
        }
    }
}