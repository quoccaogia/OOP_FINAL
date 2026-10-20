package vn.edu.crud.menu;

import vn.edu.crud.dao.CareScheduleDAO;
import vn.edu.crud.dao.FlowerBatchDAO;
import vn.edu.crud.dao.FlowerDAO;
import vn.edu.crud.model.Flower;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class CareScheduleMenu {
    private final Scanner sc = new Scanner(System.in);
    private final CareScheduleDAO dao = new CareScheduleDAO();
    private final FlowerDAO flowerDAO = new FlowerDAO();
    private final FlowerBatchDAO batchDAO = new FlowerBatchDAO();

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public void hienThi() {
        int chon;
        do {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║      QUẢN LÝ LỊCH CHĂM SÓC           ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  1. Xem tất cả lịch sử chăm sóc      ║");
            System.out.println("║  2. Ghi nhận chăm sóc loài hoa       ║");
            System.out.println("║  3. Sửa ghi chú chăm sóc             ║");
            System.out.println("║  4. Xóa lịch sử chăm sóc             ║");
            System.out.println("║  ── BÁO CÁO ──                       ║");
            System.out.println("║  5. Lịch sử của 1 nhân viên          ║");
            System.out.println("║  6. Lịch sử của 1 loài hoa           ║");
            System.out.println("║  0. Quay lại                         ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("Chọn: ");

            try { chon = Integer.parseInt(sc.nextLine()); }
            catch (NumberFormatException e) { chon = -1; }

            switch (chon) {
                case 1: inDanhSach(dao.findAll()); break;
                case 2: themCS(); break;
                case 3: suaCS(); break;
                case 4: xoaCS(); break;
                case 5:
                    System.out.print("Nhập Mã NV (VD: NV03): ");
                    inBangRutGon(dao.lichChamSocCuaNV(sc.nextLine().toUpperCase()));
                    break;
                case 6:
                    String flowerId = chonLoaiHoa();
                    if (flowerId != null) {
                        System.out.println("\nLịch sử chăm sóc của loài hoa: " + flowerId);
                        inBangRutGon(dao.lichChamSocCuaHoa(flowerId));
                    }
                    break;
                case 0: break;
                default: System.out.println("Chọn sai!");
            }
        } while (chon != 0);
    }

    private void themCS() {
        System.out.println("\n--- GHI NHẬN CHĂM SÓC LOÀI HOA ---");
        String flowerId = chonLoaiHoa();
        if (flowerId == null) return;

        String id = dao.sinhMaCSMoi();
        System.out.println("Mã lịch tự sinh: " + id);

        System.out.print("Mã nhân viên thực hiện (VD: NV03): ");
        String empId = sc.nextLine().toUpperCase();

        System.out.print("Độ tươi (VD: TƯƠI / HƠI HÉO / HÉO): ");
        String fresh = sc.nextLine().toUpperCase();

        System.out.print("Tình trạng nước (VD: ĐỦ / THIẾU): ");
        String water = sc.nextLine();

        System.out.print("Hành động đã làm (VD: Tưới nước, Tỉa lá): ");
        String action = sc.nextLine();

        System.out.print("Ghi chú thêm: ");
        String note = sc.nextLine();

        LocalDateTime date = LocalDateTime.now();

        if (dao.insert(id, flowerId, empId, date, fresh, water, action, note)) {
            System.out.println("✅ Đã ghi nhận lịch chăm sóc cho hoa " + flowerId);
            if (fresh.contains("HÉO") || fresh.contains("HỎNG")) {
                System.out.println("⚠️ Hoa đã héo/hỏng — cần thanh lý lô để không bán nhầm.");
                thanhLyLoHoaHong(flowerId);
            }
        } else {
            System.out.println("❌ Thất bại! Kiểm tra lại Mã NV xem có đúng không.");
        }
    }

    // Sau khi ghi nhận hoa hỏng: cho thanh lý luôn lô tương ứng (trừ tồn kho)
    private void thanhLyLoHoaHong(String flowerId) {
        List<Object[]> dsLo = batchDAO.loAvailableCuaHoa(flowerId);
        if (dsLo.isEmpty()) {
            System.out.println("(Loài hoa này không còn lô AVAILABLE nào để thanh lý.)");
            return;
        }

        System.out.println("\nCác lô còn hàng của " + flowerId + ":");
        for (int i = 0; i < dsLo.size(); i++) {
            Object[] r = dsLo.get(i);
            System.out.printf("  %d. Lô %-6s còn %-5s hạn %s%n",
                    (i + 1), r[0], r[1], r[2]);
        }
        System.out.print("Thanh lý lô nào? (0 = bỏ qua): ");

        int chon;
        try {
            chon = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("⚠️ Bỏ qua thanh lý.");
            return;
        }
        if (chon < 1 || chon > dsLo.size()) return;

        String batchId = (String) dsLo.get(chon - 1)[0];
        System.out.print("Xác nhận thanh lý lô " + batchId + "? (y/n): ");
        if (sc.nextLine().equalsIgnoreCase("y")) {
            System.out.println(batchDAO.thanhLyLo(batchId)
                    ? "✅ Đã thanh lý lô và trừ tồn kho tương ứng."
                    : "❌ Thanh lý thất bại!");
        }
    }

    private String chonLoaiHoa() {
        List<Flower> list = flowerDAO.findAll();
        if (list.isEmpty()) {
            System.out.println("❌ Hệ thống chưa có loài hoa nào!");
            return null;
        }

        System.out.println("--- CHỌN LOÀI HOA ---");
        System.out.printf("%-5s | %-8s | %-25s | %-15s%n", "STT", "Mã Hoa", "Tên Hoa", "Màu Sắc");
        System.out.println("-".repeat(65));

        for (int i = 0; i < list.size(); i++) {
            Flower f = list.get(i);
            System.out.printf("%-5d | %-8s | %-25s | %-15s%n", (i + 1), f.getFlowerId(), f.getName(), f.getColor());
        }

        while (true) {
            System.out.print("👉 Nhập STT loài hoa (hoặc 0 để hủy): ");
            try {
                int chon = Integer.parseInt(sc.nextLine());
                if (chon == 0) return null;
                if (chon >= 1 && chon <= list.size()) {
                    return list.get(chon - 1).getFlowerId();
                }
                System.out.println("⚠️ Chọn sai số!");
            } catch (Exception e) { System.out.println("⚠️ Vui lòng nhập số!"); }
        }
    }

    private void suaCS() {
        System.out.print("Mã chăm sóc cần sửa (VD: CS001): ");
        String id = sc.nextLine().toUpperCase();
        if (dao.findById(id).isEmpty()) { System.out.println("❌ Không tìm thấy!"); return; }

        System.out.print("Độ tươi mới (Enter để giữ nguyên): ");
        String fresh = sc.nextLine();
        System.out.print("Tình trạng nước mới (Enter để giữ nguyên): ");
        String water = sc.nextLine();
        System.out.print("Hành động mới (Enter để giữ nguyên): ");
        String action = sc.nextLine();
        System.out.print("Ghi chú mới (Enter để giữ nguyên): ");
        String note = sc.nextLine();

        List<Object[]> cu = dao.findById(id);
        Object[] row = cu.get(0);

        if (fresh.isEmpty()) fresh = (String) row[4];
        if (water.isEmpty()) water = (String) row[5];
        if (action.isEmpty()) action = (String) row[6];
        if (note.isEmpty()) note = (String) row[7];

        System.out.println(dao.update(id, fresh, water, action, note)
                ? "✅ Sửa thành công!" : "❌ Thất bại!");
    }

    private void xoaCS() {
        System.out.print("Mã lịch chăm sóc cần xóa (VD: CS001): ");
        String id = sc.nextLine().toUpperCase();
        System.out.print("Xác nhận (y/n): ");
        if (sc.nextLine().equalsIgnoreCase("y")) {
            System.out.println(dao.delete(id) ? "✅ Đã xóa!" : "❌ Thất bại!");
        }
    }

    // =====================================================
    // HÀM IN DANH SÁCH (ĐÃ BỔ SUNG HÀNH ĐỘNG & GHI CHÚ)
    // =====================================================
    private void inDanhSach(List<Object[]> list) {
        if (list.isEmpty()) { System.out.println("Không có dữ liệu."); return; }

        // Tiêu đề bảng (đã thêm Hành động và Ghi chú)
        System.out.printf("%-7s | %-8s | %-18s | %-15s | %-16s | %-10s | %-10s | %-15s | %-20s%n",
                "Mã CS", "Mã Hoa", "Tên Hoa", "Nhân Viên", "Ngày KT", "Độ Tươi", "Nước", "Hành Động", "Ghi Chú");
        System.out.println("-".repeat(145));

        for (Object[] row : list) {
            String dateStr = "N/A";
            if (row[4] != null) {
                if (row[4] instanceof java.sql.Timestamp) {
                    dateStr = ((java.sql.Timestamp) row[4]).toLocalDateTime().format(FMT);
                } else if (row[4] instanceof LocalDateTime) {
                    dateStr = ((LocalDateTime) row[4]).format(FMT);
                } else {
                    dateStr = row[4].toString().replace("T", " ");
                }
            }

            // Lấy dữ liệu Hành động (cột 7) và Ghi chú (cột 8), xử lý null
            String action = row[7] != null ? row[7].toString() : "";
            String note = row[8] != null ? row[8].toString() : "";

            // Cắt bớt chuỗi nếu quá dài để không làm vỡ bảng
            if (action.length() > 15) action = action.substring(0, 14) + "…";
            if (note.length() > 20) note = note.substring(0, 19) + "…";

            System.out.printf("%-7s | %-8s | %-18s | %-15s | %-16s | %-10s | %-10s | %-15s | %-20s%n",
                    row[0], row[1], row[2], row[3], dateStr, row[5], row[6], action, note);
        }
    }

    private void inBangRutGon(List<Object[]> list) {
        if (list.isEmpty()) { System.out.println("Không có dữ liệu."); return; }
        for (Object[] row : list) {
            StringBuilder sb = new StringBuilder();
            for (Object o : row) sb.append(o).append(" | ");
            System.out.println(sb);
        }
    }
}