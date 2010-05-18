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

import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.ProportionalControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * @author Andries Engelbrecht
 * @author Gary Pampara
 */
public class CauchyMutationStrategy extends MutationStrategy {

    private static final long serialVersionUID = 8576581034467137106L;
    private double location;
    private ControlParameter scaleStrategy;

    public CauchyMutationStrategy() {
        super();
        this.location = 0;
        this.scaleStrategy = new ProportionalControlParameter();
    }

    public CauchyMutationStrategy(CauchyMutationStrategy copy) {
        super(copy);
        this.location = copy.location;
        this.scaleStrategy = copy.scaleStrategy.getClone();
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

            if (this.getMutationProbability().getParameter() >= this.getRandomNumber().getUniform()) {
                for (int i = 0; i < chromosome.getDimension(); i++) {
                    Numeric element = chromosome.get(i);
                    double scale = this.scaleStrategy.getParameter(element.getBounds().getLowerBound(), element.getBounds().getUpperBound());
                    double value = this.getOperatorStrategy().evaluate(chromosome.getReal(i), this.getRandomNumber().getCauchy(this.location, scale));

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
