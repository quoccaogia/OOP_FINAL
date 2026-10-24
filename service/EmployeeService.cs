using System;
using System.Collections.Generic;
using System.Linq;
using vn.edu.crud.dao;
using vn.edu.crud.model;
using vn.edu.crud.util;

namespace vn.edu.crud.service;

/// <summary>
/// Tầng Service xử lý nghiệp vụ Nhân viên (Mục 3).
/// Đóng gói logic tìm kiếm nhân viên bằng LINQ & Lambda.
/// </summary>
public class EmployeeService
{
    private readonly EmployeeDAO _employeeDAO;

    public EmployeeService()
    {
        _employeeDAO = new EmployeeDAO();
    }

    public EmployeeService(EmployeeDAO employeeDAO)
    {
        _employeeDAO = employeeDAO;
    }

    // =========================================================================
    // HÀM NGHIỆP VỤ SỬ DỤNG LINQ & BIỂU THỨC LAMBDA (MỤC 3)
    // =========================================================================

    /// <summary>
    /// Tìm kiếm nhân viên theo Mã nhân viên hoặc Tên nhân viên bằng LINQ Where & Lambda.
    /// </summary>
    public List<Employee> SearchEmployeesByLambda(string keyword)
    {
        var allEmployees = _employeeDAO.FindAll();
        if (string.IsNullOrWhiteSpace(keyword))
        {
            return allEmployees;
        }

        string lowerKeyword = keyword.ToLower().Trim();

        return allEmployees
            .Where(e => (!string.IsNullOrEmpty(e.EmployeeId) && e.EmployeeId.ToLower().Contains(lowerKeyword)) ||
                        (!string.IsNullOrEmpty(e.Name) && e.Name.ToLower().Contains(lowerKeyword)))
            .ToList();
    }

    // =========================================================================
    // CÁC HÀM ĐIỀU PHỐI CRUD QUA DAO
    // =========================================================================

    public List<Employee> FindAll() => _employeeDAO.FindAll();

    public Employee? FindById(string id) => _employeeDAO.FindById(id);

    public bool Insert(Employee e) => _employeeDAO.Insert(e);

    public bool Update(Employee e) => _employeeDAO.Update(e);

    public bool Delete(string id) => _employeeDAO.Delete(id);

    public string SinhMaNVMoi() => _employeeDAO.SinhMaNVMoi();

    public string ChuanHoaSdt(string sdt) => NhapLieu.ChuanHoaSdt(sdt);

    public bool KiemTraNhanVienCoDon(string employeeId) => _employeeDAO.KiemTraNhanVienCoDon(employeeId);

    public decimal TinhDoanhThuNhanVien(string employeeId) => _employeeDAO.TinhDoanhThuNhanVien(employeeId);

    public List<object[]> NhanVienCoDonHang() => _employeeDAO.NhanVienCoDonHang();

    public List<object[]> NhanVienNhieuDonNhat() => _employeeDAO.NhanVienNhieuDonNhat();
}
