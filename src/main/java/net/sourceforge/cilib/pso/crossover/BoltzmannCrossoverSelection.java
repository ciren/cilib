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
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.LinearlyVaryingControlParameter;
import net.sourceforge.cilib.controlparameter.UpdateOnIterationControlParameter;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFuction;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.pso.PSO;

/**
 * A CrossoverSelection strategy that performs Boltzmann selection on the worst parent and the offspring if the
 * offspring is worse than the worst parent
 */
public class BoltzmannCrossoverSelection extends CrossoverSelection {

    private ProbabilityDistributionFuction distribution;
    private ControlParameter tempSchedule;

    public BoltzmannCrossoverSelection() {
        this.distribution = new UniformDistribution();

        LinearlyVaryingControlParameter cp = new LinearlyVaryingControlParameter();
        cp.setInitialValue(100);
        cp.setFinalValue(1);

        UpdateOnIterationControlParameter outerCP = new UpdateOnIterationControlParameter();
        outerCP.setDelegate(cp);
        
        this.tempSchedule = outerCP;
    }

    public BoltzmannCrossoverSelection(BoltzmannCrossoverSelection copy) {
        this.distribution = copy.distribution;
        this.tempSchedule = copy.tempSchedule.getClone();
    }

    @Override
    public P3<Boolean, Particle, Particle> doAction(PSO algorithm, Enum solutionType, Enum fitnessType) {
        P3<Boolean, Particle, Particle> result = select(algorithm, solutionType, fitnessType);
        Particle selectedParticle = result._2();
        Particle offspring = result._3();

        if (!result._1()) {
            Fitness selectedFitness = (Fitness) selectedParticle.getProperties().get(fitnessType);
            Fitness offspringFitness = (Fitness) offspring.getProperties().get(fitnessType);
            
            int sign = -selectedFitness.compareTo(offspringFitness);
            double diff = Math.abs(selectedFitness.getValue() - offspringFitness.getValue());
            double probability = 1 / (1 + Math.exp(sign * diff / tempSchedule.getParameter()));

            if (distribution.getRandomNumber() > probability) {
                return P.p(true, result._2(), result._3());
            }
        }

        return result;
    }

    @Override
    public BoltzmannCrossoverSelection getClone() {
        return new BoltzmannCrossoverSelection(this);
    }

    public void setTempSchedule(ControlParameter tSchedule) {
        this.tempSchedule = tSchedule;
    }

    public void setDistribution(ProbabilityDistributionFuction distribution) {
        this.distribution = distribution;
    }
}
