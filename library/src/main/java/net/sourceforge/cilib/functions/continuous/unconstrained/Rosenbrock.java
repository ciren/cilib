/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.unconstrained;

import com.google.common.base.Preconditions;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.functions.Gradient;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * <p>Generalised Rosenbrock function.</p>
 *This is a minimisation problem
 * <p><b>Reference:</b> X. Yao, Y. Liu, G. Liu, <i>Evolutionary Programming
 * Made Faster</i>,  IEEE Transactions on Evolutionary Computation,
 * 3(2):82--102, 1999</p>
 *
 * <p>
 * Characteristics:
 * <ul>
 * <li>Unimodal</li>
 * <li>Non Separable</li>
 * <li>Continuous</li>
 * <li>Regular</li>
 * </ul>
 * </p>
 *
 * f(x) = 0; x = 1
 *
 * x e [-2.048,2.048]
 *
 * R(-2.048, 2.048)^30
 *
 */
public class Rosenbrock extends ContinuousFunction implements Gradient {

    private static final long serialVersionUID = -5850480295351224196L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double f(Vector input) {
        double tmp = 0;

        for (int i = 0; i < input.size()-1; ++i) {
            double a = input.doubleValueOf(i);
            double b = input.doubleValueOf(i+1);

            tmp += ((100.0 * (b - a * a) * (b - a * a)) + ((a - 1.0) * (a - 1.0)));
        }

        return tmp;
    }
    
    
    public Double df(Vector input, int i) {
        Preconditions.checkArgument(input.size() > 0, "Rosenbrock function is defined for 1 dimension at least");
        double result = 0.0;
        int n = input.size();
        
        if (i == 1)
        {
            result = (2.0*input.doubleValueOf(0)-2.0)-(400.0*input.doubleValueOf(0)*input.doubleValueOf(1))+(400.0*Math.pow(input.doubleValueOf(0), 3));
        }
        else if (i >= 2 && i <= n-1)
        {
            double value1=(2.0*input.doubleValueOf(i-1)-2.0) + ((200.0*input.doubleValueOf(i-1))-(200.0*input.doubleValueOf(i-2)*input.doubleValueOf(i-2)));
	    double value2=(400.0*input.doubleValueOf(i-1)*input.doubleValueOf(i))-(400.0*Math.pow(input.doubleValueOf(i-1), 3));
	    result = value1-value2;
        }
        else if (i == n)
        {
            result = 200.0*input.doubleValueOf(n-1)-200.0*input.doubleValueOf(n-2)*input.doubleValueOf(n-2);
        }


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
