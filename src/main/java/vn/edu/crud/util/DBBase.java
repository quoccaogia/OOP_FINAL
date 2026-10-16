package vn.edu.crud.util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DBBase = đồ dùng chung cho mọi DAO.
 * Chỉ có 2 hàm:
 *   - query()  : chạy SELECT, trả về danh sách dòng
 *   - update() : chạy INSERT / UPDATE / DELETE
 */
public class DBBase {

    /**
     * Chạy câu SELECT.
     * Mỗi dòng trả về là 1 mảng Object[] (mỗi ô là 1 cột).
     *
     * Ví dụ:
     *   query("SELECT * FROM Flower WHERE color = ?", "Đỏ")
     */
    public static List<Object[]> query(String sql, Object... params) {
        List<Object[]> ketQua = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Gán tham số vào dấu ?
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            // Chạy và đọc từng dòng
            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData meta = rs.getMetaData();
                int soCot = meta.getColumnCount();

                while (rs.next()) {
                    Object[] dong = new Object[soCot];
                    for (int i = 1; i <= soCot; i++) {
                        dong[i - 1] = rs.getObject(i);
                    }
                    ketQua.add(dong);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ketQua;
    }

    /**
     * Chạy câu INSERT / UPDATE / DELETE.
     * Trả về số dòng bị ảnh hưởng.
     */
    public static int update(String sql, Object... params) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            return ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}