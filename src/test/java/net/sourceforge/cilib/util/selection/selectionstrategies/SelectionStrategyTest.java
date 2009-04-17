/**
 * Copyright (C) 2003 - 2008
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.util.selection.selectionstrategies;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.cilib.math.random.generator.NetworkBasedSeedSelectionStrategy;
import net.sourceforge.cilib.math.random.generator.Random;
import net.sourceforge.cilib.math.random.generator.Seeder;
import net.sourceforge.cilib.math.random.generator.ZeroSeederStrategy;
import net.sourceforge.cilib.util.selection.weighingstrategies.LinearWeighingStrategy;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SelectionStrategyTest {

    private class Sequence extends Random {

        private int index;
        private List<Double> sequence;

        public Sequence() {
            super(Seeder.getSeed());
            this.index = 0;
            this.sequence = new ArrayList<Double>();
        }

        public Sequence(Sequence copy) {
            super(Seeder.getSeed());
            this.index = copy.index;
            this.sequence = new ArrayList<Double>(copy.sequence);
        }

        @Override
        public Random getClone() {
            return new Sequence(this);
        }

        public void addNumber(double number) {
            this.sequence.add(number);
        }

        public void addNumbers(Double... numbers) {
            this.sequence.addAll(Arrays.asList(numbers));
        }

        public void removeNumber(int index) {
            this.sequence.remove(index);
        }

        public Double getNumber(int index) {
            return this.sequence.get(index);
        }

        @Override
        protected int next(int bits) {
            return Double.valueOf(this.nextDouble()).intValue();
        }

        @Override
        public double nextDouble() {
            double returnValue = this.sequence.get(this.index);
            this.index = (this.index + 1) % this.sequence.size();
            return returnValue;
        }

        public void reset() {
            this.index = 0;
        }
    }
    private static List<Integer> objects;

    @BeforeClass
    public static void setup() {
        List<Integer> tempObjects = new ArrayList<Integer>();
        for (int i = 0; i < 10; ++i) {
            tempObjects.add(Integer.valueOf(i + 1));
        }
        objects = Collections.unmodifiableList(tempObjects);
    }

    @AfterClass
    public static void tearDown() {
        Seeder.setSeederStrategy(new NetworkBasedSeedSelectionStrategy());
    }

    @Before
    public void resetRandomiser() {
        Seeder.setSeederStrategy(new ZeroSeederStrategy());
    }

    @Test
    public void testIndexedSelectionStrategy() {
        IndexedSelectionStrategy<Integer> selectionStrategy =
                new IndexedSelectionStrategy<Integer>();
        selectionStrategy.addIndices(0, 1, 2, 3, 4);

        Collection<Integer> selectedObjects = selectionStrategy.select(objects, 5);
        Iterator<Integer> selectedObjectIterator = selectedObjects.iterator();
        assertThat((Integer) selectedObjectIterator.next(), equalTo(Integer.valueOf(1)));
        assertThat((Integer) selectedObjectIterator.next(), equalTo(Integer.valueOf(2)));
        assertThat((Integer) selectedObjectIterator.next(), equalTo(Integer.valueOf(3)));
        assertThat((Integer) selectedObjectIterator.next(), equalTo(Integer.valueOf(4)));
        assertThat((Integer) selectedObjectIterator.next(), equalTo(Integer.valueOf(5)));
    }

    @Test
    public void testNormalisedProbabilisticSelectionStrategy() {
        Sequence sequence = new Sequence();
        NormalisedProbabilisticSelectionStrategy<Integer> selectionStrategy =
                new NormalisedProbabilisticSelectionStrategy<Integer>(sequence);
        LinearWeighingStrategy<Integer> customWeighingStrategy = new LinearWeighingStrategy<Integer>(1000.0, 1005.0);
        selectionStrategy.setWeighingStrategy(customWeighingStrategy);

        /*
         * Objects will be shuffled in the following order:
         * 7, 4, 9, 8, 10, 6, 5, 1, 3, 2
         * if the following sequence is used as part of a random number generator:
         */
        sequence.addNumbers(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);

        /*
         * Random accumulative numbers will be generated in the following order:
         * 3.5 (5.0 x 0.7), 2.4 (4.0 x 0.6), 1.61* (3.2* x 0.5), 0.23* (2.3* x 0.1), 0.3* (1.6* x 0.2)
         * if the following sequence is used as part of a random number generator:
         */
        sequence.addNumbers(0.7, 0.6, 0.5, 0.1, 0.2);

        /*
         * This means the numbers will be selected in the following order:
         *  10 (weight of 1.0  and accumulative sum of 3.6*)
         *  8  (weight of 0.7* and accumulative sum of 2.6*)
         *  9  (weight of 0.8* and accumulative sum of 1.8*)
         *  7  (weight of 0.6* and accumulative sum of 0.6*)
         *  6  (weight of 0.5* and accumulative sum of 0.8*)
         */

        Collection<Integer> selectedObjects = selectionStrategy.select(objects, 5);
        Iterator<Integer> selectedObjectIterator = selectedObjects.iterator();
        assertThat((Integer) selectedObjectIterator.next(), equalTo(Integer.valueOf(10)));
        assertThat((Integer) selectedObjectIterator.next(), equalTo(Integer.valueOf(8)));
        assertThat((Integer) selectedObjectIterator.next(), equalTo(Integer.valueOf(9)));
        assertThat((Integer) selectedObjectIterator.next(), equalTo(Integer.valueOf(7)));
        assertThat((Integer) selectedObjectIterator.next(), equalTo(Integer.valueOf(6)));
    }

    @Test
    public void testProbabilisticSelectionStrategy() {
        Sequence sequence = new Sequence();
        ProbabilisticSelectionStrategy<Integer> selectionStrategy = new ProbabilisticSelectionStrategy<Integer>(sequence);
        selectionStrategy.setWeighingStrategy(new LinearWeighingStrategy(0.1, 1.0));

        /*
         * Objects will be shuffled in the following order:
         * 7, 4, 9, 8, 10, 6, 5, 1, 3, 2
         * if the following sequence is used as part of a random number generator:
         */
        sequence.addNumbers(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);

        /*
         * Random accumulative numbers will be generated in the following order:
         * 3.85 (5.5 x 0.7), 2.94 (4.9 x 0.6), 1.95 (3.9 x 0.5), 0.3 (3.0 x 0.1), 0.46 (2.3 x 0.2)
         * if the following sequence is used as part of a random number generator:
         */
        sequence.addNumbers(0.7, 0.6, 0.5, 0.1, 0.2);

        /*
         * This means the numbers will be selected in the following order:
         *  6  (weight of 0.6 and accumulative sum of 4.4)
         *  10 (weight of 1.0 and accumulative sum of 3.8)
         *  9  (weight of 0.9 and accumulative sum of 2.0)
         *  7  (weight of 0.7 and accumulative sum of 0.7)
         *  8  (weight of 0.8 and accumulative sum of 1.2)
         */

        Collection<Integer> selectedObjects = selectionStrategy.select(objects, 5);
        Iterator<Integer> selectedObjectIterator = selectedObjects.iterator();
        assertThat((Integer) selectedObjectIterator.next(), equalTo(Integer.valueOf(6)));
        assertThat((Integer) selectedObjectIterator.next(), equalTo(Integer.valueOf(10)));
        assertThat((Integer) selectedObjectIterator.next(), equalTo(Integer.valueOf(9)));
        assertThat((Integer) selectedObjectIterator.next(), equalTo(Integer.valueOf(7)));
        assertThat((Integer) selectedObjectIterator.next(), equalTo(Integer.valueOf(8)));
    }

    @Test
    public void testRandomSelectionStrategy() {
        Sequence sequence = new Sequence();
        RandomSelectionStrategy<Integer> selectionStrategy = new RandomSelectionStrategy<Integer>(sequence);
        sequence.addNumbers(1.0, 2.0, 3.0, 4.0, 5.0);

        Collection<Integer> selectedObjects = selectionStrategy.select(objects, 5);
        Iterator<Integer> selectedObjectIterator = selectedObjects.iterator();
        assertThat((Integer) selectedObjectIterator.next(), equalTo(Integer.valueOf(2)));
        assertThat((Integer) selectedObjectIterator.next(), equalTo(Integer.valueOf(4)));
        assertThat((Integer) selectedObjectIterator.next(), equalTo(Integer.valueOf(1)));
        assertThat((Integer) selectedObjectIterator.next(), equalTo(Integer.valueOf(8)));
        assertThat((Integer) selectedObjectIterator.next(), equalTo(Integer.valueOf(10)));
    }

    @Test
    public void testRankBasedSelectionStrategy() {
        RankBasedSelectionStrategy<Double, Integer> selectionStrategy = new RankBasedSelectionStrategy<Double, Integer>();
        selectionStrategy.setWeighingStrategy(new LinearWeighingStrategy(0.1, 1.0));

        Collection<Integer> selectedObjects = selectionStrategy.select(objects, 5);
        Iterator<Integer> selectedObjectIterator = selectedObjects.iterator();
        assertThat((Integer) selectedObjectIterator.next(), equalTo(Integer.valueOf(1)));
        assertThat((Integer) selectedObjectIterator.next(), equalTo(Integer.valueOf(2)));
        assertThat((Integer) selectedObjectIterator.next(), equalTo(Integer.valueOf(3)));
        assertThat((Integer) selectedObjectIterator.next(), equalTo(Integer.valueOf(4)));
        assertThat((Integer) selectedObjectIterator.next(), equalTo(Integer.valueOf(5)));
    }

    @Test
    public void testSimpleSelectionStrategy() {
        SimpleSelectionStrategy<Integer> selectionStrategy = new SimpleSelectionStrategy<Integer>();

        Collection<Integer> selectedObjects = selectionStrategy.select(objects, 5);
        Iterator<Integer> selectedObjectIterator = selectedObjects.iterator();
        assertThat((Integer) selectedObjectIterator.next(), equalTo(Integer.valueOf(1)));
        assertThat((Integer) selectedObjectIterator.next(), equalTo(Integer.valueOf(2)));
        assertThat((Integer) selectedObjectIterator.next(), equalTo(Integer.valueOf(3)));
        assertThat((Integer) selectedObjectIterator.next(), equalTo(Integer.valueOf(4)));
        assertThat((Integer) selectedObjectIterator.next(), equalTo(Integer.valueOf(5)));
    }

    @Test
    public void testTournamentSelectionStrategy() {
        TournamentSelectionStrategy<Double, Integer> selectionStrategy = new TournamentSelectionStrategy<Double, Integer>();
        selectionStrategy.setTournamentSize(objects.size());
        selectionStrategy.setWeighingStrategy(new LinearWeighingStrategy(0.1, 1.0));

        Collection<Integer> selectedObjects = selectionStrategy.select(objects, 5);
        Iterator<Integer> selectedObjectIterator = selectedObjects.iterator();
        assertThat((Integer) selectedObjectIterator.next(), equalTo(Integer.valueOf(1)));
        assertThat((Integer) selectedObjectIterator.next(), equalTo(Integer.valueOf(2)));
        assertThat((Integer) selectedObjectIterator.next(), equalTo(Integer.valueOf(3)));
        assertThat((Integer) selectedObjectIterator.next(), equalTo(Integer.valueOf(4)));
        assertThat((Integer) selectedObjectIterator.next(), equalTo(Integer.valueOf(5)));
    }
}
