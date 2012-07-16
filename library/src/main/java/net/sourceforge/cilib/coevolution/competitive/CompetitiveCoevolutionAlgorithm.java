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
package net.sourceforge.cilib.coevolution.competitive;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.List;
import net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.coevolution.CoevolutionAlgorithm;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.problem.coevolution.CoevolutionOptimisationProblem;
import net.sourceforge.cilib.problem.coevolution.CompetitiveCoevolutionProblemAdapter;

/**
 * Implements competitive algorithms.
 */
public class CompetitiveCoevolutionAlgorithm extends MultiPopulationBasedAlgorithm 
implements CoevolutionAlgorithm {

    private static final long serialVersionUID = -3859431217295779546L;

    public CompetitiveCoevolutionAlgorithm() {
        super();
    }

    public CompetitiveCoevolutionAlgorithm(CompetitiveCoevolutionAlgorithm copy) {
        super(copy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompetitiveCoevolutionAlgorithm getClone() {
        return new CompetitiveCoevolutionAlgorithm(this);
    }

    /**
     * Initialises every population.
     */
    @Override
    public void algorithmInitialisation() {
        CoevolutionOptimisationProblem problem = (CoevolutionOptimisationProblem) optimisationProblem;
        
        if (problem.getAmountSubPopulations() != subPopulationsAlgorithms.size()) {
            throw new RuntimeException("The amount of sub populations specified do not match the amount required by the current problem");
        }

        int populationID = 1;
        for (PopulationBasedAlgorithm currentAlgorithm : subPopulationsAlgorithms) {
            problem.initializeEntities(currentAlgorithm, populationID);
            currentAlgorithm.setOptimisationProblem(new CompetitiveCoevolutionProblemAdapter(populationID, problem.getSubPopulationDomain(populationID), problem));
            populationID++;
        }
        
        for (PopulationBasedAlgorithm currentAlgorithm : subPopulationsAlgorithms) {
            currentAlgorithm.performInitialisation();        
        }
    }

    /**
     * This doesn't really make sense since in coevolution you have a best solution for each population.
     */
    @Override
    public OptimisationSolution getBestSolution() {
        OptimisationSolution bestSolution = subPopulationsAlgorithms.get(0).getBestSolution();
        
        for (PopulationBasedAlgorithm currentAlgorithm : subPopulationsAlgorithms) {
            if (bestSolution.compareTo(currentAlgorithm.getBestSolution()) < 0) {
                bestSolution = currentAlgorithm.getBestSolution();
            }
        }
        
        return bestSolution;
    }

    /**
     * Can be useful to compare how the different populations are performing.
     * @return a list of the best solution in each population.
     */
    @Override
    public List<OptimisationSolution> getSolutions() {
        List<OptimisationSolution> solutions = Lists.newArrayList();
        
        for (PopulationBasedAlgorithm currentAlgorithm : this.getPopulations()) {
            for (OptimisationSolution solution : currentAlgorithm.getSolutions()) {
                solutions.add(solution);
            }
        }
        
        return solutions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void algorithmIteration() {
        for (PopulationBasedAlgorithm currentAlgorithm : subPopulationsAlgorithms) {
            currentAlgorithm.performIteration();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOptimisationProblem(OptimisationProblem problem) {
        Preconditions.checkArgument(problem instanceof CoevolutionOptimisationProblem, "Co-evolutionaty algorithms can only optimize problems that impliment the CoevolutionOptimisationProblem interface");
        super.setOptimisationProblem(problem);
    }
}
