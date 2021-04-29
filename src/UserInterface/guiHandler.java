package UserInterface;

import javax.swing.*;

public final class guiHandler {

    static final String appName = "DiaCrypt";
    static final JPanel startPanel = new startWindow().mainPanel;
    static guiHandler instance;
    final JFrame frame;

    private guiHandler(String name, JPanel startPanel) {
        frame = new JFrame(name);
        frame.setContentPane(startPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static guiHandler getInstance() {
        if (instance == null)
            instance = new guiHandler(appName, startPanel);
        return instance;
    }

    public void changePanel(JPanel newPanel) {
        frame.getContentPane().removeAll();
        frame.setContentPane(newPanel);
        frame.revalidate();
        frame.pack();
    }
}
