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
package net.sourceforge.cilib.pso.dynamic.responsestrategies;

import java.util.List;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.Random;
import net.sourceforge.cilib.type.types.Types;

/**
 * This reaction strategy reinitializes the specified
 * {@link #setReinitializationRatio(double) ratio} of randomly chosen entities in the given
 * {@link Topology}.
 *
 * @author Theuns Cloete
 * @param <E> some {@link PopulationBasedAlgorithm population based algorithm}
 */
public class ReinitializationReactionStrategy<E extends PopulationBasedAlgorithm> extends EnvironmentChangeResponseStrategy<E> {
    private static final long serialVersionUID = -7283513652737895281L;

    protected double reinitializationRatio = 0.0;
    protected Random randomGenerator = null;

    public ReinitializationReactionStrategy() {
        // super() is automatically called
        reinitializationRatio = 0.1;
        randomGenerator = new MersenneTwister();
    }

    public ReinitializationReactionStrategy(ReinitializationReactionStrategy<E> rhs) {
        super(rhs);
        reinitializationRatio = rhs.reinitializationRatio;
        randomGenerator = rhs.randomGenerator.getClone();
    }

    @Override
    public ReinitializationReactionStrategy<E> getClone() {
        return new ReinitializationReactionStrategy<E>(this);
    }

    /**
     * Reinitialize the {@link Entity entities} inside the topology.
     *
     * {@inheritDoc}
     */
    @Override
    public void performReaction(E algorithm) {
        List<? extends Entity> entities = algorithm.getTopology().asList();
        int reinitializeCount = (int) Math.floor(reinitializationRatio * entities.size());

        reinitialize(entities, reinitializeCount);
    }

    /**
     * Reinitialize a specified number of the given entities.
     *
     * @param entities a {@link List} of entities that should be considered for
     *        reinitialization
     * @param reinitializeCount an<code>int<code> specifying how many entities should be
     *        reinitialized
     */
    protected void reinitialize(List<? extends Entity> entities, int reinitializeCount) {
        for (int i = 0; i < reinitializeCount; i++) {
            int random = randomGenerator.nextInt(entities.size());
            Entity entity = entities.get(random);
            entity.getCandidateSolution().randomize(randomGenerator);
            // remove the selected element from the all list preventing it from being selected again
            entities.remove(random);
        }
    }

    /**
     * Set the ratio of entities that should be reinitialized.
     *
     * @param rr a double value in the range <code>(0.0, 1.0)</code>
     * @throws {@link IllegalArgumentException} when the ratio is not within the above
     *         mentioned range
     */
    public void setReinitializationRatio(double rr) {
        if (rr < 0.0 || rr > 1.0) {
            throw new IllegalArgumentException("The reinitializationRatio must be in the range (0.0, 1.0)");
        }

        reinitializationRatio = rr;
    }

    /**
     * Get the ratio of entities that should be reinitialized.
     *
     * @return the ratio of entities that should be reinitialized
     */
    public double getReinitializationRatio() {
        return reinitializationRatio;
    }

    /**
     * Set the random number generator to use.
     *
     * @param r a {@link Random} object
     */
    protected void setRandomGenerator(Random r) {
        randomGenerator = r;
    }

    /**
     * Retrieve the random number generator being used.
     *
     * @return the {@link Random} object being used to generate a random sequence of numbers
     */
    protected Random getRandomGenerator() {
        return randomGenerator;
    }
}
