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
package net.sourceforge.cilib.pso.positionupdatestrategies.boundaryconstraintstrategies;

import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.TypeUtil;

/**
 * If a particle oversteps the boundary it gets randomly re-initialised within the boundary and its
 * velocity gets updated. References:
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
public class RandomBoundaryConstraintStrategy implements BoundaryConstraintStrategy {
    private static final long serialVersionUID = -4049333767309340874L;

    /**
     * Create an instance of {@linkplain RandomBoundaryConstraintStrategy}.
     */
    public RandomBoundaryConstraintStrategy() {
    }

    /**
     * Copy constructor. Create a copy of the provided {@linkplain RandomBoundaryConstraintStrategy}
     * instance.
     * @param copy The instance to copy.
     */
    public RandomBoundaryConstraintStrategy(RandomBoundaryConstraintStrategy copy) {
    }

    /**
     * {@inheritDoc}
     */
    public RandomBoundaryConstraintStrategy getClone() {
        return new RandomBoundaryConstraintStrategy(this);
    }

    /**
     * {@inheritDoc}
     */
    public void constrainLower(Numeric position, Numeric velocity) {
        constrain(position, velocity);
    }

    /**
     * {@inheritDoc}
     */
    public void constrainUpper(Numeric position, Numeric velocity) {
        constrain(position, velocity);
    }

    /**
     * Constrain the position.
     * @param position The {@linkplain Numeric} representing the position.
     * @param velocity The {@linkplain Numeric} representing the velocity.
     */
    private void constrain(Numeric position, Numeric velocity) {
        Numeric previousPosition = position.getClone();
        TypeUtil.randomize(position);
        velocity.set(position.getReal() - previousPosition.getReal());
    }
}
