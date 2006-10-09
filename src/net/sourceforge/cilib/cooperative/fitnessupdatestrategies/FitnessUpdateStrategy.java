package net.sourceforge.cilib.cooperative.fitnessupdatestrategies;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.OptimisationProblem;

public interface FitnessUpdateStrategy {
	void updateFitness(OptimisationProblem problem, Entity context);
}
