import DatabaseContext.*;
import UserInterface.*;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {// it's required that arg0 is the database connection username and arg1 is the password

        try {
            MariaDbContext.createInstance(args[0], args[1]); //Initialize the database driver, instance and connection.
        } catch (SQLException throwables) {
            System.err.println("The attempt to initialize database connection failed!...");
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("The attempt to initialize the database driver class failed!...");
            e.printStackTrace();
        }


        guiHandler gui = new guiHandler();

        //encrypt each diary and page with a password uniquely generated for each user

        // new ConsoleUserInterface();
//
//        try {
//            System.out.println(MariaDbContext.getInstance().GetUserSalt("Kity"));
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }

    }
}
