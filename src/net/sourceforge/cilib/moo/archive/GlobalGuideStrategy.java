package net.sourceforge.cilib.moo.archive;

import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.Vector;

/**
 * 
 * @author Andries Engelbrecht
 *
 */

public interface GlobalGuideStrategy {

	public Vector getGlobalGuide();
	
	public void setGlobalGuide(Vector newGlobalGuide);
	
	public void updateGlobalGuide(Particle particle);
}
