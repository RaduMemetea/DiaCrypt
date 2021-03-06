package databaseContext;

import dataModels.*;
import dataModels.complex.FullDiary;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class SqlDbContextMethods implements IDbContext {

    Connection conn;

    //Get

    public User GetUser(String username) throws SQLException {
        String sqlStatement = "SELECT * FROM `DiaCrypt`.`User` WHERE Username = ?;";

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setString(1, username);

            ResultSet rs = prepStatement.executeQuery();

            if (!rs.next()) throw new SQLException("Incorrect username or password!");

            User u = new User();

            u.ID = rs.getInt("ID");
            u.Username = rs.getString("Username");
            u.Password = rs.getString("Password");
            u.EncryptionPassword = rs.getString("EncryptionPassword");

            return u;

        }
    }

    public User GetUser(Integer id) throws SQLException {
        String sqlStatement = "SELECT * FROM `DiaCrypt`.`User` WHERE ID = ?;";

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setInt(1, id);

            ResultSet rs = prepStatement.executeQuery();

            if (!rs.next()) throw new SQLException("No user found!");

            User u = new User();

            u.ID = rs.getInt("ID");
            u.Username = rs.getString("Username");
            u.Password = rs.getString("Password");
            u.EncryptionPassword = rs.getString("EncryptionPassword");

            return u;

        }
    }

    public List<UserDiary> GetUserDiariesLink(Integer userID) throws SQLException {
        String sqlStatement = "SELECT * FROM `DiaCrypt`.`UserDiary` WHERE UserID = ?;";

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setInt(1, userID);

            ResultSet rs = prepStatement.executeQuery();

            List<UserDiary> userDiaries = new ArrayList<>();
            while (rs.next()) {
                UserDiary ud = new UserDiary();
                ud.UserID = rs.getInt("UserID");
                ud.DiaryID = rs.getInt("DiaryID");
                userDiaries.add(ud);
            }

            return userDiaries;
        }
    }

    public Diary GetDiary(Integer diaryID) throws SQLException {
        String sqlStatement = "SELECT * FROM `DiaCrypt`.`Diary` WHERE ID = ?;";

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setInt(1, diaryID);

            ResultSet rs = prepStatement.executeQuery();

            Diary diary = new Diary();

            if (!rs.next()) throw new SQLException("Diary does not exist!");

            diary.ID = rs.getInt("ID");
            diary.Title = rs.getString("Title");
            diary.CreationDate = rs.getTimestamp("CreationDate");

            return diary;
        }
    }

    public List<DiaryPage> GetDiaryPages(Integer diaryID) throws SQLException {
        String sqlStatement = "SELECT * FROM `DiaCrypt`.`DiaryPage` WHERE DiaryID = ?;";

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setInt(1, diaryID);

            ResultSet rs = prepStatement.executeQuery();

            List<DiaryPage> diaryPages = new ArrayList<>();

            while (rs.next()) {
                DiaryPage dp = new DiaryPage();

                dp.DiaryID = rs.getInt("DiaryID");
                dp.PageID = rs.getInt("PageID");

                diaryPages.add(dp);
            }

            return diaryPages;
        }
    }

    public DiaryPage GetPageDiary(Integer pageID) throws SQLException {
        String sqlStatement = "SELECT * FROM `DiaCrypt`.`DiaryPage` WHERE PageID = ?;";

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setInt(1, pageID);

            ResultSet rs = prepStatement.executeQuery();

            if (!rs.next()) throw new SQLException("Page does not have a diary!");

            DiaryPage d = new DiaryPage();

            d.DiaryID = rs.getInt("DiaryID");
            d.PageID = rs.getInt("DiaryID");

            return d;
        }
    }

    public Page GetPage(Integer pageID) throws SQLException {
        String sqlStatement = "SELECT * FROM `DiaCrypt`.`Page` WHERE ID = ?;";

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setInt(1, pageID);

            ResultSet rs = prepStatement.executeQuery();

            Page page = new Page();

            if (!rs.next()) throw new SQLException("Page does not exist!");

            page.ID = rs.getInt("ID");
            page.Text = rs.getString("Text");
            page.Number = rs.getInt("Number");

            return page;
        }
    }

    public FullDiary GetFullDiary(Integer diaryID) throws SQLException {
        FullDiary fullDiary = new FullDiary();

        var diary = GetDiary(diaryID);
        fullDiary.ID = diary.ID;
        fullDiary.Title = diary.Title;
        fullDiary.CreationDate = diary.CreationDate;

        List<DiaryPage> dpLinks = GetDiaryPages(fullDiary.ID);

        if (dpLinks != null) {
            fullDiary.Pages = new ArrayList<>();
            for (DiaryPage dp : dpLinks) {
                fullDiary.Pages.add(GetPage(dp.PageID));
            }
        }
        return fullDiary;
    }

    public List<FullDiary> GetUserDiaries(Integer userID) throws SQLException {
        var diaries = GetUserDiariesLink(userID);
        List<FullDiary> fullDiaries = new ArrayList<>();

        for (var diary : diaries)
            fullDiaries.add(GetFullDiary(diary.DiaryID));

        return fullDiaries;
    }


    //Post

    public User PostUser(User user) throws SQLException {

        String sqlStatement = "INSERT INTO `DiaCrypt`.`User`(`ID`,`Username`,`Password`,`EncryptionPassword`) VALUES(?, ?, ?, ?);";

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS)) {
            prepStatement.setInt(1, 0);
            prepStatement.setString(2, user.Username);
            prepStatement.setString(3, user.Password);
            prepStatement.setString(4, user.EncryptionPassword);

            if (prepStatement.executeUpdate() == 0)
                throw new SQLException("User insert was unsuccessful.");

            try (ResultSet rs = prepStatement.getGeneratedKeys()) {
                if (rs.next()) {
                    user.ID = rs.getInt(1);
                    return user;
                }
                throw new SQLException("User insert failed, no ID obtained.");
            }

        }
    }


    public void PostUserDiary(UserDiary userDiary) throws SQLException {
        String sqlStatement = "INSERT INTO `DiaCrypt`.`UserDiary`(`DiaryID`,`UserID`) VALUES(?,?);";

        MariaDbContext.getInstance().GetUser(userDiary.UserID);
        MariaDbContext.getInstance().GetDiary(userDiary.DiaryID);

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setInt(1, userDiary.DiaryID);
            prepStatement.setInt(2, userDiary.UserID);

            if (prepStatement.executeUpdate() == 0)
                throw new SQLException("UserDiary insert was unsuccessful.");

        }
    }


    public Diary PostDiary(Diary diary) throws SQLException {
        String sqlStatement = "INSERT INTO `DiaCrypt`.`Diary`(`ID`,`Title`,`CreationDate`) VALUES(?,?,?);";

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS)) {
            prepStatement.setInt(1, 0);
            prepStatement.setString(2, diary.Title);
            prepStatement.setTimestamp(3, new Timestamp(new Date().getTime()));

            if (prepStatement.executeUpdate() == 0)
                throw new SQLException("Diary insert was unsuccessful.");

            try (ResultSet rs = prepStatement.getGeneratedKeys()) {
                if (rs.next()) {
                    diary.ID = rs.getInt(1);
                    return diary;
                }
                throw new SQLException("Diary insert failed, no ID obtained.");
            }
        }
    }


    public void PostDiaryPage(DiaryPage diaryPage) throws SQLException {
        String sqlStatement = "INSERT INTO `DiaCrypt`.`DiaryPage`(`DiaryID`,`PageID`) VALUES(?,?);";

        MariaDbContext.getInstance().GetDiary(diaryPage.DiaryID);
        MariaDbContext.getInstance().GetPage(diaryPage.PageID);

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setInt(1, diaryPage.DiaryID);
            prepStatement.setInt(2, diaryPage.PageID);

            if (prepStatement.executeUpdate() == 0)
                throw new SQLException("DiaryPage insert was unsuccessful.");

        }
    }


    public Page PostPage(Page page) throws SQLException {
        String sqlStatement = "INSERT INTO `DiaCrypt`.`Page`(`ID`,`Number`,`Text`) VALUES(?,?,?);";

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS)) {
            prepStatement.setInt(1, 0);
            prepStatement.setInt(2, page.Number);
            prepStatement.setString(3, page.Text);

            if (prepStatement.executeUpdate() == 0)
                throw new SQLException("Page insert was unsuccessful.");

            try (ResultSet rs = prepStatement.getGeneratedKeys()) {
                if (rs.next()) {
                    page.ID = rs.getInt(1);
                    return page;
                }
                throw new SQLException("Page insert failed, no ID obtained.");
            }
        }
    }


    //Put

    public boolean PutUser(User user) throws SQLException {
        String sqlStatement = "UPDATE `DiaCrypt`.`User` SET `Username` = ?, `Password` = ?, `EncryptionPassword` = ? WHERE `ID` = ?;";

        MariaDbContext.getInstance().GetUser(user.ID);

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setString(1, user.Username);
            prepStatement.setString(2, user.Password);
            prepStatement.setString(3, user.EncryptionPassword);
            prepStatement.setInt(3, user.ID);

            return prepStatement.executeUpdate() == 1;
        }
    }


    public boolean PutDiary(Diary diary) throws SQLException {
        String sqlStatement = "UPDATE `DiaCrypt`.`Diary` SET `Title` = ?, `CreationDate` = ? WHERE `ID` = ?;";

        MariaDbContext.getInstance().GetDiary(diary.ID);

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setString(1, diary.Title);
            prepStatement.setTimestamp(2, diary.CreationDate);
            prepStatement.setInt(3, diary.ID);

            return prepStatement.executeUpdate() == 1;
        }
    }


    public boolean PutPage(Page page) throws SQLException {
        String sqlStatement = "UPDATE `DiaCrypt`.`Page` SET `Number` = ?, `Text` = ? WHERE `ID` = ?;";

        MariaDbContext.getInstance().GetPage(page.ID);

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setInt(1, page.Number);
            prepStatement.setString(2, page.Text);
            prepStatement.setInt(3, page.ID);

            return prepStatement.executeUpdate() == 1;
        }
    }


    //Delete

    public boolean DeleteUser(Integer userID) throws SQLException {
        String sqlStatement = "DELETE FROM `DiaCrypt`.`User`WHERE ID = ?;";

        MariaDbContext.getInstance().GetUser(userID);

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setInt(1, userID);
            return prepStatement.executeUpdate() == 1;
        }
    }

    public boolean DeleteDiary(Integer diaryID) throws SQLException {
        String sqlStatement = "DELETE FROM `DiaCrypt`.`Diary`WHERE ID = ?;";

        MariaDbContext.getInstance().GetDiary(diaryID);

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setInt(1, diaryID);
            return prepStatement.executeUpdate() == 1;
        }
    }

    public boolean DeletePage(Integer pageID) throws SQLException {
        String sqlStatement = "DELETE FROM `DiaCrypt`.`Page`WHERE ID = ?;";

        MariaDbContext.getInstance().GetPage(pageID);

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setInt(1, pageID);
            return prepStatement.executeUpdate() == 1;
        }
    }
}
