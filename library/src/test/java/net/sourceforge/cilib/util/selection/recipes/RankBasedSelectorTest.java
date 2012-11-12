/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.selection.recipes;

import com.google.common.collect.Lists;
import java.util.List;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.core.Is.is;

/**
 *
 */
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
        List<Integer> elements = Lists.newArrayList(9, 8, 7, 6, 5, 4, 3, 2, 1);
        RankBasedSelector<Integer> selection = new RankBasedSelector<Integer>();
        selection.setRandom(new ConstantRandomNumber());
        int selected = selection.on(elements).select();
        Assert.assertThat(selected, is(8));
    }

    private static class ConstantRandomNumber implements RandomProvider {

        private static final long serialVersionUID = 3019387660938987850L;
        private RandomProvider randomProvider = new MersenneTwister(0);

        @Override
        public boolean nextBoolean() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public int nextInt() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public int nextInt(int n) {
            return this.randomProvider.nextInt(n);
        }

        @Override
        public long nextLong() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public float nextFloat() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public double nextDouble() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void nextBytes(byte[] bytes) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
