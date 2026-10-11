package vn.edu.crud.menu;

import java.util.Scanner;

public class CareScheduleMenu {
    private final Scanner sc;
    public CareScheduleMenu(Scanner sc) { this.sc = sc; }

    public void run() {
        while (true) {
            System.out.println("\n┌─── QUẢN LÝ CHĂM SÓC HOA ──────────┐");
            System.out.println("│  1. Xem tất cả lịch chăm sóc      │");
            System.out.println("│  2. Lịch chăm sóc theo nhân viên  │");
            System.out.println("│  3. Hoa cần chăm sóc hôm nay      │");
            System.out.println("│  0. Quay lại                      │");
            System.out.println("└───────────────────────────────────┘");
            System.out.print("Chọn: ");
            int c = sc.nextInt(); sc.nextLine();
            if (c == 0) return;
            System.out.println("⏳ Chức năng đang phát triển...");
        }
    }
}