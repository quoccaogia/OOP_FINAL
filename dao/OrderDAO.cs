using System;
using System.Collections.Generic;
using vn.edu.crud.model;
using MySqlConnector;
using vn.edu.crud.util;

namespace vn.edu.crud.dao;

public class OrderDAO
{
    public string SinhMaDonMoi()
    {
        string sql = "SELECT order_id FROM `Order` ORDER BY order_id DESC LIMIT 1";
        var rows = DBBase.Query(sql);
        if (rows.Count == 0 || rows[0][0] == null)
            return "ORD001";
        string maCu = rows[0][0].ToString()!;
        int so = int.Parse(maCu.Substring(3));
        return $"ORD{so + 1:D3}";
    }

    public List<Order> FindAll()
    {
        var list = new List<Order>();
        string sql = "SELECT order_id, customer_id, employee_id, order_date, delivery_date, delivery_address, order_status FROM `Order` ORDER BY order_id DESC";

        try
        {
            using var conn = DBConnection.GetConnection();
            using var cmd = new MySqlCommand(sql, conn);
            using var reader = cmd.ExecuteReader();
            while (reader.Read())
            {
                list.Add(DocMotDon(reader));
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"[OrderDAO.FindAll Error]: {ex.Message}");
        }
        return list;
    }

    public bool TaoDonHang(Order o)
    {
        string sql = "INSERT INTO `Order` (order_id, customer_id, employee_id, order_date, delivery_date, delivery_address, order_status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        return DBBase.Update(sql,
            o.OrderId, o.CustomerId, o.EmployeeId,
            o.OrderDate, (object?)o.DeliveryDate ?? DBNull.Value,
            o.DeliveryAddress, o.OrderStatus) > 0;
    }

    public bool DonTonTai(string orderId)
    {
        string sql = "SELECT 1 FROM `Order` WHERE order_id = ?";
        return DBBase.Query(sql, orderId).Count > 0;
    }

    public int DemSoMatHang(string orderId)
    {
        string sql = "SELECT COUNT(*) FROM OrderDetail WHERE order_id = ?";
        var rows = DBBase.Query(sql, orderId);
        if (rows.Count == 0 || rows[0][0] == null) return 0;
        return Convert.ToInt32(rows[0][0]);
    }

    public Order? TimTheoMa(string orderId)
    {
        string sql = "SELECT order_id, customer_id, employee_id, order_date, delivery_date, delivery_address, order_status FROM `Order` WHERE order_id = @id";
        try
        {
            using var conn = DBConnection.GetConnection();
            using var cmd = new MySqlCommand(sql, conn);
            cmd.Parameters.AddWithValue("@id", orderId);
            using var reader = cmd.ExecuteReader();
            if (reader.Read())
            {
                return DocMotDon(reader);
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"[OrderDAO.TimTheoMa Error]: {ex.Message}");
        }
        return null;
    }

    public bool CapNhatThongTin(string orderId, string diaChi, DateTime ngayGiao, string employeeId)
    {
        string sql = "UPDATE `Order` SET delivery_address = ?, delivery_date = ?, employee_id = ? WHERE order_id = ?";
        return DBBase.Update(sql, diaChi, ngayGiao, employeeId, orderId) > 0;
    }

    public bool HoaDaCoTrongDon(string orderId, string flowerId)
    {
        string sql = "SELECT 1 FROM OrderDetail WHERE order_id = ? AND flower_id = ?";
        return DBBase.Query(sql, orderId, flowerId).Count > 0;
    }

    public bool ThemChiTiet(string orderId, string flowerId, int quantity, decimal price)
    {
        string sql = "INSERT INTO OrderDetail (order_id, flower_id, quantity, unit_price_at_sale) VALUES (?, ?, ?, ?)";
        return DBBase.Update(sql, orderId, flowerId, quantity, price) > 0;
    }

    public bool CongThemSoLuong(string orderId, string flowerId, int them)
    {
        string sql = "UPDATE OrderDetail SET quantity = quantity + ? WHERE order_id = ? AND flower_id = ?";
        return DBBase.Update(sql, them, orderId, flowerId) > 0;
    }

    public bool TruSoLuong(string flowerId, int soLuong)
    {
        string sql = "UPDATE Flower SET stock_quantity = stock_quantity - ? WHERE flower_id = ?";
        return DBBase.Update(sql, soLuong, flowerId) > 0;
    }

    public bool CongTonKho(string flowerId, int soLuong)
    {
        string sql = "UPDATE Flower SET stock_quantity = stock_quantity + ? WHERE flower_id = ?";
        return DBBase.Update(sql, soLuong, flowerId) > 0;
    }

    public int LaySoLuongTrongDon(string orderId, string flowerId)
    {
        string sql = "SELECT quantity FROM OrderDetail WHERE order_id = ? AND flower_id = ?";
        var rows = DBBase.Query(sql, orderId, flowerId);
        if (rows.Count == 0 || rows[0][0] == null) return 0;
        return Convert.ToInt32(rows[0][0]);
    }

    public string? LayTrangThaiDon(string orderId)
    {
        string sql = "SELECT order_status FROM `Order` WHERE order_id = ?";
        var rows = DBBase.Query(sql, orderId);
        if (rows.Count == 0 || rows[0][0] == null) return null;
        return rows[0][0]?.ToString();
    }

    public List<object[]> LayHoaTrongDon(string maDon)
    {
        string sql = "SELECT flower_id, quantity FROM OrderDetail WHERE order_id = ?";
        return DBBase.Query(sql, maDon);
    }

    public bool CapNhatTrangThai(string orderId, string status)
    {
        string sql = "UPDATE `Order` SET order_status = ? WHERE order_id = ?";
        return DBBase.Update(sql, status, orderId) > 0;
    }

    public bool XoaDon(string orderId)
    {
        DBBase.Update("DELETE FROM payment WHERE order_id = ?", orderId);
        return DBBase.Update("DELETE FROM `Order` WHERE order_id = ?", orderId) > 0;
    }

    public bool DonDaCoThanhToan(string orderId)
    {
        string sql = "SELECT 1 FROM Payment WHERE order_id = ? AND payment_status = 'THANH_TOAN_THANH_CONG'";
        return DBBase.Query(sql, orderId).Count > 0;
    }

    public bool XoaChiTiet(string orderId, string flowerId)
    {
        string sql = "DELETE FROM OrderDetail WHERE order_id = ? AND flower_id = ?";
        return DBBase.Update(sql, orderId, flowerId) > 0;
    }

    public List<object[]> DonHangGiaCao(decimal min)
    {
        string sql = "SELECT o.order_id, o.customer_id, o.employee_id, o.order_date, " +
                     "SUM(od.quantity * od.unit_price_at_sale) AS total " +
                     "FROM `Order` o JOIN OrderDetail od ON o.order_id = od.order_id " +
                     "WHERE o.order_status NOT IN ('DA_HUY', 'GIAO_THAT_BAI') " +
                     "GROUP BY o.order_id, o.customer_id, o.employee_id, o.order_date " +
                     "HAVING total > ?";
        return DBBase.Query(sql, min);
    }

    public List<Order> DonChuaGiaoHomNay()
    {
        var list = new List<Order>();
        string sql = "SELECT order_id, customer_id, employee_id, order_date, delivery_date, delivery_address, order_status FROM `Order` " +
                     "WHERE DATE(delivery_date) = CURDATE() " +
                     "AND order_status NOT IN ('HOAN_THANH', 'DA_HUY', 'GIAO_THAT_BAI')";

        try
        {
            using var conn = DBConnection.GetConnection();
            using var cmd = new MySqlCommand(sql, conn);
            using var reader = cmd.ExecuteReader();
            while (reader.Read())
            {
                list.Add(DocMotDon(reader));
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"[OrderDAO.DonChuaGiaoHomNay Error]: {ex.Message}");
        }
        return list;
    }

    public decimal DoanhThuHomNay()
    {
        string sql = "SELECT COALESCE(SUM(amount),0) FROM Payment " +
                     "WHERE DATE(payment_date) = CURDATE() " +
                     "AND payment_status = 'THANH_TOAN_THANH_CONG'";
        var rows = DBBase.Query(sql);
        if (rows.Count == 0 || rows[0][0] == null) return 0m;
        return Convert.ToDecimal(rows[0][0]);
    }

    public List<object[]> KhachDaTungDatHang()
    {
        string sql = "SELECT DISTINCT c.customer_id, c.name, c.phone, c.address, c.email " +
                     "FROM Customer c JOIN `Order` o ON c.customer_id = o.customer_id";
        return DBBase.Query(sql);
    }

    public List<object[]> KhachChuaTungDatHang()
    {
        string sql = "SELECT c.customer_id, c.name, c.phone, c.address, c.email " +
                     "FROM Customer c WHERE NOT EXISTS (" +
                     "SELECT 1 FROM `Order` o WHERE o.customer_id = c.customer_id)";
        return DBBase.Query(sql);
    }

    public List<object[]> KhachNhieuDonNhat()
    {
        string sql = "SELECT c.customer_id, c.name, COUNT(o.order_id) AS order_count " +
                     "FROM Customer c JOIN `Order` o ON c.customer_id = o.customer_id " +
                     "WHERE o.order_status NOT IN ('DA_HUY', 'GIAO_THAT_BAI') " +
                     "GROUP BY c.customer_id, c.name " +
                     "ORDER BY order_count DESC LIMIT 1";
        return DBBase.Query(sql);
    }

    public List<object[]> NhanVienCoDonHang()
    {
        string sql = "SELECT DISTINCT e.employee_id, e.name, e.role, e.phone, e.salary " +
                     "FROM Employee e JOIN `Order` o ON e.employee_id = o.employee_id";
        return DBBase.Query(sql);
    }

    public List<object[]> NhanVienNhieuDonNhat()
    {
        string sql = "SELECT e.employee_id, e.name, COUNT(o.order_id) AS order_count " +
                     "FROM Employee e JOIN `Order` o ON e.employee_id = o.employee_id " +
                     "WHERE o.order_status NOT IN ('DA_HUY', 'GIAO_THAT_BAI') " +
                     "GROUP BY e.employee_id, e.name " +
                     "ORDER BY order_count DESC LIMIT 1";
        return DBBase.Query(sql);
    }

    public List<object[]> TongChiTieuTheoKhach()
    {
        string sql = "SELECT c.customer_id, c.name, " +
                     "COALESCE(SUM(od.quantity * od.unit_price_at_sale),0) AS total_spent " +
                     "FROM Customer c " +
                     "LEFT JOIN `Order` o ON c.customer_id = o.customer_id " +
                     "    AND o.order_status NOT IN ('DA_HUY', 'GIAO_THAT_BAI') " +
                     "LEFT JOIN OrderDetail od ON o.order_id = od.order_id " +
                     "GROUP BY c.customer_id, c.name";
        return DBBase.Query(sql);
    }

    public List<Order> DonCuaKhach(string maKhach)
    {
        var list = new List<Order>();
        string sql = "SELECT order_id, customer_id, employee_id, order_date, delivery_date, delivery_address, order_status FROM `Order` WHERE customer_id = @id";

        try
        {
            using var conn = DBConnection.GetConnection();
            using var cmd = new MySqlCommand(sql, conn);
            cmd.Parameters.AddWithValue("@id", maKhach);
            using var reader = cmd.ExecuteReader();
            while (reader.Read())
            {
                list.Add(DocMotDon(reader));
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"[OrderDAO.DonCuaKhach Error]: {ex.Message}");
        }
        return list;
    }

    public List<object[]> ChiTietDonHang(string maDon)
    {
        string sql = "SELECT f.name AS flower_name, od.quantity, od.unit_price_at_sale " +
                     "FROM OrderDetail od JOIN Flower f ON od.flower_id = f.flower_id " +
                     "WHERE od.order_id = ?";
        return DBBase.Query(sql, maDon);
    }

    public List<object[]> ThongTinDayDuDonHang(string orderId)
    {
        string sql = "SELECT o.order_id, c.name AS ten_khach, c.phone AS sdt_khach, " +
                     "e.name AS ten_nv, o.order_date, o.delivery_date, " +
                     "o.delivery_address, o.order_status " +
                     "FROM `Order` o " +
                     "JOIN Customer c ON o.customer_id = c.customer_id " +
                     "JOIN Employee e ON o.employee_id = e.employee_id " +
                     "WHERE o.order_id = ?";
        return DBBase.Query(sql, orderId);
    }

    public List<object[]> DonCoNhieuLoaiHoa(int n)
    {
        string sql = "SELECT order_id, COUNT(DISTINCT flower_id) AS flower_count " +
                     "FROM OrderDetail GROUP BY order_id HAVING flower_count >= ?";
        return DBBase.Query(sql, n);
    }

    public List<object[]> ThongKeDonTheoTrangThai()
    {
        string sql = "SELECT order_status, COUNT(*) AS order_count " +
                     "FROM `Order` GROUP BY order_status";
        return DBBase.Query(sql);
    }

    private Order DocMotDon(MySqlDataReader reader)
    {
        return new Order
        {
            OrderId = reader.GetString(0),
            CustomerId = reader.IsDBNull(1) ? "" : reader.GetString(1),
            EmployeeId = reader.IsDBNull(2) ? "" : reader.GetString(2),
            OrderDate = reader.IsDBNull(3) ? DateTime.MinValue : reader.GetDateTime(3),
            DeliveryDate = reader.IsDBNull(4) ? DateTime.MinValue : reader.GetDateTime(4),
            DeliveryAddress = reader.IsDBNull(5) ? "" : reader.GetString(5),
            OrderStatus = reader.IsDBNull(6) ? "" : reader.GetString(6)
        };
    }
}
