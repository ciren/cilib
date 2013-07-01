/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.entity.operators.mutation;

import java.util.List;
import net.cilib.controlparameter.ControlParameter;
import net.cilib.controlparameter.ProportionalControlParameter;
import net.cilib.entity.Entity;
import net.cilib.math.random.CauchyDistribution;
import net.cilib.math.random.ProbabilityDistributionFunction;
import net.cilib.type.types.Numeric;
import net.cilib.type.types.container.Vector;

/**
 *
 */
public class CauchyMutationStrategy extends MutationStrategy {

    private static final long serialVersionUID = 8576581034467137106L;
    private double location;
    private ControlParameter scaleStrategy;
    private final ProbabilityDistributionFunction cauchy;

    public CauchyMutationStrategy() {
        super();
        this.location = 0;
        this.scaleStrategy = new ProportionalControlParameter();
        this.cauchy = new CauchyDistribution();
    }

    public CauchyMutationStrategy(CauchyMutationStrategy copy) {
        super(copy);
        this.location = copy.location;
        this.scaleStrategy = copy.scaleStrategy.getClone();
        this.cauchy = copy.cauchy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CauchyMutationStrategy getClone() {
        return new CauchyMutationStrategy(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E extends Entity> List<E> mutate(List<E> entity) {
        for (Entity current : entity) {
            Vector chromosome = (Vector) current.getCandidateSolution();

            if (this.getMutationProbability().getParameter() >= this.getRandomDistribution().getRandomNumber()) {
                for (int i = 0; i < chromosome.size(); i++) {
                    Numeric element = chromosome.get(i);
                    double scale = this.scaleStrategy.getParameter(element.getBounds().getLowerBound(), element.getBounds().getUpperBound());
                    double value = this.getOperatorStrategy().evaluate(chromosome.doubleValueOf(i), this.cauchy.getRandomNumber(this.location, scale));

                    chromosome.setReal(i, value);
                }
            }
        }
        return entity;
    }

    public double getLocation() {
        return location;
    }

    public void setLocation(double location) {
        this.location = location;
    }

    public ControlParameter getScaleStrategy() {
        return scaleStrategy;
    }

    public void setScaleStrategy(ControlParameter scaleStrategy) {
        this.scaleStrategy = scaleStrategy;
    }
}
