package net.sourceforge.cilib.moo.archive;

import java.util.Collection;

import net.sourceforge.cilib.problem.OptimisationSolution;

/* *
 * @author Andries Engelbrecht
 */

public interface Archive extends Collection<OptimisationSolution> {
	
	/*
	 * used to decide if a non-dominated solution should be accepted in the archive
	 */
	public void accept(OptimisationSolution candidateNonDominatedSolution);
	
	public void accept(Collection<OptimisationSolution> paretoFront);
	
}
