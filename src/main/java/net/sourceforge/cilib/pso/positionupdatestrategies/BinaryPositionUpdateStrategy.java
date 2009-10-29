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
import net.sourceforge.cilib.functions.activation.Sigmoid;
import net.sourceforge.cilib.type.types.container.Vector;


/**
 * Binary position update strategy to enable the BinaryPSO.
 *
 * @author Gary Pampara
 */
public class BinaryPositionUpdateStrategy implements PositionUpdateStrategy {
    private static final long serialVersionUID = -2136786203855125909L;
    private Sigmoid sigmoid;

    /**
     * Create an instance of {@linkplain BinaryPositionUpdateStrategy}.
     */
    public BinaryPositionUpdateStrategy() {
        this.sigmoid = new Sigmoid();
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public BinaryPositionUpdateStrategy(BinaryPositionUpdateStrategy copy) {
        this.sigmoid = copy.sigmoid.getClone();
    }

    /**
     * {@inheritDoc}
     */
    public BinaryPositionUpdateStrategy getClone() {
        return new BinaryPositionUpdateStrategy(this);
    }

    /**
     * BinaryPSO particle position update, as defined by Kennedy and Eberhart.
     */
    public void updatePosition(Particle particle) {
        Vector position = (Vector) particle.getPosition();
        Vector velocity = (Vector) particle.getVelocity();

        for (int i = 0; i < position.getDimension(); i++) {
            double result = sigmoid.evaluate(velocity.getReal(i));
            double rand = Math.random();

            if (rand < result) {
                position.setBit(i, true);
            }
            else {
                position.setBit(i, false);
            }
        }
    }

    /**
     * Get the sigmoid function used within the update strategy.
     * @return The {@linkplain Sigmoid} function used.
     */
    public Sigmoid getSigmoid() {
        return sigmoid;
    }

    /**
     * Set the sigmoid function to use.
     * @param sigmoid The function to set.
     */
    public void setSigmoid(Sigmoid sigmoid) {
        this.sigmoid = sigmoid;
    }

}
