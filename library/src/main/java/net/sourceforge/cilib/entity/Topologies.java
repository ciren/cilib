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

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.comparator.AscendingFitnessComparator;
import net.sourceforge.cilib.entity.topologies.Neighbourhood;
import net.sourceforge.cilib.util.distancemeasure.DistanceMeasure;
import net.sourceforge.cilib.util.distancemeasure.EuclideanDistanceMeasure;

import com.google.common.collect.Lists;

import fj.Equal;
import fj.F;
import fj.F2;
import fj.Ord;
import fj.Ordering;
import fj.data.List;
import fj.data.Stream;

/**
 * Topology related utilities.
 */
public final class Topologies {

    private Topologies() {
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

    public static <E> Neighbourhood<E> gbest() {
        return new Neighbourhood<E>() {
            @Override
            public List<E> f(List<E> list, E element) {
                return list;
            }
        };
    }

    public static <E> Neighbourhood<E> lbest(final int n) {
        return new Neighbourhood<E>() {
            @Override
            public List<E> f(final List<E> list, final E element) {
                java.util.List<E> inner = Lists.newArrayList(list);
                int ts = inner.size();
                int x = (inner.indexOf(element) - (n / 2) + ts) % ts;

                return Stream.cycle(list.toStream()).drop(x).take(n).toList();
            }
        };
    }

    public static <E> Neighbourhood<E> lbest3() {
        return lbest(3);
    }

    public static <E> Neighbourhood<E> lbest5() {
        return lbest(5);
    }

    public static <E> Neighbourhood<E> hypercube(final int n) {
        return new Neighbourhood<E>() {
            @Override
            public List<E> f(final List<E> list, final E current) {
                final int index = list.elementIndex(Equal.<E>anyEqual(), current).orSome(-1);
                return List.range(0, n).map(new F<Integer, E>() {
                    @Override
                    public E f(Integer a) {
                        return list.index(index ^ Double.valueOf(Math.pow(2, a)).intValue());
                    }
                });
            }
        };
    }

    public static <E> Neighbourhood<E> hypercube5() {
        return hypercube(5);
    }

    public static <E extends Entity> Neighbourhood<E> speciation(final DistanceMeasure distance, final ControlParameter radius, final ControlParameter n) {
        return new Neighbourhood<E>() {
            @Override
            public List<E> f(final List<E> list, final E current) {
                if (list.isEmpty()) {
                    return List.<E>nil();
                }

                final List<E> sorted = list.sort(Ord.<E>ord(new F2<E, E, Ordering>() {
                    @Override
                    public Ordering f(E a, E b) {
                        return Ordering.values()[-a.getFitness().compareTo(b.getFitness()) + 1];
                    }
                }.curry()));

                List<E> neighbours = sorted.filter(new F<E, Boolean>() {
                    @Override
                    public Boolean f(E a) {
                        return distance.distance(a.getCandidateSolution(), sorted.head().getCandidateSolution()) < radius.getParameter();
                    }
                }).take((int) n.getParameter());

                if(neighbours.exists(new F<E, Boolean>() {
                    @Override
                    public Boolean f(E a) {
                        return a.equals(current);
                    }
                })) {
                    return neighbours;
                } else {
                    return this.f(sorted.minus(Equal.<E>anyEqual(), neighbours), current);
                }
            }
        };
    }

    public static <E extends Entity> Neighbourhood<E> speciation() {
        return speciation(new EuclideanDistanceMeasure(), ConstantControlParameter.of(0.1), ConstantControlParameter.of(20));
    }

    public static <E extends Entity> Neighbourhood<E> speciation(final ControlParameter radius) {
        return speciation(new EuclideanDistanceMeasure(), radius, ConstantControlParameter.of(20));
    }

    public static <E extends Entity> Neighbourhood<E> speciation(final ControlParameter radius, final ControlParameter n) {
        return speciation(new EuclideanDistanceMeasure(), radius, n);
    }

    public static <E> Neighbourhood<E> vonNeumann() {
        return new Neighbourhood<E>() {
            private E find(List<E> list, int n, int r, int c) {
                return list.index(r * n + c);
            }

            @Override
            public List<E> f(final List<E> list, final E target) {
                final int np = list.length();
                final int index = Lists.newArrayList(list).indexOf(target);
                final int sqSide = (int) Math.round(Math.sqrt(np));
                final int nRows = (int) Math.ceil(np / (double) sqSide);
                final int row = index / sqSide;
                final int col = index % sqSide;

                final F<Integer, Integer> colsInRow = new F<Integer, Integer>() {
                    @Override
                    public Integer f(Integer r) {
                        return r == nRows - 1 ? np - r * sqSide : sqSide;
                    }
                };

                final E north = find(list, sqSide, (row - 1 + nRows) % nRows - ((col >= colsInRow.f((row - 1 + nRows) % nRows)) ? 1 : 0), col);
                final E south = find(list, sqSide, (row + 1) % nRows - ((col >= colsInRow.f((row + 1) % nRows)) ? sqSide : 0), col);
                final E east = find(list, sqSide, row, (col + 1) % colsInRow.f(row));
                final E west = find(list, sqSide, row, (col - 1 + colsInRow.f(row)) % colsInRow.f(row));

                return List.list(target, north, east, south, west);
            }
        };
    }
}
