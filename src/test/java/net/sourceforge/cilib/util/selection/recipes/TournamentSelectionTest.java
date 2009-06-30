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

import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author gpampara
 */
public class TournamentSelectionTest {

    /**
     * To determine how the tournament will work is difficult as there
     * is first a selection for random participants. This test includes
     * all elements within the first selection.
     */
    @Test
    public void tournamentSelection() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

        TournamentSelection<Integer> tournamentSelection = new TournamentSelection<Integer>();
        tournamentSelection.getTournamentSize().setParameter(1.0);
        Integer result = tournamentSelection.select(list);

        Assert.assertEquals(9, result.intValue());
    }

}
