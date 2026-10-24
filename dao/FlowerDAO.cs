using System;
using System.Collections.Generic;
using vn.edu.crud.model;
using MySqlConnector;
using vn.edu.crud.util;

namespace vn.edu.crud.dao;

public class FlowerDAO
{
    public List<Flower> FindAll()
    {
        var list = new List<Flower>();
        string sql = "SELECT flower_id, name, category, unit_price, stock_quantity, color, supplier_id FROM flower";
        try
        {
            using var conn = DBConnection.GetConnection();
            using var cmd = new MySqlCommand(sql, conn);
            using var reader = cmd.ExecuteReader();
            while (reader.Read())
            {
                list.Add(new Flower
                {
                    FlowerId = reader.GetString(0),
                    Name = reader.IsDBNull(1) ? "" : reader.GetString(1),
                    Category = reader.IsDBNull(2) ? "" : reader.GetString(2),
                    UnitPrice = reader.IsDBNull(3) ? 0 : reader.GetDecimal(3),
                    StockQuantity = reader.IsDBNull(4) ? 0 : reader.GetInt32(4),
                    Color = reader.IsDBNull(5) ? "" : reader.GetString(5),
                    SupplierId = reader.IsDBNull(6) ? "" : reader.GetString(6)
                });
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"[FlowerDAO.FindAll Error]: {ex.Message}");
        }
        return list;
    }

    public Flower? TimTheoMa(string maHoa)
    {
        string sql = "SELECT flower_id, name, category, unit_price, stock_quantity, color, supplier_id FROM flower WHERE flower_id = @id";
        try
        {
            using var conn = DBConnection.GetConnection();
            using var cmd = new MySqlCommand(sql, conn);
            cmd.Parameters.AddWithValue("@id", maHoa);
            using var reader = cmd.ExecuteReader();
            if (reader.Read())
            {
                return new Flower
                {
                    FlowerId = reader.GetString(0),
                    Name = reader.IsDBNull(1) ? "" : reader.GetString(1),
                    Category = reader.IsDBNull(2) ? "" : reader.GetString(2),
                    UnitPrice = reader.IsDBNull(3) ? 0 : reader.GetDecimal(3),
                    StockQuantity = reader.IsDBNull(4) ? 0 : reader.GetInt32(4),
                    Color = reader.IsDBNull(5) ? "" : reader.GetString(5),
                    SupplierId = reader.IsDBNull(6) ? "" : reader.GetString(6)
                };
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"[FlowerDAO.TimTheoMa Error]: {ex.Message}");
        }
        return null;
    }

    public bool Insert(string id, string name, string cat, decimal price, int stock, string color, string supId)
    {
        string sql = "INSERT INTO flower (flower_id, name, category, unit_price, stock_quantity, color, supplier_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        return DBBase.Update(sql, id, name, cat, price, stock, color, supId) > 0;
    }

    public bool Update(string id, string name, string cat, decimal price, int stock, string color, string supId)
    {
        string sql = "UPDATE flower SET name=?, category=?, unit_price=?, stock_quantity=?, color=?, supplier_id=? WHERE flower_id=?";
        return DBBase.Update(sql, name, cat, price, stock, color, supId, id) > 0;
    }

    public bool Delete(string id)
    {
        return DBBase.Update("DELETE FROM flower WHERE flower_id = ?", id) > 0;
    }

    public string SinhMaHoaMoi()
    {
        var rows = DBBase.Query("SELECT flower_id FROM flower ORDER BY flower_id DESC LIMIT 1");
        if (rows.Count == 0 || rows[0][0] == null) return "FL01";
        string maCu = rows[0][0].ToString()!;
        int so = int.Parse(maCu.Substring(2));
        return $"FL{so + 1:D2}";
    }

    public bool CongTonKho(string flowerId, int soLuong)
    {
        string sql = "UPDATE flower SET stock_quantity = stock_quantity + ? WHERE flower_id = ?";
        return DBBase.Update(sql, soLuong, flowerId) > 0;
    }

    public int DongBoTonKhoTuLo()
    {
        string sql = "UPDATE flower f SET stock_quantity = " +
                     "(SELECT COALESCE(SUM(fb.quantity_remaining), 0) FROM flowerbatch fb " +
                     " WHERE fb.flower_id = f.flower_id AND fb.status = 'AVAILABLE') " +
                     "WHERE EXISTS (SELECT 1 FROM flowerbatch fb2 WHERE fb2.flower_id = f.flower_id)";
        return DBBase.Update(sql);
    }

    public List<Flower> TimTheoMau(string mau)
    {
        var list = new List<Flower>();
        string sql = "SELECT flower_id, name, category, unit_price, stock_quantity, color, supplier_id FROM flower WHERE color = ?";
        var rows = DBBase.Query(sql, mau);
        foreach (var r in rows)
        {
            list.Add(new Flower
            {
                FlowerId = r[0]?.ToString() ?? "",
                Name = r[1]?.ToString() ?? "",
                Category = r[2]?.ToString() ?? "",
                UnitPrice = r[3] == null ? 0 : Convert.ToDecimal(r[3]),
                StockQuantity = r[4] == null ? 0 : Convert.ToInt32(r[4]),
                Color = r[5]?.ToString() ?? "",
                SupplierId = r[6]?.ToString() ?? ""
            });
        }
        return list;
    }

    public List<object[]> HoaBanChay()
    {
        string sql = "SELECT f.flower_id, f.name, SUM(od.quantity) AS total_sold " +
                     "FROM flower f JOIN orderdetail od ON f.flower_id = od.flower_id " +
                     "JOIN `Order` o ON od.order_id = o.order_id " +
                     "WHERE o.order_status NOT IN ('DA_HUY', 'GIAO_THAT_BAI') " +
                     "GROUP BY f.flower_id, f.name ORDER BY total_sold DESC";
        return DBBase.Query(sql);
    }

    public List<object[]> HoaBanCham()
    {
        string sql = "SELECT f.flower_id, f.name, COALESCE(SUM(od.quantity),0) AS total_sold " +
                     "FROM flower f LEFT JOIN orderdetail od ON f.flower_id = od.flower_id " +
                     "    AND od.order_id IN (SELECT order_id FROM `Order` " +
                     "                        WHERE order_status NOT IN ('DA_HUY', 'GIAO_THAT_BAI')) " +
                     "GROUP BY f.flower_id, f.name ORDER BY total_sold ASC";
        return DBBase.Query(sql);
    }

    public List<object[]> TongBanTheoHoa()
    {
        string sql = "SELECT f.flower_id, f.name, COALESCE(SUM(od.quantity),0) AS total_sold " +
                     "FROM flower f LEFT JOIN orderdetail od ON f.flower_id = od.flower_id " +
                     "    AND od.order_id IN (SELECT order_id FROM `Order` " +
                     "                        WHERE order_status NOT IN ('DA_HUY', 'GIAO_THAT_BAI')) " +
                     "GROUP BY f.flower_id, f.name";
        return DBBase.Query(sql);
    }

    public List<Flower> HoaChuaBanDuoc()
    {
        var list = new List<Flower>();
        string sql = "SELECT flower_id, name, category, unit_price, stock_quantity, color, supplier_id FROM flower f WHERE NOT EXISTS (" +
                     "SELECT 1 FROM orderdetail od JOIN `Order` o ON od.order_id = o.order_id " +
                     "WHERE od.flower_id = f.flower_id " +
                     "AND o.order_status NOT IN ('DA_HUY', 'GIAO_THAT_BAI'))";
        var rows = DBBase.Query(sql);
        foreach (var r in rows)
        {
            list.Add(new Flower
            {
                FlowerId = r[0]?.ToString() ?? "",
                Name = r[1]?.ToString() ?? "",
                Category = r[2]?.ToString() ?? "",
                UnitPrice = r[3] == null ? 0 : Convert.ToDecimal(r[3]),
                StockQuantity = r[4] == null ? 0 : Convert.ToInt32(r[4]),
                Color = r[5]?.ToString() ?? "",
                SupplierId = r[6]?.ToString() ?? ""
            });
        }
        return list;
    }

    public List<object[]> LoHoaTonLau(int soNgay)
    {
        string sql = "SELECT f.flower_id, f.name, fb.batch_id, fb.import_date, " +
                     "DATEDIFF(NOW(), fb.import_date) AS days_in_stock " +
                     "FROM flower f JOIN flowerbatch fb ON f.flower_id = fb.flower_id " +
                     "WHERE fb.quantity_remaining > 0 " +
                     "AND DATEDIFF(NOW(), fb.import_date) > ?";
        return DBBase.Query(sql, soNgay);
    }
}
