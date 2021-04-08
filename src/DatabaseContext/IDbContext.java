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

    User PutUser(User user);

    Diary PutDiary(Diary diary);

    Page PutPage(Page page);


    //Delete

    User DeleteUser(User user);

    Diary DeleteDiary(Diary diary);

    Page DeletePage(Page page);


}
