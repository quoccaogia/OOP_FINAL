package vn.edu.crud.menu;

import vn.edu.crud.dao.EmployeeDAO;
import vn.edu.crud.dao.FlowerBatchDAO;
import vn.edu.crud.dao.FlowerDAO;
import vn.edu.crud.dao.PurchaseOrderDAO;
import vn.edu.crud.dao.SupplierDAO;
import vn.edu.crud.model.Employee;
import vn.edu.crud.model.Flower;
import vn.edu.crud.model.Supplier;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
            System.out.println("║  2. Thêm lô hoa mới                  ║");
            System.out.println("║  3. Cập nhật số lượng còn lại        ║");
            System.out.println("║  4. Cập nhật trạng thái lô           ║");
            System.out.println("║  5. Xóa lô hoa                       ║");
            System.out.println("║  ── BÁO CÁO ──                       ║");
            System.out.println("║  6. Lô hoa còn hàng                  ║");
            System.out.println("║  7. Lô hoa sắp hết hạn (<= 3 ngày)   ║");
            System.out.println("║  0. Quay lại                         ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("Chọn: ");

            try { chon = Integer.parseInt(sc.nextLine()); }
            catch (NumberFormatException e) { chon = -1; }

            switch (chon) {
                case 1: inDanhSach(dao.findAll()); break;
                case 2: themLoHoa(); break;
                case 3: capNhatSoLuong(); break;
                case 4: capNhatTrangThai(); break;
                case 5: xoaLoHoa(); break;
                case 6: inBangRutGon(dao.loHoaConHang(), "LÔ HOA CÒN HÀNG"); break;
                case 7: inBangRutGon(dao.loHoaSapHetHan(3), "LÔ HOA SẮP HẾT HẠN"); break;
                case 0: break;
                default: System.out.println("Chọn sai!");
            }
        } while (chon != 0);
    }

    // =====================================================
    // HÀM IN BẢNG CHI TIẾT
    // =====================================================
    private void inDanhSach(List<Object[]> list) {
        if (list.isEmpty()) { System.out.println("Không có dữ liệu."); return; }

        System.out.printf("%-7s | %-8s | %-18s | %-7s | %-20s | %-8s | %-8s | %-10s | %-16s | %-12s | %-10s%n",
                "Mã Lô", "Mã PN", "Nhà Cung Cấp", "Mã Hoa", "Tên Hoa", "SL Nhập", "SL Còn", "Đơn Giá", "Ngày Nhập", "Hạn SD", "Trạng Thái");
        System.out.println("-".repeat(150));

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Object[] row : list) {
            String batchId = (String) row[0];
            String poId = (String) row[1];
            String supplierName = (String) row[2];
            String flowerId = (String) row[3];
            String flowerName = (String) row[4];
            int qtyImported = (int) row[5];
            int qtyRemaining = (int) row[6];
            BigDecimal unitCost = (BigDecimal) row[7];

            String importDateStr = "N/A";
            if (row[8] != null) {
                if (row[8] instanceof java.sql.Timestamp) {
                    importDateStr = ((java.sql.Timestamp) row[8]).toLocalDateTime().format(dtf);
                } else {
                    importDateStr = row[8].toString().replace("T", " ");
                }
            }

            String expiryDateStr = "N/A";
            if (row[9] != null) {
                if (row[9] instanceof java.sql.Date) {
                    expiryDateStr = ((java.sql.Date) row[9]).toLocalDate().format(df);
                } else {
                    expiryDateStr = row[9].toString();
                }
            }

            String status = (String) row[10];

            System.out.printf("%-7s | %-8s | %-18s | %-7s | %-20s | %-8d | %-8d | %-10.2f | %-16s | %-12s | %-10s%n",
                    batchId, poId, supplierName, flowerId, flowerName, qtyImported, qtyRemaining, unitCost, importDateStr, expiryDateStr, status);
        }
    }

    // =====================================================
    // HÀM PHỤ TRỢ: CHỌN LÔ HOA BẰNG CÁCH BẤM SỐ
    // =====================================================
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
                    return list.get(chon - 1)[0].toString(); // Trả về Mã Lô
                }
                System.out.println("⚠️ Chọn sai số, vui lòng chọn lại!");
            } catch (Exception e) {
                System.out.println("⚠️ Vui lòng nhập số!");
            }
        }
    }

    // =====================================================
    // HÀM THÊM LÔ HOA MỚI
    // =====================================================
    private void themLoHoa() {
        String id = dao.sinhMaLoMoi();
        System.out.println("\n--- THÊM LÔ HOA MỚI ---");
        System.out.println("Mã lô tự sinh: " + id);

        PurchaseOrderDAO poDAO = new PurchaseOrderDAO();
        List<Object[]> dsPN = poDAO.findAll();

        String poId = null;

        System.out.println("--- CHỌN PHIẾU NHẬP ---");
        for (int i = 0; i < dsPN.size(); i++) {
            System.out.printf("  %d. Mã PN: %s (Ngày nhập: %s)\n", (i + 1), dsPN.get(i)[0], dsPN.get(i)[3]);
        }
        System.out.println("  0. Tạo Phiếu Nhập Mới (hoặc bấm Enter)");
        System.out.print("Nhập số chọn: ");

        String choicePN = sc.nextLine().trim();

        if (choicePN.isEmpty() || choicePN.equals("0")) {
            poId = poDAO.sinhMaPNMoi();
            System.out.println("👉 Đang tạo Phiếu Nhập mới: " + poId);

            // 1. Chọn Nhà Cung Cấp
            SupplierDAO supplierDAO = new SupplierDAO();
            List<Supplier> dsNCC = supplierDAO.findAll();
            if (dsNCC.isEmpty()) { System.out.println("❌ Chưa có Nhà cung cấp nào!"); return; }

            System.out.println("--- CHỌN NHÀ CUNG CẤP ---");
            for (int i = 0; i < dsNCC.size(); i++) {
                System.out.printf("  %d. %s\n", (i + 1), dsNCC.get(i).getName());
            }
            String supId = null;
            while (supId == null) {
                System.out.print("Chọn NCC (1-" + dsNCC.size() + "): ");
                try {
                    int chonNCC = Integer.parseInt(sc.nextLine());
                    if (chonNCC >= 1 && chonNCC <= dsNCC.size()) supId = dsNCC.get(chonNCC - 1).getSupplierId();
                    else System.out.println("⚠️ Chọn sai số!");
                } catch (Exception e) { System.out.println("⚠️ Vui lòng nhập số!"); }
            }

            // 2. Chọn Nhân Viên Nhập Hàng
            EmployeeDAO employeeDAO = new EmployeeDAO();
            List<Employee> dsNV = employeeDAO.findAll();
            if (dsNV.isEmpty()) { System.out.println("❌ Chưa có Nhân viên nào!"); return; }

            System.out.println("--- CHỌN NHÂN VIÊN NHẬP HÀNG ---");
            for (int i = 0; i < dsNV.size(); i++) {
                System.out.printf("  %d. %s\n", (i + 1), dsNV.get(i).getName());
            }
            String empId = null;
            while (empId == null) {
                System.out.print("Chọn NV (1-" + dsNV.size() + "): ");
                try {
                    int chonNV = Integer.parseInt(sc.nextLine());
                    if (chonNV >= 1 && chonNV <= dsNV.size()) empId = dsNV.get(chonNV - 1).getEmployeeId();
                    else System.out.println("⚠️ Chọn sai số!");
                } catch (Exception e) { System.out.println("⚠️ Vui lòng nhập số!"); }
            }

            // 3. Lưu Phiếu Nhập
            try {
                Timestamp now = new Timestamp(System.currentTimeMillis());
                boolean isInserted = poDAO.insert(poId, supId, empId, now.toLocalDateTime(), BigDecimal.ZERO, "CHO_XU_LY", "Tạo tự động");

                if (isInserted) {
                    System.out.println("✅ Đã tạo Phiếu Nhập " + poId);
                    List<Object[]> check = poDAO.findById(poId);
                    if (check.isEmpty()) {
                        System.out.println("⚠️ CẢNH BÁO: Đã báo thành công nhưng không tìm thấy trong DB!");
                        return;
                    }
                } else {
                    System.out.println("❌ TẠO PHIẾU NHẬP THẤT BẠI! Hãy kiểm tra lại kết nối Database.");
                    return;
                }
            } catch (Exception e) {
                System.out.println("❌ LỖI SQL: " + e.getMessage());
                return;
            }

        } else {
            try {
                int chon = Integer.parseInt(choicePN);
                if (chon >= 1 && chon <= dsPN.size()) poId = dsPN.get(chon - 1)[0].toString();
                else { System.out.println("⚠️ Chọn sai số!"); return; }
            } catch (Exception e) { System.out.println("⚠️ Lỗi nhập liệu!"); return; }
        }

        // 4. Chọn Loại Hoa
        FlowerDAO flowerDAO = new FlowerDAO();
        List<Flower> dsHoa = flowerDAO.findAll();
        System.out.println("\n--- CHỌN LOẠI HOA ---");
        for (int i = 0; i < dsHoa.size(); i++) {
            System.out.printf("  %d. %s\n", (i + 1), dsHoa.get(i).getName());
        }
        String flowerId = null;
        while (flowerId == null) {
            System.out.print("Nhập số chọn Loại Hoa (1-" + dsHoa.size() + "): ");
            try {
                int chon = Integer.parseInt(sc.nextLine());
                if (chon >= 1 && chon <= dsHoa.size()) flowerId = dsHoa.get(chon - 1).getFlowerId();
                else System.out.println("⚠️ Chọn sai số!");
            } catch (Exception e) { System.out.println("⚠️ Vui lòng nhập số!"); }
        }

        // 5. Nhập số lượng & giá
        int imported = 0;
        while (imported <= 0) {
            System.out.print("Số lượng nhập: ");
            try { imported = Integer.parseInt(sc.nextLine()); } catch (Exception e) { System.out.println("⚠️ Lỗi!"); }
        }

        BigDecimal cost = BigDecimal.ZERO;
        while (cost.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.print("Đơn giá nhập: ");
            try { cost = new BigDecimal(sc.nextLine()); } catch (Exception e) { System.out.println("⚠️ Lỗi định dạng giá!"); }
        }

        System.out.print("Ghi chú: ");
        String note = sc.nextLine();

        String status = "AVAILABLE";
        LocalDateTime importDate = LocalDateTime.now();
        LocalDate expiryDate = importDate.toLocalDate().plusDays(7);

        if (dao.insert(id, poId, flowerId, imported, imported, cost, importDate, expiryDate, status, note)) {
            System.out.println("✅ Thêm lô hoa thành công! (Trạng thái: AVAILABLE)");
        } else {
            System.out.println("❌ Thất bại! Lỗi Database.");
        }
    }

    // =====================================================
    // CÁC HÀM CẬP NHẬT / XÓA
    // =====================================================
    private void capNhatSoLuong() {
        System.out.println("\n--- CẬP NHẬT SỐ LƯỢNG LÔ HOA ---");
        String id = chonLoHoa(); // Gọi hàm chọn từ danh sách
        if (id == null) return; // Nếu bấm 0 để hủy thì thoát

        System.out.print("Số lượng còn lại mới: ");
        try {
            int remaining = Integer.parseInt(sc.nextLine());
            if (remaining < 0) {
                System.out.println("⚠️ Số lượng không được âm!");
                return;
            }
            System.out.println(dao.updateRemaining(id, remaining) ? "✅ Cập nhật thành công!" : "❌ Thất bại!");
        } catch (Exception e) { System.out.println("⚠️ Lỗi định dạng!"); }
    }

    private void capNhatTrangThai() {
        System.out.println("\n--- CẬP NHẬT TRẠNG THÁI LÔ HOA ---");
        String id = chonLoHoa(); // Gọi hàm chọn từ danh sách
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

    private void xoaLoHoa() {
        System.out.println("\n--- XÓA LÔ HOA ---");
        String id = chonLoHoa(); // Gọi hàm chọn từ danh sách
        if (id == null) return;

        System.out.print("Bạn có chắc chắn muốn xóa lô hoa này? (y/n): ");
        if (sc.nextLine().equalsIgnoreCase("y")) {
            System.out.println(dao.delete(id) ? "✅ Đã xóa!" : "❌ Thất bại (Có thể do lô đã có trong đơn hàng)!");
        }
    }

    // Hàm in gọn cho báo cáo
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