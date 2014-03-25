/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.unconstrained;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.functions.NichingFunction;
import net.sourceforge.cilib.type.types.container.Vector;

import com.google.common.base.Preconditions;

import net.sourceforge.cilib.functions.Gradient;
import net.sourceforge.cilib.type.types.Numeric;

/**
 * The Modified Himmelblau function.
 * This is a maximisation problem
 * <p>Title: CILib</p>
 * <p>Description: CILib (Computational Intelligence Library)</p>
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * Characteristics:
 * <ul>
 * <li>Only defined for 2 dimensions</li>
 * <li>Multimodal</li>
 * <li>Continuous</li>
 * </ul>
 *
 * R(-6, 6)^2
 *
 * @version 1.0
 */
public class ModifiedHimmelblau extends ContinuousFunction implements Gradient, NichingFunction {

    private static final long serialVersionUID = 7323733640884766707L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double f(Vector input) {
        Preconditions.checkArgument(input.size() >= 2, "Himmelblau function is defined for atleast 2 dimensions");
        double result=0.0;
        
        for (int i = 0; i < input.size()-1; ++i) {
         double x = input.doubleValueOf(i);
         double y = input.doubleValueOf(i+1);
         result+= 200.0 - (Math.pow((x * x + y - 11.0), 2.0) + Math.pow((x + y * y - 7.0), 2.0));
        
        }
        return result;        
    }
    
      public Double df(Vector input, int i) {
        Preconditions.checkArgument(input.size() >= 2, "Himmelblau function is defined for atleast 2 dimensions");
        double result = 0.0;
        int n = input.size();
        
        if (i == 1){
        
         double x = input.doubleValueOf(i-1);
         double y = input.doubleValueOf(i); 
         result= -4.0 * x * (x * x + y - 11.0) - 2.0 * (x + y * y - 7.0);
         
        }
        else if (i >= 2 && i <= n-1){
            
        double w = input.doubleValueOf(i-2);
        double x = input.doubleValueOf(i-1);
        double y = input.doubleValueOf(i);  
            
        double value1=-2.0 * (w * w + x- 11.0) - 4.0 * x * (w + x * x - 7.0);
        double value2=-4.0 * x * (x * x + y - 11.0) - 2.0 * (x + y * y - 7.0);
       
	result = value1+value2;            
       
        }
        else if (i == n){
            
        double x = input.doubleValueOf(n-2);
        double y = input.doubleValueOf(n-1);
        
        result =  -2.0 * (x * x + y - 11.0) - 4.0 * y * (x + y * y - 7.0);
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
   
    public Vector getGradientVector (Vector input)
    {
        Vector.Builder vectorBuilder = Vector.newBuilder();
        
        for (int i = 1; i <= input.size(); ++i){
            vectorBuilder.add(this.df(input,i));
        }
        
        return vectorBuilder.build();
    }
      
   
    @Override
    public double getNicheRadius() {
            return 0.01;
    }
}
