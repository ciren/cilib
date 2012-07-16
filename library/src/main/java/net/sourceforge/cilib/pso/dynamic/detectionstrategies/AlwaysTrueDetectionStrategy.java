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
package net.sourceforge.cilib.pso.dynamic.detectionstrategies;


import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;

/**
 * Detection strategy that always return true. For environment that are constantly changing.
 */
public class AlwaysTrueDetectionStrategy extends EnvironmentChangeDetectionStrategy {

    /**
     *
     */
    private static final long serialVersionUID = 4357366261946609149L;

    public AlwaysTrueDetectionStrategy() {
    }

    public AlwaysTrueDetectionStrategy(AlwaysTrueDetectionStrategy copy) {
    }

    @Override
    public AlwaysTrueDetectionStrategy clone() {
        return new AlwaysTrueDetectionStrategy(this);
    }


    /**
     *
     * @param algorithm PSO algorithm that operates in a dynamic environment
     * @return true
     */
    public boolean detect(PopulationBasedAlgorithm algorithm) {
        return true;
    }

    public AlwaysTrueDetectionStrategy getClone(){
        return new AlwaysTrueDetectionStrategy();
    }



}


