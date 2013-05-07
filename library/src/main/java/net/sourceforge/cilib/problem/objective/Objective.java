/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem.objective;

import fj.F;
import net.sourceforge.cilib.problem.solution.Fitness;

public interface Objective {

    Fitness evaluate(double fitness);
    <A> A fold(F<Minimise, A> a, F<Maximise, A> b);
}
