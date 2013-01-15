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
 * TODO: check the shifting of this function. The optimum is set to o=[o_1, o_2,...,o_n] where o_i \in [-pi, pi].
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
public class SchwefelProblem2_13 implements ContinuousFunction {
    private double[] optimum;
    private double[][] m_a;
    private double[][] m_b;

    private double[] m_A;
    private double[] m_B;
    
    private boolean initialised;
    
    public SchwefelProblem2_13() {
        this.initialised = false;
    }
    
    public void setMatrices(int dimensions) {
        optimum = new double[dimensions];
        m_a = new double[dimensions][dimensions];
        m_b = new double[dimensions][dimensions];

        m_A = new double[dimensions];
        m_B = new double[dimensions];

        for (int i = 0; i < dimensions; i++) {
            for (int j = 0; j < dimensions; j++) {
                m_a[i][j] = Rand.nextInt(201) - 100;
                m_b[i][j] = Rand.nextInt(201) - 100;
            }
            
            optimum[i] = Rand.nextDouble() * 2 * Math.PI - Math.PI;
        }

        for (int i = 0; i < dimensions; i++) {
            m_A[i] = 0.0;
            for (int j = 0; j < dimensions; j++) {
                m_A[i] += (m_a[i][j] * Math.sin(optimum[j]) + m_b[i][j] * Math.cos(optimum[j]));
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
        
        double sum = 0.0;

        for (int i = 0; i < input.size(); i++) {
            m_B[i] = 0.0;
            for (int j = 0; j < input.size(); j++) {
                m_B[i] += (m_a[i][j] * Math.sin(input.doubleValueOf(j)) + m_b[i][j] * Math.cos(input.doubleValueOf(j)));
            }

            double temp = m_A[i] - m_B[i];
            sum += temp * temp;
        }

        return sum;
    }
}
