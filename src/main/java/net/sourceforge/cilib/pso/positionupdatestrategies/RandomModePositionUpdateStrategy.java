package net.sourceforge.cilib.pso.positionupdatestrategies;

import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Standard Position Update Strategy with boundary constraint handling. If a particle oversteps the
 * boundary it gets randomly re-initialised within the boundary and its velocity gets updated.
 * References:
 * @inproceedings{ZXB04, author = "W.-J. Zhang and X.-F. Xie and D.-C. Bi", title = "Handling
 *                       boundary constraints for numerical optimization by particle swarm flying in
 *                       periodic search space", booktitle = "IEEE Congress on Evolutionary
 *                       Computation", month = jun, year = {2004}, volume = "2", pages =
 *                       {2307--2311} }
 * @inproceedings{HW07, author = "S. Helwig and R. Wanka", title = "Particle Swarm Optimization in
 *                      High-Dimensional Bounded Search Spaces", booktitle = "Proceedings of the
 *                      2007 IEEE Swarm Intelligence Symposium", month = apr, year = {2007}, pages =
 *                      {198--205} }
 * @author Wiehann Matthysen
 */
public class RandomModePositionUpdateStrategy implements PositionUpdateStrategy {
	private static final long serialVersionUID = -9051924173885281668L;

	public RandomModePositionUpdateStrategy() {

	}

	public RandomModePositionUpdateStrategy(RandomModePositionUpdateStrategy copy) {

	}

	public RandomModePositionUpdateStrategy clone() {
		return new RandomModePositionUpdateStrategy(this);
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

			if (value < lower || value >= upper) {
				Numeric previousPosition = position_i.clone();
				position_i.randomise();
				velocity_i.setReal(position_i.getReal() - previousPosition.getReal());
			}
			else {
				position_i.setReal(value);
			}
		}
	}
}
