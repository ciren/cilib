/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.sampling;

import com.google.common.base.Preconditions;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Samples a continuous function at fixed intervals in the range [min, max).
 */
public class MinMaxFunctionSampler extends ContinuousFunctionSampler {
    private double min = Double.NaN;
    private double max = Double.NaN;

    public MinMaxFunctionSampler() {
    }

    public MinMaxFunctionSampler(double min, double max) {
        this.min = min;
        this.max = max;
    }
        
    public Vector getSamples(ContinuousFunction f, Vector values, int samples) {
        Preconditions.checkState(!((Double)min).isNaN(), "No minimum value is specified");
        Preconditions.checkState(!((Double)max).isNaN(), "No maximum value is specified");
        
        double interval = Math.abs(max - min) / samples;
        Double[] sampleValues = new Double[samples];
        
        int count = 0;        
        for (double i = min; count < samples; i += interval) {
            Vector inputVector = constructInputVector(i, values);
            sampleValues[count++] = f.apply(inputVector);
        }
        
        return Vector.of(sampleValues);
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }
}
