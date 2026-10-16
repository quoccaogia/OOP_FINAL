package vn.edu.crud.dao;

import vn.edu.crud.util.DBBase;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PurchaseOrderDAO {

    // =====================================================
    // NHÓM CRUD
    // =====================================================

    public List<Object[]> findAll() {
        String sql = "SELECT purchase_order_id, supplier_id, employee_id, " +
                "import_date, total_amount, status, note FROM PurchaseOrder";
        return DBBase.query(sql);
    }

    public List<Object[]> findById(String id) {
        String sql = "SELECT * FROM PurchaseOrder WHERE purchase_order_id = ?";
        return DBBase.query(sql, id);
    }

    public boolean insert(String id, String supId, String empId,
                          LocalDateTime date, BigDecimal total,
                          String status, String note) {
        String sql = "INSERT INTO PurchaseOrder " +
                "(purchase_order_id, supplier_id, employee_id, import_date, " +
                "total_amount, status, note) VALUES (?, ?, ?, ?, ?, ?, ?)";
        return DBBase.update(sql, id, supId, empId, date, total, status, note) > 0;
    }

    public boolean update(String id, BigDecimal total, String status, String note) {
        String sql = "UPDATE PurchaseOrder SET total_amount=?, status=?, note=? " +
                "WHERE purchase_order_id=?";
        return DBBase.update(sql, total, status, note, id) > 0;
    }

    public boolean delete(String id) {
        return DBBase.update(
                "DELETE FROM PurchaseOrder WHERE purchase_order_id = ?", id) > 0;
    }

    public String sinhMaPNMoi() {
        List<Object[]> rows = DBBase.query(
                "SELECT purchase_order_id FROM PurchaseOrder " +
                        "ORDER BY purchase_order_id DESC LIMIT 1");
        if (rows.isEmpty()) return "PN001";
        String maCu = rows.get(0)[0].toString();
        int so = Integer.parseInt(maCu.substring(2));
        return String.format("PN%03d", so + 1);
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

    public List<Object[]> phieuNhapCuaNCC(String maNCC) {
        String sql = "SELECT purchase_order_id, import_date, total_amount, status, note " +
                "FROM PurchaseOrder WHERE supplier_id = ?";
        return DBBase.query(sql, maNCC);
    }

    public List<Object[]> tatCaPhieuNhap() {
        String sql = "SELECT po.purchase_order_id, s.name AS supplier_name, " +
                "e.name AS employee_name, po.import_date, po.total_amount, " +
                "po.status, po.note " +
                "FROM PurchaseOrder po " +
                "JOIN Supplier s ON po.supplier_id = s.supplier_id " +
                "JOIN Employee e ON po.employee_id = e.employee_id " +
                "ORDER BY po.import_date DESC";
        return DBBase.query(sql);
    }
}