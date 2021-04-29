package global;

import DataModels.Complex.*;
import DataModels.*;

import java.util.ArrayList;
import java.util.List;

public class AuthUser extends User {

    private static AuthUser instance = null;

    private List<Diary> Diaries; // All diaries data(excluding contents) that belong to thew current user
    private FullDiary Diary;

    private AuthUser(User user) {
        ID = user.ID;
        Username = user.Username;
        Password = user.Password;
        Salt = user.Salt;
    }

    public static AuthUser getInstance() {
        if (instance == null)
            return null;
        return instance;
    }

    public static void DestroyInstance() {
        instance.ID = 0;
        instance.Username = null;
        instance.Password = null;
        instance.Salt = null;
        instance = null;
    }

    public static boolean createInstance(User user) {
        if (user.ID < 0 || user.Username == null || user.Salt == null) return false;
        if (instance != null) DestroyInstance();
        instance = new AuthUser(user);
        return true;
    }

    public boolean addDiary(Diary diary) {

        if (diary == null || diary.ID < 1 || diary.Title == null)
            return false;

        if (Diaries == null)
            Diaries = new ArrayList<>();

        Diaries.add(diary);

        return true;
    }

}
