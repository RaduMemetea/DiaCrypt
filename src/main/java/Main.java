import databaseContext.*;
import userInterface.*;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {// it's required that arg0 is the database connection username and arg1 is the password

        try {
            MariaDbContext.createInstance(args[0], args[1]); //Initialize the database driver, instance and connection.
        } catch (SQLException exception) {
            System.err.println("The attempt to initialize database connection failed!...");
            exception.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("The attempt to initialize the database driver class failed!...");
            e.printStackTrace();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        guiHandler.getInstance();

    }
}
