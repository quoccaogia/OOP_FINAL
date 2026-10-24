package vn.edu.crud.service;

import vn.edu.crud.dao.PaymentDAO;
import vn.edu.crud.model.Payment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Tầng Service xử lý nghiệp vụ Quản lý Thanh toán (Mục 6).
 * Đóng gói logic lọc thanh toán và tính doanh thu hôm nay bằng Lambda & Stream API.
 */
public class PaymentService {

    private final PaymentDAO paymentDAO;

    public PaymentService() {
        this.paymentDAO = new PaymentDAO();
    }

    public PaymentService(PaymentDAO paymentDAO) {
        this.paymentDAO = paymentDAO;
    }

    // =========================================================================
    // HÀM NGHIỆP VỤ SỬ DỤNG LAMBDA & STREAM API (MỤC 6)
    // =========================================================================

    /**
     * Lọc danh sách thanh toán theo phương thức bằng Lambda.
     */
    public List<Payment> theoPhuongThucLambda(String phuongThuc) {
        return paymentDAO.layDanhSachThanhToan().stream()
                .filter(p -> phuongThuc.equals(p.getPaymentMethod()))
                .collect(Collectors.toList());
    }

    /**
     * Tính tổng doanh thu hôm nay bằng Stream filter, map và reduce.
     */
    public BigDecimal doanhThuHomNayLambda() {
        LocalDate homNay = LocalDate.now();
        return paymentDAO.layDanhSachThanhToan().stream()
                .filter(p -> "THANH_TOAN_THANH_CONG".equals(p.getPaymentStatus())
                        && p.getPaymentDate() != null
                        && p.getPaymentDate().toLocalDate().equals(homNay)
                        && p.getAmount() != null)
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // =========================================================================
    // CÁC HÀM ĐIỀU PHỐI CRUD QUA DAO
    // =========================================================================

    public List<Object[]> tatCaThanhToan() {
        return paymentDAO.tatCaThanhToan();
    }

    public List<Payment> layDanhSachThanhToan() {
        return paymentDAO.layDanhSachThanhToan();
    }

    public String layMaDon(String id) {
        return paymentDAO.layMaDon(id);
    }

    public String layTrangThaiThanhToan(String id) {
        return paymentDAO.layTrangThaiThanhToan(id);
    }

    public boolean hoanTien(String id) {
        return paymentDAO.hoanTien(id);
    }

    public boolean hoanTienToanBo(String orderId) {
        return paymentDAO.hoanTienToanBo(orderId);
    }

    public boolean xoaThanhToan(String id) {
        return paymentDAO.xoaThanhToan(id);
    }

    public boolean taoThanhToan(Payment p) {
        return paymentDAO.taoThanhToan(p);
    }

    public String sinhMaThanhToan() {
        return paymentDAO.sinhMaThanhToan();
    }

    public BigDecimal tinhTongTienDon(String orderId) {
        return paymentDAO.tinhTongTienDon(orderId);
    }

    public BigDecimal tinhDaTra(String orderId) {
        return paymentDAO.tinhDaTra(orderId);
    }

    public BigDecimal tinhTienCoc(String orderId) {
        return paymentDAO.tinhTienCoc(orderId);
    }

    public BigDecimal tinhDaHoanTien(String orderId) {
        return paymentDAO.tinhDaHoanTien(orderId);
    }

    public List<Object[]> layDanhSachDonChuaThanhToan() {
        return paymentDAO.layDanhSachDonChuaThanhToan();
    }

    public List<Object[]> thanhToanTheoPhuongThuc(String phuongThuc) {
        return paymentDAO.thanhToanTheoPhuongThuc(phuongThuc);
    }

    public List<Object[]> doanhThuHomNay() {
        return paymentDAO.doanhThuHomNay();
    }
}
