package vn.edu.crud.service;

import vn.edu.crud.dao.CustomerDAO;
import vn.edu.crud.model.Customer;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Tầng Service xử lý nghiệp vụ Khách hàng (Mục 1).
 * Đóng gói toàn bộ logic tìm kiếm, lọc bằng Lambda & Stream API.
 */
public class CustomerService {

    private final CustomerDAO customerDAO;

    public CustomerService() {
        this.customerDAO = new CustomerDAO();
    }

    public CustomerService(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    // =========================================================================
    // HÀM NGHIỆP VỤ SỬ DỤNG LAMBDA & STREAM API (MỤC 1)
    // =========================================================================

    /**
     * Tìm kiếm và lọc khách hàng bằng Lambda & Stream API theo tên, SĐT hoặc email.
     */
    public List<Customer> filterCustomersByLambda(String keyword) {
        List<Customer> allCustomers = customerDAO.findAll();
        if (keyword == null || keyword.trim().isEmpty()) {
            return allCustomers;
        }
        String lowerKeyword = keyword.toLowerCase().trim();

        // Sử dụng Stream API và biểu thức Lambda (c -> ...) để lọc trực tiếp trên RAM
        return allCustomers.stream()
                .filter(c -> (c.getName() != null && c.getName().toLowerCase().contains(lowerKeyword)) ||
                             (c.getPhone() != null && c.getPhone().contains(lowerKeyword)) ||
                             (c.getEmail() != null && c.getEmail().toLowerCase().contains(lowerKeyword)))
                .collect(Collectors.toList());
    }

    // =========================================================================
    // CÁC HÀM ĐIỀU PHỐI CRUD QUA DAO
    // =========================================================================

    public List<Customer> findAll() {
        return customerDAO.findAll();
    }

    public Customer findById(String id) {
        return customerDAO.findById(id);
    }

    public List<Customer> timTheoSdt(String sdt) {
        return customerDAO.timTheoSdt(sdt);
    }

    public boolean insert(Customer c) {
        return customerDAO.insert(c);
    }

    public boolean update(Customer c) {
        return customerDAO.update(c);
    }

    public String sinhMaKhachMoi() {
        return customerDAO.sinhMaKhachMoi();
    }

    public String chuanHoaSdt(String sdt) {
        return customerDAO.chuanHoaSdt(sdt);
    }
}
