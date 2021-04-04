package UserInterface;

import DataModels.User;
import DatabaseContext.IDbContext;
import DatabaseContext.MariaIDBContext;

import java.util.Scanner;

public class ConsoleUserInterface {
    User credentials = new User();
    IDbContext context;


    public ConsoleUserInterface() {
        context = new MariaIDBContext();


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
