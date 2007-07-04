package net.sourceforge.cilib.pso.positionupdatestrategies;

import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Vector;

public class BouncingPositionUpdateStrategy implements PositionUpdateStrategy {
	private static final long serialVersionUID = -2085380951650975909L;

	public BouncingPositionUpdateStrategy() {
	}

	public BouncingPositionUpdateStrategy(BouncingPositionUpdateStrategy rhs) {
	}

	public BouncingPositionUpdateStrategy clone() {
		return new BouncingPositionUpdateStrategy(this);
	}

	public void updatePosition(Particle particle) {
		Vector position = (Vector) particle.getPosition();
		Vector velocity = (Vector) particle.getVelocity();
		for (int i = 0; i < position.getDimension(); ++i) {
			Numeric pos_i = position.getNumeric(i);
			Numeric vel_i = velocity.getNumeric(i);
			double upper = pos_i.getUpperBound();
			double lower = pos_i.getLowerBound();
			double value = pos_i.getReal() + vel_i.getReal();
			double onePercent = (upper - lower) / 100.0;
			if (value >= upper) {
				pos_i.setReal(upper - onePercent);
				vel_i.setReal(vel_i.getReal() * -1);
//				System.out.println("Reflected from upper: " + pos_i.getReal());
			}
			else if (value < lower) {
				pos_i.setReal(lower + onePercent);
				vel_i.setReal(vel_i.getReal() * -1);
//				System.out.println("Reflected from lower: " + pos_i.getReal());
			}
			else {
				pos_i.setReal(value);
			}
		}
	}
}
