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
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.Random;
import net.sourceforge.cilib.pso.dynamic.DynamicParticle;

/**
 * @author Anna Rakitianskaia, Julien Duhain
 */
public class PartialReinitialisationResponseStrategy<E extends PopulationBasedAlgorithm> extends
        ParticleReevaluationResponseStrategy<E> {
    private static final long serialVersionUID = 4619744183683905269L;

    private double reinitialisationRatio;
    private Random randomiser;

    public PartialReinitialisationResponseStrategy() {
        super();
        reinitialisationRatio = 0.5;
        randomiser = new MersenneTwister();
    }

    public PartialReinitialisationResponseStrategy(PartialReinitialisationResponseStrategy<E> copy) {
        this.reinitialisationRatio = copy.reinitialisationRatio;
        this.randomiser = copy.randomiser.getClone();
    }

    @Override
    public PartialReinitialisationResponseStrategy<E> getClone() {
        return new PartialReinitialisationResponseStrategy<E>(this);
    }

    /**
     * Respond to environment change by re-evaluating each particle's position, personal best and neighbourhood best,
     * and reinitializing the positions of a specified percentage of particles.
     * @param algorithm PSO algorithm that has to respond to environment change
     */
    @Override
    public void respond(E algorithm) {

        Topology<? extends Entity> topology = algorithm.getTopology();

        // Reevaluate current position. Update personal best (done by reevaluate()).
        Iterator<? extends Entity> iterator = topology.iterator();
        int reinitCounter = 0;
        int keepCounter = 0;
        int populationSize = algorithm.getTopology().size();
        while (iterator.hasNext()) {
            DynamicParticle current = (DynamicParticle) iterator.next();

            //makes sure the charged particles are randomly positionned accross the topology
            if(reinitCounter < Math.floor(populationSize*reinitialisationRatio) && randomiser.nextDouble() < reinitialisationRatio && current != ((E)algorithm).getTopology().getBestEntity()){
                current.getPosition().randomize(this.randomiser);
                current.getVelocity().reset();
                current.getProperties().put(EntityType.Particle.BEST_POSITION, current.getPosition().getClone());
                ++reinitCounter;
            }//if
            else if(keepCounter > Math.floor(populationSize*(1.0-reinitialisationRatio)) && current != ((E)algorithm).getTopology().getBestEntity()){
                current.getPosition().randomize(this.randomiser);
                current.getVelocity().reset();
                current.getProperties().put(EntityType.Particle.BEST_POSITION, current.getPosition().getClone());
                ++reinitCounter;
            }//else if
            else{
                ++keepCounter;
            }//else
        }

        // Re-evaluate:
        reevaluateParticles(algorithm); // super class method
    }

    /**
     * @return the reinitialisationRatio
     */
    public double getReinitialisationRatio() {
        return reinitialisationRatio;
    }

    /**
     * @param reinitialisationRatio the reinitialisationRatio to set
     */
    public void setReinitialisationRatio(double reinitialisationRatio) {
        this.reinitialisationRatio = reinitialisationRatio;
    }

    /**
     * @return the randomiser
     */
    public Random getRandomiser() {
        return randomiser;
    }

    /**
     * @param randomiser the randomiser to set
     */
    public void setRandomiser(Random randomiser) {
        this.randomiser = randomiser;
    }
}
