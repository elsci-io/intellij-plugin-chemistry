package io.elsci.intellijchemplugin.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;

import java.awt.*;

import static io.elsci.intellijchemplugin.config.Settings.POPUP_DIMS;

public class ImagePopup {
    public static JBPopup create(Project project, Image image) {
        ScalableImagePanel panel = new ScalableImagePanel(image);
        return JBPopupFactory.getInstance()
                .createComponentPopupBuilder(panel, panel)
                .setProject(project)
                .setResizable(true)
                .setMinSize(new Dimension(POPUP_DIMS.width(), POPUP_DIMS.height()))
                .setMovable(true)
                .setFocusable(true)
                .setRequestFocus(true)
                .setModalContext(false)
                .createPopup();
    }
}
