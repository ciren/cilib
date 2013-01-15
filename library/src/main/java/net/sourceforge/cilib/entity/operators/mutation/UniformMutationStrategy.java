/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.operators.mutation;

import java.util.List;
import java.util.ListIterator;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.ProportionalControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 */
public class UniformMutationStrategy extends MutationStrategy {

    private static final long serialVersionUID = -3951730432882403768L;
    private ControlParameter minStrategy, maxStrategy;

    public UniformMutationStrategy() {
        super();
        minStrategy = new ProportionalControlParameter();
        maxStrategy = new ProportionalControlParameter();
    }

    public UniformMutationStrategy(UniformMutationStrategy copy) {
        super(copy);
        this.minStrategy = copy.minStrategy.getClone();
        this.maxStrategy = copy.maxStrategy.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniformMutationStrategy getClone() {
        return new UniformMutationStrategy(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mutate(List<? extends Entity> entity) {
        for (ListIterator<? extends Entity> individual = entity.listIterator(); individual.hasNext();) {
            Entity current = individual.next();
            Vector chromosome = (Vector) current.getCandidateSolution();

            if (this.getMutationProbability().getParameter() >= this.getRandomDistribution().getRandomNumber()) {
                for (int i = 0; i < chromosome.size(); i++) {
                    double value = this.getOperatorStrategy().evaluate(chromosome.doubleValueOf(i), this.getRandomDistribution().getRandomNumber(minStrategy.getParameter(), maxStrategy.getParameter()));
                    chromosome.setReal(i, value);
                }
            }
        }
    }
}
