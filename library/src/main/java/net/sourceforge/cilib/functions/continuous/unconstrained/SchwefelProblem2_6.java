/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.unconstrained;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.math.random.generator.Rand;
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
    
    private boolean initialised;
    
    public SchwefelProblem2_6() {
        this.initialised = false;
    }
    
    public void setMatrices(int dimensions) {
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
                oBuilder.add(Rand.nextInt(201) - 100);
            }
        }
        
        optimum = oBuilder.build();
        
        for (int i = 0 ; i < dimensions ; i ++) {
            for (int j = 0 ; j < dimensions ; j ++) {
                m_A[i][j] = Rand.nextInt(1001) - 500;
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
        if(!initialised) {
            setMatrices(input.size());
            initialised = true;
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
