package vn.edu.crud.dao;

import vn.edu.crud.util.DBBase;

import java.util.List;

public class SupplierDAO {

    // =====================================================
    // NHÓM CRUD
    // =====================================================

    public List<Object[]> findAll() {
        String sql = "SELECT supplier_id, name, phone, address, representative FROM Supplier";
        return DBBase.query(sql);
    }

    public List<Object[]> findById(String id) {
        String sql = "SELECT supplier_id, name, phone, address, representative " +
                "FROM Supplier WHERE supplier_id = ?";
        return DBBase.query(sql, id);
    }

    public boolean insert(String id, String name, String phone,
                          String address, String rep) {
        String sql = "INSERT INTO Supplier " +
                "(supplier_id, name, phone, address, representative) " +
                "VALUES (?, ?, ?, ?, ?)";
        return DBBase.update(sql, id, name, phone, address, rep) > 0;
    }

    public boolean update(String id, String name, String phone,
                          String address, String rep) {
        String sql = "UPDATE Supplier SET name=?, phone=?, address=?, representative=? " +
                "WHERE supplier_id=?";
        return DBBase.update(sql, name, phone, address, rep, id) > 0;
    }

    public boolean delete(String id) {
        return DBBase.update(
                "DELETE FROM Supplier WHERE supplier_id = ?", id) > 0;
    }

    public String sinhMaNCCMoi() {
        List<Object[]> rows = DBBase.query(
                "SELECT supplier_id FROM Supplier ORDER BY supplier_id DESC LIMIT 1");
        if (rows.isEmpty()) return "NCC01";
        String maCu = rows.get(0)[0].toString();
        int so = Integer.parseInt(maCu.substring(3));
        return String.format("NCC%02d", so + 1);
    }

    // =====================================================
    // NHÓM BÁO CÁO
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
}