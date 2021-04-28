package UserInterface;

import javax.swing.*;

public class guiHandler {

    JFrame frame;

    public guiHandler() {
        frame = new JFrame("DiaCrypt");
        frame.setContentPane(new mainWindow().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
