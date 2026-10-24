package vn.edu.crud.service;

import vn.edu.crud.dao.CareScheduleDAO;
import vn.edu.crud.model.CareSchedule;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Tầng Service xử lý nghiệp vụ Lịch chăm sóc hoa (Mục 7).
 * Đóng gói logic lọc và sắp xếp lịch sử chăm sóc bằng Lambda & Stream API.
 */
public class CareScheduleService {

    private final CareScheduleDAO careScheduleDAO;

    public CareScheduleService() {
        this.careScheduleDAO = new CareScheduleDAO();
    }

    public CareScheduleService(CareScheduleDAO careScheduleDAO) {
        this.careScheduleDAO = careScheduleDAO;
    }

    // =========================================================================
    // HÀM NGHIỆP VỤ SỬ DỤNG LAMBDA & STREAM API (MỤC 7)
    // =========================================================================

    /**
     * Lọc lịch sử chăm sóc theo loài hoa, sắp xếp ngày mới nhất lên đầu bằng Lambda.
     */
    public List<CareSchedule> lichSuTheoHoaLambda(String flowerId) {
        return careScheduleDAO.layDanhSachChamSoc().stream()
                .filter(c -> flowerId.equals(c.getFlowerId()))
                .sorted(Comparator.comparing(CareSchedule::getCheckDate,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
    }

    /**
     * Lọc lịch sử chăm sóc theo nhân viên, sắp xếp ngày mới nhất lên đầu bằng Lambda.
     */
    public List<CareSchedule> lichSuTheoNhanVienLambda(String empId) {
        return careScheduleDAO.layDanhSachChamSoc().stream()
                .filter(c -> empId.equals(c.getEmployeeId()))
                .sorted(Comparator.comparing(CareSchedule::getCheckDate,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
    }

    // =========================================================================
    // CÁC HÀM ĐIỀU PHỐI CRUD QUA DAO
    // =========================================================================

    public List<Object[]> findAll() {
        return careScheduleDAO.findAll();
    }

    public List<Object[]> trangThaiHienTai() {
        return careScheduleDAO.trangThaiHienTai();
    }

    public boolean capNhatGhiChu(String careId, String note) {
        return careScheduleDAO.capNhatGhiChu(careId, note);
    }

    public List<Object[]> findById(String id) {
        return careScheduleDAO.findById(id);
    }

    public boolean insert(String id, String flowerId, String empId,
                          LocalDateTime date, String fresh, String water,
                          String action, String note) {
        return careScheduleDAO.insert(id, flowerId, empId, date, fresh, water, action, note);
    }

    public boolean update(String id, String fresh, String water, String action, String note) {
        return careScheduleDAO.update(id, fresh, water, action, note);
    }

    public boolean delete(String id) {
        return careScheduleDAO.delete(id);
    }

    public String sinhMaCSMoi() {
        return careScheduleDAO.sinhMaCSMoi();
    }

    public List<Object[]> lichChamSocCuaNV(String maNV) {
        return careScheduleDAO.lichChamSocCuaNV(maNV);
    }

    public List<Object[]> lichChamSocCuaHoa(String flowerId) {
        return careScheduleDAO.lichChamSocCuaHoa(flowerId);
    }

    public List<CareSchedule> layDanhSachChamSoc() {
        return careScheduleDAO.layDanhSachChamSoc();
    }
}
