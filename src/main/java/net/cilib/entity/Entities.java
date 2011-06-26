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
package net.cilib.entity;

import fj.F;
import fj.Unit;
import fj.data.Option;
import net.cilib.collection.immutable.CandidateSolution;
import net.cilib.collection.immutable.Velocity;
import net.sourceforge.cilib.math.random.generator.RandomProvider;

public final class Entities {

    private final static Entity DUMMY = new Entity() {

        @Override
        public CandidateSolution solution() {
            return CandidateSolution.empty();
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public Option<Double> fitness() {
            return Option.none();
        }
    };

    public static F<Unit, Particle> particleGen(final int n, final RandomProvider random) {
        return new F<Unit, Particle>() {
            @Override
            public Particle f(Unit a) {
                CandidateSolution position = CandidateSolution.replicate(n, new F<Unit, Double>() {
                    @Override
                    public Double f(Unit a) {
                        return random.nextDouble();
                    }
                });

                return new Particle(position, position,
                        Velocity.replicate(n, 0.0),
                        Option.<Double>none());
            }
        };
    }

    private Entities() {
        throw new UnsupportedOperationException();
    }

    public static <A extends Entity> A dummy() {
        return (A) DUMMY;
    }
}
