/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.visitor;

import java.util.Iterator;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.distancemeasure.DistanceMeasure;
import net.sourceforge.cilib.util.distancemeasure.EuclideanDistanceMeasure;

/**
 * Visitor to determine the size of the diameter of the provided
 * {@linkplain Topology}.
 */
public class DiameterVisitor implements TopologyVisitor {
    private double distance;
    private boolean done;
    protected DistanceMeasure distanceMeasure;

    /**
     * Default constructor.
     */
    public DiameterVisitor() {
        this.distance = -Double.MAX_VALUE;
        this.done = false;
        this.distanceMeasure = new EuclideanDistanceMeasure();
    }

    /**
     * Obtain the {@code diameter} of the provided {@code Topology}.
     * @param topology The topology to inspect.
     */
    @Override
    public void visit(Topology<? extends Entity> topology) {
        done = false;
        double maxDistance = 0.0;

        Iterator<? extends Entity> k1 = topology.iterator();
        while (k1.hasNext()) {
            Entity p1 = (Entity) k1.next();
            Vector position1 = (Vector) p1.getCandidateSolution();

            Iterator<? extends Entity> k2 = topology.iterator();
            while (k2.hasNext()) {
                Entity p2 = (Entity) k2.next();
                Vector position2 = (Vector) p2.getCandidateSolution();

                double actualDistance = distanceMeasure.distance(position1, position2);
                if (actualDistance > maxDistance)
                    maxDistance = actualDistance;
            }
        }

        distance = maxDistance;
        done = true;
    }

    /**
     * Obtain the diameter value for the {@link net.sourceforge.cilib.entity.Topology} of
     * {@code Entity} instances.
     * @return The diameter value.
     */
    @Override
    public Double getResult() {
        return this.distance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDone() {
        return done;
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
}
