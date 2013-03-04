/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.dynamic;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.RespondingMultiPopulationCriterionBasedAlgorithm;

/**
 * Dynamic iteration strategy for PSO in dynamic environments used by a higher
 * level algorithm, e.g. VEPSO, that has a number of sub-algorithms. In each
 * iteration, it checks for an environmental change in all of the swarms. If a
 * change has been detected in any of the swarms, it responds to the change
 * according to the selected response strategy for all swarms, and not for only
 * the swarm whose environment has changed.
 *
 *
 * @param <E> The {@link PopulationBasedAlgorithm} that will have it's entities'
 * positions added to the archive as potential solutions.
 */
public class HigherLevelAllDynamicIterationStrategy<E extends PopulationBasedAlgorithm> extends HigherLevelDynamicIterationStrategy {

    private static final long serialVersionUID = -4417977245641438303L;

    /**
     * Structure of Higher Level Dynamic Iteration Strategy with
     * re-initialisation:
     *
     * <ol> <li>Check for environment change in any of the swarms</li> <li>If
     * the environment has changed:</li> <ol> <li>Respond to change for all
     * swarms</li> <ol> <li>Perform normal iteration for all swarms</li> </ol>
     */
    @Override
    public void performIteration(PopulationBasedAlgorithm algorithm) {
        //get the higher level algorithm
        RespondingMultiPopulationCriterionBasedAlgorithm topLevelAlgorithm =
                (RespondingMultiPopulationCriterionBasedAlgorithm) AbstractAlgorithm.getAlgorithmList().get(0);

        boolean hasChanged = false;

        //detecting whether a change has occurred in any of the swarms' environment
        for (PopulationBasedAlgorithm popAlg : topLevelAlgorithm.getPopulations()) {
            hasChanged = this.getDetectionStrategy().detect(popAlg);
            if (hasChanged) {
                break;
            }
        }

        //respond to a change if it has occurred
        if (hasChanged) {
            for (PopulationBasedAlgorithm popAlg : topLevelAlgorithm.getPopulations()) {
                this.getResponseStrategy().respond(popAlg);
            }
        }


        for (PopulationBasedAlgorithm pAlg : topLevelAlgorithm.getPopulations()) {
            pAlg.performIteration();
        }
    }
}
