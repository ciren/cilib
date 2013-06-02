/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.am;

import fj.F;
import fj.data.Array;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.functions.continuous.am.samplingstrategies.SamplingStrategy;
import net.sourceforge.cilib.functions.continuous.am.samplingstrategies.StandardAMSamplingStrategy;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * A bit generating function that wraps a normal function
 * to perform angle modulation.
 */
public class AMBitGeneratingFunction extends F<Vector, String> {
    protected FunctionOptimisationProblem delegate;
    protected ContinuousFunction modulationFunction;
    protected SamplingStrategy sampler;
    protected int bitsPerDimension, precision;

    public AMBitGeneratingFunction() {
        this.modulationFunction = new StandardAngleModulationFunction();
        this.sampler = new StandardAMSamplingStrategy();
        this.bitsPerDimension = 0;
        this.precision = 3;
    }
    
    @Override
    public String f(Vector input) {
        StringBuilder str = new StringBuilder();
        
        int bits = bitsPerDimension * delegate.getDomain().getDimension();
        Array samplePoints = sampler.getSamplePoints(bits, input);
        
        Array<Double> sampleValues = samplePoints.map(modulationFunction);

        for (Double d : sampleValues) {
            str.append(d.doubleValue() > 0.0 ? '1' : '0');
        }
        
        return str.toString();
    }

    public void setDelegate(FunctionOptimisationProblem problem) {
        this.delegate = problem;
        bitsPerDimension = calculateBitsPerDimension(delegate.getDomain());
    }

    public FunctionOptimisationProblem getDelegate() {
        return delegate;
    }
    
    public void setModulationFunction(ContinuousFunction f) {
        this.modulationFunction = f;
    }

    public void setSampler(SamplingStrategy s) {
        this.sampler = s;
    }

    public void setPrecision(int p) {
        if (p < 0) {
            throw new ArithmeticException("Precision values must be >= 0");
        }
        
        this.precision = p;
    }

    public int getPrecision() {
        return precision;
    }

    public int getBitsPerDimension() {
        return bitsPerDimension;
    }
    
    /**
     * @param domain    the domain used to calculate the number of bits.
     * @return          the required number of bits for the specified domain.
     */
    public int calculateBitsPerDimension(DomainRegistry domain) {
        if (domain.getDomainString().contains("B")) {
            return 1;
        } else {   
            Vector bounds = (Vector) domain.getBuiltRepresentation();
            double lowerBound = bounds.boundsOf(0).getLowerBound();
            double upperBound = bounds.boundsOf(1).getUpperBound();

            double greaterRange = Math.abs(upperBound - lowerBound);
            double expandedRange = greaterRange * Math.pow(10, precision);

            return Double.valueOf(Math.ceil(Math.log(expandedRange) / Math.log(2.0))).intValue();
        }
    }
}
