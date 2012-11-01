/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single;

import com.google.common.base.Preconditions;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.functions.Function;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * @param <F> The "from" type.
 * @param <T> The "to" type.
 */
public class GenericFunctionMeasurement<F, T> implements Measurement<Real> {

    private static final long serialVersionUID = 3301062975775598397L;
    private Function<Vector, Double> function = null;

    /**
     * Create a new instance of {@linkplain GenericFunctionMeasurement}.
     */
    public GenericFunctionMeasurement() {
        function = null;
    }

    /**
     * Copy constructor. Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public GenericFunctionMeasurement(GenericFunctionMeasurement copy) {
        function = copy.function;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GenericFunctionMeasurement getClone() {
        return new GenericFunctionMeasurement(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Real getValue(Algorithm algorithm) {
        Preconditions.checkNotNull(function, "The function that should be evaluated has not been set");
        Vector vector = (Vector) algorithm.getBestSolution().getPosition();
        return Real.valueOf(function.apply(vector));
    }

    /**
     * Get the set function.
     * @return The contained function.
     */
    public Function<Vector, Double> getFunction() {
        return function;
    }

    /**
     * Set the function.
     * @param f The value to set.
     */
    public void setFunction(Function<Vector, Double> f) {
        function = f;
    }
}
