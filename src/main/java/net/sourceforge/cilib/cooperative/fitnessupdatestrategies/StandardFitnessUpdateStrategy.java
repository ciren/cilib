package net.sourceforge.cilib.cooperative.fitnessupdatestrategies;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.OptimisationProblem;

public class StandardFitnessUpdateStrategy implements FitnessUpdateStrategy {
	public void updateFitness(OptimisationProblem problem, Entity context) {
		context.setFitness(problem.getFitness(context.get(), true));
	}
}
