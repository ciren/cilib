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
package net.sourceforge.cilib.entity.visitor;

import java.util.Iterator;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.measurement.single.diversity.centerinitialisationstrategies.CenterInitialisationStrategy;
import net.sourceforge.cilib.measurement.single.diversity.centerinitialisationstrategies.GBestCenterInitialisationStrategy;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 * Determine the radius of the current {@linkplain net.sourceforge.cilib.entity.Topology topology}
 * centered on the given CenterInitialisationStrategy.
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
     *
     * @param topology
     */
    @Override
    public void visit(Topology<? extends Entity> topology) {
        done = false;
        // set radius value to be returned to zero
        double maxDistance = 0.0;
        Vector center = populationCenter.getCenter(topology);

        // initialize iterator to be used to calculate radius
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
     * Obtain the value of the {@code radius} for the visited {@code Topology}.
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
     * @return 
     */
    public CenterInitialisationStrategy getPopulationCenter() {
        return populationCenter;
    }

    /**
     * Sets the strategy to use for calculating the center of the topology.
     * @param centerCalculator 
     */
    public void setPopulationCenter(CenterInitialisationStrategy centerCalculator) {
        this.populationCenter = centerCalculator;
    }

    /**
     * Gets the distance measure used.
     * @return 
     */
    public DistanceMeasure getDistanceMeasure() {
        return distanceMeasure;
    }

    /**
     * Sets the distance measure to use.
     * @param distanceMeasure 
     */
    public void setDistanceMeasure(DistanceMeasure distanceMeasure) {
        this.distanceMeasure = distanceMeasure;
    }
}
