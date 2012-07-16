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
package net.sourceforge.cilib.pso.dynamic.responsestrategies;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.pso.dynamic.DynamicParticle;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.type.types.Real;

/**
 * This reaction strategy initialises new dimensions introduced into the particles.
 * These new dimensions are indicated by initially having the value Double.NaN.
 *
 * @param <E> some {@link PopulationBasedAlgorithm population based algorithm}
 */
public class InitialiseNaNElementsReactionStrategy<E extends PopulationBasedAlgorithm> extends EnvironmentChangeResponseStrategy<E> {
    protected RandomProvider randomGenerator = null;

    public InitialiseNaNElementsReactionStrategy() {
        randomGenerator = new MersenneTwister();
    }

    public InitialiseNaNElementsReactionStrategy(InitialiseNaNElementsReactionStrategy<E> rhs) {
        super(rhs);
        randomGenerator = rhs.randomGenerator;
    }

    @Override
    public InitialiseNaNElementsReactionStrategy<E> getClone() {
        return new InitialiseNaNElementsReactionStrategy<E>(this);
    }

    /**
     * Initialise the dimensions that have the value of Double.NaN. This is done
	 * for all the particles in the topology.
	 * 
     * {@inheritDoc}
     */
    @Override
    public void performReaction(E algorithm) {
        Topology<? extends Entity> entities = algorithm.getTopology();

        for (Entity entity : entities) {
			DynamicParticle particle = (DynamicParticle)entity;
			//initialise position
            Vector position = (Vector)particle.getPosition();
			for (int curElement = 0; curElement < position.size(); ++curElement) {
				if (Double.isNaN(position.getReal(curElement)))
            		((Real) position.get(curElement)).randomize(randomGenerator);
			}
			
			//initialise personal best
            Vector personalBest = (Vector)particle.getBestPosition();
			for (int curElement = 0; curElement < position.size(); ++curElement) {
				if (Double.isNaN(personalBest.getReal(curElement)))
            		personalBest.setReal(curElement, position.getReal(curElement));
			}

			//initialise velocity
            Vector velocity = particle.getVelocity();
			for (int curElement = 0; curElement < position.size(); ++curElement) {
				if (Double.isNaN(velocity.getReal(curElement)))
            		velocity.setReal(curElement, 0.0);
			}
        }
    }

    /**
     * Set the random number generator to use.
     *
     * @param r a {@link Random} object
     */
    protected void setRandomGenerator(RandomProvider r) {
        randomGenerator = r;
    }

    /**
     * Retrieve the random number generator being used.
     *
     * @return the {@link Random} object being used to generate a random sequence of numbers
     */
    protected RandomProvider getRandomGenerator() {
        return randomGenerator;
    }
}
