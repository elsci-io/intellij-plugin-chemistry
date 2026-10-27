package io.elsci.intellijchemplugin.ui;

import com.intellij.notification.Notification;
import com.intellij.notification.Notifications;
import com.intellij.notification.NotificationType;
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

import org.openscience.cdk.depict.DepictionGenerator;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.iterator.IteratingSDFReader;
import org.openscience.cdk.silent.SilentChemObjectBuilder;

import java.beans.PropertyChangeListener;

import static io.elsci.intellijchemplugin.chem.MoleculeUtil.DEPICTION_GENERATOR_MOL;

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
            StringBuilder svgs = new StringBuilder();
            int count = 0;
                IteratingSDFReader reader = new IteratingSDFReader(file.getInputStream(), SilentChemObjectBuilder.getInstance());
                while (reader.hasNext() && count < 1000) {
                    IAtomContainer molecule;
                    molecule = reader.next();
                    depictMolecule(DEPICTION_GENERATOR_MOL, molecule, svgs);
                    count++;
                }
            String html;
            if (count > 1) {
                html = getString(svgs, count+ " molecules");
            }
            else{
                html = getString(svgs, count+ " molecule");
            }
            browser.loadHTML(html);
        } catch (Exception e) {
            Notifications.Bus.notify(new Notification("Molecule Preview", "Error occurred:", e.getMessage(), NotificationType.ERROR));
        }
    }
    private void depictMolecule(DepictionGenerator dg, IAtomContainer molecule, StringBuilder svgs) throws CDKException {
        String svg = dg.withAtomColors().depict(molecule).toSvgStr();
        int start = svg.indexOf("<svg");
        if (start >= 0) {
            svg = svg.substring(start);
        }
        svgs.append("<div class='molecule'>");
        svgs.append(svg);
        svgs.append("</div>");
        dg.withAtomColors().depict(molecule).toSvgStr();
    }

    private static @NotNull String getString(StringBuilder svgs, String numberOfMolecules) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <style>
                    header {
                        display: flex;
                        align-items: center;
                        gap: 10px;
                        padding: 10px 15px;
                        border: 1px solid #ccc;
                        position: sticky;
                        top: 0;
                        z-index: 100;
                        background: #E8EDF2;
                    }
                    header h6 {
                        font-size: 18px;
                        margin: 0;
                        margin-right: auto;
                        font-weight: normal;
                    }
                
                    header button {
                        width: 25px;
                        height: 25px;
                        border: 1px solid #aaa;
                        border-radius: 5px;
                        font-size: 18px;
                        cursor: pointer;
                    }
                        html, body {
                            width: 100%;
                            min-height: 100%;
                            margin: 0;
                            background: #F2F3F5;
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
                                     border: 1px solid #C5CBD3
                                 }
                    </style>
                    <script>
                        let zoom = 1;
                function updateZoom() {
                  document.querySelectorAll(".molecule svg").forEach(svg => {
                    svg.style.width = (400 * zoom) + "px";
                    svg.style.height = (400 * zoom) + "px";
                  });
                };
                function zoomIn() {
                  zoom += 0.2;
                  updateZoom();
                };
                function zoomOut() {
                  if (zoom > 0.2) {
                    zoom -= 0.2;
                  };
                  updateZoom();
                };
                document.addEventListener("keydown", (event)=>{
                  if ((event.metaKey || event.ctrlKey) && event.key === "+" ) {
                    zoomIn();
                  };
                  if ((event.metaKey || event.ctrlKey) && event.key === "-" ) {
                    zoomOut();
                  }
                });
                document.addEventListener("DOMContentLoaded", () => {
                       document.getElementById("zoomInBtn").addEventListener("click", zoomIn);
                       document.getElementById("zoomOutBtn").addEventListener("click", zoomOut);
                   });
                
                    </script>
                </head>
                <body>
                <header>
                    <h6>
                """+ numberOfMolecules + """
                    </h6>
                    <button id="zoomInBtn">+</button>
                    <button id="zoomOutBtn">-</button>
                </header>
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