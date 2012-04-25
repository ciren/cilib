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
package net.sourceforge.cilib.niching.creation;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;

public class NPerIterationNicheDetection extends NicheDetection {

    private int n;
    private int current;
    private int iteration;

    public NPerIterationNicheDetection() {
        this.n = 1;
        this.current = 0;
        this.iteration = 0;
    }

    @Override
    public Boolean f(PopulationBasedAlgorithm a, Entity b) {
        if (AbstractAlgorithm.get().getIterations() != iteration) {
            iteration = AbstractAlgorithm.get().getIterations();
            current = 0;
            return true;
        } else {
            if (++current < n) {
                return true;
            }
        }

        return false;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getN() {
        return n;
    }
}
