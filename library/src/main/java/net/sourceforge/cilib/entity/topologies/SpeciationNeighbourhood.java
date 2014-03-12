/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.topologies;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.util.distancemeasure.DistanceMeasure;
import net.sourceforge.cilib.util.distancemeasure.EuclideanDistanceMeasure;
import fj.Equal;
import fj.F;
import fj.F2;
import fj.Ord;
import fj.Ordering;
import fj.data.List;

public class SpeciationNeighbourhood<E extends Entity> extends Neighbourhood<E> {

    private ControlParameter n = ConstantControlParameter.of(0.1);
    private ControlParameter radius = ConstantControlParameter.of(20);
    private DistanceMeasure distance = new EuclideanDistanceMeasure();

    public SpeciationNeighbourhood() {}

    public SpeciationNeighbourhood(ControlParameter radius, ControlParameter n) {
        this.radius = radius;
        this.n = n;
    }

    public SpeciationNeighbourhood(DistanceMeasure d, ControlParameter radius, ControlParameter n) {
        this.distance = d;
        this.radius = radius;
        this.n = n;
    }

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
                return distance.distance(a.getPosition(), sorted.head().getPosition()) < radius.getParameter();
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

    public void setDistanceMeasure(DistanceMeasure d) {
        this.distance = d;
    }

    public void setNeighbourhoodSize(ControlParameter n) {
        this.n = n;
    }

    public void setRadius(ControlParameter radius) {
        this.radius = radius;
    }
}
