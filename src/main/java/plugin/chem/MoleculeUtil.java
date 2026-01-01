package plugin.chem;

import org.openscience.cdk.depict.DepictionGenerator;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesParser;

public class MoleculeUtil {
    private static final DepictionGenerator DEPICTION_GENERATOR = new DepictionGenerator().withSize(100, 100).withZoom(3);

    public static String smilesToSvg(String smiles) throws MoleculeParseException {
        try {
            return DEPICTION_GENERATOR.depict(smilesToMolecule(smiles)).toSvgStr();
        } catch (CDKException e) {
            throw new MoleculeParseException("Couldn’t parse the SMILES string into a molecule.", e);
        }
    }

    static IAtomContainer smilesToMolecule(String smiles) throws InvalidSmilesException {
        return new SmilesParser(SilentChemObjectBuilder.getInstance()).parseSmiles(smiles);
    }
}
