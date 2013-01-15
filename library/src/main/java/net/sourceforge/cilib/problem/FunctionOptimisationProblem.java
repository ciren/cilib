/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem;

import net.sourceforge.cilib.functions.Function;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This class serves as a base class for function optimisation problems using a
 * {@link net.sourceforge.cilib.functions.Function}.
 *
 */
public class FunctionOptimisationProblem extends AbstractProblem {

    private static final long serialVersionUID = 7944544624736580311L;

    protected Function<Vector, ? extends Number> function;

    /**
     * Creates a new instance of {@code FunctionOptimisationProblem} with {@code null} function.
     * Remember to always set a {@link net.sourceforge.cilib.functions.Function} before attempting to apply
     * an {@linkplain net.sourceforge.cilib.algorithm.Algorithm algorithm} to this problem.
     *
     * @see #setFunction(net.sourceforge.cilib.functions.Function)
     */
    public FunctionOptimisationProblem() {
        this.function = null;
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public FunctionOptimisationProblem(FunctionOptimisationProblem copy) {
        super(copy);
        this.function = copy.function;
        this.objective = copy.objective;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FunctionOptimisationProblem getClone() {
        return new FunctionOptimisationProblem(this);
    }

    /**
     * Sets the function that is to be optimised.
     *
     * @param function The function.
     */
    public void setFunction(Function<Vector, ? extends Number> function) {
        this.function = function;
    }

    /**
     * Accessor for the function that is to be optimised.
     *
     * @return The function
     */
    public Function<Vector, ? extends Number> getFunction() {
        return function;
    }

    @Override
    protected Fitness calculateFitness(Type solution) {
        return objective.evaluate(function.apply((Vector) solution).doubleValue());
    }
}
