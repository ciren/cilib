/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.discrete;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.functions.Function;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Class to convert a binary vector into a continuous vector for optimisation of
 * continuous problems by a binary optimiser.
 * <p>
 * This still needs some experimental work though, to verify that it is working
 *
 * TODO: This doesn't actually make sense... should rather be a problem that
 * does the needed mapping between spaces.
 */
public class BinaryAdapter implements ContinuousFunction {
    private static final long serialVersionUID = -329657439970469569L;

    private Function<Vector, Number> function;
    private int bitsPerDimension;
    private int precision;

    /**
     * Constructor.
     */
    public BinaryAdapter() {
        bitsPerDimension = 1;
        precision = 0;
    }

    /**
     * Evaluate the {@link Vector} by decoding the binary vector into a
     * continuous vector and evaluate the results by feeding the result into
     * the wrapped function.
     *
     * @param input the bit vector to evaluate.
     * @return The result of the evaluation.
     */
    @Override
    public Double apply(Vector input) {
        Vector decodedVector = this.decodeBitString(input);
        return function.apply(decodedVector).doubleValue();
    }

//    /**
//     *
//     */
//    @Override
//    public Double getMinimum() {
//        return function.getMinimum().doubleValue();
//    }
//
//    /**
//     *
//     */
//    @Override
//    public Double getMaximum() {
//        return function.getMaximum().doubleValue();
//    }

    /**
     * @return Returns the bitsPerDimension.
     */
    public int getBitsPerDimension() {
        return bitsPerDimension;
    }

    /**
     * @param bitsPerDimension The bitsPerDimension to set.
     */
    public void setBitsPerDimension(int bitsPerDimension) {
        if (bitsPerDimension < 0)
            throw new RuntimeException("Cannot set the amount of bits in BinaryAdapter to anything < 0");

        this.bitsPerDimension = bitsPerDimension;
    }


    /**
     * @return Returns the precision.
     */
    public int getPrecision() {
        return precision;
    }


    /**
     * @param precision The precision to set.
     */
    public void setPrecision(int precision) {
        this.precision = precision;
    }


    /**
     * @return Returns the function.
     */
    public Function<Vector, Number> getFunction() {
        return function;
    }

    /**
     * @param function The function to set.
     */
    public void setFunction(Function<Vector, ? extends Number> function) {
        this.function = (Function<Vector, Number>) function;
    }


    /**
     * @param bits
     * @return a {@linkplain Vector} representing the decoded bits.
     */
    public Vector decodeBitString(Vector bits) {
        Vector.Builder vector = Vector.newBuilder();

        for (int i = 0; i < bits.size();) {
            double tmp = valueOf(bits, i, i+this.bitsPerDimension);
            tmp = transform(tmp);

            vector.add(tmp);
            i += this.bitsPerDimension;
        }

        return vector.build();
    }


    /**
     *
     * @param vector
     * @param i
     * @param j
     */
    public double valueOf(Vector vector, int i, int j) {
        double result = 0.0;
        int n = 1;

        for (int counter = j-1; counter >= i; counter--) {
            if (vector.booleanValueOf(counter)) {
                result += n;
            }

            n = n*2;
        }

        return result;
    }

    private double transform(double number) {
        double result = number;

        if (this.precision > 0) {
            int tmp = 1;
            tmp <<= this.bitsPerDimension-1;
            result -= tmp;
            result /= Math.pow(10, this.precision);
        }

        return result;
    }

//    @Override
//    public void setDomain(String representation) {
//        if (!representation.matches("^B\\^.*"))
//            throw new RuntimeException("BinaryAdapter can only accept domain strings in the form: B^?\nWhere ? is the size of the dimension");
//
//        super.setDomain(representation);
//    }
}
