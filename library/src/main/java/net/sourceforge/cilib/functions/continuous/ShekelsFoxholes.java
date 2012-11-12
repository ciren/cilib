/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * ShekelsFoxholes function.
 *
 * <p>Title: CILib</p>
 * <p>Description: CILib (Computational Intelligence Library)</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 *
 * R(-65.536, 65.536)^2
 * Minimum: 0.9980038
 * 
 * @version 1.0
 */
public class ShekelsFoxholes implements ContinuousFunction {

    private static final long serialVersionUID = 1986501892056164693L;
    private double[][] a = new double[2][25];

    public ShekelsFoxholes() {
        int index = 0;
        for (int j = -32; j <= 32; j += 16) {
            for (int i = -32; i <= 32; i += 16) {
                a[0][index] = i;
                a[1][index] = j;
                index++;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double resultI = 0.0;
        for (int i = 1; i <= 25; i++) {
            double resultJ = 0.0;
            for (int j = 0; j < 2; j++) {
                resultJ += Math.pow(input.doubleValueOf(j) - a[j][i-1], 6);
            }
            resultJ = i + resultJ;
            resultI += 1 / resultJ;
        }
        resultI = 0.002 + resultI;

        return 1.0 / resultI;
    }
}
