import java.io.IOException;
import java.util.Scanner;
import java.sql.*;

public class Main {

    public static void main(String[] args) {
        System.out.print("Hello Dreamer!\nPlease chose an action:\n");
        System.out.print("[1] Log In \\ [2] Register\n:> ");

        int action;

        UserInterface inte = new UserInterface();

        Scanner in = new Scanner(System.in);
        action = in.nextInt();//TODO Secure and limit the action choice method


        inte.GetCredentials();

        switch (action) {
            case (1) -> inte.Login();
            case (2) -> inte.Register();
        }


    }
}
