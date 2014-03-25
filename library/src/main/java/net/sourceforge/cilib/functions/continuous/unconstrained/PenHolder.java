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
 * This is a minimisation problem
 * 4 globals peaks
 * 
 */
public class PenHolder extends ContinuousFunction implements Gradient{
    
    
    /**
     * {@inheritDoc}
     */
    
    @Override
    public Double f(Vector input) {
        Preconditions.checkArgument(input.size() >= 2, "PenHolder function is only defined for more than 2 dimensions");
    
        double sum = 0.0;
        double prod=1;
        for (int j = 0; j < input.size(); j++) {
            double x=input.doubleValueOf(j);
            sum += x*x;
            prod*=(Math.cos(x));
        }
      
        double expValue=Math.exp(Math.abs(1.0-(Math.sqrt(sum))/(Math.PI)));        
        double result=-Math.exp(-Math.abs(Math.pow((prod*expValue),-1)));        
        return result;
    }
    
    public Double df(Vector input, int i){
    double result=0.0;
    double summation=0.0;
    double product=1.0;
       
    for (int j = 0; j < input.size(); j++) {
            summation+= input.doubleValueOf(j)*input.doubleValueOf(j);
            product*= Math.cos(input.doubleValueOf(j));            
        }
    
    double value=(-(Math.abs(Math.sqrt(summation)-Math.PI)/Math.PI));    
    double exp1=Math.exp((-Math.exp(value))/Math.abs(product)+value);     
    double exp2=(Math.sqrt(summation))*((Math.PI*Math.sin(input.doubleValueOf(i-1))*Math.abs(Math.sqrt(summation)-Math.PI))-(input.doubleValueOf(i-1)*Math.cos(input.doubleValueOf(i-1))));       
    double exp3=Math.PI*input.doubleValueOf(i-1)*Math.cos(input.doubleValueOf(i-1));    
    double denominator=Math.PI*Math.abs(product)*(Math.sqrt(summation))*Math.cos(input.doubleValueOf(i-1))*(Math.sqrt(summation)-Math.PI);
    
    result=(exp1*(exp2+exp3))/denominator;
	
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
    
