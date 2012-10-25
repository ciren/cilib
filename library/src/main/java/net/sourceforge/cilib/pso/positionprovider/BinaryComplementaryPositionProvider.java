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
package net.sourceforge.cilib.pso.positionprovider;

import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.functions.activation.Sigmoid;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Implementation of A novel Binary Particle Swarm Optimization.
 * Particle position is updated using complementary bits
 * <p>
 * References:
 * </p>
 * <ul><li>
 * Mojtabe Ahmadieh Khanesar, Mohammed Teshnehlab and Mahdi Aliyari Shoorehdeli.,
 * "A Novel Binary Particle Swarm Optimization" (2007). Proceedings of the 15th
 * Mediterranear Conference on Control and Automation.
 * </li></ul>
 */
public class BinaryComplementaryPositionProvider implements PositionProvider {

    private Sigmoid sigmoid;

    /**
     * Create an instance of {@linkplain BinaryComplementaryPositionProvider}.
     */
    public BinaryComplementaryPositionProvider() {
        this.sigmoid = new Sigmoid();
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public BinaryComplementaryPositionProvider(BinaryComplementaryPositionProvider copy) {
        this.sigmoid = copy.sigmoid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BinaryComplementaryPositionProvider getClone() {
        return new BinaryComplementaryPositionProvider(this);
    }

    /**
     * A 2's complement BinaryPSO particle position update.
     */
    @Override
    public Vector get(Particle particle) {
        Vector velocity = (Vector) particle.getVelocity();
        Vector position = (Vector) particle.getPosition();
        
        Vector.Builder builder = Vector.newBuilder();
        for (int i = 0; i < particle.getDimension(); i++) {
            double result = this.sigmoid.apply(velocity.doubleValueOf(i));
            double rand = Math.random();

            // 2's complement update strategy
            if (rand < result) {
                builder.addWithin(1 - position.doubleValueOf(i), position.boundsOf(i));
            } else {
                builder.addWithin(position.doubleValueOf(i), position.boundsOf(i));
            }
        }
        return builder.build();
    }

    /**
     * Get the sigmoid function used within the update strategy.
     * @return The {@linkplain Sigmoid} function used.
     */
    public Sigmoid getSigmoid() {
        return this.sigmoid;
    }

    /**
     * Set the sigmoid function to use.
     * @param sigmoid The function to set.
     */
    public void setSigmoid(Sigmoid sigmoid) {
        this.sigmoid = sigmoid;
    }
}
