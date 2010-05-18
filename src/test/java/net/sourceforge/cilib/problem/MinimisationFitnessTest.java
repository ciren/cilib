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
package net.sourceforge.cilib.problem;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 *
 * @author Edwin Peer
 */
public class MinimisationFitnessTest {

    @Test
    public void lessThan() {
        Fitness oneFitness = new MinimisationFitness(1.0);
        Fitness twoFitness = new MinimisationFitness(2.0);
        assertEquals(twoFitness.compareTo(oneFitness), -1);
    }

    @Test
    public void moreThan() {
        Fitness oneFitness = new MinimisationFitness(1.0);
        Fitness twoFitness = new MinimisationFitness(2.0);
        assertEquals(oneFitness.compareTo(twoFitness), 1);
    }

    @Test
    public void equality() {
        Fitness oneFitness = new MinimisationFitness(1.0);
        Fitness twoFitness = new MinimisationFitness(2.0);

        assertEquals(oneFitness.compareTo(oneFitness), 0);
        assertEquals(twoFitness.compareTo(twoFitness), 0);
    }

    @Test
    public void inferior() {
        Fitness oneFitness = new MinimisationFitness(1.0);
        Fitness inferior = InferiorFitness.instance();

        assertEquals(inferior.compareTo(oneFitness), -1);
        assertEquals(oneFitness.compareTo(inferior), 1);
    }

}
