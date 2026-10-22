package vn.edu.crud.dao;

import vn.edu.crud.model.PurchaseOrder;
import vn.edu.crud.util.DBBase;
import vn.edu.crud.util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

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
        if (rows.isEmpty())
            return "PN001";
        String maCu = rows.get(0)[0].toString();
        int so = Integer.parseInt(maCu.substring(2));
        return String.format("PN%03d", so + 1);
    }

    // =====================================================
    // NHÓM XỬ LÝ PHIẾU NHẬP DỞ DANG
    // =====================================================

    // Các phiếu còn treo ở CHO_XU_LY (tạo xong nhưng chưa nhập hết / bị ngắt giữa
    // chừng)
    public List<Object[]> phieuChoXuLy() {
        String sql = "SELECT po.purchase_order_id, po.import_date, s.name, COUNT(fb.batch_id) " +
                "FROM PurchaseOrder po " +
                "LEFT JOIN Supplier s ON po.supplier_id = s.supplier_id " +
                "LEFT JOIN FlowerBatch fb ON po.purchase_order_id = fb.purchase_order_id " +
                "WHERE po.status = 'CHO_XU_LY' " +
                "GROUP BY po.purchase_order_id, po.import_date, s.name " +
                "ORDER BY po.import_date";
        return DBBase.query(sql);
    }

    // Số lô đã sinh từ 1 phiếu nhập
    public int demLoCuaPhieu(String purchaseOrderId) {
        String sql = "SELECT COUNT(*) FROM FlowerBatch WHERE purchase_order_id = ?";
        List<Object[]> rows = DBBase.query(sql, purchaseOrderId);
        if (rows.isEmpty() || rows.get(0)[0] == null)
            return 0;
        return ((Number) rows.get(0)[0]).intValue();
    }

    /**
     * Chốt 1 phiếu nhập dở dang: tính lại tổng tiền từ các lô đã nhập
     * rồi chuyển sang HOAN_THANH. Không làm gì nếu phiếu chưa có lô nào.
     */
    public boolean chotPhieu(String purchaseOrderId) {
        List<Object[]> rows = DBBase.query(
                "SELECT COALESCE(SUM(quantity_imported * unit_cost), 0) " +
                        "FROM FlowerBatch WHERE purchase_order_id = ?",
                purchaseOrderId);
        if (rows.isEmpty() || rows.get(0)[0] == null)
            return false;

        BigDecimal tong = new BigDecimal(rows.get(0)[0].toString());
        if (tong.compareTo(BigDecimal.ZERO) <= 0)
            return false;

        String sql = "UPDATE PurchaseOrder SET total_amount = ?, status = 'HOAN_THANH', " +
                "note = CONCAT(COALESCE(note, ''), ' | Chốt bổ sung') " +
                "WHERE purchase_order_id = ? AND status = 'CHO_XU_LY'";
        return DBBase.update(sql, tong, purchaseOrderId) > 0;
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

    // Lấy tất cả phiếu nhập dưới dạng đối tượng PurchaseOrder
    public List<PurchaseOrder> layDanhSachPhieuNhap() {
        List<PurchaseOrder> list = new ArrayList<>();
        String sql = "SELECT * FROM purchaseorder";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                list.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<PurchaseOrder> timPhieuTheoMaLambda(String maPN) {
        return layDanhSachPhieuNhap().stream()
                .filter(po -> maPN.equals(po.getPurchaseOrderId()))
                .collect(Collectors.toList());
    }

    public List<PurchaseOrder> phieuCuaNCCLambda(String maNCC) {
        return layDanhSachPhieuNhap().stream()
                .filter(po -> maNCC.equals(po.getSupplierId()))
                .collect(Collectors.toList());
    }

    public Map<String, BigDecimal> tongNhapTheoNCCLambda() {
        return layDanhSachPhieuNhap().stream()
                .filter(po -> po.getSupplierId() != null && po.getTotalAmount() != null)
                .collect(Collectors.groupingBy(
                        PurchaseOrder::getSupplierId,
                        TreeMap::new,
                        Collectors.reducing(BigDecimal.ZERO, PurchaseOrder::getTotalAmount, BigDecimal::add)));
    }

    private PurchaseOrder mapRow(ResultSet rs) throws SQLException {
        PurchaseOrder po = new PurchaseOrder();
        po.setPurchaseOrderId(rs.getString("purchase_order_id"));
        po.setSupplierId(rs.getString("supplier_id"));
        po.setEmployeeId(rs.getString("employee_id"));
        po.setImportDate(rs.getObject("import_date", LocalDateTime.class));
        po.setTotalAmount(rs.getBigDecimal("total_amount"));
        po.setStatus(rs.getString("status"));
        po.setNote(rs.getString("note"));
        return po;
    }
}