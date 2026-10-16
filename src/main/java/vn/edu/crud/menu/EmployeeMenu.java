package vn.edu.crud.menu;

import vn.edu.crud.dao.EmployeeDAO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class EmployeeMenu {
    private final Scanner sc = new Scanner(System.in);
    private final EmployeeDAO dao = new EmployeeDAO();

    public void hienThi() {
        int chon;
        do {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║        QUẢN LÝ NHÂN VIÊN             ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  ── THAO TÁC ──                      ║");
            System.out.println("║  1. Xem tất cả                       ║");
            System.out.println("║  2. Tìm theo mã                      ║");
            System.out.println("║  3. Thêm mới                         ║");
            System.out.println("║  4. Sửa                              ║");
            System.out.println("║  5. Xóa                              ║");
            System.out.println("║  ── BÁO CÁO ──                       ║");
            System.out.println("║  6. NV đang phụ trách đơn            ║");
            System.out.println("║  7. NV nhiều đơn nhất                ║");
            System.out.println("║  0. Quay lại                         ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("Chọn: ");

            try { chon = Integer.parseInt(sc.nextLine()); }
            catch (NumberFormatException e) { chon = -1; }

            switch (chon) {
                case 1: inBang(dao.findAll()); break;
                case 2:
                    System.out.print("Nhập mã: ");
                    inBang(dao.findById(sc.nextLine()));
                    break;
                case 3: themNV(); break;
                case 4: suaNV(); break;
                case 5: xoaNV(); break;
                case 6: inBang(dao.nhanVienCoDonHang()); break;
                case 7: inBang(dao.nhanVienNhieuDonNhat()); break;
                case 0: break;
                default: System.out.println("Chọn sai!");
            }
        } while (chon != 0);
    }

    private void themNV() {
        String id = dao.sinhMaNVMoi();
        System.out.println("Mã tự sinh: " + id);
        System.out.print("Tên: ");     String name  = sc.nextLine();
        System.out.print("Chức vụ: "); String role  = sc.nextLine();
        System.out.print("SĐT: ");     String phone = sc.nextLine();
        System.out.print("Lương: ");  BigDecimal salary = new BigDecimal(sc.nextLine());
        System.out.println(dao.insert(id, name, role, phone, salary)
                ? "✅ Thêm thành công!" : "❌ Thất bại!");
    }

    private void suaNV() {
        System.out.print("Mã NV cần sửa: ");
        String id = sc.nextLine();
        if (dao.findById(id).isEmpty()) {
            System.out.println("❌ Không tìm thấy!"); return;
        }
        System.out.print("Tên mới: ");     String name  = sc.nextLine();
        System.out.print("Chức vụ mới: "); String role  = sc.nextLine();
        System.out.print("SĐT mới: ");     String phone = sc.nextLine();
        System.out.print("Lương mới: ");  BigDecimal salary = new BigDecimal(sc.nextLine());
        System.out.println(dao.update(id, name, role, phone, salary)
                ? "✅ Sửa thành công!" : "❌ Thất bại!");
    }

    private void xoaNV() {
        System.out.print("Mã NV cần xóa: ");
        String id = sc.nextLine();
        System.out.print("Xác nhận (y/n): ");
        if (sc.nextLine().equalsIgnoreCase("y")) {
            System.out.println(dao.delete(id) ? "✅ Đã xóa!" : "❌ Thất bại!");
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