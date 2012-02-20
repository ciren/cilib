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
import net.sourceforge.cilib.pso.particle.ParametizedParticle;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Binary position update strategy to enable the BinaryPSO.
 *
 */
public class BinaryPositionProvider implements PositionProvider {

    private static final long serialVersionUID = -2136786203855125909L;
    private Sigmoid sigmoid;

    /**
     * Create an instance of {@linkplain BinaryPositionProvider}.
     */
    public BinaryPositionProvider() {
        this.sigmoid = new Sigmoid();
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public BinaryPositionProvider(BinaryPositionProvider copy) {
        this.sigmoid = copy.sigmoid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BinaryPositionProvider getClone() {
        return new BinaryPositionProvider(this);
    }

    /**
     * BinaryPSO particle position update, as defined by Kennedy and Eberhart.
     */
    @Override
    public Vector get(Particle particle) {
        Vector velocity = (Vector) particle.getVelocity();
        Vector.Builder builder = Vector.newBuilder();
        for (int i = 0; i < particle.getDimension(); i++) {
            double result = this.sigmoid.apply(velocity.doubleValueOf(i));
            double rand = Math.random();

            if (rand < result) {
                builder.add(true);
            } else {
                builder.add(false);
            }
        }
        return builder.build();
    }
    
    /*
     * Not applicable
     */
    @Override
    public double getInertia(ParametizedParticle particle) {
        throw new UnsupportedOperationException("Not applicable");
    }
    
    /*
     * Not applicable
     */
    @Override
    public double getSocialAcceleration(ParametizedParticle particle) {
        throw new UnsupportedOperationException("Not applicable");
    }
    
    /*
     * Not applicable
     */
    @Override
    public double getCognitiveAcceleration(ParametizedParticle particle) {
        throw new UnsupportedOperationException("Not applicable");
    }
    
    /*
     * Not applicable
     */
    @Override
    public double getVmax(ParametizedParticle particle) {
        throw new UnsupportedOperationException("Not applicable");
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
