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
package net.sourceforge.cilib.algorithm.population;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.initialisation.PopulationInitialisationStrategy;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.visitor.TopologyVisitor;

/**
 * Base class for algorithms that focus on a single populations of entities.
 * These types of algoruthms typically include PSO , EC, ACO etc.
 * @author Gary Pampara
 */
public abstract class SinglePopulationBasedAlgorithm extends AbstractAlgorithm implements PopulationBasedAlgorithm {
    private static final long serialVersionUID = -4095104893057340895L;

    protected PopulationInitialisationStrategy<? extends Entity> initialisationStrategy;

    /**
     * Create an empty {@linkplain PopulationBasedAlgorithm}.
     */
    protected SinglePopulationBasedAlgorithm() {
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The {@linkplain PopulationBasedAlgorithm} to copy.
     */
    protected SinglePopulationBasedAlgorithm(SinglePopulationBasedAlgorithm copy) {
        super(copy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract SinglePopulationBasedAlgorithm getClone();

    /**
     * Perform the iteration within the algorithm.
     */
    @Override
    protected abstract void algorithmIteration();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract Topology<? extends Entity> getTopology();

    /**
     * Set the <tt>Topology</tt> for the population-based algorithm.
     * @param topology The {@linkplain Topology} to be set.
     */
    public abstract void setTopology(Topology<? extends Entity> topology);

    /**
     * Get the currently set {@linkplain PopulationInitialisationStrategy}.
     * @return The current {@linkplain PopulationInitialisationStrategy}.
     */
    @Override
    public PopulationInitialisationStrategy getInitialisationStrategy() {
        return initialisationStrategy;
    }

    /**
     * Set the {@linkplain PopulationInitialisationStrategy} to be used.
     * @param initialisationStrategy The {@linkplain PopulationInitialisationStrategy} to use.
     */
    @Override
    public void setInitialisationStrategy(PopulationInitialisationStrategy<? extends Entity> initialisationStrategy) {
        this.initialisationStrategy = initialisationStrategy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object accept(TopologyVisitor visitor) {
        visitor.setCurrentAlgorithm(this);
        getTopology().accept(visitor);
        return visitor.getResult();
    }

}
