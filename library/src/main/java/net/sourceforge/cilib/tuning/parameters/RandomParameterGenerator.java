/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.tuning.parameters;

import fj.F;
import fj.data.List;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.functions.Utils;

/**
 * Generates a list of values randomly within the given bounds.
 */
public class RandomParameterGenerator extends ParameterGenerator {
    
    private TuningBounds bounds;
    private int count;
    
    public RandomParameterGenerator() {
        this.bounds = new TuningBounds();
        this.count = 10;
    }

    @Override
    public Vector _1() {
        final UniformDistribution uniform = new UniformDistribution();
        return Vector.copyOf(
            List.range(0, count).map(new F<Integer, Double>(){
                @Override
                public Double f(Integer a) {
                    return bounds.getLowerBound() + bounds.getRange() * uniform.getRandomNumber();
                }                
            }.andThen(Utils.precision(precision))));
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setBounds(TuningBounds bounds) {
        this.bounds = bounds;
    }

    public TuningBounds getBounds() {
        return bounds;
    }
}
