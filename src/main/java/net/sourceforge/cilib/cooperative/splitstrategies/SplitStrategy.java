package net.sourceforge.cilib.cooperative.splitstrategies;

import java.util.List;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.cooperative.CooperativeEntity;
import net.sourceforge.cilib.cooperative.SplitCooperativeAlgorithm;
import net.sourceforge.cilib.problem.OptimisationProblem;

/**
 * The interface used to split up a given {@link OptimisationProblem} into sub-problems.
 * @author Theuns Cloete
 */
public interface SplitStrategy {
	/**
	 * Splits up the given {@link OptimisationProblem} into sub-problems and assigns all the sub-problems to {@link Algorithm}s in the population.
	 * @param problems The {@link OptimisationProblem} that will be split up.
	 * @param populations The {@link Algorithm}s participating in the {@link SplitCooperativeAlgorithm}.
	 */
	public void split(OptimisationProblem problem, CooperativeEntity context, List<PopulationBasedAlgorithm> populations);
}
