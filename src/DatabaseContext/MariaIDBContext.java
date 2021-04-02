package DatabaseContext;

import DataModels.Complex.UserDiary;
import DataModels.Diary;
import DataModels.Page;
import DataModels.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class MariaIDBContext implements IDbContext {

    static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://192.168.10.2:3307/DiaCrypt";

    static final String USER = "SoulEather";
    static final String PASS = "";

    Connection conn = null;
    Statement stmt = null;

    public MariaIDBContext() {

        try {
            Class.forName(JDBC_DRIVER);

//            System.out.println("Connecting to a selected database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
//            System.out.println("Connected database successfully...");


            stmt = conn.createStatement();

            String sql = "select * from DiaCrypt.User";

            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("Interrogation successfully in given database...");
            while (rs.next())
                System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3));
        } catch (Exception se) {
            //Handle errors for JDBC
            se.printStackTrace();
        }


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
    public UserDiary GetUserDiary(Integer UserID) {
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
