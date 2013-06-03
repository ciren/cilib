/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.dynamic.responsestrategies;

import java.util.List;
import fj.F;
import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.pso.particle.Particle;

/**
 * This reaction strategy reinitialises the specified
 * {@link #setReinitialisationRatio(double) ratio} of randomly chosen entities in the given
 * Topology.
 *
 * @param <E> some PopulationBasedAlgorithm population based algorithm
 */
public class ReinitialisationReactionStrategy<E extends SinglePopulationBasedAlgorithm> extends EnvironmentChangeResponseStrategy {
    private static final long serialVersionUID = -7283513652737895281L;

    protected double reinitialisationRatio = 0.0;

    public ReinitialisationReactionStrategy() {
        reinitialisationRatio = 0.1;
    }

    public ReinitialisationReactionStrategy(ReinitialisationReactionStrategy<E> rhs) {
        super(rhs);
        reinitialisationRatio = rhs.reinitialisationRatio;
    }

    @Override
    public ReinitialisationReactionStrategy<E> getClone() {
        return new ReinitialisationReactionStrategy<E>(this);
    }

    /**
     * Reinitialise the {@link Entity entities} inside the topology.
     *
     * {@inheritDoc}
     */
    @Override
	protected <P extends Particle, A extends SinglePopulationBasedAlgorithm<P>> void performReaction(
			A algorithm) {
        fj.data.List<P> entities = algorithm.getTopology();
        int reinitialiseCount = (int) Math.floor(reinitialisationRatio * entities.length());

        reinitialise(entities, reinitialiseCount);
    }

    /**
     * Reinitialise a specified number of the given entities.
     *
     * @param entities a {@link List} of entities that should be considered for
     *        reinitialisation
     * @param reinitialiseCount an<code>int<code> specifying how many entities should be
     *        reinitialised
     */
    protected <P extends Particle> void reinitialise(final fj.data.List<P> entities, int reinitialiseCount) {
    	fj.data.List<P> local = entities;

        for (int i = 0; i < reinitialiseCount; i++) {
            int random = Rand.nextInt(entities.length());
            final Entity entity = entities.index(random);
            entity.getPosition().randomise();
            // remove the selected element from the all list preventing it from being selected again
            local = local.filter(new F<P, Boolean>() {
				@Override
				public Boolean f(P element) {
					return (element.equals(entity)) ? false : true;
				}
            });
        }
    }

    /**
     * Set the ratio of entities that should be reinitialised.
     *
     * @param rr a double value in the range <code>(0.0, 1.0)</code>
     * @throws {@link IllegalArgumentException} when the ratio is not within the above
     *         mentioned range
     */
    public void setReinitialisationRatio(double rr) {
        if (rr < 0.0 || rr > 1.0) {
            throw new IllegalArgumentException("The reinitialisationRatio must be in the range (0.0, 1.0)");
        }

        reinitialisationRatio = rr;
    }

    /**
     * Get the ratio of entities that should be reinitialised.
     *
     * @return the ratio of entities that should be reinitialised
     */
    public double getReinitialisationRatio() {
        return reinitialisationRatio;
    }
}
