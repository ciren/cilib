/*
 * FIPSVelocityUpdate.java
 * 
 * Created on Jun 19, 2004
 *
 *  Copyright (C) 2004 - CIRG@UP 
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

import java.util.Iterator;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.controlparameterupdatestrategies.ConstantUpdateStrategy;
import net.sourceforge.cilib.controlparameterupdatestrategies.ControlParameterUpdateStrategy;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.Vector;

/**
 * @author engel
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class FIPSVelocityUpdate implements VelocityUpdateStrategy {
	private static final long serialVersionUID = -601384236026398105L;
	
	private ControlParameterUpdateStrategy randomComponent;

	/**
	 * 
	 */
	public FIPSVelocityUpdate() {
		randomComponent = new ConstantUpdateStrategy();
		randomComponent.setParameter(1.0);
	}
	
	
	public FIPSVelocityUpdate(FIPSVelocityUpdate copy) {
		this();
		randomComponent.setParameter(copy.randomComponent.getParameter());
	}
	
	
	public FIPSVelocityUpdate clone() {
		return new FIPSVelocityUpdate(this);
	}
	
	
	/**
	 * @return Returns the randomComponent.
	 */
	public ControlParameterUpdateStrategy getRandomComponent() {
		return randomComponent;
	}

	
	/**
	 * @param randomComponent The randomComponent to set.
	 */
	public void setRandomComponent(ControlParameterUpdateStrategy randomComponent) {
		this.randomComponent = randomComponent;
	}

	
	/**
	 * @see net.sourceforge.cilib.pso.VelocityUpdate#updateVelocity(net.sourceforge.cilib.pso.Particle)
	 */
	public void updateVelocity(Particle particle) {
		Vector velocity = (Vector) particle.getVelocity();
		Vector position = (Vector) particle.getPosition();
		
	   Topology<Particle> topology = ((PSO) Algorithm.get()).getTopology();
	   
	   Iterator<Particle> k;
	   for (k = topology.iterator(); k.hasNext(); ) {
	   	   Particle target = k.next();
	   	   if (target.getId() == particle.getId()) {
	   	   	   break;
	   	   }
	   } 
		
	   for (int i = 0; i < particle.getDimension(); ++i) {
		   double informationSum = 0.0;
		   int numberOfNeighbours = 0;
				 
		   for (Iterator<Particle> j = topology.neighbourhood(k); j.hasNext(); ) {
			   Particle neighbourParticle = j.next();
			   numberOfNeighbours++;
			  
			   Vector nBestPosition = (Vector) neighbourParticle.getBestPosition();
			   informationSum += randomComponent.getParameter() * (nBestPosition.getReal(i)-position.getReal(i));
		   }
			 
		   double result = 0.729 * velocity.getReal(i)+informationSum/numberOfNeighbours;
		   velocity.setReal(i, result);
			 
		   // TODO: Constriction should be added as a VMax Strategy. Add VMax strategies
		   //clamp(velocity., i);
		}
	}

	
	/**
	 * 
	 */
	public void updateControlParameters() {
		// TODO Auto-generated method stub
		
	}

		
	
}
