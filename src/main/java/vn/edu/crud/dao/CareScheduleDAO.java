package vn.edu.crud.dao;

import vn.edu.crud.util.DBBase;

import java.time.LocalDateTime;
import java.util.List;

public class CareScheduleDAO {

    // =====================================================
    // NHÓM CRUD
    // =====================================================

    public List<Object[]> findAll() {
        String sql = "SELECT care_id, flower_id, employee_id, check_date, " +
                "freshness_level, water_status, action_taken, note FROM CareSchedule";
        return DBBase.query(sql);
    }

    public List<Object[]> findById(String id) {
        String sql = "SELECT * FROM CareSchedule WHERE care_id = ?";
        return DBBase.query(sql, id);
    }

    public boolean insert(String id, String flowerId, String empId,
                          LocalDateTime date, String fresh, String water,
                          String action, String note) {
        String sql = "INSERT INTO CareSchedule " +
                "(care_id, flower_id, employee_id, check_date, freshness_level, " +
                "water_status, action_taken, note) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        return DBBase.update(sql, id, flowerId, empId, date, fresh,
                water, action, note) > 0;
    }

    public boolean update(String id, String fresh, String water,
                          String action, String note) {
        String sql = "UPDATE CareSchedule SET freshness_level=?, water_status=?, " +
                "action_taken=?, note=? WHERE care_id=?";
        return DBBase.update(sql, fresh, water, action, note, id) > 0;
    }

    public boolean delete(String id) {
        return DBBase.update(
                "DELETE FROM CareSchedule WHERE care_id = ?", id) > 0;
    }

    public String sinhMaCSMoi() {
        List<Object[]> rows = DBBase.query(
                "SELECT care_id FROM CareSchedule ORDER BY care_id DESC LIMIT 1");
        if (rows.isEmpty()) return "CS001";
        String maCu = rows.get(0)[0].toString();
        int so = Integer.parseInt(maCu.substring(2));
        return String.format("CS%03d", so + 1);
    }

    // =====================================================
    // NHÓM BÁO CÁO
    // =====================================================

    public List<Object[]> lichChamSocCuaNV(String maNV) {
        String sql = "SELECT cs.check_date, cs.freshness_level, " +
                "cs.water_status, cs.action_taken, f.name AS flower_name " +
                "FROM CareSchedule cs " +
                "JOIN Flower f ON cs.flower_id = f.flower_id " +
                "WHERE cs.employee_id = ?";
        return DBBase.query(sql, maNV);
    }
}