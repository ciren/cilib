/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
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
     */
    public static <T extends Entity> Set<T> getNeighbourhoodBestEntities(Topology<T> p) {
        return getNeighbourhoodBestEntities(p, new AscendingFitnessComparator<T>());
    }

    /**
     * Gather the best entity of each neighbourhood (in this {@link Topology}) in a
     * {@link Set} (duplicates are not allowed) and return them. A single {@link Entity} may
     * dominate in more than one neighbourhood, but we just want unique entities.
     */
    public static <E extends Entity> Set<E> getNeighbourhoodBestEntities(Topology<E> p, Comparator<? super E> c) {
        Set<E> nBests = new LinkedHashSet<E>(p.size());

        for (E e : p) {
            E best = getBestEntity(p.neighbourhood(e), c);
            if (best != null) {
                nBests.add(best);
            }
        }

        return nBests;
    }

    /**
     * Returns the current best entity from a given topology based on the current
     * fitness of the entities.
     */
    public static <T extends Entity> T getBestEntity(Collection<T> p) {
        return getBestEntity(p, new AscendingFitnessComparator<T>());
    }

    /**
     * Returns an entity from a given topology using the given comparator.
     */
    public static <T extends Entity> T getBestEntity(Collection<T> p, Comparator<? super T> c) {
        if (p.isEmpty()) {
            return null;
        }

        List<T> tmp = new ArrayList(p);
        Collections.sort(tmp, c);
        return tmp.get(tmp.size() - 1);
    }

    /**
     * Returns the current best entity from the neighbourhood of an entity
     * in a topology based on the current fitness of the entities.
     */
    public static <T extends Entity> T getNeighbourhoodBest(Topology<T> p, T e) {
        return getNeighbourhoodBest(p, e, new AscendingFitnessComparator<T>());
    }

    /**
     * Returns an entity from the neighbourhood of an entity in a topology using
     * the given comparator.
     */
    public static <T extends Entity> T getNeighbourhoodBest(Topology<T> p, T e, Comparator<? super T> c) {
        return getBestEntity(p.neighbourhood(e), c);
    }

}
