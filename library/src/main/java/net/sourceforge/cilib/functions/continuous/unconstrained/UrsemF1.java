/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.unconstrained;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

import com.google.common.base.Preconditions;
import net.sourceforge.cilib.functions.Gradient;

/**
 * UrsemF1 function.
 *This is a Maximisation problem
 * R(-2.5, 3)^2
 * Minimum: 4.81681
 *
 */
public class UrsemF1 extends ContinuousFunction  implements Gradient{

    private static final long serialVersionUID = -2595919942608678319L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double f(Vector input) {
        Preconditions.checkArgument(input.size() == 2, "UrsemF1 function is only defined for 2 dimensions");

        double x = input.doubleValueOf(0);
        double y = input.doubleValueOf(1);

        return Math.sin(2.0 * x - 0.5 * Math.PI) + 3.0 * Math.cos(y) + 0.5 * x;
    }
    
    
    public Double df(Vector input, int i) {
        Preconditions.checkArgument(input.size() == 2, "UrsemF1 function is only defined for 2 dimensions");

        double x = input.doubleValueOf(0);
        double y = input.doubleValueOf(1);
        double res = 0.0;
        
        if (i==1) {
            res = 2.0*Math.cos(2.0 * x - 0.5 * Math.PI)+0.5;
        }
        else{
            res = -3.0*Math.sin(y);
        }

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
