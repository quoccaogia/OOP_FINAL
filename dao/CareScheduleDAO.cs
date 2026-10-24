using System;
using System.Collections.Generic;
using vn.edu.crud.model;
using MySqlConnector;
using vn.edu.crud.util;

namespace vn.edu.crud.dao;

public class CareScheduleDAO
{
    public List<object[]> FindAll()
    {
        string sql = "SELECT cs.care_id, cs.flower_id, f.name AS flower_name, e.name AS employee_name, " +
                     "cs.check_date, cs.freshness_level, cs.water_status, cs.action_taken, cs.note " +
                     "FROM careschedule cs " +
                     "LEFT JOIN flower f ON cs.flower_id = f.flower_id " +
                     "LEFT JOIN employee e ON cs.employee_id = e.employee_id " +
                     "ORDER BY cs.check_date DESC";
        return DBBase.Query(sql);
    }

    public List<object[]> TrangThaiHienTai()
    {
        string sql = "SELECT f.flower_id, f.name, f.color, f.stock_quantity, " +
                     "       cs.care_id, cs.check_date, cs.freshness_level, cs.water_status, cs.note " +
                     "FROM flower f " +
                     "LEFT JOIN careschedule cs ON cs.care_id = (" +
                     "    SELECT cs2.care_id FROM careschedule cs2 " +
                     "    WHERE cs2.flower_id = f.flower_id " +
                     "    ORDER BY cs2.check_date DESC, cs2.care_id DESC LIMIT 1) " +
                     "ORDER BY f.flower_id";
        return DBBase.Query(sql);
    }

    public bool CapNhatGhiChu(string careId, string note)
    {
        return DBBase.Update("UPDATE careschedule SET note = ? WHERE care_id = ?", note, careId) > 0;
    }

    public List<object[]> FindById(string id)
    {
        string sql = "SELECT * FROM careschedule WHERE care_id = ?";
        return DBBase.Query(sql, id);
    }

    public bool Insert(string id, string flowerId, string empId,
                       DateTime date, string fresh, string water,
                       string action, string note)
    {
        string sql = "INSERT INTO careschedule " +
                     "(care_id, flower_id, employee_id, check_date, freshness_level, " +
                     "water_status, action_taken, note) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        return DBBase.Update(sql, id, flowerId, empId, date, fresh, water, action, note) > 0;
    }

    public bool Update(string id, string fresh, string water, string action, string note)
    {
        string sql = "UPDATE careschedule SET freshness_level=?, water_status=?, " +
                     "action_taken=?, note=? WHERE care_id=?";
        return DBBase.Update(sql, fresh, water, action, note, id) > 0;
    }

    public bool Delete(string id)
    {
        return DBBase.Update("DELETE FROM careschedule WHERE care_id = ?", id) > 0;
    }

    public string SinhMaCSMoi()
    {
        var rows = DBBase.Query("SELECT care_id FROM careschedule ORDER BY care_id DESC LIMIT 1");
        if (rows.Count == 0 || rows[0][0] == null)
            return "CS001";
        string maCu = rows[0][0].ToString()!;
        int so = int.Parse(maCu.Substring(2));
        return $"CS{so + 1:D3}";
    }

    public List<object[]> LichChamSocCuaNV(string maNV)
    {
        string sql = "SELECT cs.care_id, cs.check_date, cs.freshness_level, cs.water_status, " +
                     "cs.action_taken, f.name AS flower_name " +
                     "FROM careschedule cs " +
                     "LEFT JOIN flower f ON cs.flower_id = f.flower_id " +
                     "WHERE cs.employee_id = ? " +
                     "ORDER BY cs.check_date DESC";
        return DBBase.Query(sql, maNV);
    }

    public List<object[]> LichChamSocCuaHoa(string flowerId)
    {
        string sql = "SELECT cs.care_id, cs.check_date, cs.freshness_level, cs.water_status, " +
                     "cs.action_taken, cs.note, e.name AS employee_name " +
                     "FROM careschedule cs " +
                     "LEFT JOIN employee e ON cs.employee_id = e.employee_id " +
                     "WHERE cs.flower_id = ? " +
                     "ORDER BY cs.check_date DESC";
        return DBBase.Query(sql, flowerId);
    }

    public List<CareSchedule> LayDanhSachChamSoc()
    {
        var list = new List<CareSchedule>();
        string sql = "SELECT care_id, flower_id, employee_id, check_date, freshness_level, water_status, action_taken, note FROM careschedule";
        try
        {
            using var conn = DBConnection.GetConnection();
            using var cmd = new MySqlCommand(sql, conn);
            using var reader = cmd.ExecuteReader();
            while (reader.Read())
            {
                list.Add(new CareSchedule
                {
                    CareId = reader.GetString(0),
                    FlowerId = reader.IsDBNull(1) ? "" : reader.GetString(1),
                    EmployeeId = reader.IsDBNull(2) ? "" : reader.GetString(2),
                    CheckDate = reader.IsDBNull(3) ? DateTime.MinValue : reader.GetDateTime(3),
                    FreshnessLevel = reader.IsDBNull(4) ? "" : reader.GetString(4),
                    WaterStatus = reader.IsDBNull(5) ? "" : reader.GetString(5),
                    ActionTaken = reader.IsDBNull(6) ? "" : reader.GetString(6),
                    Note = reader.IsDBNull(7) ? "" : reader.GetString(7)
                });
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"[CareScheduleDAO.LayDanhSachChamSoc Error]: {ex.Message}");
        }
        return list;
    }
}
