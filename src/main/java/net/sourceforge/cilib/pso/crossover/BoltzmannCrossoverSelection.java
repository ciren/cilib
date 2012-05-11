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

import fj.P;
import fj.P3;
import java.util.Comparator;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.comparator.BoltzmannComparator;
import net.sourceforge.cilib.pso.PSO;

/**
 * A CrossoverSelection strategy that performs Boltzmann selection on the worst 
 * parent and the offspring if the offspring is worse than the worst parent
 */
public class BoltzmannCrossoverSelection extends CrossoverSelection {

    private Comparator comparator;

    public BoltzmannCrossoverSelection() {
        this.comparator = new BoltzmannComparator();
    }

    public BoltzmannCrossoverSelection(BoltzmannCrossoverSelection copy) {
        this.comparator = copy.comparator;
    }

    @Override
    public P3<Boolean, Particle, Particle> doAction(PSO algorithm, Enum solutionType, Enum fitnessType) {
        P3<Boolean, Particle, Particle> result = select(algorithm, solutionType, fitnessType);
        Particle selectedParticle = result._2();
        Particle offspring = result._3();

        if (!result._1()) {
            if (comparator.compare(selectedParticle, offspring) < 0) {
                return P.p(true, result._2(), result._3());
            }
        }

        return result;
    }

    @Override
    public BoltzmannCrossoverSelection getClone() {
        return new BoltzmannCrossoverSelection(this);
    }

    public void setComparator(Comparator comparator) {
        this.comparator = comparator;
    }

    public Comparator getComparator() {
        return comparator;
    }
}
