package DatabaseContext;

import DataModels.*;
import DataModels.Complex.FullDiary;

import java.sql.SQLException;

public interface IDbContext {


    //Get Base


    Integer GetUserID(String username, String password) throws SQLException;

    UserDiary GetUserDiary(Integer userID, Integer diaryID) throws SQLException;

    Diary GetDiary(Integer diaryID) throws SQLException; // Returns all entries that make a diary(all page ID's) for a user

    DiaryPage GetDiaryPage(Integer diaryID, Integer pageID) throws SQLException;

    Page GetPage(Integer pageID) throws SQLException;


    //Get


    //Post

    Integer PostUser(User user) throws SQLException;

    Integer PostUserDiary(UserDiary userDiary) throws SQLException;

    Integer PostDiary(Diary diary) throws SQLException;

    Integer PostDiaryPage(DiaryPage diaryPage) throws SQLException;

    Integer PostPage(Page page) throws SQLException;


    //Put

    Integer PutUser(User user) throws SQLException;

    Integer PutDiary(Diary diary) throws SQLException;

    Integer PutPage(Page page) throws SQLException;


    //Delete

    Integer DeleteUser(Integer userID) throws SQLException;

    Integer DeleteUserDiary(Integer userID, Integer diaryID) throws SQLException;

    Integer DeleteDiary(Integer diaryID) throws SQLException;

    Integer DeleteDiaryPage(Integer diaryID, Integer pageID) throws SQLException;

    Integer DeletePage(Integer pageID) throws SQLException;


}
