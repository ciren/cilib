/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.topologies;

import com.google.common.collect.UnmodifiableIterator;
import fj.*;
import fj.data.List;
import java.util.Iterator;
import java.util.NoSuchElementException;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.util.distancemeasure.DistanceMeasure;
import net.sourceforge.cilib.util.distancemeasure.EuclideanDistanceMeasure;

/**
 * <p>
 * @INPROCEEDINGS{Li04adaptivelychoosing,
 * author = {Xiaodong Li},
 * title = {Adaptively Choosing Neighbourhood Bests Using Species in a Particle Swarm Optimizer for Multimodal Function Optimization},
 * booktitle = {in GECCO-2004},
 * year = {2004},
 * pages = {105--116},
 * publisher = {Springer-Verlag}
 * }
 * </p>
 */
public class SpeciationTopology<E extends Entity> extends AbstractTopology<E> {

    private DistanceMeasure distanceMeasure;
    private ControlParameter radius;

    public SpeciationTopology() {
        this.distanceMeasure = new EuclideanDistanceMeasure();
        this.radius = ConstantControlParameter.of(100);
        this.neighbourhoodSize = ConstantControlParameter.of(20);
    }

    public SpeciationTopology(SpeciationTopology copy) {
        super(copy);
        this.distanceMeasure = copy.distanceMeasure;
        this.radius = copy.radius.getClone();
        this.neighbourhoodSize = copy.neighbourhoodSize.getClone();
    }

    @Override
    public SpeciationTopology getClone() {
        return new SpeciationTopology(this);
    }

    public void setRadius(ControlParameter radius) {
        this.radius = radius;
    }

    public void setDistanceMeasure(DistanceMeasure distanceMeasure) {
        this.distanceMeasure = distanceMeasure;
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

    public static <T extends Entity> F<List<P2<T, Integer>>, List<Integer>> getNeighbourhood(final DistanceMeasure distance,
            final ControlParameter radius, final ControlParameter size, final int index) {
        return new F<List<P2<T, Integer>>, List<Integer>>() {
            @Override
            public List<Integer> f(List<P2<T, Integer>> a) {
                if (a.isEmpty()) {
                    return List.<Integer>nil();
                }

                List<P2<T, Integer>> sorted = a.sort(Ord.<P2<T, Integer>>ord(new F2<P2<T, Integer>, P2<T, Integer>, Ordering>() {
                        @Override
                        public Ordering f(P2<T, Integer> a, P2<T, Integer> b) {
                            int result = -a._1().getFitness().compareTo(b._1().getFitness()) + 1;
                            return Ordering.values()[result];
                        }
                    }.curry()));
                List<P2<T, Integer>> filtered = sorted.filter(inRadius(distance, radius, sorted.head()._1()));
                List<P2<T, Integer>> neighbours = filtered.take((int) size.getParameter());

                if(neighbours.exists(SpeciationTopology.<T>exists(index))) {
                    return neighbours.map(P2.<T, Integer>__2());
                }

                List<P2<T, Integer>> remainder = sorted.minus(Equal.<P2<T, Integer>>equal(
                    new F2<P2<T, Integer>, P2<T, Integer>, Boolean>() {
                        @Override
                        public Boolean f(P2<T, Integer> a, P2<T, Integer> b) {
                            return a._2() == b._2();
                        }
                    }.curry()), neighbours);
                return this.f(remainder);
            }
        };
    }

    @Override
    protected Iterator<E> neighbourhoodOf(final E e) {
        return new UnmodifiableIterator<E>() {

            private List<Integer> neighbours = getNeighbourhood(distanceMeasure, radius, neighbourhoodSize, entities.indexOf(e))
                    .f(List.<Entity>iterableList((java.util.List<Entity>) entities).zipIndex());
            private int index = -1;

            @Override
            public boolean hasNext() {
                return neighbours.isNotEmpty();
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                index = neighbours.head();
                neighbours = neighbours.orTail(P.p(List.<Integer>nil()));
                return entities.get(index);
            }
        };
    }
}
