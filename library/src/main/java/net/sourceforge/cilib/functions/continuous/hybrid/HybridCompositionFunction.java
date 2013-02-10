/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.hybrid;

import com.google.common.collect.Lists;
import java.util.List;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Implementation to create hybrid composition functions from the CEC2005 benchmark functions.
 * <p>
 * Reference:
 * </p>
 * <p>
 * Suganthan, P. N., Hansen, N., Liang, J. J., Deb, K., Chen, Y., Auger, A., and Tiwari, S. (2005).
 * Problem Definitions and Evaluation Criteria for the CEC 2005 Special Session on Real-Parameter Optimization.
 * Natural Computing, 1-50. Available at: http://vg.perso.eisti.fr/These/Papiers/Bibli2/CEC05.pdf.
 * </p>
 */
public class HybridCompositionFunction implements ContinuousFunction {

    private List<SingleFunction> functions;
    private double scaleConstant;

    public HybridCompositionFunction() {
        this.functions = Lists.<SingleFunction>newArrayList();
        this.scaleConstant = 2000.0;
    }

    @Override
    public Double apply(Vector input) {
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
