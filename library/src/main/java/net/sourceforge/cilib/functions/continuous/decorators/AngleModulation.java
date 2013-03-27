/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.decorators;

import com.google.common.base.Preconditions;
import net.sourceforge.cilib.problem.AbstractProblem;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.StringBasedDomainRegistry;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Decorator pattern class to wrap a normal function to perform Angle Modulation.
 * <p>
 * The default values for angle modulation are:
 * <ul>
 *   <li>domain = "R(-1.0,1.0)^4"</li>
 *   <li>precision = 3</li>
 * </ul>
 */
public class AngleModulation extends AbstractProblem {

    private static final long serialVersionUID = -3492262439415251355L;
    private int precision;
    private int bitsPerDimension;
    private int numberOfGenerators;
    private double lowerBound;
    private double upperBound;
    private FunctionOptimisationProblem delegate;

    public AngleModulation() {
        precision = 3;
        bitsPerDimension = 0;
        numberOfGenerators = 1;
        domainRegistry = new StringBasedDomainRegistry();

        domainRegistry.setDomainString("R(-1.0:1.0)^4");
    }

    public AngleModulation(AngleModulation copy) {
        this.precision = copy.precision;
        this.bitsPerDimension = copy.bitsPerDimension;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AngleModulation getClone() {
        return new AngleModulation(this);
    }

    /**
     *
     * @return the precision.
     */
    public int getPrecision() {
        return this.precision;
    }

    /**
     *
     * @param precision
     */
    public void setPrecision(int precision) {
        if (precision < 0) {
            throw new ArithmeticException("Precision values must be >= 0");
        }

        this.precision = precision;
    }

    public void setProblem(FunctionOptimisationProblem problem) {
        this.delegate = problem;
        bitsPerDimension = getRequiredNumberOfBits(delegate.getDomain());
    }

    public FunctionOptimisationProblem getProblem() {
        return delegate;
    }

    /**
     * TODO: This needs to use an API for domain string manipulation
     * @param domain    the domain used to calculate the number of bits.
     * @return          the required number of bits for the specified domain.
     */
    public int getRequiredNumberOfBits(DomainRegistry domain) {
        if (domain.getDomainString().contains("B")) {
            return 1;
        } else {
            String range = domain.getDomainString();

            // now remove all the irrelevant details from the domain provided
            range = range.substring(range.indexOf('(') + 1);
            range = range.substring(0, range.indexOf(')'));

            String[] bounds = range.split(":");
            lowerBound = Double.valueOf(bounds[0]).doubleValue();
            upperBound = Double.valueOf(bounds[1]).doubleValue();

            double greaterRange = Math.abs(lowerBound) + Math.abs(upperBound);
            double expandedRange = greaterRange * Math.pow(10, getPrecision());

            return Double.valueOf(Math.ceil(Math.log(expandedRange) / Math.log(2.0))).intValue();
        }
    }

    /**
     * TODO: Change this to use something better than a string
     * TODO: complete this method
     *
     * @param x
     * @param dimensionBitNumber
     * @return the generated bit string.
     */
    public String generateBitString(Vector x, int dimensionBitNumber) {
        StringBuilder str = new StringBuilder();
        double a, b, c, d, result;

        for (int g = 0; g < numberOfGenerators; g++) {
            a = x.doubleValueOf(4 * g);
            b = x.doubleValueOf((4 * g) + 1);
            c = x.doubleValueOf((4 * g) + 2);
            d = x.doubleValueOf((4 * g) + 3);

            for (int i = 0; i < dimensionBitNumber * delegate.getDomain().getDimension() / numberOfGenerators; i++) {
                result = Math.sin(2 * Math.PI * (i - a) * b * Math.cos(2 * Math.PI * c * (i - a))) + d;

                if (result > 0.0) {
                    str.append('1');
                } else {
                    str.append('0');
                }
            }
        }

        return str.toString();
    }

    /**
     *
     * @param bits
     * @param dimensionBits
     * @return the decoded bit string.
     */
    public Vector decodeBitString(String bits, int dimensionBits) {
        Vector.Builder vector = Vector.newBuilder();

        for (int i = 0; i < bits.length();) {
            double tmp = valueOf(bits, i, i + dimensionBits);
            tmp = transform(tmp);

            vector.add(tmp);
            i += dimensionBits;
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
        Preconditions.checkState(bitsPerDimension * delegate.getDomain().getDimension() % bitString.length() == 0,
                "A " + bitsPerDimension * delegate.getDomain().getDimension()
                + "-dimensional problem cannot be equally split into " + numberOfGenerators + " parts.");

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
        tmp <<= this.bitsPerDimension - 1;
        result -= tmp;
        result /= Math.pow(10, getPrecision());

        return result;
    }

    @Override
    public DomainRegistry getDomain() {
        return domainRegistry;
    }

    public void setNumberOfGenerators(int numberOfGenerators) {
        this.numberOfGenerators = numberOfGenerators;

        int dimensions = 4 * numberOfGenerators;
        domainRegistry.setDomainString("R(-1.0:1.0)^" + String.valueOf(dimensions));
    }

    @Override
    protected Fitness calculateFitness(Type solution) {
        String bitString = generateBitString((Vector) solution, bitsPerDimension);
        Vector expandedVector = decodeBitString(bitString, bitsPerDimension);
        return delegate.getFitness(expandedVector);
    }
}
