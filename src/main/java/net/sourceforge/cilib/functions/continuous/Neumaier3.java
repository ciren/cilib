/*
 * Neumaier.java
 *
 * Created on June 4, 2003, 1:56 PM
 *
 * 
 * Copyright (C) 2003 - 2006 
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
 *   
 */

package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.Vector;

/**
 *
 * @author  engel
 */
public class Neumaier3 extends ContinuousFunction {
    
    /** Creates a new instance of Neumaier */
    public Neumaier3() {
    	// TODO: Fix this constraint
        // constraint.add(new ContentValidator(new NeumaierValidator()));
        setDomain("R(-900, 900)^30");
    }
    
    public Object getMinimum() {
        double dimension = getDimension();
        return new Double((dimension * (dimension + 4) * (dimension - 1)) / 6);
    }
    
    /** Each function must provide an implementation which returns the function value
     * at the given position. The length of the position array should be the same
     * as the function dimension.
     *
     * @param x The position
     *
     */
    public double evaluate(Vector x) {
        double tmp1 = 0;
        double tmp2 = 0;
        for (int i = 0; i < getDimension(); ++i) {
            tmp1 += (x.getReal(i) - 1) * (x.getReal(i) - 1);
        }
        for (int i = 1; i < getDimension(); ++i) {
            tmp2 += x.getReal(i) * x.getReal(i - 1);
        }
        return tmp1 - tmp2;
    }
    
    /*private class NeumaierValidator implements Validator {
        public void validate(DomainComponent component) {
            double dimension = component.getDimension();
            for (int i = 0; i < dimension; ++i) {
                Quantitative tmp = (Quantitative) component.getComponent(i);
                if (tmp.getLowerBound().intValue() != (- dimension * dimension)) {
                    throw new ValidationFailedException("Expected lower bound = -" + String.valueOf(dimension) + "^2");
                }
                if (tmp.getUpperBound().intValue() != (dimension * dimension)) {
                    throw new ValidationFailedException("Expected upper bound = " + String.valueOf(dimension) + "^2");
                }
            }
        }
    }*/ 
    
}
