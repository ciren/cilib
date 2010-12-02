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
public class ParticleBehavior implements Comparable<ParticleBehavior> {
    private PositionUpdateStrategy positionUpdateStrategy;
    private VelocityUpdateStrategy velocityUpdateStrategy;
    private int successCounter;
    private int selectedCounter;

    /**
     * Default constructor assigns standard position and velocity update
     * strategies to particles.
     */
    public ParticleBehavior() {
        positionUpdateStrategy = new StandardPositionUpdateStrategy();
        velocityUpdateStrategy = new StandardVelocityUpdate();
        successCounter = 0;
        selectedCounter = 0;
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
        successCounter = 0;
        selectedCounter = 0;
    }

    /**
     * Copy Constructor.
     *
     * @param copy The {@link ParticleBehavior} object to copy.
     */
    public ParticleBehavior(ParticleBehavior copy) {
        this.positionUpdateStrategy = copy.positionUpdateStrategy;
        this.velocityUpdateStrategy = copy.velocityUpdateStrategy;
        this.selectedCounter = copy.selectedCounter;
        this.successCounter = copy.successCounter;
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

    /**
     * Increment the number of times this behavior was successful
     */
    public void incrementSuccessCounter() {
        successCounter++;
    }

    /**
     * Increment the number of times this behavior has been selected
     */
    public void incrementSelectedCounter() {
        selectedCounter++;
    }

    /**
     * Get the number of times this behavior has been selected
     */
    public int getSelectedCounter() {
        return selectedCounter;
    }

    /**
     * Get the number of times this behavior was successful
     */
    public int getSuccessCounter() {
        return successCounter;
    }

    /**
     * Compare two behaviors with regards to how successful they were in finding
     * better fitness values.
     * @param o The {@link ParticleBehavior} object to compare with this object.
     * @return -1 if this behavior was less successful, 0 if the two behaviors were equally successful, 1 otherwise.
     */
    @Override
    public int compareTo(ParticleBehavior o) {
        int mySuccesses = this.successCounter;
        int otherSuccesses = o.successCounter;
        return(mySuccesses < otherSuccesses ? -1 : (mySuccesses == otherSuccesses ? 0 : 1));
    }
}
