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

/**
 * A {@linkplain net.sourceforge.cilib.controlparameter.ControlParameter control parameter}
 * that is defined to return a proportional value.
 */
public class ProportionalControlParameter implements ControlParameter {

    private static final long serialVersionUID = 8415953407107514281L;
    private double proportion;

    public ProportionalControlParameter() {
        this.proportion = 0.1;
    }

    public ProportionalControlParameter(double proportion) {
        this.proportion = proportion;
    }

    public ProportionalControlParameter(ProportionalControlParameter copy) {
        this.proportion = copy.proportion;
    }

    @Override
    public ProportionalControlParameter getClone() {
        return new ProportionalControlParameter(this);
    }

    @Override
    public double getParameter() {
        return this.proportion;
    }

    @Override
    public double getParameter(double min, double max) {
        return this.proportion * (max - min);
    }

    public double getProportion() {
        return proportion;
    }

    public void setProportion(double proportion) {
        this.proportion = proportion;
    }
    
    @Override
    public void updateParameter(double value) {
        
    }
    
}
