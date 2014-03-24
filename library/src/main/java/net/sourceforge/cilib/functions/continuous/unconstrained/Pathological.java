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
 * The Pathological function.
 *This is a minimisation problem
 * <p>Title: CILib</p>
 * <p>Description: CILib (Computational Intelligence Library)</p>
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * Characteristics:
 * <ul>
 * <li>Only defined for more than or equal to 2 dimensions</li>
 * </ul>
 *
 * R(-11,11) ^ n
 *
 * @version 1.0
 */
public class Pathological extends ContinuousFunction implements Gradient{

    private static final long serialVersionUID = 7323733640884766707L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double f(Vector input) {
        Preconditions.checkArgument(input.size() >= 2, "Pathological function is only defined for more than 2 dimensions");
        double summation = 0.0;
        double n = input.size();
		
        for (int i = 1 ; i <= n-1 ; ++i)
        {
            double x=input.doubleValueOf(i-1);
            double y=input.doubleValueOf(i);
            double numerator=-0.5+Math.pow(Math.sin(Math.sqrt(100*x*x+y*y)),2);
            double denom = (1+(0.001*(Math.pow((x-y), 4))));
            summation += 0.5+(numerator/denom);
        }
        return summation;
    }
    
    
    public Double df(Vector input, int i){
    
        double result=0.0;
    
    
    if (i==1) {
        double w = input.doubleValueOf(0);
        double x = input.doubleValueOf(1);
                
	double exp1= 0.004*Math.pow((w-x),3)*(Math.pow(Math.sin(Math.sqrt(100*w*w+x*x)),2)-0.5);
        double exp2= 200*w*Math.cos(Math.sqrt(100*w*w+x*x))*Math.sin(Math.sqrt(100*w*w+x*x));
	double den1=(1+(0.001*(Math.pow((w-x), 4))));
	double den2=(Math.sqrt(100*w*w+x*x))*den1;	
	        
        result= (exp2/den2)-(exp1/(den1*den1));
    }
    
    else if(i==input.size()) {
        double x = input.doubleValueOf(input.size()-2);
        double y = input.doubleValueOf(input.size()-1);
        
        double exp1= 0.004*Math.pow((x-y),3)*(Math.pow(Math.sin(Math.sqrt(100*x*x+y*y)),2)-0.5);
	double exp2=2*y*Math.cos(Math.sqrt(100*x*x+y*y))*Math.sin(Math.sqrt(100*x*x+y*y));
        double den1=(1+(0.001*(Math.pow((x-y), 4))));
        double den2=(Math.sqrt(100*x*x+y*y))*den1;		
	result= (exp1/(den1*den1))+(exp2/den2);
        
    }
    
    else {
        
        double w = input.doubleValueOf(i-2);
        double x = input.doubleValueOf(i-1);
        double y = input.doubleValueOf(i);
        
        double exp1=-0.004*Math.pow((x-y),3)*(Math.pow(Math.sin(Math.sqrt(100*x*x+y*y)),2)-0.5);
	double den1=(1+(0.001*(Math.pow((x-y), 4))));
		
	double exp2=200*x*Math.cos(Math.sqrt(100*x*x+y*y))*Math.sin(Math.sqrt(100*x*x+y*y));
	double den2=(Math.sqrt(100*x*x+y*y))*den1;
		
	double exp3=0.004*Math.pow((w-x),3)*(Math.pow(Math.sin(Math.sqrt(100*w*w+x*x)),2)-0.5);
	double den3=(1+(0.001*(Math.pow((w-x), 4))));
		
	double exp4=2*x*Math.cos(Math.sqrt(100*w*w+x*x))*Math.sin(Math.sqrt(100*w*w+x*x));
	double den4=(Math.sqrt(100*w*w+x*x))*den3;
		
	result=(exp1/(den1*den1))+exp2/den2+(exp3/(den3*den3))+exp4/den4;   	  
       
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
