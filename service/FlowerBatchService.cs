using System;
using System.Collections.Generic;
using System.Linq;
using vn.edu.crud.dao;
using vn.edu.crud.model;

namespace vn.edu.crud.service;

/// <summary>
/// Tầng Service xử lý nghiệp vụ Quản lý Lô hoa (Mục 9).
/// Đóng gói logic lọc lô còn hàng, quá 7 ngày và sắp hết hạn bằng LINQ & Lambda.
/// </summary>
public class FlowerBatchService
{
    private readonly FlowerBatchDAO _flowerBatchDAO;

    public FlowerBatchService()
    {
        _flowerBatchDAO = new FlowerBatchDAO();
    }

    public FlowerBatchService(FlowerBatchDAO flowerBatchDAO)
    {
        _flowerBatchDAO = flowerBatchDAO;
    }

    // =========================================================================
    // HÀM NGHIỆP VỤ SỬ DỤNG LINQ & BIỂU THỨC LAMBDA (MỤC 9)
    // =========================================================================

    /// <summary>
    /// Lọc danh sách lô hoa còn hàng (AVAILABLE & quantity > 0) bằng Lambda.
    /// </summary>
    public List<FlowerBatch> LoConHangLambda()
    {
        return _flowerBatchDAO.LayDanhSachLo()
            .Where(b => "AVAILABLE".Equals(b.Status, StringComparison.OrdinalIgnoreCase)
                     && b.QuantityRemaining > 0)
            .ToList();
    }

    /// <summary>
    /// Lọc danh sách lô hoa nhập quá 7 ngày bằng Lambda.
    /// </summary>
    public List<FlowerBatch> LoNhapQua7NgayLambda()
    {
        var homNay = DateTime.Today;
        return _flowerBatchDAO.LayDanhSachLo()
            .Where(b => b.ImportDate.HasValue
                     && b.QuantityRemaining > 0
                     && (homNay - b.ImportDate.Value.Date).TotalDays > 7)
            .ToList();
    }

    /// <summary>
    /// Lọc danh sách lô hoa sắp hết hạn trong số ngày chỉ định, sắp xếp tăng dần hạn sử dụng bằng Lambda.
    /// </summary>
    public List<FlowerBatch> LoSapHetHanLambda(int soNgay)
    {
        var homNay = DateTime.Today;
        return _flowerBatchDAO.LayDanhSachLo()
            .Where(b => "AVAILABLE".Equals(b.Status, StringComparison.OrdinalIgnoreCase)
                     && b.QuantityRemaining > 0
                     && b.ExpiryDate.HasValue
                     && (b.ExpiryDate.Value.Date - homNay).TotalDays >= 0
                     && (b.ExpiryDate.Value.Date - homNay).TotalDays <= soNgay)
            .OrderBy(b => b.ExpiryDate!.Value)
            .ToList();
    }

    // =========================================================================
    // CÁC HÀM ĐIỀU PHỐI CRUD QUA DAO
    // =========================================================================

    public List<object[]> FindAll() => _flowerBatchDAO.FindAll();

    public List<object[]> FindById(string id) => _flowerBatchDAO.FindById(id);

    public bool Insert(string id, string poId, string flowerId,
                       int imported, int remaining, decimal cost,
                       DateTime importDate, DateTime expiry,
                       string status, string note)
        => _flowerBatchDAO.Insert(id, poId, flowerId, imported, remaining, cost, importDate, expiry, status, note);

    public bool UpdateRemaining(string batchId, int remaining) => _flowerBatchDAO.UpdateRemaining(batchId, remaining);

    public bool UpdateStatus(string batchId, string status) => _flowerBatchDAO.UpdateStatus(batchId, status);

    public bool CoLo(string flowerId) => _flowerBatchDAO.CoLo(flowerId);

    public List<object[]> LoAvailableCuaHoa(string flowerId) => _flowerBatchDAO.LoAvailableCuaHoa(flowerId);

    public int XuatKho(string flowerId, int soLuong) => _flowerBatchDAO.XuatKho(flowerId, soLuong);

    public bool NhapLaiKho(string flowerId, int soLuong) => _flowerBatchDAO.NhapLaiKho(flowerId, soLuong);

    public bool DoiTrangThaiLo(string batchId, string trangThaiMoi) => _flowerBatchDAO.DoiTrangThaiLo(batchId, trangThaiMoi);

    public bool ThanhLyLo(string batchId) => _flowerBatchDAO.ThanhLyLo(batchId);

    public bool Delete(string id) => _flowerBatchDAO.Delete(id);

    public string SinhMaLoMoi() => _flowerBatchDAO.SinhMaLoMoi();

    public List<object[]> LoHoaConHang() => _flowerBatchDAO.LoHoaConHang();

    public List<object[]> LoHoaSapHetHan(int soNgay) => _flowerBatchDAO.LoHoaSapHetHan(soNgay);

    public List<FlowerBatch> LayDanhSachLo() => _flowerBatchDAO.LayDanhSachLo();
}
