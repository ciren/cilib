package net.sourceforge.cilib.moo.archive;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.cilib.problem.OptimisationSolution;

/**
 * 
 * @author Andries Engelbrecht
 *
 */

public class StandardArchive extends ArrayList<OptimisationSolution> implements Archive {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2395164492771478604L;
	
	private LocalGuideStrategy localGuideStrategy;
	private GlobalGuideStrategy globalGuideStrategy;

	public void StandardArchive() {
		localGuideStrategy = new DominatesStrategy();
		globalGuideStrategy = null;
	}
	
	public void accept(OptimisationSolution candidateNonDominatedSolution) {
		
	}
	
	public void accept(Collection<OptimisationSolution> paretoFront) {
		
	}

	public GlobalGuideStrategy getGlobalGuideStrategy() {
		return globalGuideStrategy;
	}

	public void setGlobalGuideStrategy(GlobalGuideStrategy globalGuideStrategy) {
		this.globalGuideStrategy = globalGuideStrategy;
	}

	public LocalGuideStrategy getLocalGuideStrategy() {
		return localGuideStrategy;
	}

	public void setLocalGuideStrategy(LocalGuideStrategy localGuideStrategy) {
		this.localGuideStrategy = localGuideStrategy;
	}

	

}
