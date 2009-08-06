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

import java.io.Serializable;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.pso.dynamic.DynamicIterationStrategy;
import net.sourceforge.cilib.util.Cloneable;

/**
 * This abstract class defines the interface that detection strategies have to adhere to.
 * Detection strategies are used within the scope of a {@link DynamicIterationStrategy} to
 * detect whether the environment has change during the course of an
 * {@link Algorithm algorithm's} execution.
 * @author Anna Rakatianskaia
 * @author Gary Pampara
 * @author Theuns Cloete
 */
public abstract class EnvironmentChangeDetectionStrategy<E extends PopulationBasedAlgorithm> implements Cloneable, Serializable {
    protected double epsilon = 0.0;
    protected int interval = 0;

    public EnvironmentChangeDetectionStrategy() {
        epsilon = 0.001;
        interval = 10;
    }

    public EnvironmentChangeDetectionStrategy(EnvironmentChangeDetectionStrategy<E> rhs) {
        epsilon = rhs.epsilon;
        interval = rhs.interval;
    }

    /**
     * Clone the <tt>EnvironmentChangeDetectionStrategy</tt> object.
     * @return A cloned <tt>EnvironmentChangeDetectionStrategy</tt>
     */
    public abstract EnvironmentChangeDetectionStrategy<E> getClone();

    /**
     * Check the environment in which the specified PSO algorithm is running for changes.
     * @param algorithm The <tt>PSO</tt> that runs in a dynamic environment.
     * @return true if any changes are detected, false otherwise
     */
    public abstract boolean detect(E algorithm);

    public void setEpsilon(double e) {
        if (e < 0.0) {
            throw new IllegalArgumentException("The epsilon value cannot be negative");
        }

        epsilon = e;
    }

    public double getEpsilon() {
        return epsilon;
    }

    public void setIterationsModulus(int im) {
        if (im <= 0) {
            throw new IllegalArgumentException("The number of consecutive iterations to pass cannot be <= 0");
        }

        interval = im;
    }

    public int getIterationsModulus() {
        return interval;
    }
}
