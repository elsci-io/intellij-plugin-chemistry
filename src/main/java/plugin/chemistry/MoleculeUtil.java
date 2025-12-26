package plugin.chemistry;

import org.openscience.cdk.depict.DepictionGenerator;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesParser;

public class MoleculeUtil {
    public static String smilesToSvg(String smiles) throws MoleculeParseException {
        try {
            return new DepictionGenerator().depict(smilesToMolecule(smiles)).toSvgStr();
        } catch (CDKException e) {
            throw new MoleculeParseException("Couldn’t parse the SMILES string into a molecule.", e);
        }
    }

    static IAtomContainer smilesToMolecule(String smiles) throws InvalidSmilesException {
        return new SmilesParser(SilentChemObjectBuilder.getInstance()).parseSmiles(smiles);
    }
}
