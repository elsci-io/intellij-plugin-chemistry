package io.elsci.intellijchemplugin.action;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.vfs.VirtualFileEvent;
import com.intellij.openapi.vfs.VirtualFileListener;
import io.elsci.intellijchemplugin.ui.MoleculePreviewFileEditor;
import org.jetbrains.annotations.NotNull;

public class MoleculeFileListener implements VirtualFileListener {

    private final MoleculePreviewFileEditor preview;

    public MoleculeFileListener(MoleculePreviewFileEditor preview) {
        this.preview = preview;
    }

    @Override
    public void contentsChanged(@NotNull VirtualFileEvent event) {
        try {
            preview.updatePreview();
        } catch (Throwable t) {
            Notifications.Bus.notify(new Notification("Molecule Preview", "Error occurred:", t.getMessage(), NotificationType.ERROR));
        }
    }
}