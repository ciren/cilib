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
//import net.sourceforge.cilib.pso.PSO;

/**
 * Detection strategy that returns true periodically after a known number of iteration.
 * For environment where change frequency is known.
 *
 */
public class PeriodicChangeDetectionStrategy<E extends PopulationBasedAlgorithm> extends EnvironmentChangeDetectionStrategy<E> {

    /**
     *
     */
    private static final long serialVersionUID = -5220604087545891206L;

    private int period;

    public PeriodicChangeDetectionStrategy() {
    }

    public PeriodicChangeDetectionStrategy(PeriodicChangeDetectionStrategy<E> copy) {
    }

    @Override
    public PeriodicChangeDetectionStrategy<E> clone() {
        return new PeriodicChangeDetectionStrategy<E>(this);
    }


    /**
     *
     * @param algorithm PSO algorithm that operates in a dynamic environment
     * @return true
     */
    public boolean detect(PopulationBasedAlgorithm algorithm) {
        if (algorithm.getIterations()%period == 0)
            return true;
        return false;
    }

    public AlwaysTrueDetectionStrategy getClone(){
        return new AlwaysTrueDetectionStrategy();
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }



}


