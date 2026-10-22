package vn.edu.crud.dao;

import vn.edu.crud.model.Flower;
import vn.edu.crud.util.DBBase;
import vn.edu.crud.util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FlowerDAO {

    // =========================================================================
    // HÀM SỬ DỤNG BIỂU THỨC LAMBDA & STREAM API CHO MỤC 4 (QUẢN LÝ HOA)
    // =========================================================================

    // 1. Tìm kiếm hoa theo Tên hoặc Danh mục bằng Lambda
    public List<Flower> searchFlowersByLambda(String keyword) {
        List<Flower> allFlowers = this.findAll();

        if (keyword == null || keyword.trim().isEmpty()) {
            return allFlowers;
        }

        String tuKhoa = keyword.toLowerCase().trim();

        // Sử dụng Stream API và biểu thức Lambda để lọc theo tên hoặc danh mục
        return allFlowers.stream()
                .filter(f -> (f.getName() != null && f.getName().toLowerCase().contains(tuKhoa)) ||
                             (f.getCategory() != null && f.getCategory().toLowerCase().contains(tuKhoa)))
                .collect(Collectors.toList());
    }

    // 2. Lọc danh sách hoa sắp hết hàng (tồn kho < ngưỡng) bằng Lambda
    public List<Flower> hoaSapHetLambda(int nguong) {
        List<Flower> allFlowers = this.findAll();

        return allFlowers.stream()
                .filter(f -> f.getStockQuantity() < nguong)
                .collect(Collectors.toList());
    }

    // 3. Lọc hoa theo khoảng giá bằng Lambda
    public List<Flower> timTheoKhoangGiaLambda(BigDecimal giaMin, BigDecimal giaMax) {
        List<Flower> allFlowers = this.findAll();

        return allFlowers.stream()
                .filter(f -> f.getUnitPrice() != null
                          && f.getUnitPrice().compareTo(giaMin) >= 0
                          && f.getUnitPrice().compareTo(giaMax) <= 0)
                .collect(Collectors.toList());
    }

    // 4. Sắp xếp danh sách hoa theo giá giảm dần bằng Lambda
    public List<Flower> hoaGiaGiamDanLambda() {
        List<Flower> allFlowers = this.findAll();

        return allFlowers.stream()
                .filter(f -> f.getUnitPrice() != null)
                .sorted((f1, f2) -> f2.getUnitPrice().compareTo(f1.getUnitPrice()))
                .collect(Collectors.toList());
    }

    // =====================================================
    // NHÓM CRUD
    // =====================================================

    public List<Flower> findAll() {
        List<Flower> list = new ArrayList<>();
        String sql = "SELECT * FROM flower";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(docMotHoa(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public Flower timTheoMa(String maHoa) {
        String sql = "SELECT * FROM flower WHERE flower_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHoa);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return docMotHoa(rs);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public boolean insert(String id, String name, String cat, BigDecimal price,
                          int stock, String color, String supId) {
        String sql = "INSERT INTO flower " +
                "(flower_id, name, category, unit_price, stock_quantity, color, supplier_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        return DBBase.update(sql, id, name, cat, price, stock, color, supId) > 0;
    }

    public boolean update(String id, String name, String cat, BigDecimal price,
                          int stock, String color, String supId) {
        String sql = "UPDATE flower SET name=?, category=?, unit_price=?, " +
                "stock_quantity=?, color=?, supplier_id=? WHERE flower_id=?";
        return DBBase.update(sql, name, cat, price, stock, color, supId, id) > 0;
    }

    public boolean delete(String id) {
        return DBBase.update("DELETE FROM flower WHERE flower_id = ?", id) > 0;
    }

    public String sinhMaHoaMoi() {
        List<Object[]> rows = DBBase.query("SELECT flower_id FROM flower ORDER BY flower_id DESC LIMIT 1");
        if (rows.isEmpty()) return "FL01";
        String maCu = rows.get(0)[0].toString();
        int so = Integer.parseInt(maCu.substring(2));
        return String.format("FL%02d", so + 1);
    }

    // =====================================================
    // HÀM CỘNG TỒN KHO (MỚI THÊM)
    // =====================================================
    public boolean congTonKho(String flowerId, int soLuong) {
        String sql = "UPDATE flower SET stock_quantity = stock_quantity + ? WHERE flower_id = ?";
        return DBBase.update(sql, soLuong, flowerId) > 0;
    }

    /**
     * Đồng bộ lại tồn kho từ lô: stock_quantity = tổng quantity_remaining của các lô AVAILABLE.
     *
     * Chỉ áp dụng cho hoa ĐÃ CÓ lô. Hoa chưa có lô nào (nhập tay) thì giữ nguyên số hiện tại.
     * Dùng để chữa dữ liệu cũ bị lệch giữa 2 nơi.
     *
     * @return số dòng thực sự bị thay đổi
     */
    public int dongBoTonKhoTuLo() {
        String sql = "UPDATE flower f SET stock_quantity = " +
                "(SELECT COALESCE(SUM(fb.quantity_remaining), 0) FROM flowerbatch fb " +
                " WHERE fb.flower_id = f.flower_id AND fb.status = 'AVAILABLE') " +
                "WHERE EXISTS (SELECT 1 FROM flowerbatch fb2 WHERE fb2.flower_id = f.flower_id)";
        return DBBase.update(sql);
    }

    // =====================================================
    // NHÓM BÁO CÁO
    // =====================================================

    public List<Flower> hoaSapHet(int nguong) {
        List<Flower> list = new ArrayList<>();
        String sql = "SELECT * FROM flower WHERE stock_quantity < ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, nguong);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(docMotHoa(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<Flower> timTheoTen(String tuKhoa) {
        List<Flower> list = new ArrayList<>();
        String sql = "SELECT * FROM flower WHERE name LIKE ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + tuKhoa + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(docMotHoa(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<Flower> timTheoGia(BigDecimal giaMin, BigDecimal giaMax) {
        List<Flower> list = new ArrayList<>();
        String sql = "SELECT * FROM flower WHERE unit_price BETWEEN ? AND ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, giaMin);
            ps.setBigDecimal(2, giaMax);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(docMotHoa(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<Flower> timTheoMau(String mau) {
        List<Flower> list = new ArrayList<>();
        String sql = "SELECT * FROM flower WHERE color = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, mau);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(docMotHoa(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<Object[]> hoaBanChay() {
        String sql = "SELECT f.flower_id, f.name, SUM(od.quantity) AS total_sold " +
                "FROM flower f JOIN orderdetail od ON f.flower_id = od.flower_id " +
                "JOIN `Order` o ON od.order_id = o.order_id " +
                "WHERE o.order_status NOT IN ('DA_HUY', 'GIAO_THAT_BAI') " +
                "GROUP BY f.flower_id, f.name ORDER BY total_sold DESC";
        return DBBase.query(sql);
    }

    public List<Object[]> loHoaTonLau(int soNgay) {
        String sql = "SELECT f.flower_id, f.name, fb.batch_id, fb.import_date, " +
                "DATEDIFF(NOW(), fb.import_date) AS days_in_stock " +
                "FROM flower f JOIN flowerbatch fb ON f.flower_id = fb.flower_id " +
                "WHERE fb.quantity_remaining > 0 " +
                "AND DATEDIFF(NOW(), fb.import_date) > ?";
        return DBBase.query(sql, soNgay);
    }

    public List<Flower> hoaGiaGiamDan() {
        List<Flower> list = new ArrayList<>();
        String sql = "SELECT * FROM flower ORDER BY unit_price DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(docMotHoa(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<Object[]> hoaBanCham() {
        String sql = "SELECT f.flower_id, f.name, COALESCE(SUM(od.quantity),0) AS total_sold " +
                "FROM flower f LEFT JOIN orderdetail od ON f.flower_id = od.flower_id " +
                "    AND od.order_id IN (SELECT order_id FROM `Order` " +
                "                        WHERE order_status NOT IN ('DA_HUY', 'GIAO_THAT_BAI')) " +
                "GROUP BY f.flower_id, f.name ORDER BY total_sold ASC";
        return DBBase.query(sql);
    }

    public List<Object[]> tongBanTheoHoa() {
        String sql = "SELECT f.flower_id, f.name, COALESCE(SUM(od.quantity),0) AS total_sold " +
                "FROM flower f LEFT JOIN orderdetail od ON f.flower_id = od.flower_id " +
                "    AND od.order_id IN (SELECT order_id FROM `Order` " +
                "                        WHERE order_status NOT IN ('DA_HUY', 'GIAO_THAT_BAI')) " +
                "GROUP BY f.flower_id, f.name";
        return DBBase.query(sql);
    }

    public List<Flower> hoaChuaBanDuoc() {
        List<Flower> list = new ArrayList<>();
        String sql = "SELECT * FROM flower f WHERE NOT EXISTS (" +
                "SELECT 1 FROM orderdetail od JOIN `Order` o ON od.order_id = o.order_id " +
                "WHERE od.flower_id = f.flower_id " +
                "AND o.order_status NOT IN ('DA_HUY', 'GIAO_THAT_BAI'))";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(docMotHoa(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // =====================================================
    // HELPER
    // =====================================================

    private Flower docMotHoa(ResultSet rs) throws SQLException {
        Flower f = new Flower();
        f.setFlowerId(rs.getString("flower_id"));
        f.setName(rs.getString("name"));
        f.setCategory(rs.getString("category"));
        f.setUnitPrice(rs.getBigDecimal("unit_price"));
        f.setStockQuantity(rs.getInt("stock_quantity"));
        f.setColor(rs.getString("color"));
        f.setSupplierId(rs.getString("supplier_id"));
        return f;
    }
}