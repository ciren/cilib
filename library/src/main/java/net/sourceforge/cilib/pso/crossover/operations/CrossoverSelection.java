/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.crossover.operations;

import java.util.Map;

import net.sourceforge.cilib.entity.Property;
import net.sourceforge.cilib.entity.operators.crossover.real.ParentCentricCrossoverStrategy;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.crossover.ParticleCrossoverStrategy;
import net.sourceforge.cilib.pso.crossover.particleprovider.ParticleProvider;
import net.sourceforge.cilib.pso.crossover.particleprovider.WorstParentParticleProvider;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.util.selection.Samples;
import net.sourceforge.cilib.util.selection.recipes.RandomSelector;
import net.sourceforge.cilib.util.selection.recipes.Selector;

import com.google.common.collect.Maps;
import com.google.common.collect.Lists;

import fj.F;
import fj.P;
import fj.P3;
import fj.data.List;

/**
 * An operation used in the PSOCrossoverIterationStrategy which is responsible
 * for performing the crossover and performing other actions depending on the
 * outcome of the crossover.
 */
public abstract class CrossoverSelection extends PSOCrossoverOperation {

    protected ParticleCrossoverStrategy crossoverStrategy;
    protected Selector selector;
    protected ParticleProvider particleProvider;

    public CrossoverSelection() {
        this.crossoverStrategy = new ParticleCrossoverStrategy();
        this.crossoverStrategy.setCrossoverStrategy(new ParentCentricCrossoverStrategy());
        this.selector = new RandomSelector();
        this.particleProvider = new WorstParentParticleProvider();
    }

    public CrossoverSelection(CrossoverSelection copy) {
        this.crossoverStrategy = copy.crossoverStrategy.getClone();
        this.selector = copy.selector;
        this.particleProvider = copy.particleProvider;
    }

    public P3<Boolean, Particle, Particle> select(PSO algorithm, Property solutionType, Property fitnessType) {
        boolean isBetter = false;
        fj.data.List<Particle> topology = algorithm.getTopology();
	Map<Particle, StructuredType> tmp = Maps.newHashMap();

        // get random particles
        List<Particle> parents = fj.data.List.iterableList(selector.on(topology).select(Samples.first(crossoverStrategy.getNumberOfParents()).unique()));

        //put pbest as candidate solution for the crossover
        for (Particle e : parents) {
            tmp.put(e, e.getPosition());
            e.put(Property.CANDIDATE_SOLUTION, e.<StructuredType>get(solutionType));
        }

        //perform crossover and select particle to compare with
        Particle offspring = crossoverStrategy.crossover(Lists.newArrayList(parents)).get(0);
        Particle selectedParticle = particleProvider.f(parents, offspring);

        //replace selectedEntity if offspring is better
        if (((Fitness) offspring.get(fitnessType))
                .compareTo((Fitness) selectedParticle.get(fitnessType)) > 0) {
            isBetter = true;
        }

        // revert solutions
        for (Particle e : parents) {
	        e.setPosition(tmp.get(e));
        }

        return P.p(isBetter, selectedParticle, offspring);
    }

    public void setCrossoverStrategy(ParticleCrossoverStrategy crossoverStrategy) {
        this.crossoverStrategy = crossoverStrategy;
    }

    public ParticleCrossoverStrategy getCrossoverStrategy() {
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

    public abstract P3<Boolean, Particle, Particle> doAction(PSO algorithm, Property solutionType, Property fitnessType);

    @Override
    public fj.data.List<Particle> f(PSO algorithm) {
        final P3<Boolean, Particle, Particle> result = doAction(algorithm, Property.CANDIDATE_SOLUTION, Property.FITNESS);

        if (result._1()) {
            algorithm.setTopology(algorithm.getTopology().map(new F<Particle, Particle>() {
                @Override
                public Particle f(Particle a) {
                    if (a.equals(result._2())) {
                        result._3().setNeighbourhoodBest(result._2().getNeighbourhoodBest());
                        return result._3();
                    } else {
                        return a;
                    }
                }
            }));
        }

        return algorithm.getTopology();
    }

    @Override
    public abstract CrossoverSelection getClone();
}
