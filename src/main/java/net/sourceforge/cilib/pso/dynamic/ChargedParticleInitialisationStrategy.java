package net.sourceforge.cilib.pso.dynamic;

import net.sourceforge.cilib.util.Cloneable;


/**
 * Interface for ChargedParticle intialisation
 * 
 * @author Anna Rakitianskaia
 */
public interface ChargedParticleInitialisationStrategy extends Cloneable {	
	/**
	 * Clone the ChargedParticleInitialisationStrategy 
	 * @return A copy of the ChargedParticleInitialisationStrategy
	 */
	public ChargedParticleInitialisationStrategy getClone();
	
	/**
	 * Initialise the Particle charge
	 * @param particle The particle to initialise. The particle must be of type ChargedParticle.
	 */
	public void initialise(ChargedParticle particle);
}
