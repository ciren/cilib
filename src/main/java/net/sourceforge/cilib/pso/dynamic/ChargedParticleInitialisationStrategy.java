package net.sourceforge.cilib.pso.dynamic;


/**
 * Interface for ChargedParticle intialisation
 * 
 * @author Anna Rakitianskaia
 */
public interface ChargedParticleInitialisationStrategy {	
	/**
	 * Clone the ChargedParticleInitialisationStrategy 
	 * @return A copy of the ChargedParticleInitialisationStrategy
	 */
	public ChargedParticleInitialisationStrategy clone();
	
	/**
	 * Initialise the Particle charge
	 * @param particle The particle to initialise. The particle must be of type ChargedParticle.
	 */
	public void initialise(ChargedParticle particle);
}
