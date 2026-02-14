package io.elsci.intellijchemplugin.action;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.psi.PsiFile;
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
        if (editor == null || psiFile == null) return;

        String struct = TextExtractor.selectedText(editor, psiFile, MoleculeUtil::smilesFromText);
        if (struct == null || struct.isEmpty()) return;
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
