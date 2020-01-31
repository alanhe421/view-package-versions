package cn.alanhe;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.IPopupChooserBuilder;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ViewPackageVersions extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        assert project != null;
        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        assert editor != null;
        VirtualFile file = FileDocumentManager.getInstance().getFile(editor.getDocument());
        int offset = editor.getCaretModel().getOffset();

        assert file != null;
        PsiElement element = Objects.requireNonNull(PsiManager.getInstance(project).findFile(file)).findElementAt(offset);
        PsiElement firstParent = PsiTreeUtil.findFirstParent(element, new Condition<PsiElement>() {
            @Override
            public boolean value(PsiElement psiElement) {
                return true;
            }
        });
        assert firstParent != null;
        String packageName = getPackageName((LeafPsiElement) firstParent);
        try {
            List<String> versions = this.getPackageVersions(packageName);
            this.showPopup(packageName, versions, editor);
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    @NotNull
    private String getPackageName(LeafPsiElement firstParent) {
        String name = firstParent.getText();
        return name.substring(1, name.length() - 1);
    }

    public List<String> getPackageVersions(String name) throws IOException,
            InterruptedException {
        final String command = "npm view " + name + " versions --json";
        Process p = Runtime.getRuntime().exec(command);
        p.waitFor();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                p.getInputStream()));
        String line;
        StringBuilder builder = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        Type type = new TypeToken<List<String>>() {
        }.getType();
        Gson converter = new Gson();
        List<String> result = converter.fromJson(builder.toString(), type);
        Collections.reverse(result);
        return result;
    }

    private void showPopup(String name, List<String> versions, Editor editor) {
        IPopupChooserBuilder<String> popupChooserBuilder = JBPopupFactory.getInstance().createPopupChooserBuilder(versions);
        popupChooserBuilder.setTitle(name);
        JBPopup popup = popupChooserBuilder.createPopup();
        popup.setMinimumSize(new Dimension(80, 0));
        popup.showInBestPositionFor(editor);
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        String filename = psiFile.getVirtualFile().getName();
        Presentation presentation = e.getPresentation();
        if ("package.json".equals(filename)) {
            presentation.setEnabledAndVisible(true);
            return;
        }
        presentation.setEnabledAndVisible(false);
    }
}
