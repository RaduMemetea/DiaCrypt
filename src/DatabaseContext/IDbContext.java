package DatabaseContext;

import DataModels.*;
import DataModels.Complex.FullDiary;

import java.sql.SQLException;
import java.util.List;

public interface IDbContext {


    //Get
    User GetUser(Integer UserID);    // Debatable if implementation needed

    User GetUser(String Username);

    List<Diary> GetDiary(Integer UserID); // Returns all entries that make a diary(all page ID's) for a user

    Page GetPage(Integer PageID);

    FullDiary GetUserDiary(Integer UserID);


    //Post

    Integer PostUser(User user) throws SQLException;

    Integer PostUserDiary(UserDiary userDiary) throws SQLException;

    Integer PostDiary(Diary diary) throws SQLException;

    Integer PostDiaryPages(DiaryPages diaryPages) throws SQLException;

    Integer PostPage(Page page) throws SQLException;


    //Put

    Integer PutUser(User user) throws SQLException;

    Integer PutDiary(Diary diary) throws SQLException;

    Integer PutPage(Page page) throws SQLException;


    //Delete

    Integer DeleteUser(Integer userID) throws SQLException;

    Integer DeleteUserDiary(Integer userID, Integer diaryID) throws SQLException;

    Integer DeleteDiary(Integer diaryID) throws SQLException;

    Integer DeleteDiaryPages(Integer diaryID, Integer pageID) throws SQLException;

    Integer DeletePage(Integer pageID) throws SQLException;


}
