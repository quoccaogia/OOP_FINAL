package vn.edu.crud;

import vn.edu.crud.menu.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int chon;

        do {
            System.out.println("\n╔══════════════════════════════════════════════╗");
            System.out.println("║   HỆ THỐNG QUẢN LÝ CỬA HÀNG BÁN HOA         ║");
            System.out.println("╠══════════════════════════════════════════════╣");
            System.out.println("║  1. Quản lý Khách hàng                       ║");
            System.out.println("║  2. Quản lý Nhà cung cấp                     ║");
            System.out.println("║  3. Quản lý Nhân viên                        ║");
            System.out.println("║  4. Quản lý Hoa                              ║");
            System.out.println("║  5. Quản lý Đơn hàng                         ║");
            System.out.println("║  6. Quản lý Thanh toán                       ║");
            System.out.println("║  7. Quản lý Lịch chăm sóc                    ║");
            System.out.println("║  8. Quản lý Phiếu nhập hàng                  ║");
            System.out.println("║  9. Quản lý Lô hoa                           ║");
            System.out.println("║  0. Thoát                                    ║");
            System.out.println("╚══════════════════════════════════════════════╝");
            System.out.print("Chọn chức năng: ");

            try { chon = Integer.parseInt(sc.nextLine()); }
            catch (NumberFormatException e) { chon = -1; }

            switch (chon) {
                case 1: new CustomerMenu().hienThi(); break;
                case 2: new SupplierMenu().hienThi(); break;
                case 3: new EmployeeMenu().hienThi(); break;
                case 4: new FlowerMenu().hienThi(); break;
                case 5: new OrderMenu().hienThi(); break;
                case 6: new PaymentMenu().hienThi(); break;
                case 7: new CareScheduleMenu().hienThi(); break;
                case 8: new PurchaseOrderMenu().hienThi(); break;
                case 9: new FlowerBatchMenu().hienThi(); break;
                case 0: System.out.println("Tạm biệt!"); break;
                default: System.out.println("Chức năng không hợp lệ!");
            }
        } while (chon != 0);
    }
}