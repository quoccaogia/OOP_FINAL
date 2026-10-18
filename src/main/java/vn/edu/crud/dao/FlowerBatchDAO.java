package vn.edu.crud.dao;

import vn.edu.crud.util.DBBase;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class FlowerBatchDAO {

    // Lấy tất cả kèm theo Tên NCC và Tên Hoa để in báo cáo
    public List<Object[]> findAll() {
        String sql = "SELECT fb.batch_id, fb.purchase_order_id, s.name AS supplier_name, " +
                "fb.flower_id, f.name AS flower_name, " +
                "fb.quantity_imported, fb.quantity_remaining, fb.unit_cost, " +
                "fb.import_date, fb.expiry_date, fb.status, fb.note " +
                "FROM flowerbatch fb " +
                "LEFT JOIN purchaseorder po ON fb.purchase_order_id = po.purchase_order_id " +
                "LEFT JOIN supplier s ON po.supplier_id = s.supplier_id " +
                "LEFT JOIN flower f ON fb.flower_id = f.flower_id " +
                "ORDER BY fb.batch_id DESC";
        return DBBase.query(sql);
    }

    public List<Object[]> findById(String id) {
        String sql = "SELECT * FROM flowerbatch WHERE batch_id = ?";
        return DBBase.query(sql, id);
    }

    public boolean insert(String id, String poId, String flowerId,
                          int imported, int remaining, BigDecimal cost,
                          LocalDateTime importDate, LocalDate expiry,
                          String status, String note) {
        String sql = "INSERT INTO flowerbatch " +
                "(batch_id, purchase_order_id, flower_id, quantity_imported, " +
                "quantity_remaining, unit_cost, import_date, expiry_date, status, note) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return DBBase.update(sql, id, poId, flowerId, imported, remaining,
                cost, importDate, expiry, status, note) > 0;
    }

    public boolean updateRemaining(String batchId, int remaining) {
        String sql = "UPDATE flowerbatch SET quantity_remaining = ? WHERE batch_id = ?";
        return DBBase.update(sql, remaining, batchId) > 0;
    }

    public boolean updateStatus(String batchId, String status) {
        String sql = "UPDATE flowerbatch SET status = ? WHERE batch_id = ?";
        return DBBase.update(sql, status, batchId) > 0;
    }

    public boolean delete(String id) {
        return DBBase.update("DELETE FROM flowerbatch WHERE batch_id = ?", id) > 0;
    }

    public String sinhMaLoMoi() {
        List<Object[]> rows = DBBase.query("SELECT batch_id FROM flowerbatch ORDER BY batch_id DESC LIMIT 1");
        if (rows.isEmpty()) return "L001";
        String maCu = rows.get(0)[0].toString();
        int so = Integer.parseInt(maCu.substring(1));
        return String.format("L%03d", so + 1);
    }

    // --- NHÓM BÁO CÁO ---

    public List<Object[]> loHoaConHang() {
        String sql = "SELECT fb.batch_id, f.name AS flower_name, s.name AS supplier_name, " +
                "fb.quantity_remaining, fb.expiry_date " +
                "FROM flowerbatch fb " +
                "JOIN flower f ON fb.flower_id = f.flower_id " +
                "JOIN purchaseorder po ON fb.purchase_order_id = po.purchase_order_id " +
                "JOIN supplier s ON po.supplier_id = s.supplier_id " +
                "WHERE fb.status = 'AVAILABLE' AND fb.quantity_remaining > 0 " +
                "ORDER BY fb.expiry_date";
        return DBBase.query(sql);
    }

    public List<Object[]> loHoaSapHetHan(int soNgay) {
        String sql = "SELECT fb.batch_id, f.name AS flower_name, " +
                "fb.quantity_remaining, fb.expiry_date, DATEDIFF(fb.expiry_date, CURDATE()) AS days_left " +
                "FROM flowerbatch fb JOIN flower f ON fb.flower_id = f.flower_id " +
                "WHERE fb.status = 'AVAILABLE' AND fb.quantity_remaining > 0 " +
                "AND DATEDIFF(fb.expiry_date, CURDATE()) <= ? " +
                "ORDER BY fb.expiry_date";
        return DBBase.query(sql, soNgay);
    }
}