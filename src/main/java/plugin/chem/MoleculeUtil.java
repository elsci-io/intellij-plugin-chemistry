package plugin.chem;

import org.openscience.cdk.depict.DepictionGenerator;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesParser;

import static org.openscience.cdk.depict.Depiction.UNITS_PX;
import static plugin.config.Settings.POPUP_DIMS;

public class MoleculeUtil {
    private static final int ZOOM_RATIO = POPUP_DIMS.width() / 100/*empirically found zoom ratio to have big enough scale for small molecules*/;
    private static final int QUALITY_FACTOR = 3/*to have good enough image quality when molecule popup is enlarged*/;
    private static final DepictionGenerator DEPICTION_GENERATOR = new DepictionGenerator()
            .withSize(POPUP_DIMS.width() * QUALITY_FACTOR, POPUP_DIMS.height() * QUALITY_FACTOR)
            .withZoom(ZOOM_RATIO * QUALITY_FACTOR)
            .withMargin(15);

    public static String smilesToSvg(String smiles) throws MoleculeParseException {
        try {
            return DEPICTION_GENERATOR.depict(smilesToMolecule(smiles)).toSvgStr(UNITS_PX);
        } catch (CDKException e) {
            throw new MoleculeParseException("Couldn’t parse the SMILES string into a molecule.", e);
        }
    }

    static IAtomContainer smilesToMolecule(String smiles) throws InvalidSmilesException {
        return new SmilesParser(SilentChemObjectBuilder.getInstance()).parseSmiles(smiles);
    }
}
