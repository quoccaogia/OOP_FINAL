package vn.edu.crud.dao;

import vn.edu.crud.model.Order;
import vn.edu.crud.util.DBBase;
import vn.edu.crud.util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    // =====================================================
    // NHÓM THAO TÁC
    // =====================================================

    public String sinhMaDonMoi() {
        String sql = "SELECT order_id FROM `Order` ORDER BY order_id DESC LIMIT 1";
        List<Object[]> rows = DBBase.query(sql);
        if (rows.isEmpty()) return "ORD001";
        String maCu = rows.get(0)[0].toString();
        int so = Integer.parseInt(maCu.substring(3));
        return String.format("ORD%03d", so + 1);
    }

    public boolean taoDonHang(Order o) {
        String sql = "INSERT INTO `Order` " +
                "(order_id, customer_id, employee_id, order_date, " +
                " delivery_date, delivery_address, order_status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        return DBBase.update(sql,
                o.getOrderId(), o.getCustomerId(), o.getEmployeeId(),
                o.getOrderDate(), o.getDeliveryDate(),
                o.getDeliveryAddress(), o.getOrderStatus()) > 0;
    }

    public boolean donTonTai(String orderId) {
        String sql = "SELECT 1 FROM `Order` WHERE order_id = ?";
        return !DBBase.query(sql, orderId).isEmpty();
    }

    public boolean hoaDaCoTrongDon(String orderId, String flowerId) {
        String sql = "SELECT 1 FROM OrderDetail WHERE order_id = ? AND flower_id = ?";
        return !DBBase.query(sql, orderId, flowerId).isEmpty();
    }

    public boolean themChiTiet(String orderId, String flowerId,
                               int quantity, BigDecimal price) {
        String sql = "INSERT INTO OrderDetail " +
                "(order_id, flower_id, quantity, unit_price_at_sale) " +
                "VALUES (?, ?, ?, ?)";
        return DBBase.update(sql, orderId, flowerId, quantity, price) > 0;
    }

    public boolean congThemSoLuong(String orderId, String flowerId, int them) {
        String sql = "UPDATE OrderDetail SET quantity = quantity + ? " +
                "WHERE order_id = ? AND flower_id = ?";
        return DBBase.update(sql, them, orderId, flowerId) > 0;
    }

    public boolean capNhatTrangThai(String orderId, String status) {
        String sql = "UPDATE `Order` SET order_status = ? WHERE order_id = ?";
        return DBBase.update(sql, status, orderId) > 0;
    }

    public boolean xoaDon(String orderId) {
        return DBBase.update(
                "DELETE FROM `Order` WHERE order_id = ?", orderId) > 0;
    }

    public boolean donDaCoThanhToan(String orderId) {
        String sql = "SELECT 1 FROM Payment WHERE order_id = ?";
        return !DBBase.query(sql, orderId).isEmpty();
    }

    public boolean xoaChiTiet(String orderId, String flowerId) {
        String sql = "DELETE FROM OrderDetail WHERE order_id = ? AND flower_id = ?";
        return DBBase.update(sql, orderId, flowerId) > 0;
    }

    // =====================================================
    // NHÓM BÁO CÁO
    // =====================================================

    public List<Object[]> donHangGiaCao(BigDecimal min) {
        String sql = "SELECT o.order_id, o.customer_id, o.employee_id, o.order_date, " +
                "SUM(od.quantity * od.unit_price_at_sale) AS total " +
                "FROM `Order` o JOIN OrderDetail od ON o.order_id = od.order_id " +
                "GROUP BY o.order_id, o.customer_id, o.employee_id, o.order_date " +
                "HAVING total > ?";
        return DBBase.query(sql, min);
    }

    public List<Order> donChuaGiaoHomNay() {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM `Order` " +
                "WHERE DATE(delivery_date) = CURDATE() " +
                "AND order_status NOT IN ('HOAN_THANH', 'DA_HUY')";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(docMotDon(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public BigDecimal doanhThuHomNay() {
        String sql = "SELECT COALESCE(SUM(amount),0) FROM Payment " +
                "WHERE DATE(payment_date) = CURDATE() " +
                "AND payment_status = 'THANH_TOAN_THANH_CONG'";
        List<Object[]> rows = DBBase.query(sql);
        if (rows.isEmpty() || rows.get(0)[0] == null) return BigDecimal.ZERO;
        return (BigDecimal) rows.get(0)[0];
    }

    public List<Object[]> khachDaTungDatHang() {
        String sql = "SELECT DISTINCT c.customer_id, c.name, c.phone, c.address, c.email " +
                "FROM Customer c JOIN `Order` o ON c.customer_id = o.customer_id";
        return DBBase.query(sql);
    }

    public List<Object[]> khachChuaTungDatHang() {
        String sql = "SELECT c.customer_id, c.name, c.phone, c.address, c.email " +
                "FROM Customer c WHERE NOT EXISTS (" +
                "SELECT 1 FROM `Order` o WHERE o.customer_id = c.customer_id)";
        return DBBase.query(sql);
    }

    public List<Object[]> khachNhieuDonNhat() {
        String sql = "SELECT c.customer_id, c.name, COUNT(o.order_id) AS order_count " +
                "FROM Customer c JOIN `Order` o ON c.customer_id = o.customer_id " +
                "GROUP BY c.customer_id, c.name " +
                "ORDER BY order_count DESC LIMIT 1";
        return DBBase.query(sql);
    }

    public List<Object[]> nhanVienCoDonHang() {
        String sql = "SELECT DISTINCT e.employee_id, e.name, e.role, e.phone, e.salary " +
                "FROM Employee e JOIN `Order` o ON e.employee_id = o.employee_id";
        return DBBase.query(sql);
    }

    public List<Object[]> nhanVienNhieuDonNhat() {
        String sql = "SELECT e.employee_id, e.name, COUNT(o.order_id) AS order_count " +
                "FROM Employee e JOIN `Order` o ON e.employee_id = o.employee_id " +
                "GROUP BY e.employee_id, e.name " +
                "ORDER BY order_count DESC LIMIT 1";
        return DBBase.query(sql);
    }

    public List<Object[]> tongChiTieuTheoKhach() {
        String sql = "SELECT c.customer_id, c.name, " +
                "COALESCE(SUM(od.quantity * od.unit_price_at_sale),0) AS total_spent " +
                "FROM Customer c " +
                "LEFT JOIN `Order` o ON c.customer_id = o.customer_id " +
                "LEFT JOIN OrderDetail od ON o.order_id = od.order_id " +
                "GROUP BY c.customer_id, c.name";
        return DBBase.query(sql);
    }

    public List<Order> donCuaKhach(String maKhach) {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM `Order` WHERE customer_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKhach);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(docMotDon(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<Object[]> chiTietDonHang(String maDon) {
        String sql = "SELECT f.name AS flower_name, od.quantity, od.unit_price_at_sale " +
                "FROM OrderDetail od JOIN Flower f ON od.flower_id = f.flower_id " +
                "WHERE od.order_id = ?";
        return DBBase.query(sql, maDon);
    }

    // ===== MỚI THÊM: thông tin đầy đủ của 1 đơn (JOIN khách + nhân viên) =====
    public List<Object[]> thongTinDayDuDonHang(String orderId) {
        String sql = "SELECT o.order_id, c.name AS ten_khach, c.phone AS sdt_khach, " +
                "e.name AS ten_nv, o.order_date, o.delivery_date, " +
                "o.delivery_address, o.order_status " +
                "FROM `Order` o " +
                "JOIN Customer c ON o.customer_id = c.customer_id " +
                "JOIN Employee e ON o.employee_id = e.employee_id " +
                "WHERE o.order_id = ?";
        return DBBase.query(sql, orderId);
    }

    public List<Object[]> donCoNhieuLoaiHoa(int n) {
        String sql = "SELECT order_id, COUNT(DISTINCT flower_id) AS flower_count " +
                "FROM OrderDetail GROUP BY order_id HAVING flower_count >= ?";
        return DBBase.query(sql, n);
    }

    public List<Object[]> thongKeDonTheoTrangThai() {
        String sql = "SELECT order_status, COUNT(*) AS order_count " +
                "FROM `Order` GROUP BY order_status";
        return DBBase.query(sql);
    }

    // =====================================================
    // HELPER
    // =====================================================

    private Order docMotDon(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setOrderId(rs.getString("order_id"));
        o.setCustomerId(rs.getString("customer_id"));
        o.setEmployeeId(rs.getString("employee_id"));
        o.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());
        o.setDeliveryDate(rs.getTimestamp("delivery_date").toLocalDateTime());
        o.setDeliveryAddress(rs.getString("delivery_address"));
        o.setOrderStatus(rs.getString("order_status"));
        return o;
    }
}