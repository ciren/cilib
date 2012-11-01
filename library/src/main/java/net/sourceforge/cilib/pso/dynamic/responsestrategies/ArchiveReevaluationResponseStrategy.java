/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.dynamic.responsestrategies;

import java.util.LinkedList;
import java.util.List;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.moo.archive.Archive;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;

/**
 *
 */
public class ArchiveReevaluationResponseStrategy extends EnvironmentChangeResponseStrategy<PopulationBasedAlgorithm> {

    private static final long serialVersionUID = 4757162276962451681L;

    @Override
    public EnvironmentChangeResponseStrategy<PopulationBasedAlgorithm> getClone() {
        return this;
    }

    @Override
    protected void performReaction(PopulationBasedAlgorithm algorithm) {
        for (Entity entity : algorithm.getTopology()) {
            entity.getProperties().put(EntityType.Particle.BEST_FITNESS, entity.getFitnessCalculator().getFitness(entity));
            //entity.getProperties().put(EntityType.Particle.BEST_POSITION, entity.getCandidateSolution());
            entity.calculateFitness();
        }

        Problem problem = AbstractAlgorithm.getAlgorithmList().get(0).getOptimisationProblem();

        List<OptimisationSolution> newList = new LinkedList<OptimisationSolution>();
        for (OptimisationSolution solution : Archive.Provider.get()) {
            OptimisationSolution os = new OptimisationSolution(solution.getPosition(), problem.getFitness(solution.getPosition()));
            newList.add(os);
        }

        Archive.Provider.get().clear();
        Archive.Provider.get().addAll(newList);
    }
}
