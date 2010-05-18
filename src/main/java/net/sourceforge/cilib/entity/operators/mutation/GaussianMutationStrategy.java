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
package net.sourceforge.cilib.entity.operators.mutation;

import java.util.List;
import java.util.ListIterator;

import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.ProportionalControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.math.random.GaussianDistribution;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFuction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * @author Andries Engelbrecht
 * @author Gary Pampara
 */
public class GaussianMutationStrategy extends MutationStrategy {

    private static final long serialVersionUID = -4219155909474892419L;
    private double mean;
    private ControlParameter deviationStrategy;
    private ProbabilityDistributionFuction gaussian;

    public GaussianMutationStrategy() {
        super();
        this.mean = 0;
        this.deviationStrategy = new ProportionalControlParameter();
        this.gaussian = new GaussianDistribution();
    }

    public GaussianMutationStrategy(GaussianMutationStrategy copy) {
        super(copy);
        this.mean = copy.mean;
        this.deviationStrategy = copy.deviationStrategy.getClone();
        this.gaussian = copy.gaussian;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GaussianMutationStrategy getClone() {
        return new GaussianMutationStrategy(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mutate(List<? extends Entity> entity) {
        for (ListIterator<? extends Entity> individual = entity.listIterator(); individual.hasNext();) {
            Entity current = individual.next();
            Vector chromosome = (Vector) current.getCandidateSolution();

            for (int i = 0; i < chromosome.size(); i++) {
                double random = this.getRandomDistribution().getRandomNumber();
                if (random <= this.getMutationProbability().getParameter()) {
                    double deviation = this.gaussian.getRandomNumber();
                    double value = this.getOperatorStrategy().evaluate(chromosome.doubleValueOf(i), this.gaussian.getRandomNumber(this.mean, deviation));

                    chromosome.setReal(i, value);
                }
            }
        }
    }

    public ControlParameter getDeviationStrategy() {
        return deviationStrategy;
    }

    public void setDeviationStrategy(ControlParameter deviationStrategy) {
        this.deviationStrategy = deviationStrategy;
    }

    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }
}
