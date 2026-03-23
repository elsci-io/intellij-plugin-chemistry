package io.elsci.intellijchemplugin.chem;

import org.junit.Test;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;

import static io.elsci.intellijchemplugin.chem.MoleculeUtil.smilesFromText;
import static io.elsci.intellijchemplugin.chem.MoleculeUtil.smilesToMolecule;
import static io.qala.datagen.RandomShortApi.integer;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MoleculeUtilTest {
    @Test public void parsesSmilesIntoMolecule() throws InvalidSmilesException {
        IAtomContainer molecule = smilesToMolecule("[Na+].[Cl-]");
        assertEquals(2, molecule.getAtomCount());
        assertEquals(11, molecule.getAtom(0).getAtomicNumber().intValue());
        assertEquals(17, molecule.getAtom(1).getAtomicNumber().intValue());
    }

    @Test public void findsPossibleSmilesFromArbitraryString() {
        assertEquals("Na", smilesFromText("Na", -1));
        assertEquals("Na", smilesFromText("Na",  0));
        assertEquals("Na", smilesFromText("Na",  1));
        assertEquals("Na", smilesFromText("Na",  2));
        assertEquals("Na", smilesFromText("Na",  3));

        assertEquals("Na" , smilesFromText("Na O=O Cl", integer(0, 2)));
        assertEquals("O=O", smilesFromText("Na O=O Cl", integer(3, 6)));
        assertEquals("Cl" , smilesFromText("Na O=O Cl", integer(7, 9)));

        assertTrue(smilesFromText("Na  Cl", 3).isEmpty());
    }
}
