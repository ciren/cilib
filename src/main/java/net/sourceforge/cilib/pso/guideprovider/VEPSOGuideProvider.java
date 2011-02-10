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
package net.sourceforge.cilib.pso.guideprovider;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.MultiPopulationCriterionBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.knowledgetransferstrategies.KnowledgeTransferStrategy;
import net.sourceforge.cilib.algorithm.population.knowledgetransferstrategies.SelectiveKnowledgeTransferStrategy;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.moo.criterion.CriterionBasedMOProblemAdapter;
import net.sourceforge.cilib.type.types.Blackboard;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.StructuredType;

/**
 * Vector-Evaluated Particle Swarm Optimisation Guide Provider
 *
 * <p>
 * This {@link GuideProvider} implements the basic behaviour of VEPSO where each
 * particle's global guide is selected as the position of a particle within
 * another swarm (see {@link MultiPopulationCriterionBasedAlgorithm}). Each swarm
 * is evaluated according to a different sub-objective (see {@link
 * CriterionBasedMOProblemAdapter}) of a Multi-objective optimisation problem. A
 * {@link KnowledgeTransferStrategy} is used to determine which swarm is selected
 * (either random or ring-based) as well as which particle's position within this
 * swarm will be used as guide (usually the gBest particle).
 * </p>
 *
 * <p>
 * References:
 * </p>
 * <p>
 * <ul>
 * <li> K. E. Parsopoulos, D. K. Tasoulis and M. N. Vrahatis, "Multiobjective Optimization using
 * Parallel Vector Evaluated Particle Swarm Optimization", in Proceedings of the IASTED International
 * Conference on Artificial Intelligence and Applications, vol 2, pp. 823-828, 2004.
 * </li>
 * </ul>
 * </p>
 *
 * @author Wiehann Matthysen
 */
public class VEPSOGuideProvider implements GuideProvider {

    private static final long serialVersionUID = -8916378051119235043L;
    private KnowledgeTransferStrategy knowledgeTransferStrategy;

    public VEPSOGuideProvider() {
        this.knowledgeTransferStrategy = new SelectiveKnowledgeTransferStrategy();
    }

    public VEPSOGuideProvider(VEPSOGuideProvider copy) {
        this.knowledgeTransferStrategy = copy.knowledgeTransferStrategy.getClone();
    }

    @Override
    public VEPSOGuideProvider getClone() {
        return new VEPSOGuideProvider(this);
    }

    public void setKnowledgeTransferStrategy(KnowledgeTransferStrategy knowledgeTransferStrategy) {
        this.knowledgeTransferStrategy = knowledgeTransferStrategy;
    }

    public KnowledgeTransferStrategy getKnowledgeTransferStrategy() {
        return this.knowledgeTransferStrategy;
    }

    @SuppressWarnings("unchecked")
    @Override
    public StructuredType get(Particle particle) {
        MultiPopulationBasedAlgorithm topLevelAlgorithm = (MultiPopulationBasedAlgorithm) AbstractAlgorithm.getAlgorithmList().get(0);
        Blackboard<Enum<?>, Type> knowledge = (Blackboard<Enum<?>, Type>) this.knowledgeTransferStrategy.transferKnowledge(topLevelAlgorithm.getPopulations());
        return (StructuredType) knowledge.get(EntityType.Particle.BEST_POSITION);
    }
}
