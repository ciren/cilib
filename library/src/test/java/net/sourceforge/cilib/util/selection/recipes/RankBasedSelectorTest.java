/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.selection.recipes;

import com.google.common.collect.Lists;
import java.util.List;
import net.sourceforge.cilib.math.random.generator.Rand;
import static org.hamcrest.core.Is.is;
import org.junit.Assert;
import org.junit.Test;

public class RankBasedSelectorTest {

    @Test(expected = IllegalArgumentException.class)
    public void selectEmpty() {
        List<Integer> elements = Lists.newArrayList();
        RankBasedSelector<Integer> selection = new RankBasedSelector<Integer>();
        selection.on(elements).select();
    }

    @Test
    public void selectSingle() {
        List<Integer> elements = Lists.newArrayList(1);
        RankBasedSelector<Integer> selection = new RankBasedSelector<Integer>();
        int selected = selection.on(elements).select();
        Assert.assertThat(selected, is(1));
    }

    @Test
    public void selectMultiple() {
        Rand.setSeed(0);
        List<Integer> elements = Lists.newArrayList(9, 8, 7, 6, 5, 4, 3, 2, 1);
        RankBasedSelector<Integer> selection = new RankBasedSelector<Integer>();
        int selected = selection.on(elements).select();
        Assert.assertThat(selected, is(8));
    }
}
