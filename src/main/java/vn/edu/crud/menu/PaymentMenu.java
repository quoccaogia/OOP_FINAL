package vn.edu.crud.menu;

import vn.edu.crud.dao.PaymentDAO;
import vn.edu.crud.dao.OrderDAO;

import java.util.List;
import java.util.Scanner;

public class PaymentMenu {
    private final Scanner sc = new Scanner(System.in);
    private final PaymentDAO dao = new PaymentDAO();
    private final OrderDAO orderDAO = new OrderDAO();

    public void hienThi() {
        int chon;
        do {
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

            try {
                chon = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                chon = -1;
            }

            switch (chon) {
                case 1:
                    inBang(dao.tatCaThanhToan());
                    break;
                case 2:
                    String[] dsPhuongThuc = { "TIEN_MAT", "CHUYEN_KHOAN", "MOMO", "THE_TIN_DUNG" };
                    System.out.println("Chọn phương thức thanh toán:");
                    for (int i = 0; i < dsPhuongThuc.length; i++) {
                        System.out.println("  " + (i + 1) + ". " + dsPhuongThuc[i]);
                    }
                    System.out.print("Nhập số chọn (1-" + dsPhuongThuc.length + "): ");
                    int soPT;
                    try {
                        soPT = Integer.parseInt(sc.nextLine().trim());
                    } catch (NumberFormatException e) {
                        System.out.println("❌ Phải nhập số!");
                        break;
                    }
                    if (soPT < 1 || soPT > dsPhuongThuc.length) {
                        System.out.println("❌ Chỉ được chọn từ 1 đến " + dsPhuongThuc.length + "!");
                        break;
                    }
                    inBang(dao.thanhToanTheoPhuongThuc(dsPhuongThuc[soPT - 1]));
                    break;
                case 3:
                    inBang(dao.doanhThuHomNay());
                    break;
                case 4:
                    System.out.print("Mã thanh toán cần hủy (VD: P001): ");
                    String id = sc.nextLine().trim().toUpperCase();
                    String maDon = dao.layMaDon(id);
                    if (maDon == null) {
                        System.out.println("❌ Không tìm thấy giao dịch!");
                        break;
                    }
                    if ("DA_HUY".equals(dao.layTrangThaiThanhToan(id))
                            || "DA_HOAN_TIEN".equals(dao.layTrangThaiThanhToan(id))) {
                        System.out.println("⚠️ Giao dịch " + id + " đã bị hủy/hoàn trước đó!");
                        break;
                    }
                    String trangThai = orderDAO.layTrangThaiDon(maDon);
                    if (trangThai == null) {
                        System.out.println("❌ Không đọc được trạng thái của đơn " + maDon + "!");
                        break;
                    }

                    boolean chuaGiao = "DANG_CHUAN_BI".equals(trangThai)
                            || "CHO_XU_LY".equals(trangThai)
                            || "CHO_XAC_NHAN".equals(trangThai);
                    // Đơn đã đóng (hủy / giao thất bại) mà còn giữ tiền -> cho HOÀN TIỀN
                    boolean donDaDong = "DA_HUY".equals(trangThai) || "GIAO_THAT_BAI".equals(trangThai);

                    if (!chuaGiao && !donDaDong) {
                        System.out.println("❌ Đơn " + maDon + " đang ở trạng thái " + trangThai
                                + " — không thể hủy/hoàn giao dịch.");
                        break;
                    }

                    if (donDaDong) {
                        System.out.println("ℹ️ Đơn đã đóng nhưng còn giữ tiền — thao tác này là HOÀN TIỀN cho khách.");
                        System.out.print("Xác nhận hoàn tiền giao dịch " + id + " (y/n): ");
                        if (sc.nextLine().equalsIgnoreCase("y"))
                            System.out.println(dao.hoanTien(id) ? "✅ Đã hoàn tiền!" : "❌ Thất bại!");
                    } else {
                        System.out.print("Xác nhận hủy giao dịch " + id + " (y/n): ");
                        if (sc.nextLine().equalsIgnoreCase("y"))
                            System.out.println(dao.xoaThanhToan(id) ? "✅ Đã hủy giao dịch!" : "❌ Thất bại!");
                    }
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Chọn sai!");
            }
        } while (chon != 0);
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