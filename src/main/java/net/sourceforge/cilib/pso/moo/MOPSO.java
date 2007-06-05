package net.sourceforge.cilib.pso.moo;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.cilib.moo.archive.Archive;
import net.sourceforge.cilib.moo.archive.StandardArchive;
import net.sourceforge.cilib.problem.MOOptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.MultiObjectiveParticle;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.positionupdatestrategies.GaussianPositionUpdateStrategy;
import net.sourceforge.cilib.type.types.Vector;

/**
 * @author CIRG
 */
public class MOPSO extends PSO  {
	private static final long serialVersionUID = -4388678573614103683L;
	
	private MOOptimisationProblem moproblem;
	private Archive archive;
	//private LocalGuideStrategy localGuideStrategy;
	
	public MOPSO() {
		super();
		
		this.archive = new StandardArchive();
		
		Particle particle = new MultiObjectiveParticle();
		particle.setPositionUpdateStrategy(new GaussianPositionUpdateStrategy());
		this.getInitialisationStrategy().setEntityType(particle);		
	}
	
	@Override
	public void performInitialisation() {
		super.performInitialisation();
		
		for (Particle particle : getTopology()) {
			//particle.setFitness(this.moproblem.getFitness(particle.getPosition(), true));
			particle.calculateFitness();
		}
		
		// Look at Set
		Collection<OptimisationSolution> paretoFront = new ArrayList<OptimisationSolution>();
		
		// TODO : prevent fitness re-evaluations
		// TODO : Check Jaco's code
		paretoFront.add(new OptimisationSolution(this.getMoproblem(), this.getTopology().get(0).getPosition()));
		for (Particle particle : getTopology()) {
			for (OptimisationSolution solution : paretoFront) {
				int result = solution.getFitness().compareTo(particle.getFitness()); // Check this comparing 
				if (result > 0) {
					// Replace
				}
				else if (result == 0) {
					// Add
					break;
				}
				else {
					// Ignore
				}
			}
		}
		
		this.archive.addAll(paretoFront);
		
		createHypercubes();
	}
	
	/*
	 * How the heck do we make hypercubes???
	 */
	private void createHypercubes() { // Look at creating a strategy to do this
		
	}
	
	@Override
	public void algorithmIteration() {
		// TODO Auto-generated method stub
		
		StandardArchive standardArchive = (StandardArchive) this.archive;
		
		for (Particle particle : getTopology()) {
			Particle globalGuide = selectGlobalGuide(/*hypercube*/);
		
			standardArchive.getLocalGuideStrategy().updateLocalGuide(particle);
			Vector localGuide = standardArchive.getLocalGuideStrategy().getLocalGuide();
			
			particle.setNeighbourhoodBest(globalGuide);
			particle.setBestPosition(localGuide);
			
			particle.updateVelocity();
			particle.updatePosition();
		}
		
		maintainBoundaries();
		
		for (Particle particle : getTopology()) {
			//particle.setFitness(this.moproblem.getFitness(particle.getPosition(), true));
			particle.calculateFitness();
		}
		
		// TODO :: FIX THIS!!!!!!!!!!!!!!!!!!!!
		//		 Look at Set
		Collection<OptimisationSolution> paretoFront = new ArrayList<OptimisationSolution>();
		
		// TODO : prevent fitness re-evaluations
		// TODO : Check Jaco's code
		paretoFront.add(new OptimisationSolution(this.getMoproblem(), this.getTopology().get(0).getPosition()));
		for (Particle particle : getTopology()) {
			for (OptimisationSolution solution : paretoFront) {
				int result = solution.getFitness().compareTo(particle.getFitness()); // Check this comparing 
				if (result > 0) {
					// Replace
				}
				else if (result == 0) {
					// Add
					break;
				}
				else {
					// Ignore
				}
			}
		}
		
		archive.accept(paretoFront);
	}
	
	private Particle selectGlobalGuide() { // Look at creating a strategy to do this
		return null;
	}
	
	//private Vector selectLocalGuide() { // Look at creating a strategy to do this
	//	return null;
	//}
	
	private void maintainBoundaries() {
		
	}

	public Archive getArchive() {
		return archive;
	}

	public void setArchive(Archive archive) {
		this.archive = archive;
	}

	public MOOptimisationProblem getMoproblem() {
		return moproblem;
	}

	public void setMoproblem(MOOptimisationProblem moproblem) {
		this.moproblem = moproblem;
	}
	
	

}
