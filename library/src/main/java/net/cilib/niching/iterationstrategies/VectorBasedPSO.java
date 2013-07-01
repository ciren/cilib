/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.niching.iterationstrategies;

import com.google.common.collect.Lists;
import net.cilib.algorithm.population.AbstractIterationStrategy;
import net.cilib.niching.NichingAlgorithm;
import static net.cilib.niching.NichingFunctions.createNiches;
import net.cilib.niching.NichingSwarms;

/**
 *
 */
public class VectorBasedPSO extends AbstractIterationStrategy<NichingAlgorithm> {

    @Override
    public AbstractIterationStrategy<NichingAlgorithm> getClone() {
        return this;
    }

    @Override
    public void performIteration(NichingAlgorithm alg) {
        NichingSwarms newSwarms = createNiches(alg.getNicheDetector(),
                alg.getNicheCreator(),
                alg.getMainSwarmCreationMerger())
            .andThen(alg.getSubSwarmIterator())
            .f(NichingSwarms.of(alg.getMainSwarm(), alg.getPopulations()));

        alg.setPopulations(Lists.newArrayList(newSwarms._2().toCollection()));
        alg.setMainSwarm(newSwarms._1());
    }

}
