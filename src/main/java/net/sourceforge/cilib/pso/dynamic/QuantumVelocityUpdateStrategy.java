package net.sourceforge.cilib.pso.dynamic;

import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.pso.velocityupdatestrategies.StandardVelocityUpdate;

/**
 * Velocity update strategy for QSO (Quantum PSO). Implemented according
 * to paper by Blackwell and Branke, "Multiswarms, Exclusion, and Anti-
 * Convergence in Dynamic Environments."
 * 
 * @author Anna Rakitianskaia
 *
 */
public class QuantumVelocityUpdateStrategy extends StandardVelocityUpdate {

	private static final long serialVersionUID = -940568473388702506L;
	private static final double epsilon = 0.000000001;

	/**
	 * Constructor
	 */
	public QuantumVelocityUpdateStrategy() {
		super();
	}

	/**
	 * Copy Constructor
	 * @param copy
	 */
	public QuantumVelocityUpdateStrategy(StandardVelocityUpdate copy) {
		super(copy);
	}
	
	/**
	 * Update particle velocity; do it in a standard way if the particle is neutral, and 
	 * do not update it if the particle is quantum (charged), since quantum particles do
	 * not use the velocity to update their positions.
	 * @param particle the particle to update position of
	 */
	public void updateVelocity(Particle particle) {
		ChargedParticle checkChargeParticle = (ChargedParticle) particle;
		if(checkChargeParticle.getCharge() < epsilon) {	// the particle is neutral
			super.updateVelocity(particle);
		}
    }
}
