package vn.edu.crud.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import vn.edu.crud.model.Employee;
import vn.edu.crud.util.DBBase;
import vn.edu.crud.util.DBConnection;

public class EmployeeDAO {


    @SuppressWarnings("CallToPrintStackTrace")
    public List<Employee> findAll() {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM Employee";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public Employee findById(String id) {
        String sql = "SELECT * FROM Employee WHERE employee_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public boolean insert(Employee e) {
        String sql = "INSERT INTO Employee (employee_id, name, role, phone, salary) VALUES (?, ?, ?, ?, ?)";
        return DBBase.update(sql, e.getEmployeeId(), e.getName(), e.getRole(), e.getPhone(), e.getSalary()) > 0;
    }

    public boolean update(Employee e) {
        String sql = "UPDATE Employee SET name=?, role=?, phone=?, salary=? WHERE employee_id=?";
        return DBBase.update(sql, e.getName(), e.getRole(), e.getPhone(), e.getSalary(), e.getEmployeeId()) > 0;
    }

    public boolean delete(String id) {
        return DBBase.update("DELETE FROM Employee WHERE employee_id = ?", id) > 0;
    }

    public String sinhMaNVMoi() {
        List<Object[]> rows = DBBase.query("SELECT employee_id FROM Employee ORDER BY employee_id DESC LIMIT 1");
        if (rows.isEmpty()) return "NV01";
        String maCu = rows.get(0)[0].toString();
        int so = Integer.parseInt(maCu.substring(2));
        return String.format("NV%02d", so + 1);
    }

    public boolean kiemTraNhanVienCoDon(String employeeId) {
        String sql = "SELECT 1 FROM `Order` WHERE employee_id = ? LIMIT 1";
        return !DBBase.query(sql, employeeId).isEmpty();
    }

    // Tính doanh thu nhân viên (để tính hoa hồng)
    public BigDecimal tinhDoanhThuNhanVien(String employeeId) {
        String sql = "SELECT COALESCE(SUM(od.quantity * od.unit_price_at_sale), 0) " +
                "FROM `Order` o JOIN OrderDetail od ON o.order_id = od.order_id " +
                "WHERE o.employee_id = ? AND o.order_status = 'HOAN_THANH'";
        List<Object[]> rows = DBBase.query(sql, employeeId);
        if (rows.isEmpty() || rows.get(0)[0] == null) return BigDecimal.ZERO;
        return (BigDecimal) rows.get(0)[0];
    }

    public List<Object[]> nhanVienCoDonHang() {
        String sql = "SELECT DISTINCT e.employee_id, e.name, e.role, e.phone, e.salary " +
                "FROM Employee e JOIN `Order` o ON e.employee_id = o.employee_id";
        return DBBase.query(sql);
    }

    public List<Object[]> nhanVienNhieuDonNhat() {
        String sql = "SELECT e.employee_id, e.name, COUNT(o.order_id) AS order_count " +
                "FROM Employee e JOIN `Order` o ON e.employee_id = o.employee_id " +
                "GROUP BY e.employee_id, e.name ORDER BY order_count DESC LIMIT 1";
        return DBBase.query(sql);
    }

    private Employee mapRow(ResultSet rs) throws SQLException {
        Employee e = new Employee();
        e.setEmployeeId(rs.getString("employee_id"));
        e.setName(rs.getString("name"));
        e.setRole(rs.getString("role"));
        e.setPhone(rs.getString("phone"));
        e.setSalary(rs.getBigDecimal("salary"));
        return e;
    }
}