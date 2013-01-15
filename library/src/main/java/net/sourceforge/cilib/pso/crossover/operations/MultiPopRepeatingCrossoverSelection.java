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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.crossover.pbestupdate.CurrentPositionOffspringPBestProvider;
import net.sourceforge.cilib.pso.crossover.velocityprovider.ZeroOffspringVelocityProvider;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.util.selection.Samples;

/**
 * Perform crossover on archive solutions by creating a dummy particle for each
 * archive solution selected.
 */
public class MultiPopRepeatingCrossoverSelection extends CrossoverSelection {

    private ProbabilityDistributionFunction random = new UniformDistribution();
    private ControlParameter retries;

    public MultiPopRepeatingCrossoverSelection() {
        super();
        crossoverStrategy.setPbestProvider(new CurrentPositionOffspringPBestProvider());
        crossoverStrategy.setVelocityProvider(new ZeroOffspringVelocityProvider());
        this.retries = ConstantControlParameter.of(10);
    }

    public MultiPopRepeatingCrossoverSelection(MultiPopRepeatingCrossoverSelection copy) {
        super(copy);
        this.retries = copy.retries;
    }

    @Override
    public P3<Boolean, Particle, Particle> select(PSO algorithm, Enum solutionType, Enum fitnessType) {
        boolean isBetter = false;

        List<Particle> parents = new ArrayList<Particle>();

        MultiPopulationBasedAlgorithm algs = (MultiPopulationBasedAlgorithm) AbstractAlgorithm.getAlgorithmList().get(0);
        List<PopulationBasedAlgorithm> pops = algs.getPopulations();

        if (pops.size() > 2) {
            pops = selector.on(pops).select(Samples.first(crossoverStrategy.getNumberOfParents()).unique());
        } else {
            pops = selector.on(pops).select(Samples.all());
            parents.add((Particle) selector.on(pops.get((int) random.getRandomNumber(0, pops.size())).getTopology()).select());
        }

        for (PopulationBasedAlgorithm a : pops) {
            parents.add((Particle) selector.on(a.getTopology()).select());
        }

        Map<Particle, StructuredType> tmp = Maps.newHashMap();

        //put pbest as candidate solution for the crossover
        for (Particle e : parents) {
            tmp.put(e, e.getCandidateSolution());
            e.getProperties().put(EntityType.CANDIDATE_SOLUTION, e.getNeighbourhoodBest().getBestPosition());
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

        if (isBetter && offspring.getNeighbourhoodBest() == null) {
            offspring.setNeighbourhoodBest(offspring.getClone());
        }
        return P.p(isBetter, selectedParticle, offspring);
    }

    @Override
    public P3<Boolean, Particle, Particle> doAction(PSO algorithm, Enum solutionType, Enum fitnessType) {
        int counter = 0;
        boolean isBetter;
        P3<Boolean, Particle, Particle> result;

        do {
            result = select(algorithm, solutionType, fitnessType);
            isBetter = result._1();
        } while (++counter < retries.getParameter() && !isBetter);

        return result;
    }

    public void setRetries(ControlParameter retries) {
        this.retries = retries;
    }

    public ControlParameter getRetries() {
        return retries;
    }

    @Override
    public MultiPopRepeatingCrossoverSelection getClone() {
        return new MultiPopRepeatingCrossoverSelection(this);
    }
}
