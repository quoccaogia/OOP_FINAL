package vn.edu.crud.service;

import vn.edu.crud.dao.FlowerDAO;
import vn.edu.crud.model.Flower;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Tầng Service xử lý nghiệp vụ Quản lý Hoa (Mục 4).
 * Đóng gói toàn bộ các hàm lọc, sắp xếp, tìm kiếm và tính tổng tồn kho bằng Lambda & Stream API.
 */
public class FlowerService {

    private final FlowerDAO flowerDAO;

    public FlowerService() {
        this.flowerDAO = new FlowerDAO();
    }

    public FlowerService(FlowerDAO flowerDAO) {
        this.flowerDAO = flowerDAO;
    }

    // =========================================================================
    // HÀM NGHIỆP VỤ SỬ DỤNG LAMBDA & STREAM API (MỤC 4)
    // =========================================================================

    // 1. Tìm kiếm hoa theo Tên hoặc Danh mục bằng Lambda
    public List<Flower> searchFlowersByLambda(String keyword) {
        List<Flower> allFlowers = flowerDAO.findAll();
        if (keyword == null || keyword.trim().isEmpty()) {
            return allFlowers;
        }
        String tuKhoa = keyword.toLowerCase().trim();

        return allFlowers.stream()
                .filter(f -> (f.getName() != null && f.getName().toLowerCase().contains(tuKhoa)) ||
                             (f.getCategory() != null && f.getCategory().toLowerCase().contains(tuKhoa)))
                .collect(Collectors.toList());
    }

    // 2. Lọc danh sách hoa sắp hết hàng (tồn kho < ngưỡng) bằng Lambda
    public List<Flower> hoaSapHetLambda(int nguong) {
        return flowerDAO.findAll().stream()
                .filter(f -> f.getStockQuantity() < nguong)
                .collect(Collectors.toList());
    }

    // 3. Lọc hoa theo khoảng giá bằng Lambda
    public List<Flower> timTheoKhoangGiaLambda(BigDecimal giaMin, BigDecimal giaMax) {
        return flowerDAO.findAll().stream()
                .filter(f -> f.getUnitPrice() != null
                          && f.getUnitPrice().compareTo(giaMin) >= 0
                          && f.getUnitPrice().compareTo(giaMax) <= 0)
                .collect(Collectors.toList());
    }

    // 4. Sắp xếp danh sách hoa theo giá giảm dần bằng Lambda
    public List<Flower> hoaGiaGiamDanLambda() {
        return flowerDAO.findAll().stream()
                .filter(f -> f.getUnitPrice() != null)
                .sorted((f1, f2) -> f2.getUnitPrice().compareTo(f1.getUnitPrice()))
                .collect(Collectors.toList());
    }

    // 5. Tính tổng số lượng tồn kho của tất cả hoa bằng Lambda & Stream API
    public int tinhTongTonKhoLambda() {
        return tinhTongTonKhoLambda(flowerDAO.findAll());
    }

    public int tinhTongTonKhoLambda(List<Flower> flowerList) {
        if (flowerList == null || flowerList.isEmpty()) {
            return 0;
        }
        return flowerList.stream()
                .mapToInt(Flower::getStockQuantity)
                .sum();
    }

    // =========================================================================
    // CÁC HÀM ĐIỀU PHỐI CRUD QUA DAO
    // =========================================================================

    public List<Flower> findAll() {
        return flowerDAO.findAll();
    }

    public Flower timTheoMa(String maHoa) {
        return flowerDAO.timTheoMa(maHoa);
    }

    public boolean insert(String id, String name, String cat, BigDecimal price,
                          int stock, String color, String supId) {
        return flowerDAO.insert(id, name, cat, price, stock, color, supId);
    }

    public boolean update(String id, String name, String cat, BigDecimal price,
                          int stock, String color, String supId) {
        return flowerDAO.update(id, name, cat, price, stock, color, supId);
    }

    public boolean delete(String id) {
        return flowerDAO.delete(id);
    }

    public String sinhMaHoaMoi() {
        return flowerDAO.sinhMaHoaMoi();
    }

    public boolean congTonKho(String flowerId, int soLuong) {
        return flowerDAO.congTonKho(flowerId, soLuong);
    }

    public int dongBoTonKhoTuLo() {
        return flowerDAO.dongBoTonKhoTuLo();
    }

    // Lọc hoa theo màu sắc chủ đạo bằng Lambda & Stream API (khớp Câu 08 trong Báo cáo)
    public List<Flower> timTheoMau(String mau) {
        List<Flower> allFlowers = flowerDAO.findAll();
        if (mau == null || mau.trim().isEmpty()) {
            return allFlowers;
        }
        String mauCanTim = mau.trim();
        return allFlowers.stream()
                .filter(f -> f.getColor() != null && f.getColor().equalsIgnoreCase(mauCanTim))
                .collect(Collectors.toList());
    }

    public List<Object[]> hoaBanChay() {
        return flowerDAO.hoaBanChay();
    }

    public List<Object[]> hoaBanCham() {
        return flowerDAO.hoaBanCham();
    }

    public List<Object[]> tongBanTheoHoa() {
        return flowerDAO.tongBanTheoHoa();
    }

    public List<Flower> hoaChuaBanDuoc() {
        return flowerDAO.hoaChuaBanDuoc();
    }

    public List<Object[]> loHoaTonLau(int soNgay) {
        return flowerDAO.loHoaTonLau(soNgay);
    }
}
