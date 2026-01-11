package plugin.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.util.SVGLoader;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ImagePopup {
    public static JBPopup create(Project project, String svg) throws ImageRenderException {
        ScalableImagePanel panel = new ScalableImagePanel(image(svg));
        return JBPopupFactory.getInstance()
                .createComponentPopupBuilder(panel, panel)
                .setProject(project)
                .setResizable(true)
                .setMinSize(new Dimension(300, 300))
                .setMovable(true)
                .setFocusable(true)
                .setRequestFocus(true)
                .setModalContext(false)
                .createPopup();
    }

    private static Image image(String svg) throws ImageRenderException {
        try {
            //todo: find another approach (without warnings)
            return SVGLoader.load(new ByteArrayInputStream(svg.getBytes()), 1.0f);
        } catch (IOException e) {
            throw new ImageRenderException("Couldn’t render an image from the SVG string.", e);
        }
    }
}
