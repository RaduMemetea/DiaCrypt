package UserInterface;

import DataModels.Complex.FullDiary;
import DataModels.Diary;

import java.util.ArrayList;
import java.util.List;

public class AuthUser {

    private static AuthUser instance = null;

    private final Integer ID;
    private final String Username;
    private List<Diary> Diaries; // All diaries data(excluding contents) that belong to thew current user
    private FullDiary Diary;

    private AuthUser(Integer id, String username) {
        ID = id;
        Username = username;
    }

    public static AuthUser getInstance() {
        if (instance == null)
            return null;
        return instance;
    }

    public static Boolean CreateAuthUserInstance(Integer id, String username) {
        if (id < 0 && username == null) return false;
        instance = new AuthUser(id, username);
        return true;
    }


    public Boolean AddDiary(Diary diary) {

        if (diary == null || diary.ID < 1 || diary.Title == null)
            return false;

        if (Diaries == null)
            Diaries = new ArrayList<>();

        Diaries.add(diary);

        return true;
    }

}
