package net.sourceforge.cilib.pso.positionupdatestrategies.boundaryconstraintstrategies;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.math.MathUtil;
import net.sourceforge.cilib.type.types.Numeric;

/**
 * If a particle oversteps the boundary it gets re-initialised and placed on the overstepped
 * boundary. A terbulence probability gets specified to allow particles to escape the boundaries.
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
public class NearestBoundaryConstraintStrategy implements BoundaryConstraintStrategy {
	private static final long serialVersionUID = 2444348297389576657L;

	private ControlParameter terbulenceProbability;

	public NearestBoundaryConstraintStrategy() {
		terbulenceProbability = new ConstantControlParameter();
		terbulenceProbability.setParameter(0.0);
	}

	public NearestBoundaryConstraintStrategy(NearestBoundaryConstraintStrategy copy) {
		terbulenceProbability = copy.terbulenceProbability.clone();
	}

	public NearestBoundaryConstraintStrategy clone() {
		return new NearestBoundaryConstraintStrategy(this);
	}

	public void setTerbulenceProbability(ControlParameter terbulenceProbability) {
		this.terbulenceProbability = terbulenceProbability;
	}

	public ControlParameter getTerbulenceProbability() {
		return terbulenceProbability;
	}

	/*
	 * (non-Javadoc)
	 * @see net.sourceforge.cilib.pso.positionupdatestrategies.boundaryconstraintstrategies.BoundaryConstraintStrategy#constrainLower(net.sourceforge.cilib.type.types.Numeric,
	 *      net.sourceforge.cilib.type.types.Numeric)
	 */
	public void constrainLower(Numeric position, Numeric velocity) {
		double upper = position.getUpperBound();
		double lower = position.getLowerBound();
		double range = Math.abs(upper - lower);
		Numeric previousPosition = position.clone();

		position.set(lower);	// lower boundary is inclusive
		if (MathUtil.random() < terbulenceProbability.getParameter()) {
			position.set(position.getReal() + MathUtil.random() * range);
		}

		velocity.set(position.getReal() - previousPosition.getReal());
	}

	/*
	 * (non-Javadoc)
	 * @see net.sourceforge.cilib.pso.positionupdatestrategies.boundaryconstraintstrategies.BoundaryConstraintStrategy#constrainUpper(net.sourceforge.cilib.type.types.Numeric,
	 *      net.sourceforge.cilib.type.types.Numeric)
	 */
	public void constrainUpper(Numeric position, Numeric velocity) {
		double upper = position.getUpperBound();
		double lower = position.getLowerBound();
		double range = Math.abs(upper - lower);
		Numeric previousPosition = position.clone();

		position.set(upper - INFIMUM);	// upper boundary is exclusive
		if (MathUtil.random() < terbulenceProbability.getParameter()) {
			position.set(position.getReal() - MathUtil.random() * range);
		}

		velocity.set(position.getReal() - previousPosition.getReal());
	}
}
