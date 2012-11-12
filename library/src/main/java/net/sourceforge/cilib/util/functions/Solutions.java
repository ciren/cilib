/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.functions;

import fj.F;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 */
public final class Solutions {
    public static <S extends OptimisationSolution> F<S, Vector> getPosition() {
        return new F<S, Vector>() {
            @Override
            public Vector f(S a) {
                return (Vector) a.getPosition();
            }
        };
    }

    public static <S extends OptimisationSolution> F<S, Fitness> getFitness() {
        return new F<S, Fitness>() {
            @Override
            public Fitness f(S a) {
                return a.getFitness();
            }
        };
    }
}
