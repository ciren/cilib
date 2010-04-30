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

import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.math.random.RandomNumber;

/**
 * Decorates a {@link PositionUpdateVisitor} or a {@link VelocityUpdateVisitor}
 * with random noise from any probability distribution function.
 *
 * @author Bennie Leonard
 */
public class NoisyPositionUpdateDecorator implements PositionUpdateStrategy {

    private RandomNumber random;
    private PositionUpdateStrategy delegate;

    public NoisyPositionUpdateDecorator() {
        random = new RandomNumber();
    }

    public NoisyPositionUpdateDecorator(NoisyPositionUpdateDecorator rhs) {
        this.random = rhs.random.getClone();
        this.delegate = rhs.delegate.getClone();
    }

    @Override
    public void updatePosition(Particle particle) {
        //add random noise to particle's position
        Vector position = (Vector) particle.getPosition();

        for (int i = 0; i < position.getDimension(); i++) {
            double value = position.getReal(i);
            position.setReal(i, value + random.getGaussian());
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

    public RandomNumber getRandom() {
        return random;
    }

    public void setRandom(RandomNumber random) {
        this.random = random;
    }
}
