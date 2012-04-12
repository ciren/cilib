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
import fj.P;
import fj.P2;
import fj.data.List;
import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.niching.NicheAlgorithm;
import static net.sourceforge.cilib.niching.utils.Niching.*;

public class NichePSOIterationStrategy extends AbstractIterationStrategy<NicheAlgorithm> {

    @Override
    public NichePSOIterationStrategy getClone() {
        return this;
    }

    @Override
    public void performIteration(NicheAlgorithm alg) {
        P2<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>> newSwarms = combineSwarms
                .andThen(iterateMainSwarm)
                .andThen(iterateSubswarms)
                .andThen(merge(alg.getMergeDetection(), 
                    alg.getMainSwarmMergeStrategy(), 
                    alg.getSubSwarmsMergeStrategy()))
                .andThen(absorb(alg.getAbsorptionDetection(), 
                    alg.getMainSwarmAbsorptionStrategy(), 
                    alg.getSubSwarmsAbsorptionStrategy()))
                .andThen(enforceMainSwarmTopology(alg.getMainSwarmParticle().getParticleBehavior()))
                .andThen(createNiches(alg.getNicheDetection(), 
                    alg.getSwarmCreationStrategy(), 
                    alg.getMainSwarmPostCreation()))
                .f(P.p(alg.getMainSwarm(), alg.getPopulations()));

        alg.setPopulations(Lists.newArrayList(newSwarms._2().toCollection()));
        alg.setMainSwarm(newSwarms._1());
    }
}
