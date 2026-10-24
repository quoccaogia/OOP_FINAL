package vn.edu.crud.service;

import vn.edu.crud.dao.SupplierDAO;
import vn.edu.crud.model.Supplier;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Tầng Service xử lý nghiệp vụ Nhà cung cấp (Mục 2).
 * Đóng gói logic tìm kiếm bằng Lambda & Stream API.
 */
public class SupplierService {

    private final SupplierDAO supplierDAO;

    public SupplierService() {
        this.supplierDAO = new SupplierDAO();
    }

    public SupplierService(SupplierDAO supplierDAO) {
        this.supplierDAO = supplierDAO;
    }

    // =========================================================================
    // HÀM NGHIỆP VỤ SỬ DỤNG LAMBDA & STREAM API (MỤC 2)
    // =========================================================================

    /**
     * Tìm kiếm nhà cung cấp theo tên bằng Lambda.
     */
    public List<Supplier> searchSuppliersByLambda(String keyword) {
        List<Supplier> allSuppliers = supplierDAO.findAll();
        if (keyword == null || keyword.trim().isEmpty()) {
            return allSuppliers;
        }
        String lowerKeyword = keyword.toLowerCase().trim();

        // Sử dụng Stream API và biểu thức Lambda (s -> ...)
        return allSuppliers.stream()
                .filter(s -> s.getName() != null && s.getName().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
    }

    // =========================================================================
    // CÁC HÀM ĐIỀU PHỐI CRUD QUA DAO
    // =========================================================================

    public List<Supplier> findAll() {
        return supplierDAO.findAll();
    }

    public Supplier findById(String id) {
        return supplierDAO.findById(id);
    }

    public List<Supplier> timTheoSdt(String sdt) {
        return supplierDAO.timTheoSdt(sdt);
    }

    public boolean insert(Supplier s) {
        return supplierDAO.insert(s);
    }

    public boolean update(Supplier s) {
        return supplierDAO.update(s);
    }

    public boolean delete(String id) {
        return supplierDAO.delete(id);
    }

    public boolean coHoa(String supplierId) {
        return supplierDAO.coHoa(supplierId);
    }

    public String sinhMaNCCMoi() {
        return supplierDAO.sinhMaNCCMoi();
    }

    public String chuanHoaSdt(String sdt) {
        return supplierDAO.chuanHoaSdt(sdt);
    }

    public List<Object[]> tongNhapTheoNCC() {
        return supplierDAO.tongNhapTheoNCC();
    }

    public List<Object[]> nccDangCungCapHoa() {
        return supplierDAO.nccDangCungCapHoa();
    }

    public List<Object[]> nccNhieuLoaiHoaNhat() {
        return supplierDAO.nccNhieuLoaiHoaNhat();
    }

    public List<Object[]> thongKeLoaiHoaTheoNCC() {
        return supplierDAO.thongKeLoaiHoaTheoNCC();
    }
}
