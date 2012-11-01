/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.selection.arrangement;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class ReverseArrangementTest {

    @Test
    public void arrangement() {
        List<Integer> elements = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        
        List<Integer> expected = Lists.newArrayList(elements);
        Collections.reverse(expected);

        Iterable<Integer> ordered = new ReverseArrangement().arrange(elements);

        Assert.assertTrue(Iterables.elementsEqual(expected, ordered));
    }
}
