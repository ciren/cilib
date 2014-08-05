/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.topologies;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.util.distancemeasure.DistanceMeasure;
import net.sourceforge.cilib.util.distancemeasure.EuclideanDistanceMeasure;
import fj.data.List;
import fj.F2;
import fj.Ord;
import fj.Ordering;

public class DistanceBasedNeighbourhood<E extends Entity> extends Neighbourhood<E> {

    private ControlParameter n;
    private DistanceMeasure distance;

    public DistanceBasedNeighbourhood() {
        this(ConstantControlParameter.of(3));
    }

    public DistanceBasedNeighbourhood(ControlParameter n) {
        this.n = n;
        this.distance = new EuclideanDistanceMeasure();
    }

    @Override
    public List<E> f(List<E> list, E element) {
        final StructuredType position = element.getPosition();

        List<E> sorted = list.sort(Ord.<E>ord(new F2<E, E, Ordering>() {
            @Override
            public Ordering f(E a, E b) {
                Double aToElement = distance.distance(a.getPosition(), position);
                Double bToElement = distance.distance(b.getPosition(), position);
                return Ordering.values()[aToElement.compareTo(bToElement) + 1];
            }
        }.curry()));

        return sorted.take((int)n.getParameter());
    }

    public void setNeighbourhoodSize(ControlParameter n) {
        this.n = n;
    }

    public void setDistanceMeasure(DistanceMeasure distance) {
        this.distance = distance;
    }
}
