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
package net.sourceforge.cilib.util.functions;

import fj.F;
import fj.F2;
import fj.data.List;
import java.util.Comparator;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.comparator.SocialBestFitnessComparator;

public final class PSOs {
    
    public static F2<List<Particle>, Particle, Particle> getNBest(final Comparator<Particle> comparator) {
        return new F2<List<Particle>, Particle, Particle>() {
            @Override
            public Particle f(List<Particle> a, Particle b) {
                return null;//Topologies.getNeighbourhoodBest(a, b, comparator);
            }
        };
    }

    public static F<List<Particle>, List<Particle>> updateNBest() {
        return new F<List<Particle>, List<Particle>>() {
            @Override
            public List<Particle> f(List<Particle> a) {
                return a.map(getNBest(new SocialBestFitnessComparator<Particle>()).f(a)
                        .apply(Particles.setNeighbourhoodBest().curry()));
            }
        };
    }

    public static F<List<Particle>, List<Particle>> pso() {
        return new F<List<Particle>, List<Particle>>() {
            @Override
            public List<Particle> f(List<Particle> a) {
                return updateNBest().f(a.map(Particles.updateVelocity()
                            .andThen(Particles.updatePosition())
                            .andThen(Entities.<Particle>evaluate())));
            }
        };
    }
}
