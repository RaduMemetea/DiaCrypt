package global;

import DataModels.Complex.*;
import DataModels.*;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AuthUser extends User {

    private static AuthUser instance = null;

    private List<FullDiary> Diaries;

    private DefaultTreeModel tree;

    private List<Object> contentChanges;

    private AuthUser() {
    }

    public static AuthUser getInstance() {
        if (instance == null)
            instance = new AuthUser();
        return instance;
    }

    public static void DestroyInstance() {
        instance.ID = 0;
        instance.Username = null;
        instance.Password = null;
        instance.Diaries = null;
        instance = null;
    }


    public static boolean SetUser(User user) {
        if (user.ID < 0 || user.Username == null) return false;

        if (instance != null) DestroyInstance();

        if (instance == null) AuthUser.getInstance();

        instance.ID = user.ID;
        instance.Username = user.Username;
        instance.Password = user.Password;

        return true;
    }


    public void addDiary(FullDiary diary) throws Exception {

        if (diary == null || diary.ID < 1 || diary.Title == null)
            throw new Exception("Invalid diary information's!");

        if (Diaries == null)
            Diaries = new ArrayList<>();

        Diaries.add(diary);

    }

    public DefaultTreeModel createTree() throws Exception {

        if (instance == null) throw new Exception("No User Authenticated!");

        DefaultMutableTreeNode root = new DefaultMutableTreeNode(Username);

        if (Diaries != null) {

            Collections.sort(Diaries);
            for (FullDiary diary : Diaries) {
                DefaultMutableTreeNode diaryNode = new DefaultMutableTreeNode(diary);
                root.add(diaryNode);

                Collections.sort(diary.Pages);
                for (Page page : diary.Pages) {
                    DefaultMutableTreeNode pageNode = new DefaultMutableTreeNode(page, false);
                    diaryNode.add(pageNode);
                }
            }

        }

        tree = new DefaultTreeModel(root);

        return tree;

    }


    public void addChange(Object content) {
        if (content == null) return;
        contentChanges.add(content);
    }
}
