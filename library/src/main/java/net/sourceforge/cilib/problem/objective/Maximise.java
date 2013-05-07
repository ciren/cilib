/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem.objective;

import fj.F;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.problem.solution.MaximisationFitness;

public class Maximise implements Objective {

    public Fitness evaluate(double fitness) {
        return new MaximisationFitness(fitness);
    }
    
    public <A> A fold(F<Minimise, A> a, F<Maximise, A> b) {
        return b.f(this);
    }

}
