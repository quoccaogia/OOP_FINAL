using System;
using System.Collections.Generic;
using System.Linq;
using vn.edu.crud.dao;
using vn.edu.crud.model;

namespace vn.edu.crud.service;

/// <summary>
/// Tầng Service xử lý nghiệp vụ Quản lý Hoa (Mục 4).
/// Đóng gói các hàm lọc, sắp xếp, tìm kiếm và tính tổng tồn kho bằng LINQ & Lambda.
/// </summary>
public class FlowerService
{
    private readonly FlowerDAO _flowerDAO;

    public FlowerService()
    {
        _flowerDAO = new FlowerDAO();
    }

    public FlowerService(FlowerDAO flowerDAO)
    {
        _flowerDAO = flowerDAO;
    }

    // =========================================================================
    // HÀM NGHIỆP VỤ SỬ DỤNG LINQ & BIỂU THỨC LAMBDA (MỤC 4)
    // =========================================================================

    // 1. Tìm kiếm hoa theo Tên hoặc Danh mục bằng Lambda
    public List<Flower> SearchFlowersByLambda(string keyword)
    {
        var allFlowers = _flowerDAO.FindAll();
        if (string.IsNullOrWhiteSpace(keyword))
        {
            return allFlowers;
        }

        string tuKhoa = keyword.ToLower().Trim();

        return allFlowers
            .Where(f => (!string.IsNullOrEmpty(f.Name) && f.Name.ToLower().Contains(tuKhoa)) ||
                        (!string.IsNullOrEmpty(f.Category) && f.Category.ToLower().Contains(tuKhoa)))
            .ToList();
    }

    // 2. Lọc danh sách hoa sắp hết hàng (tồn kho < ngưỡng) bằng Lambda
    public List<Flower> HoaSapHetLambda(int nguong)
    {
        return _flowerDAO.FindAll()
            .Where(f => f.StockQuantity < nguong)
            .ToList();
    }

    // 3. Lọc hoa theo khoảng giá bằng Lambda
    public List<Flower> TimTheoKhoangGiaLambda(decimal giaMin, decimal giaMax)
    {
        return _flowerDAO.FindAll()
            .Where(f => f.UnitPrice >= giaMin && f.UnitPrice <= giaMax)
            .ToList();
    }

    // 4. Sắp xếp danh sách hoa theo giá giảm dần bằng Lambda
    public List<Flower> HoaGiaGiamDanLambda()
    {
        return _flowerDAO.FindAll()
            .OrderByDescending(f => f.UnitPrice)
            .ToList();
    }

    // 5. Tính tổng số lượng tồn kho của tất cả hoa bằng LINQ Sum() & Lambda
    public int TinhTongTonKhoLambda()
    {
        return TinhTongTonKhoLambda(_flowerDAO.FindAll());
    }

    public int TinhTongTonKhoLambda(List<Flower> flowerList)
    {
        if (flowerList == null || flowerList.Count == 0)
        {
            return 0;
        }
        return flowerList.Sum(f => f.StockQuantity);
    }

    // =========================================================================
    // CÁC HÀM ĐIỀU PHỐI CRUD QUA DAO
    // =========================================================================

    public List<Flower> FindAll() => _flowerDAO.FindAll();

    public Flower? TimTheoMa(string maHoa) => _flowerDAO.TimTheoMa(maHoa);

    public bool Insert(string id, string name, string cat, decimal price, int stock, string color, string supId)
        => _flowerDAO.Insert(id, name, cat, price, stock, color, supId);

    public bool Update(string id, string name, string cat, decimal price, int stock, string color, string supId)
        => _flowerDAO.Update(id, name, cat, price, stock, color, supId);

    public bool Delete(string id) => _flowerDAO.Delete(id);

    public string SinhMaHoaMoi() => _flowerDAO.SinhMaHoaMoi();

    public bool CongTonKho(string flowerId, int soLuong) => _flowerDAO.CongTonKho(flowerId, soLuong);

    public int DongBoTonKhoTuLo() => _flowerDAO.DongBoTonKhoTuLo();

    public List<Flower> TimTheoMau(string mau) => _flowerDAO.TimTheoMau(mau);

    public List<object[]> HoaBanChay() => _flowerDAO.HoaBanChay();

    public List<object[]> HoaBanCham() => _flowerDAO.HoaBanCham();

    public List<object[]> TongBanTheoHoa() => _flowerDAO.TongBanTheoHoa();

    public List<Flower> HoaChuaBanDuoc() => _flowerDAO.HoaChuaBanDuoc();

    public List<object[]> LoHoaTonLau(int soNgay) => _flowerDAO.LoHoaTonLau(soNgay);
}
