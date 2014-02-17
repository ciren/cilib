/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem;

import net.sourceforge.cilib.functions.continuous.am.AMBitGeneratingFunction;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.type.StringBasedDomainRegistry;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Angle modulation problem class. 
 */
public class AngleModulationProblem extends AbstractProblem {
    private static final long serialVersionUID = -3492262439415251355L;
    private AMBitGeneratingFunction generatingFunction;

    public AngleModulationProblem() {
        domainRegistry = new StringBasedDomainRegistry();
        domainRegistry.setDomainString("R(-1.0:1.0)^4");
        this.generatingFunction = new AMBitGeneratingFunction();
    }

    public AngleModulationProblem(AngleModulationProblem copy) {
        this.generatingFunction = copy.generatingFunction;
        this.domainRegistry = copy.domainRegistry.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AngleModulationProblem getClone() {
        return new AngleModulationProblem(this);
    }

    /**
     * @param bits
     * @param bitsPerDimension
     * @return the decoded bit string.
     */
    public Vector decodeBitString(String bits, int bitsPerDimension) {
        Vector.Builder vector = Vector.newBuilder();
        
        for (int i = 0; i < bits.length(); i += bitsPerDimension) {
            double tmp = valueOf(bits, i, i + bitsPerDimension);
            
            if (bitsPerDimension > 1) {
                tmp = transform(tmp);
            }

            vector.add(tmp);
        }

        return vector.build();
    }

    /**
     * Determine the numeric value of the given bitstring.
     *
     * TODO: Move this into a class that will make sense.
     *
     * @param bitString The bitstring as a string
     * @param startIndex The starting index
     * @param endIndex The ending index
     * @return The value of the bitstring
     */
    public double valueOf(String bitString, int startIndex, int endIndex) {
        String substring = bitString.substring(startIndex, endIndex);
        
        return Integer.valueOf(substring, 2).intValue();
    }

    public double valueOf(String bitString, int index) {
        String substring = bitString.substring(index);
        return Integer.valueOf(substring, 2).intValue();
    }

    public double valueOf(String bitString) {
        return Integer.valueOf(bitString, 2).intValue();
    }

    /**
     *
     * @param number
     * @return the transformed number.
     */
    private double transform(double number) {
        double result = number;

        int tmp = 1;
        tmp <<= generatingFunction.getBitsPerDimension() - 1;
        result -= tmp;
        result /= Math.pow(10, generatingFunction.getPrecision());

        return result;
    }
    
    public void setGeneratingFunction(AMBitGeneratingFunction f) {
        this.generatingFunction = f;
        
        StringBuilder builder = new StringBuilder();
        Vector bounds = (Vector) this.domainRegistry.getBuiltRepresentation();
        double lowerBound = bounds.boundsOf(0).getLowerBound();
        double upperBound = bounds.boundsOf(1).getUpperBound();
        int dimensions = domainRegistry.getDimension();
        
        builder.append("R(");
        builder.append(String.valueOf(lowerBound));
        builder.append(":");
        builder.append(String.valueOf(upperBound));
        builder.append(")^");
        builder.append(String.valueOf(dimensions));
        
        domainRegistry.setDomainString(builder.toString());
    }
    
    public AMBitGeneratingFunction getGeneratingFunction() {
        return generatingFunction;
    }

    @Override
    protected Fitness calculateFitness(Type solution) {
        String bitString = generatingFunction.f((Vector) solution);
        Vector expandedVector = decodeBitString(bitString, generatingFunction.getBitsPerDimension());
        return generatingFunction.getDelegate().getFitness(expandedVector);
    }
}
