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
 * Split an {@link OptimisationProblem} into sub-problems of equal size/dimension.
 * @author Theuns Cloete
 * TODO test this class
 */
public class PerfectSplitStrategy implements SplitStrategy {
    public void split(OptimisationProblem problem, CooperativeEntity context, List<PopulationBasedAlgorithm> populations) {
        if (populations.size() < 2)
            throw new InitialisationException("There should at least be two Cooperating populations in a Cooperative Algorithm");
        if (problem.getDomain().getDimension() % populations.size() != 0)
            throw new InitialisationException("A Problem with dimension " + problem.getDomain().getDimension() + " cannot be split into parts of equal size when using " + populations.size() + " populations");
        int dimension = problem.getDomain().getDimension() / populations.size();
        int offset = 0;
        for (PopulationBasedAlgorithm population : populations) {
            // TODO check whether this cast is safe
            population.setOptimisationProblem(new CooperativeOptimisationProblemAdapter(problem, context, dimension, offset));
            offset += dimension;
        }
    }
}
