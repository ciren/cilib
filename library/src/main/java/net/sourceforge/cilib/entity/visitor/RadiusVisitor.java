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
import net.sourceforge.cilib.measurement.single.diversity.centerinitialisationstrategies.CenterInitialisationStrategy;
import net.sourceforge.cilib.measurement.single.diversity.centerinitialisationstrategies.GBestCenterInitialisationStrategy;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.distancemeasure.DistanceMeasure;
import net.sourceforge.cilib.util.distancemeasure.EuclideanDistanceMeasure;

/**
 * Determine the radius of the current {@link Topology} centered on the given
 * {@link CenterInitialisationStrategy}.
 */
public class RadiusVisitor implements TopologyVisitor {

    private double radius;
    private boolean done;
    private CenterInitialisationStrategy populationCenter;
    protected DistanceMeasure distanceMeasure;

    /**
     * Default constructor.
     */
    public RadiusVisitor() {
        this.radius = -Double.MAX_VALUE;
        this.done = false;
        this.populationCenter = new GBestCenterInitialisationStrategy();
        this.distanceMeasure = new EuclideanDistanceMeasure();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visit(Topology<? extends Entity> topology) {
        done = false;
        // set radius value to be returned to zero
        double maxDistance = 0.0;
        Vector center = populationCenter.getCenter(topology);

        // initialise iterator to be used to calculate radius
        Iterator<?> calculateRadiusIterator = topology.iterator();

        // calculate radius
        while (calculateRadiusIterator.hasNext()) {
            Entity populationEntity = (Entity) calculateRadiusIterator.next();
            Vector entityContents = (Vector) populationEntity.getCandidateSolution();

            double currentDistance = distanceMeasure.distance(center, entityContents);

            if (currentDistance > maxDistance) {
                maxDistance = currentDistance;
            }
        }

        radius = maxDistance;
        done = true;
    }

    /**
     * Obtain the value of the {@code radius} for the visited {@link Topology}.
     *
     * @return The value of the radius.
     */
    @Override
    public Double getResult() {
        return this.radius;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDone() {
        return done;
    }

    /**
     * Gets the strategy used for calculating the center of the topology.
     *
     * @return the {@linkplain CenterInitialisationStrategy}.
     */
    public CenterInitialisationStrategy getPopulationCenter() {
        return populationCenter;
    }

    /**
     * Sets the {@linkplain CenterInitialisationStrategy} to use for calculating
     * the center of the {@linkplain Topology}.
     *
     * @param centerCalculator  the {@linkplain CenterInitialisationStrategy} to
     *                          use.
     */
    public void setPopulationCenter(CenterInitialisationStrategy centerCalculator) {
        this.populationCenter = centerCalculator;
    }

    /**
     * Gets the distance measure used.
     *
     * @return the {@linkplain DistanceMeasure}.
     */
    public DistanceMeasure getDistanceMeasure() {
        return distanceMeasure;
    }

    /**
     * Sets the distance measure to use.
     *
     * @param distanceMeasure the {@linkplain DistanceMeasure} to use.
     */
    public void setDistanceMeasure(DistanceMeasure distanceMeasure) {
        this.distanceMeasure = distanceMeasure;
    }
}
