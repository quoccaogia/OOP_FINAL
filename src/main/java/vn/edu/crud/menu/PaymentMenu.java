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
            System.out.println("║  4. Hủy giao dịch                   ║");
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
                    System.out.print("Phương thức (TIEN_MAT/CHUYEN_KHOAN/MOMO/VNPAY): ");
                    inBang(dao.thanhToanTheoPhuongThuc(sc.nextLine()));
                    break;
                case 3:
                    inBang(dao.doanhThuHomNay());
                    break;
                case 4:
                    System.out.print("Mã thanh toán cần hủy: ");
                    String id = sc.nextLine().trim().toUpperCase();
                    String maDon = dao.layMaDon(id);
                    if (maDon == null) {
                        System.out.println("❌ Không tìm thấy giao dịch!");
                        break;
                    }
                    if ("DA_HUY".equals(dao.layTrangThaiThanhToan(id))) {
                        System.out.println("⚠️ Giao dịch " + id + " đã bị hủy trước đó!");
                        break;
                    }
                    String trangThai = orderDAO.layTrangThaiDon(maDon);
                    if (trangThai == null) {
                        System.out.println("❌ Không đọc được trạng thái của đơn " + maDon + "!");
                        break;
                    }
                    // Chỉ hủy giao dịch khi đơn CHƯA GIAO (còn ở khâu chuẩn bị).
                    // Đơn đã giao / đã hoàn thành / đã hủy thì không được hủy giao dịch.
                    boolean conSuaDuoc = "DANG_CHUAN_BI".equals(trangThai)
                            || "CHO_XU_LY".equals(trangThai)
                            || "CHO_XAC_NHAN".equals(trangThai);
                    if (!conSuaDuoc) {
                        System.out.println("❌ Đơn " + maDon + " đang ở trạng thái " + trangThai
                                + " — chỉ hủy được giao dịch khi đơn CHƯA GIAO.");
                        break;
                    }
                    System.out.print("Xác nhận hủy giao dịch " + id + " (y/n): ");
                    if (sc.nextLine().equalsIgnoreCase("y"))
                        System.out.println(dao.xoaThanhToan(id) ? "✅ Đã hủy giao dịch!" : "❌ Thất bại!");
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