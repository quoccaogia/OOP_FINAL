using System;
using System.Collections.Generic;
using MySqlConnector;

namespace vn.edu.crud.util;

public static class DBBase
{
    public static List<object[]> Query(string sql, params object[] parameters)
    {
        var result = new List<object[]>();
        try
        {
            using var conn = DBConnection.GetConnection();
            using var cmd = new MySqlCommand(sql, conn);

            for (int i = 0; i < parameters.Length; i++)
            {
                cmd.Parameters.AddWithValue($"@p{i}", parameters[i] ?? DBNull.Value);
            }

            // Hỗ trợ cả dấu ? truyền thống
            if (sql.Contains("?"))
            {
                cmd.Parameters.Clear();
                int idx = 0;
                string newSql = sql;
                while (newSql.Contains("?"))
                {
                    int pos = newSql.IndexOf("?");
                    newSql = newSql.Substring(0, pos) + $"@p{idx}" + newSql.Substring(pos + 1);
                    cmd.Parameters.AddWithValue($"@p{idx}", parameters[idx] ?? DBNull.Value);
                    idx++;
                }
                cmd.CommandText = newSql;
            }

            using var reader = cmd.ExecuteReader();
            int colCount = reader.FieldCount;
            while (reader.Read())
            {
                var row = new object[colCount];
                for (int i = 0; i < colCount; i++)
                {
                    row[i] = reader.IsDBNull(i) ? null! : reader.GetValue(i);
                }
                result.Add(row);
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"[DBBase.Query Error]: {ex.Message}");
        }
        return result;
    }

    public static int Update(string sql, params object[] parameters)
    {
        try
        {
            using var conn = DBConnection.GetConnection();
            using var cmd = new MySqlCommand(sql, conn);

            if (sql.Contains("?"))
            {
                int idx = 0;
                string newSql = sql;
                while (newSql.Contains("?"))
                {
                    int pos = newSql.IndexOf("?");
                    newSql = newSql.Substring(0, pos) + $"@p{idx}" + newSql.Substring(pos + 1);
                    cmd.Parameters.AddWithValue($"@p{idx}", parameters[idx] ?? DBNull.Value);
                    idx++;
                }
                cmd.CommandText = newSql;
            }
            else
            {
                for (int i = 0; i < parameters.Length; i++)
                {
                    cmd.Parameters.AddWithValue($"@p{i}", parameters[i] ?? DBNull.Value);
                }
            }

            return cmd.ExecuteNonQuery();
        }
        catch (Exception ex)
        {
            Console.WriteLine($"[DBBase.Update Error]: {ex.Message}");
            return 0;
        }
    }
}
