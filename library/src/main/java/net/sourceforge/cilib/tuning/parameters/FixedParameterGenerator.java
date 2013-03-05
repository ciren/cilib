/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.tuning.parameters;

import fj.F;
import fj.data.List;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.functions.Utils;

/**
 * Generates count parameter values which are at fixed distances from each other.
 */
public class FixedParameterGenerator extends ParameterGenerator {
    
    private TuningBounds bounds;
    private int count;
    
    public FixedParameterGenerator() {
        this.bounds = new TuningBounds();
        this.count = 10;
    }

    public void setBounds(TuningBounds bounds) {
        this.bounds = bounds;
    }

    public TuningBounds getBounds() {
        return bounds;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
    
    @Override
    public Vector _1() {
        final double inc = bounds.getRange() / count;
        return Vector.copyOf(List.range(0, count)
            .map(new F<Integer, Double>() {
                @Override
                public Double f(Integer a) {
                    return bounds.getLowerBound() + a * inc;
                }
            }.andThen(Utils.precision(precision))));
    }
}
