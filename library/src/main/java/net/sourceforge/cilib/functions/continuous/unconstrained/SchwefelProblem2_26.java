/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.unconstrained;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.functions.Gradient;
import net.sourceforge.cilib.functions.NichingFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * SchwefelProblem 2_26.
 *This is a minimisation problem
 * Characteristics:
 *
 * f(x) = -12569.5, x = (420.9687,...,420.9687);
 *
 * x e [-500,500]
 *
 * R(-500, 500)^30
 *
 */
// TODO: Check discontinuous / continuous
public class SchwefelProblem2_26 extends ContinuousFunction implements Gradient,NichingFunction  {

    private static final long serialVersionUID = -4483598483574144341L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double f(Vector input) {
        double sum = 0.0;

        for (int i = 0; i < input.size(); i++) {
            sum += input.doubleValueOf(i)*Math.sin(Math.sqrt(Math.abs(input.doubleValueOf(i))));
        }
        return -sum;
    }
    
    public Double df(Vector input, int i){
     double result=0.0;
	
     double value1=Math.sin(Math.sqrt(Math.abs(input.doubleValueOf(i-1))));        
     double value2=input.doubleValueOf(i-1)*input.doubleValueOf(i-1)*Math.cos(Math.sqrt(Math.abs(input.doubleValueOf(i-1))));
     double denom=2*Math.pow(Math.abs(input.doubleValueOf(i-1)),1.5);
        
     result=value1 +(value2/denom); 
    
     return -result;
     
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

    @Override
    public double getNicheRadius() {
        return 0.01;
    }
    
    
}

