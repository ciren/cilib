/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.tuning.parameters;

import fj.F;
import static fj.data.List.range;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.functions.Utils;

/**
 * Generates a list of values randomly within the given bounds.
 */
public class RandomParameterGenerator extends ParameterGenerator {
    
    private int count;
    private ProbabilityDistributionFunction distribution;
    
    public RandomParameterGenerator() {
        this.distribution = new UniformDistribution();
        this.count = 10;
    }

    @Override
    public Vector _1() {
        return Vector.copyOf(
            range(0, count).map(new F<Integer, Double>(){
                @Override
                public Double f(Integer a) {
                    return distribution.getRandomNumber();
                }                
            }.andThen(Utils.precision(precision))));
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setDistribution(ProbabilityDistributionFunction distribution) {
        this.distribution = distribution;
    }

    public ProbabilityDistributionFunction getDistribution() {
        return distribution;
    }
}
