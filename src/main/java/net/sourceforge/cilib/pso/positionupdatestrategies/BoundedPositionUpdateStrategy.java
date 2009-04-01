/*
 * Copyright (C) 2003 - 2008
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.pso.positionupdatestrategies;

import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.pso.positionupdatestrategies.boundaryconstraintstrategies.BoundaryConstraintStrategy;
import net.sourceforge.cilib.pso.positionupdatestrategies.boundaryconstraintstrategies.ClampingBoundaryConstraintStrategy;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Standard Position Update Strategy with boundary constraint handling. The logic that determines if
 * and when a {@linkplain Particle} steps over it's boundaries is implemented in the
 * {@link #updatePosition(net.sourceforge.cilib.entity.Particle)} method. The desired effect is
 * achieved by specifying a {@linkplain BoundaryConstraintStrategy} using the
 * {@link #setBoundaryConstraintStrategy(BoundaryConstraintStrategy)} method. Because the
 * "overstepping logic" is handled in the above mentioned method, the specific
 * {@linkplain BoundaryConstraintStrategy} should implement two methods:
 * {@linkplain BoundaryConstraintStrategy#constrainLower(Numeric, Numeric)} and
 * {@linkplain BoundaryConstraintStrategy#constrainUpper(Numeric, Numeric)}.
 * @author Wiehann Matthysen
 */
public class BoundedPositionUpdateStrategy implements PositionUpdateStrategy {
    private static final long serialVersionUID = -1323696017002776467L;

    private BoundaryConstraintStrategy boundaryConstraintStrategy;

    public BoundedPositionUpdateStrategy() {
        boundaryConstraintStrategy = new ClampingBoundaryConstraintStrategy();
    }

    public BoundedPositionUpdateStrategy(BoundedPositionUpdateStrategy copy) {
        boundaryConstraintStrategy = copy.boundaryConstraintStrategy.getClone();
    }

    public BoundedPositionUpdateStrategy getClone() {
        return new BoundedPositionUpdateStrategy(this);
    }

    public void setBoundaryConstraintStrategy(BoundaryConstraintStrategy boundaryConstraintStrategy) {
        this.boundaryConstraintStrategy = boundaryConstraintStrategy;
    }

    public BoundaryConstraintStrategy getBoundaryConstraintStrategy() {
        return boundaryConstraintStrategy;
    }

    /**
     * Update the position of the {@linkplain Particle} so that it always stays within the domain
     * boundaries. To achieve this, the specified {@linkplain #boundaryConstraintStrategy} should
     * implement the {@linkplain BoundaryConstraintStrategy#constrainLower(Numeric, Numeric)} and
     * {@linkplain BoundaryConstraintStrategy#constrainUpper(Numeric, Numeric)} methods.
     * @param particle The {@linkplain Particle} to perform the position update on which is not
     *        allowed to move outside the domain.
     */
    public void updatePosition(Particle particle) {
        Vector position = (Vector) particle.getPosition();
        Vector velocity = (Vector) particle.getVelocity();

        for (int i = 0; i < position.getDimension(); ++i) {
            Numeric currentPosition = position.getNumeric(i);
            Numeric currentVelocity = velocity.getNumeric(i);
            double newPosition = position.getReal(i);
            newPosition += velocity.getReal(i);

            if (newPosition < currentPosition.getBounds().getLowerBound()) { // crossed the lower boundary
                boundaryConstraintStrategy.constrainLower(currentPosition, currentVelocity);
            }
            else if (newPosition >= currentPosition.getBounds().getUpperBound()) { // crossed the upper boundary
                boundaryConstraintStrategy.constrainUpper(currentPosition, currentVelocity);
            }
            else { // did not cross any boundary; update as normal
                position.setReal(i, newPosition);
            }
        }
    }
}
