/*
 * RotatedFunction.java
 *
 * Created on February 24, 2003, 12:19 PM
 */

package net.sourceforge.cilib.Functions;

import net.sourceforge.cilib.Problem.*;

/**
 *
 * @author  espeer
 */
public class RotatedFunction extends Function {
    
    /** Creates a new instance of RotatedFunction */
    public RotatedFunction() {
        super(0, null, 0.0);
        function = null;
    }
    
    public void setFunction(Function function) {
        this.function = function;
        
    }
    
    public double getMinimum() {
        return function.getMinimum();
    }

    public void setDomain(Domain domain) {
        function.setDomain(domain);
    }
    
    public Domain getDomain() {
        return function.getDomain();
    }
    
    public void setDimension(int Dimension) {
        function.setDimension(dimension);
    }
    
    public int getDimension() {
        return function.getDimension();
    }
    
    /** Each function must provide an implementation which returns the function value
     * at the given position. The length of the position array should be the same
     * as the function dimension.
     *
     * @param x The position
     *
     */
    public double evaluate(double[] x) {
        return function.evaluate(x);
    }

    private Function function;
    private double[][] rotation;
}

