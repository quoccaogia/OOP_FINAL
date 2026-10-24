using System;
using System.Collections.Generic;
using System.Linq;
using vn.edu.crud.dao;
using vn.edu.crud.model;

namespace vn.edu.crud.service;

/// <summary>
/// Tầng Service xử lý nghiệp vụ Nhà cung cấp (Mục 2).
/// Đóng gói logic tìm kiếm bằng LINQ & Lambda.
/// </summary>
public class SupplierService
{
    private readonly SupplierDAO _supplierDAO;

    public SupplierService()
    {
        _supplierDAO = new SupplierDAO();
    }

    public SupplierService(SupplierDAO supplierDAO)
    {
        _supplierDAO = supplierDAO;
    }

    // =========================================================================
    // HÀM NGHIỆP VỤ SỬ DỤNG LINQ & BIỂU THỨC LAMBDA (MỤC 2)
    // =========================================================================

    /// <summary>
    /// Tìm kiếm nhà cung cấp theo tên bằng LINQ Where và Lambda (s => ...).
    /// </summary>
    public List<Supplier> SearchSuppliersByLambda(string keyword)
    {
        var allSuppliers = _supplierDAO.FindAll();
        if (string.IsNullOrWhiteSpace(keyword))
        {
            return allSuppliers;
        }

        string lowerKeyword = keyword.ToLower().Trim();

        return allSuppliers
            .Where(s => !string.IsNullOrEmpty(s.Name) && s.Name.ToLower().Contains(lowerKeyword))
            .ToList();
    }

    // =========================================================================
    // CÁC HÀM ĐIỀU PHỐI CRUD QUA DAO
    // =========================================================================

    public List<Supplier> FindAll() => _supplierDAO.FindAll();

    public Supplier? FindById(string id) => _supplierDAO.FindById(id);

    public List<Supplier> TimTheoSdt(string sdt) => _supplierDAO.TimTheoSdt(sdt);

    public bool Insert(Supplier s) => _supplierDAO.Insert(s);

    public bool Update(Supplier s) => _supplierDAO.Update(s);

    public bool Delete(string id) => _supplierDAO.Delete(id);

    public bool CoHoa(string supplierId) => _supplierDAO.CoHoa(supplierId);

    public string SinhMaNCCMoi() => _supplierDAO.SinhMaNCCMoi();

    public string ChuanHoaSdt(string sdt) => _supplierDAO.ChuanHoaSdt(sdt);

    public List<object[]> TongNhapTheoNCC() => _supplierDAO.TongNhapTheoNCC();

    public List<object[]> NccDangCungCapHoa() => _supplierDAO.NccDangCungCapHoa();

    public List<object[]> NccNhieuLoaiHoaNhat() => _supplierDAO.NccNhieuLoaiHoaNhat();

    public List<object[]> ThongKeLoaiHoaTheoNCC() => _supplierDAO.ThongKeLoaiHoaTheoNCC();
}
