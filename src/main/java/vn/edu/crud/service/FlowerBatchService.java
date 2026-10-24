package vn.edu.crud.service;

import vn.edu.crud.dao.FlowerBatchDAO;
import vn.edu.crud.model.FlowerBatch;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Tầng Service xử lý nghiệp vụ Quản lý Lô hoa (Mục 9).
 * Đóng gói logic lọc lô còn hàng, quá 7 ngày và sắp hết hạn bằng Lambda & Stream API.
 */
public class FlowerBatchService {

    private final FlowerBatchDAO flowerBatchDAO;

    public FlowerBatchService() {
        this.flowerBatchDAO = new FlowerBatchDAO();
    }

    public FlowerBatchService(FlowerBatchDAO flowerBatchDAO) {
        this.flowerBatchDAO = flowerBatchDAO;
    }

    // =========================================================================
    // HÀM NGHIỆP VỤ SỬ DỤNG LAMBDA & STREAM API (MỤC 9)
    // =========================================================================

    /**
     * Lọc danh sách lô hoa còn hàng (AVAILABLE & quantity > 0) bằng Lambda.
     */
    public List<FlowerBatch> loConHangLambda() {
        return flowerBatchDAO.layDanhSachLo().stream()
                .filter(b -> "AVAILABLE".equals(b.getStatus())
                          && b.getQuantityRemaining() > 0)
                .collect(Collectors.toList());
    }

    /**
     * Lọc danh sách lô hoa nhập quá 7 ngày bằng Lambda & ChronoUnit.
     */
    public List<FlowerBatch> loNhapQua7NgayLambda() {
        return flowerBatchDAO.layDanhSachLo().stream()
                .filter(b -> b.getImportDate() != null
                          && b.getQuantityRemaining() > 0
                          && ChronoUnit.DAYS.between(b.getImportDate().toLocalDate(), LocalDate.now()) > 7)
                .collect(Collectors.toList());
    }

    /**
     * Lọc danh sách lô hoa sắp hết hạn trong số ngày chỉ định, sắp xếp tăng dần hạn sử dụng bằng Lambda.
     */
    public List<FlowerBatch> loSapHetHanLambda(int soNgay) {
        LocalDate homNay = LocalDate.now();
        return flowerBatchDAO.layDanhSachLo().stream()
                .filter(b -> "AVAILABLE".equals(b.getStatus())
                          && b.getQuantityRemaining() > 0
                          && b.getExpiryDate() != null
                          && ChronoUnit.DAYS.between(homNay, b.getExpiryDate()) >= 0
                          && ChronoUnit.DAYS.between(homNay, b.getExpiryDate()) <= soNgay)
                .sorted(Comparator.comparing(FlowerBatch::getExpiryDate))
                .collect(Collectors.toList());
    }

    // =========================================================================
    // CÁC HÀM ĐIỀU PHỐI CRUD QUA DAO
    // =========================================================================

    public List<Object[]> findAll() {
        return flowerBatchDAO.findAll();
    }

    public List<Object[]> findById(String id) {
        return flowerBatchDAO.findById(id);
    }

    public boolean insert(String id, String poId, String flowerId,
                          int imported, int remaining, BigDecimal cost,
                          LocalDateTime importDate, LocalDate expiry,
                          String status, String note) {
        return flowerBatchDAO.insert(id, poId, flowerId, imported, remaining, cost, importDate, expiry, status, note);
    }

    public boolean updateRemaining(String batchId, int remaining) {
        return flowerBatchDAO.updateRemaining(batchId, remaining);
    }

    public boolean updateStatus(String batchId, String status) {
        return flowerBatchDAO.updateStatus(batchId, status);
    }

    public boolean doiTrangThaiLo(String id, String status) {
        return flowerBatchDAO.doiTrangThaiLo(id, status);
    }

    public boolean thanhLyLo(String id) {
        return flowerBatchDAO.thanhLyLo(id);
    }

    public boolean coLo(String flowerId) {
        return flowerBatchDAO.coLo(flowerId);
    }

    public List<Object[]> loAvailableCuaHoa(String flowerId) {
        return flowerBatchDAO.loAvailableCuaHoa(flowerId);
    }

    public int xuatKho(String flowerId, int soLuong) {
        return flowerBatchDAO.xuatKho(flowerId, soLuong);
    }

    public boolean nhapLaiKho(String flowerId, int soLuong) {
        return flowerBatchDAO.nhapLaiKho(flowerId, soLuong);
    }

    public boolean delete(String id) {
        return flowerBatchDAO.delete(id);
    }

    public String sinhMaLoMoi() {
        return flowerBatchDAO.sinhMaLoMoi();
    }

    public List<Object[]> loHoaConHang() {
        return flowerBatchDAO.loHoaConHang();
    }

    public List<Object[]> loHoaSapHetHan(int soNgay) {
        return flowerBatchDAO.loHoaSapHetHan(soNgay);
    }

    public List<FlowerBatch> layDanhSachLo() {
        return flowerBatchDAO.layDanhSachLo();
    }
}
