/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.unconstrained;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.functions.Gradient;
import net.sourceforge.cilib.functions.NichingFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * <p><b>The Shubert Function.</b></p>
 ** This is a minimisation problem
 * <p>Global Minimum:
 * <ul>
 * <li>&fnof;(<b>x</b>*) = -186.7309088</li>
 * <li> Many global minima: n=1 has 3, n=2 has 9, n=3 has 81, n=4 has 324, n has pow(3,n)</li>
 * <li> All unevenly spaced</li>
 * <li> for x<sub>i</sub> in [-10,10]</li>
 * </ul>
 * </p>
 *
 * <p>Local Minimum:
 * <ul>
 * <li> Many local minima</li>
 * </ul>
 * </p>
 *
 * <p>Characteristics:
 * <ul>
 * <li>Multi-dimensional Multi-modal</li>
 * <li>Non-Separable</li>
 * </ul>
 * </p>
 *
 */
public class Shubert extends ContinuousFunction implements Gradient, NichingFunction {

    /**
     * {@inheritDoc}
     */
    @Override
    public Double f(Vector input) {
        double result = 1.0;
        for (int i = 0; i < input.size(); ++i) {
            double x=input.doubleValueOf(i);
            double result1 = 0.0;
            for (int j = 1; j <=5; j++) {
                result1 += j*Math.cos((j+1)*x + j);
            }
            result *= result1;
        }
        return result;
    }
    
    public Double df(Vector input, int i) {
        double res = 0.0;
        double sum1 = 0.0;
        
        double prod = 1.0;
        for (int j = 1; j <= input.size(); ++j){
            double sum2 = 0.0;
            double x=input.doubleValueOf(j-1);
            for (int k = 1; k <= 5; k++){                
                sum2 += k*Math.cos((k+1)*x + k);
            }
            prod = prod*sum2;            
        }
        
        double sum3 = 0.0;
        for (int m = 1; m <= 5; ++m){
            double y=input.doubleValueOf(i-1);
            sum3 += m*Math.cos((m+1)*y+m);
         }  
        prod= prod/sum3;
        
        double sin = 0.0;
        for (int n = 1; n <= 5; ++n){
           double x=input.doubleValueOf(i-1);
            sin += -(n*(n+1)*Math.sin((n+1)*x + n));
         }
                     
        res=sin*prod;
       
        return res;        
    }
    
    public double getAverageGradientVector ( Vector x){
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
