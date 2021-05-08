package databaseContext;

import dataModels.*;
import dataModels.complex.FullDiary;

import java.sql.SQLException;
import java.util.List;

public interface IDbContext {


    //Get Base


    User GetUser(String username) throws SQLException;


    List<UserDiary> GetUserDiariesLink(Integer userID) throws SQLException;

    Diary GetDiary(Integer diaryID) throws SQLException; // Returns all entries that make a diary(all page ID's) for a user

    List<DiaryPage> GetDiaryPages(Integer diaryID) throws SQLException;

    Page GetPage(Integer pageID) throws SQLException;


    //Get


    FullDiary GetFullDiary(Integer diaryID) throws SQLException;


    //Post

    Integer PostUser(User user) throws SQLException;

    void PostUserDiary(UserDiary userDiary) throws SQLException;

    Integer PostDiary(Diary diary) throws SQLException;

    void PostDiaryPage(DiaryPage diaryPage) throws SQLException;

    Integer PostPage(Page page) throws SQLException;


    //Put

    boolean PutUser(User user) throws SQLException;

    boolean PutDiary(Diary diary) throws SQLException;

    boolean PutPage(Page page) throws SQLException;


    //Delete

    boolean DeleteUser(Integer userID) throws SQLException;

    boolean DeleteUserDiary(Integer userID, Integer diaryID) throws SQLException;

    boolean DeleteDiary(Integer diaryID) throws SQLException;

    boolean DeleteDiaryPage(Integer diaryID, Integer pageID) throws SQLException;

    boolean DeletePage(Integer pageID) throws SQLException;


}
