package net.sourceforge.cilib.pso.dynamic;

import java.util.Iterator;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.velocityupdatestrategies.StandardVelocityUpdate;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Velocity update strategy that the so called Charged PSO makes use of.
 * This is an implementation of the original Charged PSO algorithm
 * developed by Blackwell and Bentley and then further improved by
 * Blackwell and Branke. 
 * 
 * @author Anna Rakitianskaia
 *
 */
public class ChargedVelocityUpdateStrategy extends StandardVelocityUpdate {
	
	private static final long serialVersionUID = 365924556746583124L;
	private double pCore; // lower limit
	private double p; // upper limit

	public ChargedVelocityUpdateStrategy() {
		super();
		pCore = 1;
		p = 30;
	}
	
	public ChargedVelocityUpdateStrategy(ChargedVelocityUpdateStrategy copy) {
		this.inertiaWeight = copy.inertiaWeight.clone();
    	this.cognitiveAcceleration = copy.cognitiveAcceleration.clone();
    	this.socialAcceleration = copy.socialAcceleration.clone();
    	this.vMax = copy.vMax.clone();
    	
    	this.p = copy.p;
    	this.pCore = copy.pCore;
	}
	
	public ChargedVelocityUpdateStrategy clone() {
		return new ChargedVelocityUpdateStrategy(this);
	}
	
	@Override
	public void updateVelocity(Particle particle) {
    	Vector velocity = (Vector) particle.getVelocity();
    	Vector position = (Vector) particle.getPosition();
    	Vector bestPosition = (Vector) particle.getBestPosition();
    	Vector nBestPosition = (Vector) particle.getNeighbourhoodBest().getBestPosition();
    	
    	Vector acceleration = new Vector(velocity.getDimension());
    	acceleration.initialise(velocity.getDimension(), new Real(0));
    	PSO pso = (PSO)Algorithm.get();
    	Iterator<Particle> iter = null;
    	// make iter point to the current particle
    	for (Iterator<Particle> i = pso.getTopology().iterator(); i.hasNext(); ) {
    		if(i.next().getId().equals(particle.getId())) { 
    			iter = i;
    			break;
    		}
    	}
    	// Calculate acceleration of the current particle	
    	for (int i = 0; i < particle.getDimension(); ++i) {
    		double accSum = 0;
	    	for (Iterator<Particle> j = pso.getTopology().neighbourhood(iter); j.hasNext(); ) {
	            ChargedParticle other = (ChargedParticle)j.next();
	            if(particle.getId().equals(other.getId())) continue;
	            double qi = ((ChargedParticle)particle).getCharge();
	            double qj = other.getCharge();
	            Vector rij = position.subtract((Vector) other.getPosition());
	            double magnitude = rij.norm();
	            if(pCore <= magnitude && magnitude <= p) {
	            	accSum += (qi * qj / Math.pow(magnitude, 3)) * rij.getReal(i);
	            }
	        }
	    	acceleration.setReal(i, accSum);
    	}
        
        for (int i = 0; i < particle.getDimension(); ++i) {
    		double value = inertiaWeight.getParameter()*velocity.getReal(i) 
    			+ (bestPosition.getReal(i) - position.getReal(i)) * cognitiveAcceleration.getParameter()
    			+ (nBestPosition.getReal(i) - position.getReal(i)) * socialAcceleration.getParameter()
    			+ acceleration.getReal(i);
    		velocity.setReal(i, value);
    		
    		clamp(velocity, i);
    	}
    }

	/**
	 * @return the p
	 */
	public double getP() {
		return p;
	}

	/**
	 * @param p the p to set
	 */
	public void setP(double p) {
		this.p = p;
	}

	/**
	 * @return the pCore
	 */
	public double getPCore() {
		return pCore;
	}

	/**
	 * @param core the pCore to set
	 */
	public void setPCore(double core) {
		pCore = core;
	}
}
