package vn.edu.crud;

import vn.edu.crud.menu.*;

import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            printMainMenu();
            int choice = readInt("Chọn: ");

            switch (choice) {
                case 1 -> new FlowerMenu(sc).run();       // vào menu hoa
                case 2 -> new CustomerMenu(sc).run();     // vào menu khách
                case 3 -> new OrderMenu(sc).run();        // vào menu đơn
                case 4 -> new SupplierMenu(sc).run();     // vào menu NCC
                case 5 -> new EmployeeMenu(sc).run();     // vào menu NV
                case 6 -> new PaymentMenu(sc).run();      // vào menu thanh toán
                case 7 -> new CareScheduleMenu(sc).run(); // vào menu chăm sóc
                case 0 -> {
                    System.out.println("👋 Tạm biệt!");
                    return;
                }
                default -> System.out.println("❌ Lựa chọn không hợp lệ!");
            }
        }
    }

    private static void printMainMenu() {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║   QUẢN LÝ CỬA HÀNG HOA               ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║  1. Quản lý HOA                      ║");
        System.out.println("║  2. Quản lý KHÁCH HÀNG               ║");
        System.out.println("║  3. Quản lý ĐƠN HÀNG                 ║");
        System.out.println("║  4. Quản lý NHÀ CUNG CẤP             ║");
        System.out.println("║  5. Quản lý NHÂN VIÊN                ║");
        System.out.println("║  6. Quản lý THANH TOÁN               ║");
        System.out.println("║  7. Quản lý CHĂM SÓC HOA             ║");
        System.out.println("║  0. Thoát                            ║");
        System.out.println("╚══════════════════════════════════════╝");
    }

    // Helper dùng chung — có thể đưa vào InputUtil
    static int readInt(String prompt) {
        System.out.print(prompt);
        while (!sc.hasNextInt()) {
            System.out.print("⚠️  Nhập số: ");
            sc.next();
        }
        int n = sc.nextInt();
        sc.nextLine();
        return n;
    }
}