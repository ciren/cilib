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
package net.sourceforge.cilib.functions.continuous.unconstrained;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * <p>
 * TODO: check the shifting of this function. The optimum is set to o=[o_1, o_2,...,o_n] where o_i \in [-100, 100].
 * So it's always random but the optimum is always 0.
 * <br/>
 * f(x*)=0
 * </p>
 * <p>
 * Reference:
 * </p>
 * <p>
 * Suganthan, P. N., Hansen, N., Liang, J. J., Deb, K., Chen, Y., Auger, A., and Tiwari, S. (2005).
 * Problem Definitions and Evaluation Criteria for the CEC 2005 Special Session on Real-Parameter Optimization.
 * Natural Computing, 1-50. Available at: http://vg.perso.eisti.fr/These/Papiers/Bibli2/CEC05.pdf.
 * </p>
 */
public class SchwefelProblem2_6 implements ContinuousFunction {
    private Vector optimum;
    private double[][] m_A;

    private double[] m_B;
    private double[] m_z;
    
    private boolean initialized;
    
    public SchwefelProblem2_6() {
        this.initialized = false;
    }
    
    public void setMatrices(int dimensions) {
        RandomProvider random = new MersenneTwister();
        
        Vector.Builder oBuilder = Vector.newBuilder();
        m_A = new double[dimensions][dimensions];

        m_B = new double[dimensions];
        m_z = new double[dimensions];

        for (int i = 0 ; i < dimensions ; i ++) {
            if ((i+1) <= Math.ceil(dimensions / 4.0)) {
                oBuilder.add(-100);
            } else if ((i+1) >= Math.floor((3.0 * dimensions) / 4.0)) {
                oBuilder.add(100);
            } else {
                oBuilder.add(random.nextInt(201) - 100);
            }
        }
        
        optimum = oBuilder.build();
        
        for (int i = 0 ; i < dimensions ; i ++) {
            for (int j = 0 ; j < dimensions ; j ++) {
                m_A[i][j] = random.nextInt(1001) - 500;
            }
        }
        
        aTimesX(m_B, m_A, optimum);
    }
    
    /**
     * Multiplies Matrix A by Vector x and stores it in result.
     * @param result
     * @param A
     * @param x 
     */
    private void aTimesX(double[] result, double[][] A, Vector x) {
        for (int i = 0 ; i < result.length ; i ++) {
            result[i] = 0.0;
            
            for (int j = 0 ; j < result.length ; j ++) {
                result[i] += (A[i][j] * x.doubleValueOf(j));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        if(!initialized) {
            setMatrices(input.size());
            initialized = true;
        }
        
        double max = Double.NEGATIVE_INFINITY;

        aTimesX(m_z, m_A, input);

        for (int i = 0 ; i < input.size() ; i ++) {
            double temp = Math.abs(m_z[i] - m_B[i]);
            
            if (max < temp) {
                max = temp;
            }
        }

        return max;
    }
}
