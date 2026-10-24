package vn.edu.crud.menu;

import vn.edu.crud.service.FlowerBatchService;
import vn.edu.crud.service.FlowerService;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import vn.edu.crud.model.FlowerBatch;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class FlowerBatchMenu {
    private final Scanner sc = new Scanner(System.in);
    private final FlowerBatchService flowerBatchService = new FlowerBatchService();
    private final FlowerService flowerService = new FlowerService();

    public void hienThi() {
        int chon;
        do {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║           QUẢN LÝ LÔ HOA             ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  1. Xem tất cả lô hoa                ║");
            System.out.println("║  2. Cập nhật trạng thái lô           ║");
            System.out.println("║  3. Thanh lý/Hủy lô hoa (Hết hạn)    ║");
            System.out.println("║  6. Đồng bộ tồn kho từ lô hoa        ║");
            System.out.println("║  ── BÁO CÁO ──                       ║");
            System.out.println("║  4. Lô hoa còn hàng                  ║");
            System.out.println("║  5. Lô hoa sắp hết hạn (<= 3 ngày)   ║");
            System.out.println("║  7. Lô nhập quá 7 ngày               ║");
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
                    inDanhSach(flowerBatchService.findAll());
                    break;
                case 2:
                    capNhatTrangThai();
                    break;
                case 3:
                    thanhLyLoHoa();
                    break;
                case 4:
                    loConHang();
                    break;
                case 5:
                    loSapHetHan(3);
                    break;
                case 6:
                    dongBoTonKho();
                    break;
                case 7:
                    loNhapQua7Ngay();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Chọn sai!");
            }
        } while (chon != 0);
    }

    // =====================================================
    // HÀM IN BẢNG
    // =====================================================
    private void inDanhSach(List<Object[]> list) {
        if (list.isEmpty()) {
            System.out.println("Không có dữ liệu.");
            return;
        }

        System.out.printf("%-7s | %-8s | %-18s | %-7s | %-20s | %-8s | %-8s | %-10s | %-12s | %-10s%n",
                "Mã Lô", "Mã PN", "Nhà Cung Cấp", "Mã Hoa", "Tên Hoa", "SL Nhập", "SL Còn", "Đơn Giá", "Hạn SD",
                "Trạng Thái");
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
        List<Object[]> list = flowerBatchService.findAll();
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
                if (chon == 0)
                    return null;
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
        if (id == null)
            return;

        System.out.println("Chọn trạng thái mới:");
        System.out.println("  1. AVAILABLE (Đang bán)");
        System.out.println("  2. EXPIRED (Hết hạn/Hỏng)");
        System.out.println("  3. SOLD_OUT (Đã bán hết)");
        System.out.print("Nhập số chọn (1-3): ");

        String status;
        switch (sc.nextLine().trim()) {
            case "1":
                status = "AVAILABLE";
                break;
            case "2":
                status = "EXPIRED";
                break;
            case "3":
                status = "SOLD_OUT";
                break;
            default:
                System.out.println("❌ Chỉ được chọn 1, 2 hoặc 3 — không đổi gì cả!");
                return;
        }

        if (flowerBatchService.doiTrangThaiLo(id, status)) {
            System.out.println("✅ Cập nhật thành công! Tồn kho của hoa đã được cộng/trừ tương ứng.");
        } else {
            System.out.println("❌ Thất bại!");
        }
    }

    private void thanhLyLoHoa() {
        System.out.println("\n--- THANH LÝ / HỦY LÔ HOA ---");
        String id = chonLoHoa();
        if (id == null)
            return;

        System.out.println("⚠️ Thanh lý sẽ: chuyển lô sang EXPIRED, đưa số lượng còn về 0");
        System.out.println("   và TRỪ số hoa đó khỏi tồn kho (không bán được nữa).");
        System.out.print("Bạn có chắc chắn muốn thanh lý lô hoa này? (y/n): ");
        if (sc.nextLine().equalsIgnoreCase("y")) {
            if (flowerBatchService.thanhLyLo(id)) {
                System.out.println("✅ Đã thanh lý lô hoa và trừ tồn kho tương ứng!");
            } else {
                System.out.println("❌ Thất bại!");
            }
        }
    }

    // Đồng bộ lại tồn kho của mọi loại hoa theo số lượng còn trong lô.
    private void dongBoTonKho() {
        System.out.println("\n--- ĐỒNG BỘ TỒN KHO TỪ LÔ HOA ---");
        System.out.println("Tồn kho sẽ được đặt lại = tổng số còn trong các lô AVAILABLE.");
        System.out.println("⚠️ Hoa nào chưa có lô (nhập tay) thì giữ nguyên.");
        System.out.println("⚠️ Phần tồn kho không ứng với lô nào sẽ bị bỏ.");
        System.out.print("Xác nhận đồng bộ? (y/n): ");
        if (!sc.nextLine().equalsIgnoreCase("y"))
            return;

        int soDong = flowerService.dongBoTonKhoTuLo();
        System.out.println("✅ Đã đồng bộ " + soDong + " loại hoa theo lô.");
    }

    private void inBangRutGon(List<Object[]> list, String title) {
        System.out.println("\n--- " + title + " ---");
        if (list.isEmpty()) {
            System.out.println("Không có dữ liệu.");
            return;
        }

        System.out.printf("%-8s | %-20s | %-18s | %-10s | %-12s%n",
                "Mã Lô", "Tên Hoa", "Nhà Cung Cấp", "SL Còn", "Hạn SD");
        System.out.println("-".repeat(80));

        for (Object[] row : list) {
            String expiry = row.length > 4 && row[4] != null ? row[4].toString() : "";
            System.out.printf("%-8s | %-20s | %-18s | %-10s | %-12s%n",
                    row[0], row[1], row[2], row[3], expiry);
        }
    }

    
    private void loNhapQua7Ngay() {
       
        List<FlowerBatch> kq = flowerBatchService.loNhapQua7NgayLambda();

        System.out.println("\n--- LÔ HOA NHẬP QUÁ 7 NGÀY ---");
        if (kq.isEmpty()) {
            System.out.println("Không có lô nào nhập quá 7 ngày.");
            return;
        }
        System.out.printf("%-8s | %-8s | %-12s | %-8s | %-10s%n",
                "Mã Lô", "Mã Hoa", "Ngày Nhập", "SL Còn", "Trạng Thái");
        System.out.println("-".repeat(58));
        for (FlowerBatch b : kq) {
            System.out.printf("%-8s | %-8s | %-12s | %-8d | %-10s%n",
                    b.getBatchId(), b.getFlowerId(), b.getImportDate().toLocalDate(),
                    b.getQuantityRemaining(), b.getStatus());
        }
    }

    
    private void loConHang() {
       
        List<FlowerBatch> kq = flowerBatchService.loConHangLambda();

        System.out.println("\n--- LÔ HOA CÒN HÀNG ---");
        if (kq.isEmpty()) {
            System.out.println("Không có lô nào còn hàng.");
            return;
        }
        System.out.printf("%-8s | %-8s | %-12s | %-8s | %-12s%n",
                "Mã Lô", "Mã Hoa", "Ngày Nhập", "SL Còn", "Hạn SD");
        System.out.println("-".repeat(60));
        for (FlowerBatch b : kq) {
            System.out.printf("%-8s | %-8s | %-12s | %-8d | %-12s%n",
                    b.getBatchId(), b.getFlowerId(),
                    b.getImportDate() == null ? "" : b.getImportDate().toLocalDate(),
                    b.getQuantityRemaining(),
                    b.getExpiryDate() == null ? "" : b.getExpiryDate());
        }
    }

    
    private void loSapHetHan(int soNgay) {
        
        LocalDate homNay = LocalDate.now();

        List<FlowerBatch> kq = flowerBatchService.loSapHetHanLambda(soNgay);

        System.out.println("\n--- LÔ HOA SẮP HẾT HẠN (<= " + soNgay + " NGÀY) ---");
        if (kq.isEmpty()) {
            System.out.println("Không có lô nào sắp hết hạn.");
            return;
        }
        System.out.printf("%-8s | %-8s | %-8s | %-12s | %-8s%n",
                "Mã Lô", "Mã Hoa", "SL Còn", "Hạn SD", "Còn Ngày");
        System.out.println("-".repeat(56));
        for (FlowerBatch b : kq) {
            System.out.printf("%-8s | %-8s | %-8d | %-12s | %-8d%n",
                    b.getBatchId(), b.getFlowerId(), b.getQuantityRemaining(),
                    b.getExpiryDate(), ChronoUnit.DAYS.between(homNay, b.getExpiryDate()));
        }
    }
}