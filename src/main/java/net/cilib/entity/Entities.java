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
import fj.P1;
import fj.Unit;
import fj.data.List;
import fj.data.Option;
import net.sourceforge.cilib.math.random.generator.RandomProvider;

public final class Entities {

    private final static Entity DUMMY = new Entity() {
        @Override
        public List<Double> solution() {
            return List.nil();
        }

        @Override
        public Option<Double> fitness() {
            return Option.none();
        }
    };

    public static P1<Particle> particleGen(final int n, final RandomProvider random) {
        final F<Unit, Particle> func = new F<Unit, Particle>() {
            @Override
            public Particle f(Unit a) {
                List<Double> position = List.<Double>replicate(n, 0.0).map(new F<Double, Double>() {
                    @Override
                    public Double f(Double a) {
                        return random.nextDouble();
                    }
                });

                return new Particle(position, position,
                        List.replicate(n, 0.0),
                        Option.<Double>none());
            }
        };
        return new P1<Particle>() {
            @Override
            public Particle _1() {
                return func.f(Unit.unit());
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
