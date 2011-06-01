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
package net.cilib.algorithm;

import com.google.inject.Inject;
import net.cilib.collection.Topology;
import net.cilib.collection.TopologyBuffer;
import net.cilib.entity.*;
import net.cilib.event.CanRaise;
import net.cilib.event.IterationEvent;

/**
 * DE Implementation of {@code DE/x/y/z}.
 *
 * @author gpampara
 * @since 0.8
 */
public class DE<A extends Entity>  extends PopulationBasedAlgorithm<A> {
    private final MutationProvider mutationProvider;
    private final CrossoverProvider crossoverProvider;
    private final Selector selector;
    private final IndividualProvider individualProvider;
    private final FitnessProvider fitnessProvider;

    @Inject
    public DE(MutationProvider mutationProvider,
            CrossoverProvider crossoverProvider,
            Selector selector,
            IndividualProvider individualProvider,
            FitnessProvider fitnessProvider) {
        this.mutationProvider = mutationProvider;
        this.crossoverProvider = crossoverProvider;
        this.selector = selector;
        this.individualProvider = individualProvider;
        this.fitnessProvider = fitnessProvider;
    }

    @Override
    @CanRaise(IterationEvent.class)
    public Topology<A> next(Topology<A> topology) {
        TopologyBuffer<A> buffer = topology.newBuffer();
        for (A parent : topology) {
            CandidateSolution trialVector = mutationProvider.create(topology);
            CandidateSolution crossedOver = crossoverProvider.create(parent.solution(), trialVector);
            Individual offspring = individualProvider.solution(crossedOver)
                    .fitness(fitnessProvider).get();
            buffer.add((A) selector.select(parent, offspring));
            buffer.add(parent);
        }
        return buffer.build();
    }
}
