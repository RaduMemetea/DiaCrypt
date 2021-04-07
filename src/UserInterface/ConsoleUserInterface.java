package UserInterface;

import DataModels.User;
import DatabaseContext.IDbContext;

import java.util.Scanner;

public class ConsoleUserInterface {
    User credentials = new User();


    public ConsoleUserInterface() {
        System.out.print("Hello Traveler!\nWhat do you want to do?:\n");
        System.out.print("[1] Log In \\ [2] Register\n:> ");

        int action;

        Scanner in = new Scanner(System.in);
        action = in.nextInt();//TODO Secure and limit the action choice method


        GetCredentials();


        switch (action) {
            case (1) -> Login();
            case (2) -> Register();
        }
    }

    public void Login() {
        GetCredentials();


    }

    public void Register() {
        GetCredentials();

    }


    public void GetCredentials() {
        Scanner in = new Scanner(System.in);

        System.out.print("Username:> ");

        credentials.Username = in.next();//TODO Change the input model

        System.out.print("Password:> ");
        credentials.Password = getSecurePassword();

    }

    private String getSecurePassword() {
        Scanner in = new Scanner(System.in);
        return in.next();//TODO make the pass input method secure
    }
}
