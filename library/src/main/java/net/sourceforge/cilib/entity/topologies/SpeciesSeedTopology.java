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
package net.sourceforge.cilib.entity.topologies;

import fj.*;
import fj.data.List;
import java.util.Iterator;
import java.util.NoSuchElementException;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.AbstractTopology;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.IndexedIterator;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 * <p>
 * @INPROCEEDINGS{Li04adaptivelychoosing,
 *    author = {Xiaodong Li},
 *   title = {Adaptively Choosing Neighbourhood Bests Using Species in a Particle Swarm Optimizer for Multimodal Function Optimization},
 *   booktitle = {in GECCO-2004},
 *   year = {2004},
 *   pages = {105--116},
 *   publisher = {Springer-Verlag}
 * }
 * </p>
 */
public class SpeciesSeedTopology<E extends Entity> extends AbstractTopology<E> {

    private DistanceMeasure distanceMeasure;
    private ControlParameter radius;

    public SpeciesSeedTopology() {
        this.distanceMeasure = new EuclideanDistanceMeasure();
        this.radius = ConstantControlParameter.of(100);
        this.neighbourhoodSize = ConstantControlParameter.of(20);
    }

    public SpeciesSeedTopology(SpeciesSeedTopology copy) {
        super(copy);
        this.distanceMeasure = copy.distanceMeasure;
        this.radius = copy.radius.getClone();
        this.neighbourhoodSize = copy.neighbourhoodSize.getClone();
    }

    @Override
    public SpeciesSeedTopology<E> getClone() {
        return new SpeciesSeedTopology(this);
    }

    @Override
    public Iterator<E> neighbourhood(Iterator<? extends Entity> iterator) {
        return new SameSpeciesTopologyIterator<E>(this, (IndexedIterator) iterator);
    }

    public void setRadius(ControlParameter radius) {
        this.radius = radius;
    }

    @Override
    public void setNeighbourhoodSize(ControlParameter maxNeighbourhoodSize) {
        this.neighbourhoodSize = maxNeighbourhoodSize;
    }

    public void setDistanceMeasure(DistanceMeasure distanceMeasure) {
        this.distanceMeasure = distanceMeasure;
    }

    @Override
    public int getNeighbourhoodSize() {
        return Math.min((int) neighbourhoodSize.getParameter(), entities.size());
    }

    public DistanceMeasure getDistanceMeasure() {
        return distanceMeasure;
    }

    public ControlParameter getRadius() {
        return radius;
    }

    public static <T extends Entity> F<P2<T ,Integer>, Boolean> inRadius(final DistanceMeasure distance, final ControlParameter radius, final T other) {
        return new F<P2<T ,Integer>, Boolean>() {
            @Override
            public Boolean f(P2<T ,Integer> a) {
                if (distance.distance(a._1().getCandidateSolution(), other.getCandidateSolution()) < radius.getParameter()) {
                    return true;
                }

                return false;
            }
        };
    }

    public static <T extends Entity> F<P2<T, Integer>, Boolean> exists(final int index) {
        return new F<P2<T, Integer>, Boolean>() {
            @Override
            public Boolean f(P2<T, Integer> a) {
                if (a._2() == index) {
                    return true;
                }

                return false;
            }
        };
    }

    public static F<List<P2<Entity, Integer>>, List<Integer>> getNeighbourhood(final DistanceMeasure distance, 
            final ControlParameter radius, final ControlParameter size, final int index) {
        return new F<List<P2<Entity, Integer>>, List<Integer>>() {
            @Override
            public List<Integer> f(List<P2<Entity, Integer>> a) {
                if (a.isEmpty()) {
                    return List.<Integer>nil();
                }
                
                List<P2<Entity, Integer>> sorted = a.sort(Ord.<P2<Entity, Integer>>ord(new F2<P2<Entity, Integer>, P2<Entity, Integer>, Ordering>() {
                        @Override
                        public Ordering f(P2<Entity, Integer> a, P2<Entity, Integer> b) {
                            int result = -a._1().getFitness().compareTo(b._1().getFitness()) + 1;
                            return Ordering.values()[result];
                        }
                    }.curry()));
                List<P2<Entity, Integer>> filtered = sorted.filter(inRadius(distance, radius, sorted.head()._1()));
                List<P2<Entity, Integer>> neighbours = filtered.take((int) size.getParameter());
                
                if(neighbours.exists(exists(index))) {
                    return neighbours.map(P2.<Entity, Integer>__2());
                }
                
                List<P2<Entity, Integer>> remainder = sorted.minus(Equal.<P2<Entity, Integer>>equal(
                    new F2<P2<Entity, Integer>, P2<Entity, Integer>, Boolean>() {
                        @Override
                        public Boolean f(P2<Entity, Integer> a, P2<Entity, Integer> b) {
                            return a._2() == b._2();
                        }
                    }.curry()), neighbours);
                return this.f(remainder);
            }
        };
    }

    private class SameSpeciesTopologyIterator<T extends Entity> implements IndexedIterator<T> {

        private SpeciesSeedTopology<T> topology;
        private List<Integer> neighbours;
        private int index;

        public SameSpeciesTopologyIterator(SpeciesSeedTopology<T> topology, IndexedIterator<T> iterator) {
            if (iterator.getIndex() == -1) {
                throw new IllegalStateException();
            }

            List<P2<Entity, Integer>> newTopology = List.<Entity>iterableList((Topology<Entity>) topology.getClone()).zipIndex();
            this.neighbours = getNeighbourhood(distanceMeasure, radius, neighbourhoodSize, iterator.getIndex()).f(newTopology);
            this.topology = topology;
            this.index = -1;
        }

        @Override
        public int getIndex() {
            return index;
        }

        @Override
        public boolean hasNext() {
            return neighbours.isNotEmpty();
        }

        @Override
        public T next() {
            if (neighbours.isEmpty()) {
                throw new NoSuchElementException();
            }

            index = neighbours.head();
            neighbours = neighbours.orTail(P.p(List.<Integer>nil()));
            return topology.entities.get(index);
        }

        @Override
        public void remove() {
            if (index == -1) {
                throw new NoSuchElementException();
            }
            
            topology.entities.remove(index);
        }
    }
}
