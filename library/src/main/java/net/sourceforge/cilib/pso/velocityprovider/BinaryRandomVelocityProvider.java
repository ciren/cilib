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
package net.sourceforge.cilib.pso.velocityprovider;

import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Vectors;
import net.sourceforge.cilib.type.types.Bounds;

/**
 * Implementation of Modified particle swarm optimization algorithm for variable
 * selection in MLR and PLS modeling.
 * 
 * Velocity of each particle is random
 * <p>
 * References:
 * </p>
 * <ul><li>
 * Qi Shen, Jian-Hui Jiang, Chen-Xu Jiao, Guo-li Shen, Ru-Qin Yu., 
 * "Modified particle swarm optimization algorithm for variable
 * selection in MLR and PLS modeling: QSAR studies of antagonism of 
 * angiotensin II antagonists" (2004). European Journal of Pharmaceutical
 * Sciences 22, 145-152
 * </li></ul>
 */
public final class BinaryRandomVelocityProvider implements VelocityProvider {

    protected RandomProvider r1;
    protected Bounds bounds;

    public BinaryRandomVelocityProvider() {
        this(new MersenneTwister(), new Bounds(0,1));
    }

    public BinaryRandomVelocityProvider(RandomProvider r1, Bounds bounds) {
        this.r1 = r1;
        this.bounds = bounds;
    }

    public BinaryRandomVelocityProvider(BinaryRandomVelocityProvider copy) {
        this.r1 = copy.r1;
        this.bounds = copy.bounds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BinaryRandomVelocityProvider getClone() {
        return new BinaryRandomVelocityProvider(this);
    }

    @Override
    public Vector get(Particle particle) {
        Vector.Builder velocity = Vector.newBuilder();

        for(int i = 0; i < particle.getDimension(); i++) {
            velocity.addWithin(r1.nextDouble(), bounds);
        }

        return velocity.build();
    }

    public RandomProvider getR1() {
        return r1;
    }

    public void setR1(RandomProvider r1) {
        this.r1 = r1;
    }

    public Bounds getBounds() {
        return bounds;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }
}
