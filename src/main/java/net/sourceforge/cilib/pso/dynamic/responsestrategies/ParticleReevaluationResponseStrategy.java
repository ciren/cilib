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

import java.util.Iterator;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.pso.dynamic.DynamicParticle;

/**
 * @author Anna Rakitianskaia
 */
public class ParticleReevaluationResponseStrategy<E extends PopulationBasedAlgorithm> extends
        EnvironmentChangeResponseStrategy<E> {
    private static final long serialVersionUID = -4389695103800841288L;

    public ParticleReevaluationResponseStrategy() {
        // empty constructor
    }

    public ParticleReevaluationResponseStrategy(ParticleReevaluationResponseStrategy<E> copy) {
        // empty copy constructor
    }

    public ParticleReevaluationResponseStrategy<E> getClone() {
        return new ParticleReevaluationResponseStrategy<E>(this);
    }

    /**
     * Respond to environment change by re-evaluating each particle's position, personal best and neighbourhood best.
     * @param algorithm PSO algorithm that has to respond to environment change
     */
    public void respond(E algorithm) {
        reevaluateParticles(algorithm);
    }

    /**
     * Re-evaluate each particle's position, personal best and neighbourhood best.
     * @param algorithm PSO algorithm that has to respond to environment change
     */
    protected void reevaluateParticles(E algorithm) {

        Topology<? extends Entity> topology = algorithm.getTopology();

        // Reevaluate current position. Update personal best (done by reevaluate()).
        Iterator<? extends Entity> iterator = topology.iterator();
        while (iterator.hasNext()) {
            DynamicParticle current = (DynamicParticle) iterator.next();
            current.reevaluate();
        }

        // Update the neighbourhood best
        iterator = topology.iterator();
        while (iterator.hasNext()) {
            Particle current = (Particle) iterator.next();
            Iterator<? extends Entity> j = topology.neighbourhood(iterator);
            while (j.hasNext()) {
                Particle other = (Particle) j.next();
                if (current.getSocialBestFitness().compareTo(other.getNeighbourhoodBest().getSocialBestFitness()) > 0) {
                    other.setNeighbourhoodBest(current);
                }
            } // end for
        } // end for
    }

    @Override
    protected void performReaction(E algorithm) {
        reevaluateParticles(algorithm);
    }
} // end class
