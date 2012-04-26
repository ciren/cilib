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
package net.sourceforge.cilib.functions.continuous.hybrid;

import com.google.common.collect.Lists;
import java.util.List;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Implementation to create hybrid composition functions from the CEC2005 benhmark functions.
 * <p>
 * Reference:
 * </p>
 * <p>
 * Suganthan, P. N., Hansen, N., Liang, J. J., Deb, K., Chen, Y., Auger, A., and Tiwari, S. (2005).
 * Problem Definitions and Evaluation Criteria for the CEC 2005 Special Session on Real-Parameter Optimization.
 * Natural Computing, 1-50. Available at: http://vg.perso.eisti.fr/These/Papiers/Bibli2/CEC05.pdf.
 * </p>
 * @author filipe
 */
public class HybridCompositionFunction implements ContinuousFunction {
    
    private List<SingleFunction> functions;
    private double scaleConstant;
    private boolean done = false;
    
    public HybridCompositionFunction() {
        this.functions = Lists.<SingleFunction>newArrayList();
        this.scaleConstant = 2000.0;
    }

    @Override
    public Double apply(Vector input) {
        input = Vector.of(1,1,1,1,1);
        int nDims = input.size();

        // Get the raw weights
        double wMax = Double.NEGATIVE_INFINITY;
        double wSum = 0.0;
        for (SingleFunction f : functions) {
            f.shift(input);
            double sumSqr = Math.pow(f.getShifted().norm(), 2);
            
            f.setWeight(Math.exp(-1.0 * sumSqr / (2.0 * nDims * f.getSigma() * f.getSigma())));
            
            if (wMax < f.getWeight())
                wMax = f.getWeight();

            wSum += f.getWeight();
        }

        // Modify the weights
        double w1mMaxPow = 1.0 - Math.pow(wMax, 10.0);
        for (SingleFunction f : functions) {
            if (f.getWeight() != wMax) {
                f.setWeight(f.getWeight() * w1mMaxPow);
            }

            f.setWeight(f.getWeight() / wSum);
        }

        double sumF = 0.0;
        for (SingleFunction f : functions) {
            sumF += f.getWeight() * (scaleConstant * f.apply(input) + f.getBias());
        }

        if (!done) {
            done = true;
            for (SingleFunction f : functions) {
                System.out.println("w " + f.getWeight() + " sc " + scaleConstant + " fm " + f.getfMax() + " b " + f.getBias() + " af " + f.apply(input));
            }
        }
        
        return sumF;
    }
    
    /**
     * Adds a function to be composed.
     * @param function 
     */
    public void addFunction(SingleFunction function) {
        functions.add(function);
    }

    /**
     * Sets the scaling constant.
     * @param scaleConstant The new scaling constant.
     */
    public void setScaleConstant(double scaleConstant) {
        this.scaleConstant = scaleConstant;
    }

    /**
     * Gets the scaling constant.
     * @return The scaling constant.
     */
    public double getScaleConstant() {
        return scaleConstant;
    }
}
