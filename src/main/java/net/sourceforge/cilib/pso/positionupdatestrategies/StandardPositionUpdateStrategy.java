/**
 * Copyright (C) 2003 - 2009
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
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This is the normal position update as described by Kennedy and Eberhart.
 * @reference paper
 *
 * @author Gary Pampara
 *
 */
public class StandardPositionUpdateStrategy implements PositionUpdateStrategy {
    private static final long serialVersionUID = 5547754413670196513L;

    /**
     * Create an new instance of {@code StandardPositionUpdateStrategy}.
     */
    public StandardPositionUpdateStrategy() {

    }

    /**
     * Copy constructor. Copy the provided instance.
     * @param copy The instance to copy.
     */
    public StandardPositionUpdateStrategy(StandardPositionUpdateStrategy copy) {

    }

    /**
     * {@inheritDoc}
     */
    public StandardPositionUpdateStrategy getClone() {
        return new StandardPositionUpdateStrategy(this);
    }

    /**
     * {@inheritDoc}
     */
    public void updatePosition(Particle particle) {
        Vector position = (Vector) particle.getPosition();
        Vector velocity = (Vector) particle.getVelocity();

        for (int i = 0; i < position.getDimension(); ++i) {
            double value = position.getReal(i);
            value += velocity.getReal(i);
            position.setReal(i, value);
        }

    }

}
