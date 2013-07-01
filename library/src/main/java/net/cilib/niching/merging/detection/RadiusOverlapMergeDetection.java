/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.niching.merging.detection;

import net.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.cilib.controlparameter.ConstantControlParameter;
import net.cilib.controlparameter.ControlParameter;
import net.cilib.entity.Entity;
import net.cilib.entity.Topologies;
import net.cilib.entity.visitor.RadiusVisitor;
import net.cilib.math.Maths;
import net.cilib.type.types.container.Vector;
import net.cilib.util.distancemeasure.DistanceMeasure;
import net.cilib.util.distancemeasure.EuclideanDistanceMeasure;

/**
 * Determines if two swarms overlap.
 *
 * <p>
 * This overlap is determined by the radius of the sub-swarm. If the overlap
 * is less than a predefined threshold value, the sub-swarms will merge into
 * a single sub-swarm. The new swarm is returned.
 * </p>
 */
public class RadiusOverlapMergeDetection extends MergeDetection {
    private ControlParameter threshold;
    private DistanceMeasure distanceMeasure;

    /**
     * Default constructor.
     */
    public RadiusOverlapMergeDetection() {
        this.threshold = ConstantControlParameter.of(10e-8);
        this.distanceMeasure = new EuclideanDistanceMeasure();
    }

    /**
     * Determines whether two swarms overlap.
     *
     * @param swarm1 The first swarm.
     * @param swarm2 The second swarm.
     * @return True if the swarms overlap, false otherwise.
     */
    @Override
    public Boolean f(SinglePopulationBasedAlgorithm swarm1, SinglePopulationBasedAlgorithm swarm2) {
        RadiusVisitor radiusVisitor = new RadiusVisitor();
        radiusVisitor.setDistanceMeasure(distanceMeasure);

        double swarm1Radius = radiusVisitor.f(swarm1.getTopology());
        double swarm2Radius = radiusVisitor.f(swarm2.getTopology());

        Vector swarm1GBest = (Vector) Topologies.getBestEntity(swarm1.getTopology()).getCandidateSolution();
        Vector swarm2GBest = (Vector) Topologies.getBestEntity(swarm2.getTopology()).getCandidateSolution();

        double distance = distanceMeasure.distance(swarm1GBest, swarm2GBest);
        double normalizedDistance = distance / swarm1GBest.boundsOf(0).getRange();

        //special case if both radii approximate 0 or if the swarms intersect
        if ((Math.abs(swarm1Radius) < Maths.EPSILON && Math.abs(swarm2Radius) < Maths.EPSILON
                && normalizedDistance < threshold.getParameter()) || (distance < swarm1Radius + swarm2Radius)) {
            return true;
        }

        return false;
    }

    /**
     * Get the merge threshold value.
     *
     * @return The value of the merge threshold.
     */
    public ControlParameter getThreshold() {
        return threshold;
    }

    /**
     * Set the merge threshold value.
     *
     * @param threshold The value to set.
     */
    public void setThreshold(ControlParameter threshold) {
        this.threshold = threshold;
    }

    public DistanceMeasure getDistanceMeasure() {
        return distanceMeasure;
    }

    public void setDistanceMeasure(DistanceMeasure distanceMeasure) {
        this.distanceMeasure = distanceMeasure;
    }
}
