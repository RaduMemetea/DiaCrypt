package global;

import dataModels.complex.*;
import dataModels.*;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
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


    public static void SetUser(User user) throws Exception {
        if (user.ID < 0 || user.Username == null) throw new Exception("Invalid User!");

        if (instance != null) DestroyInstance();

        if (instance == null) AuthUser.getInstance();

        instance.ID = user.ID;
        instance.Username = user.Username;
        instance.Password = user.Password;

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


    public void addChange(Object content) throws Exception {
        if (content == null) throw new Exception("Invalid content");
        contentChanges.add(content);
    }
}
