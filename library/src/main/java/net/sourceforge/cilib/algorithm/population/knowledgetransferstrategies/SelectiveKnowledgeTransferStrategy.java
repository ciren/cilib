/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
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
 * An implementation of {@link KnowledgeTransferStrategy} where two
 * {@link Selector} instances are used to first select a
 * {@link PopulationBasedAlgorithm sub-population} from a collection of
 * population-based algorithms (see {@link MultiPopulationBasedAlgorithm}) and
 * then within this sub-population's
 * {@link net.sourceforge.cilib.entity.Topology}, which {@link Entity}'s
 * knowledge is to be transferred to the caller requesting it.
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
