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
package net.sourceforge.cilib.algorithm.population.knowledgetransferstrategies;

import java.util.List;

import net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.util.selection.selectionstrategies.RandomSelectionStrategy;
import net.sourceforge.cilib.util.selection.selectionstrategies.RingBasedPopulationSelectionStrategy;
import net.sourceforge.cilib.util.selection.selectionstrategies.SelectionStrategy;

/**
 * <p>
 * An implementation of {@link KnowledgeTransferStrategy} where two {@link SelectionStrategy}
 * instances are used to first select a sub-population ({@link PopulationBasedAlgorithm}) from
 * a collection of population-based algorithms (see {@link MultiPopulationBasedAlgorithm) and then
 * within this sub-population's {@link Topology} , which entity's knowledge is to be transfered
 * to the caller requesting it.
 * </p>
 *
 * @author Wiehann Matthysen
 */
public class SelectiveKnowledgeTransferStrategy implements KnowledgeTransferStrategy {

    private static final long serialVersionUID = 402688951924934682L;
    private SelectionStrategy<PopulationBasedAlgorithm> populationSelectionStrategy;
    private SelectionStrategy<Entity> entitySelectionStrategy;

    public SelectiveKnowledgeTransferStrategy() {
        this.populationSelectionStrategy = new RingBasedPopulationSelectionStrategy();
        this.entitySelectionStrategy = new RandomSelectionStrategy<Entity>();
    }

    public SelectiveKnowledgeTransferStrategy(SelectiveKnowledgeTransferStrategy copy) {
        this.populationSelectionStrategy = copy.populationSelectionStrategy.getClone();
        this.entitySelectionStrategy = copy.entitySelectionStrategy.getClone();
    }

    @Override
    public SelectiveKnowledgeTransferStrategy getClone() {
        return new SelectiveKnowledgeTransferStrategy(this);
    }

    public void setPopulationSelectionStrategy(SelectionStrategy<PopulationBasedAlgorithm> populationSelectionStrategy) {
        this.populationSelectionStrategy = populationSelectionStrategy;
    }

    public SelectionStrategy<PopulationBasedAlgorithm> getPopulationBasedAlgorithm() {
        return this.populationSelectionStrategy;
    }

    public void setEntitySelectionStrategy(SelectionStrategy<Entity> entitySelectionStrategy) {
        this.entitySelectionStrategy = entitySelectionStrategy;
    }

    public SelectionStrategy<Entity> getEntitySelectionStrategy() {
        return this.entitySelectionStrategy;
    }

    @Override
    public Type transferKnowledge(List<PopulationBasedAlgorithm> allPopulations) {
        PopulationBasedAlgorithm population = this.populationSelectionStrategy.select(allPopulations);
        Entity entity = this.entitySelectionStrategy.select(population.getTopology());
        return entity.getProperties();
    }
}
