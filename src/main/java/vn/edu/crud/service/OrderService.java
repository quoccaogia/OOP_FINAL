package vn.edu.crud.service;

import vn.edu.crud.dao.OrderDAO;
import vn.edu.crud.model.Order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Tầng Service xử lý nghiệp vụ Quản lý Đơn hàng (Mục 5).
 * Đóng gói toàn bộ các hàm lọc, tìm kiếm và sắp xếp đơn hàng bằng Lambda & Stream API.
 */
public class OrderService {

    private final OrderDAO orderDAO;

    public OrderService() {
        this.orderDAO = new OrderDAO();
    }

    public OrderService(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    // =========================================================================
    // HÀM NGHIỆP VỤ SỬ DỤNG LAMBDA & STREAM API (MỤC 5)
    // =========================================================================

    // 1. Tìm kiếm đơn hàng theo Mã đơn, Mã KH, hoặc Mã NV bằng Lambda
    public List<Order> searchOrdersByLambda(String keyword) {
        List<Order> allOrders = orderDAO.findAll();
        if (keyword == null || keyword.trim().isEmpty()) {
            return allOrders;
        }
        String kw = keyword.toLowerCase().trim();

        return allOrders.stream()
                .filter(o -> (o.getOrderId() != null && o.getOrderId().toLowerCase().contains(kw)) ||
                             (o.getCustomerId() != null && o.getCustomerId().toLowerCase().contains(kw)) ||
                             (o.getEmployeeId() != null && o.getEmployeeId().toLowerCase().contains(kw)))
                .collect(Collectors.toList());
    }

    // 2. Lọc danh sách đơn chưa giao hôm nay bằng Lambda
    public List<Order> donChuaGiaoHomNayLambda() {
        List<Order> allOrders = orderDAO.findAll();
        LocalDate homNay = LocalDate.now();

        return allOrders.stream()
                .filter(o -> o.getDeliveryDate() != null
                          && o.getDeliveryDate().toLocalDate().equals(homNay)
                          && !"HOAN_THANH".equals(o.getOrderStatus())
                          && !"DA_HUY".equals(o.getOrderStatus())
                          && !"GIAO_THAT_BAI".equals(o.getOrderStatus()))
                .collect(Collectors.toList());
    }

    // 3. Lọc danh sách đơn hàng của một khách hàng bằng Lambda
    public List<Order> donCuaKhachLambda(String maKhach) {
        List<Order> allOrders = orderDAO.findAll();
        if (maKhach == null || maKhach.trim().isEmpty()) {
            return new ArrayList<>();
        }

        return allOrders.stream()
                .filter(o -> o.getCustomerId() != null && o.getCustomerId().equalsIgnoreCase(maKhach.trim()))
                .collect(Collectors.toList());
    }

    // 4. Lọc đơn hàng theo trạng thái cụ thể bằng Lambda
    public List<Order> locDonTheoTrangThaiLambda(String status) {
        List<Order> allOrders = orderDAO.findAll();
        if (status == null || status.trim().isEmpty()) {
            return allOrders;
        }

        return allOrders.stream()
                .filter(o -> o.getOrderStatus() != null && o.getOrderStatus().equalsIgnoreCase(status.trim()))
                .collect(Collectors.toList());
    }

    // 5. Sắp xếp đơn hàng mới nhất lên đầu bằng Lambda
    public List<Order> donHangMoiNhatLambda() {
        List<Order> allOrders = orderDAO.findAll();

        return allOrders.stream()
                .filter(o -> o.getOrderDate() != null)
                .sorted((o1, o2) -> o2.getOrderDate().compareTo(o1.getOrderDate()))
                .collect(Collectors.toList());
    }

    // =========================================================================
    // CÁC HÀM ĐIỀU PHỐI QUA DAO
    // =========================================================================

    public List<Order> findAll() {
        return orderDAO.findAll();
    }

    public String sinhMaDonMoi() {
        return orderDAO.sinhMaDonMoi();
    }

    public boolean taoDonHang(Order o) {
        return orderDAO.taoDonHang(o);
    }

    public boolean donTonTai(String orderId) {
        return orderDAO.donTonTai(orderId);
    }

    public String layTrangThaiDon(String orderId) {
        return orderDAO.layTrangThaiDon(orderId);
    }

    public boolean capNhatTrangThai(String orderId, String status) {
        return orderDAO.capNhatTrangThai(orderId, status);
    }

    public boolean hoaDaCoTrongDon(String orderId, String flowerId) {
        return orderDAO.hoaDaCoTrongDon(orderId, flowerId);
    }

    public boolean congThemSoLuong(String orderId, String flowerId, int qty) {
        return orderDAO.congThemSoLuong(orderId, flowerId, qty);
    }

    public boolean themChiTiet(String orderId, String flowerId, int qty, BigDecimal price) {
        return orderDAO.themChiTiet(orderId, flowerId, qty, price);
    }

    public boolean truSoLuong(String flowerId, int qty) {
        return orderDAO.truSoLuong(flowerId, qty);
    }

    public boolean congTonKho(String flowerId, int soLuong) {
        return orderDAO.congTonKho(flowerId, soLuong);
    }

    public int demSoMatHang(String orderId) {
        return orderDAO.demSoMatHang(orderId);
    }

    public boolean donDaCoThanhToan(String orderId) {
        return orderDAO.donDaCoThanhToan(orderId);
    }

    public List<Object[]> thongTinDayDuDonHang(String orderId) {
        return orderDAO.thongTinDayDuDonHang(orderId);
    }

    public List<Object[]> layHoaTrongDon(String orderId) {
        return orderDAO.layHoaTrongDon(orderId);
    }

    public List<Object[]> chiTietDonHang(String orderId) {
        return orderDAO.chiTietDonHang(orderId);
    }

    public int laySoLuongTrongDon(String orderId, String flowerId) {
        return orderDAO.laySoLuongTrongDon(orderId, flowerId);
    }

    public boolean xoaChiTiet(String orderId, String flowerId) {
        return orderDAO.xoaChiTiet(orderId, flowerId);
    }

    public boolean xoaDon(String orderId) {
        return orderDAO.xoaDon(orderId);
    }

    public Order timTheoMa(String orderId) {
        return orderDAO.timTheoMa(orderId);
    }

    public boolean capNhatThongTin(String orderId, String diaChi, LocalDateTime ngayGiao, String employeeId) {
        return orderDAO.capNhatThongTin(orderId, diaChi, ngayGiao, employeeId);
    }

    public List<Order> donCuaKhach(String customerId) {
        return orderDAO.donCuaKhach(customerId);
    }

    public List<Object[]> donHangGiaCao(BigDecimal minTotal) {
        return orderDAO.donHangGiaCao(minTotal);
    }

    public BigDecimal doanhThuHomNay() {
        return orderDAO.doanhThuHomNay();
    }

    public List<Object[]> thongKeDonTheoTrangThai() {
        return orderDAO.thongKeDonTheoTrangThai();
    }

    public List<Object[]> donCoNhieuLoaiHoa(int soLoaiToiThieu) {
        return orderDAO.donCoNhieuLoaiHoa(soLoaiToiThieu);
    }

    public List<Object[]> khachDaTungDatHang() {
        return orderDAO.khachDaTungDatHang();
    }

    public List<Object[]> khachChuaTungDatHang() {
        return orderDAO.khachChuaTungDatHang();
    }

    public List<Object[]> khachNhieuDonNhat() {
        return orderDAO.khachNhieuDonNhat();
    }

    public List<Object[]> nhanVienNhieuDonNhat() {
        return orderDAO.nhanVienNhieuDonNhat();
    }

    public List<Object[]> tongChiTieuTheoKhach() {
        return orderDAO.tongChiTieuTheoKhach();
    }
}
