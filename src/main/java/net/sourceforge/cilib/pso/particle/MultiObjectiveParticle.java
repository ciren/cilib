package net.sourceforge.cilib.pso.particle;

import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.Vector;

public class MultiObjectiveParticle extends StandardParticle {
	private static final long serialVersionUID = 2449622504036301616L;

	public void setBestPosition(Type bestPosition) {
		this.bestPosition = (Vector) bestPosition.clone();
		bestFitness = fitnessCalculator.getFitness(bestPosition, false);
	}

}
