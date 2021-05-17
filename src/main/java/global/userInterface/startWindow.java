package global.userInterface;

import javax.swing.*;
import java.util.Objects;

public class startWindow {
    private JButton loginButton;
    private JButton registerButton;
    public JPanel mainPanel;

    public startWindow() {
        loginButton.addActionListener(e -> Objects.requireNonNull(guiHandler.getInstance()).changePanel(new credentialsForm(true).mainPanel));
        registerButton.addActionListener(e -> Objects.requireNonNull(guiHandler.getInstance()).changePanel(new credentialsForm(false).mainPanel));
    }
}
