package vn.edu.crud.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class cung cấp kết nối MySQL dùng chung cho toàn project.
 * KHÔNG chứa logic nghiệp vụ, KHÔNG chứa hàm main.
 */
public class DBConnection {

    // ====== CẤU HÌNH KẾT NỐI ======
    private static final String URL =
            "jdbc:mysql://localhost:3306/flower_shop"
                    + "?useSSL=false"
                    + "&allowPublicKeyRetrieval=true"
                    + "&serverTimezone=Asia/Ho_Chi_Minh"
                    + "&characterEncoding=UTF-8";

    private static final String USER = "root";
    private static final String PASSWORD = "10101975";   // ← đổi thành mật khẩu MySQL của bạn

    // ====== NẠP DRIVER 1 LẦN DUY NHẤT ======
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(
                    "Không tìm thấy driver MySQL. Kiểm tra mysql-connector-j trong pom.xml", e);
        }
    }

    // Chặn khởi tạo instance — class này chỉ dùng static
    private DBConnection() {}

    // ====== HÀM LẤY KẾT NỐI ======
    /**
     * Mở 1 connection mới tới MySQL.
     * Nhớ đóng connection sau khi dùng (dùng try-with-resources).
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}