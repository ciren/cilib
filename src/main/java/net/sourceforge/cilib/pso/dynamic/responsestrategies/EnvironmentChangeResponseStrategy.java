/*
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
package net.sourceforge.cilib.pso.dynamic.responsestrategies;

import java.io.Serializable;
import java.util.Iterator;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.util.Cloneable;

/**
 * TODO: Complete this Javadoc.
 */
public abstract class EnvironmentChangeResponseStrategy<E extends PopulationBasedAlgorithm> implements Cloneable, Serializable {
    protected boolean hasMemory = true;

    public EnvironmentChangeResponseStrategy() {
        this.hasMemory = true;
    }

    public EnvironmentChangeResponseStrategy(EnvironmentChangeResponseStrategy<E> rhs) {
        this.hasMemory = rhs.hasMemory;
    }

    /**
     * Clone the <tt>EnvironmentChangeResponseStrategy</tt> object.
     *
     * @return A cloned <tt>EnvironmentChangeResponseStrategy</tt>
     */
    public abstract EnvironmentChangeResponseStrategy<E> getClone();

    /**
     * Respond to the environment change and ensure that the neighbourhood best entities are
     * updated. This method (Template Method) calls two other methods in turn:
     * <ul>
     * <li>{@link #performReaction(PopulationBasedAlgorithm)}</li>
     * <li>{@link #updateNeighbourhoodBestEntities(Topology)}</li>
     * </ul>
     *
     * @param algorithm some {@link PopulationBasedAlgorithm population based algorithm}
     */
    public void respond(E algorithm) {
        performReaction(algorithm);
        if(hasMemory) updateNeighbourhoodBestEntities(algorithm.getTopology());
    }

    /**
     * This is the method responsible for responding that should be overridden by sub-classes.
     * @param algorithm
     */
    protected abstract void performReaction(E algorithm);

    /**
     * TODO: The problem with this is that it is PSO specific. It uses {@link Particle particles}
     * instead of {@link Entity entities}, because the {@link Entity} class does not have the
     * notion of a neighbourhood best.
     *
     * @param topology a topology of {@link Particle particles} :-(
     */
    protected void updateNeighbourhoodBestEntities(Topology<? extends Entity> topology) {
        for (Iterator<? extends Entity> outside = topology.iterator(); outside.hasNext(); ) {
            Particle current = (Particle) outside.next();
            current.calculateFitness();

            for (Iterator<? extends Entity> inside = topology.neighbourhood(outside); inside.hasNext(); ) {
                Particle other = (Particle) inside.next();
                if (current.getSocialBestFitness().compareTo(other.getNeighbourhoodBest().getSocialBestFitness()) > 0) {
                    other.setNeighbourhoodBest(current);
                }
            }
        }
    }

    public boolean getHasMemory() {
        return hasMemory;
    }

    public void setHasMemory(boolean hasMemory) {
        this.hasMemory = hasMemory;
    }
}
