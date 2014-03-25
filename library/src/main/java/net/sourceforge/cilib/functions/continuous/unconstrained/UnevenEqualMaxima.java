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
 * This is a maximisation problem
 *
 * Minimum: 0.0
 * R(0, 1)^1
 *
 */
public class UnevenEqualMaxima extends ContinuousFunction implements Gradient {

    private static final long serialVersionUID = 3687474318232647359L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double f(Vector input) {
        double sum = 0.0;
        for (int i = 0; i < input.size(); ++i) {
            double x = Math.pow(Math.sin(5.0 * Math.PI * (Math.pow(input.doubleValueOf(i), 0.75) - 0.05)), 6.0);
            sum += x;
        }
        return sum;
    }
    
    public Double df(Vector input, int i) {
      double res = 22.5*Math.PI*Math.pow(Math.sin(5.0 * Math.PI * (Math.pow(input.doubleValueOf(i-1), 0.75) - 0.05)), 5.0)*Math.cos(5.0 * Math.PI * (Math.pow(input.doubleValueOf(i-1), 0.75) - 0.05))*(Math.pow(input.doubleValueOf(i-1), -0.25));
       
        return res;
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
