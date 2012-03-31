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
package net.sourceforge.cilib.niching.merging;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.visitor.RadiusVisitor;
import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

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
        
        Vector swarm1GBest = (Vector) swarm1.getTopology().getBestEntity().getCandidateSolution();
        Vector swarm2GBest = (Vector) swarm2.getTopology().getBestEntity().getCandidateSolution();

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
