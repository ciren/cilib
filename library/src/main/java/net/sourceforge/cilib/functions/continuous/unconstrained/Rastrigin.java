/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.unconstrained;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.functions.Differentiable;
import net.sourceforge.cilib.functions.NichingFunction;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import fj.F;
import net.sourceforge.cilib.functions.Gradient;

/**
 * <p><b>The Rastrigin function.</b></p>
 *This is a minimisation problem
 * <p><b>Reference:</b> X. Yao and Y. Liu and G. Liu, <i>Evolutionary Programming Made Faster</i>,
 * IEEE Transactions on Evolutionary Computation, vol 3, number 2, pages 82--102, 1999.</p>
 *
 * <p>Characteristics:
 * <ul>
 * <li>Multimodal</li>
 * <li>Continuous</li>
 * <li>Seperable</li>
 * </ul>
 * </p>
 *
 * f(x) = 0; x = (0,0,...,0);
 *
 * x e [-5.12, 5.12];
 *
 * R(-5.12, 5.12)^30
 *
 */
public class Rastrigin extends ContinuousFunction implements Gradient, NichingFunction {

    private static final long serialVersionUID = 447701182683968035L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double f(Vector input) {
        double tmp = 0;
        for (int i = 0; i < input.size(); ++i) {
            tmp += input.doubleValueOf(i) * input.doubleValueOf(i) - 10.0 * Math.cos(2.0 * Math.PI * input.doubleValueOf(i));
        }
        return 10 * input.size() + tmp;
    }
    
    public Double df(Vector input, int i) {
        double res = 2.0*input.doubleValueOf(i-1)+(20.0 * Math.PI * Math.sin(2.0 * Math.PI * input.doubleValueOf(i-1)));
       
        return res;
    }

    /**
     * {@inheritDoc}
     */
    
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
