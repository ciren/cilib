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
import fj.F;
import fj.P2;
import fj.data.List;

import net.cilib.entity.HasCandidateSolution;
import net.sourceforge.cilib.math.random.generator.RandomProvider;

/**
 *
 */
public class CrossoverProvider {

    private final RandomProvider randomProvider;

    @Inject
    public CrossoverProvider(RandomProvider randomProvider) {
        this.randomProvider = randomProvider;
    }

    public List<Double> create(HasCandidateSolution target, HasCandidateSolution trialVector) {
        return create(target.solution(), trialVector.solution());
    }

    public List<Double> create(List<Double> target, List<Double> trialVector) {
        final int j = randomProvider.nextInt(trialVector.length());

        return trialVector.zip(target).zipIndex()
                .bind(new F<P2<P2<Double, Double>, Integer> , List<Double>>() {
            @Override
            public List<Double> f(P2<P2<Double, Double>, Integer> a) {
                return (randomProvider.nextDouble() < 0.5 || a._2() == j)
                        ? List.list(a._1()._1())
                        : List.list(a._1()._2());
            }
        });
    }
}
