package DatabaseContext;

import DataModels.*;
import DataModels.Complex.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class SqlDbContextMethods implements IDbContext {

    Connection conn;


    //Get Base

    public User GetUser(String username) throws SQLException {
        String sqlStatement = "SELECT * FROM `DiaCrypt`.`User` WHERE Username = ?;";

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setString(1, username);

            ResultSet rs = prepStatement.executeQuery();

            rs.next();
            User u = new User();

            u.ID = rs.getInt("ID");
            u.Username = rs.getString("Username");
            u.Password = rs.getString("Password");

            return u;

        }
    }


    public List<UserDiary> GetUserDiary(Integer userID) throws SQLException {
        String sqlStatement = "SELECT * FROM `DiaCrypt`.`UserDiary` WHERE UserID = ?;";

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setInt(1, userID);

            ResultSet rs = prepStatement.executeQuery();

            List<UserDiary> userDiarys = new ArrayList<>();
            UserDiary ud = new UserDiary();
            while (rs.next()) {
                ud.UserID = rs.getInt("UserID");
                ud.DiaryID = rs.getInt("DiaryID");
                userDiarys.add(ud);
            }

            return userDiarys;
        }
    }


    public Diary GetDiary(Integer diaryID) throws SQLException {
        String sqlStatement = "SELECT * FROM `DiaCrypt`.`Diary` WHERE ID = ?;";

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setInt(1, diaryID);


            ResultSet rs = prepStatement.executeQuery();

            Diary diary = new Diary();

            rs.next();
            diary.ID = rs.getInt("ID");
            diary.Title = rs.getString("Title");
            diary.CreationDate = rs.getTimestamp("CreationDate");


            return diary;
        }
    }


    public List<DiaryPage> GetDiaryPage(Integer diaryID) throws SQLException {
        String sqlStatement = "SELECT * FROM `DiaCrypt`.`DiaryPage` WHERE DiaryID = ?;";

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setInt(1, diaryID);

            ResultSet rs = prepStatement.executeQuery();

            List<DiaryPage> diaryPages = new ArrayList<>();

            DiaryPage dp = new DiaryPage();
            while (rs.next()) {

                dp.DiaryID = rs.getInt("DiaryID");
                dp.PageID = rs.getInt("PageID");

                diaryPages.add(dp);
            }

            return diaryPages;
        }
    }


    public Page GetPage(Integer pageID) throws SQLException {
        String sqlStatement = "SELECT * FROM `DiaCrypt`.`Page` WHERE ID = ?;";

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setInt(1, pageID);


            ResultSet rs = prepStatement.executeQuery();

            Page page = new Page();

            rs.next();
            page.ID = rs.getInt("ID");
            page.Text = rs.getString("Text");
            page.Number = rs.getInt("Number");


            return page;
        }
    }


    //Get


    public List<Diary> GetUserDiaries(Integer userID) throws SQLException {

        List<UserDiary> udLinks = GetUserDiary(userID);

        List<Diary> userDiaries = new ArrayList<>();

        for (UserDiary u : udLinks) {
            var diary = GetDiary(u.DiaryID);
            userDiaries.add(diary);
        }

        return userDiaries;

    }

    public FullDiary GetFullDiary(Integer diaryID) throws SQLException {
        FullDiary fullDiary = new FullDiary();

        var diary = GetDiary(diaryID);
        fullDiary.ID = diary.ID;
        fullDiary.Title = diary.Title;
        fullDiary.CreationDate = diary.CreationDate;

        List<DiaryPage> dpLinks = GetDiaryPage(fullDiary.ID);

        for (DiaryPage dp : dpLinks) {
            var page = GetPage(dp.PageID);
            fullDiary.Pages.add(page);
        }
        return fullDiary;
    }


    //Post

    public Integer PostUser(User user) throws SQLException {

        String sqlStatement = "INSERT INTO `DiaCrypt`.`User`(`ID`,`Username`,`Password`) VALUES(?, ?, ?);";

        try (PreparedStatement postUser = conn.prepareStatement(sqlStatement)) {
            postUser.setInt(1, 0);
            postUser.setString(2, user.Username);
            postUser.setString(3, user.Password);

            return postUser.executeUpdate();
        }
    }


    public Integer PostUserDiary(UserDiary userDiary) throws SQLException {
        String sqlStatement = "INSERT INTO `DiaCrypt`.`UserDiary`(`DiaryID`,`UserID`) VALUES(?,?);";

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setInt(1, userDiary.DiaryID);
            prepStatement.setInt(2, userDiary.UserID);

            return prepStatement.executeUpdate();
        }
    }


    public Integer PostDiary(Diary diary) throws SQLException {
        String sqlStatement = "INSERT INTO `DiaCrypt`.`Diary`(`ID`,`Title`,`CreationDate`) VALUES(?,?,?);";

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setInt(1, 0);
            prepStatement.setString(2, diary.Title);
            prepStatement.setTimestamp(3, diary.CreationDate);

            return prepStatement.executeUpdate();
        }
    }


    public Integer PostDiaryPage(DiaryPage diaryPage) throws SQLException {
        String sqlStatement = "INSERT INTO `DiaCrypt`.`DiaryPage`(`DiaryID`,`PageID`) VALUES(?,?);";

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setInt(1, diaryPage.DiaryID);
            prepStatement.setInt(2, diaryPage.PageID);

            return prepStatement.executeUpdate();
        }
    }


    public Integer PostPage(Page page) throws SQLException {
        String sqlStatement = "INSERT INTO `DiaCrypt`.`Page`(`ID`,`Number`,`Text`) VALUES(?,?,?);";

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setInt(1, 0);
            prepStatement.setInt(2, page.Number);
            prepStatement.setString(3, page.Text);

            return prepStatement.executeUpdate();
        }
    }


    //Put

    public Integer PutUser(User user) throws SQLException {
        String sqlStatement = "UPDATE `DiaCrypt`.`User` SET `Username` = ?, `Password` = ? WHERE `ID` = ?;";

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setString(1, user.Username);
            prepStatement.setString(2, user.Password);
            prepStatement.setInt(3, user.ID);

            return prepStatement.executeUpdate();
        }
    }


    public Integer PutDiary(Diary diary) throws SQLException {
        String sqlStatement = "UPDATE `DiaCrypt`.`Diary` SET `Title` = ?, `CreationDate` = ? WHERE `ID` = ?;";

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setString(1, diary.Title);
            prepStatement.setTimestamp(2, diary.CreationDate);
            prepStatement.setInt(3, diary.ID);

            return prepStatement.executeUpdate();
        }
    }


    public Integer PutPage(Page page) throws SQLException {
        String sqlStatement = "UPDATE `DiaCrypt`.`Page` SET `Number` = ?, `Text` = ? WHERE `ID` = ?;";

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setInt(1, page.Number);
            prepStatement.setString(2, page.Text);
            prepStatement.setInt(3, page.ID);

            return prepStatement.executeUpdate();
        }
    }


    //Delete

    public Integer DeleteUser(Integer userID) throws SQLException {
        String sqlStatement = "DELETE FROM `DiaCrypt`.`User`WHERE ID = ?;";

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setInt(1, userID);
            return prepStatement.executeUpdate();
        }
    }


    public Integer DeleteUserDiary(Integer userID, Integer diaryID) throws SQLException {
        String sqlStatement = "DELETE FROM `DiaCrypt`.`UserDiary`WHERE UserID = ? AND DiaryID = ?;";

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setInt(1, userID);
            prepStatement.setInt(2, diaryID);
            return prepStatement.executeUpdate();
        }
    }


    public Integer DeleteDiary(Integer diaryID) throws SQLException {
        String sqlStatement = "DELETE FROM `DiaCrypt`.`Diary`WHERE ID = ?;";

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setInt(1, diaryID);
            return prepStatement.executeUpdate();
        }
    }


    public Integer DeleteDiaryPage(Integer diaryID, Integer pageID) throws SQLException {
        String sqlStatement = "DELETE FROM `DiaCrypt`.`DiaryPage`WHERE DiaryID = ? AND PageID = ?;";

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setInt(1, diaryID);
            prepStatement.setInt(2, pageID);
            return prepStatement.executeUpdate();
        }
    }


    public Integer DeletePage(Integer pageID) throws SQLException {
        String sqlStatement = "DELETE FROM `DiaCrypt`.`Page`WHERE ID = ?;";

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setInt(1, pageID);
            return prepStatement.executeUpdate();
        }
    }
}
