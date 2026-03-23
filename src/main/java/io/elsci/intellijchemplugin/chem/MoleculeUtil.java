package io.elsci.intellijchemplugin.chem;

import org.openscience.cdk.depict.DepictionGenerator;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesParser;

import java.awt.*;

import static io.elsci.intellijchemplugin.config.Settings.POPUP_DIMS;

public class MoleculeUtil {
    private static final int ZOOM_RATIO = POPUP_DIMS.width() / 100/*empirically found zoom ratio to have big enough scale for small molecules*/;
    private static final int QUALITY_FACTOR = 3/*to have good enough image quality when molecule popup is enlarged*/;
    private static final DepictionGenerator DEPICTION_GENERATOR = new DepictionGenerator()
            .withSize(POPUP_DIMS.width() * QUALITY_FACTOR, POPUP_DIMS.height() * QUALITY_FACTOR)
            .withZoom(ZOOM_RATIO * QUALITY_FACTOR)
            .withMargin(15);

    public static Image smilesToImage(String smiles) throws MoleculeParseException {
        try {
            return DEPICTION_GENERATOR.depict(smilesToMolecule(smiles)).toImg();
        } catch (CDKException e) {
            throw new MoleculeParseException("Couldn’t parse the SMILES string into a molecule.", e);
        }
    }

    /**
     * Attempts to find a possible SMILES in the given arbitrary string that surrounds the given position.
     */
    public static String smilesFromText(String value, int idx) {
        if (idx > value.length()) idx = value.length();
        if (idx < 0) idx = 0;

        int fr = idx - 1, to = idx;
        for (; fr >= 0                 ; fr--) if (!isValidSmileSymbol(value.charAt(fr))) break;
        for (; to <= value.length() - 1; to++) if (!isValidSmileSymbol(value.charAt(to))) break;
        return value.substring(fr + 1, to);
    }

    static IAtomContainer smilesToMolecule(String smiles) throws InvalidSmilesException {
        return new SmilesParser(SilentChemObjectBuilder.getInstance()).parseSmiles(smiles);
    }

    /**
     * atoms: '[' & ']', lowercase and uppercase letters, digits, '+' & '-' for a positive & negative charges
     * bonds: '.', '-', '=', '#', '$', ':', '/', '\'
     * rings: '%'
     * branching: '(' & ')'
     * stereochemistry: '@'
     */
    private static boolean isValidSmileSymbol(char c) {
        return between(c, 'A', 'Z') || between(c, 'a', 'z') || between(c, '0', '9')
                || c == '[' || c == ']' || c == '+' || c == '-'
                || c == '.' || c == '=' || c == '#' || c == '$' || c == ':' || c == '/' || c == '\\'
                || c == '%'
                || c == '(' || c == ')'
                || c == '@';
    }

    private static boolean between(char c, char minInclusive, char maxInclusive) {
        return c >= minInclusive && c <= maxInclusive;
    }
}
