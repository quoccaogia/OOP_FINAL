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
import java.util.List;
import java.util.Scanner;

public class PurchaseOrderMenu {
    private final Scanner sc = new Scanner(System.in);
    private final PurchaseOrderDAO poDAO = new PurchaseOrderDAO();
    private final FlowerBatchDAO batchDAO = new FlowerBatchDAO();
    private final SupplierDAO supplierDAO = new SupplierDAO();
    private final EmployeeDAO employeeDAO = new EmployeeDAO();
    private final FlowerDAO flowerDAO = new FlowerDAO();

    public void hienThi() {
        int chon;
        do {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║    QUẢN LÝ PHIẾU NHẬP HÀNG           ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  ── THAO TÁC ──                      ║");
            System.out.println("║  1. Xem tất cả                       ║");
            System.out.println("║  2. Tìm theo mã                      ║");
            System.out.println("║  3. Tạo Phiếu Nhập Mới (Nhập Hoa)    ║");
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
                case 1: inBang(poDAO.findAll()); break;
                case 2:
                    System.out.print("Nhập mã: ");
                    inBang(poDAO.findById(sc.nextLine()));
                    break;
                case 3: taoPhieuNhapMoi(); break; // Gọi hàm tạo mới đã được nâng cấp
                case 4: suaPN(); break;
                case 5: xoaPN(); break;
                case 6: inBang(poDAO.tongNhapTheoNCC()); break;
                case 7:
                    System.out.print("Mã NCC: ");
                    inBang(poDAO.phieuNhapCuaNCC(sc.nextLine()));
                    break;
                case 8: inBang(poDAO.tatCaPhieuNhap()); break;
                case 0: break;
                default: System.out.println("Chọn sai!");
            }
        } while (chon != 0);
    }

    // =====================================================
    // QUY TRÌNH CHUẨN: TẠO HÓA ĐƠN -> CHỌN HOA -> SINH LÔ
    // =====================================================
    private void taoPhieuNhapMoi() {
        System.out.println("\n--- TẠO PHIẾU NHẬP HÀNG MỚI ---");
        String poId = poDAO.sinhMaPNMoi();
        System.out.println("Mã Phiếu Nhập tự sinh: " + poId);

        // 1. Chọn Nhà Cung Cấp
        List<Supplier> dsNCC = supplierDAO.findAll();
        if (dsNCC.isEmpty()) { System.out.println("❌ Chưa có NCC nào!"); return; }

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

        // 3. Tạo Phiếu Nhập Gốc
        Timestamp now = new Timestamp(System.currentTimeMillis());
        poDAO.insert(poId, supId, empId, now.toLocalDateTime(), BigDecimal.ZERO, "CHO_XU_LY", "Nhập hàng");
        System.out.println("✅ Đã tạo Phiếu Nhập gốc. Bắt đầu nhập hoa...");

        // 4. Vòng lặp nhập Hoa & Tự động sinh Lô Hoa
        BigDecimal tongTienPhieu = BigDecimal.ZERO;
        while (true) {
            List<Flower> dsHoa = flowerDAO.findAll();
            System.out.println("\n--- CHỌN LOẠI HOA NHẬP ---");
            for (int i = 0; i < dsHoa.size(); i++) {
                System.out.printf("  %d. %-20s (Mã: %s)\n", (i + 1), dsHoa.get(i).getName(), dsHoa.get(i).getFlowerId());
            }
            System.out.println("  0. Thêm loại Hoa mới (Nếu chưa có trong danh sách)");
            System.out.println("  Enter. Kết thúc nhập hoa");

            System.out.print("Chọn số (1-" + dsHoa.size() + " hoặc 0): ");
            String choiceHoa = sc.nextLine().trim();

            if (choiceHoa.isEmpty()) break; // Kết thúc vòng lặp

            String flowerId = null;
            Flower hoaChon = null;

            if (choiceHoa.equals("0")) {
                // TẠO LOẠI HOA MỚI
                flowerId = themHoaMoiTuDong(supId);
                if (flowerId == null) continue;
                hoaChon = flowerDAO.timTheoMa(flowerId);
            } else {
                // CHỌN HOA CÓ SẴN
                try {
                    int chon = Integer.parseInt(choiceHoa);
                    if (chon >= 1 && chon <= dsHoa.size()) {
                        hoaChon = dsHoa.get(chon - 1);
                        flowerId = hoaChon.getFlowerId();
                    } else { System.out.println("⚠️ Chọn sai số!"); continue; }
                } catch (Exception e) { System.out.println("⚠️ Vui lòng nhập số!"); continue; }
            }

            System.out.println("👉 Đã chọn: " + hoaChon.getName());
            System.out.print("Số lượng nhập: ");
            int imported = 0;
            try {
                imported = Integer.parseInt(sc.nextLine());
                if (imported <= 0) throw new Exception();
            } catch (Exception e) { System.out.println("⚠️ Số lượng phải > 0!"); continue; }

            System.out.print("Đơn giá nhập: ");
            BigDecimal cost = BigDecimal.ZERO;
            try {
                cost = new BigDecimal(sc.nextLine());
                if (cost.compareTo(BigDecimal.ZERO) <= 0) throw new Exception();
            } catch (Exception e) { System.out.println("⚠️ Giá phải > 0!"); continue; }

            LocalDateTime importDate = LocalDateTime.now();
            LocalDate expiryDate = importDate.toLocalDate().plusDays(7);

            String batchId = batchDAO.sinhMaLoMoi();
            boolean luuLo = batchDAO.insert(batchId, poId, flowerId, imported, imported, cost, importDate, expiryDate, "AVAILABLE", "Tự động sinh từ PN " + poId);

            if (luuLo) {
                System.out.println("✅ Đã sinh Lô Hoa " + batchId + " (Hạn SD: " + expiryDate + ")");
                tongTienPhieu = tongTienPhieu.add(cost.multiply(BigDecimal.valueOf(imported)));
            } else {
                System.out.println("❌ Lỗi khi lưu Lô Hoa!");
            }
        }

        // 5. Cập nhật lại tổng tiền cho Phiếu Nhập
        poDAO.update(poId, tongTienPhieu, "HOAN_THANH", "Nhập hàng thành công");
        System.out.println("\n🎉 TẠO PHIẾU NHẬP THÀNH CÔNG! Tổng tiền: " + tongTienPhieu + " VNĐ");
    }

    // =====================================================
    // HÀM PHỤ TRỢ: THÊM HOA MỚI TỰ ĐỘNG
    // =====================================================
    private String themHoaMoiTuDong(String supplierId) {
        String newId = flowerDAO.sinhMaHoaMoi();
        System.out.println("\n--- THÊM LOẠI HOA MỚI ---");
        System.out.println("Mã hoa tự sinh: " + newId);

        System.out.print("Tên hoa (VD: Hồng Đỏ Đà Lạt): ");
        String name = sc.nextLine();
        System.out.print("Danh mục (VD: Hồng, Lan, Cúc...): ");
        String category = sc.nextLine();

        BigDecimal price = BigDecimal.ZERO;
        while (price.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.print("Giá bán niêm yết: ");
            try { price = new BigDecimal(sc.nextLine()); } catch (Exception e) { System.out.println("⚠️ Lỗi định dạng!"); }
        }

        System.out.print("Màu sắc (VD: Đỏ, Trắng...): ");
        String color = sc.nextLine();

        // Tồn kho ban đầu = 0 (vì tồn kho thực tế sẽ do Lô Hoa cộng vào)
        boolean ok = flowerDAO.insert(newId, name, category, price, 0, color, supplierId);

        if (ok) {
            System.out.println("✅ Đã thêm loại hoa mới vào hệ thống!");
            return newId;
        } else {
            System.out.println("❌ Lỗi khi thêm hoa mới!");
            return null;
        }
    }

    // =====================================================
    // CÁC HÀM CŨ (SỬA / XÓA / IN BẢNG)
    // =====================================================
    private void suaPN() {
        System.out.print("Mã phiếu nhập cần sửa: ");
        String id = sc.nextLine();
        if (poDAO.findById(id).isEmpty()) { System.out.println("❌ Không tìm thấy!"); return; }
        System.out.print("Tổng tiền mới: "); BigDecimal total = new BigDecimal(sc.nextLine());
        System.out.print("Trạng thái mới: "); String status = sc.nextLine();
        System.out.print("Ghi chú mới: ");    String note = sc.nextLine();
        System.out.println(poDAO.update(id, total, status, note) ? "✅ Sửa thành công!" : "❌ Thất bại!");
    }

    private void xoaPN() {
        System.out.print("Mã phiếu nhập cần xóa: ");
        String id = sc.nextLine();
        System.out.print("Xác nhận (y/n): ");
        if (sc.nextLine().equalsIgnoreCase("y")) {
            System.out.println(poDAO.delete(id) ? "✅ Đã xóa!" : "❌ Thất bại!");
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