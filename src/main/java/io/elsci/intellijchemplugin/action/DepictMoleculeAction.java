package io.elsci.intellijchemplugin.action;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiLiteralValue;
import com.intellij.psi.util.PsiTreeUtil;
import io.elsci.intellijchemplugin.chem.MoleculeParseException;
import io.elsci.intellijchemplugin.chem.MoleculeUtil;
import io.elsci.intellijchemplugin.ui.ImagePopup;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class DepictMoleculeAction extends AnAction {
    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabledAndVisible(e.getProject() != null);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        if (editor == null || psiFile == null)
            return;
        /*if a string is selected together with double quotes, the caret may be placed after the string,
          and, for example, point to a semicolon, that's why checking selection*/
        int offset = editor.getSelectionModel().hasSelection()
                ? editor.getSelectionModel().getSelectionStart()
                : editor.getCaretModel().getOffset();
        PsiElement element = psiFile.findElementAt(offset);
        PsiLiteralValue literal = PsiTreeUtil.getParentOfType(element, PsiLiteralValue.class);
        if (literal == null || !(literal.getValue() instanceof String struct))
            return;
        try {
            Image image = MoleculeUtil.smilesToImage(struct);
            JBPopup popup = ImagePopup.create(e.getProject(), image);
            popup.showInBestPositionFor(editor);
        } catch (MoleculeParseException exception) {
            //do nothing
        }
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }
}
