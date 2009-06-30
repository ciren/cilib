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
package net.sourceforge.cilib.algorithm.population;

import net.sourceforge.cilib.entity.operators.CompositeOperator;
import net.sourceforge.cilib.problem.boundaryconstraint.BoundaryConstraint;
import net.sourceforge.cilib.problem.boundaryconstraint.UnconstrainedBoundary;

/**
 * Generic {@code IterationStrategy} class for all population based algorithms. Each and every {@link IterationStrategy}
 * will have a pipeline available to it in order to specify the needed operators that will be
 * required to be executed during the iteration. For the implementing classes, a default
 * pipeline is constructed and made available, this can (as with everything within CIlib) be overridden.
 *
 * @author Gary Pampara
 *
 * @param <E> The {@linkplain PopulationBasedAlgorithm} type.
 */
public abstract class AbstractIterationStrategy<E extends PopulationBasedAlgorithm> implements IterationStrategy<E> {
    private static final long serialVersionUID = -2922555178733552167L;
    protected BoundaryConstraint boundaryConstraint;
    protected CompositeOperator operatorPipeline;

    /**
     * Create an instance of the {@linkplain IterationStrategy}.
     */
    public AbstractIterationStrategy() {
        this.boundaryConstraint = new UnconstrainedBoundary();
        this.operatorPipeline = new CompositeOperator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract AbstractIterationStrategy<E> getClone();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void performIteration(E algorithm);

    /**
     * Get the currently associated {@linkplain BoundaryConstraint}.
     * @return The current {@linkplain BoundaryConstraint}.
     */
    public BoundaryConstraint getBoundaryConstraint() {
        return boundaryConstraint;
    }

    /**
     * Set the {@linkplain BoundaryConstraint} to maintain within this {@linkplain IterationStrategy}.
     * @param boundaryConstraint The {@linkplain BoundaryConstraint} to set.
     */
    public void setBoundaryConstraint(BoundaryConstraint boundaryConstraint) {
        this.boundaryConstraint = boundaryConstraint;
    }

    /**
     * Get the <code>List&lt;Operator&gt;</code> that represents the sequence
     * of operators to be performed within the current <tt>IterationStrategy</tt>.
     * @return The operator pipeline <code>List&lt;Operator&gt;</code>
     */
    public CompositeOperator getOperatorPipeline() {
        return operatorPipeline;
    }

    /**
     * Set the pipeline to be used within the current <tt>IterationStrategy</tt>.
     * @param operatorPipeline The pipeline to be used.
     */
    public void setOperatorPipeline(CompositeOperator operatorPipeline) {
        this.operatorPipeline = operatorPipeline;
    }

}
