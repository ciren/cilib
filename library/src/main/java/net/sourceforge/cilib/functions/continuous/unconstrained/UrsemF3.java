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
import net.sourceforge.cilib.functions.NichingFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * UrsemF3 function.
 *This is a Maximisation problem
 * Minimum: 2.5
 * R(-2, 2)^2
 *
 */
public class UrsemF3 extends ContinuousFunction implements Gradient, NichingFunction{

    private static final long serialVersionUID = -4477290008482842765L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double f(Vector input) {
        Preconditions.checkArgument(input.size() >= 2, "UrsemF3 function is  defined for 2+ dimensions");
        
        double temp_resul1=0.0;
        double temp_result2=0.0;
        double result=0.0;
        
        for (int i=0;i< input.size() - 1;i++){
            
        double x = input.doubleValueOf(i);
        double y = input.doubleValueOf(i+1);
        temp_resul1 = Math.sin(2.2 * Math.PI * x + 0.5 * Math.PI) * ((2.0 - Math.abs(y)) / 2.0) * ((3.0 - Math.abs(x)) / 2.0);        
        temp_result2= Math.sin(0.5 * Math.PI * y * y + 0.5 * Math.PI) * ((2.0 - Math.abs(y)) / 2.0) * ((2.0 - Math.abs(x)) / 2.0);
        result+=temp_resul1+temp_result2;
        
        }    
       return result;
    }
       
       public Double df(Vector input, int i){
    
        double result=0.0;
    
        if (i==1) {
        double w = input.doubleValueOf(0);
        double x = input.doubleValueOf(1);
        
        double exp1=(-w*(2-Math.abs(x))*(Math.cos((Math.PI*x*x)/2)))/(4*Math.abs(w)) ;
	double exp2=-0.55*Math.PI*(3-Math.abs(w))*Math.sin(2.2*Math.PI*w)*(2-Math.abs(x)) ;
	double exp3=-(w*(Math.cos(2.2*Math.PI*w))*(2-Math.abs(x)))/(4*Math.abs(w));
	        
        result= exp1+exp2+exp3;
       }
        
       else if(i==input.size()) {
           
        double x = input.doubleValueOf(input.size()-2);
        double y = input.doubleValueOf(input.size()-1);
        
	double exp1=(-Math.PI*(2-Math.abs(x))*y*(2-Math.abs(y))*(Math.sin((Math.PI*y*y)/2)))/(4) ;
	double exp2=-(((2-Math.abs(x))*y*(Math.cos((Math.PI*y*y)/2)))/(4*Math.abs(y)));
	double exp3=-(((3-Math.abs(x))*Math.cos(2.2*Math.PI*x)*y)/(4*Math.abs(y)));
		
       result= exp1+exp2+exp3;        
      }
      else {
        
        double w = input.doubleValueOf(i-2);
        double x = input.doubleValueOf(i-1);
        double y = input.doubleValueOf(i);
        
        double exp1=(-x*(2-Math.abs(y))*(Math.cos((Math.PI*y*y)/2)))/(4*Math.abs(x));
	double exp2=-(0.55*Math.PI)*(3-Math.abs(x))*Math.sin(2.2*Math.PI*x)*(2-Math.abs(y)) ;
	double exp3=-(x*(Math.cos(2.2*Math.PI*x))*(2-Math.abs(y)))/(4*Math.abs(x));
        double exp4=-((Math.PI*(2-Math.abs(w))*x*(2-Math.abs(x))*(Math.sin((Math.PI*x*x)/2)))/(4));
	double exp5=-(((2-Math.abs(w))*x*(Math.cos((Math.PI*x*x)/2)))/(4*Math.abs(x)));
	double exp6=-(((3-Math.abs(w))*Math.cos(2.2*Math.PI*w)*x)/(4*Math.abs(x)));
        
       result=exp1+exp2+exp3+exp4+exp5+exp6;   	  
       
      }
      return result;
    }
       
    public double getAverageGradientVector ( Vector x){
    
        
        double sum = 0;
        
        for (int i = 1; i <= x.size(); ++i)
        {
            sum += this.df(x,i);
        }
           
        return sum/x.size();
    }
    
    public double getGradientVectorLength (Vector x){
    
        double sumsqrt = 0;
        
        for (int i = 1; i <= x.size(); ++i)
        {
            sumsqrt += this.df(x,i)*this.df(x,i);
        }
        
        return Math.sqrt(sumsqrt);
    }
    
    public Vector getGradientVector (Vector x){
    
        Vector.Builder vectorBuilder = Vector.newBuilder();
        
        for (int i = 1; i <= x.size(); ++i)
        {
             vectorBuilder.add(this.df(x,i));
        }
        
        return vectorBuilder.build();
    }   
     @Override
     public double getNicheRadius() {
	return 0.5;
     }

}
