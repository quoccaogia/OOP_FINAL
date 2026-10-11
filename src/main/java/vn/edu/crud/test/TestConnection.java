package vn.edu.crud.test;

import vn.edu.crud.util.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * File test kết nối MySQL.
 * Chạy file này để kiểm tra DBConnection hoạt động chưa.
 * KHÔNG ảnh hưởng đến code chính.
 */
public class TestConnection {

    public static void main(String[] args) {
        System.out.println("=== TEST KẾT NỐI MYSQL ===\n");

        try (Connection conn = DBConnection.getConnection()) {

            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ KẾT NỐI THÀNH CÔNG!");
                System.out.println("   Database : " + conn.getCatalog());
                System.out.println("   MySQL    : " + conn.getMetaData().getDatabaseProductVersion());
                System.out.println("   URL      : " + conn.getMetaData().getURL());
                System.out.println("   User     : " + conn.getMetaData().getUserName());
            }

        } catch (SQLException e) {
            System.err.println("❌ KẾT NỐI THẤT BẠI!");
            System.err.println("   SQLState : " + e.getSQLState());
            System.err.println("   ErrorCode: " + e.getErrorCode());
            System.err.println("   Message  : " + e.getMessage());
        }
    }
}