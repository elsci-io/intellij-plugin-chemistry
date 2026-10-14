package io.elsci.intellijchemplugin.ui;

import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorLocation;
import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.fileEditor.FileEditorStateLevel;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.ui.jcef.JBCefBrowser;
import io.elsci.intellijchemplugin.action.MoleculeFileListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComponent;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.iterator.IteratingSDFReader;
import org.openscience.cdk.silent.SilentChemObjectBuilder;

import java.beans.PropertyChangeListener;
import java.nio.charset.StandardCharsets;

import static io.elsci.intellijchemplugin.chem.MoleculeUtil.DEPICTION_GENERATOR_MOLV3000;
import static io.elsci.intellijchemplugin.chem.MoleculeUtil.molvToMolecule;

public class MoleculePreviewFileEditor implements FileEditor {
    private final VirtualFile file;
    private final JBCefBrowser browser;
    private final MoleculeFileListener listener;

    public MoleculePreviewFileEditor(VirtualFile file) {
        this.file = file;
        browser = new JBCefBrowser();
        listener = new MoleculeFileListener(this);
        VirtualFileManager.getInstance().addVirtualFileListener(listener);
        updatePreview();
    }

    public void updatePreview() {
        try {
            String content = new String(file.contentsToByteArray(), StandardCharsets.UTF_8);
            StringBuilder svgs = new StringBuilder();
            int count = 0;
            if (file.getName().toLowerCase().endsWith(".sdf")) {
                IteratingSDFReader reader = new IteratingSDFReader(new java.io.StringReader(content), SilentChemObjectBuilder.getInstance());
                while (reader.hasNext() && count < 1000) {
                    IAtomContainer molecule;
                    molecule = reader.next();
                    String svg = DEPICTION_GENERATOR_MOLV3000.withAtomColors().depict(molecule).toSvgStr();
                    int start = svg.indexOf("<svg");
                    if (start >= 0) {
                        svg = svg.substring(start);
                    }
                    svgs.append("<div class='molecule'>");
                    svgs.append(svg);
                    svgs.append("</div>");
                    count++;
                }
                reader.close();
            } else {
                IAtomContainer molecule = molvToMolecule(content);
                String svg = DEPICTION_GENERATOR_MOLV3000.withAtomColors().depict(molecule).toSvgStr();
                int start = svg.indexOf("<svg");
                if (start >= 0) {
                    svg = svg.substring(start);
                }
                svgs.append("<div class='molecule'>");
                svgs.append(svg);
                svgs.append("</div>");
            }
            String html = getString(svgs);
            browser.loadHTML(html);
        } catch (Exception e) {
            browser.loadHTML("<html><body>" + e.getClass() + " " + e.getMessage() + "</body></html>");
        }
    }

    private static @NotNull String getString(StringBuilder svgs) {
        return """
                <!DOCTYPE html>
                        <html>
                        <head>
                            <style>
                                html, body {
                                    width: 100%;
                                    min-height: 100%;
                                    margin: 0;
                                }
                                body {
                                    overflow: auto;
                                }
                                        .molecules {
                                             width: 100%;
                                             min-height: 100%;
                                             display: flex;
                                             flex-wrap: wrap;
                                             align-items: flex-start;
                                             align-content: flex-start;
                                             gap: 10px;
                                         }
                                         .molecule {
                                             display: flex;
                                             justify-content: center;
                                             align-items: center;
                                         }
                                         .molecule svg {
                                             width: 400px;
                                             height: 400px;
                                             border: 1px solid
                                         }
                            </style>
                            <script>
                                let zoom = 1;
                                         function updateZoom() {
                                             document.querySelectorAll(".molecule svg").forEach(svg => {
                                             svg.style.width = (400 * zoom) + "px";
                                             svg.style.height = (400 * zoom) + "px";
                                             });
                                         }
                                         function zoomIn() {
                                             zoom += 0.2;
                                             updateZoom();
                                         }
                                         function zoomOut() {
                                             if (zoom > 0.2) {
                                                 zoom -= 0.2;
                                             }
                                             updateZoom();
                                         }
                                document.addEventListener("keydown", (event)=>{
                                    if ((event.metaKey || event.ctrlKey) && event.key === "+" ) {
                                       zoomIn()
                                    }
                                    if ((event.metaKey || event.ctrlKey) && event.key === "-" ) {
                                       zoomOut()
                                    }
                                });
                            </script>
                        </head>
                        <body>
                        <div class="molecules">
                """ + svgs + """
                                </div>
                                </body>
                                </html>
                """;
    }

    @Override
    public @NotNull JComponent getComponent() {
        return browser.getComponent();
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return browser.getComponent();
    }

    @Override
    public @NotNull String getName() {
        return "Preview";
    }

    @Override
    public VirtualFile getFile() {
        return file;
    }

    @Override
    public boolean isValid() {
        return file.isValid();
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void setState(@NotNull FileEditorState state) {
    }

    @Override
    public @NotNull FileEditorState getState(
            @NotNull FileEditorStateLevel level) {
        return FileEditorState.INSTANCE;
    }

    @Override
    public void addPropertyChangeListener(
            @NotNull PropertyChangeListener listener) {
    }

    @Override
    public void removePropertyChangeListener(
            @NotNull PropertyChangeListener listener) {
    }

    @Override
    public void dispose() {
        VirtualFileManager.getInstance().removeVirtualFileListener(listener);
        browser.dispose();
    }

    @Override
    public FileEditorLocation getCurrentLocation() {
        return null;
    }

    @Override
    public @Nullable <T> T getUserData(
            @NotNull Key<T> key) {
        return null;
    }

    @Override
    public <T> void putUserData(
            @NotNull Key<T> key,
            @Nullable T t) {
    }
}