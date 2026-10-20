package vn.edu.crud.menu;

import vn.edu.crud.dao.CareScheduleDAO;
import vn.edu.crud.dao.EmployeeDAO;
import vn.edu.crud.dao.FlowerBatchDAO;
import vn.edu.crud.dao.FlowerDAO;
import vn.edu.crud.model.Employee;
import vn.edu.crud.model.EmployeeRole;
import vn.edu.crud.model.Flower;
import vn.edu.crud.util.NhapLieu;

import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Quản lý chăm sóc hoa.
 *
 * Cách làm: careschedule là NHẬT KÝ (mỗi lần kiểm tra 1 dòng).
 * "Tình trạng hiện tại" của 1 loài hoa = dòng chăm sóc MỚI NHẤT của loài đó.
 * Nhờ vậy vẫn giữ được lịch sử, mà màn hình đầu tiên lại là bảng tình trạng
 * của TẤT CẢ loài hoa (kể cả loài chưa từng được kiểm tra).
 */
public class CareScheduleMenu {

    private final Scanner sc = new Scanner(System.in);
    private final CareScheduleDAO dao = new CareScheduleDAO();
    private final FlowerDAO flowerDAO = new FlowerDAO();
    private final FlowerBatchDAO batchDAO = new FlowerBatchDAO();
    private final EmployeeDAO employeeDAO = new EmployeeDAO();

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter FMT_NGAN = DateTimeFormatter.ofPattern("dd/MM HH:mm");

    /** Quá số ngày này mà chưa kiểm tra thì coi là "lâu chưa chăm". */
    private static final int SO_NGAY_CANH_BAO = 3;

    private static final String[] DO_TUOI = {"TƯƠI", "HƠI HÉO", "HÉO", "HỎNG"};
    private static final String[] TINH_TRANG_NUOC = {"ĐỦ", "THIẾU"};
    private static final String[] HANH_DONG = {
            "Tưới nước", "Thay nước", "Tỉa lá", "Tỉa cành", "Bón phân", "Khác"
    };

    public void hienThi() {
        int chon;
        do {
            System.out.println("\n╔══════════════════════════════════════╗");
            inDong("      QUẢN LÝ CHĂM SÓC HOA");
            System.out.println("╠══════════════════════════════════════╣");
            inDong("  1. Bảng tình trạng chăm sóc");
            inDong("  2. Cập nhật tình trạng");
            inDong("  3. Sửa ghi chú");
            inDong("  4. Xóa bản ghi");
            inDong("  ── TRA CỨU ──");
            inDong("  5. Cần chăm hôm nay");
            inDong("  6. Lịch sử theo loài hoa");
            inDong("  7. Lịch sử theo nhân viên");
            inDong("  0. Quay lại");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("Chọn: ");

            try {
                chon = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                chon = -1;
            }

            switch (chon) {
                case 1: inBangTrangThai(dao.trangThaiHienTai()); break;
                case 2: capNhatTinhTrang(); break;
                case 3: suaGhiChu(); break;
                case 4: xoaBanGhi(); break;
                case 5: canChamHomNay(); break;
                case 6: lichSuTheoHoa(); break;
                case 7: lichSuTheoNhanVien(); break;
                case 0: break;
                default: System.out.println("Chọn sai!");
            }
        } while (chon != 0);
    }

    private void inDong(String noiDung) {
        System.out.printf("║%-38s║%n", noiDung);
    }

    // =====================================================
    // 1. BẢNG TÌNH TRẠNG
    // =====================================================
    private void inBangTrangThai(List<Object[]> ds) {
        if (ds.isEmpty()) {
            System.out.println("Chưa có loài hoa nào.");
            return;
        }

        String[] tieuDe = {"STT", "Mã", "Tên hoa", "Tồn", "Lần cuối",
                "Độ tươi", "Nước", "Trạng thái", "Ghi chú"};
        List<String[]> dong = new ArrayList<>();

        for (int i = 0; i < ds.size(); i++) {
            Object[] r = ds.get(i);
            dong.add(new String[]{
                    String.valueOf(i + 1),
                    chuoi(r[0]),
                    cat(chuoi(r[1]), 22),
                    chuoi(r[3]),
                    thoiGianNgan(r[5]),
                    cat(chuoi(r[6]), 15),
                    cat(chuoi(r[7]), 9),
                    cat(nhanTrangThai(r), 22),
                    cat(chuoi(r[8]), 26)
            });
        }

        // Bề rộng mỗi cột = max(độ rộng hiển thị của tiêu đề, của dữ liệu)
        int[] rong = new int[tieuDe.length];
        for (int c = 0; c < tieuDe.length; c++) {
            rong[c] = doRong(tieuDe[c]);
            for (String[] d : dong) {
                rong[c] = Math.max(rong[c], doRong(d[c]));
            }
        }

        System.out.println("\n--- TÌNH TRẠNG CHĂM SÓC CỦA TẤT CẢ LOÀI HOA ---");
        inDongBang(tieuDe, rong);

        StringBuilder gach = new StringBuilder();
        for (int c = 0; c < rong.length; c++) {
            gach.append("-".repeat(rong[c]));
            if (c < rong.length - 1) gach.append("-+-");
        }
        System.out.println(gach);

        for (String[] d : dong) {
            inDongBang(d, rong);
        }
    }

    private void inDongBang(String[] o, int[] rong) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < o.length; i++) {
            sb.append(o[i]).append(" ".repeat(Math.max(0, rong[i] - doRong(o[i]))));
            if (i < o.length - 1) {
                sb.append(" | ");
            }
        }
        System.out.println(sb);
    }

    /** Độ rộng hiển thị: emoji/ký hiệu rộng 2 cột, bỏ qua variation selector. */
    private int doRong(String s) {
        if (s == null) return 0;
        int w = 0;
        for (int i = 0; i < s.length(); ) {
            int cp = s.codePointAt(i);
            i += Character.charCount(cp);
            if (cp == 0xFE0F || cp == 0xFE0E) continue;
            boolean rongDoi = (cp >= 0x1F300 && cp <= 0x1FAFF)
                    || (cp >= 0x2600 && cp <= 0x27BF);
            w += rongDoi ? 2 : 1;
        }
        return w;
    }

    private String thoiGianNgan(Object o) {
        if (o == null) return "—";
        if (o instanceof LocalDateTime) {
            return ((LocalDateTime) o).format(FMT_NGAN);
        }
        if (o instanceof java.sql.Timestamp) {
            return ((java.sql.Timestamp) o).toLocalDateTime().format(FMT_NGAN);
        }
        return thoiGian(o);
    }

    private String nhanTrangThai(Object[] r) {
        if (r[5] == null) {
            return "⚠️ Chưa kiểm tra";
        }
        String fresh = chuanHoa(r[6]);
        String water = chuanHoa(r[7]);

        // So khớp "mềm" để đọc được cả dữ liệu cũ (VD: "Tươi tốt (100%)", "Thiếu nhẹ")
        // lẫn dữ liệu mới (TƯƠI / HƠI HÉO / HÉO / HỎNG).
        if (fresh.contains("HƠI HÉO")) return "🟠 Cần chú ý";
        if (fresh.contains("HÉO") || fresh.contains("HỎNG")) return "🔴 Cần xử lý";
        if (fresh.contains("KHÔ")) return "🟠 Cần chú ý";
        if (water.startsWith("THIẾU")) return "🟠 Cần chú ý";
        if (soNgayTu(r[5]) > SO_NGAY_CANH_BAO) return "🟡 Lâu chưa kiểm tra";
        return "✅ Ổn";
    }

    // =====================================================
    // 2. CẬP NHẬT TÌNH TRẠNG (sinh bản ghi mới)
    // =====================================================
    private void capNhatTinhTrang() {
        List<Object[]> ds = dao.trangThaiHienTai();
        if (ds.isEmpty()) {
            System.out.println("Chưa có loài hoa nào.");
            return;
        }
        inBangTrangThai(ds);

        System.out.print("\nChọn STT loài hoa cần cập nhật (0 = hủy): ");
        int chon;
        try {
            chon = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("❌ Phải nhập số!");
            return;
        }
        if (chon == 0) return;
        if (chon < 1 || chon > ds.size()) {
            System.out.println("❌ STT không hợp lệ!");
            return;
        }

        Object[] r = ds.get(chon - 1);
        String flowerId = chuoi(r[0]);

        System.out.println("\nLoài hoa : " + chuoi(r[1]) + " (" + flowerId + ")");
        System.out.println("Hiện tại : " + nhanTrangThai(r));
        System.out.println("Ghi chú lần trước: "
                + (r[8] == null || r[8].toString().isBlank() ? "(trống)" : r[8]));

        String empId = chonNhanVien();
        if (empId == null) {
            System.out.println("Đã hủy thao tác.");
            return;
        }

        String fresh = DO_TUOI[chonTrongDanhSach("Độ tươi:", DO_TUOI)];
        String water = TINH_TRANG_NUOC[chonTrongDanhSach("Tình trạng nước:", TINH_TRANG_NUOC)];

        int idxHanhDong = chonTrongDanhSach("Hành động đã làm:", HANH_DONG);
        String action = (idxHanhDong == HANH_DONG.length - 1)
                ? NhapLieu.batBuoc(sc, "Hành động khác", "VD: Cắt gốc, phun sương")
                : HANH_DONG[idxHanhDong];

        System.out.print("Ghi chú (Enter để bỏ trống, VD: Hoa hơi héo): ");
        String note = sc.nextLine().trim();

        String id = dao.sinhMaCSMoi();
        if (dao.insert(id, flowerId, empId, LocalDateTime.now(), fresh, water, action, note)) {
            System.out.println("✅ Đã ghi nhận " + id + " cho " + flowerId + " — " + fresh + " / " + water);
            if (!note.isEmpty()) {
                System.out.println("📝 Ghi chú: " + note);
            }
            if (fresh.contains("HÉO") || fresh.contains("HỎNG")) {
                System.out.println("⚠️ Hoa đã héo/hỏng — cần thanh lý lô để không bán nhầm.");
                thanhLyLoHoaHong(flowerId);
            }
        } else {
            System.out.println("❌ Ghi nhận thất bại!");
        }
    }

    // =====================================================
    // 3. SỬA GHI CHÚ (chỉ sửa note, không đổi tình trạng đã ghi)
    // =====================================================
    private void suaGhiChu() {
        System.out.print("Mã chăm sóc cần sửa (VD: CS003, Enter = chọn theo loài hoa): ");
        String id = sc.nextLine().trim().toUpperCase();

        List<Object[]> banGhi;
        if (id.isEmpty()) {
            List<Object[]> ds = dao.trangThaiHienTai();
            if (ds.isEmpty()) {
                System.out.println("Chưa có loài hoa nào.");
                return;
            }
            inBangTrangThai(ds);
            System.out.print("\nChọn STT loài hoa (0 = hủy): ");

            int chon;
            try {
                chon = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("❌ Phải nhập số!");
                return;
            }
            if (chon < 1 || chon > ds.size()) return;

            Object[] r = ds.get(chon - 1);
            if (r[4] == null) {
                System.out.println("⚠️ Loài hoa này chưa có bản ghi chăm sóc nào để sửa.");
                return;
            }
            id = chuoi(r[4]);
            banGhi = dao.findById(id);
        } else {
            banGhi = dao.findById(id);
        }

        if (banGhi.isEmpty()) {
            System.out.println("❌ Không tìm thấy bản ghi " + id + "!");
            return;
        }

        Object[] row = banGhi.get(0);
        String noteCu = row[7] == null ? "" : row[7].toString();

        System.out.println("\n--- BẢN GHI " + row[0] + " ---");
        System.out.println("Loài hoa : " + row[1] + " | Nhân viên: " + row[2] + " | " + thoiGian(row[3]));
        System.out.println("Tình trạng: " + row[4] + " / " + row[5] + " | Hành động: " + row[6]);
        System.out.println("Ghi chú  : " + (noteCu.isEmpty() ? "(trống)" : noteCu));

        System.out.print("Ghi chú mới (Enter = giữ nguyên): ");
        String note = sc.nextLine().trim();
        if (note.isEmpty()) {
            System.out.println("Không đổi gì.");
            return;
        }
        if (note.equals(noteCu)) {
            System.out.println("Ghi chú không thay đổi.");
            return;
        }

        System.out.println(dao.capNhatGhiChu(id, note)
                ? "✅ Đã cập nhật ghi chú."
                : "❌ Cập nhật thất bại!");
    }

    // =====================================================
    // 4. XÓA BẢN GHI
    // =====================================================
    private void xoaBanGhi() {
        System.out.print("Mã chăm sóc cần xóa (VD: CS001): ");
        String id = sc.nextLine().trim().toUpperCase();

        if (dao.findById(id).isEmpty()) {
            System.out.println("❌ Không tìm thấy bản ghi " + id + "!");
            return;
        }

        System.out.print("Xác nhận xóa " + id + "? (y/n): ");
        if (sc.nextLine().equalsIgnoreCase("y")) {
            System.out.println(dao.delete(id) ? "✅ Đã xóa." : "❌ Thất bại!");
        }
    }

    // =====================================================
    // 5. CẦN CHĂM HÔM NAY
    // =====================================================
    private void canChamHomNay() {
        List<Object[]> ds = dao.trangThaiHienTai();
        List<Object[]> can = new ArrayList<>();
        for (Object[] r : ds) {
            if (!nhanTrangThai(r).startsWith("✅")) {
                can.add(r);
            }
        }

        if (can.isEmpty()) {
            System.out.println("✅ Tất cả " + ds.size() + " loài hoa đều đang ổn.");
            return;
        }

        System.out.println("\n--- CẦN CHĂM HÔM NAY: " + can.size() + "/" + ds.size() + " loài ---");
        inBangTrangThai(can);
        System.out.println("\n(gồm: chưa kiểm tra, héo/hỏng, thiếu nước, hoặc quá "
                + SO_NGAY_CANH_BAO + " ngày chưa kiểm tra)");
    }

    // =====================================================
    // 6, 7. TRA CỨU LỊCH SỬ
    // =====================================================
    private void lichSuTheoHoa() {
        String flowerId = chonLoaiHoa();
        if (flowerId == null) return;

        System.out.println("\n--- LỊCH SỬ CHĂM SÓC CỦA " + flowerId + " ---");
        inBangRutGon(dao.lichChamSocCuaHoa(flowerId),
                "Mã CS", "Ngày kiểm tra", "Độ tươi", "Nước", "Hành động", "Ghi chú", "Nhân viên");
    }

    private void lichSuTheoNhanVien() {
        String empId = chonNhanVien();
        if (empId == null) return;

        System.out.println("\n--- LỊCH SỬ CHĂM SÓC CỦA " + empId + " ---");
        inBangRutGon(dao.lichChamSocCuaNV(empId),
                "Mã CS", "Ngày kiểm tra", "Độ tươi", "Nước", "Hành động", "Loài hoa");
    }

    // =====================================================
    // HÀM HỖ TRỢ
    // =====================================================
    private String chonNhanVien() {
        List<Employee> ds = employeeDAO.findAll();
        if (ds.isEmpty()) {
            System.out.println("❌ Chưa có nhân viên nào!");
            return null;
        }

        System.out.println("\n--- CHỌN NHÂN VIÊN THỰC HIỆN ---");
        for (int i = 0; i < ds.size(); i++) {
            Employee e = ds.get(i);
            System.out.printf("  %d. %-22s (Mã: %s - %s)%n",
                    (i + 1), e.getName(), e.getEmployeeId(),
                    EmployeeRole.getLabelFromString(e.getRole()));
        }

        while (true) {
            System.out.print("Chọn số (0 = hủy): ");
            try {
                int chon = Integer.parseInt(sc.nextLine().trim());
                if (chon == 0) return null;
                if (chon >= 1 && chon <= ds.size()) {
                    return ds.get(chon - 1).getEmployeeId();
                }
                System.out.println("⚠️ Chọn sai số!");
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Vui lòng nhập số!");
            }
        }
    }

    private int chonTrongDanhSach(String tieuDe, String[] ds) {
        System.out.println("\n" + tieuDe);
        for (int i = 0; i < ds.length; i++) {
            System.out.println("  " + (i + 1) + ". " + ds[i]);
        }
        while (true) {
            System.out.print("Chọn (1-" + ds.length + "): ");
            try {
                int chon = Integer.parseInt(sc.nextLine().trim());
                if (chon >= 1 && chon <= ds.length) return chon - 1;
                System.out.println("⚠️ Chọn sai số!");
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Vui lòng nhập số!");
            }
        }
    }

    private String chonLoaiHoa() {
        List<Flower> list = flowerDAO.findAll();
        if (list.isEmpty()) {
            System.out.println("❌ Hệ thống chưa có loài hoa nào!");
            return null;
        }

        System.out.println("\n--- CHỌN LOÀI HOA ---");
        System.out.printf("%-5s | %-8s | %-25s | %-15s%n", "STT", "Mã Hoa", "Tên Hoa", "Màu Sắc");
        System.out.println("-".repeat(65));

        for (int i = 0; i < list.size(); i++) {
            Flower f = list.get(i);
            System.out.printf("%-5d | %-8s | %-25s | %-15s%n",
                    (i + 1), f.getFlowerId(), f.getName(), f.getColor());
        }

        while (true) {
            System.out.print("👉 Nhập STT loài hoa (hoặc 0 để hủy): ");
            try {
                int chon = Integer.parseInt(sc.nextLine().trim());
                if (chon == 0) return null;
                if (chon >= 1 && chon <= list.size()) {
                    return list.get(chon - 1).getFlowerId();
                }
                System.out.println("⚠️ Chọn sai số!");
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Vui lòng nhập số!");
            }
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
            System.out.printf("  %d. Lô %-6s còn %-5s hạn %s%n", (i + 1), r[0], r[1], r[2]);
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

    // =====================================================
    // TIỆN ÍCH
    // =====================================================
    private String chuanHoa(Object o) {
        if (o == null) return "";
        return Normalizer.normalize(o.toString().trim().toUpperCase(), Normalizer.Form.NFC);
    }

    private String chuoi(Object o) {
        return o == null ? "" : o.toString();
    }

    private String cat(String s, int max) {
        if (s == null) return "";
        if (s.codePointCount(0, s.length()) <= max) return s;
        int end = s.offsetByCodePoints(0, max - 1);
        return s.substring(0, end) + "…";
    }

    private String thoiGian(Object o) {
        if (o == null) return "—";
        if (o instanceof java.sql.Timestamp) {
            return ((java.sql.Timestamp) o).toLocalDateTime().format(FMT);
        }
        if (o instanceof java.sql.Date) {
            return ((java.sql.Date) o).toLocalDate() + "";
        }
        if (o instanceof LocalDateTime) {
            return ((LocalDateTime) o).format(FMT);
        }
        return o.toString();
    }

    private long soNgayTu(Object o) {
        LocalDate d;
        if (o instanceof java.sql.Timestamp) {
            d = ((java.sql.Timestamp) o).toLocalDateTime().toLocalDate();
        } else if (o instanceof java.sql.Date) {
            d = ((java.sql.Date) o).toLocalDate();
        } else if (o instanceof LocalDateTime) {
            d = ((LocalDateTime) o).toLocalDate();
        } else {
            return 0;
        }
        return ChronoUnit.DAYS.between(d, LocalDate.now());
    }

    private void inBangRutGon(List<Object[]> list, String... tieuDe) {
        if (list.isEmpty()) {
            System.out.println("Không có dữ liệu.");
            return;
        }
        System.out.println(String.join(" | ", tieuDe));
        System.out.println("-".repeat(110));
        for (Object[] row : list) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < row.length; i++) {
                String o = row[i] == null ? "" : row[i].toString();
                if (i == 1) o = thoiGian(row[i]);
                sb.append(cat(o, 30)).append(" | ");
            }
            System.out.println(sb);
        }
    }
}
