/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import net.sourceforge.cilib.entity.comparator.AscendingFitnessComparator;

/**
 * Topology related utilities.
 */
public final class Topologies {

    private Topologies() {
    }

    /**
     * Obtain the best entity from each neighbourhood and return them.
     * @param <T> The {@code Entity} type.
     * @param topology The topology to query.
     * @return a {@code Set} of neighbourhood best entities.
     */
    public static <T extends Entity> Set<T> getNeighbourhoodBestEntities(Topology<T> topology) {
        return getNeighbourhoodBestEntities(topology, new AscendingFitnessComparator<T>());
    }

    /**
     * Gather the best entity of each neighbourhood (in this {@link Topology}) in a
     * {@link Set} (duplicates are not allowed) and return them. A single {@link Entity} may
     * dominate in more than one neighbourhood, but we just want unique entities.
     *
     * @param <T> The entity type.
     * @param topology The topology to query.
     * @param comparator The comparator to use.
     * @return a {@link Set} consisting of the best entity of each neighbourhood in the
     *         topology
     */
    public static <T extends Entity> Set<T> getNeighbourhoodBestEntities(Topology<T> topology, Comparator<? super T> comparator) {
        // a Set does not allow duplicates
        Set<T> neighbourhoodBests = new HashSet<T>(topology.size());
        Iterator<T> topologyIterator = topology.iterator();

        // iterate over all entities in the topology
        while (topologyIterator.hasNext()) {
            topologyIterator.next();
            T currentBestEntity = getIteratorBest(topology.neighbourhood(topologyIterator), comparator);

            // only gather unique entities
            if (currentBestEntity != null) {
                neighbourhoodBests.add(currentBestEntity);
            }
        }

        return neighbourhoodBests;
    }

    /**
     * Returns the current best entity from a given topology based on the current 
     * fitness of the entities.
     * 
     * @param <T> The entity type.
     * @param topology The topology to query
     * @return The current best entity in the topology
     */
    public static <T extends Entity> T getBestEntity(Topology<T> topology) {
        return getBestEntity(topology, new AscendingFitnessComparator<T>());
    }

    /**
     * Returns an entity from a given topology using the given comparator.
     * 
     * @param <T> The entity type.
     * @param topology The topology to query.
     * @param comparator The comparator to use to compare entities.
     * @return The best entity in the topology according to the comparator.
     */
    public static <T extends Entity> T getBestEntity(Topology<T> topology, Comparator<? super T> comparator) {
        return getIteratorBest(topology.iterator(), comparator);
    }
    
    /**
     * Returns the current best entity from the neighbourhood of an entity
     * in a topology based on the current fitness of the entities.
     * 
     * @param <T> The entity type.
     * @param topology The topology to query
     * @return The current best entity in the neighbourhood.
     */
    public static <T extends Entity> T getNeighbourhoodBest(Topology<T> topology, T entity) {
        return getNeighbourhoodBest(topology, entity, new AscendingFitnessComparator<T>());
    }

    /**
     * Returns an entity from the neighbourhood of an entity in a topology using 
     * the given comparator.
     * 
     * @param <T> The entity type.
     * @param topology The topology to query.
     * @param comparator The comparator to use to compare entities.
     * @return The best entity in the topology according to the comparator.
     */
    public static <T extends Entity> T getNeighbourhoodBest(Topology<T> topology, T entity, Comparator<? super T> comparator) {
        Iterator<T> entities = topology.iterator();
        Iterator<T> neighbours = null;
        
        while (entities.hasNext()) {
            if (entities.next().equals(entity)) {
                neighbours = topology.neighbourhood(entities);
                break;
            }
        }
        
        return getIteratorBest(neighbours, comparator);
    }
    
    /**
     * Helper method which compares entities in an iterator according to a 
     * comparator.
     * 
     * @param <T> The entity type.
     * @param iterator The iterator to query.
     * @param comparator 
     * @return 
     */
    private static <T extends Entity> T getIteratorBest(Iterator<T> iterator, Comparator<? super T> comparator) {
        T bestEntity = null;

        while (iterator.hasNext()) {
            T entity = iterator.next();
            if (bestEntity == null) {
                bestEntity = entity;
                continue;
            }

            if (comparator.compare(bestEntity, entity) < 0) { // bestEntity is worse than entity
                bestEntity = entity;
            }
        }

        return bestEntity;
    }

}
