package global.userInterface;

import dataModels.Page;
import dataModels.complex.FullDiary;
import global.AuthUser;
import global.SecurityHandler;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import java.sql.SQLException;


public class mainWindow {

    public JPanel mainPanel;
    public JTree diariesTree;
    public DefaultMutableTreeNode selectedNode = null;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton saveButton;
    private JButton logOutButton;
    private JTextPane pageTextPane;


    private Focus addFocus;
    private Focus editFocus;
    private Focus deleteFocus;

    private boolean inPageEdit = false;
    private JButton cancelButton;

    public mainWindow() {

        enableDefaultFocus();
        diariesTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        diariesTree.setModel(AuthUser.getInstance().getTree());


        logOutButton.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(new JFrame(), "Are you sure you want to log out?", "Log Out?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) != 0)
                return;

            SecurityHandler.LogOut();
            guiHandler.getInstance().changePanel(new startWindow().mainPanel);
        });


        addButton.addActionListener(e -> {

            switch (addFocus) {

                case ROOT -> {
                    var input = JOptionPane.showInputDialog(new JFrame(), "Diary title:");

                    if (input == null || input.equals("")) {
                        JOptionPane.showMessageDialog(new JFrame(), "Invalid diary title!", "Warning!", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    try {
                        SecurityHandler.addDiary(input);
                    } catch (Exception exception) {
                        JOptionPane.showMessageDialog(new JFrame(), exception.getMessage(), "Warning!", JOptionPane.WARNING_MESSAGE);
                        exception.printStackTrace();
                        return;
                    }

                }

                case DIARY -> {

                    if (selectedNode == null || !(selectedNode.getUserObject() instanceof FullDiary fullDiary))
                        return;

                    try {
                        SecurityHandler.addPage("", (fullDiary.ID));
                    } catch (Exception exception) {
                        JOptionPane.showMessageDialog(new JFrame(), exception.getMessage(), "Warning!", JOptionPane.WARNING_MESSAGE);
                        exception.printStackTrace();
                        return;
                    }
                }
            }
            updateTree();

        });


        editButton.addActionListener(e -> {
            switch (editFocus) {
                case DIARY -> {

                    if (selectedNode == null || !(selectedNode.getUserObject() instanceof FullDiary fullDiary))
                        return;

                    var input = JOptionPane.showInputDialog(new JFrame(), "New diary title:");

                    if (input == null || input.equals("")) {
                        JOptionPane.showMessageDialog(new JFrame(), "Invalid diary title!", "Warning!", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    if (input.equals(fullDiary.Title)) {
                        JOptionPane.showMessageDialog(new JFrame(), "Title not changed!", "Warning!", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    if (input.length() < 3) {
                        JOptionPane.showMessageDialog(new JFrame(), "Title to short!", "Warning!", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    if (input.length() > 1000) {
                        JOptionPane.showMessageDialog(new JFrame(), "Title to long!", "Warning!", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    fullDiary.Title = input;

                    try {
                        SecurityHandler.updateDiaryTitle(fullDiary);
                    } catch (Exception exception) {
                        JOptionPane.showMessageDialog(new JFrame(), exception.getMessage(), "Warning!", JOptionPane.WARNING_MESSAGE);
                        exception.printStackTrace();
                    }

                    updateTree();

                }

                case PAGE -> {

                    if (selectedNode == null || !(selectedNode.getUserObject() instanceof Page page))
                        return;

                    setPageEditFocus();
                    guiHandler.getInstance().frame.setTitle("Edit: " + (((FullDiary) ((DefaultMutableTreeNode) selectedNode.getParent()).getUserObject())).Title + " - page: " + page.Number);

                }

            }

        });

        deleteButton.addActionListener(e -> {
            switch (deleteFocus) {
                case DIARY -> {

                    if (selectedNode == null || !(selectedNode.getUserObject() instanceof FullDiary fullDiary))
                        return;

                    if (JOptionPane.showConfirmDialog(new JFrame(), "Are you sure you want to delete diary " + fullDiary.Title + "?", "Delete?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) != 0)
                        return;

                    try {
                        SecurityHandler.deleteDiary(fullDiary.ID);
                    } catch (SQLException exception) {
                        JOptionPane.showMessageDialog(new JFrame(), exception.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
                        exception.printStackTrace();
                    }
                }

                case PAGE -> {

                    if (selectedNode == null || !(selectedNode.getUserObject() instanceof Page page))
                        return;

                    if (JOptionPane.showConfirmDialog(new JFrame(), "Are you sure you want to delete page " + page.Number + "?", "Delete?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) != 0)
                        return;

                    try {
                        SecurityHandler.deletePage(page.ID);
                    } catch (Exception exception) {
                        JOptionPane.showMessageDialog(new JFrame(), exception.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
                        exception.printStackTrace();
                    }
                }

            }

            updateTree();
        });


        saveButton.addActionListener(e -> {

            if (!inPageEdit) return;

            if (selectedNode == null || !(selectedNode.getUserObject() instanceof Page page)) {
                JOptionPane.showMessageDialog(new JFrame(), "The page could not be saved!", "Error!", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (pageTextPane.getText().length() >= ((2 << 16) / 4)) {
                JOptionPane.showMessageDialog(new JFrame(), "The text is to long, you are `" + (pageTextPane.getText().length() - ((2 << 16) / 4) + 1) + "` characters over the limit", "Warning!", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (pageTextPane.getText().equals(page.Text)) {
                enableDefaultFocus();
                return;
            }

            page.Text = pageTextPane.getText();

            try {
                SecurityHandler.updatePage(page);
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(new JFrame(), exception.getMessage(), "Warning!", JOptionPane.WARNING_MESSAGE);
                exception.printStackTrace();
                return;
            }

            enableDefaultFocus();
            guiHandler.getInstance().frame.setTitle("DiaCrypt");
            updateTree();
        });

        cancelButton.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(new JFrame(), "Are you sure you wanna stop editing?", "Stop edit?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) != 0)
                return;

            enableDefaultFocus();
            guiHandler.getInstance().frame.setTitle("DiaCrypt");

        });


        diariesTree.addTreeSelectionListener(e -> {

            pageTextPane.setText("");

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) diariesTree.getLastSelectedPathComponent();

            if (node == null) {
                enableDefaultFocus();
                return;
            }

            if (node.isRoot()) {
                enableRootFocus();
                return;
            }


            if (node.getUserObject() instanceof FullDiary) {
                enableDiaryFocus();
                selectedNode = node;
                return;
            }

            if (node.getUserObject() instanceof Page page) {
                enablePageFocus();
                selectedNode = node;
                pageTextPane.setText(page.Text);
            }

        });

    }

    private void enableDefaultFocus() {

        setButtonsFocus(Focus.NULL);
        selectedNode = null;

        addButton.setEnabled(false);
        addButton.setVisible(false);

        editButton.setEnabled(false);
        editButton.setVisible(false);

        deleteButton.setEnabled(false);
        deleteButton.setVisible(false);

        saveButton.setEnabled(false);
        saveButton.setVisible(false);

        cancelButton.setEnabled(false);
        cancelButton.setVisible(false);

        pageTextPane.setEnabled(false);

        diariesTree.setEnabled(true);

        inPageEdit = false;
    }

    private void enableRootFocus() {
        enableDefaultFocus();
        setButtonsFocus(Focus.ROOT);
        selectedNode = null;

        addButton.setEnabled(true);
        addButton.setVisible(true);
        addButton.setText("Add Diary");

    }

    private void enableDiaryFocus() {
        enableDefaultFocus();
        setButtonsFocus(Focus.DIARY);

        addButton.setEnabled(true);
        addButton.setVisible(true);
        addButton.setText("Add Page");

        editButton.setEnabled(true);
        editButton.setVisible(true);
        editButton.setText("Edit Diary");

        deleteButton.setEnabled(true);
        deleteButton.setVisible(true);
        deleteButton.setText("Delete Diary");


    }

    private void enablePageFocus() {
        enableDefaultFocus();
        setButtonsFocus(Focus.PAGE);

        editButton.setEnabled(true);
        editButton.setVisible(true);
        editButton.setText("Edit Page");

        deleteButton.setEnabled(true);
        deleteButton.setVisible(true);
        deleteButton.setText("Delete Page");

    }

    private void setPageEditFocus() {
        addButton.setEnabled(false);
        addButton.setVisible(false);

        editButton.setEnabled(false);
        editButton.setVisible(false);

        deleteButton.setEnabled(false);
        deleteButton.setVisible(false);

        saveButton.setEnabled(true);
        saveButton.setVisible(true);

        cancelButton.setEnabled(true);
        cancelButton.setVisible(true);

        pageTextPane.setEnabled(true);

        diariesTree.setEnabled(false);
        inPageEdit = true;
    }

    private void setButtonsFocus(Focus focus) {
        addFocus = focus;
        editFocus = focus;
        deleteFocus = focus;
    }

    private void updateTree() {
        AuthUser.getInstance().refreshDiaries();
        diariesTree.setModel(AuthUser.getInstance().getTree());
    }


    private enum Focus {
        NULL,
        ROOT,
        DIARY,
        PAGE
    }

}
