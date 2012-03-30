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
package net.sourceforge.cilib.pso.niching;

import fj.P2;
import fj.data.List;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;

/**
 *
 */
public class NichingSwarms extends P2<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>> {
    
    final private PopulationBasedAlgorithm mainSwarm;
    final private List<PopulationBasedAlgorithm> subswarms;
    
    public static NichingSwarms of(PopulationBasedAlgorithm ms, List<PopulationBasedAlgorithm> ss) {
        return new NichingSwarms(ms, ss);
    }
    
    private NichingSwarms(PopulationBasedAlgorithm ms, List<PopulationBasedAlgorithm> ss) {
        this.mainSwarm = ms;
        this.subswarms = ss;
    }

    @Override
    public PopulationBasedAlgorithm _1() {
        return mainSwarm;
    }

    @Override
    public List<PopulationBasedAlgorithm> _2() {
        return subswarms;
    }
}
