/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.pso.positionupdatestrategies;

import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * @author Gary Pampara
 * @author Andries Engelbrecht
 * @deprecated Refer to {@linkplain net.sourceforge.cilib.pso.positionupdatestrategies.LinearPositionUpdateStrategy the replacing class}.
 *
 */
@Deprecated
public class BareBonesPositionUpdateStrategy implements PositionUpdateStrategy {
    private static final long serialVersionUID = -6562797054222387899L;


    /**
     * Default construstor.
     */
    public BareBonesPositionUpdateStrategy() {

    }


    /**
     * Copy Constructor for the <tt>BareBonesPositionUpdateStrategy</tt>.
     * @param copy The <tt>BareBonesPositionUpdateStrategy</tt> to copy
     */
    public BareBonesPositionUpdateStrategy(BareBonesPositionUpdateStrategy copy) {

    }


    /**
     * Create a clone of this <tt>BareBonesPositionUpdateStrategy</tt>.
     *
     * @return A <tt>BareBonesPositionUpdateStrategy</tt> object which is a clone of the
     *         object whose <code>clone</code> was called.
     */
    public BareBonesPositionUpdateStrategy getClone() {
        return new BareBonesPositionUpdateStrategy(this);
    }


    /**
     * {@inheritDoc}
     */
    public void updatePosition(Particle particle) {
        Vector position = (Vector) particle.getPosition();
        Vector velocity = (Vector) particle.getVelocity();

        for (int i = 0; i < position.size(); i++) {
            position.setReal(i, velocity.doubleValueOf(i));
        }

    }

}
