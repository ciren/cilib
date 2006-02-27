/*
 * Created on Jun 19, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
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
