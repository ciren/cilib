package net.sourceforge.cilib.cooperative.splitstrategies;

import java.util.List;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.InitialisationException;
import net.sourceforge.cilib.algorithm.OptimisationAlgorithm;
import net.sourceforge.cilib.cooperative.CooperativeEntity;
import net.sourceforge.cilib.problem.CooperativeOptimisationProblemAdapter;
import net.sourceforge.cilib.problem.OptimisationProblem;

/**
 * TODO test this class
 * Split an {@link OptimisationProblem} into sub-problems of equal size/dimension.
 * @author Theuns Cloete
 */
public class PerfectSplitStrategy implements SplitStrategy {
	public void split(OptimisationProblem problem, CooperativeEntity context, List<Algorithm> populations) {
		if(populations.size() < 2)
			throw new IllegalArgumentException("There should at least be two Cooperating populations in a Cooperative Algorithm");
		if(problem.getDomain().getDimension() % populations.size() != 0)
			throw new InitialisationException("A Problem with dimension " + problem.getDomain().getDimension() + " cannot be split into parts of equal size when using " + populations.size() + " populatins");
		int dimension = problem.getDomain().getDimension() / populations.size();
		int offset = 0;
		for(Algorithm population : populations) {
			//TODO check whether this cast is safe
			((OptimisationAlgorithm)population).setOptimisationProblem(new CooperativeOptimisationProblemAdapter(problem, context, dimension, offset));
			offset += dimension;
		}
	}
}
