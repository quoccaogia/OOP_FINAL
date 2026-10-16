package vn.edu.crud.menu;

import vn.edu.crud.dao.FlowerBatchDAO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class FlowerBatchMenu {
    private final Scanner sc = new Scanner(System.in);
    private final FlowerBatchDAO dao = new FlowerBatchDAO();
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter FMT_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void hienThi() {
        int chon;
        do {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║         QUẢN LÝ LÔ HOA               ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  ── THAO TÁC ──                      ║");
            System.out.println("║  1. Xem tất cả                       ║");
            System.out.println("║  2. Tìm theo mã                      ║");
            System.out.println("║  3. Thêm mới                         ║");
            System.out.println("║  4. Cập nhật số lượng còn lại        ║");
            System.out.println("║  5. Cập nhật trạng thái              ║");
            System.out.println("║  6. Xóa                              ║");
            System.out.println("║  ── BÁO CÁO ──                       ║");
            System.out.println("║  7. Lô hoa còn hàng                  ║");
            System.out.println("║  8. Lô hoa sắp hết hạn               ║");
            System.out.println("║  9. Lô hoa theo loại                 ║");
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
                case 3: themLo(); break;
                case 4: capNhatRemaining(); break;
                case 5: capNhatStatus(); break;
                case 6: xoaLo(); break;
                case 7: inBang(dao.loHoaConHang()); break;
                case 8:
                    System.out.print("Số ngày tới hạn: ");
                    inBang(dao.loHoaSapHetHan(Integer.parseInt(sc.nextLine())));
                    break;
                case 9:
                    System.out.print("Mã hoa: ");
                    inBang(dao.loHoaTheoLoai(sc.nextLine()));
                    break;
                case 0: break;
                default: System.out.println("Chọn sai!");
            }
        } while (chon != 0);
    }

    private void themLo() {
        String id = dao.sinhMaLoMoi();
        System.out.println("Mã tự sinh: " + id);
        System.out.print("Mã phiếu nhập: ");        String poId = sc.nextLine();
        System.out.print("Mã hoa: ");               String flowerId = sc.nextLine();
        System.out.print("Số lượng nhập: ");        int imported = Integer.parseInt(sc.nextLine());
        System.out.print("Số lượng còn lại: ");     int remaining = Integer.parseInt(sc.nextLine());
        System.out.print("Đơn giá nhập: ");         BigDecimal cost = new BigDecimal(sc.nextLine());
        System.out.print("Ngày nhập (yyyy-MM-dd HH:mm:ss): ");
        LocalDateTime importDate = LocalDateTime.parse(sc.nextLine(), FMT);
        System.out.print("Hạn dùng (yyyy-MM-dd): ");
        LocalDate expiry = LocalDate.parse(sc.nextLine(), FMT_DATE);
        System.out.print("Trạng thái (AVAILABLE/DEPLETED/EXPIRED/DAMAGED): ");
        String status = sc.nextLine();
        System.out.print("Ghi chú: "); String note = sc.nextLine();
        System.out.println(dao.insert(id, poId, flowerId, imported, remaining,
                cost, importDate, expiry, status, note)
                ? "✅ Thêm thành công!" : "❌ Thất bại!");
    }

    private void capNhatRemaining() {
        System.out.print("Mã lô: ");           String id = sc.nextLine();
        System.out.print("Số lượng còn lại: "); int remaining = Integer.parseInt(sc.nextLine());
        System.out.println(dao.updateRemaining(id, remaining)
                ? "✅ Cập nhật thành công!" : "❌ Thất bại!");
    }

    private void capNhatStatus() {
        System.out.print("Mã lô: ");            String id = sc.nextLine();
        System.out.print("Trạng thái mới: ");   String status = sc.nextLine();
        System.out.println(dao.updateStatus(id, status)
                ? "✅ Cập nhật thành công!" : "❌ Thất bại!");
    }

    private void xoaLo() {
        System.out.print("Mã lô cần xóa: ");
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