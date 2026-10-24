package vn.edu.crud.service;

import vn.edu.crud.dao.EmployeeDAO;
import vn.edu.crud.model.Employee;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Tầng Service xử lý nghiệp vụ Nhân viên (Mục 3).
 * Đóng gói logic tìm kiếm nhân viên bằng Lambda & Stream API.
 */
public class EmployeeService {

    private final EmployeeDAO employeeDAO;

    public EmployeeService() {
        this.employeeDAO = new EmployeeDAO();
    }

    public EmployeeService(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    // =========================================================================
    // HÀM NGHIỆP VỤ SỬ DỤNG LAMBDA & STREAM API (MỤC 3)
    // =========================================================================

    /**
     * Tìm kiếm nhân viên theo Mã nhân viên hoặc Tên nhân viên bằng Lambda.
     */
    public List<Employee> searchEmployeesByLambda(String keyword) {
        List<Employee> allEmployees = employeeDAO.findAll();
        if (keyword == null || keyword.trim().isEmpty()) {
            return allEmployees;
        }
        String lowerKeyword = keyword.toLowerCase().trim();

        // Sử dụng Stream API và biểu thức Lambda (e -> ...)
        return allEmployees.stream()
                .filter(e -> (e.getEmployeeId() != null && e.getEmployeeId().toLowerCase().contains(lowerKeyword)) ||
                             (e.getName() != null && e.getName().toLowerCase().contains(lowerKeyword)))
                .collect(Collectors.toList());
    }

    // =========================================================================
    // CÁC HÀM ĐIỀU PHỐI CRUD QUA DAO
    // =========================================================================

    public List<Employee> findAll() {
        return employeeDAO.findAll();
    }

    public Employee findById(String id) {
        return employeeDAO.findById(id);
    }

    public boolean insert(Employee e) {
        return employeeDAO.insert(e);
    }

    public boolean update(Employee e) {
        return employeeDAO.update(e);
    }

    public boolean delete(String id) {
        return employeeDAO.delete(id);
    }

    public String sinhMaNVMoi() {
        return employeeDAO.sinhMaNVMoi();
    }

    public boolean kiemTraNhanVienCoDon(String employeeId) {
        return employeeDAO.kiemTraNhanVienCoDon(employeeId);
    }

    public BigDecimal tinhDoanhThuNhanVien(String employeeId) {
        return employeeDAO.tinhDoanhThuNhanVien(employeeId);
    }

    public List<Object[]> nhanVienCoDonHang() {
        return employeeDAO.nhanVienCoDonHang();
    }

    public List<Object[]> nhanVienNhieuDonNhat() {
        return employeeDAO.nhanVienNhieuDonNhat();
    }
}
