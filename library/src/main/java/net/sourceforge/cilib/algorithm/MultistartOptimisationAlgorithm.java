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
package net.sourceforge.cilib.algorithm;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.coevolution.cooperative.ParticipatingAlgorithm;
import net.sourceforge.cilib.coevolution.cooperative.contributionselection.ContributionSelectionStrategy;
import net.sourceforge.cilib.problem.AbstractProblem;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.problem.solution.InferiorFitness;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;
import net.sourceforge.cilib.stoppingcondition.StoppingCondition;
import net.sourceforge.cilib.type.types.Type;

/**
 * <code>MultistartOptimisationAlgorithm</code> is simply a wrapper. The wrapped
 * optimisation algorithm is subject to restart conditions. Each time one of these
 * conditions is satisfied, the wrapped algorithm is re-initialised and execution continues until
 * this algorithm's stopping conditions are satisfied.
 *
 * <p>
 * This class implements a generalised multistart optimisation algorithm. The
 * original Multistart PSO is due to F. van den Bergh, reference:
 *          F. van den Bergh, "An Analysis of Particle Swarm Optimizers,"
 *          PhD thesis, Department of Computer Science,
 *          University of Pretoria, South Africa, 2002.
 *
 */
public class MultistartOptimisationAlgorithm extends AbstractAlgorithm implements ParticipatingAlgorithm {
    private static final long serialVersionUID = 1493525363256406120L;

    /** Creates a new instance of MultistartOptimisationAlgorithm. */
    public MultistartOptimisationAlgorithm() {
        singleIteration = new SingleIteration();
        problem = null;
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public MultistartOptimisationAlgorithm(MultistartOptimisationAlgorithm copy) {
        super(copy);
        this.problem = copy.problem.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MultistartOptimisationAlgorithm getClone() {
        return new MultistartOptimisationAlgorithm(this);
    }

    /**
     * Sets the target optimisation algorithm that is subject to restarting.
     *
     * @param algorithm Any {@link OptimisationAlgorithm} that extends {@link Algorithm}.
     */
    public void setTargetAlgorithm(Algorithm algorithm) {
        optimisationAlgorithm = (AbstractAlgorithm) algorithm;
        this.algorithm = (AbstractAlgorithm) algorithm;
        this.algorithm.addStoppingCondition(singleIteration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Problem getOptimisationProblem() {
        return problem.getTarget();
    }

    /**
     * Return the fitness of the solution.
     * @return The current {@linkplain Fitness}.
     */
    public Fitness getSolutionFitness() {
        return fitness;
    }

    /**
     * Set the optimisation problem.
     * @param problem The problem to set.
     */
    @Override
    public void setOptimisationProblem(Problem problem) {
        this.problem = new MultistartProblemAdapter(problem);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContributionSelectionStrategy getContributionSelectionStrategy() {
        return ((ParticipatingAlgorithm)algorithm).getContributionSelectionStrategy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setContributionSelectionStrategy(ContributionSelectionStrategy strategy) {
        ((ParticipatingAlgorithm)algorithm).setContributionSelectionStrategy(strategy);
    }

    /**
     * Add a stopping condition used to determine when the algorithm
     * should be restarted. Equivalent to calling {@link Algorithm#addStoppingCondition(StoppingCondition)} on
     * the algorithm set in {@link #setTargetAlgorithm(OptimisationAlgorithm)}.
     *
     * @param indicator The {@link StoppingCondition} to be added.
     */
    public void addRestartCondition(StoppingCondition condition) {
        algorithm.addStoppingCondition(condition);
    }

    /**
     * Removes a restart condition.
     * Equivalent to calling {@link Algorithm#removeStoppingCondition(StoppingCondition)} on
     * the algorithm set in {@link #setTargetAlgorithm(OptimisationAlgorithm)}.
     *
     * @param condition The {@link StoppingCondition} to be removed.
     */
    public void removeRestartCondition(StoppingCondition condition) {
        algorithm.removeStoppingCondition(condition);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void algorithmInitialisation() {
        if (problem != null) {
            optimisationAlgorithm.setOptimisationProblem(problem);
        }
        fitness = InferiorFitness.instance();
        restarts = 0;
        algorithm.performInitialisation();
        solution = optimisationAlgorithm.getBestSolution();
    }

    /**
     * Perform an algorithm iteration, then restart the {@linkplain Algorithm} and increment
     * the number of restarts.
     */
    @Override
    public void algorithmIteration() {
        algorithm.run();
        singleIteration.reset();

        OptimisationSolution tmp = optimisationAlgorithm.getBestSolution();
        if (tmp.getFitness().compareTo(fitness) > 0) {
            fitness = tmp.getFitness();
            solution = tmp;
        }

        if (algorithm.isFinished()) {
            problem.resetFitnessCounter();
            algorithm.performInitialisation();
            ++restarts;
        }
    }

    /**
     * Returns the number of times that the algorithm has been restarted.
     *
     * @return The number of restarts.
     */
    public int getRestarts() {
        return restarts;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OptimisationSolution getBestSolution() {
        return solution;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<OptimisationSolution> getSolutions() {
        // TODO: Fix this so that all the solutions found at the time of each restart are added to the collection

        ArrayList<OptimisationSolution> solutions = new ArrayList<OptimisationSolution>(1);
        solutions.add(getBestSolution());
        return solutions;
    }

    private AbstractAlgorithm algorithm;
    private AbstractAlgorithm optimisationAlgorithm;
    private int restarts;
    private SingleIteration singleIteration;
    private MultistartProblemAdapter problem;
    private OptimisationSolution solution;
    private Fitness fitness;

    private class MultistartProblemAdapter extends AbstractProblem {

        private static final long serialVersionUID = -3156973576101060294L;

        public MultistartProblemAdapter() {

        }

        public MultistartProblemAdapter(Problem target) {
            this.target = target;
        }

        public MultistartProblemAdapter(MultistartProblemAdapter copy) {

        }

        @Override
        public MultistartProblemAdapter getClone() {
            return new MultistartProblemAdapter(this);
        }

        public Problem getTarget() {
            return target;
        }

        /* (non-Javadoc)
         * @see net.sourceforge.cilib.Problem.OptimisationProblemAdapter#calculateFitness(java.lang.Object)
         */
        @Override
        protected Fitness calculateFitness(Type solution) {
            return target.getFitness(solution);
        }

        public void resetFitnessCounter() {
            fitnessEvaluations.set(0);
        }

        private Problem target;

    }

    private class SingleIteration implements StoppingCondition<Algorithm> {
        private static final long serialVersionUID = 7136206631115015558L;

        private int iteration;

        @Override
        public double getPercentageCompleted(Algorithm algorithm) {
            if (iteration == algorithm.getIterations()) {
                return 0.0;
            }
            else {
                return 1.0;
            }
        }

        @Override
        public boolean apply(Algorithm input) {
            return iteration != algorithm.getIterations();
        }

        public void reset() {
            iteration = algorithm.getIterations();
        }
    }

}
