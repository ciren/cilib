package net.sourceforge.cilib.pso.positionupdatestrategies;

import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Standard Position Update Strategy with boundary constraint handling. If a particle oversteps the
 * upper boundary it gets re-initialised and placed near the lower boundary and vice versa.
 * References:
 * @inproceedings{ZXB04, author = "W.-J. Zhang and X.-F. Xie and D.-C. Bi", title = "Handling
 *                       boundary constraints for numerical optimization by particle swarm flying in
 *                       periodic search space", booktitle = "IEEE Congress on Evolutionary
 *                       Computation", month = jun, year = {2004}, volume = "2", pages =
 *                       {2307--2311} }
 * @author Wiehann Matthysen
 */
public class PeriodicModePositionUpdateStrategy implements PositionUpdateStrategy {
	private static final long serialVersionUID = -315555375434322617L;

	public PeriodicModePositionUpdateStrategy() {

	}

	public PeriodicModePositionUpdateStrategy(PeriodicModePositionUpdateStrategy copy) {

	}

	public PeriodicModePositionUpdateStrategy clone() {
		return new PeriodicModePositionUpdateStrategy(this);
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
			double range = Math.abs(upper - lower);
			double value = position_i.getReal() + velocity_i.getReal();

			if (value < lower) {
				position_i.setReal(upper - (lower - position_i.getReal()) % range);
			}
			else if (value >= upper) {
				position_i.setReal(lower + (position_i.getReal() - upper) % range);
			}
			else {
				position_i.setReal(value);
			}
		}
	}
}
