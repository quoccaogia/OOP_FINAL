package vn.edu.crud.menu;

import vn.edu.crud.dao.CustomerDAO;
import vn.edu.crud.dao.EmployeeDAO;
import vn.edu.crud.dao.FlowerBatchDAO;
import vn.edu.crud.dao.FlowerDAO;
import vn.edu.crud.dao.OrderDAO;
import vn.edu.crud.dao.PaymentDAO;
import vn.edu.crud.model.Customer;
import vn.edu.crud.model.Employee;
import vn.edu.crud.model.Flower;
import vn.edu.crud.model.Order;
import vn.edu.crud.model.Payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OrderMenu {

    private final Scanner sc = new Scanner(System.in);
    private final OrderDAO orderDAO = new OrderDAO();
    private final PaymentDAO paymentDAO = new PaymentDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final EmployeeDAO employeeDAO = new EmployeeDAO();
    private final FlowerDAO flowerDAO = new FlowerDAO();
    private final FlowerBatchDAO batchDAO = new FlowerBatchDAO();

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void hienThi() {
        int chon = -1;
        do {
            try {
                System.out.println("\n╔══════════════════════════════════════╗");
                System.out.println("║        QUẢN LÝ ĐƠN HÀNG              ║");
                System.out.println("╠══════════════════════════════════════╣");
                System.out.println("║  ── THAO TÁC ──                      ║");
                System.out.println("║  1. Tạo đơn hàng mới                 ║");
                System.out.println("║  2. Thêm hoa vào đơn                 ║");
                System.out.println("║  3. Thanh toán đơn hàng              ║");
                System.out.println("║  4. Cập nhật trạng thái đơn          ║");
                System.out.println("║  5. Xóa chi tiết hoa khỏi đơn        ║");
                System.out.println("║  6. Xóa đơn hàng                     ║");
                System.out.println("║  7. Xem chi tiết 1 đơn               ║");
                System.out.println("║  ── BÁO CÁO ──                       ║");
                System.out.println("║  8. Đơn hàng giá trị cao (> 500k)    ║");
                System.out.println("║  9. Đơn chưa giao hôm nay            ║");
                System.out.println("║ 10. Doanh thu hôm nay                ║");
                System.out.println("║ 11. Đơn hàng của 1 khách (Nhập SĐT)  ║");
                System.out.println("║ 12. Thống kê đơn theo trạng thái     ║");
                System.out.println("║ 13. Đơn có nhiều loại hoa (>= 3)     ║");
                System.out.println("║ 14. Khách đã từng đặt hàng           ║");
                System.out.println("║ 15. Khách chưa từng đặt hàng         ║");
                System.out.println("║ 16. Khách nhiều đơn nhất             ║");
                System.out.println("║ 17. NV nhiều đơn nhất                ║");
                System.out.println("║ 18. Tổng chi tiêu theo khách         ║");
                System.out.println("║  0. Quay lại                         ║");
                System.out.println("╚══════════════════════════════════════╝");
                System.out.print("Chọn: ");

                chon = Integer.parseInt(sc.nextLine().trim());

                switch (chon) {
                    case 1: taoDonHang(); break;
                    case 2: themHoaVaoDon(); break;
                    case 3: thanhToanDon(); break;
                    case 4: capNhatTrangThai(); break;
                    case 5: xoaChiTiet(); break;
                    case 6: xoaDon(); break;
                    case 7: xemChiTietDon(); break;
                    case 8: inBang(orderDAO.donHangGiaCao(new BigDecimal("500000"))); break;
                    case 9: inDsDon(orderDAO.donChuaGiaoHomNay()); break;
                    case 10: System.out.println("Doanh thu hôm nay: " + orderDAO.doanhThuHomNay() + " VND"); break;
                    case 11: xemDonCuaKhach(); break;
                    case 12: inBang(orderDAO.thongKeDonTheoTrangThai()); break;
                    case 13: inBang(orderDAO.donCoNhieuLoaiHoa(3)); break;
                    case 14: inBang(orderDAO.khachDaTungDatHang()); break;
                    case 15: inBang(orderDAO.khachChuaTungDatHang()); break;
                    case 16: inBang(orderDAO.khachNhieuDonNhat()); break;
                    case 17: inBang(orderDAO.nhanVienNhieuDonNhat()); break;
                    case 18: inBang(orderDAO.tongChiTieuTheoKhach()); break;
                    case 0: System.out.println("Quay lại menu chính."); break;
                    default: System.out.println("⚠️ Chọn sai, vui lòng chọn lại!");
                }
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Vui lòng nhập SỐ, không nhập chữ!");
                chon = -1;
            } catch (Exception e) {
                System.out.println("❌ Lỗi không mong muốn: " + e.getMessage());
                chon = -1;
            }
        } while (chon != 0);
    }

    // Hàm dịch mã trạng thái sang tiếng Việt để in ra màn hình
    private String dichTrangThai(String status) {
        if (status == null) return "Không rõ";
        switch (status) {
            case "DANG_CHUAN_BI":
            case "CHO_XU_LY":
            case "CHO_XAC_NHAN":
                return "Đang chuẩn bị hoa";
            case "DANG_GIAO":
                return "Đang giao hàng";
            case "HOAN_THANH":
                return "Hoàn thành";
            case "GIAO_THAT_BAI":
                return "Giao thất bại";
            case "DA_HUY":
                return "Đã hủy";
            default:
                return status;
        }
    }

    // Tình trạng thanh toán của đơn: chỉ có 2 mức sau khi đã thu tiền
    //   - Đã trả đủ cọc (20%)
    //   - Đã trả hết đơn
    private String dichTrangThaiThanhToan(BigDecimal tongTien, BigDecimal daTra) {
        if (tongTien == null || tongTien.compareTo(BigDecimal.ZERO) <= 0)
            return "Chưa có gì để thu";
        if (daTra == null || daTra.compareTo(BigDecimal.ZERO) <= 0)
            return "Chưa đặt cọc";
        if (daTra.compareTo(tongTien) < 0)
            return "Đã trả đủ cọc";
        return "Đã trả hết đơn";
    }

    // =====================================================
    // 1. TẠO ĐƠN HÀNG
    // =====================================================
    private void taoDonHang() {
        try {
            System.out.println("\n--- TẠO ĐƠN HÀNG MỚI ---");

            Order o = new Order();
            o.setOrderId(orderDAO.sinhMaDonMoi());
            System.out.println("Mã đơn tự sinh: " + o.getOrderId());

            System.out.print("Nhập SĐT khách hàng (Bấm Enter để tạo khách mới): ");
            String sdtTho = sc.nextLine().trim();
            String sdt = customerDAO.chuanHoaSdt(sdtTho);

            if (!sdt.isEmpty()) {
                List<Customer> dsKH = customerDAO.timTheoSdt(sdt);
                if (dsKH.isEmpty()) {
                    System.out.println("❌ Không tìm thấy khách hàng nào dùng SĐT: " + sdt);
                    System.out.print("👉 Tạo khách hàng mới với SĐT này? (y/n): ");
                    if (sc.nextLine().equalsIgnoreCase("y")) {
                        o.setCustomerId(taoKhachHangNhanh(sdt));
                    } else {
                        System.out.println("Đã hủy tạo đơn hàng.");
                        return;
                    }
                } else {
                    Customer kh = dsKH.get(0);
                    o.setCustomerId(kh.getCustomerId());
                    System.out.println("✅ Khách hàng: " + kh.getName() + " (Mã: " + kh.getCustomerId() + ")");
                }
            } else {
                o.setCustomerId(taoKhachHangNhanh(""));
            }

            if (o.getCustomerId() == null) {
                System.out.println("❌ Tạo đơn hàng thất bại (Chưa có khách hàng).");
                return;
            }

            List<Employee> dsNV = employeeDAO.findAll();
            if (dsNV.isEmpty()) {
                System.out.println("❌ Hệ thống chưa có nhân viên nào!");
                return;
            }
            System.out.println("\n--- CHỌN NHÂN VIÊN PHỤ TRÁCH ---");
            for (int i = 0; i < dsNV.size(); i++) {
                Employee nv = dsNV.get(i);
                System.out.printf("  %d. %-20s (Mã: %s - %s)\n", (i + 1), nv.getName(), nv.getEmployeeId(), nv.getRole());
            }

            String employeeId = null;
            while (employeeId == null) {
                System.out.print("Nhập số chọn nhân viên (1-" + dsNV.size() + "): ");
                try {
                    int chonNV = Integer.parseInt(sc.nextLine());
                    if (chonNV >= 1 && chonNV <= dsNV.size()) {
                        employeeId = dsNV.get(chonNV - 1).getEmployeeId();
                    } else {
                        System.out.println("⚠️ Vui lòng chọn đúng số!");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("⚠️ Vui lòng nhập số!");
                }
            }
            o.setEmployeeId(employeeId);

            o.setOrderDate(LocalDateTime.now());
            System.out.println("Ngày đặt: " + o.getOrderDate().format(FMT));

            System.out.print("Ngày giao (yyyy-MM-dd HH:mm:ss, Enter = +2h): ");
            String dateStr = sc.nextLine().trim();
            if (dateStr.isEmpty()) {
                o.setDeliveryDate(o.getOrderDate().plusHours(2));
                System.out.println("→ Dùng mặc định: " + o.getDeliveryDate().format(FMT));
            } else {
                try {
                    if (dateStr.length() == 16) dateStr += ":00";
                    o.setDeliveryDate(LocalDateTime.parse(dateStr.replace(" ", "T")));
                } catch (DateTimeParseException e) {
                    System.out.println("❌ Ngày giờ không hợp lệ! Lấy mặc định +2h.");
                    o.setDeliveryDate(o.getOrderDate().plusHours(2));
                }
            }

            System.out.print("Địa chỉ giao (Enter = lấy địa chỉ khách): ");
            String addr = sc.nextLine().trim();
            if (addr.isEmpty()) {
                addr = layDiaChiKhach(o.getCustomerId());
                System.out.println("→ Dùng địa chỉ khách: " + addr);
            }
            o.setDeliveryAddress(addr);

            // ĐỔI TRẠNG THÁI MẶC ĐỊNH THÀNH DANG_CHUAN_BI
            o.setOrderStatus("DANG_CHUAN_BI");

            System.out.println("\n>>> Đang tạo đơn...");
            if (orderDAO.taoDonHang(o)) {
                System.out.println("✅ Tạo đơn thành công! Mã đơn: " + o.getOrderId());
                System.out.println("👉 Hãy vào chức năng [2] để thêm hoa vào đơn.");
            } else {
                System.out.println("❌ Tạo đơn thất bại!");
            }
        } catch (Exception e) {
            System.out.println("❌ Lỗi khi tạo đơn: " + e.getMessage());
        }
    }

    // =====================================================
    // 2. THÊM HOA VÀO ĐƠN
    // =====================================================
    private void themHoaVaoDon() {
        try {
            System.out.println("\n--- THÊM HOA VÀO ĐƠN ---");

            List<Order> dsDon = orderDAO.findAll();

            if (dsDon.isEmpty()) {
                System.out.println("⚠️ Chưa có đơn hàng nào!");
                return;
            }

            System.out.println("\n╔═══════════════════════════════════════════════════════════════════════╗");
            System.out.println("║                     DANH SÁCH ĐƠN HÀNG HIỆN CÓ                        ║");
            System.out.println("╠════╦══════════╦════════════════════════════╦════════════════════╦═════════╣");
            System.out.printf("║ %-2s ║ %-8s ║ %-26s ║ %-18s ║ %-7s ║%n", "STT", "Mã đơn", "Khách hàng", "Trạng thái", "Số hoa");
            System.out.println("╠════╬══════════╬════════════════════════════╬════════════════════╬═════════╣");

            int sttDon = 1;
            for (Order o : dsDon) {
                System.out.printf("║ %-2d ║ %-8s ║ %-26s ║ %-18s ║ %-7d ║%n",
                        sttDon++, o.getOrderId(), catChuoi(layTenKhach(o.getCustomerId()), 26),
                        catChuoi(dichTrangThai(o.getOrderStatus()), 18), demSoLoaiHoaTrongDon(o.getOrderId()));
            }
            System.out.println("╚════╩══════════╩════════════════════════════╩════════════════════╩═════════╝");
            System.out.println("  0. Hủy thao tác");

            System.out.print("\nNhập STT đơn muốn thêm hoa (0 để hủy): ");
            int chonDon = Integer.parseInt(sc.nextLine().trim());
            if (chonDon == 0) return;
            if (chonDon < 1 || chonDon > dsDon.size()) {
                System.out.println("❌ STT không hợp lệ!");
                return;
            }

            Order donChon = dsDon.get(chonDon - 1);
            String orderId = donChon.getOrderId();

            // THÊM TRIM() ĐỂ XÓA DẤU CÁCH THỪA NẾU CÓ
            String trangThaiDon = donChon.getOrderStatus() != null ? donChon.getOrderStatus().trim() : "";

            // =====================================================
            // CHỐT CHẶN LOGIC NGHIỆP VỤ (BỌC THÉP)
            // =====================================================
            boolean isHopLe = trangThaiDon.equalsIgnoreCase("DANG_CHUAN_BI")
                    || trangThaiDon.equalsIgnoreCase("CHO_XU_LY")
                    || trangThaiDon.equalsIgnoreCase("CHO_XAC_NHAN");

            if (!isHopLe) {
                System.out.println("\n❌ TỪ CHỐI THÊM HOA!");
                // In ra để Debug xem Database đang lưu chính xác chữ gì
                System.out.println("👉 Trạng thái gốc trong Database: [" + trangThaiDon + "]");
                System.out.println("👉 Đơn hàng " + orderId + " đang ở trạng thái: [" + dichTrangThai(trangThaiDon) + "]");
                System.out.println("👉 Chỉ được phép thêm/sửa hoa khi đơn hàng đang ở trạng thái 'Đang chuẩn bị hoa'.");
                return; // Chặn đứng
            }
            // =====================================================

            // Nếu vượt qua được chốt chặn trên, bắt đầu vòng lặp chọn hoa
            while (true) {
                List<Flower> dsHoa = new ArrayList<>();
                for (Flower f : flowerDAO.findAll()) {
                    if (f.getStockQuantity() > 0) dsHoa.add(f);
                }
                if (dsHoa.isEmpty()) {
                    System.out.println("⚠️ Không còn hoa nào trong kho!");
                    break;
                }

                System.out.println("\n╔════╦═════════╦════════════════════════════╦══════════════╦═════════╗");
                System.out.printf("║ %-2s ║ %-7s ║ %-26s ║ %-12s ║ %-7s ║%n", "STT", "Mã hoa", "Tên hoa", "Đơn giá", "Tồn");
                System.out.println("╠════╬═════════╬════════════════════════════╬══════════════╬═════════╣");
                int sttHoa = 1;
                for (Flower f : dsHoa) {
                    System.out.printf("║ %-2d ║ %-7s ║ %-26s ║ %-12s ║ %-7d ║%n",
                            sttHoa++, f.getFlowerId(), catChuoi(f.getName(), 26), f.getUnitPrice() + "đ", f.getStockQuantity());
                }
                System.out.println("╚════╩═════════╩════════════════════════════╩══════════════╩═════════╝");
                System.out.println("  0. Thoát chế độ thêm hoa");

                System.out.print("\nNhập STT hoa muốn thêm (0 để thoát): ");
                int chonHoa = Integer.parseInt(sc.nextLine().trim());
                if (chonHoa == 0) break;
                if (chonHoa < 1 || chonHoa > dsHoa.size()) {
                    System.out.println("❌ STT không hợp lệ!");
                    continue;
                }

                Flower hoaChon = dsHoa.get(chonHoa - 1);
                int quantity = -1;
                while (true) {
                    System.out.print("Số lượng (tối đa " + hoaChon.getStockQuantity() + ", 0 để hủy): ");
                    int sl = Integer.parseInt(sc.nextLine().trim());
                    if (sl == 0) break;
                    if (sl > hoaChon.getStockQuantity()) {
                        System.out.println("❌ Vượt tồn kho!");
                        continue;
                    }
                    quantity = sl;
                    break;
                }

                if (quantity <= 0) continue;

                String flowerId = hoaChon.getFlowerId();
                boolean ok = orderDAO.hoaDaCoTrongDon(orderId, flowerId)
                        ? orderDAO.congThemSoLuong(orderId, flowerId, quantity)
                        : orderDAO.themChiTiet(orderId, flowerId, quantity, hoaChon.getUnitPrice());

                if (ok) {
                    // Trừ lô trước (FEFO: lô hạn gần nhất trừ trước), rồi trừ tồn kho.
                    int daTruTheoLo = batchDAO.xuatKho(flowerId, quantity);
                    orderDAO.truSoLuong(flowerId, quantity);
                    System.out.println("✅ Đã thêm " + hoaChon.getName() + " x" + quantity + " vào đơn!");
                    if (daTruTheoLo < quantity && batchDAO.coLo(flowerId)) {
                        System.out.println("⚠️ Tồn kho và lô đang lệch (chỉ trừ được " + daTruTheoLo + "/"
                                + quantity + " bông từ lô). Vào Lô hoa → [6] Đồng bộ tồn kho để sửa.");
                    }
                } else {
                    System.out.println("❌ Thêm thất bại!");
                }

                System.out.print("\nThêm hoa khác vào đơn? (c/K, Enter = c): ");
                if (sc.nextLine().trim().equalsIgnoreCase("k")) break;
            }
        } catch (Exception e) {
            System.out.println("❌ Lỗi: " + e.getMessage());
        }
    }

    // =====================================================
    // 3. THANH TOÁN ĐƠN HÀNG
    // =====================================================
    private void thanhToanDon() {
        try {
            System.out.println("\n--- THANH TOÁN ĐƠN HÀNG ---");

            List<Object[]> dsChuaTT = paymentDAO.layDanhSachDonChuaThanhToan();

            if (dsChuaTT.isEmpty()) {
                System.out.println("✅ Tuyệt vời! Hiện tại không có đơn hàng nào nợ tiền.");
                return;
            }

            System.out.println("DANH SÁCH ĐƠN HÀNG CHƯA THU ĐỦ TIỀN:");
            System.out.printf("%-5s | %-10s | %-20s | %-20s | %-15s | %-15s | %-18s%n",
                    "STT", "Mã Đơn", "Khách Hàng", "Trạng Thái", "Tổng Tiền", "Đã Trả", "Thanh Toán");
            System.out.println("-".repeat(122));

            for (int i = 0; i < dsChuaTT.size(); i++) {
                Object[] row = dsChuaTT.get(i);
                System.out.printf("%-5d | %-10s | %-20s | %-20s | %-15s | %-15s | %-18s%n",
                        (i + 1), row[0], row[2], dichTrangThai(row[1].toString()), row[3], row[4],
                        dichTrangThaiThanhToan((BigDecimal) row[3], (BigDecimal) row[4]));
            }

            System.out.print("\n👉 Nhập STT đơn hàng cần thanh toán (hoặc 0 để hủy): ");
            int chon = Integer.parseInt(sc.nextLine().trim());

            if (chon == 0) return;
            if (chon < 1 || chon > dsChuaTT.size()) {
                System.out.println("❌ STT không hợp lệ!");
                return;
            }

            String orderId = dsChuaTT.get(chon - 1)[0].toString();

            BigDecimal tongTien = paymentDAO.tinhTongTienDon(orderId);
            BigDecimal daTra = paymentDAO.tinhDaTra(orderId);
            BigDecimal conLai = tongTien.subtract(daTra);
            BigDecimal tienCoc = paymentDAO.tinhTienCoc(orderId);

            System.out.println("\n--- CHI TIẾT THANH TOÁN ĐƠN " + orderId + " ---");
            System.out.println("Tổng tiền đơn: " + tongTien + " VND");
            System.out.println("Đã trả:        " + daTra + " VND");
            System.out.println("Còn lại:       " + conLai + " VND");
            System.out.println("Tình trạng:    " + dichTrangThaiThanhToan(tongTien, daTra));

            if (conLai.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("✅ Đơn này đã thanh toán đủ, không còn gì để thu.");
                return;
            }

            // =====================================================
            // CHỈ CÓ 2 MỨC THANH TOÁN HỢP LỆ:
            //   - Chưa trả gì  -> trả ĐỦ TIỀN CỌC (20%) hoặc TRẢ HẾT CẢ ĐƠN
            //   - Đã trả cọc   -> chỉ còn TRẢ HẾT PHẦN CÒN LẠI
            // Không cho nhập số tiền lẻ ở giữa, nhờ vậy đơn không bao giờ
            // rơi vào trạng thái "trả nhiều hơn cọc nhưng chưa hết đơn"
            // (chính là trạng thái trước đây bị kẹt: không hủy được mà
            //  cũng không xóa được).
            // =====================================================
            BigDecimal amount;
            if (daTra.compareTo(BigDecimal.ZERO) == 0) {
                System.out.println("\nChọn hình thức thanh toán:");
                System.out.println("  1. Trả đủ tiền cọc (20%) : " + tienCoc + " VND");
                System.out.println("  2. Trả hết cả tiền đơn   : " + tongTien + " VND");
                System.out.println("  0. Hủy thao tác");
                System.out.print("Nhập số chọn: ");

                int hinhThuc;
                try {
                    hinhThuc = Integer.parseInt(sc.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("❌ Phải nhập số!");
                    return;
                }
                if (hinhThuc == 0) {
                    System.out.println("Đã hủy thao tác.");
                    return;
                }
                if (hinhThuc == 1) {
                    amount = tienCoc;
                } else if (hinhThuc == 2) {
                    amount = tongTien;
                } else {
                    System.out.println("❌ Chỉ được chọn 0, 1 hoặc 2!");
                    return;
                }
            } else {
                System.out.println("\nĐơn đang ở mức '" + dichTrangThaiThanhToan(tongTien, daTra)
                        + "', chỉ còn thanh toán nốt phần còn lại.");
                System.out.print("Xác nhận khách trả hết " + conLai + " VND? (y/n): ");
                if (!sc.nextLine().equalsIgnoreCase("y")) {
                    System.out.println("Đã hủy thao tác.");
                    return;
                }
                amount = conLai;
            }

            List<String> phuongThucHopLe = List.of("TIEN_MAT", "CHUYEN_KHOAN", "MOMO", "THE_TIN_DUNG");
            System.out.print("Phương thức (TIEN_MAT/CHUYEN_KHOAN/MOMO/THE_TIN_DUNG, Enter = TIEN_MAT): ");
            String method = sc.nextLine().trim().toUpperCase();
            if (method.isEmpty()) method = "TIEN_MAT";
            if (!phuongThucHopLe.contains(method)) {
                System.out.println("❌ Phương thức không hợp lệ!");
                return;
            }

            Payment p = new Payment();
            p.setPaymentId(paymentDAO.sinhMaThanhToan());
            p.setOrderId(orderId);
            p.setAmount(amount);
            p.setPaymentMethod(method);
            p.setPaymentDate(LocalDateTime.now());
            p.setPaymentStatus("THANH_TOAN_THANH_CONG");

            if (paymentDAO.taoThanhToan(p)) {
                BigDecimal daTraSau = daTra.add(amount);
                System.out.println("✅ Thanh toán thành công! Mã GD: " + p.getPaymentId());
                System.out.println("Đã trả: " + daTraSau + " / " + tongTien + " VND - "
                        + dichTrangThaiThanhToan(tongTien, daTraSau));

                if (daTraSau.compareTo(tongTien) >= 0) {
                    System.out.println("🎉 Khách đã trả hết đơn. Khi giao hoa xong hãy cập nhật trạng thái sang HOÀN THÀNH.");
                } else {
                    System.out.println("👉 Đơn đang ở mức '" + dichTrangThaiThanhToan(tongTien, daTraSau)
                            + "'. Khi khách trả nốt, vào lại chức năng này "
                            + "và chọn trả hết phần còn lại (" + conLai.subtract(amount) + " VND).");
                }
            } else {
                System.out.println("❌ Thanh toán thất bại!");
            }
        } catch (Exception e) {
            System.out.println("❌ Lỗi khi thanh toán: " + e.getMessage());
        }
    }

    // =====================================================
    // 4. CẬP NHẬT TRẠNG THÁI
    // =====================================================
    private List<String> trangThaiTiepTheo(String hienTai) {
        List<String> ds = new ArrayList<>();
        if (hienTai == null) return ds;

        switch (hienTai) {
            case "DANG_CHUAN_BI": // Trạng thái mới
            case "CHO_XU_LY":     // Tương thích ngược với dữ liệu cũ
            case "CHO_XAC_NHAN":
                ds.add("DANG_GIAO");
                ds.add("DA_HUY");
                break;
            case "DANG_GIAO":
                ds.add("HOAN_THANH");
                ds.add("GIAO_THAT_BAI");   // khách không nhận hàng
                break;
        }
        return ds;
    }

    private void capNhatTrangThai() {
        System.out.print("Nhập Mã đơn cần cập nhật: ");
        String orderId = sc.nextLine().toUpperCase();

        if (!orderDAO.donTonTai(orderId)) {
            System.out.println("❌ Không tìm thấy đơn!");
            return;
        }

        String hienTai = orderDAO.layTrangThaiDon(orderId);
        System.out.println("Trạng thái hiện tại: " + dichTrangThai(hienTai));

        List<String> dsTiep = trangThaiTiepTheo(hienTai);
        if (dsTiep.isEmpty()) {
            System.out.println("⚠️ Đơn đã kết thúc, không thể đổi trạng thái!");
            return;
        }

        System.out.println("Chọn trạng thái mới:");
        for (int i = 0; i < dsTiep.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + dichTrangThai(dsTiep.get(i)));
        }
        System.out.print("Nhập số chọn: ");

        try {
            int chon = Integer.parseInt(sc.nextLine().trim());
            if (chon < 1 || chon > dsTiep.size()) {
                System.out.println("⚠️ Chọn sai!");
                return;
            }
            String trangThaiMoi = dsTiep.get(chon - 1);

            if ("DA_HUY".equals(trangThaiMoi)) {
                huyDon(orderId);
                return;
            }

            if ("GIAO_THAT_BAI".equals(trangThaiMoi)) {
                giaoThatBai(orderId);
                return;
            }

            if ("HOAN_THANH".equals(trangThaiMoi)) {
                BigDecimal tongTien = paymentDAO.tinhTongTienDon(orderId);
                BigDecimal daTra = paymentDAO.tinhDaTra(orderId);

                if (daTra.compareTo(tongTien) < 0) {
                    System.out.println("❌ KHÔNG THỂ HOÀN THÀNH ĐƠN HÀNG!");
                    System.out.println("👉 Khách hàng chưa thanh toán đủ. Tổng tiền: " + tongTien + " VND, mới trả: " + daTra + " VND.");
                    System.out.println("👉 Vui lòng thu đủ tiền trước khi giao hoa thành công.");
                    return;
                }
            }

            if (orderDAO.capNhatTrangThai(orderId, trangThaiMoi)) {
                System.out.println("✅ Cập nhật thành công sang: " + dichTrangThai(trangThaiMoi));
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Phải nhập số!");
        }
    }

    private void huyDon(String orderId) {
        BigDecimal tongTien = paymentDAO.tinhTongTienDon(orderId);
        BigDecimal daTra = paymentDAO.tinhDaTra(orderId);

        // Đơn đã trả HẾT thì không cho hủy trực tiếp nữa.
        // Muốn hủy phải hoàn khoản đã thu trước (menu Thanh toán -> Hủy giao dịch),
        // sau đó đơn quay về mức 'Đã trả đủ cọc' và hủy được bình thường.
        if (tongTien.compareTo(BigDecimal.ZERO) > 0 && daTra.compareTo(tongTien) >= 0) {
            System.out.println("❌ Đơn đã thanh toán ĐỦ (" + daTra + " / " + tongTien + " VND) nên không thể hủy.");
            System.out.println("👉 Nếu thu nhầm, vào menu Thanh toán → [4] Hủy giao dịch để hoàn khoản đã thu, "
                    + "rồi quay lại hủy đơn.");
            return;
        }

        // Còn lại chỉ có 2 khả năng: chưa trả gì, hoặc đã trả đủ tiền cọc.
        if (daTra.compareTo(BigDecimal.ZERO) > 0) {
            System.out.println("⚠️ Đơn đang ở mức '" + dichTrangThaiThanhToan(tongTien, daTra)
                    + "': khách đã trả " + daTra + " VND.");
            System.out.println("   Hủy đơn thì khách MẤT toàn bộ số tiền đã trả này (cửa hàng không hoàn lại).");
        }

        System.out.print("Xác nhận hủy đơn " + orderId + " (y/n): ");
        if (!sc.nextLine().equalsIgnoreCase("y")) return;

        List<Object[]> dsHoa = orderDAO.layHoaTrongDon(orderId);
        if (orderDAO.capNhatTrangThai(orderId, "DA_HUY")) {
            for (Object[] dong : dsHoa) {
                String maHoa = (String) dong[0];
                int soLuong = (Integer) dong[1];
                traHoaVeKho(maHoa, soLuong);
            }
            System.out.println("✅ Đã hủy đơn và trả hoa về kho!");
        } else {
            System.out.println("❌ Hủy đơn thất bại!");
        }
    }

    // =====================================================
    // GIAO HÀNG THẤT BẠI
    // Khách không nhận hàng: trả toàn bộ hoa về kho, rồi chốt đơn ở trạng thái
    // GIAO_THAT_BAI. Tiền đã thu thì người dùng chọn HOÀN lại hay GIỮ lại.
    // =====================================================
    private void giaoThatBai(String orderId) {
        BigDecimal daTra = paymentDAO.tinhDaTra(orderId);
        List<Object[]> dsHoa = orderDAO.layHoaTrongDon(orderId);

        System.out.println("\n--- GIAO HÀNG THẤT BẠI ---");
        System.out.println("Sẽ trả " + dsHoa.size() + " loại hoa trong đơn về kho.");

        boolean hoanTien = false;
        if (daTra.compareTo(BigDecimal.ZERO) > 0) {
            System.out.println("Khách đã trả: " + daTra + " VND");
            System.out.println("Chọn cách xử lý tiền:");
            System.out.println("  1. Giao thất bại + HOÀN lại " + daTra + " VND cho khách");
            System.out.println("  2. Giao thất bại + GIỮ lại " + daTra + " VND (khách mất tiền)");
            System.out.println("  0. Hủy thao tác");
            System.out.print("Nhập số chọn: ");

            int chon;
            try {
                chon = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("❌ Phải nhập số!");
                return;
            }
            if (chon == 0) {
                System.out.println("Đã hủy thao tác.");
                return;
            }
            if (chon != 1 && chon != 2) {
                System.out.println("❌ Chỉ được chọn 0, 1 hoặc 2!");
                return;
            }
            hoanTien = (chon == 1);
        } else {
            System.out.println("Đơn chưa thu tiền → không cần xử lý tiền.");
            System.out.print("Xác nhận đơn " + orderId + " giao thất bại? (y/n): ");
            if (!sc.nextLine().equalsIgnoreCase("y")) {
                System.out.println("Đã hủy thao tác.");
                return;
            }
        }

        // 1. Hoàn tiền (nếu chọn hoàn): đánh dấu giao dịch đã thu sang DA_HOAN_TIEN
        if (hoanTien && !paymentDAO.hoanTienToanBo(orderId)) {
            System.out.println("❌ Hoàn tiền thất bại — chưa thay đổi trạng thái đơn.");
            return;
        }

        // 2. Trả hoa về kho
        for (Object[] dong : dsHoa) {
            String maHoa = (String) dong[0];
            int soLuong = (Integer) dong[1];
            traHoaVeKho(maHoa, soLuong);
        }

        // 3. Chốt trạng thái
        if (orderDAO.capNhatTrangThai(orderId, "GIAO_THAT_BAI")) {
            System.out.println("✅ Đã ghi nhận GIAO THẤT BẠI cho đơn " + orderId + ".");
            if (hoanTien) {
                System.out.println("💰 Đã hoàn " + daTra + " VND cho khách (giao dịch chuyển sang DA_HOAN_TIEN).");
            } else if (daTra.compareTo(BigDecimal.ZERO) > 0) {
                System.out.println("💰 Cửa hàng giữ lại " + daTra + " VND của khách.");
            }
            System.out.println("📦 Đã trả toàn bộ hoa về kho.");
            System.out.println("👉 Đơn đã kết thúc. Nếu muốn giao lại, tạo đơn mới.");
        } else {
            System.out.println("❌ Cập nhật trạng thái thất bại!");
        }
    }

    // =====================================================
    // 5, 6, 7. XEM/XÓA CHI TIẾT ĐƠN HÀNG
    // =====================================================
    private void xemChiTietDon() {
        System.out.print("Nhập Mã đơn cần xem (VD: ORD001): ");
        String orderId = sc.nextLine().toUpperCase();

        if (!orderDAO.donTonTai(orderId)) {
            System.out.println("❌ Không tìm thấy đơn hàng!");
            return;
        }

        List<Object[]> thongTin = orderDAO.thongTinDayDuDonHang(orderId);
        if (!thongTin.isEmpty()) {
            Object[] o = thongTin.get(0);
            System.out.println("\n--- THÔNG TIN ĐƠN HÀNG " + orderId + " ---");
            System.out.println("Khách hàng : " + o[1] + " (SĐT: " + o[2] + ")");
            System.out.println("Nhân viên  : " + o[3]);
            System.out.println("Ngày đặt   : " + o[4]);
            System.out.println("Ngày giao  : " + o[5]);
            System.out.println("Địa chỉ    : " + o[6]);
            System.out.println("Trạng thái : " + dichTrangThai(o[7].toString()));

            BigDecimal tongTien = paymentDAO.tinhTongTienDon(orderId);
            BigDecimal daTra = paymentDAO.tinhDaTra(orderId);
            BigDecimal daHoan = paymentDAO.tinhDaHoanTien(orderId);
            System.out.println("Thanh toán : " + daTra + " / " + tongTien + " VND - "
                    + dichTrangThaiThanhToan(tongTien, daTra)
                    + (daHoan.compareTo(BigDecimal.ZERO) > 0 ? " (đã hoàn " + daHoan + " VND)" : ""));
        }

        System.out.println("\n--- CHI TIẾT CÁC MẶT HÀNG ---");
        List<Object[]> chiTiet = orderDAO.chiTietDonHang(orderId);
        if (chiTiet.isEmpty()) {
            System.out.println("(Chưa có mặt hàng nào)");
        } else {
            System.out.printf("%-25s | %-10s | %-15s%n", "Tên Hoa", "Số Lượng", "Đơn Giá");
            System.out.println("-".repeat(55));
            for (Object[] row : chiTiet) {
                System.out.printf("%-25s | %-10s | %-15s%n", row[0], row[1], row[2]);
            }
        }
    }

    private void xoaChiTiet() {
        try {
            System.out.print("Mã đơn: ");
            String orderId = sc.nextLine().trim();
            if (!orderDAO.donTonTai(orderId)) {
                System.out.println("❌ Đơn không tồn tại!");
                return;
            }
            if (orderDAO.donDaCoThanhToan(orderId)) {
                System.out.println("❌ Đơn đã thanh toán, không thể xóa!");
                return;
            }

            String trangThai = orderDAO.layTrangThaiDon(orderId);
            if ("HOAN_THANH".equals(trangThai) || "DA_HUY".equals(trangThai)
                    || "GIAO_THAT_BAI".equals(trangThai)) {
                System.out.println("❌ Đơn đã hoàn thành / đã hủy / giao thất bại, không thể xóa hoa!");
                return;
            }
            System.out.print("Mã hoa cần xóa: ");
            String flowerId = sc.nextLine().trim();
            if (!orderDAO.hoaDaCoTrongDon(orderId, flowerId)) {
                System.out.println("⚠️ Hoa này không có trong đơn!");
                return;
            }

            System.out.print("Xác nhận xóa (y/n): ");
            if (sc.nextLine().equalsIgnoreCase("y")) {
                int soLuong = orderDAO.laySoLuongTrongDon(orderId, flowerId);
                if (orderDAO.xoaChiTiet(orderId, flowerId)) {
                    traHoaVeKho(flowerId, soLuong);
                    System.out.println("✅ Đã xóa chi tiết hoa khỏi đơn!");
                } else {
                    System.out.println("❌ Xóa thất bại!");
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Lỗi: " + e.getMessage());
        }
    }

    private void xoaDon() {
        System.out.print("Nhập Mã đơn cần xóa: ");
        String orderId = sc.nextLine().toUpperCase();

        if (!orderDAO.donTonTai(orderId)) {
            System.out.println("❌ Không tìm thấy đơn!");
            return;
        }

        String trangThai = orderDAO.layTrangThaiDon(orderId);
        boolean daHuy = "DA_HUY".equals(trangThai);
        boolean giaoThatBai = "GIAO_THAT_BAI".equals(trangThai);
        // Đơn đã đóng (hủy hoặc giao thất bại) thì hoa ĐÃ được trả về kho rồi.
        boolean daDong = daHuy || giaoThatBai;
        boolean daThuTien = orderDAO.donDaCoThanhToan(orderId);

        // Đơn còn hiệu lực mà đã thu được tiền -> không cho xóa, phải xử lý tiền trước.
        // Đơn ĐÃ ĐÓNG thì được xóa (kể cả còn khoản cọc giữ lại), nhưng phải cảnh báo.
        if (daThuTien && !daDong) {
            System.out.println("❌ KHÔNG THỂ XÓA! Đơn hàng này còn đang giữ "
                    + paymentDAO.tinhDaTra(orderId) + " VND tiền khách đã trả.");
            System.out.println("👉 Hãy hủy đơn nếu khách bỏ, hoặc vào Thanh toán → [4] Hủy giao dịch "
                    + "để hoàn HẾT số tiền đã thu trước khi xóa.");
            return;
        }

        if (daDong) {
            System.out.println("ℹ️ Đơn đã " + (daHuy ? "hủy" : "giao thất bại")
                    + " trước đó — hoa đã được trả về kho, nên xóa đơn sẽ KHÔNG cộng kho lần nữa.");
        }
        if (daThuTien) {
            System.out.println("⚠️ Đơn đã thu " + paymentDAO.tinhDaTra(orderId)
                    + " VND. Xóa đơn sẽ xóa luôn chứng từ khoản này (không hoàn tiền).");
        }

        System.out.print("Bạn có chắc chắn muốn xóa đơn hàng này? (y/n): ");
        if (!sc.nextLine().equalsIgnoreCase("y")) return;

        List<Object[]> dsHoa = orderDAO.layHoaTrongDon(orderId);
        if (orderDAO.xoaDon(orderId)) {
            if (daDong) {
                // Kho đã được trả lúc hủy / giao thất bại -> không cộng lại lần 2.
                System.out.println("✅ Đã xóa đơn (kho không thay đổi vì hoa đã về kho trước đó).");
            } else {
                for (Object[] dong : dsHoa) {
                    String maHoa = (String) dong[0];
                    int soLuong = (Integer) dong[1];
                    traHoaVeKho(maHoa, soLuong);
                }
                System.out.println("✅ Đã xóa đơn và trả hoa về kho!");
            }
        } else {
            System.out.println("❌ Xóa thất bại!");
        }
    }

    // =====================================================
    // CÁC HÀM HỖ TRỢ KHÁC
    // =====================================================
    private String taoKhachHangNhanh(String sdtDaNhap) {
        String id = customerDAO.sinhMaKhachMoi();
        System.out.println("\n--- TẠO KHÁCH HÀNG MỚI (Mã: " + id + ") ---");

        String sdt = sdtDaNhap;
        if (sdt == null || sdt.isEmpty()) {
            System.out.print("SĐT khách hàng: ");
            sdt = customerDAO.chuanHoaSdt(sc.nextLine());
        }

        System.out.print("Tên khách hàng: ");
        String ten = sc.nextLine();
        System.out.print("Địa chỉ: ");
        String diaChi = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();

        Customer khMoi = new Customer(id, ten, sdt, diaChi, email);
        return customerDAO.insert(khMoi) ? id : null;
    }

    private void xemDonCuaKhach() {
        System.out.print("Nhập SĐT khách hàng: ");
        String sdt = customerDAO.chuanHoaSdt(sc.nextLine());

        List<Customer> dsKH = customerDAO.timTheoSdt(sdt);
        if (dsKH.isEmpty()) {
            System.out.println("❌ Không tìm thấy khách hàng!");
            return;
        }

        Customer kh = dsKH.get(0);
        System.out.println("--- ĐƠN HÀNG CỦA: " + kh.getName() + " ---");
        inDsDon(orderDAO.donCuaKhach(kh.getCustomerId()));
    }

    private String layDiaChiKhach(String customerId) {
        try {
            Customer c = customerDAO.findById(customerId);
            if (c != null && c.getAddress() != null) return c.getAddress();
        } catch (Exception ignored) { }
        return "Chưa xác định";
    }

    private String layTenKhach(String customerId) {
        try {
            Customer c = customerDAO.findById(customerId);
            if (c != null && c.getName() != null) return c.getName();
        } catch (Exception ignored) { }
        return customerId;
    }

    // Trả hoa về kho: cộng lại tồn kho VÀ cộng lại vào lô (nếu còn lô AVAILABLE/SOLD_OUT)
    // để flower.stock_quantity và quantity_remaining không bị lệch.
    private void traHoaVeKho(String flowerId, int soLuong) {
        orderDAO.congTonKho(flowerId, soLuong);
        batchDAO.nhapLaiKho(flowerId, soLuong);
    }

    private int demSoLoaiHoaTrongDon(String orderId) {
        try {
            List<Object[]> ct = orderDAO.chiTietDonHang(orderId);
            return ct == null ? 0 : ct.size();
        } catch (Exception e) {
            return 0;
        }
    }

    private String catChuoi(String s, int maxLen) {
        if (s == null) return "";
        if (s.length() <= maxLen) return s;
        return s.substring(0, maxLen - 1) + "…";
    }

    private void inDsDon(List<Order> list) {
        if (list == null || list.isEmpty()) {
            System.out.println("Không có dữ liệu.");
            return;
        }
        System.out.printf("%-10s | %-15s | %-20s | %-20s%n", "Mã Đơn", "Mã KH", "Ngày Đặt", "Trạng Thái");
        System.out.println("-".repeat(75));
        for (Order o : list) {
            System.out.printf("%-10s | %-15s | %-20s | %-20s%n",
                    o.getOrderId(), o.getCustomerId(), o.getOrderDate().toString().replace("T", " "), dichTrangThai(o.getOrderStatus()));
        }
    }

    private void inBang(List<?> list) {
        if (list == null || list.isEmpty()) {
            System.out.println("Không có dữ liệu.");
            return;
        }
        for (Object o : list) {
            if (o instanceof Object[]) {
                StringBuilder sb = new StringBuilder();
                for (Object cell : (Object[]) o) sb.append(cell).append(" | ");
                System.out.println(sb);
            } else {
                System.out.println(o);
            }
        }
    }
}