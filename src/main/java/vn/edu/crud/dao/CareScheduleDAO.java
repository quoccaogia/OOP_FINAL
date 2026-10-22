package vn.edu.crud.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

import vn.edu.crud.model.CareSchedule;
import vn.edu.crud.util.DBBase;
import vn.edu.crud.util.DBConnection;

public class CareScheduleDAO {

    // Lấy tất cả lịch sử chăm sóc (JOIN với bảng Flower)
    public List<Object[]> findAll() {
        String sql = "SELECT cs.care_id, cs.flower_id, f.name AS flower_name, e.name AS employee_name, " +
                "cs.check_date, cs.freshness_level, cs.water_status, cs.action_taken, cs.note " +
                "FROM careschedule cs " +
                "LEFT JOIN flower f ON cs.flower_id = f.flower_id " +
                "LEFT JOIN employee e ON cs.employee_id = e.employee_id " +
                "ORDER BY cs.check_date DESC";
        return DBBase.query(sql);
    }

    /**
     * Bảng tình trạng chăm sóc: TẤT CẢ loài hoa + bản ghi chăm sóc MỚI NHẤT của mỗi
     * loài.
     * Hoa chưa từng chăm vẫn xuất hiện (các cột chăm sóc = null).
     *
     * Cột trả về:
     * 0 flower_id | 1 name | 2 color | 3 stock_quantity
     * 4 care_id | 5 check_date | 6 freshness_level | 7 water_status | 8 note
     */
    public List<Object[]> trangThaiHienTai() {
        String sql = "SELECT f.flower_id, f.name, f.color, f.stock_quantity, " +
                "       cs.care_id, cs.check_date, cs.freshness_level, cs.water_status, cs.note " +
                "FROM flower f " +
                "LEFT JOIN careschedule cs ON cs.care_id = (" +
                "    SELECT cs2.care_id FROM careschedule cs2 " +
                "    WHERE cs2.flower_id = f.flower_id " +
                "    ORDER BY cs2.check_date DESC, cs2.care_id DESC LIMIT 1) " +
                "ORDER BY f.flower_id";
        return DBBase.query(sql);
    }

    // Sửa riêng phần ghi chú của 1 bản ghi (không đổi tình trạng đã ghi nhận)
    public boolean capNhatGhiChu(String careId, String note) {
        return DBBase.update("UPDATE careschedule SET note = ? WHERE care_id = ?",
                note, careId) > 0;
    }

    public List<Object[]> findById(String id) {
        String sql = "SELECT * FROM careschedule WHERE care_id = ?";
        return DBBase.query(sql, id);
    }

    // Sửa lại tham số thành flowerId
    public boolean insert(String id, String flowerId, String empId,
            LocalDateTime date, String fresh, String water,
            String action, String note) {
        String sql = "INSERT INTO careschedule " +
                "(care_id, flower_id, employee_id, check_date, freshness_level, " +
                "water_status, action_taken, note) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        return DBBase.update(sql, id, flowerId, empId, date, fresh, water, action, note) > 0;
    }

    public boolean update(String id, String fresh, String water, String action, String note) {
        String sql = "UPDATE careschedule SET freshness_level=?, water_status=?, " +
                "action_taken=?, note=? WHERE care_id=?";
        return DBBase.update(sql, fresh, water, action, note, id) > 0;
    }

    public boolean delete(String id) {
        return DBBase.update("DELETE FROM careschedule WHERE care_id = ?", id) > 0;
    }

    public String sinhMaCSMoi() {
        List<Object[]> rows = DBBase.query("SELECT care_id FROM careschedule ORDER BY care_id DESC LIMIT 1");
        if (rows.isEmpty())
            return "CS001";
        String maCu = rows.get(0)[0].toString();
        int so = Integer.parseInt(maCu.substring(2));
        return String.format("CS%03d", so + 1);
    }

    // Lịch chăm sóc của 1 nhân viên
    public List<Object[]> lichChamSocCuaNV(String maNV) {
        String sql = "SELECT cs.care_id, cs.check_date, cs.freshness_level, cs.water_status, " +
                "cs.action_taken, f.name AS flower_name " +
                "FROM careschedule cs " +
                "LEFT JOIN flower f ON cs.flower_id = f.flower_id " +
                "WHERE cs.employee_id = ? " +
                "ORDER BY cs.check_date DESC";
        return DBBase.query(sql, maNV);
    }

    // Lịch sử chăm sóc của 1 Loài hoa cụ thể
    public List<Object[]> lichChamSocCuaHoa(String flowerId) {
        String sql = "SELECT cs.care_id, cs.check_date, cs.freshness_level, cs.water_status, " +
                "cs.action_taken, cs.note, e.name AS employee_name " +
                "FROM careschedule cs " +
                "LEFT JOIN employee e ON cs.employee_id = e.employee_id " +
                "WHERE cs.flower_id = ? " +
                "ORDER BY cs.check_date DESC";
        return DBBase.query(sql, flowerId);
    }

    // Lấy tất cả bản ghi chăm sóc dưới dạng đối tượng CareSchedule 
    @SuppressWarnings("CallToPrintStackTrace")
    public List<CareSchedule> layDanhSachChamSoc() {
        List<CareSchedule> list = new ArrayList<>();
        String sql = "SELECT * FROM careschedule";
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

    public List<CareSchedule> lichSuTheoHoaLambda(String flowerId){
        return layDanhSachChamSoc().stream()
                .filter(c -> flowerId.equals(c.getFlowerId()))
                .sorted(Comparator.comparing(CareSchedule::getCheckDate,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
    }

    public List<CareSchedule> lichSuTheoNhanVienLambda(String empId){
        return layDanhSachChamSoc().stream()
                .filter(c -> empId.equals(c.getEmployeeId()))
                .sorted(Comparator.comparing(CareSchedule::getCheckDate,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());

    }

    private CareSchedule mapRow(ResultSet rs) throws SQLException {
        CareSchedule c = new CareSchedule();
        c.setCareId(rs.getString("care_id"));
        c.setFlowerId(rs.getString("flower_id"));
        c.setEmployeeId(rs.getString("employee_id"));
        c.setCheckDate(rs.getObject("check_date", LocalDateTime.class));
        c.setFreshnessLevel(rs.getString("freshness_level"));
        c.setWaterStatus(rs.getString("water_status"));
        c.setActionTaken(rs.getString("action_taken"));
        c.setNote(rs.getString("note"));
        return c;
    }
}