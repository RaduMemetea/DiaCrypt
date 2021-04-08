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
    public Integer PostUser(User user) throws SQLException {

        String sqlStatement = "INSERT INTO `DiaCrypt`.`User`(`ID`,`Username`,`Password`,`Salt`) VALUES(?, ?, ?, ?);";

        try (PreparedStatement postUser = conn.prepareStatement(sqlStatement)) {
            postUser.setInt(1, 0);
            postUser.setString(2, user.Username);
            postUser.setString(3, user.Password);
            postUser.setString(4, user.Salt);

            return postUser.executeUpdate();
        }
    }

    @Override
    public Integer PostUserDiary(UserDiary userDiary) throws SQLException {
        String sqlStatement = "INSERT INTO `DiaCrypt`.`UserDiary`(`DiaryID`,`UserID`) VALUES(?,?);";

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setInt(1, userDiary.DiaryID);
            prepStatement.setInt(2, userDiary.UserID);

            return prepStatement.executeUpdate();
        }
    }

    @Override
    public Integer PostDiary(Diary diary) throws SQLException {
        String sqlStatement = "INSERT INTO `DiaCrypt`.`Diary`(`ID`,`Title`,`CreationDate`) VALUES(?,?,?);";

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setInt(1, 0);
            prepStatement.setString(2, diary.Title);
            prepStatement.setTimestamp(3, diary.CreationDate);

            return prepStatement.executeUpdate();
        }
    }

    @Override
    public Integer PostDiaryPages(DiaryPages diaryPages) throws SQLException {
        String sqlStatement = "INSERT INTO `DiaCrypt`.`DiaryPages`(`DiaryID`,`PageID`) VALUES(?,?);";

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setInt(1, diaryPages.DiaryID);
            prepStatement.setInt(2, diaryPages.PageID);

            return prepStatement.executeUpdate();
        }
    }

    @Override
    public Integer PostPage(Page page) throws SQLException {
        String sqlStatement = "INSERT INTO `DiaCrypt`.`Page`(`ID`,`Number`,`Text`) VALUES(?,?,?);";

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setInt(1, 0);
            prepStatement.setInt(2, page.Number);
            prepStatement.setString(3, page.Text);

            return prepStatement.executeUpdate();
        }
    }


    @Override
    public Integer PutUser(User user) throws SQLException {
        String sqlStatement = "UPDATE `DiaCrypt`.`User` SET `Username` = ?, `Password` = ?, `Salt` = ? WHERE `ID` = ?;";

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setString(1, user.Username);
            prepStatement.setString(2, user.Password);
            prepStatement.setString(3, user.Salt);
            prepStatement.setInt(4, user.ID);

            return prepStatement.executeUpdate();
        }
    }

    @Override
    public Integer PutDiary(Diary diary) throws SQLException {
        String sqlStatement = "UPDATE `DiaCrypt`.`Diary` SET `Title` = ?, `CreationDate` = ? WHERE `ID` = ?;";

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setString(1, diary.Title);
            prepStatement.setTimestamp(2, diary.CreationDate);
            prepStatement.setInt(3, diary.ID);

            return prepStatement.executeUpdate();
        }
    }

    @Override
    public Integer PutPage(Page page) throws SQLException {
        String sqlStatement = "UPDATE `DiaCrypt`.`Page` SET `Number` = ?, `Text` = ? WHERE `ID` = ?;";

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setInt(1, page.Number);
            prepStatement.setString(2, page.Text);
            prepStatement.setInt(3, page.ID);

            return prepStatement.executeUpdate();
        }
    }


    @Override
    public Integer DeleteUser(Integer userID) throws SQLException {
        String sqlStatement = "DELETE FROM `DiaCrypt`.`User`WHERE ID = ?;";

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setInt(1, userID);
            return prepStatement.executeUpdate();
        }
    }

    @Override
    public Integer DeleteUserDiary(Integer userID, Integer diaryID) throws SQLException {
        String sqlStatement = "DELETE FROM `DiaCrypt`.`UserDiary`WHERE UserID = ? AND DiaryID = ?;";

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setInt(1, userID);
            prepStatement.setInt(2, diaryID);
            return prepStatement.executeUpdate();
        }
    }

    @Override
    public Integer DeleteDiary(Integer diaryID) throws SQLException {
        String sqlStatement = "DELETE FROM `DiaCrypt`.`Diary`WHERE ID = ?;";

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setInt(1, diaryID);
            return prepStatement.executeUpdate();
        }
    }

    @Override
    public Integer DeleteDiaryPages(Integer diaryID, Integer pageID) throws SQLException {
        String sqlStatement = "DELETE FROM `DiaCrypt`.`DiaryPages`WHERE DiaryID = ? AND PageID = ?;";

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setInt(1, diaryID);
            prepStatement.setInt(2, pageID);
            return prepStatement.executeUpdate();
        }
    }

    @Override
    public Integer DeletePage(Integer pageID) throws SQLException {
        String sqlStatement = "DELETE FROM `DiaCrypt`.`Page`WHERE ID = ?;";

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setInt(1, pageID);
            return prepStatement.executeUpdate();
        }
    }
}
