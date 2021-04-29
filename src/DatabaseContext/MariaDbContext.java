package DatabaseContext;

import java.sql.*;

public class MariaDbContext extends DbContextMethods {
    private static MariaDbContext instance = null;

    static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    static final String DB_URL = "jdbc:mariadb://192.168.10.2:3307/DiaCrypt?useTLS=true&trustServerCertificate=true";//TLS 1.2
    private final String USER;
    private final String PASS;


    private MariaDbContext(String user, String pass) {
        USER = user;
        PASS = pass;
    }

    public static MariaDbContext getInstance() {
        if (instance == null)
            return null;
        return instance;
    }

    public static void createInstance(String uname, String pass) throws SQLException, ClassNotFoundException {
        if (instance != null) instance.Close();

        instance = new MariaDbContext(uname, pass);

        instance.Open();
    }

    void Open() throws ClassNotFoundException, SQLException {
        if (conn != null) return;

        Class.forName(JDBC_DRIVER);
        conn = DriverManager.getConnection(DB_URL, USER, PASS);

    }

    public void Close() throws SQLException {
        if (conn == null) return;
        conn.close();
        conn = null;
    }

}
