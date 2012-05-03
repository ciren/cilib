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
package net.sourceforge.cilib.functions.continuous.decorators;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.type.types.container.Vector.Function;

/**
 * A decorator that makes a function non-continuous.
 * <pre>
 *              x_j          if |x_j|&lt;0.5
 * x_j = round(2 * x_j) / 2  otherwise
 * 
 * where
 *            a-1  if x&lt;=0 && b&gt;=0.5
 * round(x) =  a   if b&lt;0.5
 *            a+1  if x&gt;0 && b&gt;=0.5
 * </pre>
 * <p>
 * Reference:
 * </p>
 * <p>
 * Suganthan, P. N., Hansen, N., Liang, J. J., Deb, K., Chen, Y., Auger, A., and Tiwari, S. (2005).
 * Problem Definitions and Evaluation Criteria for the CEC 2005 Special Session on Real-Parameter Optimization.
 * Natural Computing, 1-50. Available at: http://vg.perso.eisti.fr/These/Papiers/Bibli2/CEC05.pdf.
 * </p>
 */
public class RoundingFunctionDecorator implements ContinuousFunction {
    private ContinuousFunction function;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        return function.apply(input.map(new Function<Numeric, Numeric>() {
            @Override
            public Numeric apply(Numeric f) {
                if (Math.abs(f.doubleValue()) < 0.5) {
                    return f;
                }
                
                return Real.valueOf(round(2 * f.doubleValue()) / 2.0);
            }            
        }));
    }
    
    /**
     * The round function.
     * <pre>
     *            a-1  if x&lt;=0 && b&gt;=0.5
     * round(x) =  a   if b&lt;0.5
     *            a+1  if x&gt;0 && b&gt;=0.5
     * </pre>
     * @param f
     * @return 
     */
    private int round(double f) {
        double b = f - (int) f;
        if (f <= 0.0 && b >= 0.5) {
            return (int) f - 1;
        } else if(b < 0.5) {
            return (int) f;
        } else {
            return (int) f + 1;
        }
    }
    
        /**
     * Get the function that is decorated.
     * @return Returns the decorated function.
     */
    public ContinuousFunction getFunction() {
        return function;
    }

    /**
     * Set the decorated function.
     * @param function The function to decorate.
     */
    public void setFunction(ContinuousFunction function) {
        this.function = function;
    }
}
