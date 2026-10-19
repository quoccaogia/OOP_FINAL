package vn.edu.crud.menu;

import vn.edu.crud.dao.CareScheduleDAO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class CareScheduleMenu {
    private final Scanner sc = new Scanner(System.in);
    private final CareScheduleDAO dao = new CareScheduleDAO();
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void hienThi() {
        int chon;
        do {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║      QUẢN LÝ LỊCH CHĂM SÓC           ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  ── THAO TÁC ──                      ║");
            System.out.println("║  1. Xem tất cả                       ║");
            System.out.println("║  2. Thêm mới                         ║");
            System.out.println("║  3. Sửa                              ║");
            System.out.println("║  4. Xóa                              ║");
            System.out.println("║  ── BÁO CÁO ──                       ║");
            System.out.println("║  5. Lịch chăm sóc của 1 nhân viên    ║");
            System.out.println("║  0. Quay lại                         ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("Chọn: ");

            try {
                chon = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                chon = -1;
            }

            switch (chon) {
                case 1:
                    inBang(dao.findAll());
                    break;
                case 2:
                    themCS();
                    break;
                case 3:
                    suaCS();
                    break;
                case 4:
                    xoaCS();
                    break;
                case 5:
                    System.out.print("Mã nhân viên: ");
                    inBang(dao.lichChamSocCuaNV(sc.nextLine()));
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Chọn sai!");
            }
        } while (chon != 0);
    }

    private void themCS() {
        String id = dao.sinhMaCSMoi();
        System.out.println("Mã tự sinh: " + id);
        System.out.print("Mã hoa: ");
        String flowerId = sc.nextLine();
        System.out.print("Mã nhân viên: ");
        String empId = sc.nextLine();
        System.out.print("Ngày kiểm tra (yyyy-MM-dd HH:mm:ss): ");
        LocalDateTime date;
        try {
            date = LocalDateTime.parse(sc.nextLine(), FMT);
        } catch (Exception e) {
            date = LocalDateTime.now();
        }
        System.out.print("Độ tươi: ");
        String fresh = sc.nextLine();
        System.out.print("Tình trạng nước: ");
        String water = sc.nextLine();
        System.out.print("Hành động: ");
        String action = sc.nextLine();
        System.out.print("Ghi chú: ");
        String note = sc.nextLine();
        System.out.println(dao.insert(id, flowerId, empId, date, fresh, water, action, note)
                ? "✅ Thêm thành công!"
                : "❌ Thất bại!");
    }

    private void suaCS() {
        System.out.print("Mã chăm sóc cần sửa: ");
        String id = sc.nextLine();
        List<Object[]> rows = dao.findById(id);
        if (rows.isEmpty()) {
            System.out.println("❌ Không tìm thấy!");
            return;
        }
        Object[] cu = rows.get(0);

        System.out.println("(Enter = giữ nguyên giá trị cũ trong ngoặc)");
        String fresh = nhapHoacGiu("Độ tươi mới", cu[4]);
        String water = nhapHoacGiu("Tình trạng nước mới", cu[5]);
        String action = nhapHoacGiu("Hành động mới", cu[6]);
        String note = nhapHoacGiu("Ghi chú mới", cu[7]);
        System.out.println(dao.update(id, fresh, water, action, note)
                ? "✅ Sửa thành công!": "❌ Thất bại!");
    }

    // In giá trị cũ trong ngoặc; bấm Enter (để trống) thì giữ giá trị cũ
    private String nhapHoacGiu(String nhan, Object giaTriCu) {
        String cu = (giaTriCu == null) ? null : giaTriCu.toString();
        System.out.print(nhan + " [" + (cu == null ? "" : cu) + "]: ");
        String s = sc.nextLine().trim();
        return s.isEmpty() ? cu : s;
    }

    private void xoaCS() {
        System.out.print("Mã chăm sóc cần xóa: ");
        String id = sc.nextLine();
        System.out.print("Xác nhận (y/n): ");
        if (sc.nextLine().equalsIgnoreCase("y")) {
            System.out.println(dao.delete(id) ? "✅ Đã xóa!" : "❌ Thất bại!");
        }
    }

    private void inBang(List<Object[]> list) {
        if (list.isEmpty()) {
            System.out.println("Không có dữ liệu.");
            return;
        }
        for (Object[] row : list) {
            StringBuilder sb = new StringBuilder();
            for (Object o : row)
                sb.append(o).append(" | ");
            System.out.println(sb);
        }
    }
}