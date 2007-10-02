/*
 * StandardVelocityUpdate.java
 *
 * Created on September 22, 2003, 1:03 PM
 *
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

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.RandomizingControlParameter;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.type.types.container.Vector;


/**
 *
 * @author  Edwin Peer
 */
public class StandardVelocityUpdate implements VelocityUpdateStrategy {
	private static final long serialVersionUID = 8204479765311251730L;
	
	protected ControlParameter inertiaWeight;
    protected ControlParameter socialAcceleration;
    protected ControlParameter cognitiveAcceleration;
    protected ControlParameter vMax;
    
    /** Creates a new instance of StandardVelocityUpdate */
    public StandardVelocityUpdate() {      
        inertiaWeight = new ConstantControlParameter();
        cognitiveAcceleration = new RandomizingControlParameter();
        socialAcceleration = new RandomizingControlParameter();
        vMax = new ConstantControlParameter();
        
        inertiaWeight.setParameter(0.729844);
        cognitiveAcceleration.setParameter(1.496180);
        socialAcceleration.setParameter(1.496180);
        vMax.setParameter(Double.MAX_VALUE);
    }
    
    
    /**
     * 
     * @param copy
     */
    public StandardVelocityUpdate(StandardVelocityUpdate copy) {
    	this.inertiaWeight = copy.inertiaWeight.clone();
    	this.cognitiveAcceleration = copy.cognitiveAcceleration.clone();
    	this.socialAcceleration = copy.socialAcceleration.clone();
    	this.vMax = copy.vMax.clone();
    }
    
    
    /**
     * 
     */
    public StandardVelocityUpdate clone() {
    	return new StandardVelocityUpdate(this);
    }

    
    /**
     * 
     */
    public void updateVelocity(Particle particle) {
    	Vector velocity = (Vector) particle.getVelocity();
    	Vector position = (Vector) particle.getPosition();
    	Vector bestPosition = (Vector) particle.getBestPosition();
    	Vector nBestPosition = (Vector) particle.getNeighbourhoodBest().getBestPosition();
        
        for (int i = 0; i < particle.getDimension(); ++i) {
    		double value = inertiaWeight.getParameter()*velocity.getReal(i) 
    			+ (bestPosition.getReal(i) - position.getReal(i)) * cognitiveAcceleration.getParameter()
    			+ (nBestPosition.getReal(i) - position.getReal(i)) * socialAcceleration.getParameter();
    		velocity.setReal(i, value);
    		
    		clamp(velocity, i);
    	}
    }
    
    
    /**
     * 
     */
    public void updateControlParameters(Particle particle) {
    	this.inertiaWeight.updateParameter();
    	this.cognitiveAcceleration.updateParameter();
    	this.socialAcceleration.updateParameter();
    	this.vMax.updateParameter();
	}
    
    
    /**
     * TODO: Need to have a VMax strategy
	 * @param velocity
	 * @param i
	 */
	protected void clamp(Vector velocity, int i) {
		if (velocity.getReal(i) < -vMax.getParameter())
			velocity.setReal(i, -vMax.getParameter());
		else if (velocity.getReal(i) > vMax.getParameter())
			velocity.setReal(i, vMax.getParameter());
	}

	
    /**
     * @return Returns the cognitiveComponent.
     */
    public ControlParameter getCognitiveAcceleration() {
        return cognitiveAcceleration;
    }

    /**
     * @param cognitiveComponent The cognitiveComponent to set.
     */
    public void setCognitiveAcceleration(ControlParameter cognitiveComponent) {
        this.cognitiveAcceleration = cognitiveComponent;
    }

    /**
     * @return Returns the inertiaComponent.
     */
    public ControlParameter getInertiaWeight() {
        return inertiaWeight;
    }

    /**
     * @param inertiaComponent The inertiaComponent to set.
     */
    public void setInertiaWeight(ControlParameter inertiaWeight) {
        this.inertiaWeight = inertiaWeight;
    }

    /**
     * @return Returns the socialComponent.
     */
    public ControlParameter getSocialAcceleration() {
        return socialAcceleration;
    }

    /**
     * @param socialComponent The socialComponent to set.
     */
    public void setSocialAcceleration(ControlParameter socialComponent) {
        this.socialAcceleration = socialComponent;
    }
    
    
    public ControlParameter getVMax() {
		return vMax;
	}

	public void setVMax(ControlParameter max) {
		vMax = max;
	}

}
