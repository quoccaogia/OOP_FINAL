using System;
using System.Collections.Generic;
using System.Linq;
using vn.edu.crud.dao;
using vn.edu.crud.model;

namespace vn.edu.crud.service;

/// <summary>
/// Tầng Service xử lý nghiệp vụ Quản lý Đơn hàng (Mục 5).
/// Đóng gói các hàm lọc, tìm kiếm và sắp xếp đơn hàng bằng LINQ & Lambda.
/// </summary>
public class OrderService
{
    private readonly OrderDAO _orderDAO;

    public OrderService()
    {
        _orderDAO = new OrderDAO();
    }

    public OrderService(OrderDAO orderDAO)
    {
        _orderDAO = orderDAO;
    }

    // =========================================================================
    // HÀM NGHIỆP VỤ SỬ DỤNG LINQ & BIỂU THỨC LAMBDA (MỤC 5)
    // =========================================================================

    // 1. Tìm kiếm đơn hàng theo Mã đơn, Mã KH, hoặc Mã NV bằng Lambda
    public List<Order> SearchOrdersByLambda(string keyword)
    {
        var allOrders = _orderDAO.FindAll();
        if (string.IsNullOrWhiteSpace(keyword))
        {
            return allOrders;
        }

        string kw = keyword.ToLower().Trim();

        return allOrders
            .Where(o => (!string.IsNullOrEmpty(o.OrderId) && o.OrderId.ToLower().Contains(kw)) ||
                        (!string.IsNullOrEmpty(o.CustomerId) && o.CustomerId.ToLower().Contains(kw)) ||
                        (!string.IsNullOrEmpty(o.EmployeeId) && o.EmployeeId.ToLower().Contains(kw)))
            .ToList();
    }

    // 2. Lọc danh sách đơn chưa giao hôm nay bằng Lambda
    public List<Order> DonChuaGiaoHomNayLambda()
    {
        var allOrders = _orderDAO.FindAll();
        var homNay = DateTime.Today;

        return allOrders
            .Where(o => o.DeliveryDate.HasValue
                     && o.DeliveryDate.Value.Date == homNay
                     && !"HOAN_THANH".Equals(o.OrderStatus, StringComparison.OrdinalIgnoreCase)
                     && !"DA_HUY".Equals(o.OrderStatus, StringComparison.OrdinalIgnoreCase)
                     && !"GIAO_THAT_BAI".Equals(o.OrderStatus, StringComparison.OrdinalIgnoreCase))
            .ToList();
    }

    // 3. Lọc danh sách đơn hàng của một khách hàng bằng Lambda
    public List<Order> DonCuaKhachLambda(string maKhach)
    {
        var allOrders = _orderDAO.FindAll();
        if (string.IsNullOrWhiteSpace(maKhach))
        {
            return new List<Order>();
        }

        return allOrders
            .Where(o => !string.IsNullOrEmpty(o.CustomerId) && o.CustomerId.Equals(maKhach.Trim(), StringComparison.OrdinalIgnoreCase))
            .ToList();
    }

    // 4. Lọc đơn hàng theo trạng thái cụ thể bằng Lambda
    public List<Order> LocDonTheoTrangThaiLambda(string status)
    {
        var allOrders = _orderDAO.FindAll();
        if (string.IsNullOrWhiteSpace(status))
        {
            return allOrders;
        }

        return allOrders
            .Where(o => !string.IsNullOrEmpty(o.OrderStatus) && o.OrderStatus.Equals(status.Trim(), StringComparison.OrdinalIgnoreCase))
            .ToList();
    }

    // 5. Sắp xếp đơn hàng mới nhất lên đầu bằng Lambda
    public List<Order> DonHangMoiNhatLambda()
    {
        return _orderDAO.FindAll()
            .Where(o => o.OrderDate != DateTime.MinValue)
            .OrderByDescending(o => o.OrderDate)
            .ToList();
    }

    // =========================================================================
    // CÁC HÀM ĐIỀU PHỐI QUA DAO
    // =========================================================================

    public List<Order> FindAll() => _orderDAO.FindAll();

    public string SinhMaDonMoi() => _orderDAO.SinhMaDonMoi();

    public bool TaoDonHang(Order o) => _orderDAO.TaoDonHang(o);

    public bool DonTonTai(string orderId) => _orderDAO.DonTonTai(orderId);

    public int DemSoMatHang(string orderId) => _orderDAO.DemSoMatHang(orderId);

    public Order? TimTheoMa(string orderId) => _orderDAO.TimTheoMa(orderId);

    public bool CapNhatThongTin(string orderId, string diaChi, DateTime ngayGiao, string employeeId)
        => _orderDAO.CapNhatThongTin(orderId, diaChi, ngayGiao, employeeId);

    public bool HoaDaCoTrongDon(string orderId, string flowerId) => _orderDAO.HoaDaCoTrongDon(orderId, flowerId);

    public bool ThemChiTiet(string orderId, string flowerId, int quantity, decimal price)
        => _orderDAO.ThemChiTiet(orderId, flowerId, quantity, price);

    public bool CongThemSoLuong(string orderId, string flowerId, int them)
        => _orderDAO.CongThemSoLuong(orderId, flowerId, them);

    public bool TruSoLuong(string flowerId, int soLuong) => _orderDAO.TruSoLuong(flowerId, soLuong);

    public bool CongTonKho(string flowerId, int soLuong) => _orderDAO.CongTonKho(flowerId, soLuong);

    public int LaySoLuongTrongDon(string orderId, string flowerId) => _orderDAO.LaySoLuongTrongDon(orderId, flowerId);

    public string? LayTrangThaiDon(string orderId) => _orderDAO.LayTrangThaiDon(orderId);

    public List<object[]> LayHoaTrongDon(string maDon) => _orderDAO.LayHoaTrongDon(maDon);

    public bool CapNhatTrangThai(string orderId, string status) => _orderDAO.CapNhatTrangThai(orderId, status);

    public bool XoaDon(string orderId) => _orderDAO.XoaDon(orderId);

    public bool DonDaCoThanhToan(string orderId) => _orderDAO.DonDaCoThanhToan(orderId);

    public bool XoaChiTiet(string orderId, string flowerId) => _orderDAO.XoaChiTiet(orderId, flowerId);

    public List<object[]> DonHangGiaCao(decimal min) => _orderDAO.DonHangGiaCao(min);

    public List<Order> DonChuaGiaoHomNay() => _orderDAO.DonChuaGiaoHomNay();

    public decimal DoanhThuHomNay() => _orderDAO.DoanhThuHomNay();

    public List<object[]> KhachDaTungDatHang() => _orderDAO.KhachDaTungDatHang();

    public List<object[]> KhachChuaTungDatHang() => _orderDAO.KhachChuaTungDatHang();

    public List<object[]> KhachNhieuDonNhat() => _orderDAO.KhachNhieuDonNhat();

    public List<object[]> NhanVienCoDonHang() => _orderDAO.NhanVienCoDonHang();

    public List<object[]> NhanVienNhieuDonNhat() => _orderDAO.NhanVienNhieuDonNhat();

    public List<object[]> TongChiTieuTheoKhach() => _orderDAO.TongChiTieuTheoKhach();

    public List<Order> DonCuaKhach(string maKhach) => _orderDAO.DonCuaKhach(maKhach);

    public List<object[]> ChiTietDonHang(string maDon) => _orderDAO.ChiTietDonHang(maDon);

    public List<object[]> ThongTinDayDuDonHang(string orderId) => _orderDAO.ThongTinDayDuDonHang(orderId);

    public List<object[]> DonCoNhieuLoaiHoa(int n) => _orderDAO.DonCoNhieuLoaiHoa(n);

    public List<object[]> ThongKeDonTheoTrangThai() => _orderDAO.ThongKeDonTheoTrangThai();
}
