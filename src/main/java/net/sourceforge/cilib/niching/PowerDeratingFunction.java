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
package net.sourceforge.cilib.niching;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * R^1
 */
public class PowerDeratingFunction implements DeratingFunction {
    private ControlParameter radius;
    private ControlParameter alpha;
    
    public PowerDeratingFunction() {
        this.radius = ConstantControlParameter.of(0.25);
        this.alpha = ConstantControlParameter.of(2.0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        if (input.size() != 1) {
            throw new RuntimeException("Derating functions may only be used in one dimension.");
        }

        if (input.doubleValueOf(0) >= radius.getParameter()) {
            return 1.0;
        }

        return input.doubleValueOf(0) >= radius.getParameter() ? 
                1.0 : Math.pow(input.doubleValueOf(0) / radius.getParameter(), alpha.getParameter());
    }
    
    /**
     * Set the value of the radius.
     * @param radius The value to set.
     */
    public void setRadius(ControlParameter radius) {
        this.radius = radius;
    }

    /**
     * Get the value of the radius.
     * @return The value of the radius.
     */
    @Override
    public double getRadius() {
        return radius.getParameter();
    }

    /**
     * Set the value of <code>alpha</code>.
     * @param alpha The value to set.
     */
    public void setAlpha(ControlParameter alpha) {
        this.alpha = alpha;
    }

    /**
     * Get the value of alpha.
     * @return The value of alpha.
     */
    public double getAlpha() {
        return alpha.getParameter();
    }
}
