/*
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
package net.sourceforge.cilib.pso.dynamic.detectionstrategies;


import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
//import net.sourceforge.cilib.pso.PSO;

/**
 * Detection strategy that returns true periodically after a known number of iteration.
 * For environment where change frequency is known.
 *
 * @author Julien Duhain
 */
public class PeriodicChangeDetectionStrategy extends    EnvironmentChangeDetectionStrategy {

    /**
     *
     */
    private static final long serialVersionUID = -5220604087545891206L;

    private int period;

    public PeriodicChangeDetectionStrategy() {
    }

    public PeriodicChangeDetectionStrategy(PeriodicChangeDetectionStrategy copy) {
    }

    @Override
    public PeriodicChangeDetectionStrategy clone() {
        return new PeriodicChangeDetectionStrategy(this);
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


