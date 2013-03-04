/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem.boundaryconstraint;

import java.util.Iterator;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * <p>
 * If a <b>particle</b> oversteps the boundary it gets re-initialised and placed on the overstepped
 * boundary. A turbulence probability gets specified to allow particles to escape the boundaries.
 * </p>
 * <p>
 * References:
 * </p>
 * <pre>
 * {@literal @}inproceedings{ZXB04, author = "W.-J. Zhang and X.-F. Xie and D.-C. Bi",
 *                 title = "Handling boundary constraints for numerical optimization by
 *                 particle swarm flying in periodic search space",
 *                 booktitle = "IEEE Congress on Evolutionary Computation", month = jun,
 *                 year = {2004}, volume = "2", pages = {2307--2311} }
 * {@literal @}inproceedings{HW07, author = "S. Helwig and R. Wanka",
 *                 title = "Particle Swarm Optimization in High-Dimensional Bounded Search Spaces",
 *                 booktitle = "Proceedings of the 2007 IEEE Swarm Intelligence Symposium", month = apr,
 *                 year = {2007}, pages = {198--205} }
 * </pre>
 *
 */
public class NearestBoundaryConstraint implements BoundaryConstraint {

    private static final long serialVersionUID = 3177150919194273857L;
    private ControlParameter turbulenceProbability;
    private ProbabilityDistributionFunction random;

    /**
     * Create an instance of the constraint with a turbulence probability
     * initially set to 0.0.
     */
    public NearestBoundaryConstraint() {
        turbulenceProbability = ConstantControlParameter.of(0.0);
        this.random = new UniformDistribution();
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public NearestBoundaryConstraint(NearestBoundaryConstraint copy) {
        this.turbulenceProbability = copy.turbulenceProbability.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BoundaryConstraint getClone() {
        return new NearestBoundaryConstraint(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enforce(Entity entity) {
        StructuredType<?> s = (StructuredType<?>) entity.getProperties().get(EntityType.Particle.VELOCITY);

        if (s == null) {
            throw new UnsupportedOperationException("Cannot perform this boundary constrain on a "
                    + entity.getClass().getSimpleName());
        }

        Vector.Builder positionBuilder = Vector.newBuilder();
        Vector.Builder velocityBuilder = Vector.newBuilder();

        Iterator<?> pIterator = entity.getCandidateSolution().iterator();
        Iterator<?> vIterator = s.iterator();

        while (pIterator.hasNext()) {
            Numeric position = (Numeric) pIterator.next();
            Numeric velocity = (Numeric) vIterator.next();
            Bounds bounds = position.getBounds();

            double previousPosition = position.doubleValue();

            if (Double.compare(position.doubleValue(), bounds.getLowerBound()) < 0) {
                if (random.getRandomNumber() < turbulenceProbability.getParameter()) {
                    positionBuilder.add(position.doubleValue() + random.getRandomNumber() * bounds.getRange());
                } else {
                    positionBuilder.add(bounds.getLowerBound());    // lower boundary is inclusive
                }
                velocityBuilder.add(position.doubleValue() - previousPosition);
            } else if (Double.compare(position.doubleValue(), bounds.getUpperBound()) > 0) {
                if (random.getRandomNumber() < turbulenceProbability.getParameter()) {
                    positionBuilder.add(position.doubleValue() - random.getRandomNumber() * bounds.getRange());
                } else {
                    positionBuilder.add(bounds.getUpperBound() - Maths.EPSILON);    // upper boundary is exclusive
                }
                velocityBuilder.add(position.doubleValue() - previousPosition);
            } else {
                positionBuilder.add(position);
                velocityBuilder.add(velocity);
            }
        }
        entity.getProperties().put(EntityType.CANDIDATE_SOLUTION, positionBuilder.build());
        entity.getProperties().put(EntityType.Particle.VELOCITY, velocityBuilder.build());
    }

    /**
     * Get the {@linkplain ControlParameter} representing the current turbulence probability.
     * @return The turbulence {@linkplain ControlParameter}.
     */
    public ControlParameter getTurbulenceProbability() {
        return turbulenceProbability;
    }

    /**
     * Set the turbulence probability with the provided {@linkplain ControlParameter}.
     * @param turbulenceProbability The value to set.
     */
    public void setTurbulenceProbability(ControlParameter turbulenceProbability) {
        this.turbulenceProbability = turbulenceProbability;
    }
}
