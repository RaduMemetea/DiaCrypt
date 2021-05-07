package UserInterface;

import global.AuthUser;
import global.SecurityHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class mainWindow {
    public JPanel mainPanel;
    public JTree diariesTree;
    private JButton logOutButton;
    private JButton saveButton;
    private JTextPane pageTextPane;
    private JButton cancelButton;
    private JButton addDiary;
    private JButton addPageButton;

    public mainWindow() {
        structureOrPageFocus(true);

        try {
            diariesTree.setModel(AuthUser.getInstance().createTree());
        } catch (Exception exception) {
            System.err.println(exception.getMessage());
        }


        logOutButton.addActionListener(e -> {
            if (!guiHandler.getInstance().getConfirmation("Are you sure you want to log out?")) return;
            SecurityHandler.LogOut();
            guiHandler.getInstance().changePanel(new startWindow().mainPanel);
        });


        addDiary.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });


        addPageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }


    private void structureOrPageFocus(boolean state) {//True is structure false is page
        if (state) {
            addDiary.setEnabled(state);
            addPageButton.setEnabled(state);
            diariesTree.setEnabled(state);

            pageTextPane.setEditable(!state);
            saveButton.setEnabled(!state);
            cancelButton.setEnabled(!state);
        } else {
            addDiary.setEnabled(!state);
            addPageButton.setEnabled(!state);
            diariesTree.setEnabled(!state);

            pageTextPane.setEditable(state);
            saveButton.setEnabled(state);
            cancelButton.setEnabled(state);
        }
    }

}
