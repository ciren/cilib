/**
 * Computational Intelligence Library (CIlib) Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP) Department of Computer
 * Science University of Pretoria South Africa
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.pso.niching;

import fj.*;
import fj.data.List;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.pso.niching.creation.NicheCreationStrategy;
import net.sourceforge.cilib.pso.niching.creation.NicheDetection;
import net.sourceforge.cilib.pso.niching.merging.MergeDetection;
import net.sourceforge.cilib.pso.niching.merging.MergeStrategy;
import net.sourceforge.cilib.pso.niching.merging.StandardMergeStrategy;
import net.sourceforge.cilib.pso.particle.ParticleBehavior;

/**
 * These are generic functions used in Niching algorithms. e.g. Merging,
 * absorption sub-population creation. They use given strategies to accomplish a
 * task and can be seen as higher order functions.
 */
public final class Niching {

    /**
     * Merges sub-swarms that satisfy given conditions. Also handles merging
     * with the main swarm if applicable.
     *
     * @param mergeDetection The detection strategy that determines if two
     * swarms must be merged.
     * @param mainSwarmMergeStrategy The merging strategy that decides how the
     * merged swarm interacts with the main swarm.
     * @param subSwarmsMergeStrategy The merging strategy used to merge two
     * sub-swarms.
     *
     * @return A tuple containing the main swarm in the first element and the
     * sub-swarms in the second element.
     */
    public static F<P2<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>>, P2<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>>> 
            merge(final MergeDetection mergeDetection, final MergeStrategy mainSwarmMergeStrategy, final MergeStrategy subSwarmsMergeStrategy) {
        return new F<P2<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>>, P2<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>>>() {
            @Override
            public P2<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>> f(P2<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>> swarms) {
                if (swarms._2().isEmpty() || swarms._2().length() == 1) {
                    return swarms.duplicate()._1();
                }

                PopulationBasedAlgorithm newMainSwarm = swarms._2().tail()
                        .filter(mergeDetection.f(swarms._2().head()))
                        .foldLeft(mainSwarmMergeStrategy, swarms._1());

                PopulationBasedAlgorithm mergedSwarms = swarms._2().tail()
                        .filter(mergeDetection.f(swarms._2().head()))
                        .foldLeft(subSwarmsMergeStrategy, swarms._2().head());

                P2<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>> newSwarms =
                        this.f(P.p(newMainSwarm, swarms._2().tail().removeAll(mergeDetection.f(swarms._2().head()))));

                return P.p(newSwarms._1(), List.cons(mergedSwarms, newSwarms._2()));
            }
        };
    }

    /**
     * Performs absorption between one sub-swarm and the main swarm.
     *
     * @param absorptionDetection The detection strategy that determines if an
     * entity in the main swarm must be absorbed.
     * @param mainSwarmAbsorptionStrategy The merging strategy that decides how
     * the absorbed entities interact with the main swarm.
     * @param subSwarmsAbsorptionStrategy The merging strategy used to merge an
     * entity with a sub-swarm.
     *
     * @return A tuple containing the main swarm in the first element and the
     * sub-swarm with absorbed entities in the second element.
     */
    public static F<P2<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>>, P2<PopulationBasedAlgorithm, PopulationBasedAlgorithm>> 
            absorbSingleSwarm(final MergeDetection absorptionDetection, final MergeStrategy mainSwarmAbsorptionStrategy, final MergeStrategy subSwarmsAbsorptionStrategy) {
        return new F<P2<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>>, P2<PopulationBasedAlgorithm, PopulationBasedAlgorithm>>() {
            @Override
            public P2<PopulationBasedAlgorithm, PopulationBasedAlgorithm> f(P2<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>> swarms) {
                PopulationBasedAlgorithm newSubSwarm = swarms._2()
                        .filter(absorptionDetection.f(swarms._1()))
                        .foldLeft(subSwarmsAbsorptionStrategy, swarms._1());

                PopulationBasedAlgorithm unmergedSwarms = swarms._2()
                        .removeAll(absorptionDetection.f(swarms._1()))
                        .foldLeft(new StandardMergeStrategy(), emptyPopulation.f(swarms._2().head()));

                PopulationBasedAlgorithm mergedSwarms = swarms._2()
                        .filter(absorptionDetection.f(swarms._1()))
                        .foldLeft(new StandardMergeStrategy(), emptyPopulation.f(swarms._2().head()));

                PopulationBasedAlgorithm newMainSwarm = mainSwarmAbsorptionStrategy.f(unmergedSwarms, mergedSwarms);

                return P.p(newMainSwarm, newSubSwarm);
            }
        };
    }

    /**
     * Performs absorption between the main swarm and each sub-swarm. Each
     * entity in the main swarm gets placed into a swarm of its own. This allows
     * the merging strategies to be used instead of duplicating the strategies
     * for entities.
     *
     * @param absorptionDetection The detection strategy that determines if an
     * entity in the main swarm must be absorbed.
     * @param mainSwarmAbsorptionStrategy The merging strategy that decides how
     * the absorbed entities interact with the main swarm.
     * @param subSwarmsAbsorptionStrategy The merging strategy used to merge an
     * entity with a sub-swarm.
     *
     * @return A tuple containing the main swarm in the first element and the
     * sub-swarms in the second element.
     */
    public static F<P2<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>>, P2<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>>> 
            absorb(final MergeDetection absorptionDetection, final MergeStrategy mainSwarmAbsorptionStrategy, final MergeStrategy subSwarmsAbsorptionStrategy) {
        return new F<P2<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>>, P2<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>>>() {
            @Override
            public P2<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>> f(P2<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>> swarms) {
                if (swarms._2().isEmpty() || swarms._1().getTopology().isEmpty()) {
                    return swarms.duplicate()._1();
                }

                P2<PopulationBasedAlgorithm, PopulationBasedAlgorithm> newPopulations =
                        absorbSingleSwarm(absorptionDetection, mainSwarmAbsorptionStrategy, subSwarmsAbsorptionStrategy)
                        .f(P.p(swarms._2().head(), swarmToAlgorithms.f(swarms._1())));

                P2<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>> joinedPopulations =
                        this.f(P.p(newPopulations._1(), swarms._2().tail()));

                return P.p(joinedPopulations._1(), List.cons(newPopulations._2(), joinedPopulations._2()));
            }
        };
    }

    /**
     * Creates sub-swarms around the detected niches.
     *
     * @param nicheDetection The detection strategy that determines if a niche 
     * should be created around an entity.
     * @param creationStrategy The strategy that creates the niches.
     * @param mainSwarmCreationMergingStrategy The merging strategy that decides
     * how entities that are removed from the main swarm interact with it.
     * 
     * @return A tuple containing the main swarm in the first element and the
     * sub-swarms in the second element.
     */
    public static F<P2<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>>, P2<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>>> 
            createNiches(final NicheDetection nicheDetection, final NicheCreationStrategy creationStrategy, final MergeStrategy mainSwarmCreationMergingStrategy) {
        return new F<P2<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>>, P2<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>>>() {
            @Override
            public P2<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>> f(P2<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>> swarms) {
                List<Entity> entities = List.<Entity>iterableList((Topology<Entity>) swarms._1().getTopology());
                List<Entity> filteredEntities = entities.filter(nicheDetection);

                // make sure there are enough entities to put into a new swarm
                if (filteredEntities.isEmpty() || entities.length() == 1) {
                    return swarms.duplicate()._1();
                }

                P2<PopulationBasedAlgorithm, PopulationBasedAlgorithm> createdSwarms = 
                        creationStrategy.f(swarms._1(), filteredEntities.head());

                P2<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>> s = 
                        this.f(P.p(createdSwarms._1(), swarms._2().cons(createdSwarms._2())));

                PopulationBasedAlgorithm newMainSwarm = mainSwarmCreationMergingStrategy
                        .f(s._1(), createdSwarms._2());

                return P.p(newMainSwarm, s._2());
            }
        };
    }
    
    /**
     * Converts a swarm into a list of single entity populations.
     */
    public static F<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>> 
            swarmToAlgorithms = new F<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>>() {
        @Override
        public List<PopulationBasedAlgorithm> f(final PopulationBasedAlgorithm a) {
            return entitiesToAlgorithms.f((Topology<Entity>) a.getTopology(), a);
        }
    };
    
    /**
     * Converts a single entity to a population of the given type.
     */
    public static F2<Entity, PopulationBasedAlgorithm, PopulationBasedAlgorithm> 
            entityToAlgorithm = new F2<Entity, PopulationBasedAlgorithm, PopulationBasedAlgorithm>() {
        @Override
        public PopulationBasedAlgorithm f(Entity e, PopulationBasedAlgorithm p) {
            PopulationBasedAlgorithm tmp = p.getClone();
            tmp.getTopology().clear();
            ((Topology<Entity>) tmp.getTopology()).add(e);

            return (PopulationBasedAlgorithm) tmp;
        }
    };
    
    /**
     * Converts a list of entities into single entity populations.
     */
    public static F2<Iterable<Entity>, PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>> 
            entitiesToAlgorithms = new F2<Iterable<Entity>, PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>>() {
        @Override
        public List<PopulationBasedAlgorithm> f(Iterable<Entity> a, PopulationBasedAlgorithm b) {
            return List.<Entity>iterableList(a).map(entityToAlgorithm.flip().f(b));
        }
    };
    
    /**
     * Returns an empty population of the given population type;
     */
    public static F<PopulationBasedAlgorithm, PopulationBasedAlgorithm> 
            emptyPopulation = new F<PopulationBasedAlgorithm, PopulationBasedAlgorithm>() {
        @Override
        public PopulationBasedAlgorithm f(PopulationBasedAlgorithm a) {
            PopulationBasedAlgorithm tmp = a.getClone();
            tmp.getTopology().clear();
            return tmp;
        }
    };

    /**
     * Makes sure a swarm has a neighborhood best and the correct particle
     * behavior.
     *
     * @param pb The particle behavior to give each entity in the swarm.
     * 
     * @return The enforced swarm.
     */
    public static F<PopulationBasedAlgorithm, PopulationBasedAlgorithm> enforceTopology(final ParticleBehavior pb) {
        return new F<PopulationBasedAlgorithm, PopulationBasedAlgorithm>() {
            @Override
            public PopulationBasedAlgorithm f(PopulationBasedAlgorithm a) {
                PopulationBasedAlgorithm tmp = a.getClone();

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

    /**
     * Given a main population and a list of sub-populations, the main swarms
     * topology is enforced with the given behavior.
     * 
     * @param pb The particle behavior to give each entity in the main swarm.
     * @return 
     */
    public static F<P2<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>>, P2<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>>> 
            enforceMainSwarmTopology(final ParticleBehavior pb) {
        return new F<P2<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>>, P2<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>>>() {
            @Override
            public P2<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>> f(P2<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>> a) {
                return P.p(enforceTopology(pb).f(a._1()), a._2());
            }
        };
    }
    
    /**
     * Combines a given main swarm and list of sub-swarms into a tuple which can 
     * be used by these functions.
     */
    public static F<P2<PopulationBasedAlgorithm, java.util.List<PopulationBasedAlgorithm>>, P2<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>>> 
            combineSwarms = new F<P2<PopulationBasedAlgorithm, java.util.List<PopulationBasedAlgorithm>>, P2<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>>>() {
        @Override
        public P2<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>> f(P2<PopulationBasedAlgorithm, java.util.List<PopulationBasedAlgorithm>> a) {
            return P.p(a._1(), List.<PopulationBasedAlgorithm>iterableList(a._2()));
        }
    };
    
    /**
     * Given the tuple of swarms, first performs an iteration of the main swarm 
     * and then an iteration of each sub-swarm.
     */
    public static F<P2<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>>, P2<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>>> 
            iterateAllSwarms = new F<P2<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>>, P2<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>>>() {
        @Override
        public P2<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>> f(P2<PopulationBasedAlgorithm, List<PopulationBasedAlgorithm>> a) {
            a._1().performIteration();
            a._2().foreach(new Effect<PopulationBasedAlgorithm>() {
                @Override
                public void e(PopulationBasedAlgorithm c) {
                    c.performIteration();
                }
            });

            return a;
        }
    };
}
