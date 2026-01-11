package plugin.chem;

import org.openscience.cdk.depict.DepictionGenerator;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesParser;

import static org.openscience.cdk.depict.Depiction.UNITS_PX;

public class MoleculeUtil {
    private static final DepictionGenerator DEPICTION_GENERATOR = new DepictionGenerator().withSize(300 * 5, 300 * 5).withZoom(3 * 5);

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
