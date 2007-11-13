package net.sourceforge.cilib.pso.dynamic;

import net.sourceforge.cilib.pso.particle.StandardParticle;

/**
 * Special particle type to use with dynamic algorithms. The extra functionality
 * that it adds is the ability to re-evaluate both current and best position of
 * the particle. A dynamic algorithm usually re-evaluates all particles when a
 * change in the environment has been detected.
 * 
 * @author Anna Rakitianskaia
 */
public class DynamicParticle extends StandardParticle {
	
	private static final long serialVersionUID = 1752969607979236619L;
	
	public DynamicParticle() {
		super();
	}

    public DynamicParticle(DynamicParticle copy) {
    	super(copy);
    }
    
    public DynamicParticle clone() {
       	return new DynamicParticle(this);
    }
           
    /**
     * re-evaluate both best and current position of the particle
     */
    public void reevaluate() {
    	this.properties.put("bestFitness", fitnessCalculator.getFitness(getBestPosition(), true));
    	this.calculateFitness();
    }
}
