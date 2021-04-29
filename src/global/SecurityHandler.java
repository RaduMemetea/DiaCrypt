package global;

public class SecurityHandler {


    public static String Argon2(String pass) {
        return null;
    }

    public static String Argon2(String pass, String salt) {
        return null;
    }


    public static boolean validateUsername(String username) {
        return true;
    }

    public static boolean validatePassword(char[] password) {
        return true;
    }

    public static boolean validatePassword(char[] password1, char[] password2) {
        return true;
    }

    public static boolean createUserSession(String username, char[] password) throws Exception {
        validateUsername(username);
        validatePassword(password);
//        throw new Exception("a");
        return true;
    }

    public static boolean createUser(String username, char[] password1, char[] password2) {
        validateUsername(username);
        validatePassword(password1, password2);
        return true;
    }
}
