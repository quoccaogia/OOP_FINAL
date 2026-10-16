package vn.edu.crud.menu;

import vn.edu.crud.dao.PurchaseOrderDAO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class PurchaseOrderMenu {
    private final Scanner sc = new Scanner(System.in);
    private final PurchaseOrderDAO dao = new PurchaseOrderDAO();
    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void hienThi() {
        int chon;
        do {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║    QUẢN LÝ PHIẾU NHẬP HÀNG           ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  ── THAO TÁC ──                      ║");
            System.out.println("║  1. Xem tất cả                       ║");
            System.out.println("║  2. Tìm theo mã                      ║");
            System.out.println("║  3. Thêm mới                         ║");
            System.out.println("║  4. Sửa                              ║");
            System.out.println("║  5. Xóa                              ║");
            System.out.println("║  ── BÁO CÁO ──                       ║");
            System.out.println("║  6. Tổng tiền nhập theo NCC          ║");
            System.out.println("║  7. Phiếu nhập theo NCC              ║");
            System.out.println("║  8. Tất cả phiếu nhập (kèm tên)      ║");
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
                case 3: themPN(); break;
                case 4: suaPN(); break;
                case 5: xoaPN(); break;
                case 6: inBang(dao.tongNhapTheoNCC()); break;
                case 7:
                    System.out.print("Mã NCC: ");
                    inBang(dao.phieuNhapCuaNCC(sc.nextLine()));
                    break;
                case 8: inBang(dao.tatCaPhieuNhap()); break;
                case 0: break;
                default: System.out.println("Chọn sai!");
            }
        } while (chon != 0);
    }

    private void themPN() {
        String id = dao.sinhMaPNMoi();
        System.out.println("Mã tự sinh: " + id);
        System.out.print("Mã NCC: ");         String supId = sc.nextLine();
        System.out.print("Mã nhân viên: ");   String empId = sc.nextLine();
        System.out.print("Ngày nhập (yyyy-MM-dd HH:mm:ss, Enter = now): ");
        String s = sc.nextLine();
        LocalDateTime date = s.isEmpty() ? LocalDateTime.now() :
                LocalDateTime.parse(s, FMT);
        System.out.print("Tổng tiền: ");      BigDecimal total = new BigDecimal(sc.nextLine());
        System.out.print("Trạng thái (RECEIVED/CANCELLED): ");
        String status = sc.nextLine();
        System.out.print("Ghi chú: ");        String note = sc.nextLine();
        System.out.println(dao.insert(id, supId, empId, date, total, status, note)
                ? "✅ Thêm thành công!" : "❌ Thất bại!");
    }

    private void suaPN() {
        System.out.print("Mã phiếu nhập cần sửa: ");
        String id = sc.nextLine();
        if (dao.findById(id).isEmpty()) { System.out.println("❌ Không tìm thấy!"); return; }
        System.out.print("Tổng tiền mới: "); BigDecimal total = new BigDecimal(sc.nextLine());
        System.out.print("Trạng thái mới: "); String status = sc.nextLine();
        System.out.print("Ghi chú mới: ");    String note = sc.nextLine();
        System.out.println(dao.update(id, total, status, note)
                ? "✅ Sửa thành công!" : "❌ Thất bại!");
    }

    private void xoaPN() {
        System.out.print("Mã phiếu nhập cần xóa: ");
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