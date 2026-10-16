package vn.edu.crud.dao;

import vn.edu.crud.util.DBBase;

import java.math.BigDecimal;
import java.util.List;

public class EmployeeDAO {

    // =====================================================
    // NHÓM CRUD
    // =====================================================

    public List<Object[]> findAll() {
        String sql = "SELECT employee_id, name, role, phone, salary FROM Employee";
        return DBBase.query(sql);
    }

    public List<Object[]> findById(String id) {
        String sql = "SELECT employee_id, name, role, phone, salary " +
                "FROM Employee WHERE employee_id = ?";
        return DBBase.query(sql, id);
    }

    public boolean insert(String id, String name, String role,
                          String phone, BigDecimal salary) {
        String sql = "INSERT INTO Employee (employee_id, name, role, phone, salary) " +
                "VALUES (?, ?, ?, ?, ?)";
        return DBBase.update(sql, id, name, role, phone, salary) > 0;
    }

    public boolean update(String id, String name, String role,
                          String phone, BigDecimal salary) {
        String sql = "UPDATE Employee SET name=?, role=?, phone=?, salary=? " +
                "WHERE employee_id=?";
        return DBBase.update(sql, name, role, phone, salary, id) > 0;
    }

    public boolean delete(String id) {
        return DBBase.update(
                "DELETE FROM Employee WHERE employee_id = ?", id) > 0;
    }

    public String sinhMaNVMoi() {
        List<Object[]> rows = DBBase.query(
                "SELECT employee_id FROM Employee ORDER BY employee_id DESC LIMIT 1");
        if (rows.isEmpty()) return "NV01";
        String maCu = rows.get(0)[0].toString();
        int so = Integer.parseInt(maCu.substring(2));
        return String.format("NV%02d", so + 1);
    }

    // =====================================================
    // NHÓM BÁO CÁO
    // =====================================================

    public List<Object[]> nhanVienCoDonHang() {
        String sql = "SELECT DISTINCT e.employee_id, e.name, e.role, e.phone, e.salary " +
                "FROM Employee e JOIN `Order` o ON e.employee_id = o.employee_id";
        return DBBase.query(sql);
    }

    public List<Object[]> nhanVienNhieuDonNhat() {
        String sql = "SELECT e.employee_id, e.name, COUNT(o.order_id) AS order_count " +
                "FROM Employee e JOIN `Order` o ON e.employee_id = o.employee_id " +
                "GROUP BY e.employee_id, e.name " +
                "ORDER BY order_count DESC LIMIT 1";
        return DBBase.query(sql);
    }
}