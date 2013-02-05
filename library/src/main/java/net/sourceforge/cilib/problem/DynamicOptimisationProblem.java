/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem;

import net.sourceforge.cilib.functions.DynamicFunction;
import net.sourceforge.cilib.problem.changestrategy.ChangeStrategy;
import net.sourceforge.cilib.problem.changestrategy.IterationBasedSingleChangeStrategy;
import net.sourceforge.cilib.problem.objective.Maximise;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.type.types.Type;

public class DynamicOptimisationProblem extends AbstractProblem {

    protected DynamicFunction<Type, ? extends Number> function;
    protected ChangeStrategy changeStrategy;

    public DynamicOptimisationProblem() {
        this.objective = new Maximise();
        this.changeStrategy = new IterationBasedSingleChangeStrategy(1);
    }

    public DynamicOptimisationProblem(DynamicOptimisationProblem copy) {
        super(copy);
        this.function = copy.function;
        this.changeStrategy = copy.changeStrategy;
    }

    @Override
    public DynamicOptimisationProblem getClone() {
        return new DynamicOptimisationProblem(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Fitness calculateFitness(Type solution) {
        if (changeStrategy.shouldApply(this)) {
            function.changeEnvironment();
        }

        return objective.evaluate(function.apply(solution).doubleValue());
    }

    /**
     * <p>
     * Returns the error for the given solution. That is, a lower error value is returned if the
     * given solution is a better maximiser for the function. This method assumes that the
     * {@link FunctionOptimisationProblem} contains a {@link DynamicFunction}.
     * </p>
     * <p>
     * The lowest possible error (corresponding to the best solution) should be 0. However, if the
     * function incorrectly reports its maximum value then it is possible for error values to be
     * negative.
     * </p>
     *
     * @param solution The solution for which an error is sought.
     * @return The error.
     */
    public double getError(Type solution) {
        return function.getOptimum().doubleValue() - function.apply(solution).doubleValue();
    }

    public DynamicFunction<Type, ? extends Number> getFunction() {
        return function;
    }

    public void setFunction(DynamicFunction<Type, ? extends Number> function) {
        this.function = function;
    }

    public void setChangeStrategy(ChangeStrategy changeStrategy) {
        this.changeStrategy = changeStrategy;
    }
}
