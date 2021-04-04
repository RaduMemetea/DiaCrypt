import UserInterface.ConsoleUserInterface;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.print("Hello Dreamer!\nPlease chose an action:\n");
        System.out.print("[1] Log In \\ [2] Register\n:> ");

        int action;

        ConsoleUserInterface ui = new ConsoleUserInterface();

        Scanner in = new Scanner(System.in);
        action = in.nextInt();//TODO Secure and limit the action choice method


        ui.GetCredentials();

        switch (action) {
            case (1) -> ui.Login();
            case (2) -> ui.Register();
        }


    }
}
