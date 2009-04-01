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

import java.io.Serializable;

import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.util.Cloneable;

/**
 * This interface defines two methods that will ensure that a {@linkplain Particle} does not overstep
 * it's boundaries: {@linkplain #constrainLower(Numeric, Numeric)} and
 * {@linkplain #constrainUpper(Numeric, Numeric)}.
 * @author Wiehann Matthysen
 */
public interface BoundaryConstraintStrategy extends Cloneable, Serializable {
    /**
     * The term <i>infimum</i> refers to the <i>greatest lower bound</i> in mathematics. This is the value
     * that should be subtracted from the upperbound if needed.
     */
    public final double INFIMUM = 0.000000000000001;

    /**
     * Clone the stategy by creating a new object with the same contents and values as the current
     * object.
     * @return A clone of the current <tt>BoundaryConstraintStrategy</tt>
     */
    public BoundaryConstraintStrategy getClone();

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
