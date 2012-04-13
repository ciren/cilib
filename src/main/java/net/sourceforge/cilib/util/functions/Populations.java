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
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.pso.particle.ParticleBehavior;

public final class Populations {
    /**
     * Returns an empty population of the given population type;
     */
    public static <P extends PopulationBasedAlgorithm> F<P, P> emptyPopulation() {
        return new F<P, P>() {
            @Override
            public P f(P a) {
                P tmp = (P) a.getClone();
                tmp.getTopology().clear();
                return tmp;
            }
        };
    }

    /**
     * Converts a swarm into a list of single entity populations.
     */
    public static <P extends PopulationBasedAlgorithm, E extends Entity> F<P, List<P>> populationToAlgorithms() {
        return new F<P, List<P>>() {
            @Override
            public List<P> f(final P a) {
                return Populations.<P, E>entitiesToAlgorithms().f((Topology<E>) a.getTopology(), a);
            }
        };
    }

    /**
     * Converts a single entity to a population of the given type.
     */
    public static <P extends PopulationBasedAlgorithm, E extends Entity> F2<E, P, P> entityToAlgorithm() {
        return new F2<E, P, P>() {
            @Override
            public P f(E e, P p) {
                P tmp = (P) p.getClone();
                tmp.getTopology().clear();
                ((Topology<E>) tmp.getTopology()).add(e);

                return tmp;
            }
        };
    }

    /**
     * Converts a list of entities into single entity populations.
     */
    public static <P extends PopulationBasedAlgorithm, E extends Entity> F2<Iterable<E>, P, List<P>> entitiesToAlgorithms() {
        return new F2<Iterable<E>, P, List<P>>() {
            @Override
            public List<P> f(Iterable<E> a, P b) {
                return List.iterableList(a).map(Populations.<P, E>entityToAlgorithm().flip().f(b));
            }
        };
    }

    /**
     * Makes sure a swarm has a neighborhood best and the correct particle behavior.
     *
     * @param pb The particle behavior to give each entity in the swarm.
     *
     * @return The enforced swarm.
     */
    public static <P extends PopulationBasedAlgorithm> F<P, P> enforceTopology(final ParticleBehavior pb) {
        return new F<P, P>() {
            @Override
            public P f(P a) {
                P tmp = (P) a.getClone();

                if (!tmp.getTopology().isEmpty() && tmp.getTopology().get(0) instanceof Particle) {
                    for (Entity e : tmp.getTopology()) {
                        Particle p = (Particle) e;
                        p.setParticleBehavior(pb);
                    }
                }

                return tmp;
            }
        };
    }
}
