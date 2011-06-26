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

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

import java.util.List;
import net.cilib.collection.immutable.CandidateSolution;
import net.cilib.entity.HasCandidateSolution;
import net.sourceforge.cilib.math.random.generator.RandomProvider;

/**
 *
 * @author gpampara
 */
public class CrossoverProvider {

    private final RandomProvider randomProvider;

    @Inject
    public CrossoverProvider(RandomProvider randomProvider) {
        this.randomProvider = randomProvider;
    }

    public CandidateSolution create(HasCandidateSolution target, HasCandidateSolution trialVector) {
        return create(target.solution(), trialVector.solution());
    }

    public CandidateSolution create(CandidateSolution target, CandidateSolution trialVector) {
        Preconditions.checkArgument(target.size() == trialVector.size(), "ERROR! different sizes");
        final List<Integer> crossoverPoints = Lists.newArrayList();

        // Select the crossover points
        int random = randomProvider.nextInt(trialVector.size());
        crossoverPoints.add(random);

        for (int i = 0, n = trialVector.size(); i < n; i++) {
            if (randomProvider.nextInt() < 0.5 && i != random) {
                crossoverPoints.add(i);
            }
        }

        return conditionalZip(trialVector, target, crossoverPoints);
    }

    private CandidateSolution conditionalZip(final CandidateSolution trialVector,
            final CandidateSolution target, final List<Integer> crossoverPoints) {

        throw new UnsupportedOperationException();

        // I don't like this increment on index...
//        final F2<Double, Double, Double> f = new F2<Double, Double, Double>() {
//            int index = 0;
//            @Override
//            public Double f(Double a, Double b) {
//                if (crossoverPoints.contains(index++)) {
//                    return a;
//                } else {
//                    return b;
//                }
//            }
//        };

//        return trialVector.zipWith(target, f);
//        CandidateSolution.Builder offspringBuilder = CandidateSolution.newBuilder();
//        for (int i = 0; i < trialVector.size(); i++) {
//            if (crossoverPoints.contains(Integer.valueOf(i))) {
//                offspringBuilder.add(trialVector.get(i));
//            } else {
//                offspringBuilder.add(target.get(i));
//            }
//        }
//
//        return offspringBuilder.build();
    }
}
