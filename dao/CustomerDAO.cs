using System;
using System.Collections.Generic;
using vn.edu.crud.model;
using MySqlConnector;
using vn.edu.crud.util;

namespace vn.edu.crud.dao;

public class CustomerDAO
{
    public List<Customer> FindAll()
    {
        var list = new List<Customer>();
        string sql = "SELECT customer_id, name, phone, address, email FROM Customer";
        try
        {
            using var conn = DBConnection.GetConnection();
            using var cmd = new MySqlCommand(sql, conn);
            using var reader = cmd.ExecuteReader();
            while (reader.Read())
            {
                list.Add(new Customer
                {
                    CustomerId = reader.GetString(0),
                    Name = reader.IsDBNull(1) ? "" : reader.GetString(1),
                    Phone = reader.IsDBNull(2) ? "" : reader.GetString(2),
                    Address = reader.IsDBNull(3) ? "" : reader.GetString(3),
                    Email = reader.IsDBNull(4) ? "" : reader.GetString(4)
                });
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"[CustomerDAO.FindAll Error]: {ex.Message}");
        }
        return list;
    }

    public Customer? FindById(string id)
    {
        string sql = "SELECT customer_id, name, phone, address, email FROM Customer WHERE customer_id = @id";
        try
        {
            using var conn = DBConnection.GetConnection();
            using var cmd = new MySqlCommand(sql, conn);
            cmd.Parameters.AddWithValue("@id", id);
            using var reader = cmd.ExecuteReader();
            if (reader.Read())
            {
                return new Customer
                {
                    CustomerId = reader.GetString(0),
                    Name = reader.IsDBNull(1) ? "" : reader.GetString(1),
                    Phone = reader.IsDBNull(2) ? "" : reader.GetString(2),
                    Address = reader.IsDBNull(3) ? "" : reader.GetString(3),
                    Email = reader.IsDBNull(4) ? "" : reader.GetString(4)
                };
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"[CustomerDAO.FindById Error]: {ex.Message}");
        }
        return null;
    }

    public List<Customer> TimTheoSdt(string sdt)
    {
        var list = new List<Customer>();
        string sql = "SELECT customer_id, name, phone, address, email FROM Customer WHERE phone LIKE @phone";
        try
        {
            using var conn = DBConnection.GetConnection();
            using var cmd = new MySqlCommand(sql, conn);
            cmd.Parameters.AddWithValue("@phone", "%" + sdt + "%");
            using var reader = cmd.ExecuteReader();
            while (reader.Read())
            {
                list.Add(new Customer
                {
                    CustomerId = reader.GetString(0),
                    Name = reader.IsDBNull(1) ? "" : reader.GetString(1),
                    Phone = reader.IsDBNull(2) ? "" : reader.GetString(2),
                    Address = reader.IsDBNull(3) ? "" : reader.GetString(3),
                    Email = reader.IsDBNull(4) ? "" : reader.GetString(4)
                });
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"[CustomerDAO.TimTheoSdt Error]: {ex.Message}");
        }
        return list;
    }

    public bool Insert(Customer c)
    {
        string sql = "INSERT INTO Customer (customer_id, name, phone, address, email) VALUES (?, ?, ?, ?, ?)";
        return DBBase.Update(sql, c.CustomerId, c.Name, c.Phone, c.Address, c.Email) > 0;
    }

    public bool Update(Customer c)
    {
        string sql = "UPDATE Customer SET name=?, phone=?, address=?, email=? WHERE customer_id=?";
        return DBBase.Update(sql, c.Name, c.Phone, c.Address, c.Email, c.CustomerId) > 0;
    }

    public string SinhMaKhachMoi()
    {
        var rows = DBBase.Query("SELECT customer_id FROM Customer ORDER BY customer_id DESC LIMIT 1");
        if (rows.Count == 0 || rows[0][0] == null) return "KH001";
        string maCu = rows[0][0].ToString()!;
        int so = int.Parse(maCu.Substring(2));
        return $"KH{so + 1:D3}";
    }

    public string ChuanHoaSdt(string sdt)
    {
        if (string.IsNullOrWhiteSpace(sdt)) return "";
        string s = sdt.Trim().Replace(" ", "");
        if (s.StartsWith("+84")) s = "0" + s.Substring(3);
        while (s.StartsWith("00")) s = s.Substring(1);
        return s;
    }
}
