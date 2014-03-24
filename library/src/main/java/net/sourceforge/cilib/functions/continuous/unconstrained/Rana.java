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
 *This is a minimisation problem
 * 
 */
public class Rana extends ContinuousFunction implements Gradient{
    
    @Override
    public Double f(Vector input) {
        Preconditions.checkArgument(input.size() >= 2, "Rana function is only defined for 2 or more dimensions");

        double sum = 0.0;
        double a=0.0;
        double b=0.0;
        
        
        for (int i = 0; i < input.size() - 1; i++) {
            a = Math.sqrt(Math.abs(input.doubleValueOf(i+1)+1-input.doubleValueOf(i)));
            b = Math.sqrt(Math.abs(input.doubleValueOf(i+1)+1+input.doubleValueOf(i)));
            
            sum += (input.doubleValueOf(i)*Math.sin(a)*Math.cos(b)
                    + (input.doubleValueOf(i+1)+1)*Math.cos(a)*Math.sin(b));
        }
        return sum;
    }
    
    
    public Double df(Vector input, int i)
    {
        Preconditions.checkArgument(input.size() > 0, "Rana function is defined for at least 2 dimension ");
        
        double res = 0.0;
        int n = input.size();
        
        if (i == 1)
        {
            double val1=input.doubleValueOf(0);
	    double val2=input.doubleValueOf(1);
			
	    double a = 1 - val1 + val2;
            double b = 1 + val1 + val2;
            double sina = Math.sin(Math.sqrt(Math.abs(a)));
            double sinb = Math.sin(Math.sqrt(Math.abs(b)));
            double cosa = Math.cos(Math.sqrt(Math.abs(a)));
            double cosb = Math.cos(Math.sqrt(Math.abs(b)));
			
	   double exp1= (((val2+1)*(b)*cosa*cosb)-(val1*b*sina*sinb))/(2*(Math.sqrt(Math.abs(b)))*(Math.abs(b)));
	   double exp2= (((val2+1)*(a)*sina*sinb)-(val1*a*cosa*cosb))/(2*(Math.sqrt(Math.abs(a)))*(Math.abs(a)));
	   double exp3= sina*cosb;
			
	    res = exp1+exp2+exp3;
        }
        else if (i == n)
        {
            double val1=input.doubleValueOf(n-2);
	    double val2=input.doubleValueOf(n-1);
			
	    double a = 1 - val1 + val2;
            double b = 1 + val1 + val2;
			
            double sina = Math.sin(Math.sqrt(Math.abs(a)));
            double sinb = Math.sin(Math.sqrt(Math.abs(b)));
            double cosa = Math.cos(Math.sqrt(Math.abs(a)));
            double cosb = Math.cos(Math.sqrt(Math.abs(b)));
            
	    double exp1= (((val2+1)*(b)*cosa*cosb)-(val1*b*sina*sinb))/(2*(Math.sqrt(Math.abs(b)))*(Math.abs(b)));
	    double exp2= (((val1)*(a)*cosa*cosb)-((val2+1)*a*sina*sinb))/(2*(Math.sqrt(Math.abs(a)))*(Math.abs(a)));
	    double exp3= cosa*sinb;
			
	    res = exp1+exp2+exp3;
			
	}
        else
        {
            double val1=input.doubleValueOf(i-2);
	    double val2=input.doubleValueOf(i-1);
	    double val3=input.doubleValueOf(i);
			
	    double a = 1 - val1 + val2;
            double b = 1 + val1 + val2;
			
	    double c = 1 - val2 + val3;
            double d = 1 + val2 + val3;
						
            double sina = Math.sin(Math.sqrt(Math.abs(a)));
            double sinb = Math.sin(Math.sqrt(Math.abs(b)));
            double cosa = Math.cos(Math.sqrt(Math.abs(a)));
            double cosb = Math.cos(Math.sqrt(Math.abs(b)));
            
	    double sinc = Math.sin(Math.sqrt(Math.abs(c)));
            double sind = Math.sin(Math.sqrt(Math.abs(d)));
            double cosc = Math.cos(Math.sqrt(Math.abs(c)));
            double cosd = Math.cos(Math.sqrt(Math.abs(d)));
			
	   double exp1= (((val2+1)*(b)*cosa*cosb)-(val1*b*sina*sinb))/(2*(Math.sqrt(Math.abs(b)))*(Math.abs(b)));
	   double exp2= (((val1)*(a)*cosa*cosb)-((val2+1)*a*sina*sinb))/(2*(Math.sqrt(Math.abs(a)))*(Math.abs(a)));
	   double exp3= cosa*sinb;		
			
	   double exp4= (((val3+1)*(c)*sinc*sind)-(val2*c*cosc*cosd))/(2*(Math.sqrt(Math.abs(c)))*(Math.abs(c)));
	   double exp5= (((val3+1)*(d)*cosc*cosd)-(val2*d*sinc*sind))/(2*(Math.sqrt(Math.abs(d)))*(Math.abs(d)));
	   double exp6= sinc*cosd;
			
            res=exp1+exp2+exp3+exp4+exp5+exp6;
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
