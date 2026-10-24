package vn.edu.crud.menu;

import vn.edu.crud.util.InBangHelper;

import vn.edu.crud.service.*;
import vn.edu.crud.model.Customer;
import vn.edu.crud.model.Employee;
import vn.edu.crud.model.Flower;
import vn.edu.crud.model.Order;
import vn.edu.crud.model.Payment;
import vn.edu.crud.util.AnToan;
import vn.edu.crud.util.NhapLieu;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class OrderMenu {

    private final Scanner sc = new Scanner(System.in);
    private final OrderService orderService = new OrderService();
    private final PaymentService paymentService = new PaymentService();
    private final CustomerService customerService = new CustomerService();
    private final EmployeeService employeeService = new EmployeeService();
    private final FlowerService flowerService = new FlowerService();
    private final FlowerBatchService batchService = new FlowerBatchService();

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final NumberFormat VND = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

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
                System.out.println("║ 19. Sửa thông tin đơn (địa chỉ/ngày) ║");
                System.out.println("║ 20. Tìm kiếm đơn (Mã/KH/NV)          ║");
                System.out.println("║ 21. Lọc đơn theo trạng thái (Lambda) ║");
                System.out.println("║ 22. Đơn hàng mới nhất (Lambda)       ║");
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
                    case 8: InBangHelper.inBang(orderService.donHangGiaCao(new BigDecimal("500000")), new String[]{"Mã Đơn", "Mã KH", "Mã NV", "Ngày Đặt", "Tổng Tiền"}); break;
                    case 9: inDsDon(orderService.donChuaGiaoHomNayLambda()); break;
                    case 10: System.out.println("Doanh thu hôm nay: " + orderService.doanhThuHomNay() + " VND"); break;
                    case 11: xemDonCuaKhach(); break;
                    case 12: InBangHelper.inBang(orderService.thongKeDonTheoTrangThai(), new String[]{"Trạng Thái", "Số Lượng Đơn"}); break;
                    case 13: InBangHelper.inBang(orderService.donCoNhieuLoaiHoa(3), new String[]{"Mã Đơn", "Số Loài Hoa"}); break;
                    case 14: InBangHelper.inBang(orderService.khachDaTungDatHang(), new String[]{"Mã KH", "Tên KH", "SĐT", "Địa Chỉ", "Email"}); break;
                    case 15: InBangHelper.inBang(orderService.khachChuaTungDatHang(), new String[]{"Mã KH", "Tên KH", "SĐT", "Địa Chỉ", "Email"}); break;
                    case 16: InBangHelper.inBang(orderService.khachNhieuDonNhat(), new String[]{"Mã KH", "Tên KH", "Số Lượng Đơn"}); break;
                    case 17: InBangHelper.inBang(orderService.nhanVienNhieuDonNhat(), new String[]{"Mã NV", "Tên NV", "Số Lượng Đơn"}); break;
                    case 18: InBangHelper.inBang(orderService.tongChiTieuTheoKhach(), new String[]{"Mã KH", "Tên KH", "Tổng Chi Tiêu"}); break;
                    case 19: suaThongTinDon(); break;
                    case 20: timKiemDonHangLambda(); break;
                    case 21: locDonTheoTrangThaiLambda(); break;
                    case 22: donHangMoiNhatLambda(); break;
                    case 0: System.out.println("Quay lại menu chính."); break;
                    default: System.out.println("⚠️ Chọn sai, vui lòng chọn lại!");
                }
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Vui lòng nhập SỐ, không nhập chữ! (VD: 1 để tạo đơn, 0 để quay lại)");
                chon = -1;
            } catch (Exception e) {
                AnToan.baoLoi(e, "số trong menu 0-22. VD: 1 = tạo đơn, 20 = tìm kiếm đơn");
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
            o.setOrderId(orderService.sinhMaDonMoi());
            System.out.println("Mã đơn tự sinh: " + o.getOrderId());

            System.out.println("(Bấm Enter nếu là khách mới, chưa có SĐT)");
            String sdt = NhapLieu.sdtKhach(sc, "SĐT khách hàng", false);

            if (!sdt.isEmpty()) {
                List<Customer> dsKH = customerService.timTheoSdt(sdt);
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

            List<Employee> dsNV = employeeService.findAll();
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

            LocalDateTime ngayMacDinh = o.getOrderDate().plusHours(2);
            while (true) {
                System.out.print("Ngày giao (yyyy-MM-dd HH:mm:ss, Enter = +2h): ");
                String dateStr = sc.nextLine().trim();
                if (dateStr.isEmpty()) {
                    o.setDeliveryDate(ngayMacDinh);
                    System.out.println("→ Dùng mặc định: " + ngayMacDinh.format(FMT));
                    break;
                }
                try {
                    if (dateStr.length() == 16) dateStr += ":00";
                    LocalDateTime moi = LocalDateTime.parse(dateStr.replace(" ", "T"));
                    if (moi.isBefore(o.getOrderDate())) {
                        System.out.println("❌ Ngày giao không được trước ngày đặt ("
                                + o.getOrderDate().format(FMT) + ")!");
                        continue;
                    }
                    o.setDeliveryDate(moi);
                    break;
                } catch (DateTimeParseException e) {
                    System.out.println("❌ Ngày giờ không hợp lệ! Ví dụ: 2026-10-25 15:00:00");
                }
            }

            System.out.print("Địa chỉ giao (Enter = lấy địa chỉ khách, VD: 123 Lê Lợi, Quận 1): ");
            String addr = sc.nextLine().trim();
            if (addr.isEmpty()) {
                addr = layDiaChiKhach(o.getCustomerId());
                System.out.println("→ Dùng địa chỉ khách: " + addr);
            }
            o.setDeliveryAddress(addr);

            // ĐỔI TRẠNG THÁI MẶC ĐỊNH THÀNH DANG_CHUAN_BI
            o.setOrderStatus("DANG_CHUAN_BI");

            System.out.println("\n>>> Đang tạo đơn...");
            if (orderService.taoDonHang(o)) {
                System.out.println("✅ Tạo đơn thành công! Mã đơn: " + o.getOrderId());
                System.out.println("👉 Hãy vào chức năng [2] để thêm hoa vào đơn.");
            } else {
                System.out.println("❌ Tạo đơn thất bại!");
            }
        } catch (Exception e) {
            AnToan.baoLoi(e, "SĐT 10 số (VD 0901234567), ngày giao dạng 2026-10-25 15:00:00, chọn nhân viên theo số");
        }
    }

    // =====================================================
    // 2. THÊM HOA VÀO ĐƠN
    // =====================================================
    private void themHoaVaoDon() {
        try {
            System.out.println("\n--- THÊM HOA VÀO ĐƠN ---");

            List<Order> dsDon = orderService.findAll();

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

            // Đã thu tiền (cọc/tạm ứng) thì khóa chi tiết đơn: thêm hoa sẽ làm
            // tiền cọc không còn đủ 20% và tổng tiền lệch với số đã thu.
            if (paymentService.tinhDaTra(orderId).compareTo(BigDecimal.ZERO) > 0) {
                System.out.println("\n❌ TỪ CHỐI THÊM HOA!");
                System.out.println("👉 Đơn đã thu " + paymentService.tinhDaTra(orderId)
                        + " VND nên không thể thêm/sửa hoa.");
                System.out.println("👉 Nếu cần sửa: vào Thanh toán → [4] Hủy giao dịch để hoàn khoản đã thu trước.");
                return;
            }
            // =====================================================

            // Nếu vượt qua được chốt chặn trên, bắt đầu vòng lặp chọn hoa
            boolean daBaoHoaChuaCoGia = false;
            while (true) {
                List<Flower> dsHoa = new ArrayList<>();
                int soHoaChuaCoGia = 0;
                for (Flower f : flowerService.findAll()) {
                    if (f.getStockQuantity() <= 0) continue;
                    // Hoa chưa có giá bán (> 0) thì không bán được: đơn sẽ có tổng 0 đồng,
                    // mà đơn 0 đồng thì không thể thanh toán / không thể hoàn thành.
                    if (f.getUnitPrice() == null || f.getUnitPrice().compareTo(BigDecimal.ZERO) <= 0) {
                        soHoaChuaCoGia++;
                        continue;
                    }
                    dsHoa.add(f);
                }
                if (soHoaChuaCoGia > 0 && !daBaoHoaChuaCoGia) {
                    daBaoHoaChuaCoGia = true;
                    System.out.println("⚠️ " + soHoaChuaCoGia + " loại hoa chưa có giá bán (> 0) nên không bán được —"
                            + " sửa giá ở menu Hoa → [4] Sửa.");
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
                while (quantity < 0) {
                    System.out.print("Số lượng (1-" + hoaChon.getStockQuantity()
                            + ", VD: 10, 0 để hủy): ");
                    int sl;
                    try {
                        sl = Integer.parseInt(sc.nextLine().trim());
                    } catch (NumberFormatException e) {
                        System.out.println("⚠️ Phải nhập số. VD: 10");
                        continue;
                    }
                    if (sl == 0) break;
                    if (sl < 0) {
                        System.out.println("⚠️ Số lượng phải lớn hơn 0.");
                        continue;
                    }
                    if (sl > hoaChon.getStockQuantity()) {
                        System.out.println("❌ Vượt tồn kho (chỉ còn " + hoaChon.getStockQuantity() + ").");
                        continue;
                    }
                    quantity = sl;
                }

                if (quantity <= 0) continue;

                String flowerId = hoaChon.getFlowerId();
                boolean ok = orderService.hoaDaCoTrongDon(orderId, flowerId)
                        ? orderService.congThemSoLuong(orderId, flowerId, quantity)
                        : orderService.themChiTiet(orderId, flowerId, quantity, hoaChon.getUnitPrice());

                if (ok) {
                    // Trừ lô trước (FEFO: lô hạn gần nhất trừ trước), rồi trừ tồn kho.
                    int daTruTheoLo = batchService.xuatKho(flowerId, quantity);
                    orderService.truSoLuong(flowerId, quantity);
                    System.out.println("✅ Đã thêm " + hoaChon.getName() + " x" + quantity + " vào đơn!");
                    if (daTruTheoLo < quantity && batchService.coLo(flowerId)) {
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
            AnToan.baoLoi(e, "chọn STT đơn/STT hoa theo số, số lượng là số nguyên từ 1 đến tồn kho");
        }
    }

    // =====================================================
    // 3. THANH TOÁN ĐƠN HÀNG
    // =====================================================
    private void thanhToanDon() {
        try {
            System.out.println("\n--- THANH TOÁN ĐƠN HÀNG ---");

            List<Object[]> dsChuaTT = paymentService.layDanhSachDonChuaThanhToan();

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

            BigDecimal tongTien = paymentService.tinhTongTienDon(orderId);
            BigDecimal daTra = paymentService.tinhDaTra(orderId);
            BigDecimal conLai = tongTien.subtract(daTra);
            BigDecimal tienCoc = paymentService.tinhTienCoc(orderId);

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
            p.setPaymentId(paymentService.sinhMaThanhToan());
            p.setOrderId(orderId);
            p.setAmount(amount);
            p.setPaymentMethod(method);
            p.setPaymentDate(LocalDateTime.now());
            p.setPaymentStatus("THANH_TOAN_THANH_CONG");

            if (paymentService.taoThanhToan(p)) {
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
            AnToan.baoLoi(e, "chọn STT đơn theo số, chọn 0/1/2, phương thức TIEN_MAT/CHUYEN_KHOAN/MOMO/THE_TIN_DUNG");
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
        List<Order> dsDon = orderService.findAll();
        List<Order> dsChuaXong = new ArrayList<>();
        for (Order o : dsDon) {
            String st = o.getOrderStatus();
            if (st != null && !st.equalsIgnoreCase("HOAN_THANH")
                    && !st.equalsIgnoreCase("DA_HUY")
                    && !st.equalsIgnoreCase("GIAO_THAT_BAI")) {
                dsChuaXong.add(o);
            }
        }

        if (dsChuaXong.isEmpty()) {
            System.out.println("⚠️ Hiện không có đơn hàng nào đang chờ xử lý.");
            return;
        }

        System.out.println("\nDANH SÁCH ĐƠN HÀNG ĐANG XỬ LÝ:");
        System.out.printf("%-4s | %-8s | %-22s | %-18s%n", "STT", "Mã Đơn", "Khách Hàng", "Trạng Thái");
        System.out.println("-".repeat(58));
        for (int i = 0; i < dsChuaXong.size(); i++) {
            Order o = dsChuaXong.get(i);
            System.out.printf("%-4d | %-8s | %-22s | %-18s%n",
                    (i + 1), o.getOrderId(), catChuoi(layTenKhach(o.getCustomerId()), 22),
                    dichTrangThai(o.getOrderStatus()));
        }

        System.out.print("\nNhập STT đơn hoặc Mã đơn cần cập nhật (0 để hủy): ");
        String nhap = sc.nextLine().trim();
        if (nhap.equals("0") || nhap.isEmpty()) return;

        String orderId = null;
        try {
            int stt = Integer.parseInt(nhap);
            if (stt >= 1 && stt <= dsChuaXong.size()) {
                orderId = dsChuaXong.get(stt - 1).getOrderId();
            }
        } catch (NumberFormatException ignored) {}

        if (orderId == null) {
            orderId = nhap.toUpperCase();
        }

        if (!orderService.donTonTai(orderId)) {
            System.out.println("❌ Không tìm thấy đơn: " + orderId);
            return;
        }

        inThongTinDon(orderId);
        String hienTai = orderService.layTrangThaiDon(orderId);
        System.out.println("  Trạng thái: " + dichTrangThai(hienTai));

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

            // Đơn phải có ít nhất 1 mặt hàng mới được chuyển sang giao hàng / hoàn thành.
            if (orderService.demSoMatHang(orderId) == 0) {
                System.out.println("❌ KHÔNG THỂ CHUYỂN TRẠNG THÁI: đơn hàng chưa có loại hoa nào.");
                System.out.println("👉 Vào chức năng [2] Thêm hoa vào đơn rồi thử lại (hoặc hủy đơn).");
                return;
            }

            // Muốn giao hàng thì phải thu ít nhất tiền cọc (20%)
            if ("DANG_GIAO".equals(trangThaiMoi)) {
                BigDecimal daTra = paymentService.tinhDaTra(orderId);
                BigDecimal tienCoc = paymentService.tinhTienCoc(orderId);
                if (daTra.compareTo(tienCoc) < 0) {
                    System.out.println("❌ CHƯA THU ĐỦ TIỀN CỌC, KHÔNG THỂ GIAO HÀNG!");
                    System.out.println("👉 Cần cọc " + tienCoc + " VND (20% đơn), mới thu: " + daTra + " VND.");
                    System.out.println("👉 Vào chức năng [3] Thanh toán để thu cọc trước.");
                    return;
                }
            }

            if ("HOAN_THANH".equals(trangThaiMoi)) {
                BigDecimal tongTien = paymentService.tinhTongTienDon(orderId);
                BigDecimal daTra = paymentService.tinhDaTra(orderId);

                if (!orderService.donDaCoThanhToan(orderId)) {
                    System.out.println("❌ KHÔNG THỂ HOÀN THÀNH ĐƠN HÀNG!");
                    System.out.println("👉 Đơn chưa có giao dịch thanh toán nào thành công.");
                    return;
                }

                if (daTra.compareTo(tongTien) < 0) {
                    System.out.println("❌ KHÔNG THỂ HOÀN THÀNH ĐƠN HÀNG!");
                    System.out.println("👉 Khách hàng chưa thanh toán đủ. Tổng tiền: " + tongTien + " VND, mới trả: " + daTra + " VND.");
                    System.out.println("👉 Vui lòng thu đủ tiền trước khi giao hoa thành công.");
                    return;
                }
            }

            if (orderService.capNhatTrangThai(orderId, trangThaiMoi)) {
                System.out.println("✅ Cập nhật thành công sang: " + dichTrangThai(trangThaiMoi));
            }
        } catch (Exception e) {
            AnToan.baoLoi(e, "mã đơn dạng ORD001 (VD: ORD001), chọn trạng thái bằng số 1 hoặc 2");
        }
    }

    // In nhanh thông tin đơn (khách, nhân viên, ngày giao, tiền) để tránh nhầm khi nhập mã
    private void inThongTinDon(String orderId) {
        List<Object[]> tt = orderService.thongTinDayDuDonHang(orderId);
        if (tt.isEmpty()) return;

        Object[] o = tt.get(0);
        String ngayGiao = o[5] == null ? "—" : String.valueOf(o[5]).replace("T", " ");

        System.out.println("\n--- ĐƠN " + orderId + " ---");
        System.out.println("  Khách hàng: " + o[1] + " (SĐT: " + o[2] + ")");
        System.out.println("  Nhân viên : " + o[3]);
        System.out.println("  Ngày giao : " + ngayGiao);
        System.out.println("  Tiền      : " + VND.format(paymentService.tinhTongTienDon(orderId))
                + " / đã trả " + VND.format(paymentService.tinhDaTra(orderId)));
    }

    private void huyDon(String orderId) {
        try {
            huyDonImpl(orderId);
        } catch (Exception e) {
            AnToan.baoLoi(e, "mã đơn dạng ORD001 (VD: ORD001)");
        }
    }

    private void huyDonImpl(String orderId) {
        BigDecimal tongTien = paymentService.tinhTongTienDon(orderId);
        BigDecimal daTra = paymentService.tinhDaTra(orderId);

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

        List<Object[]> dsHoa = orderService.layHoaTrongDon(orderId);
        if (orderService.capNhatTrangThai(orderId, "DA_HUY")) {
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
        try {
            giaoThatBaiImpl(orderId);
        } catch (Exception e) {
            AnToan.baoLoi(e, "mã đơn dạng ORD001 (VD: ORD001) và chọn 0/1/2");
        }
    }

    private void giaoThatBaiImpl(String orderId) {
        BigDecimal daTra = paymentService.tinhDaTra(orderId);
        List<Object[]> dsHoa = orderService.layHoaTrongDon(orderId);

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
        if (hoanTien && !paymentService.hoanTienToanBo(orderId)) {
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
        if (orderService.capNhatTrangThai(orderId, "GIAO_THAT_BAI")) {
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
        try {
            xemChiTietDonImpl();
        } catch (Exception e) {
            AnToan.baoLoi(e, "mã đơn dạng ORD001 (VD: ORD001)");
        }
    }

    private void xemChiTietDonImpl() {
        System.out.print("Nhập Mã đơn cần xem (VD: ORD001): ");
        String orderId = sc.nextLine().toUpperCase();

        if (!orderService.donTonTai(orderId)) {
            System.out.println("❌ Không tìm thấy đơn hàng!");
            return;
        }

        List<Object[]> thongTin = orderService.thongTinDayDuDonHang(orderId);
        if (!thongTin.isEmpty()) {
            Object[] o = thongTin.get(0);
            System.out.println("\n--- THÔNG TIN ĐƠN HÀNG " + orderId + " ---");
            System.out.println("Khách hàng : " + o[1] + " (SĐT: " + o[2] + ")");
            System.out.println("Nhân viên  : " + o[3]);
            System.out.println("Ngày đặt   : " + o[4]);
            System.out.println("Ngày giao  : " + o[5]);
            System.out.println("Địa chỉ    : " + o[6]);
            System.out.println("Trạng thái : " + dichTrangThai(o[7].toString()));

            BigDecimal tongTien = paymentService.tinhTongTienDon(orderId);
            BigDecimal daTra = paymentService.tinhDaTra(orderId);
            BigDecimal daHoan = paymentService.tinhDaHoanTien(orderId);
            System.out.println("Thanh toán : " + daTra + " / " + tongTien + " VND - "
                    + dichTrangThaiThanhToan(tongTien, daTra)
                    + (daHoan.compareTo(BigDecimal.ZERO) > 0 ? " (đã hoàn " + daHoan + " VND)" : ""));
        }

        System.out.println("\n--- CHI TIẾT CÁC MẶT HÀNG ---");
        List<Object[]> chiTiet = orderService.chiTietDonHang(orderId);
        if (chiTiet.isEmpty()) {
            System.out.println("(Chưa có mặt hàng nào)");
        } else {
            InBangHelper.inBang(new String[]{"Tên Hoa", "Số Lượng", "Đơn Giá"}, chiTiet);
        }
    }

    private void xoaChiTiet() {
        try {
            System.out.print("Mã đơn (VD: ORD001): ");
            String orderId = sc.nextLine().trim();
            if (!orderService.donTonTai(orderId)) {
                System.out.println("❌ Đơn không tồn tại!");
                return;
            }
            inThongTinDon(orderId);
            if (orderService.donDaCoThanhToan(orderId)) {
                System.out.println("❌ Đơn đã thanh toán, không thể xóa!");
                return;
            }

            String trangThai = orderService.layTrangThaiDon(orderId);
            boolean chuaGiao = "DANG_CHUAN_BI".equals(trangThai)
                    || "CHO_XU_LY".equals(trangThai)
                    || "CHO_XAC_NHAN".equals(trangThai);
            if (!chuaGiao) {
                System.out.println("❌ Chỉ xóa hoa được khi đơn CHƯA GIAO — đơn này đang: "
                        + dichTrangThai(trangThai));
                return;
            }
            System.out.print("Mã hoa cần xóa (VD: FL01): ");
            String flowerId = sc.nextLine().trim();
            if (!orderService.hoaDaCoTrongDon(orderId, flowerId)) {
                System.out.println("⚠️ Hoa này không có trong đơn!");
                return;
            }

            System.out.print("Xác nhận xóa (y/n): ");
            if (sc.nextLine().equalsIgnoreCase("y")) {
                int soLuong = orderService.laySoLuongTrongDon(orderId, flowerId);
                if (orderService.xoaChiTiet(orderId, flowerId)) {
                    traHoaVeKho(flowerId, soLuong);
                    System.out.println("✅ Đã xóa chi tiết hoa khỏi đơn!");
                } else {
                    System.out.println("❌ Xóa thất bại!");
                }
            }
        } catch (Exception e) {
            AnToan.baoLoi(e, "mã đơn dạng ORD001 và mã hoa dạng FL01 (VD: ORD001 / FL03)");
        }
    }

    private void xoaDon() {
        try {
            xoaDonImpl();
        } catch (Exception e) {
            AnToan.baoLoi(e, "mã đơn dạng ORD001 (VD: ORD001) — nhập đúng mã in ở danh sách đơn");
        }
    }

    private void xoaDonImpl() {
        System.out.print("Nhập Mã đơn cần xóa (VD: ORD001): ");
        String orderId = sc.nextLine().toUpperCase();

        if (!orderService.donTonTai(orderId)) {
            System.out.println("❌ Không tìm thấy đơn!");
            return;
        }

        inThongTinDon(orderId);
        String trangThai = orderService.layTrangThaiDon(orderId);
        boolean daHuy = "DA_HUY".equals(trangThai);
        boolean giaoThatBai = "GIAO_THAT_BAI".equals(trangThai);
        // Đơn đã đóng (hủy hoặc giao thất bại) thì hoa ĐÃ được trả về kho rồi.
        boolean daDong = daHuy || giaoThatBai;
        boolean daThuTien = orderService.donDaCoThanhToan(orderId);

        // Đơn còn hiệu lực mà đã thu được tiền -> không cho xóa, phải xử lý tiền trước.
        // Đơn ĐÃ ĐÓNG thì được xóa (kể cả còn khoản cọc giữ lại), nhưng phải cảnh báo.
        if (daThuTien && !daDong) {
            System.out.println("❌ KHÔNG THỂ XÓA! Đơn hàng này còn đang giữ "
                    + paymentService.tinhDaTra(orderId) + " VND tiền khách đã trả.");
            System.out.println("👉 Hãy hủy đơn nếu khách bỏ, hoặc vào Thanh toán → [4] Hủy giao dịch "
                    + "để hoàn HẾT số tiền đã thu trước khi xóa.");
            return;
        }

        if (daDong) {
            System.out.println("ℹ️ Đơn đã " + (daHuy ? "hủy" : "giao thất bại")
                    + " trước đó — hoa đã được trả về kho, nên xóa đơn sẽ KHÔNG cộng kho lần nữa.");
        }
        if (daThuTien) {
            System.out.println("⚠️ Đơn đã thu " + paymentService.tinhDaTra(orderId)
                    + " VND. Xóa đơn sẽ xóa luôn chứng từ khoản này (không hoàn tiền).");
        }

        System.out.print("Bạn có chắc chắn muốn xóa đơn hàng này? (y/n): ");
        if (!sc.nextLine().equalsIgnoreCase("y")) return;

        List<Object[]> dsHoa = orderService.layHoaTrongDon(orderId);
        if (orderService.xoaDon(orderId)) {
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
    // 19. SỬA THÔNG TIN ĐƠN (địa chỉ / ngày giao / nhân viên)
    // =====================================================
    private void suaThongTinDon() {
        try {
            suaThongTinDonImpl();
        } catch (Exception e) {
            AnToan.baoLoi(e, "mã đơn ORD001, ngày giao dạng 2026-10-25 15:00:00, nhân viên chọn theo số");
        }
    }

    private void suaThongTinDonImpl() {
        System.out.print("Nhập Mã đơn cần sửa: ");
        String orderId = sc.nextLine().toUpperCase();

        Order don = orderService.timTheoMa(orderId);
        if (don == null) {
            System.out.println("❌ Không tìm thấy đơn!");
            return;
        }

        String trangThai = don.getOrderStatus();
        boolean chuaGiao = "DANG_CHUAN_BI".equals(trangThai)
                || "CHO_XU_LY".equals(trangThai)
                || "CHO_XAC_NHAN".equals(trangThai);
        if (!chuaGiao) {
            System.out.println("❌ Chỉ sửa được đơn CHƯA GIAO — đơn này đang: " + dichTrangThai(trangThai));
            return;
        }
        if (don.getOrderDate() == null || don.getDeliveryDate() == null) {
            System.out.println("❌ Đơn thiếu ngày đặt / ngày giao, không sửa được!");
            return;
        }

        System.out.println("\n--- SỬA THÔNG TIN ĐƠN " + orderId + " ---");
        System.out.println("Địa chỉ hiện tại  : " + don.getDeliveryAddress());
        System.out.println("Ngày giao hiện tại: " + don.getDeliveryDate().format(FMT));
        System.out.println("Nhân viên hiện tại: " + layTenNV(don.getEmployeeId()));

        System.out.print("Địa chỉ mới (Enter = giữ nguyên, VD: 123 Lê Lợi, Quận 1): ");
        String diaChi = sc.nextLine().trim();
        if (diaChi.isEmpty()) diaChi = don.getDeliveryAddress();

        LocalDateTime ngayGiao = don.getDeliveryDate();
        while (true) {
            System.out.print("Ngày giao mới (yyyy-MM-dd HH:mm:ss, Enter = giữ nguyên): ");
            String s = sc.nextLine().trim();
            if (s.isEmpty()) break;
            try {
                if (s.length() == 16) s += ":00";
                LocalDateTime moi = LocalDateTime.parse(s.replace(" ", "T"));
                if (moi.isBefore(don.getOrderDate())) {
                    System.out.println("❌ Ngày giao không được trước ngày đặt ("
                            + don.getOrderDate().format(FMT) + ")!");
                    continue;
                }
                ngayGiao = moi;
                break;
            } catch (DateTimeParseException e) {
                System.out.println("❌ Sai định dạng! Ví dụ: 2026-10-25 15:00:00");
            }
        }

        String employeeId = don.getEmployeeId();
        System.out.print("Đổi nhân viên phụ trách? (y/n): ");
        if (sc.nextLine().equalsIgnoreCase("y")) {
            List<Employee> dsNV = employeeService.findAll();
            for (int i = 0; i < dsNV.size(); i++) {
                System.out.printf("  %d. %-20s (Mã: %s)\n", (i + 1),
                        dsNV.get(i).getName(), dsNV.get(i).getEmployeeId());
            }
            System.out.print("Chọn số (Enter = giữ nguyên): ");
            String chon = sc.nextLine().trim();
            if (!chon.isEmpty()) {
                try {
                    int so = Integer.parseInt(chon);
                    if (so >= 1 && so <= dsNV.size()) {
                        employeeId = dsNV.get(so - 1).getEmployeeId();
                    } else {
                        System.out.println("⚠️ Chọn sai — giữ nguyên nhân viên cũ.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("⚠️ Không phải số — giữ nguyên nhân viên cũ.");
                }
            }
        }

        if (orderService.capNhatThongTin(orderId, diaChi, ngayGiao, employeeId)) {
            System.out.println("✅ Đã cập nhật thông tin đơn.");
        } else {
            System.out.println("❌ Cập nhật thất bại!");
        }
    }

    private String layTenNV(String employeeId) {
        try {
            Employee e = employeeService.findById(employeeId);
            if (e != null && e.getName() != null) return e.getName();
        } catch (Exception ignored) {
        }
        return employeeId;
    }

    // =====================================================
    // CÁC HÀM HỖ TRỢ KHÁC
    // =====================================================
    private String taoKhachHangNhanh(String sdtDaNhap) {
        String id = customerService.sinhMaKhachMoi();
        System.out.println("\n--- TẠO KHÁCH HÀNG MỚI (Mã: " + id + ") ---");

        String sdt = sdtDaNhap;
        if (sdt == null || sdt.isEmpty()) {
            sdt = NhapLieu.sdtKhach(sc, "SĐT khách hàng", true);
        }

        String ten = NhapLieu.batBuoc(sc, "Tên khách hàng", "VD: Nguyễn Văn A");
        String diaChi = NhapLieu.tuyChon(sc, "Địa chỉ", "VD: Quận 1, TP.HCM");
        String email = NhapLieu.email(sc, "Email", false);

        Customer khMoi = new Customer(id, ten, sdt, diaChi, email);
        return customerService.insert(khMoi) ? id : null;
    }

    private void xemDonCuaKhach() {
        try {
            xemDonCuaKhachImpl();
        } catch (Exception e) {
            AnToan.baoLoi(e, "SĐT khách 10 số (VD: 0901234567)");
        }
    }

    private void xemDonCuaKhachImpl() {
        System.out.print("Nhập SĐT khách hàng (VD: 0901234567): ");
        String sdt = customerService.chuanHoaSdt(sc.nextLine());

        List<Customer> dsKH = customerService.timTheoSdt(sdt);
        if (dsKH.isEmpty()) {
            System.out.println("❌ Không tìm thấy khách hàng!");
            return;
        }

        Customer kh = dsKH.get(0);
        System.out.println("--- ĐƠN HÀNG CỦA: " + kh.getName() + " ---");
        // Lambda: lọc đơn theo mã khách hàng
        inDsDon(orderService.donCuaKhachLambda(kh.getCustomerId()));
    }

    private String layDiaChiKhach(String customerId) {
        try {
            Customer c = customerService.findById(customerId);
            if (c != null && c.getAddress() != null) return c.getAddress();
        } catch (Exception ignored) { }
        return "Chưa xác định";
    }

    private String layTenKhach(String customerId) {
        try {
            Customer c = customerService.findById(customerId);
            if (c != null && c.getName() != null) return c.getName();
        } catch (Exception ignored) { }
        return customerId;
    }

    // Trả hoa về kho: cộng lại tồn kho VÀ cộng lại vào lô (nếu còn lô AVAILABLE/SOLD_OUT)
    // để flower.stock_quantity và quantity_remaining không bị lệch.
    private void traHoaVeKho(String flowerId, int soLuong) {
        orderService.congTonKho(flowerId, soLuong);
        batchService.nhapLaiKho(flowerId, soLuong);
    }

    private int demSoLoaiHoaTrongDon(String orderId) {
        try {
            List<Object[]> ct = orderService.chiTietDonHang(orderId);
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

    // =========================================================================
    // CÁC CHỨC NĂNG LỌC & SẮP XẾP ĐƠN HÀNG BẰNG LAMBDA (MỤC 5)
    // =========================================================================
    private void locDonTheoTrangThaiLambda() {
        System.out.println("\n--- LỌC ĐƠN HÀNG THEO TRẠNG THÁI (SỬ DỤNG LAMBDA) ---");
        System.out.println("Các trạng thái gợi ý: CHO_XAC_NHAN, CHO_XU_LY, DANG_CHUAN_BI, DANG_GIAO, HOAN_THANH, DA_HUY, GIAO_THAT_BAI");
        System.out.print("Nhập trạng thái cần lọc (hoặc để trống để lấy tất cả): ");
        String status = sc.nextLine().trim();

        List<Order> ketQua = orderService.locDonTheoTrangThaiLambda(status);
        if (ketQua.isEmpty()) {
            System.out.println("❌ Không tìm thấy đơn hàng nào có trạng thái: \"" + status + "\"");
        } else {
            System.out.println("✅ Tìm thấy " + ketQua.size() + " đơn hàng phù hợp:");
            inDsDon(ketQua);
        }
    }

    private void donHangMoiNhatLambda() {
        System.out.println("\n--- DANH SÁCH ĐƠN HÀNG MỚI NHẤT (SẮP XẾP BẰNG LAMBDA) ---");
        List<Order> dsMoiNhat = orderService.donHangMoiNhatLambda();
        if (dsMoiNhat.isEmpty()) {
            System.out.println("Không có dữ liệu đơn hàng.");
        } else {
            inDsDon(dsMoiNhat);
        }
    }

    // =========================================================================
    // CHỨC NĂNG TÌM KIẾM ĐƠN HÀNG BẰNG LAMBDA (MỤC 5)
    // =========================================================================
    private void timKiemDonHangLambda() {
        System.out.println("\n--- TÌM KIẾM ĐƠN HÀNG (SỬ DỤNG LAMBDA) ---");
        System.out.print("Nhập từ khóa cần tìm (Mã đơn, Mã KH hoặc Mã NV): ");
        String tuKhoa = sc.nextLine();

        // Gọi hàm searchOrdersByLambda đã viết trong OrderService
        List<Order> ketQua = orderService.searchOrdersByLambda(tuKhoa);

        if (ketQua.isEmpty()) {
            System.out.println("❌ Không tìm thấy đơn hàng nào khớp với từ khóa: \"" + tuKhoa + "\"");
        } else {
            System.out.println("✅ Tìm thấy " + ketQua.size() + " đơn hàng phù hợp:");
            inDsDon(ketQua);
        }
    }

    private void inDsDon(List<Order> list) {
        if (list == null || list.isEmpty()) {
            System.out.println("Không có dữ liệu.");
            return;
        }
        List<Object[]> rows = list.stream().map(o -> new Object[]{
            o.getOrderId(), o.getCustomerId(), o.getOrderDate(), dichTrangThai(o.getOrderStatus())
        }).toList();
        InBangHelper.inBang(new String[]{"Mã Đơn", "Mã KH", "Ngày Đặt", "Trạng Thái"}, rows);
    }

    private void inBang(List<?> list) {
        if (list == null || list.isEmpty()) {
            System.out.println("Không có dữ liệu.");
            return;
        }
        java.util.List<Object[]> rows = new java.util.ArrayList<>();
        for (Object item : list) {
            if (item instanceof Object[]) rows.add((Object[]) item);
        }
        InBangHelper.inBang(rows, null);
    }
}