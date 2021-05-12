package databaseContext;

import dataModels.*;
import dataModels.complex.FullDiary;

import java.sql.SQLException;
import java.util.List;

public interface IDbContext {


    //Get


    User GetUser(String username) throws SQLException;

    User GetUser(Integer id) throws SQLException;

    List<UserDiary> GetUserDiariesLink(Integer userID) throws SQLException;

    Diary GetDiary(Integer diaryID) throws SQLException;

    List<DiaryPage> GetDiaryPages(Integer diaryID) throws SQLException;

    Page GetPage(Integer pageID) throws SQLException;

    FullDiary GetFullDiary(Integer diaryID) throws SQLException;

    List<FullDiary> GetUserDiaries(Integer userID) throws SQLException;

    DiaryPage GetPageDiary(Integer pageID) throws SQLException;

    //Post


    User PostUser(User user) throws SQLException;

    void PostUserDiary(UserDiary userDiary) throws SQLException;

    Diary PostDiary(Diary diary) throws SQLException;

    void PostDiaryPage(DiaryPage diaryPage) throws SQLException;

    Page PostPage(Page page) throws SQLException;


    //Put


    boolean PutUser(User user) throws SQLException;

    boolean PutDiary(Diary diary) throws SQLException;

    boolean PutPage(Page page) throws SQLException;


    //Delete


    boolean DeleteUser(Integer userID) throws SQLException;

    boolean DeleteDiary(Integer diaryID) throws SQLException;

    boolean DeletePage(Integer pageID) throws SQLException;


}
