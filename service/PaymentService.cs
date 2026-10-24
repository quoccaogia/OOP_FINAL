using System;
using System.Collections.Generic;
using System.Linq;
using vn.edu.crud.dao;
using vn.edu.crud.model;

namespace vn.edu.crud.service;

/// <summary>
/// Tầng Service xử lý nghiệp vụ Quản lý Thanh toán (Mục 6).
/// Đóng gói logic lọc thanh toán và tính doanh thu hôm nay bằng LINQ & Lambda.
/// </summary>
public class PaymentService
{
    private readonly PaymentDAO _paymentDAO;

    public PaymentService()
    {
        _paymentDAO = new PaymentDAO();
    }

    public PaymentService(PaymentDAO paymentDAO)
    {
        _paymentDAO = paymentDAO;
    }

    // =========================================================================
    // HÀM NGHIỆP VỤ SỬ DỤNG LINQ & BIỂU THỨC LAMBDA (MỤC 6)
    // =========================================================================

    /// <summary>
    /// Lọc danh sách thanh toán theo phương thức bằng LINQ Where & Lambda.
    /// </summary>
    public List<Payment> TheoPhuongThucLambda(string phuongThuc)
    {
        return _paymentDAO.LayDanhSachThanhToan()
            .Where(p => p.PaymentMethod == phuongThuc)
            .ToList();
    }

    /// <summary>
    /// Tính tổng doanh thu hôm nay bằng LINQ Where, Select và Sum.
    /// </summary>
    public decimal DoanhThuHomNayLambda()
    {
        var homNay = DateTime.Today;
        return _paymentDAO.LayDanhSachThanhToan()
            .Where(p => "THANH_TOAN_THANH_CONG".Equals(p.PaymentStatus, StringComparison.OrdinalIgnoreCase)
                     && p.PaymentDate.HasValue
                     && p.PaymentDate.Value.Date == homNay)
            .Sum(p => p.Amount);
    }

    // =========================================================================
    // CÁC HÀM ĐIỀU PHỐI CRUD QUA DAO
    // =========================================================================

    public string SinhMaThanhToan() => _paymentDAO.SinhMaThanhToan();

    public decimal TinhTongTienDon(string orderId) => _paymentDAO.TinhTongTienDon(orderId);

    public decimal TinhDaTra(string orderId) => _paymentDAO.TinhDaTra(orderId);

    public bool TaoThanhToan(Payment p) => _paymentDAO.TaoThanhToan(p);

    public string? LayMaDon(string paymentId) => _paymentDAO.LayMaDon(paymentId);

    public string? LayTrangThaiThanhToan(string paymentId) => _paymentDAO.LayTrangThaiThanhToan(paymentId);

    public decimal TinhTienCoc(string orderId) => _paymentDAO.TinhTienCoc(orderId);

    public bool XoaThanhToan(string paymentId) => _paymentDAO.XoaThanhToan(paymentId);

    public bool HoanTien(string paymentId) => _paymentDAO.HoanTien(paymentId);

    public bool HoanTienToanBo(string orderId) => _paymentDAO.HoanTienToanBo(orderId);

    public decimal TinhDaHoanTien(string orderId) => _paymentDAO.TinhDaHoanTien(orderId);

    public List<object[]> LayDanhSachDonChuaThanhToan() => _paymentDAO.LayDanhSachDonChuaThanhToan();

    public List<object[]> ThanhToanTheoPhuongThuc(string phuongThuc) => _paymentDAO.ThanhToanTheoPhuongThuc(phuongThuc);

    public List<object[]> DoanhThuHomNay() => _paymentDAO.DoanhThuHomNay();

    public List<object[]> TatCaThanhToan() => _paymentDAO.TatCaThanhToan();

    public List<Payment> LayDanhSachThanhToan() => _paymentDAO.LayDanhSachThanhToan();
}
