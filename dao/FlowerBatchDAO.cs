using System;
using System.Collections.Generic;
using vn.edu.crud.model;
using MySqlConnector;
using vn.edu.crud.util;

namespace vn.edu.crud.dao;

public class FlowerBatchDAO
{
    public List<object[]> FindAll()
    {
        string sql = "SELECT fb.batch_id, fb.purchase_order_id, s.name AS supplier_name, " +
                     "fb.flower_id, f.name AS flower_name, " +
                     "fb.quantity_imported, fb.quantity_remaining, fb.unit_cost, " +
                     "fb.import_date, fb.expiry_date, fb.status, fb.note " +
                     "FROM flowerbatch fb " +
                     "LEFT JOIN purchaseorder po ON fb.purchase_order_id = po.purchase_order_id " +
                     "LEFT JOIN supplier s ON po.supplier_id = s.supplier_id " +
                     "LEFT JOIN flower f ON fb.flower_id = f.flower_id " +
                     "ORDER BY fb.batch_id DESC";
        return DBBase.Query(sql);
    }

    public List<object[]> FindById(string id)
    {
        string sql = "SELECT * FROM flowerbatch WHERE batch_id = ?";
        return DBBase.Query(sql, id);
    }

    public bool Insert(string id, string poId, string flowerId,
                       int imported, int remaining, decimal cost,
                       DateTime importDate, DateTime expiry,
                       string status, string note)
    {
        string sql = "INSERT INTO flowerbatch " +
                     "(batch_id, purchase_order_id, flower_id, quantity_imported, " +
                     "quantity_remaining, unit_cost, import_date, expiry_date, status, note) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return DBBase.Update(sql, id, poId, flowerId, imported, remaining,
            cost, importDate, expiry.Date, status, note) > 0;
    }

    public bool UpdateRemaining(string batchId, int remaining)
    {
        string sql = "UPDATE flowerbatch SET quantity_remaining = ? WHERE batch_id = ?";
        return DBBase.Update(sql, remaining, batchId) > 0;
    }

    public bool UpdateStatus(string batchId, string status)
    {
        string sql = "UPDATE flowerbatch SET status = ? WHERE batch_id = ?";
        return DBBase.Update(sql, status, batchId) > 0;
    }

    public bool CoLo(string flowerId)
    {
        return DBBase.Query("SELECT 1 FROM flowerbatch WHERE flower_id = ? LIMIT 1", flowerId).Count > 0;
    }

    public List<object[]> LoAvailableCuaHoa(string flowerId)
    {
        return DBBase.Query(
            "SELECT batch_id, quantity_remaining, expiry_date FROM flowerbatch " +
            "WHERE flower_id = ? AND status = 'AVAILABLE' AND quantity_remaining > 0 " +
            "ORDER BY expiry_date",
            flowerId);
    }

    public int XuatKho(string flowerId, int soLuong)
    {
        if (soLuong <= 0) return 0;

        var dsLo = DBBase.Query(
            "SELECT batch_id, quantity_remaining FROM flowerbatch " +
            "WHERE flower_id = ? AND status = 'AVAILABLE' AND quantity_remaining > 0 " +
            "ORDER BY expiry_date ASC, batch_id ASC",
            flowerId);

        int conLai = soLuong;
        foreach (var lo in dsLo)
        {
            if (conLai <= 0) break;
            string batchId = lo[0]?.ToString() ?? "";
            int con = lo[1] == null ? 0 : Convert.ToInt32(lo[1]);
            int lay = Math.Min(conLai, con);
            int moi = con - lay;
            DBBase.Update(
                "UPDATE flowerbatch SET quantity_remaining = ?, status = ? WHERE batch_id = ?",
                moi, moi == 0 ? "SOLD_OUT" : "AVAILABLE", batchId);
            conLai -= lay;
        }
        return soLuong - conLai;
    }

    public bool NhapLaiKho(string flowerId, int soLuong)
    {
        if (soLuong <= 0) return false;

        var dsLo = DBBase.Query(
            "SELECT batch_id FROM flowerbatch " +
            "WHERE flower_id = ? AND status IN ('AVAILABLE', 'SOLD_OUT') " +
            "ORDER BY (status = 'AVAILABLE') DESC, expiry_date DESC, batch_id DESC " +
            "LIMIT 1",
            flowerId);

        if (dsLo.Count == 0) return false;

        string batchId = dsLo[0][0]?.ToString() ?? "";
        return DBBase.Update(
            "UPDATE flowerbatch SET quantity_remaining = quantity_remaining + ?, status = 'AVAILABLE' WHERE batch_id = ?",
            soLuong, batchId) > 0;
    }

    public bool DoiTrangThaiLo(string batchId, string trangThaiMoi)
    {
        var rows = DBBase.Query(
            "SELECT flower_id, quantity_remaining, status FROM flowerbatch WHERE batch_id = ?",
            batchId);
        if (rows.Count == 0) return false;

        string flowerId = rows[0][0]?.ToString() ?? "";
        int con = rows[0][1] == null ? 0 : Convert.ToInt32(rows[0][1]);
        string trangThaiCu = rows[0][2]?.ToString() ?? "";

        bool ok = DBBase.Update(
            "UPDATE flowerbatch SET status = ? WHERE batch_id = ?",
            trangThaiMoi, batchId) > 0;
        if (!ok) return false;

        bool cuAvailable = "AVAILABLE".Equals(trangThaiCu, StringComparison.OrdinalIgnoreCase);
        bool moiAvailable = "AVAILABLE".Equals(trangThaiMoi, StringComparison.OrdinalIgnoreCase);
        int delta = 0;
        if (cuAvailable && !moiAvailable) delta = -con;
        else if (!cuAvailable && moiAvailable) delta = con;

        if (delta != 0 && !string.IsNullOrEmpty(flowerId))
        {
            DBBase.Update(
                "UPDATE flower SET stock_quantity = stock_quantity + ? WHERE flower_id = ?",
                delta, flowerId);
        }
        return true;
    }

    public bool ThanhLyLo(string batchId)
    {
        var rows = DBBase.Query(
            "SELECT flower_id, quantity_remaining, status, note FROM flowerbatch WHERE batch_id = ?",
            batchId);
        if (rows.Count == 0) return false;

        string flowerId = rows[0][0]?.ToString() ?? "";
        int con = rows[0][1] == null ? 0 : Convert.ToInt32(rows[0][1]);
        string trangThaiCu = rows[0][2]?.ToString() ?? "";
        string noteCu = rows[0][3]?.ToString() ?? "";

        string noteMoi = noteCu + (string.IsNullOrEmpty(noteCu) ? "" : " | ") + $"Thanh lý {con} bông";
        bool ok = DBBase.Update(
            "UPDATE flowerbatch SET status = 'EXPIRED', quantity_remaining = 0, note = ? WHERE batch_id = ?",
            noteMoi, batchId) > 0;
        if (!ok) return false;

        if ("AVAILABLE".Equals(trangThaiCu, StringComparison.OrdinalIgnoreCase) && con > 0 && !string.IsNullOrEmpty(flowerId))
        {
            DBBase.Update(
                "UPDATE flower SET stock_quantity = stock_quantity - ? WHERE flower_id = ?",
                con, flowerId);
        }
        return true;
    }

    public bool Delete(string id)
    {
        return DBBase.Update("DELETE FROM flowerbatch WHERE batch_id = ?", id) > 0;
    }

    public string SinhMaLoMoi()
    {
        var rows = DBBase.Query("SELECT batch_id FROM flowerbatch ORDER BY batch_id DESC LIMIT 1");
        if (rows.Count == 0 || rows[0][0] == null)
            return "L001";
        string maCu = rows[0][0].ToString()!;
        int so = int.Parse(maCu.Substring(1));
        return $"L{so + 1:D3}";
    }

    public List<object[]> LoHoaConHang()
    {
        string sql = "SELECT fb.batch_id, f.name AS flower_name, s.name AS supplier_name, " +
                     "fb.quantity_remaining, fb.expiry_date " +
                     "FROM flowerbatch fb " +
                     "JOIN flower f ON fb.flower_id = f.flower_id " +
                     "JOIN purchaseorder po ON fb.purchase_order_id = po.purchase_order_id " +
                     "JOIN supplier s ON po.supplier_id = s.supplier_id " +
                     "WHERE fb.status = 'AVAILABLE' AND fb.quantity_remaining > 0 " +
                     "ORDER BY fb.expiry_date";
        return DBBase.Query(sql);
    }

    public List<object[]> LoHoaSapHetHan(int soNgay)
    {
        string sql = "SELECT fb.batch_id, f.name AS flower_name, " +
                     "fb.quantity_remaining, fb.expiry_date, DATEDIFF(fb.expiry_date, CURDATE()) AS days_left " +
                     "FROM flowerbatch fb JOIN flower f ON fb.flower_id = f.flower_id " +
                     "WHERE fb.status = 'AVAILABLE' AND fb.quantity_remaining > 0 " +
                     "AND DATEDIFF(fb.expiry_date, CURDATE()) BETWEEN 0 AND ? " +
                     "ORDER BY fb.expiry_date";
        return DBBase.Query(sql, soNgay);
    }

    public List<FlowerBatch> LayDanhSachLo()
    {
        var list = new List<FlowerBatch>();
        string sql = "SELECT batch_id, purchase_order_id, flower_id, quantity_imported, quantity_remaining, unit_cost, import_date, expiry_date, status, note FROM flowerbatch";
        try
        {
            using var conn = DBConnection.GetConnection();
            using var cmd = new MySqlCommand(sql, conn);
            using var reader = cmd.ExecuteReader();
            while (reader.Read())
            {
                list.Add(new FlowerBatch
                {
                    BatchId = reader.GetString(0),
                    PurchaseOrderId = reader.IsDBNull(1) ? "" : reader.GetString(1),
                    FlowerId = reader.IsDBNull(2) ? "" : reader.GetString(2),
                    QuantityImported = reader.IsDBNull(3) ? 0 : reader.GetInt32(3),
                    QuantityRemaining = reader.IsDBNull(4) ? 0 : reader.GetInt32(4),
                    UnitCost = reader.IsDBNull(5) ? 0m : reader.GetDecimal(5),
                    ImportDate = reader.IsDBNull(6) ? DateTime.MinValue : reader.GetDateTime(6),
                    ExpiryDate = reader.IsDBNull(7) ? DateTime.MinValue : reader.GetDateTime(7),
                    Status = reader.IsDBNull(8) ? "" : reader.GetString(8),
                    Note = reader.IsDBNull(9) ? "" : reader.GetString(9)
                });
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"[FlowerBatchDAO.LayDanhSachLo Error]: {ex.Message}");
        }
        return list;
    }
}
