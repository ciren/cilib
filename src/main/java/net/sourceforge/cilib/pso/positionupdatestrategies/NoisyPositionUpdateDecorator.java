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

import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.math.random.GaussianDistribution;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFuction;

/**
 * Decorates a {@link PositionUpdateVisitor} or a {@link VelocityUpdateVisitor}
 * with random noise from any probability distribution function.
 *
 * @author Bennie Leonard
 */
public class NoisyPositionUpdateDecorator implements PositionUpdateStrategy {

    private ProbabilityDistributionFuction distribution;
    private PositionUpdateStrategy delegate;

    public NoisyPositionUpdateDecorator() {
        distribution = new GaussianDistribution();
    }

    public NoisyPositionUpdateDecorator(NoisyPositionUpdateDecorator rhs) {
        this.distribution = rhs.distribution;
        this.delegate = rhs.delegate.getClone();
    }

    @Override
    public void updatePosition(Particle particle) {
        //add random noise to particle's position
        Vector position = (Vector) particle.getPosition();

        for (int i = 0; i < position.getDimension(); i++) {
            double value = position.getReal(i);
            position.setReal(i, value + distribution.getRandomNumber());
        }

        delegate.updatePosition(particle);
    }

    @Override
    public NoisyPositionUpdateDecorator getClone() {
        return new NoisyPositionUpdateDecorator(this);
    }

    public PositionUpdateStrategy getDelegate() {
        return delegate;
    }

    public void setDelegate(PositionUpdateStrategy delegate) {
        this.delegate = delegate;
    }

    public ProbabilityDistributionFuction getDistribution() {
        return distribution;
    }

    public void setDistribution(ProbabilityDistributionFuction distribution) {
        this.distribution = distribution;
    }
}
