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
 * <p><b>The Generalized Ackley.</b></p>
 *
 * <p><b>Reference:</b> T.Back, <i>Evolutionary Algorithms in Theory and Practice</i>,
 * Oxford University Press, 1996</p>
 *This is a minimization problem
 * <p>Minimum:
 * <ul>
 * <li> &fnof;(<b>x</b>*) = 0</li>
 * <li> <b>x</b>* = (0, 0, ...., 0)</li>
 * <li> for x<sub>i</sub> in [-32.768,32.768]</li>
 * </ul>
 * </p>
 *
 * <p>Characteristics:
 * <ul>
 * <li>Multimodal</li>
 * <li>Seperable</li>
 * <li>Regular</li>
 * </ul>
 * </p>
 * R(-32.768, 32.768)^30
 *
 *
 */
public class Ackley extends ContinuousFunction implements Gradient{

    private static final long serialVersionUID = -7803711986955989075L;

    /* (non-Javadoc)
     * @see net.sourceforge.cilib.functions.redux.ContinuousFunction#evaluate(net.sourceforge.cilib.type.types.container.Vector)
     */
    @Override
    public Double f(Vector input) {
        final double size = (double)(input.size());
        double sumsq = 0.0;
        double sumcos = 0.0;
        for (int i = 0; i < size; i++) {
            sumsq += input.doubleValueOf(i) * input.doubleValueOf(i);
            sumcos += Math.cos(2.0 * Math.PI * input.doubleValueOf(i));
        }
        return 20.0 + Math.exp(1)-(20.0 * Math.exp(-0.2 * Math.sqrt(sumsq / size))) - (Math.exp(sumcos / size));
    }
    
    
    public Double df(Vector input, int i) {
        final double size = (double)(input.size());
        double sumsq = 0.0;
        double sumcos = 0.0;
        
        for (int k = 0; k < size; ++k) {
            sumsq += input.doubleValueOf(k) * input.doubleValueOf(k);
            sumcos += Math.cos(2.0 * Math.PI * input.doubleValueOf(k));
        }
        double value1 = Math.sin(2.0*Math.PI*input.doubleValueOf(i-1));
               
        double exp1=(2*Math.PI*Math.exp(sumcos/size)*value1)/size;
                
        double exp2=(4*input.doubleValueOf(i-1))*(Math.exp(-0.2 * Math.sqrt(sumsq / size)))/(Math.sqrt(sumsq)*Math.sqrt(size));
        
        return exp1+exp2;
        
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
