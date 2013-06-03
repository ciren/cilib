/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.operators.mutation;

import fj.F;
import java.util.List;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Property;
import net.sourceforge.cilib.math.random.GaussianDistribution;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Vectors;

/**
 *
 */
public class SelfAdaptiveMutationStrategy extends MutationStrategy {

    private static final long serialVersionUID = -8942505730267916237L;
    private final ProbabilityDistributionFunction randomSingle;
    private final ProbabilityDistributionFunction randomDimension;
    private double tau;
    private double tauPrime;

    public SelfAdaptiveMutationStrategy() {
        this.randomSingle = new GaussianDistribution();
        this.randomDimension = new GaussianDistribution();
        this.tau = Double.NaN;
        this.tauPrime = Double.NaN;
    }

    @Override
    public MutationStrategy getClone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <E extends Entity> List<E> mutate(List<E> offspringList) {
        initialiseConstants(offspringList);

        final double pre = tauPrime * randomSingle.getRandomNumber();

        for (Entity offspring : offspringList) {
            Vector candidateSolution = (Vector) offspring.getPosition();
            Vector strategy = (Vector) offspring.get(Property.STRATEGY_PARAMETERS);

            // Update the offspring
            for (int i = 0; i < candidateSolution.size(); i++) {
                double value = candidateSolution.doubleValueOf(i) + strategy.doubleValueOf(i) * randomDimension.getRandomNumber();
                candidateSolution.setReal(i, value);
            }

            // Update the strategy parameters
            Vector newStrategy = Vectors.transform(strategy, new F<Numeric, Double>() {
                @Override
                public Double f(Numeric from) {
                    double exponent = pre + tau * randomDimension.getRandomNumber();
                    return from.doubleValue() * Math.exp(exponent);
                }
            });
            offspring.put(Property.STRATEGY_PARAMETERS, newStrategy);
        }
        return offspringList;
    }

    private void initialiseConstants(List<? extends Entity> offspringList) {
        Entity first = offspringList.get(0);

        if (Double.compare(tau, Double.NaN) == 0) {
            tau = 1.0 / (Math.sqrt(2 * Math.sqrt(first.getDimension())));
            tauPrime = 1.0 / Math.sqrt(2 * first.getDimension());
        }
    }
}
