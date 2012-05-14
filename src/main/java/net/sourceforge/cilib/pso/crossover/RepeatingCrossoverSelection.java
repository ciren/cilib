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
package net.sourceforge.cilib.pso.crossover;

import fj.P3;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.pso.PSO;

/**
 * A CrossoverSelection strategy that performs crossover on random parents until an offspring better than the worst
 * parent is found or n times.
 */
public class RepeatingCrossoverSelection extends CrossoverSelection {

    private ControlParameter retries;

    public RepeatingCrossoverSelection() {
        super();
        this.retries = ConstantControlParameter.of(10);
    }

    public RepeatingCrossoverSelection(RepeatingCrossoverSelection copy) {
        super(copy);
        this.retries = copy.retries;
    }

    @Override
    public P3<Boolean, Particle, Particle> doAction(PSO algorithm, Enum solutionType, Enum fitnessType) {
        int counter = 0;
        boolean isBetter;
        P3<Boolean, Particle, Particle> result;

        do {
            result = select(algorithm, solutionType, fitnessType);
            isBetter = result._1();
        } while(++counter < retries.getParameter() && !isBetter);

        return result;
    }

    public void setRetries(ControlParameter retries) {
        this.retries = retries;
    }

    @Override
    public RepeatingCrossoverSelection getClone() {
        return new RepeatingCrossoverSelection(this);
    }
}
