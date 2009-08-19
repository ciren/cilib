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
package net.sourceforge.cilib.cooperative.splitstrategies;

import java.util.List;

import net.sourceforge.cilib.algorithm.InitialisationException;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.cooperative.CooperativeEntity;
import net.sourceforge.cilib.problem.CooperativeOptimisationProblemAdapter;
import net.sourceforge.cilib.problem.OptimisationProblem;

/**
 * Split an {@link OptimisationProblem} into sub-problems of unequal size/dimension. Defaults into a
 * split of equal sizes if possible
 * @author Olusegun Olorunda
 */
public class ImperfectSplitStrategy extends IndexedSplitStrategy {
/*    public ImperfectSplitStrategy() {
    }
*/
    @Override
    public void split(OptimisationProblem problem, CooperativeEntity context, List<PopulationBasedAlgorithm> populations) {
        if (populations.size() < 2)
            throw new InitialisationException("There should at least be two Cooperating populations in a Cooperative Algorithm");
        if (problem.getDomain().getDimension() < populations.size())
            throw new InitialisationException("Problem dimensionality should be equal to or greater than the number of cooperating populations.");

/*        if (problem.getDomain().getDimension() % populations.size() != 0)
            throw new InitialisationException("A Problem with dimension " + problem.getDomain().getDimension() + " cannot be split into parts of equal size when using " + populations.size() + " populations");
*/
        int splitSize = problem.getDomain().getDimension() / populations.size();
        int remainder = problem.getDomain().getDimension() % populations.size();
        int offset = 0;

        if (remainder == 0) {
            for (PopulationBasedAlgorithm population : populations) {
                population.setOptimisationProblem(new CooperativeOptimisationProblemAdapter(problem, context, splitSize, offset));
                offset += splitSize;
            }
        }
        else {
            int currentIndexPosition = 0;

            for (int i = 0; i < (populations.size() - 1); i++) {
                if (remainder > 0) {
                    currentIndexPosition += (splitSize + 1);
                    --remainder;
                }
                else {
                    currentIndexPosition += splitSize;
                }

                addSplitIndex(currentIndexPosition);
            }

            for (int i = 0; i < populations.size(); ++i) {
                PopulationBasedAlgorithm population = populations.get(i);
                offset = indices.get(i);
                int dimension;
                if ((i + 1) < indices.size())
                    dimension = indices.get(i + 1) - indices.get(i);
                else
                    dimension = problem.getDomain().getDimension() - indices.get(i);
                // TODO check whether this cast is safe
                population.setOptimisationProblem(new CooperativeOptimisationProblemAdapter(problem, context, dimension, offset));
            }
        }
    }
}
