using System;
using System.Collections.Generic;
using vn.edu.crud.model;
using MySqlConnector;
using vn.edu.crud.util;

namespace vn.edu.crud.dao;

public class EmployeeDAO
{
    public List<Employee> FindAll()
    {
        var list = new List<Employee>();
        string sql = "SELECT employee_id, name, role, phone, salary FROM Employee";
        try
        {
            using var conn = DBConnection.GetConnection();
            using var cmd = new MySqlCommand(sql, conn);
            using var reader = cmd.ExecuteReader();
            while (reader.Read())
            {
                list.Add(new Employee
                {
                    EmployeeId = reader.GetString(0),
                    Name = reader.IsDBNull(1) ? "" : reader.GetString(1),
                    Role = reader.IsDBNull(2) ? "" : reader.GetString(2),
                    Phone = reader.IsDBNull(3) ? "" : reader.GetString(3),
                    Salary = reader.IsDBNull(4) ? 0 : reader.GetDecimal(4)
                });
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"[EmployeeDAO.FindAll Error]: {ex.Message}");
        }
        return list;
    }

    public Employee? FindById(string id)
    {
        string sql = "SELECT employee_id, name, role, phone, salary FROM Employee WHERE employee_id = @id";
        try
        {
            using var conn = DBConnection.GetConnection();
            using var cmd = new MySqlCommand(sql, conn);
            cmd.Parameters.AddWithValue("@id", id);
            using var reader = cmd.ExecuteReader();
            if (reader.Read())
            {
                return new Employee
                {
                    EmployeeId = reader.GetString(0),
                    Name = reader.IsDBNull(1) ? "" : reader.GetString(1),
                    Role = reader.IsDBNull(2) ? "" : reader.GetString(2),
                    Phone = reader.IsDBNull(3) ? "" : reader.GetString(3),
                    Salary = reader.IsDBNull(4) ? 0 : reader.GetDecimal(4)
                };
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"[EmployeeDAO.FindById Error]: {ex.Message}");
        }
        return null;
    }

    public bool Insert(Employee e)
    {
        string sql = "INSERT INTO Employee (employee_id, name, role, phone, salary) VALUES (?, ?, ?, ?, ?)";
        return DBBase.Update(sql, e.EmployeeId, e.Name, e.Role, e.Phone, e.Salary) > 0;
    }

    public bool Update(Employee e)
    {
        string sql = "UPDATE Employee SET name=?, role=?, phone=?, salary=? WHERE employee_id=?";
        return DBBase.Update(sql, e.Name, e.Role, e.Phone, e.Salary, e.EmployeeId) > 0;
    }

    public bool Delete(string id)
    {
        return DBBase.Update("DELETE FROM Employee WHERE employee_id = ?", id) > 0;
    }

    public string SinhMaNVMoi()
    {
        var rows = DBBase.Query("SELECT employee_id FROM Employee ORDER BY employee_id DESC LIMIT 1");
        if (rows.Count == 0 || rows[0][0] == null) return "NV01";
        string maCu = rows[0][0].ToString()!;
        int so = int.Parse(maCu.Substring(2));
        return $"NV{so + 1:D2}";
    }

    public bool KiemTraNhanVienCoDon(string employeeId)
    {
        return DBBase.Query("SELECT 1 FROM `Order` WHERE employee_id = ? LIMIT 1", employeeId).Count > 0;
    }

    public decimal TinhDoanhThuNhanVien(string employeeId)
    {
        string sql = "SELECT COALESCE(SUM(od.quantity * od.unit_price_at_sale), 0) " +
                     "FROM `Order` o JOIN OrderDetail od ON o.order_id = od.order_id " +
                     "WHERE o.employee_id = ? AND o.order_status = 'HOAN_THANH'";
        var rows = DBBase.Query(sql, employeeId);
        if (rows.Count == 0 || rows[0][0] == null) return 0;
        return Convert.ToDecimal(rows[0][0]);
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
                     "GROUP BY e.employee_id, e.name ORDER BY order_count DESC LIMIT 1";
        return DBBase.Query(sql);
    }
}
