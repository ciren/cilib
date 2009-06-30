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
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.Random;

/**
 * This {@link EnvironmentChangeResponseStrategy reaction strategy} wraps both a
 * {@link ReinitializationReactionStrategy} and a {@link ReevaluationReactionStrategy}. It
 * firstly performs the specified
 * {@link #setReinitializationRatio(double) number of reinitializations} on randomly chosen
 * entities in the {@link Topology}. Then it performs the specified
 * {@link #setReevaluationRatio(double) number of reevaluations} on randomly chosen entities
 * from those that remain.
 *
 * @author Theuns Cloete
 * @param <E> some {@link PopulationBasedAlgorithm population based algorithm}
 */
public class DualReactionStrategy<E extends PopulationBasedAlgorithm> extends EnvironmentChangeResponseStrategy<E> {
    private static final long serialVersionUID = -1944318436718779296L;

    protected ReevaluationReactionStrategy<E> reevaluation = null;
    protected ReinitializationReactionStrategy<E> reinitialization = null;
    protected Random randomGenerator = null;

    public DualReactionStrategy() {
        // calls super() automatically
        reevaluation = new ReevaluationReactionStrategy<E>();
        reinitialization = new ReinitializationReactionStrategy<E>();
        this.setRandomGenerator(new MersenneTwister());
    }

    public DualReactionStrategy(DualReactionStrategy<E> rhs) {
        super(rhs);
        reevaluation = rhs.reevaluation.getClone();
        reinitialization = rhs.reinitialization.getClone();
        this.setRandomGenerator(rhs.randomGenerator.getClone());
    }

    @Override
    public DualReactionStrategy<E> getClone() {
        return new DualReactionStrategy<E>(this);
    }

    /**
     * Respond by firstly reinitializing a specified number of entities and then reevaluating
     * another specified number of entities.
     *
     * {@inheritDoc}
     */
    @Override
    public void performReaction(E algorithm) {
        List<? extends Entity> entities = algorithm.getTopology().asList();
        int reinitializeCount = (int) Math.floor(reinitialization.getReinitializationRatio() * entities.size());
        int reevaluateCount = (int) Math.floor(reevaluation.getReevaluationRatio() * entities.size());

        reinitialization.reinitialize(entities, reinitializeCount);
        reevaluation.reevaluate(entities, reevaluateCount);
    }

    /**
     * Make sure that the sum of the two ratios are within the range [0.0, 1.0)
     *
     * @throws {@link IllegalArgumentException} when the sum of the the two ratios are not
     *         within the above mentioned range
     */
    private void validateRatios() {
        if (reinitialization.getReinitializationRatio() + reevaluation.getReevaluationRatio() > 1.0) {
            throw new IllegalArgumentException("reinitializationRatio + reevaluationRatio may not be > 1");
        }

        if (reinitialization.getReinitializationRatio() + reevaluation.getReevaluationRatio() <= 0.0) {
            throw new IllegalArgumentException("reinitializationRatio + reevaluationRatio <= 0 causes this reaction to be useless");
        }
    }

    /**
     * Set the ratio of entities that should be reinitialized. This is defered to the wrapped
     * {@link ReinitializationReactionStrategy}.
     *
     * @param rr a double value in the range <code>(0.0, 1.0)</code>
     */
    public void setReinitializationRatio(double rr) {
        reinitialization.setReinitializationRatio(rr);
        validateRatios();
    }

    /**
     * Get the ratio of entities that should be reinitialized. This is defered to the wrapped
     * {@link ReinitializationReactionStrategy}.
     *
     * @return the ratio of entities that should be reinitialized
     */
    public double getReinitializationRatio() {
        return reinitialization.getReinitializationRatio();
    }

    /**
     * Set the ratio of entities that should be reevaluated. This is defered to the wrapped
     * {@link ReevaluationReactionStrategy}.
     *
     * @param rer a double value in the range <code>(0.0, 1.0)</code>
     */
    public void setReevaluationRatio(double rer) {
        reevaluation.setReevaluationRatio(rer);
        validateRatios();
    }

    /**
     * Get the ratio of entities that should be reevaluated. This is defered to the wrapped
     * {@link ReevaluationReactionStrategy}.
     *
     * @return the ratio of entities that should be reevaluated
     */
    public double getReevaluationRatio() {
        return reevaluation.getReevaluationRatio();
    }

    /**
     * Set the random number generator to use. Also make sure that the wrapped reaction
     * strategies ({@link #reevaluation} and {@link #reinitialization}) use the same
     * generator, so that the random numbers that are retrieved are in the same sequence.
     *
     * @param r a {@link Random} object
     */
    public void setRandomGenerator(Random r) {
        randomGenerator = r;
        reevaluation.setRandomGenerator(randomGenerator);
        reinitialization.setRandomGenerator(randomGenerator);
    }

    /**
     * Retrieve the random number generator being used.
     *
     * @return the {@link Random} object being used to generate a random sequence of numbers
     */
    public Random getRandomGenerator() {
        return randomGenerator;
    }
}
