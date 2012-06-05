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

import net.sourceforge.cilib.pso.crossover.util.ParticleProvider;
import net.sourceforge.cilib.pso.crossover.util.WorstParentParticleProvider;
import com.google.common.collect.Maps;
import fj.P;
import fj.P3;
import java.util.List;
import java.util.Map;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.entity.operators.crossover.real.ParentCentricCrossoverStrategy;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.util.selection.Samples;
import net.sourceforge.cilib.util.selection.recipes.RandomSelector;
import net.sourceforge.cilib.util.selection.recipes.Selector;

/**
 * An operation used in the PSOCrossoverIterationStrategy which is responsible for performing the crossover and
 * performing other actions depending on the outcome of the crossover.
 */
public abstract class CrossoverSelection implements PSOCrossoverOperation {

    private CrossoverStrategy crossoverStrategy;
    private Selector selector;
    private ParticleProvider particleProvider;

    public CrossoverSelection() {
        this.crossoverStrategy = new ParentCentricCrossoverStrategy();
        this.selector = new RandomSelector();
        this.particleProvider = new WorstParentParticleProvider();
    }

    public CrossoverSelection(CrossoverSelection copy) {
        this.crossoverStrategy = copy.crossoverStrategy.getClone();
        this.selector = copy.selector;
        this.particleProvider = copy.particleProvider;
    }

    public P3<Boolean, Particle, Particle> select(PSO algorithm, Enum solutionType, Enum fitnessType) {
        boolean isBetter = false;
        Topology<Particle> topology = algorithm.getTopology();
	Map<Entity, StructuredType> tmp = Maps.newHashMap();

        // get random particles
        List<Entity> parents = selector.on(topology).select(Samples.first((int) crossoverStrategy.getNumberOfParents()).unique());

        //put pbest as candidate solution for the crossover
        for (Entity e : parents) {
            tmp.put(e, e.getCandidateSolution());
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
	    e.setCandidateSolution(tmp.get(e));
        }

        return P.p(isBetter, selectedParticle, offspring);
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
    public Topology<Particle> performCrossoverOpertation(PSO algorithm) {
        P3<Boolean, Particle, Particle> result = doAction(algorithm, EntityType.CANDIDATE_SOLUTION, EntityType.FITNESS);
        
        if (result._1()) {
            int i = algorithm.getTopology().indexOf(result._2());
            result._3().setNeighbourhoodBest(result._2().getNeighbourhoodBest());
            algorithm.getTopology().set(i, result._3());
        }

        return algorithm.getTopology();
    }

    @Override
    public abstract CrossoverSelection getClone();
}
