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
package net.sourceforge.cilib.algorithm.population.knowledgetransferstrategies;

import java.util.List;

import net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.util.selection.recipes.ElitistSelector;
import net.sourceforge.cilib.util.selection.recipes.RingBasedPopulationSelector;
import net.sourceforge.cilib.util.selection.recipes.Selector;

/**
 * <p>
 * An implementation of {@link KnowledgeTransferStrategy} where two {@link Selection}
 * instances are used to first select a sub-population ({@link PopulationBasedAlgorithm}) from
 * a collection of population-based algorithms (see {@link MultiPopulationBasedAlgorithm) and then
 * within this sub-population's {@link Topology}, which entity's knowledge is to be transfered
 * to the caller requesting it.
 * </p>
 *
 * @author Wiehann Matthysen
 */
public class SelectiveKnowledgeTransferStrategy implements KnowledgeTransferStrategy {

    private static final long serialVersionUID = 402688951924934682L;

    private Selector<PopulationBasedAlgorithm> populationSelection;
    private Selector<Entity> entitySelection;

    public SelectiveKnowledgeTransferStrategy() {
        this.populationSelection = new RingBasedPopulationSelector();
        this.entitySelection = new ElitistSelector<Entity>();
    }

    public SelectiveKnowledgeTransferStrategy(SelectiveKnowledgeTransferStrategy copy) {
        this.populationSelection = copy.populationSelection;
        this.entitySelection = copy.entitySelection;
    }

    @Override
    public SelectiveKnowledgeTransferStrategy getClone() {
        return new SelectiveKnowledgeTransferStrategy(this);
    }

    public void setPopulationSelection(Selector<PopulationBasedAlgorithm> populationSelection) {
        this.populationSelection = populationSelection;
    }

    public Selector<PopulationBasedAlgorithm> getPopulationSelection() {
        return this.populationSelection;
    }

    public void setEntitySelection(Selector<Entity> entitySelection) {
        this.entitySelection = entitySelection;
    }

    public Selector<Entity> getEntitySelection() {
        return this.entitySelection;
    }

    @Override
    public Type transferKnowledge(List<PopulationBasedAlgorithm> allPopulations) {
        PopulationBasedAlgorithm population = this.populationSelection.on(allPopulations).select();
        Entity entity = this.entitySelection.on((Iterable<Entity>) population.getTopology()).select();
        return entity.getProperties();
    }
}
