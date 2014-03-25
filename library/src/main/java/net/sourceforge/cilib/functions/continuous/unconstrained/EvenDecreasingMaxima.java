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
 * Multimodal2 function.
 *This is a maximisation problem
 * Minimum: 0.0
 * R(0, 1)^1
 *
 */
public class EvenDecreasingMaxima extends ContinuousFunction implements Gradient {

    private static final long serialVersionUID = -5046586719830749372L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double f(Vector input) {
        double sum = 0.0;
        for (int i = 0; i < input.size(); i++) {
            double x1 = Math.pow(Math.sin(5.0 * Math.PI * input.doubleValueOf(i)), 6.0);
            double exp1 = Math.pow((input.doubleValueOf(i) - 0.1) / 0.8, 2.0);
            double exp2 = -2.0 * Math.log(2.0)* exp1;
            double x2 = Math.exp(exp2);
            sum += x1 * x2;
        }
        return sum;
    }
    
    public Double df(Vector input, int i){
	
        double result=0.0;
	
	double exp= Math.exp(-2.0 * Math.log(2)*(Math.pow((input.doubleValueOf(i-1) - 0.1) / 0.8, 2.0)));
        double sin5=Math.pow(Math.sin(5.0 * Math.PI *input.doubleValueOf(i-1)),5);
	double cos = 30*Math.PI*Math.cos(5.0*Math.PI*input.doubleValueOf(i-1));  	
	double value=(4*(input.doubleValueOf(i-1)-0.1)*Math.log(2))/ (0.8 * 0.8);
	double sin6=Math.pow(Math.sin(5.0 * Math.PI *input.doubleValueOf(i-1)),6);
	
    result=(exp*sin5*cos)-(exp*value*sin6);
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
