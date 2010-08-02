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
package net.sourceforge.cilib.pso.particle;

import net.sourceforge.cilib.pso.positionupdatestrategies.PositionUpdateStrategy;
import net.sourceforge.cilib.pso.positionupdatestrategies.StandardPositionUpdateStrategy;
import net.sourceforge.cilib.pso.velocityupdatestrategies.StandardVelocityUpdate;
import net.sourceforge.cilib.pso.velocityupdatestrategies.VelocityUpdateStrategy;

/**
 * A {@link ParticleBehavior} object encapsulates the {@link PositionUpdateStrategy}
 * and {@link VelocityUpdateStrategy} that a particle uses.
 *
 * @author Bennie Leonard
 */
public class ParticleBehavior {
    private PositionUpdateStrategy positionUpdateStrategy;
    private VelocityUpdateStrategy velocityUpdateStrategy;

    /**
     * Default constructor assigns standard position and velocity update
     * strategies to particles.
     */
    public ParticleBehavior() {
        positionUpdateStrategy = new StandardPositionUpdateStrategy();
        velocityUpdateStrategy = new StandardVelocityUpdate();
    }

    /**
     * Constructor that assigns a given position and velocity update strategy
     * to a particle.
     *
     * @param p The {@link PositionUpdateStrategy} to use.
     * @param v The {@link VelocityUpdateStrategy} to use.
     */
    public ParticleBehavior(PositionUpdateStrategy p, VelocityUpdateStrategy v) {
        positionUpdateStrategy = p;
        velocityUpdateStrategy = v;
    }

    /**
     * Copy Constructor.
     *
     * @param copy The {@link ParticleBehavior} object to copy.
     */
    public ParticleBehavior(ParticleBehavior copy) {
        this.positionUpdateStrategy = copy.positionUpdateStrategy;
        this.velocityUpdateStrategy = copy.velocityUpdateStrategy;
    }

    /**
     * {@inheritDoc}
     */
    public ParticleBehavior getClone() {
        return new ParticleBehavior(this);
    }

    /**
     * Get the currently set {@link PositionUpdateStrategy}.
     *
     * @return The current {@link PositionUpdateStrategy}.
     */
    public PositionUpdateStrategy getPositionUpdateStrategy() {
        return positionUpdateStrategy;
    }

    /**
     * Set the {@link PositionUpdateStrategy}.
     *
     * @param strategy The {@link PositionUpdateStrategy} to set.
     */
    public void setPositionUpdateStrategy(PositionUpdateStrategy strategy) {
        positionUpdateStrategy = strategy;
    }

    /**
     * Get the currently set {@link VelocityUpdateStrategy}.
     *
     * @return The current {@link VelocityUpdateStrategy}.
     */
    public VelocityUpdateStrategy getVelocityUpdateStrategy() {
        return velocityUpdateStrategy;
    }

    /**
     * Set the {@link VelocityUpdateStrategy}.
     *
     * @param strategy The {@link VelocityUpdateStrategy} to set.
     */
    public void setVelocityUpdateStrategy(VelocityUpdateStrategy strategy) {
        velocityUpdateStrategy = strategy;
    }
}
