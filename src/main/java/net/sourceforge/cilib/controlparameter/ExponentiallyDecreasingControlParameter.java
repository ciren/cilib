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
package net.sourceforge.cilib.controlparameter;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.Algorithm;

/**
 * A {@linkplain net.sourceforge.cilib.controlparameter.ControlParameter control parameter}
 * that is defined to update itself in an exponentially decreasing manner. The rate of change
 * within the parameter is based on the percentage complete of the running
 * {@linkplain net.sourceforge.cilib.algorithm.Algorithm algorithm}.
 */
public class ExponentiallyDecreasingControlParameter extends BoundedControlParameter {
    private static final long serialVersionUID = 2703195595193249266L;

    /**
     * Create a new instance of {@code ExponentiallyDecreasingControlParameter}.
     */
    public ExponentiallyDecreasingControlParameter() {
        super();
    }

    /**
     * Copy constructor.
     * @param copy The instance to copy.
     */
    public ExponentiallyDecreasingControlParameter(ExponentiallyDecreasingControlParameter copy) {
        super(copy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExponentiallyDecreasingControlParameter getClone() {
        return new ExponentiallyDecreasingControlParameter(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {
        double result = Math.exp((-1) * AbstractAlgorithm.get().getPercentageComplete());
        this.parameter.setReal(result);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUpperBound(double value) {
        super.setUpperBound(value);
        this.parameter.setReal(value);
    }

}
