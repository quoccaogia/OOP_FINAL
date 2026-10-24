using MySqlConnector;

namespace vn.edu.crud.util;

public static class DBConnection
{
    public const string ConnectionString = 
        "Server=localhost;Port=3306;Database=flower_shop;User ID=root;Password=10101975;SslMode=None;AllowPublicKeyRetrieval=True;";

    public static MySqlConnection GetConnection()
    {
        var conn = new MySqlConnection(ConnectionString);
        conn.Open();
        return conn;
    }
}
