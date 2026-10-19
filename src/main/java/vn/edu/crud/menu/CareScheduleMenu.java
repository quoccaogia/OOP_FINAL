package vn.edu.crud.menu;

import vn.edu.crud.dao.CareScheduleDAO;
import vn.edu.crud.dao.FlowerBatchDAO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class CareScheduleMenu {
    private final Scanner sc = new Scanner(System.in);
    private final CareScheduleDAO dao = new CareScheduleDAO();
    private final FlowerBatchDAO batchDAO = new FlowerBatchDAO();

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public void hienThi() {
        int chon;
        do {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║      QUẢN LÝ LỊCH CHĂM SÓC           ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  1. Xem tất cả lịch sử chăm sóc      ║");
            System.out.println("║  2. Ghi nhận chăm sóc lô hoa         ║");
            System.out.println("║  3. Xem lịch sử của 1 lô hoa         ║");
            System.out.println("║  4. Xem lịch sử của 1 nhân viên      ║");
            System.out.println("║  5. Xóa lịch sử chăm sóc             ║");
            System.out.println("║  0. Quay lại                         ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("Chọn: ");

            try { chon = Integer.parseInt(sc.nextLine()); }
            catch (NumberFormatException e) { chon = -1; }

            switch (chon) {
                case 1: inDanhSach(dao.findAll()); break;
                case 2: themLichChamSoc(); break;
                case 3: xemLichSuCuaLo(); break;
                case 4:
                    System.out.print("Nhập Mã NV (VD: NV03): ");
                    inBangRutGon(dao.lichChamSocCuaNV(sc.nextLine().toUpperCase()));
                    break;
                case 5: xoaLichChamSoc(); break;
                case 0: break;
                default: System.out.println("Chọn sai!");
            }
        } while (chon != 0);
    }

    private void themLichChamSoc() {
        System.out.println("\n--- GHI NHẬN CHĂM SÓC LÔ HOA ---");
        String batchId = chonLoHoa();
        if (batchId == null) return;

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

        if (dao.insert(id, batchId, empId, date, fresh, water, action, note)) {
            System.out.println("✅ Đã ghi nhận lịch chăm sóc cho lô " + batchId);
            if (fresh.contains("HÉO")) {
                System.out.println("⚠️ LƯU Ý: Hoa đã héo. Bạn nên vào Menu 'Quản lý lô hoa' để Thanh lý lô này!");
            }
        } else {
            System.out.println("❌ Thất bại! Kiểm tra lại Mã NV xem có đúng không.");
        }
    }

    private String chonLoHoa() {
        List<Object[]> list = batchDAO.findAll();
        if (list.isEmpty()) {
            System.out.println("❌ Hệ thống chưa có lô hoa nào!");
            return null;
        }

        System.out.println("--- CHỌN LÔ HOA CẦN CHĂM SÓC ---");
        System.out.printf("%-5s | %-8s | %-20s | %-10s%n", "STT", "Mã Lô", "Tên Hoa", "Trạng Thái");
        System.out.println("-".repeat(55));

        for (int i = 0; i < list.size(); i++) {
            Object[] row = list.get(i);
            System.out.printf("%-5d | %-8s | %-20s | %-10s%n", (i + 1), row[0], row[4], row[10]);
        }

        while (true) {
            System.out.print("👉 Nhập STT lô hoa (hoặc 0 để hủy): ");
            try {
                int chon = Integer.parseInt(sc.nextLine());
                if (chon == 0) return null;
                if (chon >= 1 && chon <= list.size()) {
                    return list.get(chon - 1)[0].toString();
                }
                System.out.println("⚠️ Chọn sai số!");
            } catch (Exception e) { System.out.println("⚠️ Vui lòng nhập số!"); }
        }
    }

    private void xemLichSuCuaLo() {
        System.out.println("\n--- XEM LỊCH SỬ CHĂM SÓC CỦA LÔ ---");
        String batchId = chonLoHoa();
        if (batchId == null) return;

        System.out.println("\nLịch sử chăm sóc của lô: " + batchId);
        inBangRutGon(dao.lichChamSocCuaLo(batchId));
    }

    private void xoaLichChamSoc() {
        System.out.print("Nhập Mã lịch chăm sóc cần xóa (VD: CS001): ");
        String id = sc.nextLine().toUpperCase();
        System.out.print("Xác nhận xóa? (y/n): ");
        if (sc.nextLine().equalsIgnoreCase("y")) {
            System.out.println(dao.delete(id) ? "✅ Đã xóa!" : "❌ Thất bại!");
        }
    }

    private void inDanhSach(List<Object[]> list) {
        if (list.isEmpty()) { System.out.println("Không có dữ liệu."); return; }

        System.out.printf("%-7s | %-8s | %-20s | %-20s | %-16s | %-12s | %-12s%n",
                "Mã CS", "Mã Lô", "Tên Hoa", "Nhân Viên", "Ngày Kiểm Tra", "Độ Tươi", "Nước");
        System.out.println("-".repeat(115));

        for (Object[] row : list) {
            String dateStr = "N/A";
            if (row[4] != null) {
                if (row[4] instanceof java.sql.Timestamp) {
                    dateStr = ((java.sql.Timestamp) row[4]).toLocalDateTime().format(FMT);
                } else {
                    dateStr = row[4].toString();
                }
            }

            System.out.printf("%-7s | %-8s | %-20s | %-20s | %-16s | %-12s | %-12s%n",
                    row[0], row[1], row[2], row[3], dateStr, row[5], row[6]);
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