package global.userInterface;

import global.SecurityHandler;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class credentialsForm {
    public JPanel mainPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel pass2Label;
    private JPasswordField passwordField2;
    private JButton submitButton;
    private JButton cancelButton;

    public credentialsForm(boolean formType) {

        if (formType) {       // True for login; False for register
            submitButton.setText("Log in");
            mainPanel.setPreferredSize(new Dimension(250, 130));
        } else {
            submitButton.setText("Register");
            mainPanel.setPreferredSize(new Dimension(250, 180));
        }

        pass2Label.setEnabled(!formType);
        pass2Label.setVisible(!formType);
        passwordField2.setEnabled(!formType);
        passwordField2.setVisible(!formType);


        submitButton.addActionListener(e -> {

            if (formType) {
                try {
                    SecurityHandler.createUserSession(usernameField.getText(), passwordField.getPassword());
                } catch (SQLException exception) {
                    exception.printStackTrace();
                    return;
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(new JFrame(), exception.getMessage(), "Warning!", JOptionPane.WARNING_MESSAGE);
                    exception.printStackTrace();
                    return;
                }
                guiHandler.getInstance().changePanel(new mainWindow().mainPanel);

            } else {
                try {
                    SecurityHandler.createUser(usernameField.getText(), passwordField.getPassword(), passwordField2.getPassword());
                } catch (SQLException exception) {
                    exception.printStackTrace();
                    return;
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(new JFrame(), exception.getMessage(), "Warning!", JOptionPane.WARNING_MESSAGE);
                    exception.printStackTrace();
                    return;
                }
                guiHandler.getInstance().changePanel(new startWindow().mainPanel);
            }

        });

        cancelButton.addActionListener(e -> guiHandler.getInstance().changePanel(new startWindow().mainPanel));
    }
}
