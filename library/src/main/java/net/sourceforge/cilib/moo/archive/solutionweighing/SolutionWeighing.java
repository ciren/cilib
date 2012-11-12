/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.moo.archive.solutionweighing;


import net.sourceforge.cilib.util.Cloneable;
import net.sourceforge.cilib.util.selection.weighting.Weighting;

/**
 * <p>
 * Weighs an optimisation solution for selection to be either removed from the
 * {@code Archive} or selected to be used as guide during the search process.
 * </p>
 *
 */
public interface SolutionWeighing extends Weighting, Cloneable {

    @Override
    abstract SolutionWeighing getClone();

//    @Override
//    boolean weigh(List<Selection.Entry<OptimisationSolution>> elements);
}
