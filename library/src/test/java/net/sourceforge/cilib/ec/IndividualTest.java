/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.ec;

import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.type.types.container.Vector;
import static org.junit.Assert.*;
import org.junit.Test;

public class IndividualTest {

    @Test
    public void testClone() {
        Vector genes = Vector.of(1,2,3,4,5);
        Individual i = new Individual();
        i.setCandidateSolution(genes);

        Individual clone = i.getClone();

        assertEquals(5, i.getDimension());
        assertEquals(5, clone.getDimension());
        assertEquals(i.getDimension(), clone.getDimension());

        Vector cloneVector = (Vector) clone.getCandidateSolution();

        for (int k = 0; k < cloneVector.size(); k++) {
            assertEquals(genes.get(k), cloneVector.get(k));
        }
    }

    @Test
    public void equals() {
        Individual i1 = new Individual();
        Individual i2 = new Individual();

        assertFalse(i1.equals(i2));
        assertFalse(i2.equals(i1));
        assertTrue(i1.equals(i1));

        assertFalse(i1.equals(null));
    }

    @Test
    public void hashCodes() {
        Individual i1 = new Individual();
        Individual i2 = new Individual();

        assertTrue(i1.hashCode() != i2.hashCode());
    }

}
