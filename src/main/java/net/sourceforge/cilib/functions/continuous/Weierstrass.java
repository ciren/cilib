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
package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * f(x*)=0, x*=[0,0,...,0] in [-0.5, 0.5]^n
 * <p>
 * Reference:
 * </p>
 * <p>
 * Suganthan, P. N., Hansen, N., Liang, J. J., Deb, K., Chen, Y., Auger, A., and Tiwari, S. (2005).
 * Problem Definitions and Evaluation Criteria for the CEC 2005 Special Session on Real-Parameter Optimization.
 * Natural Computing, 1-50. Available at: http://vg.perso.eisti.fr/These/Papiers/Bibli2/CEC05.pdf.
 * </p>
 */
public class Weierstrass implements ContinuousFunction {
    private double a;
    private double b;
    private int kMax;
    private double constant;

    /**
     * Default constructor.
     */
    public Weierstrass() {
        this.a = 0.5;
        this.b = 3.0;
        this.kMax = 20;
        
        computeConstant();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double tmp = 0;
        
        for (int i = 0; i < input.size(); i++) {
            for(int k = 0; k <= kMax; k++) {
                tmp += Math.pow(a, k) * Math.cos(2 * Math.PI * Math.pow(b, k) * (input.doubleValueOf(i) + 0.5));
            }
        }

        return tmp - input.size() * constant;
    }

    /**
     * Sets the kMax parameter.
     * @param kMax 
     */
    public void setkMax(int kMax) {
        this.kMax = kMax;
        computeConstant();
    }

    /**
     * Sets the b parameter.
     * @param b 
     */
    public void setB(double b) {
        this.b = b;
        computeConstant();
    }

    /**
     * Sets the a parameter.
     * @param a 
     */
    public void setA(double a) {
        this.a = a;
        computeConstant();
    }
    
    /**
     * This computes the second term which is a constant so it can be stored
     * separately and only needs to be computed when the function parameters 
     * change.
     */
    private void computeConstant() {
        double tmp = 0.0;
        
        for(int k = 0; k <= kMax; k++) {
            tmp += Math.pow(a, k) * Math.cos(2 * Math.PI * Math.pow(b, k) * 0.5);
        }
        
        constant = tmp;
    }
}