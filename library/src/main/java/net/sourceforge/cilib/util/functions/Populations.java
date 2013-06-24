/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.functions;

import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.behaviour.Behaviour;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.pso.particle.Particle;
import fj.F;
import fj.F2;
import fj.data.List;

public final class Populations {
    /**
     * Returns an empty population of the given population type;
     */
    public static <P extends SinglePopulationBasedAlgorithm> F<P, P> emptyPopulation() {
        return new F<P, P>() {
            @Override
            public P f(P a) {
                P tmp = (P) a.getClone();
                tmp.setTopology(List.nil());
                return tmp;
            }
        };
    }

    /**
     * Converts a swarm into a list of single entity populations.
     */
    public static <P extends SinglePopulationBasedAlgorithm, E extends Entity> F<P, List<P>> populationToAlgorithms() {
        return new F<P, List<P>>() {
            @Override
            public List<P> f(final P a) {
                return Populations.<P, E>entitiesToAlgorithms().f((Iterable<E>) a.getTopology(), a);
            }
        };
    }

    /**
     * Converts a single entity to a population of the given type.
     */
    public static <P extends SinglePopulationBasedAlgorithm, E extends Entity> F2<E, P, P> entityToAlgorithm() {
        return new F2<E, P, P>() {
            @Override
            public P f(E e, P p) {
                P tmp = (P) p.getClone();
                tmp.setTopology(List.single(e));
                //((fj.data.List<E>) tmp.getTopology()).add(e);

                return tmp;
            }
        };
    }

    /**
     * Converts a list of entities into single entity populations.
     */
    public static <P extends SinglePopulationBasedAlgorithm, E extends Entity> F2<Iterable<E>, P, List<P>> entitiesToAlgorithms() {
        return new F2<Iterable<E>, P, List<P>>() {
            @Override
            public List<P> f(Iterable<E> a, P b) {
                return List.iterableList(a).map(Populations.<P, E>entityToAlgorithm().flip().f(b));
            }
        };
    }

    /**
     * Makes sure a swarm has a neighbourhood best and the correct particle behavior.
     *
     * @param pb The particle behavior to give each entity in the swarm.
     *
     * @return The enforced swarm.
     */
    public static <P extends SinglePopulationBasedAlgorithm> F<P, P> enforceTopology(final Behaviour pb) {
        return new F<P, P>() {
            @Override
            public P f(P a) {
                P tmp = (P) a.getClone();

                if (!tmp.getTopology().isEmpty() && tmp.getTopology().head() instanceof Particle) {
                	List<Particle> local = tmp.getTopology();
                    for (Entity e : local) {
                        e.setBehaviour(pb);
                    }
                }

                return tmp;
            }
        };
    }
}
