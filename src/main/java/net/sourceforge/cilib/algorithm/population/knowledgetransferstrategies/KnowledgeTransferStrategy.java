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
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.util.Cloneable;

/**
 * <p>
 * This interface is used in combination with a {@link MultiPopulationBasedAlgorithm}
 * to enable different types of knowledge (like global best particle positions etc.) to
 * be shared among different sub-populations during a search.
 * </p>
 *
 * @author Wiehann Matthysen
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
