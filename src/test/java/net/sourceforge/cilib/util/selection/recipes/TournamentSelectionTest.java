/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.util.selection.recipes;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import net.sourceforge.cilib.controlparameter.ProportionalControlParameter;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomAdaptor;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.Matchers.hasItem;

/**
 *
 * @author gpampara
 */
public class TournamentSelectionTest {

    @Test(expected = IllegalArgumentException.class)
    public void selectEmpty() {
        List<Integer> elements = Lists.newArrayList();
        TournamentSelection<Integer> selection = new TournamentSelection<Integer>();
        selection.select(elements);
    }

    @Test
    public void selectSingle() {
        List<Integer> elements = Lists.newArrayList(1);
        TournamentSelection<Integer> selection = new TournamentSelection<Integer>();
        selection.setTournamentSize(new ProportionalControlParameter(1.0));
        int selected = selection.select(elements);
        Assert.assertThat(selected, is(1));
    }

    /**
     * This test shows that when tournament selection considers the entire
     * selection as the tournament, it will select the best entity from the
     * initial selection. This behaviour is equivalent to elitist-selection.
     */
    @Test
    public void fullTournament() {
        List<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        TournamentSelection<Integer> selection = new TournamentSelection<Integer>();
        selection.setTournamentSize(new ProportionalControlParameter(1.0));
        int selected = selection.select(list);
        Assert.assertThat(selected, is(9));
    }

    @Test
    public void partialTournament() {
        List<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        TournamentSelection<Integer> selection = new TournamentSelection<Integer>();
        selection.getTournamentSize().setParameter(0.5);
        selection.setRandom(new ConstantRandomNumber());
        int selected = selection.select(list);

        List<Integer> otherList = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9);

        // First shuffle.
        Collections.shuffle(otherList, new RandomAdaptor(new ConstantRandomNumber()));

        // Select tournament list.
        int tournamentSize = Double.valueOf(selection.getTournamentSize().getParameter() * otherList.size()).intValue();
        List<Integer> tournamentList = otherList.subList(otherList.size() - tournamentSize, otherList.size());
        Assert.assertThat(tournamentList, hasItem(selected));

        // Sort tournament, and select best.
        Collections.sort(tournamentList);
        int bestIndex = tournamentList.size() - 1;
        Assert.assertThat(tournamentList.get(bestIndex), is(equalTo(selected)));
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
