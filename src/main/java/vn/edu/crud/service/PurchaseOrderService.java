package vn.edu.crud.service;

import vn.edu.crud.dao.PurchaseOrderDAO;
import vn.edu.crud.model.PurchaseOrder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Tầng Service xử lý nghiệp vụ Phiếu nhập hàng (Mục 8).
 * Đóng gói logic tìm kiếm và tổng hợp tiền nhập theo NCC bằng Lambda & Stream API.
 */
public class PurchaseOrderService {

    private final PurchaseOrderDAO purchaseOrderDAO;

    public PurchaseOrderService() {
        this.purchaseOrderDAO = new PurchaseOrderDAO();
    }

    public PurchaseOrderService(PurchaseOrderDAO purchaseOrderDAO) {
        this.purchaseOrderDAO = purchaseOrderDAO;
    }

    // =========================================================================
    // HÀM NGHIỆP VỤ SỬ DỤNG LAMBDA & STREAM API (MỤC 8)
    // =========================================================================

    /**
     * Tìm phiếu nhập theo mã phiếu bằng Lambda.
     */
    public List<PurchaseOrder> timPhieuTheoMaLambda(String maPN) {
        return purchaseOrderDAO.layDanhSachPhieuNhap().stream()
                .filter(po -> maPN.equals(po.getPurchaseOrderId()))
                .collect(Collectors.toList());
    }

    /**
     * Lọc phiếu nhập của 1 nhà cung cấp bằng Lambda.
     */
    public List<PurchaseOrder> phieuCuaNCCLambda(String maNCC) {
        return purchaseOrderDAO.layDanhSachPhieuNhap().stream()
                .filter(po -> maNCC.equals(po.getSupplierId()))
                .collect(Collectors.toList());
    }

    /**
     * Gom nhóm và tính tổng tiền nhập theo từng NCC bằng groupingBy & reducing.
     */
    public Map<String, BigDecimal> tongNhapTheoNCCLambda() {
        return purchaseOrderDAO.layDanhSachPhieuNhap().stream()
                .filter(po -> po.getSupplierId() != null && po.getTotalAmount() != null)
                .collect(Collectors.groupingBy(
                        PurchaseOrder::getSupplierId,
                        TreeMap::new,
                        Collectors.reducing(BigDecimal.ZERO, PurchaseOrder::getTotalAmount, BigDecimal::add)));
    }

    // =========================================================================
    // CÁC HÀM ĐIỀU PHỐI CRUD QUA DAO
    // =========================================================================

    public List<Object[]> findAll() {
        return purchaseOrderDAO.findAll();
    }

    public List<Object[]> tatCaPhieuNhap() {
        return purchaseOrderDAO.tatCaPhieuNhap();
    }

    public String sinhMaPNMoi() {
        return purchaseOrderDAO.sinhMaPNMoi();
    }

    public boolean insert(String id, String supId, String empId, LocalDateTime date,
                          BigDecimal total, String status, String note) {
        return purchaseOrderDAO.insert(id, supId, empId, date, total, status, note);
    }

    public boolean update(String id, BigDecimal total, String status, String note) {
        return purchaseOrderDAO.update(id, total, status, note);
    }

    public boolean delete(String id) {
        return purchaseOrderDAO.delete(id);
    }

    public List<Object[]> findById(String id) {
        return purchaseOrderDAO.findById(id);
    }

    public int demLoCuaPhieu(String poId) {
        return purchaseOrderDAO.demLoCuaPhieu(poId);
    }

    public List<Object[]> phieuChoXuLy() {
        return purchaseOrderDAO.phieuChoXuLy();
    }

    public boolean chotPhieu(String poId) {
        return purchaseOrderDAO.chotPhieu(poId);
    }

    public List<Object[]> phieuNhapCuaNCC(String supplierId) {
        return purchaseOrderDAO.phieuNhapCuaNCC(supplierId);
    }

    public List<PurchaseOrder> layDanhSachPhieuNhap() {
        return purchaseOrderDAO.layDanhSachPhieuNhap();
    }
}
