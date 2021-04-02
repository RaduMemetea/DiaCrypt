package DatabaseContext;

import DataModels.*;
import DataModels.Complex.UserDiary;

import java.util.List;

public interface IDbContext {


    //Get
    User GetUser(Integer UserID);    // Debatable if implementation needed

    User GetUser(String Username);

    List<Diary> GetDiary(Integer UserID); // Returns all entries that make a diary(all page ID's) for a user

    Page GetPage(Integer PageID);

    UserDiary GetUserDiary(Integer UserID);


    //Post

    User PostUser(User user);

    Diary PostDiary(Diary diary);

    Page PostPage(Page page);


    //Put

    User PutUser(User user);

    Diary PutDiary(Diary diary);

    Page PutPage(Page page);


    //Delete

    User DeleteUser(User user);

    Diary DeleteDiary(Diary diary);

    Page DeletePage(Page page);


}
