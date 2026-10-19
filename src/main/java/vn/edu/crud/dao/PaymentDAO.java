package vn.edu.crud.dao;

import vn.edu.crud.model.Payment;
import vn.edu.crud.util.DBBase;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {

    // =====================================================
    // NHÓM THAO TÁC
    // =====================================================

    public String sinhMaThanhToan() {
        String sql = "SELECT payment_id FROM payment ORDER BY payment_id DESC LIMIT 1";
        List<Object[]> rows = DBBase.query(sql);
        if (rows.isEmpty())
            return "P001";
        String maCu = rows.get(0)[0].toString();
        int so = Integer.parseInt(maCu.substring(1));
        return String.format("P%03d", so + 1);
    }

    public BigDecimal tinhTongTienDon(String orderId) {
        String sql = "SELECT COALESCE(SUM(quantity * unit_price_at_sale), 0) " +
                "FROM orderdetail WHERE order_id = ?";
        List<Object[]> rows = DBBase.query(sql, orderId);
        if (rows.isEmpty() || rows.get(0)[0] == null)
            return BigDecimal.ZERO;
        return (BigDecimal) rows.get(0)[0];
    }

    public BigDecimal tinhDaTra(String orderId) {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM payment " +
                "WHERE order_id = ? AND payment_status = 'THANH_TOAN_THANH_CONG'";
        List<Object[]> rows = DBBase.query(sql, orderId);
        if (rows.isEmpty() || rows.get(0)[0] == null)
            return BigDecimal.ZERO;
        return (BigDecimal) rows.get(0)[0];
    }

    public boolean taoThanhToan(Payment p) {
        String sql = "INSERT INTO payment " +
                "(payment_id, order_id, amount, payment_method, " +
                " payment_date, payment_status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        return DBBase.update(sql,
                p.getPaymentId(), p.getOrderId(), p.getAmount(),
                p.getPaymentMethod(), p.getPaymentDate(),
                p.getPaymentStatus()) > 0;
    }

    public String layMaDon(String paymentId) {
        String sql = "SELECT order_id FROM payment WHERE payment_id = ?";
        List<Object[]> rows = DBBase.query(sql, paymentId);
        if (rows.isEmpty() || rows.get(0)[0] == null)
            return null;
        return (String) rows.get(0)[0];
    }

    public String layTrangThaiThanhToan(String paymentId) {
        String sql = "SELECT payment_status FROM payment WHERE payment_id = ?";
        List<Object[]> rows = DBBase.query(sql, paymentId);
        if (rows.isEmpty() || rows.get(0)[0] == null)
            return null;
        return (String) rows.get(0)[0];
    }

    public BigDecimal tinhTienCoc(String orderId) {
        return tinhTongTienDon(orderId)
                .multiply(new BigDecimal("0.2"))
                .setScale(0, RoundingMode.CEILING);
    }

    public boolean xoaThanhToan(String paymentId) {
        return DBBase.update(
                "UPDATE payment SET payment_status = 'DA_HUY' WHERE payment_id = ?",
                paymentId) > 0;
    }

    // =====================================================
    // HÀM MỚI: LẤY DANH SÁCH ĐƠN CHƯA THANH TOÁN ĐỦ
    // =====================================================
    public List<Object[]> layDanhSachDonChuaThanhToan() {
        // Lấy tất cả đơn (trừ đơn đã hủy)
        String sql = "SELECT o.order_id, o.order_status, c.name, " +
                "(SELECT COALESCE(SUM(quantity * unit_price_at_sale), 0) FROM orderdetail od WHERE od.order_id = o.order_id) as total, " +
                "(SELECT COALESCE(SUM(amount), 0) FROM payment p WHERE p.order_id = o.order_id AND p.payment_status = 'THANH_TOAN_THANH_CONG') as paid " +
                "FROM `order` o " +
                "JOIN customer c ON o.customer_id = c.customer_id " +
                "WHERE o.order_status != 'DA_HUY'";

        List<Object[]> rawList = DBBase.query(sql);
        List<Object[]> ketQua = new ArrayList<>();

        // Lọc ở Java: Chỉ lấy những đơn có Tổng tiền > Đã trả (tức là còn nợ)
        for (Object[] row : rawList) {
            BigDecimal total = (BigDecimal) row[3];
            BigDecimal paid = (BigDecimal) row[4];
            if (total.compareTo(paid) > 0 && total.compareTo(BigDecimal.ZERO) > 0) {
                ketQua.add(row);
            }
        }
        return ketQua;
    }

    // =====================================================
    // NHÓM BÁO CÁO
    // =====================================================

    public List<Object[]> thanhToanTheoPhuongThuc(String phuongThuc) {
        String sql = "SELECT * FROM payment WHERE payment_method = ?";
        return DBBase.query(sql, phuongThuc);
    }

    public List<Object[]> doanhThuHomNay() {
        String sql = "SELECT COALESCE(SUM(amount),0) AS total " +
                "FROM payment " +
                "WHERE DATE(payment_date) = CURDATE() " +
                "AND payment_status = 'THANH_TOAN_THANH_CONG'";
        return DBBase.query(sql);
    }

    public List<Object[]> tatCaThanhToan() {
        String sql = "SELECT payment_id, order_id, amount, payment_method, " +
                "payment_date, payment_status FROM payment";
        return DBBase.query(sql);
    }
}