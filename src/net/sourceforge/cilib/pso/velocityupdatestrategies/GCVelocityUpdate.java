/*
 * GCVelocityUpdate.java
 *
 * Created on September 22, 2003, 2:52 PM
 * 
 * Copyright (C) 2003 - 2006 
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science 
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA 
 */

package net.sourceforge.cilib.pso.velocityupdatestrategies;

import java.util.Random;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.math.random.generator.KnuthSubtractive;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.GCDecorator;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.Vector;

/**
 *
 * @author  Edwin Peer
 */
public class GCVelocityUpdate implements VelocityUpdateStrategy {
	
    private StandardVelocityUpdate standard;
    private Random rhoRandomGenerator;
    
    private int successThreshold;
    private int failureThreshold;
    private double rhoContractCoefficient;
    private double rhoExpandCoefficient;
    
    
    /** Creates a new instance of GCVelocityUpdate */
    public GCVelocityUpdate() {
        standard = new StandardVelocityUpdate();
        rhoRandomGenerator = new KnuthSubtractive();
        
        successThreshold = 5;
        failureThreshold = 5;
        rhoExpandCoefficient = 1.2;
        rhoContractCoefficient = 0.5;
    }
    
    public GCVelocityUpdate(GCVelocityUpdate copy) {
    	this();
    	
    	successThreshold = copy.successThreshold;
    	failureThreshold = copy.failureThreshold;
    	rhoExpandCoefficient = copy.rhoExpandCoefficient;
    	rhoContractCoefficient = copy.rhoContractCoefficient;
    }
    
    public GCVelocityUpdate clone() {
    	return new GCVelocityUpdate(this);
    }
    
    public void setStandardVelocityUpdate(StandardVelocityUpdate standard) {
        this.standard = standard;
    }
    
    public StandardVelocityUpdate getStandardVelocityUpdate() {
        return standard;
    }
    
    public void updateVelocity(Particle particle) {
        if (particle.getNeighbourhoodBest().getId() == particle.getId()) {
            double rho = GCDecorator.extract(particle).getRho();

            Vector velocity = (Vector) particle.getVelocity();            
            Vector nBestPosition = (Vector) particle.getNeighbourhoodBest().getBestPosition();
            
            PSO pso = (PSO) Algorithm.get();
            Vector swarmBestPosition = (Vector) pso.getBestParticle().getPosition();
            
            for (int i = 0; i < particle.getDimension(); ++i) {
            	double result = nBestPosition.getReal(i) - swarmBestPosition.getReal(i) + 
            		this.standard.getInertiaWeight().getParameter()*velocity.getReal(i) + 
            		rho * (1 - 2 * rhoRandomGenerator.nextFloat());
                /*velocity.setReal(i, bestPosition.getReal(i) - position.getReal(i) + 
                standard.getInertiaComponent().get() * velocity.getReal(i) 
                  + rho * (1 - 2 * rhoRandomGenerator.nextFloat()));*/
            	velocity.setReal(i, result);
            }
        }
        else {
            standard.updateVelocity(particle);
        }
    }
    
    /**
     * Sets the random number generator used in the GC update equation. The default is {@link net.sourceforge.cilib.math.random.generator.KnuthSubtractive}.
     *
     * @param rhoRandomGenerator The {@link java.util.Random} generator used in the GC update.
     */
    public void setRhoRandomGenerator(Random rhoRandomGenerator) {
        this.rhoRandomGenerator = rhoRandomGenerator;
    }
    
    /**
     * Accessor for the random number generator used in the GC update equation.
     *
     * @return The {@link java.util.Random} generator used in the GC update.
     */
    public Random getRhoRandomGenerator() {
        return rhoRandomGenerator;
    }
    
    
    /**
     * Sets the success threshold used to determine when to increase rho. The default is 5.
     *
     * @param successThreshold The success threshold.
     */
    public void setSuccessThreshold(int successThreshold) {
        this.successThreshold = successThreshold;
    }
    
    /**
     * Accessor for the success threshold used to determine when to increase rho.
     *
     * @return The success threshold.
     */
    public int getSuccessThreshold() {
        return successThreshold;
    }
    
    /**
     * Sets the failure threshold used to determine when to decrease rho. The default is 5.
     *
     * @param failureThreshold The failure threshold.
     */
    public void setFailureThreshold(int failureThreshold) {
        this.failureThreshold = failureThreshold;
    }
    
    /**
     * Accessor for the failure threshold used to determine when to decrease rho. The default is 5.
     *
     * @return The failure threshold.
     */
    public int getFailureThreshold() {
        return failureThreshold;
    }
    
    /**
     * Sets the multiplication coefficient used to increase rho. The default is 1.2.
     *
     * @param rhoExpandCoefficient The rho expansion coefficient
     */
    public void setRhoExpandCoefficient(double rhoExpandCoefficient) {
        this.rhoExpandCoefficient = rhoExpandCoefficient;
    }
    
    /**
     * Accessor for the multiplication coefficient used to increase rho.
     *
     * @return The rho expansion coefficient.
     */
    public double getRhoExpandCoefficient() {
        return rhoExpandCoefficient;
    }
    
    /**
     * Sets the multiplication coefficient used to decrease rho. The default is 0.5.
     *
     * @param rhoContractCoefficient The rho contraction coefficient.
     */
    public void setRhoContractCoefficient(double rhoContractCoefficient) {
        this.rhoContractCoefficient = rhoContractCoefficient;
    }
    
    /** 
     * Accessor for the multiplication coefficient used to decrease rho. 
     *
     * @return The rho contration coefficient.
     */
    public double getRhoContractCoefficient() {
        return rhoContractCoefficient;
    }
    

	public void updateControlParameters() {
		// TODO Auto-generated method stub
		
	}
    
}
