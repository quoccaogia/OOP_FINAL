using System;
using System.Collections.Generic;
using System.Linq;
using vn.edu.crud.dao;
using vn.edu.crud.model;

namespace vn.edu.crud.service;

/// <summary>
/// Tầng Service xử lý nghiệp vụ Phiếu nhập hàng (Mục 8).
/// Đóng gói logic tìm kiếm và tổng hợp tiền nhập theo NCC bằng LINQ & Lambda.
/// </summary>
public class PurchaseOrderService
{
    private readonly PurchaseOrderDAO _purchaseOrderDAO;

    public PurchaseOrderService()
    {
        _purchaseOrderDAO = new PurchaseOrderDAO();
    }

    public PurchaseOrderService(PurchaseOrderDAO purchaseOrderDAO)
    {
        _purchaseOrderDAO = purchaseOrderDAO;
    }

    // =========================================================================
    // HÀM NGHIỆP VỤ SỬ DỤNG LINQ & BIỂU THỨC LAMBDA (MỤC 8)
    // =========================================================================

    /// <summary>
    /// Tìm phiếu nhập theo mã phiếu bằng Lambda.
    /// </summary>
    public List<PurchaseOrder> TimPhieuTheoMaLambda(string maPN)
    {
        return _purchaseOrderDAO.LayDanhSachPhieuNhap()
            .Where(po => po.PurchaseOrderId == maPN)
            .ToList();
    }

    /// <summary>
    /// Lọc phiếu nhập của 1 nhà cung cấp bằng Lambda.
    /// </summary>
    public List<PurchaseOrder> PhieuCuaNCCLambda(string maNCC)
    {
        return _purchaseOrderDAO.LayDanhSachPhieuNhap()
            .Where(po => po.SupplierId == maNCC)
            .ToList();
    }

    /// <summary>
    /// Gom nhóm và tính tổng tiền nhập theo từng NCC bằng LINQ GroupBy & Sum.
    /// </summary>
    public Dictionary<string, decimal> TongNhapTheoNCCLambda()
    {
        return _purchaseOrderDAO.LayDanhSachPhieuNhap()
            .Where(po => !string.IsNullOrEmpty(po.SupplierId) && po.TotalAmount > 0)
            .GroupBy(po => po.SupplierId)
            .OrderBy(g => g.Key)
            .ToDictionary(g => g.Key, g => g.Sum(po => po.TotalAmount));
    }

    // =========================================================================
    // CÁC HÀM ĐIỀU PHỐI CRUD QUA DAO
    // =========================================================================

    public List<object[]> FindAll() => _purchaseOrderDAO.FindAll();

    public List<object[]> FindById(string id) => _purchaseOrderDAO.FindById(id);

    public bool Insert(string id, string supId, string empId,
                       DateTime date, decimal total,
                       string status, string note)
        => _purchaseOrderDAO.Insert(id, supId, empId, date, total, status, note);

    public bool Update(string id, decimal total, string status, string note)
        => _purchaseOrderDAO.Update(id, total, status, note);

    public bool Delete(string id) => _purchaseOrderDAO.Delete(id);

    public string SinhMaPNMoi() => _purchaseOrderDAO.SinhMaPNMoi();

    public List<object[]> PhieuChoXuLy() => _purchaseOrderDAO.PhieuChoXuLy();

    public int DemLoCuaPhieu(string purchaseOrderId) => _purchaseOrderDAO.DemLoCuaPhieu(purchaseOrderId);

    public bool ChotPhieu(string purchaseOrderId) => _purchaseOrderDAO.ChotPhieu(purchaseOrderId);

    public List<object[]> TongNhapTheoNCC() => _purchaseOrderDAO.TongNhapTheoNCC();

    public List<object[]> PhieuNhapCuaNCC(string maNCC) => _purchaseOrderDAO.PhieuNhapCuaNCC(maNCC);

    public List<object[]> TatCaPhieuNhap() => _purchaseOrderDAO.TatCaPhieuNhap();

    public List<PurchaseOrder> LayDanhSachPhieuNhap() => _purchaseOrderDAO.LayDanhSachPhieuNhap();
}
