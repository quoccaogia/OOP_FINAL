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

    // =====================================================
    // NHÓM XUẤT / NHẬP KHO THEO LÔ
    // Quy ước: flower.stock_quantity = tổng quantity_remaining của các lô AVAILABLE.
    // Mọi thao tác làm đổi số lượng của lô đều phải cập nhật flower.stock_quantity
    // tương ứng để 2 nơi không lệch nhau.
    // =====================================================

    /** Hoa này đã có lô hay chưa (hoa nhập tay thì không có lô). */
    public boolean coLo(String flowerId) {
        return !DBBase.query(
                "SELECT 1 FROM flowerbatch WHERE flower_id = ? LIMIT 1", flowerId).isEmpty();
    }

    /**
     * Xuất kho theo lô: trừ dần các lô AVAILABLE, lô hạn gần nhất trừ trước (FEFO).
     * Lô nào hết hàng thì chuyển SOLD_OUT.
     *
     * @return số lượng thực sự trừ được (0 nếu hoa không có lô;
     *         nhỏ hơn soLuong nếu các lô không đủ).
     */
    public int xuatKho(String flowerId, int soLuong) {
        if (soLuong <= 0) return 0;

        List<Object[]> dsLo = DBBase.query(
                "SELECT batch_id, quantity_remaining FROM flowerbatch " +
                        "WHERE flower_id = ? AND status = 'AVAILABLE' AND quantity_remaining > 0 " +
                        "ORDER BY expiry_date ASC, batch_id ASC",
                flowerId);

        int conLai = soLuong;
        for (Object[] lo : dsLo) {
            if (conLai <= 0) break;
            String batchId = (String) lo[0];
            int con = lo[1] == null ? 0 : ((Number) lo[1]).intValue();
            int lay = Math.min(conLai, con);
            int moi = con - lay;
            DBBase.update(
                    "UPDATE flowerbatch SET quantity_remaining = ?, status = ? WHERE batch_id = ?",
                    moi, moi == 0 ? "SOLD_OUT" : "AVAILABLE", batchId);
            conLai -= lay;
        }
        return soLuong - conLai;
    }

    /**
     * Nhập lại hàng đã xuất (xóa chi tiết / hủy đơn / giao thất bại):
     * cộng vào lô AVAILABLE có hạn xa nhất, nếu không có thì mở lại lô SOLD_OUT gần nhất.
     * Không đụng tới lô EXPIRED (hàng đã hỏng thì không bán lại).
     *
     * @return true nếu đã cộng được vào một lô.
     */
    public boolean nhapLaiKho(String flowerId, int soLuong) {
        if (soLuong <= 0) return false;

        List<Object[]> dsLo = DBBase.query(
                "SELECT batch_id FROM flowerbatch " +
                        "WHERE flower_id = ? AND status IN ('AVAILABLE', 'SOLD_OUT') " +
                        "ORDER BY (status = 'AVAILABLE') DESC, expiry_date DESC, batch_id DESC " +
                        "LIMIT 1",
                flowerId);
        if (dsLo.isEmpty()) return false;

        String batchId = (String) dsLo.get(0)[0];
        return DBBase.update(
                "UPDATE flowerbatch SET quantity_remaining = quantity_remaining + ?, status = 'AVAILABLE' " +
                        "WHERE batch_id = ?",
                soLuong, batchId) > 0;
    }

    /**
     * Đổi trạng thái lô và cập nhật tồn kho của hoa cho khớp:
     * rời AVAILABLE thì trừ tồn, quay lại AVAILABLE thì cộng tồn.
     */
    public boolean doiTrangThaiLo(String batchId, String trangThaiMoi) {
        List<Object[]> rows = DBBase.query(
                "SELECT flower_id, quantity_remaining, status FROM flowerbatch WHERE batch_id = ?",
                batchId);
        if (rows.isEmpty()) return false;

        String flowerId = (String) rows.get(0)[0];
        int con = rows.get(0)[1] == null ? 0 : ((Number) rows.get(0)[1]).intValue();
        String trangThaiCu = (String) rows.get(0)[2];

        boolean ok = DBBase.update(
                "UPDATE flowerbatch SET status = ? WHERE batch_id = ?",
                trangThaiMoi, batchId) > 0;
        if (!ok) return false;

        boolean cuAvailable = "AVAILABLE".equalsIgnoreCase(trangThaiCu);
        boolean moiAvailable = "AVAILABLE".equalsIgnoreCase(trangThaiMoi);
        int delta = 0;
        if (cuAvailable && !moiAvailable) delta = -con;
        else if (!cuAvailable && moiAvailable) delta = con;

        if (delta != 0 && flowerId != null) {
            DBBase.update(
                    "UPDATE flower SET stock_quantity = stock_quantity + ? WHERE flower_id = ?",
                    delta, flowerId);
        }
        return true;
    }

    /**
     * Thanh lý (hủy) lô: chuyển EXPIRED, đưa quantity_remaining về 0,
     * trừ đúng số đó khỏi tồn kho của hoa và ghi lại số lượng đã hủy vào note.
     */
    public boolean thanhLyLo(String batchId) {
        List<Object[]> rows = DBBase.query(
                "SELECT flower_id, quantity_remaining, status, note FROM flowerbatch WHERE batch_id = ?",
                batchId);
        if (rows.isEmpty()) return false;

        String flowerId = (String) rows.get(0)[0];
        int con = rows.get(0)[1] == null ? 0 : ((Number) rows.get(0)[1]).intValue();
        String trangThaiCu = (String) rows.get(0)[2];
        String noteCu = rows.get(0)[3] == null ? "" : rows.get(0)[3].toString();

        String noteMoi = noteCu + (noteCu.isEmpty() ? "" : " | ") + "Thanh lý " + con + " bông";
        boolean ok = DBBase.update(
                "UPDATE flowerbatch SET status = 'EXPIRED', quantity_remaining = 0, note = ? " +
                        "WHERE batch_id = ?",
                noteMoi, batchId) > 0;
        if (!ok) return false;

        if ("AVAILABLE".equalsIgnoreCase(trangThaiCu) && con > 0 && flowerId != null) {
            DBBase.update(
                    "UPDATE flower SET stock_quantity = stock_quantity - ? WHERE flower_id = ?",
                    con, flowerId);
        }
        return true;
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
                "AND DATEDIFF(fb.expiry_date, CURDATE()) BETWEEN 0 AND ? " +
                "ORDER BY fb.expiry_date";
        return DBBase.query(sql, soNgay);
    }
}