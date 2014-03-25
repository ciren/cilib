/**
 * __ __ _____ / /_/ /_ Computational Intelligence Library (CIlib) / ___/ / / /
 * __ \ (c) CIRG @ UP / /__/ / / / /_/ / http://cilib.net \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.unconstrained;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.functions.Gradient;
import net.sourceforge.cilib.functions.NichingFunction;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Multimodal4 function.
 *This is a maximisation problem
 * Minimum for domain: 0.0 R(0, 1)^1
 *
 */
public class UnevenDecreasingMaxima extends ContinuousFunction implements Gradient, NichingFunction {

    private static final long serialVersionUID = -957215773660609565L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double f(Vector input) {
        double sum = 0.0;
        for (int i = 0; i < input.size(); i++) {
            double x = Math.pow(Math.sin(5.0 * Math.PI * (Math.pow(input.doubleValueOf(i), 0.75) - 0.05)), 6.0);
            double exp1 = -2.0 * Math.log(2) * (Math.pow((input.doubleValueOf(i) - 0.08) / 0.854, 2.0));
            double y = Math.exp(exp1);
            sum += x * y;
        }
        return sum;
    }

     public double df(double x) {
	
	 double exp = Math.exp(-2.0 * Math.log(2) * Math.pow((x - 0.08) / 0.854, 2.0));
	 double sin5 = Math.pow(Math.sin(5.0 * Math.PI * (Math.pow(x, 0.75) - 0.05)), 5.0);
	 double sin6 = Math.pow(Math.sin(5.0 * Math.PI * (Math.pow(x, 0.75) - 0.05)), 6.0);
	 double cos = 45 * Math.PI * Math.cos(5.0 * Math.PI * (Math.pow(x, 0.75) - 0.05));
	 double rest = (x - 0.08) * Math.log(2) * 4 / (0.854 * 0.854);
	 double denom = 2 * Math.pow(x, 0.25);
	 
	 return (((cos*sin5*exp)/denom)-(sin6*rest*exp));
	 
	}
	

    public double getAverageGradientVector(Vector x) {
        double sum = 0;
        for (Numeric n : getGradientVector(x)) {
            sum += n.doubleValue();
        }
        return sum / x.size();
    }

    public double getGradientVectorLength(Vector x) {
        return getGradientVector(x).length();
    }

    public Vector getGradientVector(Vector x) {
        Vector.Builder vectorBuilder = Vector.newBuilder();

        for (int i = 0; i < x.size(); ++i) {
            vectorBuilder.add(this.df(x.doubleValueOf(i)));
        }

        return vectorBuilder.build();
    }

    @Override
    public double getNicheRadius() {
        return 0.01;
    }
}
