package vn.edu.crud.dao;

import vn.edu.crud.model.Payment;
import vn.edu.crud.util.DBBase;

import java.math.BigDecimal;
import java.util.List;

public class PaymentDAO {

    // =====================================================
    // NHÓM THAO TÁC
    // =====================================================

    public String sinhMaThanhToan() {
        String sql = "SELECT payment_id FROM Payment ORDER BY payment_id DESC LIMIT 1";
        List<Object[]> rows = DBBase.query(sql);
        if (rows.isEmpty())
            return "P001";
        String maCu = rows.get(0)[0].toString();
        int so = Integer.parseInt(maCu.substring(1));
        return String.format("P%03d", so + 1);
    }

    public BigDecimal tinhTongTienDon(String orderId) {
        String sql = "SELECT COALESCE(SUM(quantity * unit_price_at_sale), 0) " +
                "FROM OrderDetail WHERE order_id = ?";
        List<Object[]> rows = DBBase.query(sql, orderId);
        if (rows.isEmpty() || rows.get(0)[0] == null)
            return BigDecimal.ZERO;
        return (BigDecimal) rows.get(0)[0];
    }

    public BigDecimal tinhDaTra(String orderId) {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM Payment " +
                "WHERE order_id = ? AND payment_status = 'THANH_TOAN_THANH_CONG'";
        List<Object[]> rows = DBBase.query(sql, orderId);
        if (rows.isEmpty() || rows.get(0)[0] == null)
            return BigDecimal.ZERO;
        return (BigDecimal) rows.get(0)[0];
    }

    public boolean taoThanhToan(Payment p) {
        String sql = "INSERT INTO Payment " +
                "(payment_id, order_id, amount, payment_method, " +
                " payment_date, payment_status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        return DBBase.update(sql,
                p.getPaymentId(), p.getOrderId(), p.getAmount(),
                p.getPaymentMethod(), p.getPaymentDate(),
                p.getPaymentStatus()) > 0;
    }

    public String layMaDon(String paymentId) {
        String sql = "SELECT order_id FROM Payment WHERE payment_id = ?";
        List<Object[]> rows = DBBase.query(sql, paymentId);
        if (rows.isEmpty() || rows.get(0)[0] == null)
            return null;
        return (String) rows.get(0)[0];
    }

    public String layTrangThaiThanhToan(String paymentId) {
        String sql = "SELECT payment_status FROM Payment WHERE payment_id = ?";
        List<Object[]> rows = DBBase.query(sql, paymentId);
        if (rows.isEmpty() || rows.get(0)[0] == null)
            return null;
        return (String) rows.get(0)[0];
    }

    public boolean xoaThanhToan(String paymentId) {
        return DBBase.update(
                "UPDATE Payment SET payment_status = 'DA_HUY' WHERE payment_id = ?",
                paymentId) > 0;
    }

    // =====================================================
    // NHÓM BÁO CÁO
    // =====================================================

    public List<Object[]> thanhToanTheoPhuongThuc(String phuongThuc) {
        String sql = "SELECT * FROM Payment WHERE payment_method = ?";
        return DBBase.query(sql, phuongThuc);
    }

    public List<Object[]> doanhThuHomNay() {
        String sql = "SELECT COALESCE(SUM(amount),0) AS total " +
                "FROM Payment " +
                "WHERE DATE(payment_date) = CURDATE() " +
                "AND payment_status = 'THANH_TOAN_THANH_CONG'";
        return DBBase.query(sql);
    }

    public List<Object[]> tatCaThanhToan() {
        String sql = "SELECT payment_id, order_id, amount, payment_method, " +
                "payment_date, payment_status FROM Payment";
        return DBBase.query(sql);
    }
}