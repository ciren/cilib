/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.decorators;

import fj.F;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Bit;

/**
 * Decorator class to convert a real-valued vector to a binary-valued vector.
 * The domain of the problem is a parameter of the conversion function.
 *
 * Used in the Probability Binary PSO (PBPSO).
 * <p>
 * Reference:
 * <p>
 * L. Wang, X. Wang, J. Fu, and L. Zhen. "A novel probability binary particle
 * swarm optimization algorithm and its application." Journal of software 3.9
 * (2008): 28-35.
 */
public class PBPSOFunctionDecorator implements ContinuousFunction {
    private ContinuousFunction function;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        return function.apply(input.map(new F<Numeric, Numeric>() {
            @Override
            public Numeric f(Numeric x) {
                double rmin = x.getBounds().getLowerBound();
                double rmax = x.getBounds().getUpperBound();
                double lx = (x.doubleValue() - rmin) / (rmax - rmin);
                return Bit.valueOf(Rand.nextDouble() <= lx);
            }
        }));
    }

    /**
     * Set the function that is to be decorated.
     *
     * @param function the function to decorated.
     */
    public void setFunction(ContinuousFunction function) {
        this.function = function;
    }
}
