package net.sourceforge.cilib.moo.archive;

import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.Vector;

/**
 * @author Andries Engelbrecht
 */
public class DominatesStrategy implements LocalGuideStrategy {

	private Vector localGuide = null;
	
	/* Must newLocalGuide be cloned??*/
	public void setLocalGuide(Vector newLocalGuide) {
		this.localGuide = newLocalGuide;
	}
	
	public Vector getLocalGuide() {
		return this.localGuide;
	}
	
	/*rather Entity than Particle*/
	public void updateLocalGuide(Particle particle) {
		/*only of position dominates personalBestPosition, then 
		 * the new personal best position becomes the current position
		 */
		if (particle.getFitness().compareTo(particle.getBestFitness()) == 0)
			 this.localGuide = (Vector) particle.getPosition().clone();
	}

}
