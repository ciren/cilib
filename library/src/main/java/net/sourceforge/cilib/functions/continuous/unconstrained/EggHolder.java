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
 * <p><b>The Egg Holder.</b></p>
 *This is a minimisation problem
 * <p><b>Reference:</b> S.K. Mishra, <i>Some New Test Functions
 * for Global Optimization and Performance of Repulsive Particle
 * Swarm Methods</i>, Technical Report, North-Eastern Hill University,
 * India, 2006</p>
 *
 * <p>Note: n >= 2</p>
 *
 * <p>Minimum:
 * <ul>
 * <li> f(<b>x</b>*) approx -959.64 </li>
 * <li> <b>x</b>* = (512,404.2319) for n=2</li>
 * <li> for x_i in [-512,512]</li>
 * </ul>
 * </p>
 *
 * <p>Characteristics:
 * <ul>
 * <li>Only defined for 2+ dimensions</li>
 * <li>Multimodal</li>
 * <li>Non-separable</li>
 * <li>Not regular</li>
 * </ul>
 * </p>
 *
 * R(-512.0,512.0)^30
 *
 */
public class EggHolder extends ContinuousFunction implements Gradient {

    private static final long serialVersionUID = 358993985066821115L;

    @Override
    public Double f(Vector input) {
        Preconditions.checkArgument(input.size() >= 2, "EggHolder function is only defined for 2 or more dimensions");

        double sum = 0.0;
        for (int j = 0; j < input.size() - 1; j++) {
            sum += (-1*(input.doubleValueOf(j+1) + 47.0)
                    *Math.sin(Math.sqrt(Math.abs(input.doubleValueOf(j+1) + input.doubleValueOf(j)/2.0 + 47.0)))
                    + Math.sin(Math.sqrt(Math.abs(input.doubleValueOf(j) - (input.doubleValueOf(j+1)+47.0))))
                    *(-1.0*input.doubleValueOf(j)));
        }
        return sum;
    }
    
    public Double df(Vector input, int i){
    double result=0.0;
    
    
    if (i==1){
        double x = input.doubleValueOf(0);
        double y = input.doubleValueOf(1);
        
        double exp1=(x*(y-x+47.0)*Math.cos(Math.sqrt(Math.abs(y-x+47.0))))/(2.0*Math.pow(Math.abs(y-x+47.0),0.5)*Math.abs(y-x+47.0));		
        double exp2=((-47.0-y)*(47.0+x/2.0+y)*Math.cos(Math.sqrt(Math.abs(47.0+x/2.0+y))))/(4.0*Math.pow(Math.abs(y+x/2+47.0),0.5)*(Math.abs(y+x/2+47.0)));
        double exp3=(Math.sin(Math.sqrt(Math.abs(y-x+47.0))));
        
        result= exp1+exp2-exp3;
    }
    
    else if(i==input.size()) {
        double x = input.doubleValueOf(input.size()-2);
        double y = input.doubleValueOf(input.size()-1);
               
        double e1=(x*(y-x+47.0)*Math.cos(Math.sqrt(Math.abs(y-x+47.0))))/(2.0*Math.pow(Math.abs(y-x+47.0),1.5));
        double e2=((-47.0-y)*(47.0+x/2.0+y)*Math.cos(Math.sqrt(Math.abs(47.0+x/2.0+y))))/(2.0*Math.pow(Math.abs(y+x/2+47.0),1.5));
        double e3=-(Math.sin(Math.sqrt(Math.abs(y+x/2+47.0))));
        
        result= e3+e2-e1;        
    }
    
    else {
        
        double w = input.doubleValueOf(i-2);
        double x = input.doubleValueOf(i-1);
        double y = input.doubleValueOf(i);
        
        double ex1=((47.0+x/2.0+y)*(-47.0-y)*Math.cos(Math.sqrt(Math.abs(47.0+x/2.0+y))))/(4.0*Math.pow(Math.abs(y+x/2+47.0),3/2));              
        double ex2=Math.sin(Math.sqrt(Math.abs(y-x+47)));
        double ex3=((x)*(y-x+47.0)*Math.cos(Math.sqrt(Math.abs(y-x+47.0))))/(2.0*Math.pow(Math.abs(y-x+47.0),3/2));
        double ex4=Math.sin(Math.sqrt(Math.abs(x-w/2+47.0)));
        double ex5=((47.0+w/2.0+x)*(-47.0-x)*Math.cos(Math.sqrt(Math.abs(47.0+w/2.0+x))))/(2.0*Math.pow(Math.abs(x+w/2+47.0),3/2));
        double ex6=(w*(x-w+47.0)*Math.cos(Math.sqrt(Math.abs(x-w+47.0))))/(2.0*Math.pow(Math.abs(x-w+47.0),3/2));	
             
        result=ex1-ex2+ex3-ex4+ex5-ex6;
        
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
