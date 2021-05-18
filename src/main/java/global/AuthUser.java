package global;

import dataModels.Page;
import dataModels.User;
import dataModels.complex.FullDiary;
import databaseContext.MariaDbContext;

import javax.crypto.SecretKey;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.util.Collections;
import java.util.List;

public class AuthUser extends User {
    public SecretKey secretKey;

    private static AuthUser instance = null;

    private List<FullDiary> Diaries;

    private DefaultTreeModel tree;

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
        instance.EncryptionPassword = null;
        instance.Diaries = null;
        instance = null;
    }


    public void SetUser(User user) throws Exception {
        if (user.ID < 0 || user.Username == null) throw new Exception("Invalid User!");

        if (instance != null) DestroyInstance();

        if (instance == null) AuthUser.getInstance();

        instance.ID = user.ID;
        instance.Username = user.Username;
        instance.Password = user.Password;
        instance.EncryptionPassword = user.EncryptionPassword;

    }

    public void SetDiaries(List<FullDiary> diaries) throws Exception {
        if (instance == null) throw new Exception("No User Authenticated!");

        for (var diary : diaries) {
            diary.Title = SecurityHandler.aesDecrypt(diary.Title);
            for (var page : diary.Pages)
                page.Text = SecurityHandler.aesDecrypt(page.Text);
        }

        instance.Diaries = diaries;
    }

    public void createTree() {

        DefaultMutableTreeNode root = new DefaultMutableTreeNode(Username);

        if (Diaries != null) {

            Collections.sort(Diaries);
            for (FullDiary diary : Diaries) {
                DefaultMutableTreeNode diaryNode = new DefaultMutableTreeNode(diary);
                root.add(diaryNode);

                if (diary.Pages != null) {
                    Collections.sort(diary.Pages);
                    int n = 0;
                    for (Page page : diary.Pages) {
                        page.Number = ++n;
                        DefaultMutableTreeNode pageNode = new DefaultMutableTreeNode(page, false);
                        diaryNode.add(pageNode);
                    }
                }
            }

        }

        tree = new DefaultTreeModel(root);

    }


    public void refreshDiaries() {
        try {
            SetDiaries(MariaDbContext.getInstance().GetUserDiaries(ID));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        createTree();
    }

    public DefaultTreeModel getTree() {
        return tree;
    }
}
