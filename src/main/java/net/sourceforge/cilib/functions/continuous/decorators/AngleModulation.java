/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.functions.continuous.decorators;

import net.sourceforge.cilib.functions.AbstractFunction;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.functions.Function;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * A Decorator pattern class to wrap a normal function to perform Angle Modulation.
 *
 * The deault values for angle modulation are:
 * <p>
 * <ul>
 *   <li>domain = "R(-1.0,1.0)^4"</li>
 *   <li>precision = 3</li>
 * </ul>
 * </p>
 *
 * @author Gary Pampara
 */
public class AngleModulation extends ContinuousFunction {
    private static final long serialVersionUID = -3492262439415251355L;
    private int precision;
    private int requiredBits;
    private double lowerBound;
    private double upperBound;
    private AbstractFunction function;

    public AngleModulation() {
        setDomain("R(-1.0,1.0)^4");
        precision = 3;
        requiredBits = 0;
    }

    public AngleModulation(AngleModulation copy) {
        setDomain(copy.getDomain());
        this.precision = copy.precision;
        this.requiredBits = copy.requiredBits;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AngleModulation getClone() {
        return new AngleModulation(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double getMinimum() {
        Number n = (Number) function.getMinimum();
        return n.doubleValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double getMaximum() {
        Number n = (Number) function.getMaximum();
        return n.doubleValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double evaluate(Vector input) {
        String solution = generateBitString(input);
        Vector expandedVector = decodeBitString(solution);
        Number result = (Number) function.evaluate(expandedVector);
        return result.doubleValue();
    }

    /**
     *
     * @return
     */
    public int getPrecision() {
        return this.precision;
    }

    /**
     *
     * @param precision
     */
    public void setPrecision(int precision) {
        if (precision < 0)
            throw new ArithmeticException("Presicion values must be >= 0");

        this.precision = precision;
    }

    /**
     *
     * @return
     */
    public Function getFunction() {
        return this.function;
    }

    /**
     *
     * @param decoratedFunciton
     */
    public void setFunction(Function decoratedFunciton) {
        this.function = (AbstractFunction) decoratedFunciton;
        requiredBits = getRequiredNumberOfBits(function.getDomainRegistry());
    }

    /**
     *
     * @param domain
     * @return
     */
    public int getRequiredNumberOfBits(DomainRegistry domain) {
        if (domain.getDomainString().contains("B")) {
            return 1;
        }
        else {
            String range = domain.getDomainString();

            // now remove all the irrelevant details from the domain provided
            range = range.substring(range.indexOf('(')+1);
            range = range.substring(0, range.indexOf(')'));

            String [] bounds = range.split(",");
            lowerBound = Double.valueOf(bounds[0]).doubleValue();
            upperBound = Double.valueOf(bounds[1]).doubleValue();

            double greaterRange = Math.abs(lowerBound) + Math.abs(upperBound);
            double expandedRange = greaterRange * Math.pow(10, getPrecision());

            return Double.valueOf(Math.ceil(Math.log(expandedRange) / Math.log(2.0))).intValue();
        }

    }

    /**
     * @TODO: Change this to use something better than a string
     * @TODO: complete this method
     *
     * @param x
     * @return
     */
    public String generateBitString(Vector x) {
        double a = x.getReal(0);
        double b = x.getReal(1);
        double c = x.getReal(2);
        double d = x.getReal(3);

        StringBuilder str = new StringBuilder();

        for (int i = 0; i < requiredBits*function.getDimension(); i++) {
            double result =  Math.sin(2*Math.PI*(i-a) * b * Math.cos(2*Math.PI*c*(i-a))) + d;

            if (result > 0.0)
                str.append('1');
            else
                str.append('0');
        }

        return str.toString();
    }

    /**
     *
     * @param bits
     * @return
     */
    private Vector decodeBitString(String bits) {
        Vector vector = new Vector();

        for (int i = 0; i < bits.length();) {
            double tmp = valueOf(bits, i, i+requiredBits);
            tmp = transform(tmp);

            vector.append(new Real(tmp));
            i += requiredBits;
        }

        return vector;
    }

    /**
     * Determine the numeric value of the given bitstring.
     *
     * TODO: Move this into a class that will make sense.
     *
     * @param bitString The bitsting as a string
     * @param startIndex The starting index
     * @param endIndex The ending index
     * @return The value of the bitstring
     */
    public double valueOf(String bitString, int startIndex, int endIndex) {
        double result = 0.0;

        String substring = bitString.substring(startIndex, endIndex);
        result = Integer.valueOf(substring, 2).intValue();

        return result;
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
     * @return
     */
    private double transform(double number) {
        double result = number;

        int tmp = 1;
        tmp <<= this.requiredBits-1;
        result -= tmp;
        result /= Math.pow(10, getPrecision());

        return result;
    }

}
