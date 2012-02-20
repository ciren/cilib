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
import net.sourceforge.cilib.pso.particle.ParametizedParticle;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * TODO: Complete this javadoc.
 */
public class GaussianPositionProvider implements PositionProvider {

    private static final long serialVersionUID = 1888395118987536803L;

    /*
     * {@inheritDoc}
     */
    @Override
    public GaussianPositionProvider getClone() {
        throw new UnsupportedOperationException("Implementation is required.");
    }

    /*
     * {@inheritDoc}
     */
    @Override
    public Vector get(Particle particle) {
        throw new UnsupportedOperationException("Implementation is required.");
    }
    
    /*
     * {@inheritDoc}
     */
    @Override
    public double getInertia(ParametizedParticle particle) {
        throw new UnsupportedOperationException("Implementation is required.");
    }
    
    /*
     * {@inheritDoc}
     */
    @Override
    public double getSocialAcceleration(ParametizedParticle particle) {
        throw new UnsupportedOperationException("Implementation is required.");
    }
    
    /*
     * {@inheritDoc}
     */
    @Override
    public double getCognitiveAcceleration(ParametizedParticle particle) {
        throw new UnsupportedOperationException("Implementation is required.");
    }
    
    /*
     * {@inheritDoc}
     */
    @Override
    public double getVmax(ParametizedParticle particle) {
        throw new UnsupportedOperationException("Implementation is required.");
    }
}
