/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.dynamic.responsestrategies;

import java.util.List;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.util.selection.Samples;
import net.sourceforge.cilib.util.selection.recipes.RandomSelector;

/**
 * This reaction strategy reevaluates the specified
 * {@link #setReevaluationRatio(double) ratio} of randomly chosen entities in
 * the given {@link Topology}.
 *
 * @param <E> some {@link PopulationBasedAlgorithm population based algorithm}
 */
public class ReevaluationReactionStrategy<E extends PopulationBasedAlgorithm> extends EnvironmentChangeResponseStrategy<E> {

    private static final long serialVersionUID = -5549918743502730714L;
    protected double reevaluationRatio = 0.0;

    public ReevaluationReactionStrategy() {
        reevaluationRatio = 0.1;
    }

    public ReevaluationReactionStrategy(ReevaluationReactionStrategy<E> rhs) {
        super(rhs);
        reevaluationRatio = rhs.reevaluationRatio;
    }

    @Override
    public ReevaluationReactionStrategy<E> getClone() {
        return new ReevaluationReactionStrategy<E>(this);
    }

    /**
     * Just reevaluate the {@link Entity entities} inside the topology. An
     * entity's implementation should handle updating anything else that is
     * necessary, e.g. a {@link StandardParticle}'s
     * <code>personal best position</code> in the case of {@link PSO}.
     *
     * {@inheritDoc}
     */
    @Override
    public void performReaction(E algorithm) {
        Topology<? extends Entity> entities = algorithm.getTopology();
        int reevaluateCount = (int) Math.floor(reevaluationRatio * entities.size());

        reevaluate(entities, reevaluateCount);
    }

    /**
     * Reevaluate a specified percentage of the given entities.
     *
     * @param entities a {@link List} of entities that should be considered for
     * reevaluation
     * @param reevaluateCount an <code>int<code> specifying how many entities should be
     *        reevaluated
     */
    protected void reevaluate(List<? extends Entity> entities, int reevaluateCount) {
        RandomSelector selector = new RandomSelector();
        List<? extends Entity> subList = selector.on(entities).select(Samples.first(reevaluateCount));
        for (Entity entity : subList) {
            // FIXME: does not reevaluate the _best_ position.
            entity.calculateFitness();
        }
    }

    /**
     * Set the ratio of entities that should be reevaluated.
     *
     * @param rer a double value in the range <code>(0.0, 1.0)</code>
     * @throws {@link IllegalArgumentException} when the ratio is not within the
     * above mentioned range
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
}
