package net.sourceforge.cilib.pso.dynamic;


/**
 * @author Anna Rakitianskaia
 *
 */
public class StandardChargedParticleInitialisationStrategy implements
		ChargedParticleInitialisationStrategy {
	
	private double chargedRatio; // determines the percentage of the swarm that is to be charged
	private double chargeMagnitude; // charge magnitude
	private static int populationSize;
	private static int chargedCounter = 0; // maybe a bad idea, but this variable keeps track of the number of particles already charged
	
	public StandardChargedParticleInitialisationStrategy() {
		// defaults:
		chargedRatio = 0.5;	// one half of the swarm is charged => Atomic swarm
		chargeMagnitude = 16; // the obscure value 16 comes from the article where the chraged PSO was analysed for the 1st time by its creators
	}
	
	public StandardChargedParticleInitialisationStrategy(StandardChargedParticleInitialisationStrategy copy) {
		this.chargedRatio = copy.chargedRatio;
		this.chargeMagnitude = copy.chargeMagnitude;
	}
		
	public StandardChargedParticleInitialisationStrategy clone() {
		return new StandardChargedParticleInitialisationStrategy(this);
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.pso.particle.initialisation.ChargedParticleInitialisationStrategy#initialise(net.sourceforge.cilib.pso.particle.ChargedParticle)
	 */
	public void initialise(ChargedParticle particle) {
		if(chargedCounter < Math.floor(populationSize*chargedRatio)) {
			particle.setCharge(chargeMagnitude);
			++chargedCounter;
		} else {
			particle.setCharge(0);
		}			
	}


	/**
	 * @return the chargedRatio
	 */
	public double getChargedRatio() {
		return chargedRatio;
	}


	/**
	 * @param chargedRatio the chargedRatio to set
	 */
	public void setChargedRatio(double chargedRatio) {
		this.chargedRatio = chargedRatio;
	}

	/**
	 * @return the chargeMagnitude
	 */
	public double getChargeMagnitude() {
		return chargeMagnitude;
	}

	/**
	 * @param chargeMagnitude the chargeMagnitude to set
	 */
	public void setChargeMagnitude(double chargeMagnitude) {
		this.chargeMagnitude = chargeMagnitude;
	}

	/**
	 * @param populationSize the populationSize to set
	 */
	public void setPopulationSize(int populationSize) {
		StandardChargedParticleInitialisationStrategy.populationSize = populationSize;
	}

}
