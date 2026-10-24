using System;
using System.Collections.Generic;
using vn.edu.crud.model;
using MySqlConnector;
using vn.edu.crud.util;

namespace vn.edu.crud.dao;

public class PaymentDAO
{
    public string SinhMaThanhToan()
    {
        string sql = "SELECT payment_id FROM payment ORDER BY payment_id DESC LIMIT 1";
        var rows = DBBase.Query(sql);
        if (rows.Count == 0 || rows[0][0] == null)
            return "P001";
        string maCu = rows[0][0].ToString()!;
        int so = int.Parse(maCu.Substring(1));
        return $"P{so + 1:D3}";
    }

    public decimal TinhTongTienDon(string orderId)
    {
        string sql = "SELECT COALESCE(SUM(quantity * unit_price_at_sale), 0) FROM orderdetail WHERE order_id = ?";
        var rows = DBBase.Query(sql, orderId);
        if (rows.Count == 0 || rows[0][0] == null)
            return 0m;
        return Convert.ToDecimal(rows[0][0]);
    }

    public decimal TinhDaTra(string orderId)
    {
        string sql = "SELECT COALESCE(SUM(amount), 0) FROM payment WHERE order_id = ? AND payment_status = 'THANH_TOAN_THANH_CONG'";
        var rows = DBBase.Query(sql, orderId);
        if (rows.Count == 0 || rows[0][0] == null)
            return 0m;
        return Convert.ToDecimal(rows[0][0]);
    }

    public bool TaoThanhToan(Payment p)
    {
        string sql = "INSERT INTO payment (payment_id, order_id, amount, payment_method, payment_date, payment_status) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        return DBBase.Update(sql,
            p.PaymentId, p.OrderId, p.Amount,
            p.PaymentMethod, (object?)p.PaymentDate ?? DBNull.Value,
            p.PaymentStatus) > 0;
    }

    public string? LayMaDon(string paymentId)
    {
        string sql = "SELECT order_id FROM payment WHERE payment_id = ?";
        var rows = DBBase.Query(sql, paymentId);
        if (rows.Count == 0 || rows[0][0] == null) return null;
        return rows[0][0]?.ToString();
    }

    public string? LayTrangThaiThanhToan(string paymentId)
    {
        string sql = "SELECT payment_status FROM payment WHERE payment_id = ?";
        var rows = DBBase.Query(sql, paymentId);
        if (rows.Count == 0 || rows[0][0] == null) return null;
        return rows[0][0]?.ToString();
    }

    public decimal TinhTienCoc(string orderId)
    {
        return Math.Ceiling(TinhTongTienDon(orderId) * 0.2m);
    }

    public bool XoaThanhToan(string paymentId)
    {
        return DBBase.Update("UPDATE payment SET payment_status = 'DA_HUY' WHERE payment_id = ?", paymentId) > 0;
    }

    public bool HoanTien(string paymentId)
    {
        string sql = "UPDATE payment SET payment_status = 'DA_HOAN_TIEN' WHERE payment_id = ? AND payment_status = 'THANH_TOAN_THANH_CONG'";
        return DBBase.Update(sql, paymentId) > 0;
    }

    public bool HoanTienToanBo(string orderId)
    {
        string sql = "UPDATE payment SET payment_status = 'DA_HOAN_TIEN' WHERE order_id = ? AND payment_status = 'THANH_TOAN_THANH_CONG'";
        return DBBase.Update(sql, orderId) > 0;
    }

    public decimal TinhDaHoanTien(string orderId)
    {
        string sql = "SELECT COALESCE(SUM(amount), 0) FROM payment WHERE order_id = ? AND payment_status = 'DA_HOAN_TIEN'";
        var rows = DBBase.Query(sql, orderId);
        if (rows.Count == 0 || rows[0][0] == null)
            return 0m;
        return Convert.ToDecimal(rows[0][0]);
    }

    public List<object[]> LayDanhSachDonChuaThanhToan()
    {
        string sql = "SELECT o.order_id, o.order_status, c.name, " +
                     "(SELECT COALESCE(SUM(quantity * unit_price_at_sale), 0) FROM orderdetail od WHERE od.order_id = o.order_id) as total, " +
                     "(SELECT COALESCE(SUM(amount), 0) FROM payment p WHERE p.order_id = o.order_id AND p.payment_status = 'THANH_TOAN_THANH_CONG') as paid " +
                     "FROM `Order` o " +
                     "JOIN Customer c ON o.customer_id = c.customer_id " +
                     "WHERE o.order_status NOT IN ('DA_HUY', 'GIAO_THAT_BAI')";

        var rawList = DBBase.Query(sql);
        var ketQua = new List<object[]>();

        foreach (var row in rawList)
        {
            decimal total = row[3] == null ? 0m : Convert.ToDecimal(row[3]);
            decimal paid = row[4] == null ? 0m : Convert.ToDecimal(row[4]);
            if (total > paid && total > 0m)
            {
                ketQua.Add(row);
            }
        }
        return ketQua;
    }

    public List<object[]> ThanhToanTheoPhuongThuc(string phuongThuc)
    {
        string sql = "SELECT * FROM payment WHERE payment_method = ?";
        return DBBase.Query(sql, phuongThuc);
    }

    public List<object[]> DoanhThuHomNay()
    {
        string sql = "SELECT COALESCE(SUM(amount),0) AS total FROM payment " +
                     "WHERE DATE(payment_date) = CURDATE() AND payment_status = 'THANH_TOAN_THANH_CONG'";
        return DBBase.Query(sql);
    }

    public List<object[]> TatCaThanhToan()
    {
        string sql = "SELECT payment_id, order_id, amount, payment_method, payment_date, payment_status FROM payment";
        return DBBase.Query(sql);
    }

    public List<Payment> LayDanhSachThanhToan()
    {
        var list = new List<Payment>();
        string sql = "SELECT payment_id, order_id, amount, payment_method, payment_date, payment_status FROM payment";
        try
        {
            using var conn = DBConnection.GetConnection();
            using var cmd = new MySqlCommand(sql, conn);
            using var reader = cmd.ExecuteReader();
            while (reader.Read())
            {
                list.Add(new Payment
                {
                    PaymentId = reader.GetString(0),
                    OrderId = reader.IsDBNull(1) ? "" : reader.GetString(1),
                    Amount = reader.IsDBNull(2) ? 0m : reader.GetDecimal(2),
                    PaymentMethod = reader.IsDBNull(3) ? "" : reader.GetString(3),
                    PaymentDate = reader.IsDBNull(4) ? DateTime.MinValue : reader.GetDateTime(4),
                    PaymentStatus = reader.IsDBNull(5) ? "" : reader.GetString(5)
                });
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"[PaymentDAO.LayDanhSachThanhToan Error]: {ex.Message}");
        }
        return list;
    }
}
