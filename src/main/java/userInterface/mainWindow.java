package userInterface;

import dataModels.Page;
import dataModels.complex.FullDiary;
import global.AuthUser;
import global.SecurityHandler;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import java.sql.SQLException;


public class mainWindow {

    private JButton addButton;
    private JButton editButton;

    public JPanel mainPanel;
    public JTree diariesTree;
    private JButton logOutButton;
    private JButton saveButton;
    private JTextPane pageTextPane;
    private JButton cancelButton;
    private JButton deleteButton;

    private Focus addFocus;
    private Focus editFocus;
    private Focus deleteFocus;

    private final Node selectedNode = new Node();
    private boolean inPageEdit = false;

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

                    if (selectedNode.object == null || !(selectedNode.object.getUserObject() instanceof FullDiary fullDiary))
                        return;

                    Page newPage;

                    try {
                        newPage = SecurityHandler.addPage("", (fullDiary.ID));
                    } catch (Exception exception) {
                        JOptionPane.showMessageDialog(new JFrame(), exception.getMessage(), "Warning!", JOptionPane.WARNING_MESSAGE);
                        exception.printStackTrace();
                        return;
                    }

                    setPageEditFocus();
                    guiHandler.getInstance().frame.setTitle("Edit: " + (fullDiary.Title + " - page: " + newPage.Number));

                }

            }
            updateTree();

        });


        editButton.addActionListener(e -> {
            switch (editFocus) {
                case DIARY -> {

                    if (selectedNode.object == null || !(selectedNode.object.getUserObject() instanceof FullDiary fullDiary))
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
                    } catch (SQLException exception) {
                        JOptionPane.showMessageDialog(new JFrame(), exception.getMessage(), "Warning!", JOptionPane.WARNING_MESSAGE);
                        exception.printStackTrace();
                    }


                }

                case PAGE -> {

                    if (selectedNode.object == null || !(selectedNode.object.getUserObject() instanceof Page page))
                        return;

                    setPageEditFocus();
                    guiHandler.getInstance().frame.setTitle("Edit: " + (((FullDiary) ((DefaultMutableTreeNode) selectedNode.object.getParent()).getUserObject())).Title + " - page: " + page.Number);

                }

            }
            updateTree();

        });

        deleteButton.addActionListener(e -> {
            switch (deleteFocus) {
                case DIARY -> {

                    if (selectedNode.object == null || !(selectedNode.object.getUserObject() instanceof FullDiary fullDiary))
                        return;

                    if (JOptionPane.showConfirmDialog(new JFrame(), "Are you sure you want to delete diary " + fullDiary.Title + "?", "Delete?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) != 0)
                        return;

                    SecurityHandler.deleteDiary(fullDiary.ID);
                }

                case PAGE -> {

                    if (selectedNode.object == null || !(selectedNode.object.getUserObject() instanceof Page page))
                        return;

                    if (JOptionPane.showConfirmDialog(new JFrame(), "Are you sure you want to delete page " + page.Number + "?", "Delete?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) != 0)
                        return;

                    SecurityHandler.deletePage(page.ID);
                }

            }

            updateTree();
        });


        saveButton.addActionListener(e -> {

            if (!inPageEdit) return;

            if (selectedNode.object == null || !(selectedNode.object.getUserObject() instanceof Page page)) {
                JOptionPane.showMessageDialog(new JFrame(), "The page could not be saved!", "Error!", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (pageTextPane.getText().length() >= (2 ^ 16)) {
                JOptionPane.showMessageDialog(new JFrame(), "The text is to long, you are `" + (pageTextPane.getText().length() - (2 ^ 16) + 1) + "` characters over the limit", "Warning!", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (pageTextPane.getText().equals(page.Text)) {
                enableDefaultFocus();
                return;
            }

            page.Text = pageTextPane.getText();

            try {
                SecurityHandler.updatePage(page);
            } catch (SQLException exception) {
                JOptionPane.showMessageDialog(new JFrame(), exception.getMessage(), "Warning!", JOptionPane.WARNING_MESSAGE);
                exception.printStackTrace();
                return;
            }

            enableDefaultFocus();
            guiHandler.getInstance().frame.setTitle("DiaCrypt");
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

            if (node.isLeaf()) {
                enablePageFocus();
                selectedNode.type = Focus.PAGE;
                selectedNode.object = node;

                if (selectedNode.object.getUserObject() instanceof Page page)
                    pageTextPane.setText(page.Text);

                return;
            }

            enableDiaryFocus();
            selectedNode.type = Focus.DIARY;
            selectedNode.object = node;


        });

    }

    private void enableDefaultFocus() {

        setButtonsFocus(Focus.NULL);
        selectedNode.type = Focus.NULL;
        selectedNode.object = null;

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
        selectedNode.type = Focus.ROOT;
        selectedNode.object = null;

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
        //todo do this, its important!
    }


    private enum Focus {
        NULL,
        ROOT,
        DIARY,
        PAGE
    }

    private static class Node {
        public Focus type;
        public DefaultMutableTreeNode object;
    }

}
