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
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.problem.OptimisationProblem;
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
    public static F<NichingSwarms, NichingSwarms> 
            merge(final MergeDetection mergeDetection, final MergeStrategy mainSwarmMergeStrategy, final MergeStrategy subSwarmsMergeStrategy) {
        return new F<NichingSwarms, NichingSwarms>() {
            @Override
            public NichingSwarms f(NichingSwarms swarms) {
                if (swarms._2().isEmpty() || swarms._2().length() == 1) {
                    return swarms;
                }

                PopulationBasedAlgorithm newMainSwarm = swarms._2().tail()
                        .filter(mergeDetection.f(swarms._2().head()))
                        .foldLeft(mainSwarmMergeStrategy, swarms._1());

                PopulationBasedAlgorithm mergedSwarms = swarms._2().tail()
                        .filter(mergeDetection.f(swarms._2().head()))
                        .foldLeft(subSwarmsMergeStrategy, swarms._2().head());

                NichingSwarms newSwarms =
                        this.f(NichingSwarms.of(newMainSwarm, swarms._2().tail().removeAll(mergeDetection.f(swarms._2().head()))));

                return NichingSwarms.of(newSwarms._1(), List.cons(mergedSwarms, newSwarms._2()));
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
    public static F<NichingSwarms, P2<PopulationBasedAlgorithm, PopulationBasedAlgorithm>> 
            absorbSingleSwarm(final MergeDetection absorptionDetection, final MergeStrategy mainSwarmAbsorptionStrategy, final MergeStrategy subSwarmsAbsorptionStrategy) {
        return new F<NichingSwarms, P2<PopulationBasedAlgorithm, PopulationBasedAlgorithm>>() {
            @Override
            public P2<PopulationBasedAlgorithm, PopulationBasedAlgorithm> f(NichingSwarms swarms) {
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
    public static F<NichingSwarms, NichingSwarms> 
            absorb(final MergeDetection absorptionDetection, final MergeStrategy mainSwarmAbsorptionStrategy, final MergeStrategy subSwarmsAbsorptionStrategy) {
        return new F<NichingSwarms, NichingSwarms>() {
            @Override
            public NichingSwarms f(NichingSwarms swarms) {
                if (swarms._2().isEmpty() || swarms._1().getTopology().isEmpty()) {
                    return swarms;
                }

                P2<PopulationBasedAlgorithm, PopulationBasedAlgorithm> newPopulations =
                        absorbSingleSwarm(absorptionDetection, mainSwarmAbsorptionStrategy, subSwarmsAbsorptionStrategy)
                        .f(NichingSwarms.of(swarms._2().head(), swarmToAlgorithms.f(swarms._1())));

                NichingSwarms joinedPopulations =
                        this.f(NichingSwarms.of(newPopulations._1(), swarms._2().tail()));

                return NichingSwarms.of(joinedPopulations._1(), List.cons(newPopulations._2(), joinedPopulations._2()));
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
    public static F<NichingSwarms, NichingSwarms> 
            createNiches(final NicheDetection nicheDetection, final NicheCreationStrategy creationStrategy, final MergeStrategy mainSwarmCreationMergingStrategy) {
        return new F<NichingSwarms, NichingSwarms>() {
            @Override
            public NichingSwarms f(NichingSwarms swarms) {
                List<Entity> entities = List.<Entity>iterableList((Topology<Entity>) swarms._1().getTopology());
                List<Entity> filteredEntities = entities.filter(nicheDetection);

                // make sure there are enough entities to put into a new swarm
                if (filteredEntities.isEmpty() || entities.length() == 1) {
                    return swarms;
                }

                P2<PopulationBasedAlgorithm, PopulationBasedAlgorithm> createdSwarms = 
                        creationStrategy.f(swarms._1(), filteredEntities.head());

                NichingSwarms s = 
                        this.f(NichingSwarms.of(createdSwarms._1(), swarms._2().cons(createdSwarms._2())));

                PopulationBasedAlgorithm newMainSwarm = mainSwarmCreationMergingStrategy
                        .f(s._1(), createdSwarms._2());

                return NichingSwarms.of(newMainSwarm, s._2());
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
    public static F<NichingSwarms, NichingSwarms> 
            enforceMainSwarmTopology(final ParticleBehavior pb) {
        return new F<NichingSwarms, NichingSwarms>() {
            @Override
            public NichingSwarms f(NichingSwarms a) {
                return NichingSwarms.of(enforceTopology(pb).f(a._1()), a._2());
            }
        };
    }
    
    /**
     * Combines a given main swarm and list of sub-swarms into a tuple which can 
     * be used by these functions.
     */
    public static F<P2<PopulationBasedAlgorithm, java.util.List<PopulationBasedAlgorithm>>, NichingSwarms> 
            combineSwarms = new F<P2<PopulationBasedAlgorithm, java.util.List<PopulationBasedAlgorithm>>, NichingSwarms>() {
        @Override
        public NichingSwarms f(P2<PopulationBasedAlgorithm, java.util.List<PopulationBasedAlgorithm>> a) {
            return NichingSwarms.of(a._1(), List.<PopulationBasedAlgorithm>iterableList(a._2()));
        }
    };
    
    public static F<NichingSwarms, NichingSwarms> 
            initialiseMainSwarm = new F<NichingSwarms, NichingSwarms>() {
        @Override
        public NichingSwarms f(NichingSwarms a) {
            ((AbstractAlgorithm) a._1()).initialise();

            return a;
        }
    };
    
    public static F<PopulationBasedAlgorithm, Boolean> isFinished = new F<PopulationBasedAlgorithm, Boolean>() {
        @Override
        public Boolean f(PopulationBasedAlgorithm b) {
            return b.isFinished();
        }                    
    };
    
    public static Effect<PopulationBasedAlgorithm> iterateSwarm = new Effect<PopulationBasedAlgorithm>() {
        @Override
        public void e(PopulationBasedAlgorithm c) {
            if (!c.isFinished()) {
                c.performIteration();
            }
        }
    };
    
    public static Effect<PopulationBasedAlgorithm> setOptimisationProblem(final OptimisationProblem problem) {
        return new Effect<PopulationBasedAlgorithm>() {
            @Override
            public void e(PopulationBasedAlgorithm c) {
                c.setOptimisationProblem(problem);
            }
        };
    }
    
    public static F<NichingSwarms, NichingSwarms> 
            iterateMainSwarm = new F<NichingSwarms, NichingSwarms>() {
        @Override
        public NichingSwarms f(NichingSwarms a) {
            iterateSwarm.e(a._1());
            return a;
        }
    };
    
    public static F<NichingSwarms, NichingSwarms> 
            iterateSubswarms = new F<NichingSwarms, NichingSwarms>() {
        @Override
        public NichingSwarms f(NichingSwarms a) {
            a._2().foreach(iterateSwarm);
            return a;
        }
    };
}
