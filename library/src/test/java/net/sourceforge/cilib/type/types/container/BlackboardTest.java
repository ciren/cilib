/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.type.types.container;

import static org.junit.Assert.assertEquals;
import net.sourceforge.cilib.type.types.Blackboard;
import net.sourceforge.cilib.type.types.Real;

import org.junit.Test;

public class BlackboardTest {

    @Test
    public void toStringTest() {
        Blackboard<String, Real> properties = new Blackboard<String, Real>();

        properties.put("first", Real.valueOf(1.0));
        properties.put("second", Real.valueOf(2.0));

        // The output will be different as the hascode of the string "second" evaluates before
        // the value of "first"
        assertEquals("{second=2.0, first=1.0}", properties.toString());
    }
}
