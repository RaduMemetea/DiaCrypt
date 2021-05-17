package global.userInterface;

import javax.swing.*;

public class startWindow {
    private JButton loginButton;
    private JButton registerButton;
    public JPanel mainPanel;

    public startWindow() {
        loginButton.addActionListener(e -> guiHandler.getInstance().changePanel(new credentialsForm(true).mainPanel));
        registerButton.addActionListener(e -> guiHandler.getInstance().changePanel(new credentialsForm(false).mainPanel));
    }
}
