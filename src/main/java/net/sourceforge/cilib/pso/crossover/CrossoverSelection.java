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
import java.util.List;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.entity.operators.crossover.ParentCentricCrossoverStrategy;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.util.Cloneable;
import net.sourceforge.cilib.util.selection.Samples;
import net.sourceforge.cilib.util.selection.recipes.RandomSelector;
import net.sourceforge.cilib.util.selection.recipes.Selector;

/**
 * An operation used in the PSOCrossoverIterationStrategy which is responsible for performing the crossover and
 * performing other actions depending on the outcome of the crossover.
 */
public abstract class CrossoverSelection implements Cloneable {

    private CrossoverStrategy crossoverStrategy;
    private Selector selector;
    private ControlParameter numberOfParents;
    private ParticleProvider particleProvider;

    private enum TempEnums {
        TEMP
    };

    public CrossoverSelection() {
        this.crossoverStrategy = new ParentCentricCrossoverStrategy();
        this.selector = new RandomSelector();
        this.numberOfParents = ConstantControlParameter.of(3);
        this.particleProvider = new WorstParentParticleProvider();
    }

    public CrossoverSelection(CrossoverSelection copy) {
        this.crossoverStrategy = copy.crossoverStrategy.getClone();
        this.selector = copy.selector;
        this.numberOfParents = copy.numberOfParents.getClone();
        this.particleProvider = copy.particleProvider;
    }

    public P3<Boolean, Particle, Particle> select(PSO algorithm, Enum solutionType, Enum fitnessType) {
        boolean isBetter = false;
        Topology<Particle> topology = algorithm.getTopology();

        // get random particles
        List<Entity> parents = selector.on(topology).select(Samples.first((int) numberOfParents.getParameter()).unique());

        //put pbest as candidate solution for the crossover
        for (Entity e : parents) {
            Particle p = (Particle) e;
            e.getProperties().put(TempEnums.TEMP, p.getCandidateSolution());
            e.getProperties().put(EntityType.CANDIDATE_SOLUTION, e.getProperties().get(solutionType));
        }

        //perform crossover and compute offspring's fitness
        Particle offspring = (Particle) crossoverStrategy.crossover(parents).get(0);
        offspring.calculateFitness();

        Particle selectedParticle = particleProvider.get(parents);

        //replace selectedEntity if offspring is better
        if (((Fitness) offspring.getProperties().get(fitnessType)).compareTo((Fitness) selectedParticle.getProperties().get(fitnessType)) > 0) {
            isBetter = true;
        }

        // revert solutions
        for (Entity e : parents) {
            e.getProperties().put(EntityType.CANDIDATE_SOLUTION, e.getProperties().get(TempEnums.TEMP));
        }

        return P.p(isBetter, selectedParticle, offspring);
    }

    public void setNumberOfParents(ControlParameter numberOfParents) {
        this.numberOfParents = numberOfParents;
    }

    public ControlParameter getNumberOfParents() {
        return numberOfParents;
    }

    public void setCrossoverStrategy(CrossoverStrategy crossoverStrategy) {
        this.crossoverStrategy = crossoverStrategy;
    }

    public CrossoverStrategy getCrossoverStrategy() {
        return crossoverStrategy;
    }

    public void setParticleProvider(ParticleProvider particleProvider) {
        this.particleProvider = particleProvider;
    }

    public ParticleProvider getParticleProvider() {
        return particleProvider;
    }

    public Selector getSelector() {
        return selector;
    }

    public void setSelector(Selector selector) {
        this.selector = selector;
    }

    public abstract P3<Boolean, Particle, Particle> doAction(PSO algorithm, Enum solutionType, Enum fitnessType);

    @Override
    public abstract CrossoverSelection getClone();
}
