package plugin.chem;

import org.junit.Test;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;

import static org.junit.Assert.assertEquals;

public class MoleculeUtilTest {
    @Test
    public void parsesSmilesIntoMolecule() throws InvalidSmilesException {
        IAtomContainer molecule = MoleculeUtil.smilesToMolecule("[Na+].[Cl-]");
        assertEquals(2, molecule.getAtomCount());
        assertEquals(11, molecule.getAtom(0).getAtomicNumber().intValue());
        assertEquals(17, molecule.getAtom(1).getAtomicNumber().intValue());
    }
}