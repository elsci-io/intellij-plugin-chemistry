package plugin;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.popup.JBPopup;
import org.jetbrains.annotations.NotNull;
import plugin.chemistry.MoleculeParseException;
import plugin.chemistry.MoleculeUtil;
import plugin.ui.MoleculePopup;
import plugin.ui.MoleculeRenderException;

public class ShowMoleculeAction extends AnAction {
    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabledAndVisible(e.getProject() != null);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        if (editor != null) {
            String mol = editor.getCaretModel().getPrimaryCaret().getSelectedText();
            if (mol != null) {
                mol = mol.replace("\"", "");
                try {
                    String svg = MoleculeUtil.smilesToSvg(mol);
                    JBPopup popup = MoleculePopup.create(svg);
                    popup.showInBestPositionFor(editor);
                } catch (MoleculeParseException | MoleculeRenderException exception) {
                  //do nothing
                }
            }
        }
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }
}
