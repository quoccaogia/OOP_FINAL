using System;
using System.Collections.Generic;
using vn.edu.crud.model;
using MySqlConnector;
using vn.edu.crud.util;

namespace vn.edu.crud.dao;

public class SupplierDAO
{
    public List<Supplier> FindAll()
    {
        var list = new List<Supplier>();
        string sql = "SELECT supplier_id, name, phone, address, representative FROM Supplier";
        try
        {
            using var conn = DBConnection.GetConnection();
            using var cmd = new MySqlCommand(sql, conn);
            using var reader = cmd.ExecuteReader();
            while (reader.Read())
            {
                list.Add(new Supplier
                {
                    SupplierId = reader.GetString(0),
                    Name = reader.IsDBNull(1) ? "" : reader.GetString(1),
                    Phone = reader.IsDBNull(2) ? "" : reader.GetString(2),
                    Address = reader.IsDBNull(3) ? "" : reader.GetString(3),
                    Representative = reader.IsDBNull(4) ? "" : reader.GetString(4)
                });
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"[SupplierDAO.FindAll Error]: {ex.Message}");
        }
        return list;
    }

    public Supplier? FindById(string id)
    {
        string sql = "SELECT supplier_id, name, phone, address, representative FROM Supplier WHERE supplier_id = @id";
        try
        {
            using var conn = DBConnection.GetConnection();
            using var cmd = new MySqlCommand(sql, conn);
            cmd.Parameters.AddWithValue("@id", id);
            using var reader = cmd.ExecuteReader();
            if (reader.Read())
            {
                return new Supplier
                {
                    SupplierId = reader.GetString(0),
                    Name = reader.IsDBNull(1) ? "" : reader.GetString(1),
                    Phone = reader.IsDBNull(2) ? "" : reader.GetString(2),
                    Address = reader.IsDBNull(3) ? "" : reader.GetString(3),
                    Representative = reader.IsDBNull(4) ? "" : reader.GetString(4)
                };
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"[SupplierDAO.FindById Error]: {ex.Message}");
        }
        return null;
    }

    public List<Supplier> TimTheoSdt(string sdt)
    {
        var list = new List<Supplier>();
        string sql = "SELECT supplier_id, name, phone, address, representative FROM Supplier WHERE phone LIKE @phone";
        try
        {
            using var conn = DBConnection.GetConnection();
            using var cmd = new MySqlCommand(sql, conn);
            cmd.Parameters.AddWithValue("@phone", "%" + sdt + "%");
            using var reader = cmd.ExecuteReader();
            while (reader.Read())
            {
                list.Add(new Supplier
                {
                    SupplierId = reader.GetString(0),
                    Name = reader.IsDBNull(1) ? "" : reader.GetString(1),
                    Phone = reader.IsDBNull(2) ? "" : reader.GetString(2),
                    Address = reader.IsDBNull(3) ? "" : reader.GetString(3),
                    Representative = reader.IsDBNull(4) ? "" : reader.GetString(4)
                });
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"[SupplierDAO.TimTheoSdt Error]: {ex.Message}");
        }
        return list;
    }

    public bool Insert(Supplier s)
    {
        string sql = "INSERT INTO Supplier (supplier_id, name, phone, address, representative) VALUES (?, ?, ?, ?, ?)";
        return DBBase.Update(sql, s.SupplierId, s.Name, s.Phone, s.Address, s.Representative) > 0;
    }

    public bool Update(Supplier s)
    {
        string sql = "UPDATE Supplier SET name=?, phone=?, address=?, representative=? WHERE supplier_id=?";
        return DBBase.Update(sql, s.Name, s.Phone, s.Address, s.Representative, s.SupplierId) > 0;
    }

    public bool Delete(string id)
    {
        return DBBase.Update("DELETE FROM Supplier WHERE supplier_id = ?", id) > 0;
    }

    public bool CoHoa(string supplierId)
    {
        return DBBase.Query("SELECT 1 FROM Flower WHERE supplier_id = ? LIMIT 1", supplierId).Count > 0;
    }

    public string SinhMaNCCMoi()
    {
        var rows = DBBase.Query("SELECT supplier_id FROM Supplier ORDER BY supplier_id DESC LIMIT 1");
        if (rows.Count == 0 || rows[0][0] == null) return "NCC01";
        string maCu = rows[0][0].ToString()!;
        int so = int.Parse(maCu.Substring(3));
        return $"NCC{so + 1:D2}";
    }

    public string ChuanHoaSdt(string sdt)
    {
        if (string.IsNullOrWhiteSpace(sdt)) return "";
        string s = sdt.Trim().Replace(" ", "");
        if (s.StartsWith("+84")) s = "0" + s.Substring(3);
        while (s.StartsWith("00")) s = s.Substring(1);
        return s;
    }

    public List<object[]> TongNhapTheoNCC()
    {
        string sql = "SELECT s.supplier_id, s.name, " +
                     "COALESCE(SUM(fb.quantity_imported * fb.unit_cost),0) AS total_import " +
                     "FROM Supplier s " +
                     "LEFT JOIN PurchaseOrder po ON s.supplier_id = po.supplier_id " +
                     "LEFT JOIN FlowerBatch fb ON po.purchase_order_id = fb.purchase_order_id " +
                     "GROUP BY s.supplier_id, s.name";
        return DBBase.Query(sql);
    }

    public List<object[]> NccDangCungCapHoa()
    {
        string sql = "SELECT DISTINCT s.supplier_id, s.name, s.phone, s.address, s.representative " +
                     "FROM Supplier s JOIN Flower f ON s.supplier_id = f.supplier_id";
        return DBBase.Query(sql);
    }

    public List<object[]> NccNhieuLoaiHoaNhat()
    {
        string sql = "SELECT s.supplier_id, s.name, COUNT(f.flower_id) AS flower_count " +
                     "FROM Supplier s JOIN Flower f ON s.supplier_id = f.supplier_id " +
                     "GROUP BY s.supplier_id, s.name " +
                     "ORDER BY flower_count DESC LIMIT 1";
        return DBBase.Query(sql);
    }

    public List<object[]> ThongKeLoaiHoaTheoNCC()
    {
        string sql = "SELECT s.supplier_id, s.name, COUNT(f.flower_id) AS flower_count " +
                     "FROM Supplier s LEFT JOIN Flower f ON s.supplier_id = f.supplier_id " +
                     "GROUP BY s.supplier_id, s.name";
        return DBBase.Query(sql);
    }
}
