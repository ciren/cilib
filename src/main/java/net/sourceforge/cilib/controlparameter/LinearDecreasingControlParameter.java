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
package net.sourceforge.cilib.controlparameter;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.type.parser.DomainParser;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * A {@linkplain net.sourceforge.cilib.controlparameter.ControlParameter control parameter}
 * that is defined to update itself in an linearly decreasing manner. The rate of change
 * within the parameter is based on the percentage complete of the running
 * {@linkplain net.sourceforge.cilib.algorithm.Algorithm algorithm}.
 *
 * @author Gary Pampara
 */
public class LinearDecreasingControlParameter implements BoundedControlParameter {

    private static final long serialVersionUID = -7213083955334884076L;
    private Real parameter;

    /**
     * Create a new instance of {@code LinearDecreasingControlParameter}.
     */
    public LinearDecreasingControlParameter() {
        parameter = Real.valueOf(0.0);
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public LinearDecreasingControlParameter(LinearDecreasingControlParameter copy) {
        this.parameter = copy.parameter.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LinearDecreasingControlParameter getClone() {
        return new LinearDecreasingControlParameter(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateParameter() {
        double result = getUpperBound() - (getUpperBound() - getLowerBound()) * AbstractAlgorithm.get().getPercentageComplete();
        parameter = Real.valueOf(result, parameter.getBounds());

        if (this.parameter.doubleValue() < this.parameter.getBounds().getLowerBound()) {
            this.parameter = Real.valueOf(this.parameter.getBounds().getLowerBound(), parameter.getBounds());
        } else if (this.parameter.doubleValue() > this.parameter.getBounds().getUpperBound()) {
            this.parameter = Real.valueOf(this.parameter.getBounds().getUpperBound(), parameter.getBounds());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUpperBound(double value) {
        this.parameter = Real.valueOf(value, new Bounds(parameter.getBounds().getLowerBound(), value));
    }

    @Override
    public double getParameter() {
        return parameter.doubleValue();
    }

    @Override
    public double getParameter(double min, double max) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setParameter(double value) {
        this.parameter = Real.valueOf(value, parameter.getBounds());
    }

    @Override
    public double getLowerBound() {
        return parameter.getBounds().getLowerBound();
    }

    @Override
    public void setLowerBound(double lower) {
        parameter = Real.valueOf(lower, new Bounds(lower, parameter.getBounds().getUpperBound()));
    }

    @Override
    public double getUpperBound() {
        return parameter.getBounds().getUpperBound();
    }

    @Override
    public void setRange(String range) {
        Vector v = (Vector) DomainParser.parse(range);

        if (v.size() != 1) {
            throw new RuntimeException("Range incorrect in BoundedUpdateStrategy! Please correct");
        } else {
            this.parameter = (Real) v.get(0);
        }
    }
}
