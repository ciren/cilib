/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.io.transform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class SelectiveDataOperatorTest {

    @Test
    public void testParseSelectionString() {
        List<Integer> expected = new ArrayList<Integer>();
        List<Integer> list;

        expected.addAll(Arrays.asList(1));
        list = SelectiveDataOperator.parseSelectionString("1 : 1");
        Assert.assertEquals(expected, list);

        expected.clear();
        expected.addAll(Arrays.asList(2,3,4,5));
        list = SelectiveDataOperator.parseSelectionString("2 : 5");
        Assert.assertEquals(expected, list);
    }

}
