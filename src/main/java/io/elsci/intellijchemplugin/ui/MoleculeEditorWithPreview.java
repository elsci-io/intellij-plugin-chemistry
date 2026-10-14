package io.elsci.intellijchemplugin.ui;

import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.fileEditor.TextEditorWithPreview;
import org.jetbrains.annotations.NotNull;

public class MoleculeEditorWithPreview extends TextEditorWithPreview {
    public MoleculeEditorWithPreview(
            @NotNull TextEditor editor,
            @NotNull MoleculePreviewFileEditor preview) {
        super(editor, preview, "Molecule", Layout.SHOW_EDITOR_AND_PREVIEW);
    }
}