/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.unconstrained;

import fj.F;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.functions.continuous.decorators.IllConditionedFunctionDecorator;
import net.sourceforge.cilib.functions.continuous.decorators.RotatedFunctionDecorator;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;

/**
 * Lunasec bi-Rastrigin function as specified in CEC2013.
 *
 * Note: This function requires a horizontal shift.
 *
 * <p>
 * Reference:
 * </p>
 * <p>
 * Liang, J. J., B. Y. Qu, and P. N. Suganthan.
 * "Problem Definitions and Evaluation Criteria for the CEC 2013 Special Session
 * on Real-Parameter Optimization." (2013).
 * </p>
 *
 */
public class LunacekBiRastrigin implements ContinuousFunction {
    private double mu0;
    private double d;
    private double horizontalScale;
    private ControlParameter horizontalShift;
    private ContinuousFunction rastriginDecorator;

    public LunacekBiRastrigin() {
        this.mu0 = 2.5;
        this.d = 1.0;
        this.horizontalScale = 0.1;
        this.horizontalShift = ConstantControlParameter.of(0.0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        Vector xHat = input.map(new F<Numeric, Numeric>() {
            @Override
            public Numeric f(Numeric a) {
                double y = horizontalScale * (a.doubleValue() - horizontalShift.getParameter());
                double value = 2 * y;
                if (horizontalShift.getParameter() < 0) {
                    value *= -1;
                }
                return Real.valueOf(value + mu0);
            }
        });

        Vector xHatLessMu0 = xHat.map(new F<Numeric, Numeric>() {
            @Override
            public Numeric f(Numeric a) {
                return Real.valueOf(a.doubleValue() - mu0);
            }
        });

        double s = calculateS(input.size());
        double mu1 = calculateMu1(s);

        double minSum1 = 0, minSum2 = 0;
        for (int i = 0; i < xHat.size(); i++) {
            minSum1 += Math.pow(xHat.doubleValueOf(i) - mu0, 2);
            minSum2 += Math.pow(xHat.doubleValueOf(i) - mu1, 2);
        }
        minSum2 *= s;
        minSum2 += this.d * input.size();

        double biRastriginTerm = rastriginDecorator.apply(xHatLessMu0);

        return Math.min(minSum1, minSum2) + biRastriginTerm;
    }

    /**
     * Calculate s by using the size of the input vector
     */
    private double calculateS(int size) {
        return 1.0 - (1.0 / (2 * Math.sqrt(size + 20) - 8.2));
    }

    /**
     * Calculates mu1 by using mu, d and s.
     */
    private double calculateMu1(double s) {
        return -Math.sqrt((this.mu0 * this.mu0 - this.d) / s);
    }

    /**
     * Sets the mu0 parameter.
     * @param mu0
     */
    public void setMu0(double mu0) {
        this.mu0 = mu0;
    }

    /**
     * Sets the d parameter.
     * @param d
     */
    public void setD(double d) {
        this.d = d;
    }

    /**
     * Sets the horizontal shift.
     * @param horizontalShift
     */
    public void setHorizontalShift(ControlParameter horizontalShift) {
        this.horizontalShift = horizontalShift;
    }

    /**
     * Sets the horizontal scale.
     * @param horizontalScale
     */
    public void setHorizontalScale(Double horizontalScale) {
        this.horizontalScale = horizontalScale;
    }

    /**
     * Sets the decorator to be used with the Rastrigin part of the function.
     * @param rastriginDecorator
     */
    public void setRastriginDecorator(ContinuousFunction rastriginDecorator) {
        this.rastriginDecorator = rastriginDecorator;
    }
}
