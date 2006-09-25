package net.sourceforge.cilib.pso.particle.initialisation;

import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.Vector;

public class ZeroInitialVelocityStrategy implements
		VelocityInitialisationStrategy {

	public void initialise(Particle particle) {
		Vector velocity = (Vector) particle.getVelocity();
		
		velocity.reset();
		
		System.out.println("velocity was reset");
		
	}

}
