/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.unconstrained;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * X. Li, A. Engelbrecht, and M.G. Epitropakis, ``Benchmark Functions for CEC'2013 Special Session and Competition 
 * on Niching Methods for Multimodal Function Optimization'', Technical Report, Evolutionary Computation and Machine 
 * Learning Group, RMIT University, Australia, 2013
 */
public class RastriginNiching implements ContinuousFunction {
    
    private List<Double> k;

    public RastriginNiching() {
        this.k = new ArrayList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        Preconditions.checkState(k.size() >= input.size(), "Not enough k values for the given input vector.");
        double sum = 0;
        for (int i = 0; i < input.size(); ++i) {
            sum += 10 + 9 * Math.cos(2 * Math.PI * input.doubleValueOf(i) * k.get(i));
        }
        return -sum;
    }
    
    public void setK(Double k) {
        this.k.add(k);
    }
}
