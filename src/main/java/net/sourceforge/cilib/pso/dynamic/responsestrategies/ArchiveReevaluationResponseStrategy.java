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
package net.sourceforge.cilib.pso.dynamic.responsestrategies;

import java.util.LinkedList;
import java.util.List;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.moo.archive.Archive;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationSolution;

/**
 *
 * @author gpampara
 */
public class ArchiveReevaluationResponseStrategy extends EnvironmentChangeResponseStrategy<PopulationBasedAlgorithm> {

    private static final long serialVersionUID = 4757162276962451681L;

    @Override
    public EnvironmentChangeResponseStrategy<PopulationBasedAlgorithm> getClone() {
        return this;
    }

    @Override
    protected void performReaction(PopulationBasedAlgorithm algorithm) {
        for (Entity entity : algorithm.getTopology()) {
            entity.getProperties().put(EntityType.Particle.BEST_FITNESS, entity.getFitnessCalculator().getFitness(entity));
            //entity.getProperties().put(EntityType.Particle.BEST_POSITION, entity.getCandidateSolution());
            entity.calculateFitness();
        }

        PopulationBasedAlgorithm populationBasedAlgorithm = (PopulationBasedAlgorithm) AbstractAlgorithm.getAlgorithmList().get(0);
        OptimisationProblem problem = populationBasedAlgorithm.getOptimisationProblem();

        List<OptimisationSolution> newList = new LinkedList<OptimisationSolution>();
        for (OptimisationSolution solution : Archive.Provider.get()) {
            OptimisationSolution os = new OptimisationSolution(solution.getPosition(), problem.getFitness(solution.getPosition()));
            newList.add(os);
        }

        Archive.Provider.get().clear();
        Archive.Provider.get().addAll(newList);
    }
}
