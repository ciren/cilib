/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.am.samplingstrategies;

import com.google.common.base.Preconditions;
import fj.data.Array;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Calculates sampling points at fixed intervals in the range [min, max).
 */
public class MinMaxAMSamplingStrategy extends AbstractSamplingStrategy {
    private double min = Double.NaN;
    private double max = Double.NaN;

    public MinMaxAMSamplingStrategy() {
    }

    public MinMaxAMSamplingStrategy(double min, double max) {
        this.min = min;
        this.max = max;
    }
        
    @Override
    public Array<Vector> getSamplePoints(int samples, Vector values) {
        Preconditions.checkState(!((Double)min).isNaN(), "No minimum value is specified");
        Preconditions.checkState(!((Double)max).isNaN(), "No maximum value is specified");
        
        double interval = Math.abs(max - min) / samples;
        Vector[] points = new Vector[samples];
        
        int count = 0;        
        for (double i = min; count < samples; i += interval) {
            points[count++] = constructInputVector(i, values);
        }
        
        return Array.array(points);
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
