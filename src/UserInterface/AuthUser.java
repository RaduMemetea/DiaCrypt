package UserInterface;

import DataModels.Diary;

public class AuthUser {

    private static AuthUser instance = null;

    private Integer ID;
    private String Username;
    private Diary Diary; // stores a copy of the diary present in the db
    private Diary DiaryChanges; // stores full only entries that changed from Diary ^^


    private AuthUser() {

    }

    public static AuthUser getInstance() {
        if (instance == null)
            instance = new AuthUser();
        return instance;
    }

}
