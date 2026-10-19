package vn.edu.crud.dao;

import vn.edu.crud.util.DBBase;

import java.time.LocalDateTime;
import java.util.List;

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
        if (rows.isEmpty()) return "CS001";
        String maCu = rows.get(0)[0].toString();
        int so = Integer.parseInt(maCu.substring(2));
        return String.format("CS%03d", so + 1);
    }

    // Lịch chăm sóc của 1 nhân viên
    public List<Object[]> lichChamSocCuaNV(String maNV) {
        String sql = "SELECT cs.check_date, cs.freshness_level, cs.water_status, cs.action_taken, " +
                "f.name AS flower_name " +
                "FROM careschedule cs " +
                "LEFT JOIN flower f ON cs.flower_id = f.flower_id " +
                "WHERE cs.employee_id = ? " +
                "ORDER BY cs.check_date DESC";
        return DBBase.query(sql, maNV);
    }

    // Lịch sử chăm sóc của 1 Loài hoa cụ thể
    public List<Object[]> lichChamSocCuaHoa(String flowerId) {
        String sql = "SELECT cs.check_date, cs.freshness_level, cs.water_status, " +
                "cs.action_taken, cs.note, e.name AS employee_name " +
                "FROM careschedule cs " +
                "LEFT JOIN employee e ON cs.employee_id = e.employee_id " +
                "WHERE cs.flower_id = ? " +
                "ORDER BY cs.check_date DESC";
        return DBBase.query(sql, flowerId);
    }
}