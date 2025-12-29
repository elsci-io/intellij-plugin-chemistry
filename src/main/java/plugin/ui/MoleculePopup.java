package plugin.ui;

import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.SVGLoader;
import com.intellij.util.ui.JBImageIcon;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class MoleculePopup {
    public static JBPopup create(String svg) throws MoleculeRenderException {
        try {
            //todo: investigate if there is alternative approach without warnings
            Image image = SVGLoader.load(new ByteArrayInputStream(svg.getBytes()), 1.0f);
            JBLabel label = new JBLabel(new JBImageIcon(image));
            return JBPopupFactory.getInstance()
                    .createComponentPopupBuilder(label, null/*keyboard focus isn't needed*/)
                    .setTitle(null/*not to have header*/)
                    .setResizable(false)
                    .setMovable(false)
                    .setRequestFocus(true)
                    .createPopup();
        } catch (IOException e) {
            throw new MoleculeRenderException("Couldn’t render a molecule from the SVG string.", e);
        }
    }
}
