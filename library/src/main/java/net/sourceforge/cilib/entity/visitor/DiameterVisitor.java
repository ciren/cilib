/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.visitor;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.distancemeasure.DistanceMeasure;
import net.sourceforge.cilib.util.distancemeasure.EuclideanDistanceMeasure;
import fj.F;
import fj.Ord;
import fj.data.List;

/**
 * Visitor to determine the size of the diameter of the provided
 * Topology.
 */
public class DiameterVisitor<E extends Entity> extends TopologyVisitor<E, Double> {

    protected DistanceMeasure distanceMeasure;

    /**
     * Default constructor.
     */
    public DiameterVisitor() {
        this.distanceMeasure = new EuclideanDistanceMeasure();
    }

    /**
     * Gets the distance measure used.
     * @return the distance measure.
     */
    public DistanceMeasure getDistanceMeasure() {
        return distanceMeasure;
    }

    /**
     * Sets the distance measure to use.
     * @param distanceMeasure the distance measure to use.
     */
    public void setDistanceMeasure(DistanceMeasure distanceMeasure) {
        this.distanceMeasure = distanceMeasure;
    }

    @Override
    public Double f(final List<E> list) {
        return list.map(new F<E, Double>() {
            @Override
            public Double f(final E outer) {
                return list.map(new F<E, Double>() {
                    public Double f(final E inner) {
                        return distanceMeasure.distance(outer.getPosition(), inner.getPosition());
                    }
                }).maximum(Ord.doubleOrd);
            }
        }).maximum(Ord.doubleOrd);
    }
}
