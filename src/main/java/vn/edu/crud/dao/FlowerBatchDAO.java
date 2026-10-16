package vn.edu.crud.dao;

import vn.edu.crud.util.DBBase;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class FlowerBatchDAO {

    // =====================================================
    // NHÓM CRUD
    // =====================================================

    public List<Object[]> findAll() {
        String sql = "SELECT batch_id, purchase_order_id, flower_id, quantity_imported, " +
                "quantity_remaining, unit_cost, import_date, expiry_date, status, note " +
                "FROM FlowerBatch";
        return DBBase.query(sql);
    }

    public List<Object[]> findById(String id) {
        String sql = "SELECT * FROM FlowerBatch WHERE batch_id = ?";
        return DBBase.query(sql, id);
    }

    public boolean insert(String id, String poId, String flowerId,
                          int imported, int remaining, BigDecimal cost,
                          LocalDateTime importDate, LocalDate expiry,
                          String status, String note) {
        String sql = "INSERT INTO FlowerBatch " +
                "(batch_id, purchase_order_id, flower_id, quantity_imported, " +
                "quantity_remaining, unit_cost, import_date, expiry_date, status, note) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return DBBase.update(sql, id, poId, flowerId, imported, remaining,
                cost, importDate, expiry, status, note) > 0;
    }

    public boolean updateRemaining(String batchId, int remaining) {
        String sql = "UPDATE FlowerBatch SET quantity_remaining = ? WHERE batch_id = ?";
        return DBBase.update(sql, remaining, batchId) > 0;
    }

    public boolean updateStatus(String batchId, String status) {
        String sql = "UPDATE FlowerBatch SET status = ? WHERE batch_id = ?";
        return DBBase.update(sql, status, batchId) > 0;
    }

    public boolean delete(String id) {
        return DBBase.update(
                "DELETE FROM FlowerBatch WHERE batch_id = ?", id) > 0;
    }

    public String sinhMaLoMoi() {
        List<Object[]> rows = DBBase.query(
                "SELECT batch_id FROM FlowerBatch ORDER BY batch_id DESC LIMIT 1");
        if (rows.isEmpty()) return "L001";
        String maCu = rows.get(0)[0].toString();
        int so = Integer.parseInt(maCu.substring(1));
        return String.format("L%03d", so + 1);
    }

    // =====================================================
    // NHÓM BÁO CÁO
    // =====================================================

    public List<Object[]> loHoaConHang() {
        String sql = "SELECT fb.batch_id, f.name AS flower_name, " +
                "fb.quantity_remaining, fb.expiry_date, fb.status " +
                "FROM FlowerBatch fb " +
                "JOIN Flower f ON fb.flower_id = f.flower_id " +
                "WHERE fb.status = 'AVAILABLE' AND fb.quantity_remaining > 0 " +
                "ORDER BY fb.expiry_date";
        return DBBase.query(sql);
    }

    public List<Object[]> loHoaSapHetHan(int soNgay) {
        String sql = "SELECT fb.batch_id, f.name AS flower_name, " +
                "fb.quantity_remaining, fb.expiry_date, " +
                "DATEDIFF(fb.expiry_date, CURDATE()) AS days_left " +
                "FROM FlowerBatch fb " +
                "JOIN Flower f ON fb.flower_id = f.flower_id " +
                "WHERE fb.status = 'AVAILABLE' AND fb.quantity_remaining > 0 " +
                "AND DATEDIFF(fb.expiry_date, CURDATE()) <= ? " +
                "ORDER BY fb.expiry_date";
        return DBBase.query(sql, soNgay);
    }

    public List<Object[]> loHoaTheoLoai(String maHoa) {
        String sql = "SELECT batch_id, quantity_imported, quantity_remaining, " +
                "unit_cost, import_date, expiry_date, status " +
                "FROM FlowerBatch WHERE flower_id = ?";
        return DBBase.query(sql, maHoa);
    }
}