package vn.edu.crud.menu;

import vn.edu.crud.dao.FlowerBatchDAO;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class FlowerBatchMenu {
    private final Scanner sc = new Scanner(System.in);
    private final FlowerBatchDAO dao = new FlowerBatchDAO();

    public void hienThi() {
        int chon;
        do {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║           QUẢN LÝ LÔ HOA             ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  1. Xem tất cả lô hoa                ║");
            System.out.println("║  2. Cập nhật trạng thái lô           ║");
            System.out.println("║  3. Thanh lý/Hủy lô hoa (Hết hạn)    ║");
            System.out.println("║  ── BÁO CÁO ──                       ║");
            System.out.println("║  4. Lô hoa còn hàng                  ║");
            System.out.println("║  5. Lô hoa sắp hết hạn (<= 3 ngày)   ║");
            System.out.println("║  0. Quay lại                         ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("Chọn: ");

            try { chon = Integer.parseInt(sc.nextLine()); }
            catch (NumberFormatException e) { chon = -1; }

            switch (chon) {
                case 1: inDanhSach(dao.findAll()); break;
                case 2: capNhatTrangThai(); break;
                case 3: thanhLyLoHoa(); break;
                case 4: inBangRutGon(dao.loHoaConHang(), "LÔ HOA CÒN HÀNG"); break;
                case 5: inBangRutGon(dao.loHoaSapHetHan(3), "LÔ HOA SẮP HẾT HẠN"); break;
                case 0: break;
                default: System.out.println("Chọn sai!");
            }
        } while (chon != 0);
    }

    // =====================================================
    // HÀM IN BẢNG
    // =====================================================
    private void inDanhSach(List<Object[]> list) {
        if (list.isEmpty()) { System.out.println("Không có dữ liệu."); return; }

        System.out.printf("%-7s | %-8s | %-18s | %-7s | %-20s | %-8s | %-8s | %-10s | %-12s | %-10s%n",
                "Mã Lô", "Mã PN", "Nhà Cung Cấp", "Mã Hoa", "Tên Hoa", "SL Nhập", "SL Còn", "Đơn Giá", "Hạn SD", "Trạng Thái");
        System.out.println("-".repeat(135));

        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Object[] row : list) {
            String expiryDateStr = "N/A";
            if (row[9] != null) {
                if (row[9] instanceof java.sql.Date) {
                    expiryDateStr = ((java.sql.Date) row[9]).toLocalDate().format(df);
                } else {
                    expiryDateStr = row[9].toString();
                }
            }

            System.out.printf("%-7s | %-8s | %-18s | %-7s | %-20s | %-8s | %-8s | %-10s | %-12s | %-10s%n",
                    row[0], row[1], row[2], row[3], row[4], row[5], row[6], row[7], expiryDateStr, row[10]);
        }
    }

    private String chonLoHoa() {
        List<Object[]> list = dao.findAll();
        if (list.isEmpty()) {
            System.out.println("❌ Hệ thống chưa có lô hoa nào!");
            return null;
        }

        System.out.println("\n--- DANH SÁCH LÔ HOA ---");
        System.out.printf("%-5s | %-8s | %-20s | %-8s | %-10s%n", "STT", "Mã Lô", "Tên Hoa", "SL Còn", "Trạng Thái");
        System.out.println("-".repeat(65));

        for (int i = 0; i < list.size(); i++) {
            Object[] row = list.get(i);
            System.out.printf("%-5d | %-8s | %-20s | %-8d | %-10s%n",
                    (i + 1), row[0], row[4], row[6], row[10]);
        }

        while (true) {
            System.out.print("👉 Nhập STT lô hoa cần chọn (hoặc 0 để hủy): ");
            try {
                int chon = Integer.parseInt(sc.nextLine());
                if (chon == 0) return null;
                if (chon >= 1 && chon <= list.size()) {
                    return list.get(chon - 1)[0].toString();
                }
                System.out.println("⚠️ Chọn sai số, vui lòng chọn lại!");
            } catch (Exception e) {
                System.out.println("⚠️ Vui lòng nhập số!");
            }
        }
    }

    private void capNhatTrangThai() {
        System.out.println("\n--- CẬP NHẬT TRẠNG THÁI LÔ HOA ---");
        String id = chonLoHoa();
        if (id == null) return;

        System.out.println("Chọn trạng thái mới:");
        System.out.println("  1. AVAILABLE (Đang bán)");
        System.out.println("  2. EXPIRED (Hết hạn/Hỏng)");
        System.out.println("  3. SOLD_OUT (Đã bán hết)");
        System.out.print("Nhập số chọn (1-3): ");

        String status = "AVAILABLE";
        String choice = sc.nextLine();
        if (choice.equals("2")) status = "EXPIRED";
        else if (choice.equals("3")) status = "SOLD_OUT";

        System.out.println(dao.updateStatus(id, status) ? "✅ Cập nhật thành công!" : "❌ Thất bại!");
    }

    private void thanhLyLoHoa() {
        System.out.println("\n--- THANH LÝ / HỦY LÔ HOA ---");
        String id = chonLoHoa();
        if (id == null) return;

        System.out.print("Bạn có chắc chắn muốn thanh lý lô hoa này? (y/n): ");
        if (sc.nextLine().equalsIgnoreCase("y")) {
            if (dao.updateStatus(id, "EXPIRED")) {
                System.out.println("✅ Đã thanh lý lô hoa thành công! (Trạng thái: EXPIRED)");
            } else {
                System.out.println("❌ Thất bại!");
            }
        }
    }

    private void inBangRutGon(List<Object[]> list, String title) {
        System.out.println("\n--- " + title + " ---");
        if (list.isEmpty()) { System.out.println("Không có dữ liệu."); return; }

        System.out.printf("%-8s | %-20s | %-18s | %-10s | %-12s%n",
                "Mã Lô", "Tên Hoa", "Nhà Cung Cấp", "SL Còn", "Hạn SD");
        System.out.println("-".repeat(80));

        for (Object[] row : list) {
            String expiry = row.length > 4 && row[4] != null ? row[4].toString() : "";
            System.out.printf("%-8s | %-20s | %-18s | %-10s | %-12s%n",
                    row[0], row[1], row[2], row[3], expiry);
        }
    }
}