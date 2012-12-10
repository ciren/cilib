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
package net.sourceforge.cilib.stoppingcondition;

import java.util.ArrayList;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.io.StandardPatternDataTable;
import net.sourceforge.cilib.nn.NeuralNetwork;
import net.sourceforge.cilib.problem.nn.NNTrainingProblem;
import net.sourceforge.cilib.stoppingcondition.nnperformancecomparators.NNPerformanceComparator;
import net.sourceforge.cilib.stoppingcondition.nnperformancecomparators.OneTailedWilcoxonNNPerformanceComparator;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Algorithm is complete if no significant improvement in NN performance is observed.
 * Statistical tests such as the (Wilcoxon signed-rank test) can be used to determine
 * the significance of an improvement.
 * 
 * This class keeps memory of the performance of the NN from previous stopping condition test.
 */
public class NNPerformanceChangeStoppingCondition implements StoppingCondition<Algorithm> {

    private ArrayList<Vector> previousResults;
    private NNPerformanceComparator comparator;

    public NNPerformanceChangeStoppingCondition() {
        previousResults = null;
        comparator = new OneTailedWilcoxonNNPerformanceComparator();
    }

    public NNPerformanceChangeStoppingCondition(NNPerformanceChangeStoppingCondition rhs) {
        previousResults = rhs.previousResults;
        comparator = rhs.comparator;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public double getPercentageCompleted(Algorithm algorithm) {
        return Math.min(1.0, comparator.getLastPValue()/(comparator.getTargetPValue()));
    }

    /**
     * Applies stopping condition test.
     * @param algorithm The algorithm to be stopped.
     * @result true if algorithm is complete.
     */
    @Override
    public boolean apply(Algorithm algorithm) {
        NNTrainingProblem problem = (NNTrainingProblem) algorithm.getOptimisationProblem();
        StandardPatternDataTable validationSet = problem.getValidationSet();
        NeuralNetwork currentNetwork = problem.getNeuralNetwork();
        currentNetwork.setWeights((Vector) algorithm.getBestSolution().getPosition());

        //calculate output set for the new architecture
        ArrayList<Vector> currentResults = new ArrayList<Vector>();
        for (StandardPattern curPattern : validationSet) {
            Vector output = currentNetwork.evaluatePattern(curPattern);
            currentResults.add(output);
        }

        boolean isSame = false;
        if (previousResults != null)
            isSame = comparator.isSame(validationSet, previousResults, currentResults);

        previousResults = currentResults;

        return isSame;
    }

    /**
     * Sets the NN comparator to be used.
     * @param newComparator The new comparator.
     */
    public void setComparator(NNPerformanceComparator newComparator) {
        comparator = newComparator;
    }

    /**
     * Gets the results from the new NN from the previous test.
     * Should only be used for testing purposes.
     * @return Previous results.
     */
    public ArrayList<Vector> getPreviousResults() {
        return previousResults;
    }
}
