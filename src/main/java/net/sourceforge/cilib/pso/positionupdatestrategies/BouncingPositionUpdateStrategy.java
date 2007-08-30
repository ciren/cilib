package net.sourceforge.cilib.pso.positionupdatestrategies;

import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Instead of making use of boundary constraints that reinitialise entire Particles (or components
 * thereof), this class is a proactive approach to prevent the Particles from moving outside of the
 * domain. Before moving a Particle to it's new location, this position update strategy first checks
 * to see whether the Particle will be outside of the domain. If not, the Particle is moved. If one
 * of the components of a Particle will be outside of the domain, it is placed very close to the
 * boundary of the domain (but still inside) and the Particle's velocity for that component is
 * inverted (multiplied by -1), effectively making the Particle bounce of the sides of the domain.
 * @author Theuns Cloete
 */
public class BouncingPositionUpdateStrategy implements PositionUpdateStrategy {
	private static final long serialVersionUID = -2085380951650975909L;

	public BouncingPositionUpdateStrategy() {
	}

	public BouncingPositionUpdateStrategy(BouncingPositionUpdateStrategy rhs) {
	}

	public BouncingPositionUpdateStrategy clone() {
		return new BouncingPositionUpdateStrategy(this);
	}

	/*
	 * (non-Javadoc)
	 * @see net.sourceforge.cilib.pso.positionupdatestrategies.PositionUpdateStrategy#updatePosition(net.sourceforge.cilib.entity.Particle)
	 */
	public void updatePosition(Particle particle) {
		Vector position = (Vector) particle.getPosition();
		Vector velocity = (Vector) particle.getVelocity();

		for (int i = 0; i < position.getDimension(); ++i) {
			Numeric position_i = position.getNumeric(i);
			Numeric velocity_i = velocity.getNumeric(i);
			double upper = position_i.getUpperBound();
			double lower = position_i.getLowerBound();
			double value = position_i.getReal() + velocity_i.getReal();
			double onePercent = (upper - lower) / 100.0;

			if (value < lower) {
				position_i.setReal(lower + onePercent);
				velocity_i.setReal(velocity_i.getReal() * -1);
			}
			else if (value >= upper) {
				position_i.setReal(upper - onePercent);
				velocity_i.setReal(velocity_i.getReal() * -1);
			}
			else {
				position_i.setReal(value);
			}
		}
	}
}
