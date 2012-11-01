/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single.diversity.normalisation;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.visitor.DiameterVisitor;
import net.sourceforge.cilib.entity.visitor.TopologyVisitor;

/**
 * Normalisation based on the {@linkplain Topology}.
 */
public class TopologyBasedNormalisation implements DiversityNormalisation {

    private TopologyVisitor visitor;

    /**
     * Create an instance of the {@linkplain TopologyBasedNormalisation}.
     */
    public TopologyBasedNormalisation() {
        visitor = new DiameterVisitor();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getNormalisationParameter(PopulationBasedAlgorithm algorithm) {
        algorithm.getTopology().accept(visitor);

        return (Double) visitor.getResult();
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
