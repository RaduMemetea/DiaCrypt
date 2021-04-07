package DatabaseContext;

import DataModels.*;
import DataModels.Complex.*;

import java.sql.*;
import java.util.List;

public class MariaDbContext implements IDbContext {
    static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    static final String DB_URL = "jdbc:mariadb://192.168.10.2:3307/DiaCrypt";
    private static MariaDbContext instance = null;
    private final String USER;
    private final String PASS;

    Connection conn = null;
    Statement stmt = null;


    private MariaDbContext(String user, String pass) {
        USER = user;
        PASS = pass;
    }

    public static MariaDbContext getInstance() {
        if (instance == null)
            return null;
        return instance;
    }

    public static void CreateDbInstance(String uname, String pass) throws SQLException, ClassNotFoundException {
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

        if (stmt != null) {
            stmt.close();
            stmt = null;
        }

        conn.close();
        conn = null;
    }


    @Override
    public User GetUser(Integer UserID) {
        return null;
    }

    @Override
    public User GetUser(String Username) {
        return null;
    }

    @Override
    public List<Diary> GetDiary(Integer UserID) {
        return null;
    }

    @Override
    public Page GetPage(Integer PageID) {
        return null;
    }

    @Override
    public FullDiary GetUserDiary(Integer UserID) {
        return null;
    }


    @Override
    public User PostUser(User user) {
        return null;
    }

    @Override
    public Diary PostDiary(Diary diary) {
        return null;
    }

    @Override
    public Page PostPage(Page page) {
        return null;
    }


    @Override
    public User PutUser(User user) {
        return null;
    }

    @Override
    public Diary PutDiary(Diary diary) {
        return null;
    }

    @Override
    public Page PutPage(Page page) {
        return null;
    }


    @Override
    public User DeleteUser(User user) {
        return null;
    }

    @Override
    public Diary DeleteDiary(Diary diary) {
        return null;
    }

    @Override
    public Page DeletePage(Page page) {
        return null;
    }
}
