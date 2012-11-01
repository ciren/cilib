/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.type;

import net.sourceforge.cilib.type.types.Types;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class StringBasedDomainRegistryTest {

    @Test
    public void structureExists() {
        DomainRegistry registry = new StringBasedDomainRegistry();
        registry.setDomainString("R(-30.0:30)^30");

        Vector vector = (Vector) registry.getBuiltRepresentation();
        Assert.assertEquals(30, vector.size());
        Assert.assertTrue(Types.isInsideBounds(vector));
    }

}
