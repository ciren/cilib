package net.sourceforge.cilib.pso.positionupdatestrategies.boundaryconstraintstrategies;

import java.io.Serializable;

import net.sourceforge.cilib.type.types.Numeric;

/**
 * This interface defines two methods that will ensure that a {@linkplain Particle} does not overstep
 * it's boundaries: {@linkplain #constrainLower(Numeric, Numeric)} and
 * {@linkplain #constrainUpper(Numeric, Numeric)}.
 * @author Wiehann Matthysen
 */
public interface BoundaryConstraintStrategy extends Serializable {

	/**
	 * Clone the stategy by creating a new object with the same contents and values as the current
	 * object.
	 * @return A clone of the current <tt>BoundaryConstraintStrategy</tt>
	 */
	public BoundaryConstraintStrategy clone();

	/**
	 * This method is called when the position of a particle has overstepped the lower boundary of
	 * the search space. It's responsible for updating the position to a new location within the
	 * search space boundaries. This might also involve modifying the velocity of the particle.
	 * @param position The position component of a particle that should be updated to some area
	 *        within the search space boundaries.
	 * @param velocity The velocity component of a particle that might also be affected by the
	 *        contraint operation.
	 */
	public void constrainLower(Numeric position, Numeric velocity);

	/**
	 * This method is called when the position of a particle has overstepped the upper boundary of
	 * the search space. It's responsible for updating the position to a new location within the
	 * search space boundaries. This might also involve modifying the velocity of the particle.
	 * @param position The position component of a particle that should be updated to some area
	 *        within the search space boundaries.
	 * @param velocity The velocity component of a particle that might also be affected by the
	 *        contraint operation.
	 */
	public void constrainUpper(Numeric position, Numeric velocity);
}
