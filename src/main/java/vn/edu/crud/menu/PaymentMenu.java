package vn.edu.crud.menu;

import vn.edu.crud.dao.OrderDAO;
import vn.edu.crud.dao.PaymentDAO;
import vn.edu.crud.model.Payment;
import vn.edu.crud.util.AnToan;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class PaymentMenu {
    private final Scanner sc = new Scanner(System.in);
    private final PaymentDAO dao = new PaymentDAO();
    private final OrderDAO orderDAO = new OrderDAO();

    public void hienThi() {
        int chon = -1;
        do {
            try {
                System.out.println("\n╔══════════════════════════════════════╗");
                System.out.println("║       QUẢN LÝ THANH TOÁN             ║");
                System.out.println("╠══════════════════════════════════════╣");
                System.out.println("║  1. Xem tất cả                       ║");
                System.out.println("║  2. Theo phương thức                 ║");
                System.out.println("║  3. Doanh thu hôm nay                ║");
                System.out.println("║  4. Hủy giao dịch / Hoàn tiền        ║");
                System.out.println("║  0. Quay lại                         ║");
                System.out.println("╚══════════════════════════════════════╝");
                System.out.print("Chọn: ");

                chon = Integer.parseInt(sc.nextLine().trim());

                switch (chon) {
                    case 1:
                        xemTatCa();
                        break;
                    case 2:
                        locTheoPhuongThuc();
                        break;
                    case 3:
                        doanhThuHomNay();
                        break;
                    case 4:
                        huyHoanGiaoDich();
                        break;
                    case 0:
                        System.out.println("Quay lại menu chính.");
                        break;
                    default:
                        System.out.println("⚠️ Chọn sai! Vui lòng chọn số từ 0 đến 4 (VD: 1 = xem tất cả, 3 = xem doanh thu).");
                }
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Vui lòng nhập SỐ, không nhập chữ! Gợi ý: chọn số từ 0 đến 4 (VD: 1 để xem danh sách, 0 để quay lại).");
                chon = -1;
            } catch (Exception e) {
                AnToan.baoLoi(e, "số trong menu 0-4. VD: 1 = xem tất cả, 4 = hủy giao dịch/hoàn tiền");
                chon = -1;
            }
        } while (chon != 0);
    }

    // =====================================================
    // 1. XEM TẤT CẢ GIAO DỊCH
    // =====================================================
    private void xemTatCa() {
        try {
            inBang(dao.tatCaThanhToan());
        } catch (Exception e) {
            AnToan.baoLoi(e, "không thể tải danh sách thanh toán từ cơ sở dữ liệu");
        }
    }

    // =====================================================
    // 2. LỌC THEO PHƯƠNG THỨC THANH TOÁN
    // =====================================================
    private void locTheoPhuongThuc() {
        try {
            String[] dsPhuongThuc = { "TIEN_MAT", "CHUYEN_KHOAN", "MOMO", "THE_TIN_DUNG" };
            System.out.println("\n--- CHỌN PHƯƠNG THỨC THANH TOÁN ---");
            for (int i = 0; i < dsPhuongThuc.length; i++) {
                System.out.println("  " + (i + 1) + ". " + dsPhuongThuc[i]);
            }
            System.out.println("  0. Hủy thao tác");
            System.out.print("Nhập số chọn (1-" + dsPhuongThuc.length + ", 0 để hủy): ");

            int soPT;
            try {
                soPT = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Vui lòng nhập SỐ! Gợi ý: nhập số từ 1 đến " + dsPhuongThuc.length + " (VD: 1 cho TIEN_MAT).");
                return;
            }

            if (soPT == 0) {
                System.out.println("Đã hủy thao tác.");
                return;
            }

            if (soPT < 1 || soPT > dsPhuongThuc.length) {
                System.out.println("⚠️ Lựa chọn không hợp lệ! Gợi ý: chỉ chọn từ 1 đến " + dsPhuongThuc.length + " (VD: 1 cho TIEN_MAT, 2 cho CHUYEN_KHOAN).");
                return;
            }

            inTheoPhuongThuc(dsPhuongThuc[soPT - 1]);
        } catch (Exception e) {
            AnToan.baoLoi(e, "chọn số từ 1 đến 4. VD: 1 = TIEN_MAT, 2 = CHUYEN_KHOAN, 3 = MOMO, 4 = THE_TIN_DUNG");
        }
    }

    // Lambda: các giao dịch theo một phương thức thanh toán
    private void inTheoPhuongThuc(String phuongThuc) {
        try {
            List<Payment> ds = dao.layDanhSachThanhToan();

            List<Payment> kq = ds.stream()
                    .filter(p -> phuongThuc.equals(p.getPaymentMethod()))
                    .collect(Collectors.toList());

            System.out.println("\n--- GIAO DỊCH PHƯƠNG THỨC " + phuongThuc + " ---");
            if (kq.isEmpty()) {
                System.out.println("Không có giao dịch nào sử dụng phương thức này.");
                return;
            }
            System.out.printf("%-8s | %-8s | %-14s | %-20s | %-22s%n",
                    "Mã TT", "Mã Đơn", "Số Tiền", "Ngày TT", "Trạng Thái");
            System.out.println("-".repeat(84));
            for (Payment p : kq) {
                System.out.printf("%-8s | %-8s | %-14s | %-20s | %-22s%n",
                        p.getPaymentId(), p.getOrderId(), p.getAmount(),
                        p.getPaymentDate() == null ? "" : p.getPaymentDate().toLocalDate(),
                        p.getPaymentStatus());
            }
        } catch (Exception e) {
            AnToan.baoLoi(e, "phương thức thanh toán (TIEN_MAT, CHUYEN_KHOAN, MOMO, THE_TIN_DUNG)");
        }
    }

    // =====================================================
    // 3. DOANH THU HÔM NAY (Java Stream API)
    // =====================================================
    private void doanhThuHomNay() {
        try {
            List<Payment> ds = dao.layDanhSachThanhToan();
            LocalDate homNay = LocalDate.now();

            BigDecimal tong = ds.stream()
                    .filter(p -> "THANH_TOAN_THANH_CONG".equals(p.getPaymentStatus())
                            && p.getPaymentDate() != null
                            && p.getPaymentDate().toLocalDate().equals(homNay)
                            && p.getAmount() != null)
                    .map(Payment::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            System.out.println("\n--- DOANH THU HÔM NAY ---");
            System.out.println("Doanh thu hôm nay (" + homNay + "): " + tong + " VND");
        } catch (Exception e) {
            AnToan.baoLoi(e, "tính toán doanh thu các giao dịch hôm nay");
        }
    }

    // =====================================================
    // 4. HỦY GIAO DỊCH / HOÀN TIỀN
    // =====================================================
    private void huyHoanGiaoDich() {
        try {
            System.out.print("Mã thanh toán cần hủy / hoàn (VD: P001, Enter để hủy): ");
            String id = sc.nextLine().trim().toUpperCase();
            if (id.isEmpty()) {
                System.out.println("Đã hủy thao tác.");
                return;
            }

            String maDon = dao.layMaDon(id);
            if (maDon == null) {
                System.out.println("❌ Không tìm thấy giao dịch có mã: " + id);
                System.out.println("👉 Gợi ý: Mã thanh toán có dạng P001, P002... Bạn có thể xem danh sách tại chức năng [1].");
                return;
            }

            String trangThaiTT = dao.layTrangThaiThanhToan(id);
            if ("DA_HUY".equals(trangThaiTT) || "DA_HOAN_TIEN".equals(trangThaiTT)) {
                System.out.println("⚠️ Giao dịch " + id + " đã ở trạng thái [" + trangThaiTT + "] trước đó, không thể thực hiện lại!");
                return;
            }

            String trangThai = orderDAO.layTrangThaiDon(maDon);
            if (trangThai == null) {
                System.out.println("❌ Không đọc được trạng thái của đơn hàng " + maDon + "!");
                return;
            }

            boolean chuaGiao = "DANG_CHUAN_BI".equals(trangThai)
                    || "CHO_XU_LY".equals(trangThai)
                    || "CHO_XAC_NHAN".equals(trangThai);
            // Đơn đã đóng (hủy / giao thất bại) mà còn giữ tiền -> cho HOÀN TIỀN
            boolean donDaDong = "DA_HUY".equals(trangThai) || "GIAO_THAT_BAI".equals(trangThai);

            if (!chuaGiao && !donDaDong) {
                System.out.println("❌ Đơn " + maDon + " đang ở trạng thái [" + trangThai
                        + "] — không thể hủy/hoàn giao dịch.");
                System.out.println("👉 Gợi ý: Chỉ hủy giao dịch khi đơn chưa giao (Đang chuẩn bị hoa), hoặc hoàn tiền khi đơn đã đóng (Đã hủy / Giao thất bại).");
                return;
            }

            if (donDaDong) {
                System.out.println("ℹ️ Đơn hàng đã đóng (" + trangThai + ") nhưng còn giữ tiền — thao tác này là HOÀN TIỀN cho khách.");
                System.out.print("Xác nhận hoàn tiền giao dịch " + id + "? (y/n, VD: y): ");
                String xacNhan = sc.nextLine().trim();
                if (xacNhan.equalsIgnoreCase("y")) {
                    if (dao.hoanTien(id)) {
                        System.out.println("✅ Đã hoàn tiền thành công cho giao dịch " + id + "!");
                    } else {
                        System.out.println("❌ Hoàn tiền thất bại!");
                    }
                } else {
                    System.out.println("Đã hủy thao tác hoàn tiền.");
                }
            } else {
                System.out.print("Xác nhận hủy giao dịch " + id + "? (y/n, VD: y): ");
                String xacNhan = sc.nextLine().trim();
                if (xacNhan.equalsIgnoreCase("y")) {
                    if (dao.xoaThanhToan(id)) {
                        System.out.println("✅ Đã hủy giao dịch " + id + " thành công!");
                    } else {
                        System.out.println("❌ Hủy giao dịch thất bại!");
                    }
                } else {
                    System.out.println("Đã hủy thao tác.");
                }
            }
        } catch (Exception e) {
            AnToan.baoLoi(e, "mã thanh toán dạng P001 (VD: P001, P002...) và nhập 'y' hoặc 'n' để xác nhận");
        }
    }

    // =====================================================
    // HELPER IN BẢNG
    // =====================================================
    private void inBang(List<Object[]> list) {
        try {
            if (list == null || list.isEmpty()) {
                System.out.println("Không có dữ liệu.");
                return;
            }
            System.out.printf("%-8s | %-8s | %-14s | %-16s | %-20s | %-22s%n",
                    "Mã TT", "Mã Đơn", "Số Tiền", "Phương Thức", "Ngày TT", "Trạng Thái");
            System.out.println("-".repeat(100));
            for (Object[] row : list) {
                if (row.length >= 6) {
                    System.out.printf("%-8s | %-8s | %-14s | %-16s | %-20s | %-22s%n",
                            row[0], row[1], row[2], row[3], row[4], row[5]);
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (Object o : row)
                        sb.append(o).append(" | ");
                    System.out.println(sb);
                }
            }
        } catch (Exception e) {
            AnToan.baoLoi(e, "hiển thị danh sách thanh toán");
        }
    }
}
