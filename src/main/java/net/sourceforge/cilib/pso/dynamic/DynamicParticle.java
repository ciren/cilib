package net.sourceforge.cilib.pso.dynamic;

import java.util.Map;

import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.Type;

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
    	this.velocityUpdateStrategy = copy.velocityUpdateStrategy.clone(); // Check this
    	this.positionUpdateStrategy = copy.positionUpdateStrategy.clone();
    	this.neighbourhoodBestUpdateStrategy = copy.neighbourhoodBestUpdateStrategy;
    	this.velocityInitialisationStrategy = copy.velocityInitialisationStrategy.clone();
    	
    	this.fitnessCalculator = copy.fitnessCalculator.clone();
    	
    	for (Map.Entry<String, Type> entry : copy.properties.entrySet()) {
    		String key = entry.getKey().toString();
    		this.properties.put(key, entry.getValue().clone());
    	}
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
