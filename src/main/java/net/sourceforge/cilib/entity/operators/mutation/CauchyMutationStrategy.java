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
package net.sourceforge.cilib.entity.operators.mutation;

import java.util.List;

import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.ProportionalControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.topologies.TopologyHolder;
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

    /**
     * {@inheritDoc}
     */
    @Override
//    public void performOperation(Topology<? extends Entity> topology, Topology<Entity> offspring) {
    public void performOperation(TopologyHolder holder) {
//        this.mutate(holder.getOffpsring());
        throw new UnsupportedOperationException("This needs to be fixed");
    }


}
