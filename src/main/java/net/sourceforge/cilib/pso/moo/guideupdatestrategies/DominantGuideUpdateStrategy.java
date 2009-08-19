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
package net.sourceforge.cilib.pso.moo.guideupdatestrategies;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * <p>
 * Implementation of {@link GuideUpdateStrategy} where a particle's guide
 * gets updated if and only if the new guide dominates the previous guide.
 * </p>
 *
 * @author Wiehann Matthysen
 */
public class DominantGuideUpdateStrategy implements GuideUpdateStrategy {

    private static final long serialVersionUID = -2025890754745107472L;

    public DominantGuideUpdateStrategy() {
    }

    public DominantGuideUpdateStrategy(GuideUpdateStrategy copy) {
    }

    @Override
    public DominantGuideUpdateStrategy getClone() {
        return new DominantGuideUpdateStrategy(this);
    }

    @Override
    public void updateGuide(Particle particle, EntityType.Particle.Guide guideType, Vector newGuide) {
        Vector previousGuide = (Vector) particle.getProperties().get(guideType);
        Algorithm topLevelAlgorithm = AbstractAlgorithm.getAlgorithmList().get(0);
        OptimisationProblem problem = topLevelAlgorithm.getOptimisationProblem();
        Fitness currentFitness = problem.getFitness(newGuide);
        if (previousGuide == null || currentFitness.compareTo(problem.getFitness(previousGuide)) > 0) {
            particle.getProperties().put(guideType, newGuide);
        }
    }
}
