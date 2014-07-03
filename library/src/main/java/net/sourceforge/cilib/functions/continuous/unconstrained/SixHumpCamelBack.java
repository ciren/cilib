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

/**
 * This is a minimisation problem
 * InvertedSixHumpCamelBack function.
 *
 * <p>Title: CILib</p>
 * <p>Description: CILib (Computational Intelligence Library)</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 *
 * <p>
 * Characteristics:
 * <ul>
 * <li>Only defined for 2 dimensions</li>
 * <li>Multimodal</li>
 * <li>Continuous</li>
 * <li>Non Separable</li>
 * </ul>
 * </p>
 *
 * f(x) = -1.0316; x = (-0.0898, 0.1726); x = (0.0898, -0.1726)
 * x_1 e [-3, 3]; x_2 e [-2, 2]
 *
 * R(-3,3),R(-2,2)
 *
 * @version 1.0
 */
public class SixHumpCamelBack extends ContinuousFunction implements Gradient, NichingFunction {

    private static final long serialVersionUID = -3834640752316926216L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double f(Vector input) {
        Preconditions.checkArgument(input.size() >= 2, "SixHumpCamelBack function is defined for atleast 2 dimensions");

        double result=0.0;
        
        for (int i = 0; i < input.size()-1; ++i) {
            
         double x = input.doubleValueOf(i);
         double y = input.doubleValueOf(i+1);
         
         result+= ((4.0*x*x)-(2.1*Math.pow(x,4))+(Math.pow(x,6)/3.0)+(x*y)-(4.0*y*y)+(4.0*Math.pow(y,4)));
        
        }
        return result;  
    }
    
      public Double df(Vector input, int i) {
        Preconditions.checkArgument(input.size() >= 2, "SixHumpCamelBack function is defined for atleast 2 dimensions");
        double result = 0.0;
        int n = input.size();
        
        if (i == 1){
        
         double x = input.doubleValueOf(i-1);
         double y = input.doubleValueOf(i); 
         
         result= 8.0*x-8.4*Math.pow(x,3)+2.0*Math.pow(x,5)+y;
         
        }
        else if (i >= 2 && i <= n-1){
            
        double w = input.doubleValueOf(i-2);
        double x = input.doubleValueOf(i-1);
        double y = input.doubleValueOf(i);             
           
	result = w +  2.0*Math.pow(x,5)+ 7.6*Math.pow(x,3) +y;    
       
        }
        else if (i == n){
            
        double x = input.doubleValueOf(n-2);
        double y = input.doubleValueOf(n-1);
        
        result = x-8.0*y+16.0*Math.pow(y,3);
        }
        return result;
    }
         
    public double getAverageGradientVector ( Vector x)
    {
        double sum = 0;
        
        for (int i = 1; i <= x.size(); ++i){
            sum += this.df(x,i);
        }
           
        return sum/x.size();
    }
    
    public double getGradientVectorLength (Vector x){
       double sumsqrt = 0;
       for (int i = 1; i <= x.size(); ++i){
           sumsqrt += this.df(x,i)*this.df(x,i);
        }       
        return Math.sqrt(sumsqrt);
    }
    
    public Vector getGradientVector (Vector x){
       Vector.Builder vectorBuilder = Vector.newBuilder();
       for (int i = 1; i <= x.size(); ++i){
          vectorBuilder.add(this.df(x,i));
        }       
        return vectorBuilder.build();
    }

     @Override
     public double getNicheRadius() {
	return 0.5;
     }
     
}
