package vn.edu.crud.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import vn.edu.crud.model.Customer;
import vn.edu.crud.util.DBBase;
import vn.edu.crud.util.DBConnection;

public class CustomerDAO {

    @SuppressWarnings({"CallToStackTrace", "CallToPrintStackTrace"})
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

    // Hàm này cực kỳ quan trọng để OrderMenu lấy địa chỉ khách hàng
    @SuppressWarnings({"CallToStackTrace", "CallToPrintStackTrace"})
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

    // Tìm theo SĐT (Dùng cho nghiệp vụ tiếp đón)
    @SuppressWarnings({"CallToStackTrace", "CallToPrintStackTrace"})
    public List<Customer> timTheoSdt(String sdt) {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM Customer WHERE phone LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + sdt + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // =========================================================================
    // HÀM SỬ DỤNG BIỂU THỨC LAMBDA & STREAM API CHO MỤC 1
    // =========================================================================
    @SuppressWarnings("CallToStackTrace")
    public List<Customer> filterCustomersByLambda(String keyword) {
        List<Customer> allCustomers = this.findAll();
        if (keyword == null || keyword.trim().isEmpty()) {
            return allCustomers;
        }
        String lowerKeyword = keyword.toLowerCase().trim();
        
        // Sử dụng Stream API và Biểu thức Lambda để lọc dữ liệu trực tiếp trong bộ nhớ
        return allCustomers.stream()
                .filter(c -> (c.getName() != null && c.getName().toLowerCase().contains(lowerKeyword)) ||
                             (c.getPhone() != null && c.getPhone().contains(lowerKeyword)) ||
                             (c.getEmail() != null && c.getEmail().toLowerCase().contains(lowerKeyword)))
                .collect(Collectors.toList());
    }

    @SuppressWarnings({"CallToStackTrace", "CallToPrintStackTrace"})
    public boolean insert(Customer c) {
        String sql = "INSERT INTO Customer (customer_id, name, phone, address, email) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getCustomerId());
            ps.setString(2, c.getName());
            ps.setString(3, c.getPhone());
            ps.setString(4, c.getAddress());
            ps.setString(5, c.getEmail());
            return ps.executeUpdate() > 0;
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("⚠️ LỖI: Số điện thoại [" + c.getPhone() + "] đã được dùng cho khách hàng khác!");
            return false;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @SuppressWarnings({"CallToStackTrace", "CallToPrintStackTrace"})
    public boolean update(Customer c) {
        String sql = "UPDATE Customer SET name=?, phone=?, address=?, email=? WHERE customer_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getPhone());
            ps.setString(3, c.getAddress());
            ps.setString(4, c.getEmail());
            ps.setString(5, c.getCustomerId());
            return ps.executeUpdate() > 0;
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("⚠️ LỖI: Số điện thoại [" + c.getPhone() + "] đã bị trùng với người khác!");
            return false;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public String sinhMaKhachMoi() {
        List<Object[]> rows = DBBase.query("SELECT customer_id FROM Customer ORDER BY customer_id DESC LIMIT 1");
        if (rows.isEmpty()) return "KH001";
        String maCu = rows.get(0)[0].toString();
        int so = Integer.parseInt(maCu.substring(2));
        return String.format("KH%03d", so + 1);
    }

    public String chuanHoaSdt(String sdt) {
        if (sdt == null || sdt.trim().isEmpty()) return "";
        String s = sdt.trim().replaceAll("\\s+", "");
        if (s.startsWith("+84")) s = "0" + s.substring(3);
        s = s.replaceFirst("^0+", "0");
        return s;
    }

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