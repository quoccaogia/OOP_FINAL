package vn.edu.crud;

import java.util.Scanner;

import vn.edu.crud.menu.CareScheduleMenu;
import vn.edu.crud.menu.CustomerMenu;
import vn.edu.crud.menu.EmployeeMenu;
import vn.edu.crud.menu.FlowerBatchMenu;
import vn.edu.crud.menu.FlowerMenu;
import vn.edu.crud.menu.OrderMenu;
import vn.edu.crud.menu.PaymentMenu;
import vn.edu.crud.menu.PurchaseOrderMenu;
import vn.edu.crud.menu.SupplierMenu;

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
                case 1 -> new CustomerMenu().hienThi();
                case 2 -> new SupplierMenu().hienThi();
                case 3 -> new EmployeeMenu().hienThi();
                case 4 -> new FlowerMenu().hienThi();
                case 5 -> new OrderMenu().hienThi();
                case 6 -> new PaymentMenu().hienThi();
                case 7 -> new CareScheduleMenu().hienThi();
                case 8 -> new PurchaseOrderMenu().hienThi();
                case 9 -> new FlowerBatchMenu().hienThi();
                case 0 -> System.out.println("Tạm biệt!");
                default -> System.out.println("Chức năng không hợp lệ!");
            }
        } while (chon != 0);
    }
}