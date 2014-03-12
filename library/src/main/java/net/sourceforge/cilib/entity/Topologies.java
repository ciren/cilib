/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;

import net.sourceforge.cilib.entity.comparator.AscendingFitnessComparator;
import net.sourceforge.cilib.entity.topologies.Neighbourhood;
import net.sourceforge.cilib.pso.particle.Particle;

import com.google.common.collect.Lists;

import fj.F;
import fj.data.List;

/**
 * Topology related utilities.
 */
public final class Topologies {

    private Topologies() {
    }

    public static Set<Particle> getNBestEntities(List<Particle> p) {
        Set<Particle> nBests = new LinkedHashSet<Particle>(p.length());

        for (Particle e : p) {
            Particle best = e.getNeighbourhoodBest();
            if (best != null) {
                nBests.add(best);
            }
        }

        return nBests;
    }

    /**
     * Obtain the best entity from each neighbourhood and return them.
     */
    public static <T extends Entity> Set<T> getNeighbourhoodBestEntities(List<T> p, Neighbourhood<T> neighbourhood) {
        return getNeighbourhoodBestEntities(p, neighbourhood, new AscendingFitnessComparator<T>());
    }

    /**
     * Gather the best entity of each neighbourhood (in this {@link Topology}) in a
     * {@link Set} (duplicates are not allowed) and return them. A single {@link Entity} may
     * dominate in more than one neighbourhood, but we just want unique entities.
     */
    public static <E extends Entity> Set<E> getNeighbourhoodBestEntities(List<E> p,
            Neighbourhood<E> neighbourhood, Comparator<? super E> c) {
        Set<E> nBests = new LinkedHashSet<E>(p.length());

        F<E, List<E>> partial = neighbourhood.f(p);

        for (E e : p) {
            E best = getBestEntity(partial.f(e), c);
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
    public static <T extends Entity> T getBestEntity(List<T> p) {
        return getBestEntity(p, new AscendingFitnessComparator<T>());
    }

    /**
     * Returns an entity from a given topology using the given comparator.
     */
    public static <T extends Entity> T getBestEntity(List<T> p, Comparator<? super T> c) {
        if (p.isEmpty()) {
            return null;
        }

        java.util.List<T> tmp = Lists.newArrayList(p);
        Collections.sort(tmp, c);
        return tmp.get(tmp.size() - 1);
    }

    /**
     * Returns the current best entity from the neighbourhood of an entity
     * in a topology based on the current fitness of the entities.
     */
    public static <T extends Entity> T getNeighbourhoodBest(List<T> p, T e, Neighbourhood<T> neighbourhood) {
        return getNeighbourhoodBest(p, e, neighbourhood, new AscendingFitnessComparator<T>());
    }

    /**
     * Returns an entity from the neighbourhood of an entity in a topology using
     * the given comparator.
     */
    public static <T extends Entity> T getNeighbourhoodBest(List<T> p, T e, Neighbourhood<T> neighbourhood, Comparator<? super T> c) {
        return getBestEntity(neighbourhood.f(p, e), c);
    }
}
