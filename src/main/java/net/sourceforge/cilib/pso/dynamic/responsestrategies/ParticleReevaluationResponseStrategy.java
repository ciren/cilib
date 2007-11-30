package net.sourceforge.cilib.pso.dynamic.responsestrategies;

import java.util.Iterator;

import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.dynamic.DynamicParticle;

/**
 * @author Anna Rakitianskaia
 */
public class ParticleReevaluationResponseStrategy implements
		EnvironmentChangeResponseStrategy {
	
	public ParticleReevaluationResponseStrategy() {
		// empty constructor
	}
	
	public ParticleReevaluationResponseStrategy(ParticleReevaluationResponseStrategy copy) {
		// empty copy constructor
	}

	public ParticleReevaluationResponseStrategy getClone() {
		return new ParticleReevaluationResponseStrategy(this);
	}
	
	/** 
	 * Respond to environment change by re-evaluating each particle's position, personal best and neighbourhood best.
	 * @param algorithm PSO algorithm that has to respond to environment change
	 */
	public void respondToChange(PSO algorithm) {
		reevaluateParticles(algorithm);
	}
	
	/**
	 * Re-evaluate each particle's position, personal best and neighbourhood best.
	 * @param algorithm PSO algorithm that has to respond to environment change
	 */
	protected void reevaluateParticles(PSO algorithm) {

		Topology<Particle> topology = algorithm.getTopology();
		
		// Reevaluate current position. Update personal best (done by reevaluate()).
		for (Iterator<? extends Particle> i = topology.iterator(); i.hasNext(); ) {
			DynamicParticle current = (DynamicParticle)(i.next());
			current.reevaluate();
		}
		
		// Update the neighbourhood best
		for (Iterator<? extends Particle> i = topology.iterator(); i.hasNext(); ) {
			Particle current = i.next();
            for (Iterator<? extends Particle> j = topology.neighbourhood(i); j.hasNext(); ) {
            	Particle other = j.next();
            	if (current.getSocialBestFitness().compareTo( other.getNeighbourhoodBest().getSocialBestFitness()) > 0) {
            		other.setNeighbourhoodBest(current);
                }
            } // end for
		} // end for
	}
} // end class
