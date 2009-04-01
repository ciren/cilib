/**
 * Copyright (C) 2003 - 2008
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

package net.sourceforge.cilib.algorithm.population;

import java.util.List;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.initialisation.PopulationInitialisationStrategy;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.visitor.TopologyVisitor;
import net.sourceforge.cilib.problem.OptimisationSolution;

/**
 * Base class for all algorithms that manage a collection of
 * {@linkplain Entity entities} in some manner.
 */
public abstract class PopulationBasedAlgorithm extends Algorithm {

    protected PopulationBasedAlgorithm() {
    }

    protected PopulationBasedAlgorithm(PopulationBasedAlgorithm copy) {
        super(copy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract PopulationBasedAlgorithm getClone();

    /**
     * Return the size of the current population.
     * @return The size of the population.
     */
    public abstract int getPopulationSize();

    /**
     * {@inheritDoc}
     */
    @Override
    protected abstract void algorithmIteration();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract OptimisationSolution getBestSolution();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract List<OptimisationSolution> getSolutions();

    /**
     * Get the current collection (population) of {@linkplain Entity entities}.
     * @return The current population.
     */
    public abstract Topology<? extends Entity> getTopology();

    /**
     * General method to accept a visitor to perform a calculation on the current algorithm. The
     * operation is generally deferred down to the underlying topology associated with the
     * algorithm, as the algorithm does not contain information, but rather only behaviour to alter
     * the candidate solutions that are managed by the <tt>Topology</tt>.
     * @param visitor The <tt>Visitor</tt> to be applied to the algorithm
     * @return The result of the visitor operation.
     */
    public abstract Object accept(TopologyVisitor visitor);

    /**
     *
     * @param initialisationStrategy
     */
    public abstract void setInitialisationStrategy(PopulationInitialisationStrategy initialisationStrategy);

    public abstract PopulationInitialisationStrategy getInitialisationStrategy();

}
