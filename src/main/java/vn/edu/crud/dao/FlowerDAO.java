package vn.edu.crud.dao;

import vn.edu.crud.model.Flower;
import vn.edu.crud.model.FlowerSales;
import vn.edu.crud.util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO = Data Access Object
 * Chịu trách nhiệm CRUD + 10 câu query nghiệp vụ cho bảng Flower.
 */
public class FlowerDAO {

    // ══════════════════════════════════════════════════
    // CRUD CƠ BẢN (đã có từ trước)
    // ══════════════════════════════════════════════════

    public List<Flower> findAll() {
        List<Flower> list = new ArrayList<>();
        String sql = "SELECT * FROM Flower";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // Q05 — Search hoa theo mã
    public Flower findById(String id) {
        String sql = "SELECT * FROM Flower WHERE flower_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // Q03 — Tìm hoa có tên chứa từ khoá
    public List<Flower> findByName(String keyword) {
        List<Flower> list = new ArrayList<>();
        String sql = "SELECT * FROM Flower WHERE name LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // Q08 — Tìm hoa theo màu
    public List<Flower> findByColor(String color) {
        List<Flower> list = new ArrayList<>();
        String sql = "SELECT * FROM Flower WHERE color = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, color);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean insert(Flower f) {
        String sql = "INSERT INTO Flower (flower_id, name, category, unit_price, "
                + "stock_quantity, color, supplier_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, f.getFlowerId());
            ps.setString(2, f.getName());
            ps.setString(3, f.getCategory());
            ps.setBigDecimal(4, f.getUnitPrice());
            ps.setInt(5, f.getStockQuantity());
            ps.setString(6, f.getColor());
            ps.setString(7, f.getSupplierId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean update(Flower f) {
        String sql = "UPDATE Flower SET name=?, category=?, unit_price=?, "
                + "stock_quantity=?, color=?, supplier_id=? WHERE flower_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, f.getName());
            ps.setString(2, f.getCategory());
            ps.setBigDecimal(3, f.getUnitPrice());
            ps.setInt(4, f.getStockQuantity());
            ps.setString(5, f.getColor());
            ps.setString(6, f.getSupplierId());
            ps.setString(7, f.getFlowerId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean delete(String id) {
        String sql = "DELETE FROM Flower WHERE flower_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // ══════════════════════════════════════════════════
    // 10 CÂU QUERY NGHIỆP VỤ
    // ══════════════════════════════════════════════════

    // ─── Q01: Hoa sắp hết hàng (stock < 10) ───
    public List<Flower> findLowStock() {
        List<Flower> list = new ArrayList<>();
        String sql = "SELECT * FROM Flower WHERE stock_quantity < 10 "
                + "ORDER BY stock_quantity";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // ─── Q06: Hoa có giá trong khoảng [min, max] ───
    public List<Flower> findByPriceRange(BigDecimal min, BigDecimal max) {
        List<Flower> list = new ArrayList<>();
        String sql = "SELECT * FROM Flower WHERE unit_price BETWEEN ? AND ? "
                + "ORDER BY unit_price";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, min);
            ps.setBigDecimal(2, max);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // ─── Q07: Hoa bán chạy nhất (top N) ───
    public List<FlowerSales> findBestSellers(int limit) {
        List<FlowerSales> list = new ArrayList<>();
        String sql = "SELECT f.*, "
                + "COALESCE((SELECT SUM(quantity) FROM OrderDetail "
                + "          WHERE flower_id = f.flower_id), 0) AS total_sold "
                + "FROM Flower f "
                + "ORDER BY total_sold DESC "
                + "LIMIT ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Flower f = mapRow(rs);
                    int sold = rs.getInt("total_sold");
                    list.add(new FlowerSales(f, sold));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // ─── Q10: Sắp xếp hoa theo giá giảm dần ───
    public List<Flower> findAllOrderByPriceDesc() {
        List<Flower> list = new ArrayList<>();
        String sql = "SELECT * FROM Flower ORDER BY unit_price DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // ─── Q12: Hoa bán ế nhất (top N ít bán nhất) ───
    public List<FlowerSales> findWorstSellers(int limit) {
        List<FlowerSales> list = new ArrayList<>();
        String sql = "SELECT f.*, "
                + "COALESCE((SELECT SUM(quantity) FROM OrderDetail "
                + "          WHERE flower_id = f.flower_id), 0) AS total_sold "
                + "FROM Flower f "
                + "ORDER BY total_sold ASC "
                + "LIMIT ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Flower f = mapRow(rs);
                    int sold = rs.getInt("total_sold");
                    list.add(new FlowerSales(f, sold));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // ─── Q23: Tổng số lượng đã bán của từng loại hoa ───
    public List<FlowerSales> findSoldQuantityByFlower() {
        List<FlowerSales> list = new ArrayList<>();
        String sql = "SELECT f.*, "
                + "COALESCE((SELECT SUM(quantity) FROM OrderDetail "
                + "          WHERE flower_id = f.flower_id), 0) AS total_sold "
                + "FROM Flower f "
                + "ORDER BY total_sold DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Flower f = mapRow(rs);
                int sold = rs.getInt("total_sold");
                list.add(new FlowerSales(f, sold));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // ─── Q28: Hoa chưa từng xuất hiện trong đơn hàng nào ───
    public List<Flower> findNeverOrdered() {
        List<Flower> list = new ArrayList<>();
        String sql = "SELECT * FROM Flower "
                + "WHERE flower_id NOT IN (SELECT DISTINCT flower_id FROM OrderDetail)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // ══════════════════════════════════════════════════
    // HELPER
    // ══════════════════════════════════════════════════
    private Flower mapRow(ResultSet rs) throws SQLException {
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