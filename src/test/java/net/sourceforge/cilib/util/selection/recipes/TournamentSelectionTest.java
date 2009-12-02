/**
 * Copyright (C) 2003 - 2009
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
package net.sourceforge.cilib.util.selection.recipes;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import net.sourceforge.cilib.controlparameter.ProportionalControlParameter;
import net.sourceforge.cilib.math.random.generator.Random;
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

    @Test(expected = IndexOutOfBoundsException.class)
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
        Collections.shuffle(otherList, new ConstantRandomNumber());

        // Select tournament list.
        int tournamentSize = Double.valueOf(selection.getTournamentSize().getParameter() * otherList.size()).intValue();
        List<Integer> tournamentList = otherList.subList(otherList.size() - tournamentSize, otherList.size());
        Assert.assertThat(tournamentList, hasItem(selected));

        // Sort tournament, and select best.
        Collections.sort(tournamentList);
        int bestIndex = tournamentList.size() - 1;
        Assert.assertThat(tournamentList.get(bestIndex), is(equalTo(selected)));
    }

    private static class ConstantRandomNumber extends Random {

        private static final long serialVersionUID = 3019387660938987850L;

        public ConstantRandomNumber() {
            super(0);
        }

        @Override
        public Random getClone() {
            return this;
        }

        @Override
        public int nextInt(int n) {
            return super.nextInt(n);
        }
    }
}
