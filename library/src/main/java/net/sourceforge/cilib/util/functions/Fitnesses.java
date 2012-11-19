/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.functions;

import fj.F;
import net.sourceforge.cilib.problem.solution.Fitness;

public final class Fitnesses {
    
    private Fitnesses() {}
    
    public static <T extends Fitness> F<T, Double> getValue() {
        return new F<T, Double>() {
            @Override
            public Double f(T a) {
                return a.getValue();
            }
        };
    }

}
