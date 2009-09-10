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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.cilib.algorithm.InitialisationException;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.cooperative.CooperativeEntity;
import net.sourceforge.cilib.problem.CooperativeOptimisationProblemAdapter;
import net.sourceforge.cilib.problem.OptimisationProblem;

/**
 * @author Wiehann Matthysen
 */
public class IndexedSplitStrategy implements SplitStrategy {
    protected ArrayList<Integer> indices;

    public IndexedSplitStrategy() {
        indices = new ArrayList<Integer>();
        indices.add(0);
    }

    public void addSplitIndex(int index) {
        indices.add(index);
    }

    public int getSplitIndex(int position) {
        return this.indices.get(position);
    }

    /*
     * (non-Javadoc)
     * @see net.sourceforge.cilib.cooperative.splitstrategies.SplitStrategy#split(net.sourceforge.cilib.problem.OptimisationProblem,
     *      net.sourceforge.cilib.cooperative.CooperativeEntity, java.util.List)
     */
    @Override
    public void split(OptimisationProblem problem, CooperativeEntity context, List<PopulationBasedAlgorithm> populations) {
        if (populations.size() < 2)
            throw new IllegalArgumentException("There should at least be two Cooperating populations in a Cooperative Algorithm");
        if (indices.size() != populations.size())
            throw new InitialisationException("The number of indices (" + indices.size() + ") is not sufficient to divide into the number of populations.");
        if (indices.size() == 0)
            throw new InitialisationException("No split indices set.");
        if (problem.getDomain().getDimension() < populations.size())
            throw new InitialisationException("Problem dimensionality should be equal to or greater than the number of cooperating populations.");

        for (int i = 0; i < populations.size(); ++i) {
            PopulationBasedAlgorithm population = populations.get(i);
            int offset = indices.get(i);
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
