package global;

import dataModels.*;
import databaseContext.MariaDbContext;
import de.mkammerer.argon2.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SecurityHandler {


    public static String Argon2Generate(char[] pass) {
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id, 32, 64);
        String hash;
        try {
            hash = argon2.hash(10, 1024 * 1024, 4, pass);
        } finally {
            argon2.wipeArray(pass);
        }
        return Base64Encode(hash);
    }

    public static boolean Argon2Verify(char[] pass, String hash) {
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id, 32, 64);
        return argon2.verify(Base64Decode(hash), pass);
    }


    public static String Base64Encode(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    public static String Base64Decode(String text) {
        return new String(Base64.getDecoder().decode(text));
    }

    public static boolean validateUsername(String username) {
        //todo check for existence
        return true;
    }

    public static boolean validatePassword(char[] password) {
        return true;
    }

    public static boolean validatePassword(char[] password1, char[] password2) {
        return true;
    }

    public static void createUserSession(String username, char[] password) throws Exception {
        validateUsername(username);
        validatePassword(password);

        var user = MariaDbContext.getInstance().GetUser(username);
        var okPass = Argon2Verify(password, user.Password);
        if (!okPass) throw new Exception("Incorrect username or password!");

        AuthUser.getInstance().SetUser(user);
        AuthUser.getInstance().SetDiaries(
                MariaDbContext.getInstance().GetUserDiaries(
                        AuthUser.getInstance().ID
                )
        );
    }

    public static boolean createUser(String username, char[] password1, char[] password2) throws Exception {
        validateUsername(username);
        validatePassword(password1, password2);

        User u = new User();
        u.ID = 0;
        u.Username = username;
        u.Password = Argon2Generate(password1);

        return MariaDbContext.getInstance().PostUser(u) > 0;
    }

    public static void LogOut() {
        AuthUser.DestroyInstance();
    }

    public static Integer addDiary(String title) throws Exception {
        if (title.length() < 3) throw new Exception("Invalid diary title!");

        var dia = new Diary();
        dia.Title = title;

        var diaryID = MariaDbContext.getInstance().PostDiary(dia);

        var ud = new UserDiary();
        ud.DiaryID = diaryID;
        ud.UserID = AuthUser.getInstance().ID;

        MariaDbContext.getInstance().PostUserDiary(ud);

        addPage("", diaryID);

        return diaryID;
    }

    public static Page addPage(String pageText, Integer diaryID) throws Exception {
        if (pageText == null) throw new Exception("Invalid page text!");
        if (diaryID < 1) throw new Exception("Invalid diary id!");

        var page = new Page();
        page.Text = pageText;
        page.Number = MariaDbContext.getInstance().GetDiaryPages(diaryID).size() + 1;

        page.ID = MariaDbContext.getInstance().PostPage(page);

        var dp = new DiaryPage();
        dp.DiaryID = diaryID;
        dp.PageID = page.ID;

        MariaDbContext.getInstance().PostDiaryPage(dp);

        return page;
    }


}
