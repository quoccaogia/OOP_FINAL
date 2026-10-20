package vn.edu.crud.dao;

import vn.edu.crud.model.Supplier;
import vn.edu.crud.util.DBBase;
import vn.edu.crud.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {

    // =====================================================
    // NHÓM CRUD
    // =====================================================

    public List<Supplier> findAll() {
        List<Supplier> list = new ArrayList<>();
        String sql = "SELECT * FROM Supplier";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // Hàm tìm theo ID (Trả về Object Supplier thay vì List<Object[]>)
    public Supplier findById(String id) {
        String sql = "SELECT * FROM Supplier WHERE supplier_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // Tìm theo SĐT (Dùng cho nghiệp vụ tiếp nhận)
    public List<Supplier> timTheoSdt(String sdt) {
        List<Supplier> list = new ArrayList<>();
        String sql = "SELECT * FROM Supplier WHERE phone LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + sdt + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean insert(Supplier s) {
        String sql = "INSERT INTO Supplier (supplier_id, name, phone, address, representative) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getSupplierId());
            ps.setString(2, s.getName());
            ps.setString(3, s.getPhone());
            ps.setString(4, s.getAddress());
            ps.setString(5, s.getRepresentative());
            return ps.executeUpdate() > 0;
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("⚠️ LỖI: Số điện thoại [" + s.getPhone() + "] đã được dùng cho NCC khác!");
            return false;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean update(Supplier s) {
        String sql = "UPDATE Supplier SET name=?, phone=?, address=?, representative=? WHERE supplier_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getName());
            ps.setString(2, s.getPhone());
            ps.setString(3, s.getAddress());
            ps.setString(4, s.getRepresentative());
            ps.setString(5, s.getSupplierId());
            return ps.executeUpdate() > 0;
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("⚠️ LỖI: Số điện thoại [" + s.getPhone() + "] đã bị trùng với NCC khác!");
            return false;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean delete(String id) {
        return DBBase.update("DELETE FROM Supplier WHERE supplier_id = ?", id) > 0;
    }

    // NCC còn đang cung cấp loại hoa nào không (để chặn xóa theo đúng quy tắc RESTRICT)
    public boolean coHoa(String supplierId) {
        String sql = "SELECT 1 FROM Flower WHERE supplier_id = ? LIMIT 1";
        return !DBBase.query(sql, supplierId).isEmpty();
    }

    public String sinhMaNCCMoi() {
        List<Object[]> rows = DBBase.query("SELECT supplier_id FROM Supplier ORDER BY supplier_id DESC LIMIT 1");
        if (rows.isEmpty()) return "NCC01";
        String maCu = rows.get(0)[0].toString();
        int so = Integer.parseInt(maCu.substring(3));
        return String.format("NCC%02d", so + 1);
    }

    public String chuanHoaSdt(String sdt) {
        if (sdt == null || sdt.trim().isEmpty()) return "";
        String s = sdt.trim().replaceAll("\\s+", "");
        if (s.startsWith("+84")) s = "0" + s.substring(3);
        s = s.replaceFirst("^0+", "0");
        return s;
    }

    // =====================================================
    // NHÓM BÁO CÁO (Giữ nguyên từ code cũ của bạn)
    // =====================================================

    public List<Object[]> tongNhapTheoNCC() {
        String sql = "SELECT s.supplier_id, s.name, " +
                "COALESCE(SUM(fb.quantity_imported * fb.unit_cost),0) AS total_import " +
                "FROM Supplier s " +
                "LEFT JOIN PurchaseOrder po ON s.supplier_id = po.supplier_id " +
                "LEFT JOIN FlowerBatch fb ON po.purchase_order_id = fb.purchase_order_id " +
                "GROUP BY s.supplier_id, s.name";
        return DBBase.query(sql);
    }

    public List<Object[]> nccDangCungCapHoa() {
        String sql = "SELECT DISTINCT s.supplier_id, s.name, s.phone, s.address, s.representative " +
                "FROM Supplier s JOIN Flower f ON s.supplier_id = f.supplier_id";
        return DBBase.query(sql);
    }

    public List<Object[]> nccNhieuLoaiHoaNhat() {
        String sql = "SELECT s.supplier_id, s.name, COUNT(f.flower_id) AS flower_count " +
                "FROM Supplier s JOIN Flower f ON s.supplier_id = f.supplier_id " +
                "GROUP BY s.supplier_id, s.name " +
                "ORDER BY flower_count DESC LIMIT 1";
        return DBBase.query(sql);
    }

    public List<Object[]> thongKeLoaiHoaTheoNCC() {
        String sql = "SELECT s.supplier_id, s.name, COUNT(f.flower_id) AS flower_count " +
                "FROM Supplier s LEFT JOIN Flower f ON s.supplier_id = f.supplier_id " +
                "GROUP BY s.supplier_id, s.name";
        return DBBase.query(sql);
    }

    // =====================================================
    // HELPER
    // =====================================================
    private Supplier mapRow(ResultSet rs) throws SQLException {
        Supplier s = new Supplier();
        s.setSupplierId(rs.getString("supplier_id"));
        s.setName(rs.getString("name"));
        s.setPhone(rs.getString("phone"));
        s.setAddress(rs.getString("address"));
        s.setRepresentative(rs.getString("representative"));
        return s;
    }
}