/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.selection.arrangement;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class SortedArrangementTest {

    @Test
    public void arrange() {
        List<Integer> elements = Arrays.asList(9, 8, 7, 6, 5, 4, 3, 2, 1);
        Iterable<Integer> ordered = new SortedArrangement().arrange(elements);

        Assert.assertTrue(Iterables.elementsEqual(Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9), ordered));
    }
}
