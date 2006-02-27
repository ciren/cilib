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
package net.sourceforge.cilib.PSO;

import java.util.Iterator;

import net.sourceforge.cilib.Algorithm.Algorithm;

/**
 * @author engel
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class FIPSVelocityUpdate extends StandardVelocityUpdate {

	/**
	 * 
	 */
	public FIPSVelocityUpdate() {
		super();
		cognitiveComponent = null;
		socialComponent = new FIPSAcceleration();
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.PSO.VelocityUpdate#updateVelocity(net.sourceforge.cilib.PSO.Particle)
	 */
	public void updateVelocity(Particle particle) {
		double[] velocity = particle.getVelocity();
		double[] position = particle.getPosition();
		
	   Topology topology = ((PSO) Algorithm.get()).getTopology();
	   Iterator k = topology.particles();
	   while (k.hasNext()) {
	   	   Particle target = (Particle) k.next();
	   	   if (target.getId() == particle.getId()) {
	   	   	   break;
	   	   }
	   } 
		
		for (int i = 0; i < particle.getDimension(); ++i) {
			double informationSum = 0.0;
			int numberOfNeighbours = 0;
				 
			 for (Iterator j = topology.neighbourhood(k); j.hasNext(); ) {
			 	Particle neighbourParticle = (Particle) j.next();
			 	numberOfNeighbours++;
			    double [] nBestPosition = neighbourParticle.getBestPosition();
			    informationSum += socialComponent.get() * (nBestPosition[i]-position[i]); 
			 }
			 velocity[i] = inertiaComponent.get() * velocity[i] +
			                                      informationSum/numberOfNeighbours;
			 
			clamp(velocity, i);
		}
	}	

}
