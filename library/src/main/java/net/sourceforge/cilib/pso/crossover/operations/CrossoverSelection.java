/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.crossover.operations;

import com.google.common.collect.Maps;
import fj.P;
import fj.P3;
import java.util.List;
import java.util.Map;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Topology;
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

    public P3<Boolean, Particle, Particle> select(PSO algorithm, Enum solutionType, Enum fitnessType) {
        boolean isBetter = false;
        Topology<Particle> topology = algorithm.getTopology();
	Map<Particle, StructuredType> tmp = Maps.newHashMap();

        // get random particles
        List<Particle> parents = selector.on(topology).select(Samples.first(crossoverStrategy.getNumberOfParents()).unique());

        //put pbest as candidate solution for the crossover
        for (Particle e : parents) {
            tmp.put(e, e.getCandidateSolution());
            e.getProperties().put(EntityType.CANDIDATE_SOLUTION, e.getProperties().get(solutionType));
        }

        //perform crossover and select particle to compare with
        Particle offspring = (Particle) crossoverStrategy.crossover(parents).get(0);
        Particle selectedParticle = particleProvider.f(parents, offspring);

        //replace selectedEntity if offspring is better
        if (((Fitness) offspring.getProperties().get(fitnessType))
                .compareTo((Fitness) selectedParticle.getProperties().get(fitnessType)) > 0) {
            isBetter = true;
        }

        // revert solutions
        for (Particle e : parents) {
	    e.setCandidateSolution(tmp.get(e));
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

    public abstract P3<Boolean, Particle, Particle> doAction(PSO algorithm, Enum solutionType, Enum fitnessType);

    @Override
    public Topology<Particle> f(PSO algorithm) {
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
