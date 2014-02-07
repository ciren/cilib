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
import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Property;
import net.sourceforge.cilib.moo.archive.Archive;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;
import net.sourceforge.cilib.pso.particle.Particle;

/**
 * This strategy re-evaluates the solutions in the archive after a change occurred
 * in the environment. If solutions have become dominated after the change, 
 * they are removed from the archive.
 */
public class ArchiveReevaluationResponseStrategy extends EnvironmentChangeResponseStrategy {

    private static final long serialVersionUID = 4757162276962451681L;

    @Override
    public EnvironmentChangeResponseStrategy getClone() {
        return this;
    }

    @Override
	protected <P extends Particle, A extends SinglePopulationBasedAlgorithm<P>> void performReaction(
			A algorithm) {
        for (Entity entity : algorithm.getTopology()) {
            entity.put(Property.BEST_FITNESS, entity.getBehaviour().getFitnessCalculator().getFitness(entity));
            //entity.put(Property.BEST_POSITION, entity.getPosition());
            entity.updateFitness(entity.getBehaviour().getFitnessCalculator().getFitness(entity));
        }

        Problem problem = AbstractAlgorithm.getAlgorithmList().head().getOptimisationProblem();

        List<OptimisationSolution> newList = new LinkedList<>();
        for (OptimisationSolution solution : Archive.Provider.get()) {
            OptimisationSolution os = new OptimisationSolution(solution.getPosition(), problem.getFitness(solution.getPosition()));
            newList.add(os);
        }

        Archive.Provider.get().clear();
        Archive.Provider.get().addAll(newList);
    }

}
