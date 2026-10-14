package io.elsci.intellijchemplugin.ui;

import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorPolicy;
import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.fileEditor.impl.text.TextEditorProvider;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

public class MoleculeFileEditorProvider implements FileEditorProvider, DumbAware {
    @Override
    public boolean accept(@NotNull Project project, @NotNull VirtualFile file) {
        return file.getName().toLowerCase().endsWith(".mol") || file.getName().toLowerCase().endsWith(".sdf");
    }

    @Override
    public @NotNull FileEditor createEditor(@NotNull Project project, @NotNull VirtualFile file) {
        TextEditor editor = (TextEditor) TextEditorProvider.getInstance().createEditor(project, file);
        MoleculePreviewFileEditor preview = new MoleculePreviewFileEditor(file);
        return new MoleculeEditorWithPreview(editor, preview);
    }

    @Override
    public @NotNull String getEditorTypeId() {
        return "molecule-editor";
    }

    @Override
    public @NotNull FileEditorPolicy getPolicy() {
        return FileEditorPolicy.HIDE_DEFAULT_EDITOR;
    }
}