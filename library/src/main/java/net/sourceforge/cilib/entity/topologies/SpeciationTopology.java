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
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.util.distancemeasure.DistanceMeasure;
import net.sourceforge.cilib.util.distancemeasure.EuclideanDistanceMeasure;
import net.sourceforge.cilib.util.functions.Utils;

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
    private List<List<Integer>> nHoods;
    private int lastIteration;
    private boolean perCall;

    public SpeciationTopology() {
        this.distanceMeasure = new EuclideanDistanceMeasure();
        this.radius = ConstantControlParameter.of(100);
        this.neighbourhoodSize = ConstantControlParameter.of(20);
        this.lastIteration = -1;
        this.perCall = false;
    }

    public SpeciationTopology(SpeciationTopology copy) {
        super(copy);
        this.distanceMeasure = copy.distanceMeasure;
        this.radius = copy.radius.getClone();
        this.neighbourhoodSize = copy.neighbourhoodSize.getClone();
        this.lastIteration = -1;
        this.perCall = copy.perCall;
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

    public void setPerCall(boolean perCall) {
        this.perCall = perCall;
    }

    public boolean getPerCall() {
        return perCall;
    }

    public static <T extends Entity> F<P2<T ,Integer>, Boolean> inRadius(final DistanceMeasure distance, final ControlParameter radius, final T other) {
        return new F<P2<T ,Integer>, Boolean>() {
            @Override
            public Boolean f(P2<T ,Integer> a) {
                return distance.distance(a._1().getCandidateSolution(), other.getCandidateSolution()) < radius.getParameter();
            }
        };
    }
    
    /**
     * This is used to help with the unit test. i.e. no "empty stack exception"
     */
    public int getIteration() {
        return AbstractAlgorithm.get().getIterations();
    }
    
    public static <T extends Entity> F<P2<List<P2<T, Integer>>,List<List<Integer>>>, List<List<Integer>>> getNeighbourhoods(final DistanceMeasure distance,
            final ControlParameter radius, final ControlParameter size) {
        return new F<P2<List<P2<T, Integer>>,List<List<Integer>>>, List<List<Integer>>>() {
            @Override
            public List<List<Integer>> f(P2<List<P2<T, Integer>>,List<List<Integer>>> a) {
                if (a._1().isEmpty()) {
                    return a._2();
                }

                List<P2<T, Integer>> sorted = a._1().sort(Ord.<P2<T, Integer>>ord(new F2<P2<T, Integer>, P2<T, Integer>, Ordering>() {
                        @Override
                        public Ordering f(P2<T, Integer> a, P2<T, Integer> b) {
                            int result = -a._1().getFitness().compareTo(b._1().getFitness()) + 1;
                            return Ordering.values()[result];
                        }
                    }.curry()));

                List<P2<T, Integer>> neighbours = sorted
                        .filter(inRadius(distance, radius, sorted.head()._1()))
                        .take((int) size.getParameter());

                List<P2<T, Integer>> remainder = sorted.minus(
                        Equal.<T,Integer>p2Equal(Utils.<T>alwaysEqual(), Equal.intEqual), 
                        neighbours);
                return this.f(P.p(remainder, a._2().cons(neighbours.map(P2.<T,Integer>__2()))));
            }
        };
    }
    
    private List<Integer> getNeighbourhood(final E e) {
        int currentIteration = getIteration();
        if (currentIteration > lastIteration || nHoods == null || perCall) {
            lastIteration = currentIteration;
            nHoods = getNeighbourhoods(distanceMeasure, radius, neighbourhoodSize)
                .f(P.p(List.<Entity>iterableList((java.util.List<Entity>) entities).zipIndex(),List.<List<Integer>>nil()));
        }
        
        final int index = entities.indexOf(e);
        return nHoods.find(new F<List<Integer>, Boolean>() {
            @Override
            public Boolean f(List<Integer> a) {
                return a.exists(Equal.intEqual.eq(index));
            }
        }).orSome(List.single(index));
    }

    @Override
    protected Iterator<E> neighbourhoodOf(final E e) {
        return new UnmodifiableIterator<E>() {

            private List<Integer> neighbours = getNeighbourhood(e);
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
