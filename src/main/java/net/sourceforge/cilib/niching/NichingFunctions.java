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
package net.sourceforge.cilib.niching;

import fj.F;
import fj.P;
import fj.P2;
import fj.data.List;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.niching.creation.NicheCreationStrategy;
import net.sourceforge.cilib.niching.creation.NicheDetection;
import net.sourceforge.cilib.niching.merging.MergeStrategy;
import net.sourceforge.cilib.niching.merging.StandardMergeStrategy;
import net.sourceforge.cilib.niching.merging.detection.MergeDetection;
import net.sourceforge.cilib.util.functions.Populations;

/**
 * These are generic functions used in NichingFunctions algorithms. e.g. Merging, absorption, sub-population creation. They
 * use given strategies to accomplish a task and can be seen as higher order functions.
 */
public final class NichingFunctions {
    
    public abstract static class NichingFunction extends F<NichingSwarms, NichingSwarms> {
    }

    /**
     * Merges sub-swarms that satisfy given conditions. Also handles merging with the main swarm if applicable.
     *
     * @param mergeDetection The detection strategy that determines if two swarms must be merged.
     * @param mainSwarmMergeStrategy The merging strategy that decides how the merged swarm interacts with the main
     * swarm.
     * @param subSwarmsMergeStrategy The merging strategy used to merge two sub-swarms.
     *
     * @return A tuple containing the main swarm in the first element and the sub-swarms in the second element.
     */
    public static NichingFunction merge(final MergeDetection mergeDetection,
            final MergeStrategy mainSwarmMergeStrategy,
            final MergeStrategy subSwarmsMergeStrategy) {
        return new NichingFunction() {

            @Override
            public NichingSwarms f(NichingSwarms swarms) {
                if (swarms.getSubswarms().isEmpty() || swarms.getSubswarms().length() == 1) {
                    return swarms;
                }

                PopulationBasedAlgorithm newMainSwarm = swarms.getSubswarms().tail()
                        .filter(mergeDetection.f(swarms.getSubswarms().head()))
                        .foldLeft(mainSwarmMergeStrategy, swarms.getMainSwarm());

                PopulationBasedAlgorithm mergedSwarms = swarms.getSubswarms().tail()
                        .filter(mergeDetection.f(swarms.getSubswarms().head()))
                        .foldLeft(subSwarmsMergeStrategy, swarms.getSubswarms().head());

                NichingSwarms newSwarms =
                        this.f(NichingSwarms.of(newMainSwarm, 
                        swarms.getSubswarms().tail().removeAll(mergeDetection.f(swarms.getSubswarms().head()))));

                return NichingSwarms.of(newSwarms.getMainSwarm(), List.cons(mergedSwarms, newSwarms.getSubswarms()));
            }
        };
    }

    /**
     * Performs absorption between one sub-swarm and the main swarm.
     *
     * @param absorptionDetection The detection strategy that determines if an entity in the main swarm must be
     * absorbed.
     * @param mainSwarmAbsorptionStrategy The merging strategy that decides how the absorbed entities interact with
     * the main swarm.
     * @param subSwarmsAbsorptionStrategy The merging strategy used to merge an entity with a sub-swarm.
     *
     * @return A tuple containing the main swarm in the first element and the sub-swarm with absorbed entities in
     * the second element.
     */
    public static F<NichingSwarms, P2<PopulationBasedAlgorithm, PopulationBasedAlgorithm>> absorbSingleSwarm(
            final MergeDetection absorptionDetection,
            final MergeStrategy mainSwarmAbsorptionStrategy,
            final MergeStrategy subSwarmsAbsorptionStrategy) {
        return new F<NichingSwarms, P2<PopulationBasedAlgorithm, PopulationBasedAlgorithm>>() {

            @Override
            public P2<PopulationBasedAlgorithm, PopulationBasedAlgorithm> f(NichingSwarms swarms) {
                PopulationBasedAlgorithm newSubSwarm = swarms.getSubswarms()
                        .filter(absorptionDetection.f(swarms.getMainSwarm()))
                        .foldLeft(subSwarmsAbsorptionStrategy, swarms.getMainSwarm());

                PopulationBasedAlgorithm unmergedSwarms = swarms.getSubswarms()
                        .removeAll(absorptionDetection.f(swarms.getMainSwarm()))
                        .foldLeft(new StandardMergeStrategy(), Populations.emptyPopulation().f(swarms.getSubswarms().head()));

                PopulationBasedAlgorithm mergedSwarms = swarms.getSubswarms()
                        .filter(absorptionDetection.f(swarms.getMainSwarm()))
                        .foldLeft(new StandardMergeStrategy(), Populations.emptyPopulation().f(swarms.getSubswarms().head()));

                PopulationBasedAlgorithm newMainSwarm = mainSwarmAbsorptionStrategy.f(unmergedSwarms, mergedSwarms);

                return P.p(newMainSwarm, newSubSwarm);
            }
        };
    }

    /**
     * Performs absorption between the main swarm and each sub-swarm. Each entity in the main swarm gets placed
     * into a swarm of its own. This allows the merging strategies to be used instead of duplicating the strategies
     * for entities.
     *
     * @param absorptionDetection The detection strategy that determines if an entity in the main swarm must be
     * absorbed.
     * @param mainSwarmAbsorptionStrategy The merging strategy that decides how the absorbed entities interact with
     * the main swarm.
     * @param subSwarmsAbsorptionStrategy The merging strategy used to merge an entity with a sub-swarm.
     *
     * @return A tuple containing the main swarm in the first element and the sub-swarms in the second element.
     */
    public static NichingFunction absorb(final MergeDetection absorptionDetection,
            final MergeStrategy mainSwarmAbsorptionStrategy,
            final MergeStrategy subSwarmsAbsorptionStrategy) {
        return new NichingFunction() {

            @Override
            public NichingSwarms f(NichingSwarms swarms) {
                if (swarms.getSubswarms().isEmpty() || swarms.getMainSwarm().getTopology().isEmpty()) {
                    return swarms;
                }

                P2<PopulationBasedAlgorithm, PopulationBasedAlgorithm> newPopulations =
                        absorbSingleSwarm(absorptionDetection, mainSwarmAbsorptionStrategy, subSwarmsAbsorptionStrategy)
                        .f(NichingSwarms.of(swarms.getSubswarms().head(), Populations.populationToAlgorithms().f(swarms.getMainSwarm())));

                NichingSwarms joinedPopulations = this.f(NichingSwarms.of(newPopulations._1(), swarms.getSubswarms().tail()));

                return NichingSwarms.of(joinedPopulations.getMainSwarm(), List.cons(newPopulations._2(), joinedPopulations.getSubswarms()));
            }
        };
    }

    /**
     * Creates sub-swarms around the detected niches.
     *
     * @param nicheDetection The detection strategy that determines if a niche should be created around an entity.
     * @param creationStrategy The strategy that creates the niches.
     * @param mainSwarmCreationMergingStrategy The merging strategy that decides how entities that are removed from
     * the main swarm interact with it.
     *
     * @return A tuple containing the main swarm in the first element and the sub-swarms in the second element.
     */
    public static NichingFunction createNiches(final NicheDetection nicheDetection,
            final NicheCreationStrategy creationStrategy, final MergeStrategy mainSwarmCreationMergingStrategy) {
        return new NichingFunction() {

            @Override
            public NichingSwarms f(NichingSwarms swarms) {
                List<Entity> entities = List.<Entity>iterableList((Topology<Entity>) swarms.getMainSwarm().getTopology());
                List<Entity> filteredEntities = entities.filter(nicheDetection.f(swarms.getMainSwarm()));

                // make sure there are entities to put into a new swarm
                // TODO: if (filteredEntities.isEmpty()) {
                if (filteredEntities.isEmpty() || entities.length() == 1) {
                    return swarms;
                }

                NichingSwarms createdSwarms = creationStrategy.f(swarms, filteredEntities.head());

                NichingSwarms s = this.f(NichingSwarms.of(createdSwarms.getMainSwarm(),
                        swarms.getSubswarms().cons(createdSwarms.getSubswarms().head())));

                PopulationBasedAlgorithm newMainSwarm = mainSwarmCreationMergingStrategy.f(s.getMainSwarm(),
                        createdSwarms.getSubswarms().head());

                return NichingSwarms.of(newMainSwarm, s.getSubswarms());
            }
        };
    }
}
