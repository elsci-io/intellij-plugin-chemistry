package plugin.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.util.SVGLoader;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class MoleculePopup {
    public static JBPopup create(Project project, String svg) throws MoleculeRenderException {
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

    private static Image image(String svg) throws MoleculeRenderException {
        try {
            //todo: find another approach (without warnings)
            return SVGLoader.load(new ByteArrayInputStream(svg.getBytes()), 1.0f);
        } catch (IOException e) {
            throw new MoleculeRenderException("Couldn’t render a molecule from the SVG string.", e);
        }
    }
}
