package global;

import dataModels.*;
import dataModels.complex.FullDiary;
import databaseContext.MariaDbContext;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.Arrays;
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
        return Base64.getEncoder().encodeToString(hash.getBytes(StandardCharsets.UTF_8));
    }

    public static boolean Argon2Verify(char[] pass, String hash) {
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id, 32, 64);
        return argon2.verify(new String(Base64.getDecoder().decode(hash)), pass);
    }

    public static String aesEncrypt(String text) throws Exception {

        var iv = aesUtil.generateIV();

        var enc = aesUtil.encrypt(text.getBytes(StandardCharsets.UTF_8), AuthUser.getInstance().secretKey, iv);

        var i = Base64.getEncoder().encodeToString(iv.getIV());
        var fin = i + ',' + enc;

        return Base64.getEncoder().encodeToString(fin.getBytes(StandardCharsets.UTF_8));

    }

    public static String aesDecrypt(String text) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        var split = new String(Base64.getDecoder().decode(text)).split(",", 2);

        byte[] iv;
        byte[] encryptedEK;

        iv = Base64.getDecoder().decode(split[0]);
        encryptedEK = split[1].getBytes(StandardCharsets.UTF_8);


        return aesUtil.decrypt(encryptedEK, AuthUser.getInstance().secretKey, new IvParameterSpec(iv));
    }


    public static String deriveEncryptedKey(char[] password) throws Exception {
        var salt1 = aesUtil.generateSalt();
        var secretKey1 = aesUtil.deriveKeyFromPassword(password, salt1);

        var salt2 = aesUtil.generateSalt();
        var secretKey2 = aesUtil.deriveKeyFromPassword(password, salt2);

        // This key can be stored on a second system to facilitate implementing forgotten password process without needing to encrypt all the diaries again
        var b64EncryptionKey = Base64.getEncoder().encodeToString(secretKey1.getEncoded());

        var iv = aesUtil.generateIV();

        var encryptedEK = aesUtil.encrypt(b64EncryptionKey.getBytes(StandardCharsets.UTF_8), secretKey2, iv);

        var s = Base64.getEncoder().encodeToString(salt2);
        var i = Base64.getEncoder().encodeToString(iv.getIV());

        var fin = s + ',' + i + ',' + encryptedEK;

        return Base64.getEncoder().encodeToString(fin.getBytes(StandardCharsets.UTF_8));
    }

    public static SecretKey decryptKey(String encryptedPassword, char[] password) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {

        var split = new String(Base64.getDecoder().decode(encryptedPassword)).split(",", 3);


        byte[] salt;
        byte[] iv;
        byte[] encryptedEK;

        salt = Base64.getDecoder().decode(split[0]);
        iv = Base64.getDecoder().decode(split[1]);
        encryptedEK = split[2].getBytes(StandardCharsets.UTF_8);


        var secretKey = aesUtil.deriveKeyFromPassword(password, salt);

        var decryptedEK = aesUtil.decrypt(encryptedEK, secretKey, new IvParameterSpec(iv));

        return aesUtil.recreateSecretKey(decryptedEK);
    }


    public static void validateUsername(String username) throws Exception {
        if (username == null) throw new Exception("Invalid Username!");
        if (username.equals("")) throw new Exception("Invalid Username!");
        if (username.length() < 4) throw new Exception("Username to short!");
        if (username.length() > 100) throw new Exception("Username to long!");

    }

    public static void validatePassword(char[] password) throws Exception {
        if (password == null) throw new Exception("Invalid Password!");
        if (password.length < 8) throw new Exception("Password to short!");
        if (password.length > 100) throw new Exception("Password to long!");
    }

    public static void validatePassword(char[] password1, char[] password2) throws Exception {
        try {
            validatePassword(password1);
        } catch (Exception exception) {
            throw new Exception("1:" + exception.getMessage());
        }

        try {
            validatePassword(password2);
        } catch (Exception exception) {
            throw new Exception("2:" + exception.getMessage());
        }

        if (!Arrays.equals(password1, password2)) throw new Exception("Passwords do not match!");
    }


    public static void createUserSession(String username, char[] password) throws Exception {
        validateUsername(username);
        validatePassword(password);

        var user = MariaDbContext.getInstance().GetUser(username);
        var okPass = Argon2Verify(password, user.Password);
        if (!okPass) throw new Exception("Incorrect username or password!");

        AuthUser.getInstance().SetUser(user);
        AuthUser.getInstance().secretKey = decryptKey(user.EncryptionPassword, password);//decrypt the key with password
        AuthUser.getInstance().refreshDiaries();

    }

    public static void createUser(String username, char[] password1, char[] password2) throws Exception {
        validateUsername(username);
        validatePassword(password1, password2);

        boolean flag;

        try {
            MariaDbContext.getInstance().GetUser(username);
            flag = true;
        } catch (Exception exception) {
            flag = false;
        }

        if (flag) throw new Exception("Username already registered!");


        Arrays.fill(password2, (char) 0);

        User u = new User();
        u.Username = username;
        u.EncryptionPassword = deriveEncryptedKey(password1);//generate a key encrypted with password
        u.Password = Argon2Generate(password1);

        u.ID = MariaDbContext.getInstance().PostUser(u).ID;

        MariaDbContext.getInstance().GetUser(u.ID);
    }

    public static void LogOut() {
        AuthUser.DestroyInstance();
    }


    public static Diary addDiary(String title) throws Exception {
        if (title.length() < 3) throw new Exception("Title to short!");
        if (title.length() > 1000) throw new Exception("Title to long!");

        var dia = new Diary();
        dia.Title = aesEncrypt(title);
        dia.ID = MariaDbContext.getInstance().PostDiary(dia).ID;


        var ud = new UserDiary();
        ud.DiaryID = dia.ID;
        ud.UserID = AuthUser.getInstance().ID;

        MariaDbContext.getInstance().PostUserDiary(ud);

        addPage("", dia.ID);

        return MariaDbContext.getInstance().GetDiary(dia.ID);
    }

    public static Page addPage(String pageText, Integer diaryID) throws Exception {
        if (pageText == null) return null;

        MariaDbContext.getInstance().GetDiary(diaryID);

        var page = new Page();
        page.Text = aesEncrypt(pageText);
        page.Number = MariaDbContext.getInstance().GetDiaryPages(diaryID).size() + 1;
        page.ID = MariaDbContext.getInstance().PostPage(page).ID;

        var dp = new DiaryPage();
        dp.DiaryID = diaryID;
        dp.PageID = page.ID;

        MariaDbContext.getInstance().PostDiaryPage(dp);

        return MariaDbContext.getInstance().GetPage(page.ID);
    }


    public static void deletePage(Integer id) throws Exception {
        if (id < 1) return;
        if (MariaDbContext.getInstance().GetDiaryPages(MariaDbContext.getInstance().GetPageDiary(id).DiaryID).size() <= 1)
            throw new Exception("Page could not be deleted because it's the only one remaining.");

        MariaDbContext.getInstance().DeletePage(id);
    }

    public static void deleteDiary(Integer id) throws SQLException {
        if (id < 1) return;

        MariaDbContext.getInstance().DeleteDiary(id);
    }

    public static void updateDiaryTitle(FullDiary fullDiary) throws Exception {
        fullDiary.Title = aesEncrypt(fullDiary.Title);
        MariaDbContext.getInstance().PutDiary(fullDiary.getDiary());
    }

    public static void updatePage(Page page) throws Exception {
        page.Text = aesEncrypt(page.Text);
        MariaDbContext.getInstance().PutPage(page);
    }
}
