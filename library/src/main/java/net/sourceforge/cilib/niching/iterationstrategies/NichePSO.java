/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.niching.iterationstrategies;

import com.google.common.collect.Lists;
import fj.P2;
import fj.data.List;
import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.niching.NichingAlgorithm;
import static net.sourceforge.cilib.niching.NichingFunctions.*;
import net.sourceforge.cilib.niching.NichingSwarms;
import static net.sourceforge.cilib.niching.NichingSwarms.onMainSwarm;
import net.sourceforge.cilib.niching.creation.NicheCreationStrategy;
import net.sourceforge.cilib.niching.creation.NicheDetection;
import net.sourceforge.cilib.niching.merging.MergeStrategy;
import net.sourceforge.cilib.pso.particle.Particle;
import static net.sourceforge.cilib.util.functions.Populations.enforceTopology;

public class NichePSO extends AbstractIterationStrategy<NichingAlgorithm> {

    @Override
    public NichePSO getClone() {
        return this;
    }

    /**
     * <p>
     * Perform an iteration of NichePSO.
     * </p>
     * <p>
     * The general format of this method would be the following steps:
     * <ol>
     *   <li>Perform an iteration of the main swarm.</li>
     *   <li>Perform an iteration for each of the contained sub-swarms.</li>
     *   <li>Merge any sub-swarms as defined my the associated {@link MergeStrategy}.</li>
     *   <li>Perform an absorption step defined by a {@link MergeStrategy}.</li>
     *   <li>Identify any new potential niches using a {@link NicheDetection}.</li>
     *   <li>Create new sub-swarms via a {@link NicheCreationStrategy} for the identified niches.</li>
     * </ol>
     * </p>
     */
    @Override
    public void performIteration(NichingAlgorithm alg) {
        P2<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>> newSwarms =
                onMainSwarm(alg.getMainSwarmIterator())
                .andThen(alg.getSubSwarmIterator())
                .andThen(merge(alg.getMergeDetector(),
                    alg.getMainSwarmMerger(),
                    alg.getSubSwarmMerger()))
                .andThen(absorb(alg.getAbsorptionDetector(),
                    alg.getMainSwarmAbsorber(),
                    alg.getSubSwarmAbsorber()))
                .andThen(onMainSwarm(enforceTopology(((Particle) alg.getEntityType()).getParticleBehavior())))
                .andThen(createNiches(alg.getNicheDetector(),
                    alg.getNicheCreator(),
                    alg.getMainSwarmCreationMerger()))
                .f(NichingSwarms.of(alg.getMainSwarm(), alg.getPopulations()));

        alg.setPopulations(Lists.newArrayList(newSwarms._2().toCollection()));
        alg.setMainSwarm(newSwarms._1());
    }
}
