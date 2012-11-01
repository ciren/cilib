/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.selection.arrangement;

import com.google.common.collect.Lists;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;

/**
 *
 */
public class RingBasedArrangementTest {

    @Test
    public void arrange() {
        List<Integer> elements = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        List<Integer> ordered = Lists.newArrayList(new RingBasedArrangement(5).arrange(elements));
        Assert.assertTrue(!ordered.isEmpty());

        Assert.assertThat(ordered.get(0), is(6));
        Assert.assertThat(ordered.get(1), is(7));
        Assert.assertThat(ordered.get(2), is(8));
        Assert.assertThat(ordered.get(3), is(9));
        Assert.assertThat(ordered.get(4), is(1));
        Assert.assertThat(ordered.get(5), is(2));
        Assert.assertThat(ordered.get(6), is(3));
        Assert.assertThat(ordered.get(7), is(4));
        Assert.assertThat(ordered.get(8), is(5));
    }
}
