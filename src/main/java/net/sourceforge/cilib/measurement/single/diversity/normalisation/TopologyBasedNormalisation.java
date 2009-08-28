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
package net.sourceforge.cilib.measurement.single.diversity.normalisation;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.visitor.DiameterVisitor;
import net.sourceforge.cilib.entity.visitor.TopologyVisitor;

/**
 * Normalisation based on the {@linkplain Topology}.
 */
public class TopologyBasedNormalisation extends NormalisationParameter {

    private TopologyVisitor visitor;

    /**
     * Create an instance of the {@linkplain TopologyBasedNormalisation}.
     */
    public TopologyBasedNormalisation() {
        super();
        visitor = new DiameterVisitor();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getValue() {
        PopulationBasedAlgorithm algorithm = (PopulationBasedAlgorithm) AbstractAlgorithm.get();
        visitor.setDistanceMeasure(distanceMeasure);
        algorithm.accept(visitor);
        this.normalisationParameter = (Double) visitor.getResult();

        return this.normalisationParameter;
    }

    /**
     * Get the decorated {@linkplain TopologyVisitor}.
     * @return The decorated {@linkplain TopologyVisitor}.
     */
    public TopologyVisitor getVisitor() {
        return visitor;
    }

    /**
     * Set the {@linkplain TopologyVisitor} to be decorated.
     * @param visitor The {@linkplain TopologyVisitor} to set.
     */
    public void setVisitor(TopologyVisitor visitor) {
        this.visitor = visitor;
    }

}
