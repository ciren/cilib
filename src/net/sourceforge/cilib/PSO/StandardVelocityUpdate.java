/*
 * StandardVelocityUpdate.java
 *
 * Created on September 22, 2003, 1:03 PM
 *
 * 
 * Copyright (C) 2003, 2004 - CIRG@UP 
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

package net.sourceforge.cilib.PSO;

import net.sourceforge.cilib.Type.Types.Vector;


/**
 *
 * @author  espeer
 */
public class StandardVelocityUpdate implements VelocityUpdate {
    
    /** Creates a new instance of StandardVelocityUpdate */
    public StandardVelocityUpdate() {
        maximumVelocity = Double.MAX_VALUE;
        
        inertiaComponent = new StandardInertia();
        cognitiveComponent = new StandardAcceleration();
        socialComponent = new StandardAcceleration();     
    }

    public void updateVelocity(Particle particle) {
        //double[] velocity = particle.getVelocity();
        //double[] position = particle.getPosition();
        //double[] bestPosition = particle.getBestPosition();
        //double[] nBestPosition = particle.getNeighbourhoodBest().getBestPosition();
    	Vector velocity = particle.getVelocity();
    	Vector position = particle.getPosition();
    	Vector bestPosition = particle.getBestPosition();
    	Vector nBestPosition = particle.getNeighbourhoodBest().getBestPosition();
        
        /*for (int i = 0; i < particle.getDimension(); ++i) {
            velocity[i] = inertiaComponent.get() * velocity[i]
                + (bestPosition[i] - position[i]) * cognitiveComponent.get()
                + (nBestPosition[i] - position[i]) * socialComponent.get();
            
            clamp(velocity, i);
        }*/
    	
    	for (int i = 0; i < particle.getDimension(); ++i) {
    		double value = inertiaComponent.get()*velocity.getReal(i) 
    			+ (bestPosition.getReal(i) - position.getReal(i)) * cognitiveComponent.get()
    			+ (nBestPosition.getReal(i) - position.getReal(i)) * socialComponent.get();

    		velocity.setReal(i, value);
    		clamp(velocity, i);
    	}
    }    
    
    
    /**
	 * @param velocity
	 * @param i
	 */
	/*protected void clamp(double[] velocity, int i) {
		if (velocity[i] < -maximumVelocity) {
		    velocity[i] = -maximumVelocity;
		}
		else if (velocity[i] > maximumVelocity) {
		    velocity[i] = maximumVelocity;
		}
	}*/
	
	protected void clamp(Vector velocity, int i) {
		if (velocity.getReal(i) < -maximumVelocity)
			velocity.setReal(i, -maximumVelocity);
		else if (velocity.getReal(i) > maximumVelocity)
			velocity.setReal(i, maximumVelocity);
	}

	/**
     * Accessor for the maximum absolute velocity permitted in any dimension. 
     *
     * @return The maximum velocity.
     */
    public double getMaximumVelocity() {
        return maximumVelocity;
    }
    
    /**
     * Sets the maximum absolute velocity permitted in any dimension. The default is Double.MAX_VALUE. That is, vmax is not used by default.
     *
     * @param vmax The maximum velocity.
     */
    public void setMaximumVelocity(double maximumVelocity) {
        this.maximumVelocity = maximumVelocity;
    }

    /**
     * @return Returns the cognitiveComponent.
     */
    public VelocityComponent getCognitiveComponent() {
        return cognitiveComponent;
    }

    /**
     * @param cognitiveComponent The cognitiveComponent to set.
     */
    public void setCognitiveComponent(VelocityComponent cognitiveComponent) {
        this.cognitiveComponent = cognitiveComponent;
    }

    /**
     * @return Returns the inertiaComponent.
     */
    public VelocityComponent getInertiaComponent() {
        return inertiaComponent;
    }

    /**
     * @param inertiaComponent The inertiaComponent to set.
     */
    public void setInertiaComponent(VelocityComponent inertiaComponent) {
        this.inertiaComponent = inertiaComponent;
    }

    /**
     * @return Returns the socialComponent.
     */
    public VelocityComponent getSocialComponent() {
        return socialComponent;
    }

    /**
     * @param socialComponent The socialComponent to set.
     */
    public void setSocialComponent(VelocityComponent socialComponent) {
        this.socialComponent = socialComponent;
    }

    private double maximumVelocity;
    
    protected VelocityComponent inertiaComponent;
    protected VelocityComponent socialComponent;
    protected VelocityComponent cognitiveComponent;
    
    
}
