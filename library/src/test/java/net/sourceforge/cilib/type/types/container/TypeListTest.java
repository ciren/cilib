/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.type.types.container;

import net.sourceforge.cilib.type.types.Int;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class TypeListTest {

    @Test
    public void subList() {
        TypeList list = new TypeList();
        list.add(Vector.of());
        list.add(Int.valueOf(0));

        TypeList subList = list.subList(0, 1);

        Assert.assertEquals(2, subList.size());
    }
}
