package vn.edu.crud.dao;

import vn.edu.crud.model.Customer;
import vn.edu.crud.util.DBBase;
import vn.edu.crud.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    // =====================================================
    // NHÓM CRUD
    // =====================================================

    public List<Customer> findAll() {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM Customer";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public Customer findById(String id) {
        String sql = "SELECT * FROM Customer WHERE customer_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public boolean insert(Customer c) {
        String sql = "INSERT INTO Customer (customer_id, name, phone, address, email) " +
                "VALUES (?, ?, ?, ?, ?)";
        return DBBase.update(sql,
                c.getCustomerId(), c.getName(), c.getPhone(),
                c.getAddress(), c.getEmail()) > 0;
    }

    public boolean update(Customer c) {
        String sql = "UPDATE Customer SET name=?, phone=?, address=?, email=? " +
                "WHERE customer_id=?";
        return DBBase.update(sql,
                c.getName(), c.getPhone(), c.getAddress(),
                c.getEmail(), c.getCustomerId()) > 0;
    }

    public boolean delete(String id) {
        return DBBase.update(
                "DELETE FROM Customer WHERE customer_id = ?", id) > 0;
    }

    public String sinhMaKhachMoi() {
        List<Object[]> rows = DBBase.query(
                "SELECT customer_id FROM Customer ORDER BY customer_id DESC LIMIT 1");
        if (rows.isEmpty()) return "KH001";
        String maCu = rows.get(0)[0].toString();
        int so = Integer.parseInt(maCu.substring(2));
        return String.format("KH%03d", so + 1);
    }

    // =====================================================
    // HELPER
    // =====================================================

    private Customer mapRow(ResultSet rs) throws SQLException {
        Customer c = new Customer();
        c.setCustomerId(rs.getString("customer_id"));
        c.setName(rs.getString("name"));
        c.setPhone(rs.getString("phone"));
        c.setAddress(rs.getString("address"));
        c.setEmail(rs.getString("email"));
        return c;
    }
}