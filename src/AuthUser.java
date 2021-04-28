import DataModels.Complex.*;
import DataModels.*;

import java.util.ArrayList;
import java.util.List;

public class AuthUser extends User {

    private static AuthUser instance = null;

    private List<Diary> Diaries; // All diaries data(excluding contents) that belong to thew current user

    public static AuthUser getInstance() {
        if (instance == null)
            return null;
        return instance;
    }

    private FullDiary Diary;


    private AuthUser(User user) {
        ID = user.ID;
        Username = user.Username;
        Password = user.Password;
        Salt = user.Salt;
    }

    public static Boolean createInstance(User user) {
        if (user.ID < 0 || user.Username == null || user.Salt == null) return false;
        instance = new AuthUser(user);
        return true;
    }

    public Boolean addDiary(Diary diary) {

        if (diary == null || diary.ID < 1 || diary.Title == null)
            return false;

        if (Diaries == null)
            Diaries = new ArrayList<>();

        Diaries.add(diary);

        return true;
    }

}
