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
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.util.Cloneable;

/**
 * <p>
 * This interface is used in combination with a {@link MultiPopulationBasedAlgorithm}
 * to enable different types of knowledge (like global best particle positions etc.) to
 * be shared among different sub-populations during a search.
 * </p>
 *
 */
public interface KnowledgeTransferStrategy extends Cloneable {

    @Override
    KnowledgeTransferStrategy getClone();

    /**
     * Returns knowledge that was gained from an entity within the list of populations.
     * @param allPopulations The list of populations that will be used to select an
     * entity who's knowledge will be used.
     * @return The knowledge that was gained.
     */
    Type transferKnowledge(List<PopulationBasedAlgorithm> allPopulations);
}
