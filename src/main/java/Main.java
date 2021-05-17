import databaseContext.MariaDbContext;
import global.userInterface.guiHandler;

import javax.swing.*;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {// it's required that arg0 is the database connection username and arg1 is the password

        try {
            MariaDbContext.createInstance(args[0], args[1]); //Initialize the database driver, instance and connection.
        } catch (SQLException exception) {
            System.err.println("The attempt to initialize database connection failed!...");
            JOptionPane.showMessageDialog(new JFrame(), "The attempt to initialize database connection failed!...", "Error!", JOptionPane.ERROR_MESSAGE);
            exception.printStackTrace();
            return;
        } catch (ClassNotFoundException e) {
            System.err.println("The attempt to initialize the database driver class failed!...");
            JOptionPane.showMessageDialog(new JFrame(), "The attempt to initialize the database driver class failed!...", "Error!", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(new JFrame(), exception.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
            exception.printStackTrace();
            return;
        }

        guiHandler.getInstance();
    }
}
