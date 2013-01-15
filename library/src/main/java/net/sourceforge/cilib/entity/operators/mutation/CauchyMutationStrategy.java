/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.operators.mutation;

import java.util.List;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.ProportionalControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.math.random.CauchyDistribution;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 */
public class CauchyMutationStrategy extends MutationStrategy {

    private static final long serialVersionUID = 8576581034467137106L;
    private double location;
    private ControlParameter scaleStrategy;
    private ProbabilityDistributionFunction cauchy;

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
    public void mutate(List<? extends Entity> entity) {
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
