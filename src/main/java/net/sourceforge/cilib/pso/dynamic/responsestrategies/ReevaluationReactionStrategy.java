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
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.StandardParticle;

/**
 * This reaction strategy reevaluates the specified
 * {@link #setReevaluationRatio(double) ratio} of randomly chosen entities in the given
 * {@link Topology}.
 *
 * @author Theuns Cloete
 * @param <E> some {@link PopulationBasedAlgorithm population based algorithm}
 */
public class ReevaluationReactionStrategy<E extends PopulationBasedAlgorithm> extends EnvironmentChangeResponseStrategy<E> {
    private static final long serialVersionUID = -5549918743502730714L;

    protected double reevaluationRatio = 0.0;
    protected Random randomGenerator = null;

    public ReevaluationReactionStrategy() {
        // super() called automatically
        reevaluationRatio = 0.1;
        randomGenerator = new MersenneTwister();
    }

    public ReevaluationReactionStrategy(ReevaluationReactionStrategy<E> rhs) {
        super(rhs);
        reevaluationRatio = rhs.reevaluationRatio;
        randomGenerator = rhs.randomGenerator.getClone();
    }

    @Override
    public ReevaluationReactionStrategy<E> getClone() {
        return new ReevaluationReactionStrategy<E>(this);
    }

    /**
     * Just reevaluate the {@link Entity entities} inside the topology. An entity's
     * implementation should handle updating anything else that is necessary, e.g. a
     * {@link StandardParticle}'s <code>personal best position</code> in the case of
     * {@link PSO}.
     *
     * {@inheritDoc}
     */
    @Override
    public void performReaction(E algorithm) {
        List<? extends Entity> entities = algorithm.getTopology().asList();
        int reevaluateCount = (int) Math.floor(reevaluationRatio * entities.size());

        reevaluate(entities, reevaluateCount);
    }

    /**
     * Reevaluate a specified percentage of the given entities.
     *
     * @param entities a {@link List} of entities that should be considered for reevaluation
     * @param reevaluateCount an <code>int<code> specifying how many entities should be
     *        reevaluated
     */
    protected void reevaluate(List<? extends Entity> entities, int reevaluateCount) {
        for (int i = 0; i < reevaluateCount; i++) {
            int random = randomGenerator.nextInt(entities.size());
            Entity entity = entities.get(random);
            // FIXME: does not reevaluate the _best_ positon.
            entity.calculateFitness();
            // remove the selected element from the all list preventing it from being selected again
            entities.remove(random);
        }
    }

    /**
     * Set the ratio of entities that should be reevaluated.
     *
     * @param rer a double value in the range <code>(0.0, 1.0)</code>
     * @throws {@link IllegalArgumentException} when the ratio is not within the above
     *         mentioned range
     */
    public void setReevaluationRatio(double rer) {
        if (rer < 0.0 || rer > 1.0) {
            throw new IllegalArgumentException("The reevaluationRatio must be in the range (0.0, 1.0)");
        }

        reevaluationRatio = rer;
    }

    /**
     * Get the ratio of entities that should be reevaluated.
     *
     * @return the ratio of entities that should be reevaluated
     */
    public double getReevaluationRatio() {
        return reevaluationRatio;
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
