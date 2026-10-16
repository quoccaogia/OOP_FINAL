package vn.edu.crud.menu;

import vn.edu.crud.dao.SupplierDAO;

import java.util.List;
import java.util.Scanner;

public class SupplierMenu {
    private final Scanner sc = new Scanner(System.in);
    private final SupplierDAO dao = new SupplierDAO();

    public void hienThi() {
        int chon;
        do {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║      QUẢN LÝ NHÀ CUNG CẤP            ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  ── THAO TÁC ──                      ║");
            System.out.println("║  1. Xem tất cả                       ║");
            System.out.println("║  2. Tìm theo mã                      ║");
            System.out.println("║  3. Thêm mới                         ║");
            System.out.println("║  4. Sửa                              ║");
            System.out.println("║  5. Xóa                              ║");
            System.out.println("║  ── BÁO CÁO ──                       ║");
            System.out.println("║  6. Tổng tiền nhập theo NCC          ║");
            System.out.println("║  7. NCC đang cung cấp hoa            ║");
            System.out.println("║  8. NCC nhiều loại hoa nhất          ║");
            System.out.println("║  9. Thống kê loại hoa theo NCC       ║");
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
                case 3: themNCC(); break;
                case 4: suaNCC(); break;
                case 5: xoaNCC(); break;
                case 6: inBang(dao.tongNhapTheoNCC()); break;
                case 7: inBang(dao.nccDangCungCapHoa()); break;
                case 8: inBang(dao.nccNhieuLoaiHoaNhat()); break;
                case 9: inBang(dao.thongKeLoaiHoaTheoNCC()); break;
                case 0: break;
                default: System.out.println("Chọn sai!");
            }
        } while (chon != 0);
    }

    private void themNCC() {
        String id = dao.sinhMaNCCMoi();
        System.out.println("Mã tự sinh: " + id);
        System.out.print("Tên: ");            String name  = sc.nextLine();
        System.out.print("SĐT: ");            String phone = sc.nextLine();
        System.out.print("Địa chỉ: ");        String addr  = sc.nextLine();
        System.out.print("Người đại diện: "); String rep   = sc.nextLine();
        System.out.println(dao.insert(id, name, phone, addr, rep)
                ? "✅ Thêm thành công!" : "❌ Thất bại!");
    }

    private void suaNCC() {
        System.out.print("Mã NCC cần sửa: ");
        String id = sc.nextLine();
        if (dao.findById(id).isEmpty()) {
            System.out.println("❌ Không tìm thấy!"); return;
        }
        System.out.print("Tên mới: ");            String name  = sc.nextLine();
        System.out.print("SĐT mới: ");            String phone = sc.nextLine();
        System.out.print("Địa chỉ mới: ");        String addr  = sc.nextLine();
        System.out.print("Người đại diện mới: "); String rep   = sc.nextLine();
        System.out.println(dao.update(id, name, phone, addr, rep)
                ? "✅ Sửa thành công!" : "❌ Thất bại!");
    }

    private void xoaNCC() {
        System.out.print("Mã NCC cần xóa: ");
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