package vn.edu.crud.menu;

import vn.edu.crud.util.InBangHelper;

import vn.edu.crud.service.EmployeeService;
import vn.edu.crud.service.FlowerBatchService;
import vn.edu.crud.service.FlowerService;
import vn.edu.crud.service.PurchaseOrderService;
import vn.edu.crud.service.SupplierService;
import vn.edu.crud.model.Employee;
import vn.edu.crud.model.Flower;
import vn.edu.crud.model.Supplier;
import vn.edu.crud.util.NhapLieu;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import vn.edu.crud.model.PurchaseOrder;
import java.util.Map;

public class PurchaseOrderMenu {
    private final Scanner sc = new Scanner(System.in);
    private final PurchaseOrderService purchaseOrderService = new PurchaseOrderService();
    private final FlowerBatchService batchService = new FlowerBatchService();
    private final SupplierService supplierService = new SupplierService();
    private final EmployeeService employeeService = new EmployeeService();
    private final FlowerService flowerService = new FlowerService();

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
            System.out.println("║  ── XỬ LÝ DỞ DANG ──                 ║");
            System.out.println("║  9. Chốt phiếu nhập dở dang          ║");
            System.out.println("║  ── BÁO CÁO ──                       ║");
            System.out.println("║  6. Tổng tiền nhập theo NCC          ║");
            System.out.println("║  7. Phiếu nhập theo NCC              ║");
            System.out.println("║  8. Tất cả phiếu nhập (kèm tên)      ║");
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
                    InBangHelper.inBang(purchaseOrderService.findAll(), new String[]{"Mã PN", "Mã NCC", "Mã NV", "Ngày Nhập", "Tổng Tiền", "Trạng Thái", "Ghi Chú"});
                    break;
                case 2:
                    System.out.print("Nhập mã: ");
                    timPhieuTheoMa(sc.nextLine().trim().toUpperCase());
                    break;
                case 3:
                    taoPhieuNhapMoi();
                    break;
                case 4:
                    suaPN();
                    break;
                case 5:
                    xoaPN();
                    break;
                case 6:
                    tongNhapTheoNCC();
                    break;
                case 7:
                    System.out.print("Mã NCC: ");
                    phieuCuaNCC(sc.nextLine().trim().toUpperCase());
                    break;
                case 8:
                    InBangHelper.inBang(purchaseOrderService.tatCaPhieuNhap(), new String[]{"Mã PN", "Nhà Cung Cấp", "Nhân Viên Nhập", "Ngày Nhập", "Tổng Tiền", "Trạng Thái", "Ghi Chú"});
                    break;
                case 9:
                    chotPhieuNhap();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Chọn sai!");
            }
        } while (chon != 0);
    }

    private void taoPhieuNhapMoi() {
        System.out.println("\n--- TẠO PHIẾU NHẬP HÀNG MỚI ---");
        String poId = purchaseOrderService.sinhMaPNMoi();
        System.out.println("Mã Phiếu Nhập tự sinh: " + poId);

        // 1. Chọn Nhà Cung Cấp
        List<Supplier> dsNCC = supplierService.findAll();
        if (dsNCC.isEmpty()) {
            System.out.println("❌ Chưa có NCC nào!");
            return;
        }

        System.out.println("--- CHỌN NHÀ CUNG CẤP ---");
        for (int i = 0; i < dsNCC.size(); i++) {
            System.out.printf("  %d. %s\n", (i + 1), dsNCC.get(i).getName());
        }
        String supId = null;
        while (supId == null) {
            System.out.print("Chọn NCC (1-" + dsNCC.size() + "): ");
            try {
                int chonNCC = Integer.parseInt(sc.nextLine());
                if (chonNCC >= 1 && chonNCC <= dsNCC.size())
                    supId = dsNCC.get(chonNCC - 1).getSupplierId();
                else
                    System.out.println("⚠️ Chọn sai số!");
            } catch (Exception e) {
                System.out.println("⚠️ Vui lòng nhập số!");
            }
        }

        // 2. Chọn Nhân Viên Nhập Hàng
        List<Employee> dsNV = employeeService.findAll();
        if (dsNV.isEmpty()) {
            System.out.println("❌ Chưa có Nhân viên nào!");
            return;
        }

        System.out.println("--- CHỌN NHÂN VIÊN NHẬP HÀNG ---");
        for (int i = 0; i < dsNV.size(); i++) {
            System.out.printf("  %d. %s\n", (i + 1), dsNV.get(i).getName());
        }
        String empId = null;
        while (empId == null) {
            System.out.print("Chọn NV (1-" + dsNV.size() + "): ");
            try {
                int chonNV = Integer.parseInt(sc.nextLine());
                if (chonNV >= 1 && chonNV <= dsNV.size())
                    empId = dsNV.get(chonNV - 1).getEmployeeId();
                else
                    System.out.println("⚠️ Chọn sai số!");
            } catch (Exception e) {
                System.out.println("⚠️ Vui lòng nhập số!");
            }
        }

        // 3. Tạo Phiếu Nhập Gốc
        Timestamp now = new Timestamp(System.currentTimeMillis());
        purchaseOrderService.insert(poId, supId, empId, now.toLocalDateTime(), BigDecimal.ZERO, "CHO_XU_LY", "Nhập hàng");
        System.out.println("✅ Đã tạo Phiếu Nhập gốc. Bắt đầu nhập hoa...");

        // 4. Vòng lặp nhập Hoa & Tự động sinh Lô Hoa
        BigDecimal tongTienPhieu = BigDecimal.ZERO;
        while (true) {
            List<Flower> dsHoa = flowerService.findAll();
            System.out.println("\n--- CHỌN LOẠI HOA NHẬP ---");
            for (int i = 0; i < dsHoa.size(); i++) {
                System.out.printf("  %d. %-20s (Mã: %s)\n", (i + 1), dsHoa.get(i).getName(),
                        dsHoa.get(i).getFlowerId());
            }
            System.out.println("  0. Thêm loại Hoa mới (Nếu chưa có trong danh sách)");
            System.out.println("  Enter. Kết thúc nhập hoa");

            System.out.print("Chọn số (1-" + dsHoa.size() + " hoặc 0): ");
            String choiceHoa = sc.nextLine().trim();

            if (choiceHoa.isEmpty())
                break; // Kết thúc vòng lặp

            String flowerId = null;
            Flower hoaChon = null;

            if (choiceHoa.equals("0")) {
                flowerId = themHoaMoiTuDong(supId);
                if (flowerId == null)
                    continue;
                hoaChon = flowerService.timTheoMa(flowerId);
            } else {
                try {
                    int chon = Integer.parseInt(choiceHoa);
                    if (chon >= 1 && chon <= dsHoa.size()) {
                        hoaChon = dsHoa.get(chon - 1);
                        flowerId = hoaChon.getFlowerId();
                    } else {
                        System.out.println("⚠️ Chọn sai số!");
                        continue;
                    }
                } catch (Exception e) {
                    System.out.println("⚠️ Vui lòng nhập số!");
                    continue;
                }
            }

            System.out.println("👉 Đã chọn: " + hoaChon.getName());
            System.out.print("Số lượng nhập (VD: 100): ");
            int imported = 0;
            try {
                imported = Integer.parseInt(sc.nextLine());
                if (imported <= 0)
                    throw new Exception();
            } catch (Exception e) {
                System.out.println("⚠️ Số lượng phải > 0!");
                continue;
            }

            BigDecimal cost = NhapLieu.tien(sc, "Đơn giá nhập", "tối thiểu 500đ, VD: 3000", false);

            // Cảnh báo nếu giá nhập >= giá bán niêm yết
            if (hoaChon.getUnitPrice() != null && cost.compareTo(hoaChon.getUnitPrice()) >= 0) {
                System.out.println("⚠️ CẢNH BÁO: Giá nhập (" + cost + " VNĐ) cao hơn hoặc bằng giá bán niêm yết (" + hoaChon.getUnitPrice() + " VNĐ)!");
                System.out.print("👉 Bạn có chắc chắn muốn nhập giá này không? (y/n): ");
                String xacNhan = sc.nextLine().trim().toLowerCase();
                if (!xacNhan.equals("y")) {
                    System.out.println("Đã hủy nhập giá này, vui lòng nhập lại.");
                    continue;
                }
            }

            LocalDateTime importDate = LocalDateTime.now();
            LocalDate expiryDate = importDate.toLocalDate().plusDays(7);

            String batchId = batchService.sinhMaLoMoi();
            boolean luuLo = batchService.insert(batchId, poId, flowerId, imported, imported, cost, importDate, expiryDate,
                    "AVAILABLE", "Tự động sinh từ PN " + poId);

            if (luuLo) {
                System.out.println("✅ Đã sinh Lô Hoa " + batchId + " (Hạn SD: " + expiryDate + ")");

                // === ĐIỂM SỬA ĐỔI QUAN TRỌNG: CỘNG TỒN KHO VÀO BẢNG FLOWER ===
                flowerService.congTonKho(flowerId, imported);
                // =========================================================

                tongTienPhieu = tongTienPhieu.add(cost.multiply(BigDecimal.valueOf(imported)));
            } else {
                System.out.println("❌ Lỗi khi lưu Lô Hoa!");
            }
        }

        // 5. Chưa nhập được lô nào -> phiếu rỗng, hủy luôn thay vì để HOAN_THANH 0 đồng
        if (tongTienPhieu.compareTo(BigDecimal.ZERO) <= 0) {
            purchaseOrderService.delete(poId);
            System.out.println("\n⚠️ Chưa nhập loại hoa nào — phiếu nhập rỗng đã được hủy (không tạo phiếu 0 đồng).");
            return;
        }

        // 6. Cập nhật lại tổng tiền cho Phiếu Nhập
        purchaseOrderService.update(poId, tongTienPhieu, "HOAN_THANH", "Nhập hàng thành công");
        System.out.println("\n🎉 TẠO PHIẾU NHẬP THÀNH CÔNG! Tổng tiền: " + tongTienPhieu + " VNĐ");
    }

    private String themHoaMoiTuDong(String supplierId) {
        String newId = flowerService.sinhMaHoaMoi();
        System.out.println("\n--- THÊM LOẠI HOA MỚI ---");
        System.out.println("Mã hoa tự sinh: " + newId);

        String name = NhapLieu.batBuoc(sc, "Tên hoa", "VD: Hồng Đỏ Đà Lạt");
        String category = NhapLieu.batBuoc(sc, "Danh mục", "VD: Hồng, Lan, Cúc");
        BigDecimal price = NhapLieu.tien(sc, "Giá bán niêm yết", "VD: 5000", false);
        String color = NhapLieu.batBuoc(sc, "Màu sắc", "VD: Đỏ, Trắng");

        // Tồn kho ban đầu = 0
        boolean ok = flowerService.insert(newId, name, category, price, 0, color, supplierId);

        if (ok) {
            System.out.println("✅ Đã thêm loại hoa mới vào hệ thống!");
            return newId;
        } else {
            System.out.println("❌ Lỗi khi thêm hoa mới!");
            return null;
        }
    }

    private void suaPN() {
        System.out.print("Mã phiếu nhập cần sửa (VD: PN001): ");
        String id = sc.nextLine().trim().toUpperCase();
        List<Object[]> cu = purchaseOrderService.findById(id);
        if (cu.isEmpty()) {
            System.out.println("❌ Không tìm thấy!");
            return;
        }

        // Cột: 0 mã PN, 1 mã NCC, 2 mã NV, 3 ngày nhập, 4 tổng tiền, 5 trạng thái, 6
        // ghi chú
        Object[] row = cu.get(0);
        BigDecimal total = row[4] == null ? BigDecimal.ZERO : new BigDecimal(row[4].toString());
        String status = row[5] == null ? "CHO_XU_LY" : row[5].toString();
        String noteCu = row[6] == null ? "" : row[6].toString();

        System.out.println("\n--- PHIẾU NHẬP " + id + " ---");
        System.out.println("Tổng tiền (theo lô) : " + total + " VND");
        System.out.println("Trạng thái          : " + status);
        System.out.println("Ghi chú hiện tại    : " + noteCu);
        System.out.println("⚠️ Tổng tiền và trạng thái do hệ thống tính từ các lô — chỉ được sửa ghi chú.");
        System.out.println("   (Muốn đổi số lượng/giá nhập thì lập phiếu nhập mới.)");
        if ("CHO_XU_LY".equals(status)) {
            System.out.println("👉 Phiếu này CHƯA CHỐT (dở dang) — dùng chức năng [9] Chốt phiếu nhập.");
        }

        System.out.print("Ghi chú mới (Enter = giữ nguyên, VD: Nhập đợt 1 tháng 10): ");
        String note = sc.nextLine();
        if (note.isEmpty())
            note = noteCu;

        System.out.println(purchaseOrderService.update(id, total, status, note) ? "✅ Sửa thành công!" : "❌ Thất bại!");
    }

    private void xoaPN() {
        System.out.print("Mã phiếu nhập cần xóa (VD: PN001): ");
        String id = sc.nextLine().trim().toUpperCase();

        if (purchaseOrderService.findById(id).isEmpty()) {
            System.out.println("❌ Không tìm thấy phiếu nhập " + id + "!");
            return;
        }

        int soLo = purchaseOrderService.demLoCuaPhieu(id);
        if (soLo > 0) {
            System.out.println("❌ KHÔNG THỂ XÓA! Phiếu đã sinh " + soLo + " lô hoa (xóa sẽ mất dữ liệu nhập).");
            System.out.println("👉 Nếu phiếu bị dở dang, dùng [9] Chốt phiếu nhập dở dang.");
            return;
        }

        System.out.print("Xác nhận (y/n): ");
        if (sc.nextLine().equalsIgnoreCase("y")) {
            System.out.println(purchaseOrderService.delete(id) ? "✅ Đã xóa!" : "❌ Thất bại!");
        }
    }

    // Chốt các phiếu nhập bị treo ở CHO_XU_LY (nhập dở / app bị ngắt giữa chừng)
    private void chotPhieuNhap() {
        List<Object[]> ds = purchaseOrderService.phieuChoXuLy();
        if (ds.isEmpty()) {
            System.out.println("✅ Không có phiếu nhập nào dở dang.");
            return;
        }

        System.out.println("\n--- PHIẾU NHẬP DỞ DANG ---");
        System.out.printf("%-5s | %-10s | %-20s | %-20s | %-6s%n",
                "STT", "Mã PN", "Ngày nhập", "Nhà cung cấp", "Số lô");
        System.out.println("-".repeat(72));
        for (int i = 0; i < ds.size(); i++) {
            Object[] r = ds.get(i);
            System.out.printf("%-5d | %-10s | %-20s | %-20s | %-6s%n",
                    (i + 1), r[0], r[1], r[2], r[3]);
        }

        System.out.print("\nChọn phiếu cần chốt (0 = hủy): ");
        int chon;
        try {
            chon = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("❌ Phải nhập số!");
            return;
        }
        if (chon == 0)
            return;
        if (chon < 1 || chon > ds.size()) {
            System.out.println("❌ STT không hợp lệ!");
            return;
        }

        String poId = ds.get(chon - 1)[0].toString();
        int soLo = purchaseOrderService.demLoCuaPhieu(poId);
        if (soLo == 0) {
            System.out.println("❌ Phiếu " + poId + " chưa có lô nào — không có gì để chốt.");
            System.out.println("👉 Dùng [5] Xóa để bỏ phiếu rỗng này.");
            return;
        }

        System.out.println("Phiếu " + poId + " có " + soLo + " lô. Tổng tiền sẽ được tính lại từ các lô.");
        System.out.print("Xác nhận chốt phiếu " + poId + "? (y/n): ");
        if (sc.nextLine().equalsIgnoreCase("y")) {
            System.out.println(purchaseOrderService.chotPhieu(poId)
                    ? "✅ Đã chốt phiếu nhập (trạng thái HOAN_THANH, tổng tiền tính theo lô)."
                    : "❌ Chốt thất bại!");
        }
    }

    private void inBang(List<Object[]> list) {
        InBangHelper.inBang(list, null);
    }

    
    private void timPhieuTheoMa(String maPN) {
        List<PurchaseOrder> kq = purchaseOrderService.timPhieuTheoMaLambda(maPN);
        inDanhSachPhieu(kq);
    }

    
    private void phieuCuaNCC(String maNCC) {
        List<PurchaseOrder> kq = purchaseOrderService.phieuCuaNCCLambda(maNCC);
        inDanhSachPhieu(kq);
    }

    
    private void tongNhapTheoNCC() {
        Map<String, BigDecimal> tong = purchaseOrderService.tongNhapTheoNCCLambda();

        System.out.println("\n--- TỔNG TIỀN NHẬP THEO NHÀ CUNG CẤP ---");
        if (tong.isEmpty()) {
            System.out.println("Không có dữ liệu.");
            return;
        }
        List<Object[]> rows = new java.util.ArrayList<>();
        tong.forEach((ncc, tien) -> rows.add(new Object[]{ncc, tien}));
        InBangHelper.inBang(new String[]{"Mã NCC", "Tổng nhập"}, rows);
    }

    // In danh sách phiếu nhập (dùng chung cho các truy vấn lambda)
    private void inDanhSachPhieu(List<PurchaseOrder> ds) {
        if (ds.isEmpty()) {
            System.out.println("Không có dữ liệu.");
            return;
        }
        List<Object[]> rows = ds.stream().map(po -> new Object[]{
            po.getPurchaseOrderId(), po.getSupplierId(), po.getEmployeeId(),
            po.getImportDate(), po.getTotalAmount(), po.getStatus(),
            po.getNote() == null ? "" : po.getNote()
        }).toList();
        InBangHelper.inBang(new String[]{"Mã PN", "Mã NCC", "Mã NV", "Ngày nhập", "Tổng tiền", "Trạng thái", "Ghi chú"}, rows);
    }
}