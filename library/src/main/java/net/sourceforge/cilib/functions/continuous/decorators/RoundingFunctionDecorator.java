/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.decorators;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import fj.F;

/**
 * A decorator that makes a function non-continuous.
 * {@code
 *              x_j          if |x_j|&lt;0.5
 * x_j = round(2 * x_j) / 2  otherwise
 *
 * where
 *            a-1  if x&lt;=0 && b&gt;=0.5
 * round(x) =  a   if b&lt;0.5
 *            a+1  if x&gt;0 && b&gt;=0.5
 * }
 * <p>
 * Reference:
 * </p>
 * <p>
 * Suganthan, P. N., Hansen, N., Liang, J. J., Deb, K., Chen, Y., Auger, A.,
 * and Tiwari, S. (2005). Problem Definitions and Evaluation Criteria for the
 * CEC 2005 Special Session on Real-Parameter Optimization. Natural Computing,
 * 1-50. Available at: http://vg.perso.eisti.fr/These/Papiers/Bibli2/CEC05.pdf.
 * </p>
 */
public class RoundingFunctionDecorator extends ContinuousFunction {
    private ContinuousFunction function;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double f(Vector input) {
        return function.f(input.map(new F<Numeric, Numeric>() {
            @Override
            public Numeric f(Numeric f) {
                if (Math.abs(f.doubleValue()) < 0.5) {
                    return f;
                }

                return Real.valueOf(round(2 * f.doubleValue()) / 2.0);
            }
        }));
    }

    /**
     * The round function.
     * @{code
     *            a-1  if x&lt;=0 && b&gt;=0.5
     * round(x) =  a   if b&lt;0.5
     *            a+1  if x&gt;0 && b&gt;=0.5
     * }
     * @param f a number to round.
     * @return  the rounded number.
     */
    private int round(double f) {
        double b = f - (int) f;
        if (f <= 0.0 && b >= 0.5) {
            return (int) f - 1;
        } else if(b < 0.5) {
            return (int) f;
        } else {
            return (int) f + 1;
        }
    }

    /**
     * Get the function that is decorated.
     * @return the decorated function.
     */
    public ContinuousFunction getFunction() {
        return function;
    }

    /**
     * Sets the decorated function.
     * @param function The function to decorate.
     */
    public void setFunction(ContinuousFunction function) {
        this.function = function;
    }
}
