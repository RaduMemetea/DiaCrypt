package DatabaseContext;

import DataModels.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class DbContextMethods implements IDbContext {

    Connection conn;


    //Get Base

    public Integer GetUserID(String username, String password) throws SQLException {
        String sqlStatement = "SELECT `User`.`ID` FROM `DiaCrypt`.`User` WHERE Username = ? AND Password = ?;";

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setString(1, username);
            prepStatement.setString(2, password);

            ResultSet rs = prepStatement.executeQuery();

            rs.next();
            return rs.getInt("ID");

        }
    }


    public UserDiary GetUserDiary(Integer userID, Integer diaryID) throws SQLException {
        String sqlStatement = "SELECT * FROM `DiaCrypt`.`UserDiary` WHERE UserID = ? AND DiaryID = ?;";

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setInt(1, userID);
            prepStatement.setInt(2, diaryID);

            ResultSet rs = prepStatement.executeQuery();

            UserDiary userDiary = new UserDiary();

            while (rs.next()) {
                userDiary.UserID = rs.getInt("UserID");
                userDiary.DiaryID = rs.getInt("DiaryID");
            }

            return userDiary;
        }
    }


    public Diary GetDiary(Integer diaryID) throws SQLException {
        String sqlStatement = "SELECT * FROM `DiaCrypt`.`Diary` WHERE ID = ?;";

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setInt(1, diaryID);


            ResultSet rs = prepStatement.executeQuery();

            Diary diary = new Diary();

            while (rs.next()) {
                diary.ID = rs.getInt("ID");
                diary.Title = rs.getString("Title");
                diary.CreationDate = rs.getTimestamp("CreationDate");
            }


            return diary;
        }
    }


    public DiaryPage GetDiaryPage(Integer diaryID, Integer pageID) throws SQLException {
        String sqlStatement = "SELECT * FROM `DiaCrypt`.`DiaryPage` WHERE DiaryID = ? AND PageID = ?;";

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setInt(1, diaryID);
            prepStatement.setInt(2, pageID);

            ResultSet rs = prepStatement.executeQuery();

            DiaryPage diaryPage = new DiaryPage();

            while (rs.next()) {
                diaryPage.DiaryID = rs.getInt("DiaryID");
                diaryPage.PageID = rs.getInt("PageID");
            }

            return diaryPage;
        }
    }


    public Page GetPage(Integer pageID) throws SQLException {
        String sqlStatement = "SELECT * FROM `DiaCrypt`.`Page` WHERE ID = ?;";

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setInt(1, pageID);


            ResultSet rs = prepStatement.executeQuery();

            Page page = new Page();

            while (rs.next()) {
                page.ID = rs.getInt("ID");
                page.Text = rs.getString("Text");
                page.Number = rs.getInt("Number");
            }


            return page;
        }
    }

    //Get


    //Post

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
        String sqlStatement = "UPDATE `DiaCrypt`.`User` SET `Username` = ?, `Password` = ?, `Salt` = ? WHERE `ID` = ?;";

        try (PreparedStatement prepStatement = conn.prepareStatement(sqlStatement)) {
            prepStatement.setString(1, user.Username);
            prepStatement.setString(2, user.Password);
            prepStatement.setString(3, user.Salt);
            prepStatement.setInt(4, user.ID);

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
