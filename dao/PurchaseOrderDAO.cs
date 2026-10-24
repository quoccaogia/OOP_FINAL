using System;
using System.Collections.Generic;
using vn.edu.crud.model;
using MySqlConnector;
using vn.edu.crud.util;

namespace vn.edu.crud.dao;

public class PurchaseOrderDAO
{
    public List<object[]> FindAll()
    {
        string sql = "SELECT purchase_order_id, supplier_id, employee_id, " +
                     "import_date, total_amount, status, note FROM PurchaseOrder";
        return DBBase.Query(sql);
    }

    public List<object[]> FindById(string id)
    {
        string sql = "SELECT * FROM PurchaseOrder WHERE purchase_order_id = ?";
        return DBBase.Query(sql, id);
    }

    public bool Insert(string id, string supId, string empId,
                       DateTime date, decimal total,
                       string status, string note)
    {
        string sql = "INSERT INTO PurchaseOrder " +
                     "(purchase_order_id, supplier_id, employee_id, import_date, " +
                     "total_amount, status, note) VALUES (?, ?, ?, ?, ?, ?, ?)";
        return DBBase.Update(sql, id, supId, empId, date, total, status, note) > 0;
    }

    public bool Update(string id, decimal total, string status, string note)
    {
        string sql = "UPDATE PurchaseOrder SET total_amount=?, status=?, note=? WHERE purchase_order_id=?";
        return DBBase.Update(sql, total, status, note, id) > 0;
    }

    public bool Delete(string id)
    {
        return DBBase.Update("DELETE FROM PurchaseOrder WHERE purchase_order_id = ?", id) > 0;
    }

    public string SinhMaPNMoi()
    {
        var rows = DBBase.Query("SELECT purchase_order_id FROM PurchaseOrder ORDER BY purchase_order_id DESC LIMIT 1");
        if (rows.Count == 0 || rows[0][0] == null)
            return "PN001";
        string maCu = rows[0][0].ToString()!;
        int so = int.Parse(maCu.Substring(2));
        return $"PN{so + 1:D3}";
    }

    public List<object[]> PhieuChoXuLy()
    {
        string sql = "SELECT po.purchase_order_id, po.import_date, s.name, COUNT(fb.batch_id) " +
                     "FROM PurchaseOrder po " +
                     "LEFT JOIN Supplier s ON po.supplier_id = s.supplier_id " +
                     "LEFT JOIN FlowerBatch fb ON po.purchase_order_id = fb.purchase_order_id " +
                     "WHERE po.status = 'CHO_XU_LY' " +
                     "GROUP BY po.purchase_order_id, po.import_date, s.name " +
                     "ORDER BY po.import_date";
        return DBBase.Query(sql);
    }

    public int DemLoCuaPhieu(string purchaseOrderId)
    {
        string sql = "SELECT COUNT(*) FROM FlowerBatch WHERE purchase_order_id = ?";
        var rows = DBBase.Query(sql, purchaseOrderId);
        if (rows.Count == 0 || rows[0][0] == null) return 0;
        return Convert.ToInt32(rows[0][0]);
    }

    public bool ChotPhieu(string purchaseOrderId)
    {
        var rows = DBBase.Query("SELECT COALESCE(SUM(quantity_imported * unit_cost), 0) FROM FlowerBatch WHERE purchase_order_id = ?", purchaseOrderId);
        if (rows.Count == 0 || rows[0][0] == null)
            return false;

        decimal tong = Convert.ToDecimal(rows[0][0]);
        if (tong <= 0m)
            return false;

        string sql = "UPDATE PurchaseOrder SET total_amount = ?, status = 'HOAN_THANH', " +
                     "note = CONCAT(COALESCE(note, ''), ' | Chốt bổ sung') " +
                     "WHERE purchase_order_id = ? AND status = 'CHO_XU_LY'";
        return DBBase.Update(sql, tong, purchaseOrderId) > 0;
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

    public List<object[]> PhieuNhapCuaNCC(string maNCC)
    {
        string sql = "SELECT purchase_order_id, import_date, total_amount, status, note FROM PurchaseOrder WHERE supplier_id = ?";
        return DBBase.Query(sql, maNCC);
    }

    public List<object[]> TatCaPhieuNhap()
    {
        string sql = "SELECT po.purchase_order_id, s.name AS supplier_name, " +
                     "e.name AS employee_name, po.import_date, po.total_amount, " +
                     "po.status, po.note " +
                     "FROM PurchaseOrder po " +
                     "JOIN Supplier s ON po.supplier_id = s.supplier_id " +
                     "JOIN Employee e ON po.employee_id = e.employee_id " +
                     "ORDER BY po.import_date DESC";
        return DBBase.Query(sql);
    }

    public List<PurchaseOrder> LayDanhSachPhieuNhap()
    {
        var list = new List<PurchaseOrder>();
        string sql = "SELECT purchase_order_id, supplier_id, employee_id, import_date, total_amount, status, note FROM purchaseorder";
        try
        {
            using var conn = DBConnection.GetConnection();
            using var cmd = new MySqlCommand(sql, conn);
            using var reader = cmd.ExecuteReader();
            while (reader.Read())
            {
                list.Add(new PurchaseOrder
                {
                    PurchaseOrderId = reader.GetString(0),
                    SupplierId = reader.IsDBNull(1) ? "" : reader.GetString(1),
                    EmployeeId = reader.IsDBNull(2) ? "" : reader.GetString(2),
                    ImportDate = reader.IsDBNull(3) ? DateTime.MinValue : reader.GetDateTime(3),
                    TotalAmount = reader.IsDBNull(4) ? 0m : reader.GetDecimal(4),
                    Status = reader.IsDBNull(5) ? "" : reader.GetString(5),
                    Note = reader.IsDBNull(6) ? "" : reader.GetString(6)
                });
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"[PurchaseOrderDAO.LayDanhSachPhieuNhap Error]: {ex.Message}");
        }
        return list;
    }
}
