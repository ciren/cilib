/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.unconstrained;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.functions.Gradient;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Generalised Griewank function.
 *This is a minimisation problem
 * <p>
 * Characteristics:
 * <ul>
 * <li>Multi-modal</li>
 * <li>Non-separable</li>
 * <li>Regular</li>
 * </ul>
 *
 * f(x) = 0; x = (0,0,...,0);
 * x_i e (-600,600)
 *
 * R(-600, 600)^30
 *
 */
public class Griewank extends ContinuousFunction implements Gradient{

    private static final long serialVersionUID = 1095225532651577254L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double f(Vector input) {
        double sumsq = 0;
        double prod = 1;
        
        for (int i = 0; i < input.size(); ++i) {
            sumsq += input.doubleValueOf(i) * input.doubleValueOf(i);
            prod *= Math.cos(input.doubleValueOf(i) / Math.sqrt(i+1));
        }
        return 1 + sumsq * (1.0 / 4000.0) - prod;
    }
    
    public Double df(Vector input, int i){
    double result=0.0;
	
	double value1=(1.0/2000.0)*input.doubleValueOf(i-1);
	double value2=(Math.sin(input.doubleValueOf(i-1)/Math.sqrt(i))*(1.0/Math.sqrt(i)));
                       
	double value3=1;
	for (int j=1;j<input.size();j++) {
           value3*=(Math.cos(input.doubleValueOf(j-1)/Math.sqrt(j))*(1.0/Math.sqrt(j)));
        }
	double currentInputcos=(Math.cos(input.doubleValueOf(i-1)/Math.sqrt(i))*(1.0/Math.sqrt(i)));
        value3=(value3/currentInputcos);
   
        result=value1+(value2*value3);
    
        return result;
    }
    
    public double getAverageGradientVector ( Vector x)
    {
        
        double sum = 0;
        
        for (int i = 1; i <= x.size(); ++i)
        {
            sum += this.df(x,i);
        }
           
        return sum/x.size();
    }
    
    public double getGradientVectorLength (Vector x)
    {
        double sumsqrt = 0;
        
        for (int i = 1; i <= x.size(); ++i)
        {
            sumsqrt += this.df(x,i)*this.df(x,i);
        }
        
        return Math.sqrt(sumsqrt);
    }
    
    public Vector getGradientVector (Vector x)
    {
        Vector.Builder vectorBuilder = Vector.newBuilder();
        
        for (int i = 1; i <= x.size(); ++i)
        {
             vectorBuilder.add(this.df(x,i));
        }
        
        return vectorBuilder.build();
    }
}
