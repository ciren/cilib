/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.moo.criterion.objectiveassignmentstrategies;

import java.util.List;
import net.sourceforge.cilib.algorithm.population.MultiPopulationCriterionBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.problem.MOOptimisationProblem;
import net.sourceforge.cilib.util.Cloneable;

/**
 * <p>
 * Used by {@link MultiPopulationCriterionBasedAlgorithm} to assign the different sub-objectives
 * in a {@link MOOptimisationProblem} to specific {@link PopulationBasedAlgorithm}s.
 * </p>
 *
 */
public interface ObjectiveAssignmentStrategy extends Cloneable {

    @Override
    public ObjectiveAssignmentStrategy getClone();

    public void assignObjectives(MOOptimisationProblem problem, List<PopulationBasedAlgorithm> populations);
}
