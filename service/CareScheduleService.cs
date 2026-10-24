using System;
using System.Collections.Generic;
using System.Linq;
using vn.edu.crud.dao;
using vn.edu.crud.model;

namespace vn.edu.crud.service;

/// <summary>
/// Tầng Service xử lý nghiệp vụ Lịch chăm sóc hoa (Mục 7).
/// Đóng gói logic lọc và sắp xếp lịch sử chăm sóc bằng LINQ & Lambda.
/// </summary>
public class CareScheduleService
{
    private readonly CareScheduleDAO _careScheduleDAO;

    public CareScheduleService()
    {
        _careScheduleDAO = new CareScheduleDAO();
    }

    public CareScheduleService(CareScheduleDAO careScheduleDAO)
    {
        _careScheduleDAO = careScheduleDAO;
    }

    // =========================================================================
    // HÀM NGHIỆP VỤ SỬ DỤNG LINQ & BIỂU THỨC LAMBDA (MỤC 7)
    // =========================================================================

    /// <summary>
    /// Lọc lịch sử chăm sóc theo loài hoa, sắp xếp ngày mới nhất lên đầu bằng Lambda.
    /// </summary>
    public List<CareSchedule> LichSuTheoHoaLambda(string flowerId)
    {
        return _careScheduleDAO.LayDanhSachChamSoc()
            .Where(c => c.FlowerId == flowerId)
            .OrderByDescending(c => c.CheckDate)
            .ToList();
    }

    /// <summary>
    /// Lọc lịch sử chăm sóc theo nhân viên, sắp xếp ngày mới nhất lên đầu bằng Lambda.
    /// </summary>
    public List<CareSchedule> LichSuTheoNhanVienLambda(string empId)
    {
        return _careScheduleDAO.LayDanhSachChamSoc()
            .Where(c => c.EmployeeId == empId)
            .OrderByDescending(c => c.CheckDate)
            .ToList();
    }

    // =========================================================================
    // CÁC HÀM ĐIỀU PHỐI CRUD QUA DAO
    // =========================================================================

    public List<object[]> FindAll() => _careScheduleDAO.FindAll();

    public List<object[]> TrangThaiHienTai() => _careScheduleDAO.TrangThaiHienTai();

    public bool CapNhatGhiChu(string careId, string note) => _careScheduleDAO.CapNhatGhiChu(careId, note);

    public List<object[]> FindById(string id) => _careScheduleDAO.FindById(id);

    public bool Insert(string id, string flowerId, string empId,
                       DateTime date, string fresh, string water,
                       string action, string note)
        => _careScheduleDAO.Insert(id, flowerId, empId, date, fresh, water, action, note);

    public bool Update(string id, string fresh, string water, string action, string note)
        => _careScheduleDAO.Update(id, fresh, water, action, note);

    public bool Delete(string id) => _careScheduleDAO.Delete(id);

    public string SinhMaCSMoi() => _careScheduleDAO.SinhMaCSMoi();

    public List<object[]> LichChamSocCuaNV(string maNV) => _careScheduleDAO.LichChamSocCuaNV(maNV);

    public List<object[]> LichChamSocCuaHoa(string flowerId) => _careScheduleDAO.LichChamSocCuaHoa(flowerId);

    public List<CareSchedule> LayDanhSachChamSoc() => _careScheduleDAO.LayDanhSachChamSoc();
}
