package io.elsci.intellijchemplugin.action;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiLiteralValue;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.function.BiFunction;

import static io.elsci.intellijchemplugin.action.StringUtil.trimQuotesAndWhitespaces;

class TextExtractor {
    public static String selectedText(Editor editor, PsiFile psiFile,
                                      BiFunction<String, Integer, String> plainTextPostProcessor) {
        //extracting explicitly selected text
        if (editor.getSelectionModel().hasSelection())
            return trimQuotesAndWhitespaces(editor.getSelectionModel().getSelectedText());

        //extracting when the caret points to literal value (e.g., in java, python, etc. files)
        int offset = editor.getCaretModel().getOffset();
        PsiElement element = psiFile.findElementAt(offset);
        PsiLiteralValue literal = PsiTreeUtil.getParentOfType(element, PsiLiteralValue.class);
        if (literal != null && literal.getValue() instanceof String value) return value;

        //extracting when the caret points to plain text value (e.g., in txt, csv, etc. files)
        Document doc = editor.getDocument();
        int lineNum = doc.getLineNumber(offset);
        int offsetFr = doc.getLineStartOffset(lineNum);
        int offsetTo = doc.getLineEndOffset(lineNum);
        String value = doc.getText(new TextRange(offsetFr, offsetTo));
        return trimQuotesAndWhitespaces(value);
    }
}
