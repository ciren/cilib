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
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.moo.archive.Archive;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.problem.solution.InferiorFitness;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.crossover.pbestupdate.CurrentPositionOffspringPBestProvider;
import net.sourceforge.cilib.pso.crossover.velocityprovider.ZeroOffspringVelocityProvider;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.util.selection.Samples;
import net.sourceforge.cilib.util.selection.recipes.ElitistSelector;

/**
 * Perform crossover on archive solutions by creating a dummy particle for each
 * archive solution selected.
 */
public class ArchiveRepeatingCrossoverSelection extends CrossoverSelection {

    private ControlParameter retries;

    public ArchiveRepeatingCrossoverSelection() {
        super();
        crossoverStrategy.setPbestProvider(new CurrentPositionOffspringPBestProvider());
        crossoverStrategy.setVelocityProvider(new ZeroOffspringVelocityProvider());
        this.retries = ConstantControlParameter.of(10);
    }

    public ArchiveRepeatingCrossoverSelection(ArchiveRepeatingCrossoverSelection copy) {
        super(copy);
        this.retries = copy.retries;
    }

    @Override
    public P3<Boolean, Particle, Particle> select(PSO algorithm, Enum solutionType, Enum fitnessType) {
        boolean isBetter = false;

        List<Particle> parents = new ArrayList<Particle>();
        List<OptimisationSolution> solutions = new ArrayList<OptimisationSolution>();
        Archive archive = Archive.Provider.get();

        //select 3 non-dominated solutions and create dummy particles to perform crossover
        //if less than 3, select the remaining as random best particles
        if (archive.size() < crossoverStrategy.getNumberOfParents()) {
            parents = new ElitistSelector().on(algorithm.getTopology()).select(Samples.first(crossoverStrategy.getNumberOfParents() - archive.size()).unique());
            if (!archive.isEmpty()) {
                solutions = selector.on(archive).select(Samples.all()); //should give all archive solutions
            }
        } else {
            solutions = selector.on(archive).select(Samples.first(crossoverStrategy.getNumberOfParents()).unique());
        }

        //create particle from each solution
        for (OptimisationSolution sol : solutions) {
            Particle p = new StandardParticle();
            p.getProperties().put(EntityType.CANDIDATE_SOLUTION, sol.getPosition());
            p.getProperties().put(EntityType.Particle.BEST_FITNESS, InferiorFitness.instance());
            p.getProperties().put(EntityType.Particle.BEST_POSITION, sol.getPosition());
            p.calculateFitness();
            p.getProperties().put(EntityType.Particle.BEST_FITNESS, p.getFitness());
            p.getProperties().put(EntityType.PREVIOUS_FITNESS, p.getFitness());
            parents.add(p);
        }

        Map<Particle, StructuredType> tmp = Maps.newHashMap();

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
    public ArchiveRepeatingCrossoverSelection getClone() {
        return new ArchiveRepeatingCrossoverSelection(this);
    }
}
