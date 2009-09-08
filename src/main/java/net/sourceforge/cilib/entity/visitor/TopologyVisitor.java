/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.entity.visitor;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.container.visitor.Visitor;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 * Base class for all visitor instances that visit an entire {@link Topology}
 * at once. These type of visitors are generally assocaited with the
 * calculation of topology related information, such as diameter and
 * radius calculations of the provided topologies.
 *
 * @author gpampara
 */
public abstract class TopologyVisitor implements Visitor<Topology<? extends Entity>> {

    protected PopulationBasedAlgorithm currentAlgorithm;
    protected DistanceMeasure distanceMeasure;

    public TopologyVisitor() {
        distanceMeasure = new EuclideanDistanceMeasure();
    }

    /**
     * Perfrom the visit operation on the provided {@link Topology}.
     * @param topology The {@link Topology} to visit.
     */
    public abstract void visit(Topology<? extends Entity> topology);

    /**
     * Get the result of the visitor after it has performed the visit()
     * action.
     * @return The result of the visit()
     */
    public abstract Object getResult();

    public DistanceMeasure getDistanceMeasure() {
        return distanceMeasure;
    }

    public void setDistanceMeasure(DistanceMeasure distanceMeasure) {
        this.distanceMeasure = distanceMeasure;
    }

    public PopulationBasedAlgorithm getCurrentAlgorithm() {
        return currentAlgorithm;
    }

    public void setCurrentAlgorithm(PopulationBasedAlgorithm currentAlgorithm) {
        this.currentAlgorithm = currentAlgorithm;
    }

}
