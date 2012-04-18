/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
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
import static net.sourceforge.cilib.niching.NichingSwarms.onSubswarms;
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
     *   <li>Perform an absorption step defined by a {@link AbsorptionStrategy}.</li>
     *   <li>Identify any new potential niches using a {@link NicheDetection}.</li>
     *   <li>Create new sub-swarms via a {@link NicheCreationStrategy} for the identified niches.</li>
     * </ol>
     * </p>
     */
    @Override
    public void performIteration(NichingAlgorithm alg) {
        P2<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>> newSwarms =
                    onMainSwarm(alg.getMainSwarmIterator())
                .andThen(onSubswarms(alg.getSubSwarmIterator()))
                .andThen(merge(alg.getMergeDetector(),
                    alg.getMainSwarmMerger(),
                    alg.getSubSwarmMerger()))
                .andThen(absorb(alg.getAbsorptionDetector(),
                    alg.getMainSwarmAbsorber(),
                    alg.getSubSwarmAbsorber()))
                .andThen(onMainSwarm(enforceTopology(alg.getMainSwarmBehavior().getParticleBehavior())))
                .andThen(createNiches(alg.getNicheDetector(),
                    alg.getNicheCreator(),
                    alg.getMainSwarmCreationMerger()))
                .f(NichingSwarms.of(alg.getMainSwarm(), alg.getPopulations()));

        alg.setPopulations(Lists.newArrayList(newSwarms._2().toCollection()));
        alg.setMainSwarm(newSwarms._1());
    }
}
