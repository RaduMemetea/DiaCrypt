package userInterface;

import dataModels.Page;
import dataModels.complex.FullDiary;
import global.AuthUser;
import global.SecurityHandler;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;


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
    private Node selectedNode = new Node();

    public mainWindow() {

        diariesTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        enableDefaultFocus();


        try {
            diariesTree.setModel(AuthUser.getInstance().createTree());
        } catch (Exception exception) {
            System.err.println(exception.getMessage());
        }


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
                    try {
                        SecurityHandler.addDiary(input);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }

                    //todo update tree
                }
                case DIARY -> {

                    if (selectedNode.type != Focus.DIARY) {
                        JOptionPane.showMessageDialog(new JFrame(), "Please select a diary!", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    Page newPage;
                    try {
                        newPage = SecurityHandler.addPage("", ((FullDiary) selectedNode.object).ID);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        return;
                    }

                    setPageEditFocus(true);
                    guiHandler.getInstance().frame.setTitle("Editing " + ((FullDiary) selectedNode.object).Title + " - page: " + newPage.Number);

                    //todo  update tree

                }

            }


        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (editFocus) {
                    case DIARY -> {

                        //todo prompt to get new diary title, update tree

                    }
                    case PAGE -> {

                        //todo change focus to page content

                    }

                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (deleteFocus) {
                    case DIARY -> {

                        //todo get confirmation, delete diary, update tree

                    }
                    case PAGE -> {

                        //todo get confirmation, delete page, update tree

                    }

                }
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
                enableDefaultFocus();
                guiHandler.getInstance().frame.setTitle("DiaCrypt");

            }
        });


        diariesTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {

                DefaultMutableTreeNode node = (DefaultMutableTreeNode) diariesTree.getLastSelectedPathComponent();

                if (node == null) {
                    enableDefaultFocus();
                    selectedNode.type = Focus.NULL;
                    selectedNode = null;
                    return;
                }

                if (node.isRoot()) {
                    enableRootFocus();
                    selectedNode.type = Focus.ROOT;
                    selectedNode = null;
                    return;
                }

                if (node.isLeaf()) {
                    enablePageFocus();
                    selectedNode.type = Focus.PAGE;
                    selectedNode.object = node.getUserObject();

                    return;
                }

                enableDiaryFocus();
                selectedNode.type = Focus.DIARY;
                selectedNode.object = node.getUserObject();

            }
        });

    }

    private void enableDefaultFocus() {

        setButtonsFocus(Focus.NULL);

        addButton.setEnabled(false);
        addButton.setVisible(false);

        editButton.setEnabled(false);
        editButton.setVisible(false);

        deleteButton.setEnabled(false);
        deleteButton.setVisible(false);


        setPageEditFocus(false);
    }

    private void enableRootFocus() {

        setButtonsFocus(Focus.ROOT);

        addButton.setEnabled(true);
        addButton.setVisible(true);
        addButton.setText("Add Diary");

        editButton.setEnabled(false);
        editButton.setVisible(false);

        deleteButton.setEnabled(false);
        deleteButton.setVisible(false);


        setPageEditFocus(false);
    }

    private void enableDiaryFocus() {

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


        setPageEditFocus(false);
    }

    private void enablePageFocus() {
        setButtonsFocus(Focus.PAGE);

        addButton.setEnabled(false);
        addButton.setVisible(false);

        editButton.setEnabled(true);
        editButton.setVisible(true);
        editButton.setText("Edit Page");

        deleteButton.setEnabled(true);
        deleteButton.setVisible(true);
        deleteButton.setText("Delete Page");

        setPageEditFocus(false);
    }

    private void setPageEditFocus(boolean state) {
        if (state) enableDefaultFocus();
        saveButton.setEnabled(state);
        saveButton.setVisible(state);

        cancelButton.setEnabled(state);
        cancelButton.setVisible(state);

        pageTextPane.setEnabled(state);

        diariesTree.setEnabled(!state);
    }

    private void setButtonsFocus(Focus focus) {
        addFocus = focus;
        editFocus = focus;
        deleteFocus = focus;
    }

    enum Focus {
        NULL,
        ROOT,
        DIARY,
        PAGE
    }

    class Node {
        public Focus type;
        public Object object;
    }

}
