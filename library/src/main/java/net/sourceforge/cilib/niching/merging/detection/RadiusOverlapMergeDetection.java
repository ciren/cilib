/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.niching.merging.detection;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.entity.visitor.RadiusVisitor;
import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.distancemeasure.DistanceMeasure;
import net.sourceforge.cilib.util.distancemeasure.EuclideanDistanceMeasure;

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
    public Boolean f(PopulationBasedAlgorithm swarm1, PopulationBasedAlgorithm swarm2) {
        RadiusVisitor radiusVisitor = new RadiusVisitor();
        radiusVisitor.setDistanceMeasure(distanceMeasure);

        swarm1.accept(radiusVisitor);
        double swarm1Radius = radiusVisitor.getResult().doubleValue();

        swarm2.accept(radiusVisitor);
        double swarm2Radius = radiusVisitor.getResult().doubleValue();

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
