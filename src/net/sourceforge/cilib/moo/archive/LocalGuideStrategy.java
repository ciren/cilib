package net.sourceforge.cilib.moo.archive;

import net.sourceforge.cilib.type.types.Vector;
import net.sourceforge.cilib.controlparameterupdatestrategies.ControlParameterUpdateStrategy;
import net.sourceforge.cilib.pso.particle.Particle;

/**
 * @author Andries Engelbrecht
 */
public interface LocalGuideStrategy {
	
	public Vector getLocalGuide();
	
	public void setLocalGuide(Vector newLocalGuide);
	
	public void updateLocalGuide(Particle particle);
}



