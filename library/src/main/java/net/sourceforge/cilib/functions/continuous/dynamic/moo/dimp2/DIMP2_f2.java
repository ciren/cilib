/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.dynamic.moo.dimp2;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This function is the f2 function of the DIMP2 problem defined in the
 * following paper: W.T. Koo and C.K. Goh and K.C. Tan. A predictive gradien
 * strategy for multiobjective evolutionary algorithms in a fast changing
 * environment, Memetic Computing, 2:87-110, 2010.
 *
 */
public class DIMP2_f2 implements ContinuousFunction {

    //member
    ContinuousFunction dimp2_g;
    ContinuousFunction dimp2_h;
    FunctionOptimisationProblem dimp2_g_problem;
    FunctionOptimisationProblem dimp2_h_problem;

    //Domain("R(-1, 1)^20")

    /**
     * Sets the g function with a specified problem.
     * @param problem FunctionOptimisationProblem used for the g function.
     */
    public void setDIMP2_g(FunctionOptimisationProblem problem) {
        this.dimp2_g_problem = problem;
        this.dimp2_g = (ContinuousFunction) problem.getFunction();
    }

    /**
     * Returns the problem used to set the g function.
     * @return dimp2_g_problem FunctionOptimisationProblem used for the g
     * function.
     */
    public FunctionOptimisationProblem getDIMP2_g_problem() {
        return this.dimp2_g_problem;
    }

    /**
     * Sets the g function that is used in the DIMP2 problem without specifying
     * the problem.
     * @param dimp2_g ContinuousFunction used for the g function.
     */
    public void setDIMP2_g(ContinuousFunction dimp2_g) {
        this.dimp2_g = dimp2_g;
    }

    /**
     * Returns the g function that is used in the DIMP2 problem.
     * @return dimp2_g ContinuousFunction used for the g function.
     */
    public ContinuousFunction getDIMP2_g() {
        return this.dimp2_g;
    }

    /**
     * Sets the h function with a specified problem.
     * @param problem FunctionOptimisationProblem used for the h function.
     */
    public void setDIMP2_h(FunctionOptimisationProblem problem) {
        this.dimp2_h_problem = problem;
        this.dimp2_h = (ContinuousFunction) problem.getFunction();
    }

    /**
     * Returns the problem used to set the h function.
     * @return dimp2_h_problem FunctionOptimisationProblem used for the h
     * function.
     */
    public FunctionOptimisationProblem getDIMP2_h_problem() {
        return this.dimp2_h_problem;
    }

    /**
     * Sets the h function that is used in the DIMP2 problem without specifying
     * the problem.
     * @param dimp2_h ContinuousFunction used for the h function.
     */
    public void setDIMP2_h(ContinuousFunction dimp2_h) {
        this.dimp2_h = dimp2_h;
    }

    /**
     * Sets the f1 hunction that is used in the DIMP2 problem without specifying
     * the problem.
     * @return dimp2_h ContinuousFunction used for the h function.
     */
    public ContinuousFunction getDIMP2_h() {
        return this.dimp2_h;
    }

    /**
     * Evaluates the function. g*h
     */
    @Override
    public Double apply(Vector input) {
        int iteration = AbstractAlgorithm.get().getIterations();
        return apply(iteration, input);
    }

    /**
     * Evaluates the function for a specific iteration. g*h
     */
    public Double apply(int iteration, Vector input) {
        Vector y = input.copyOfRange(1, input.size());
        double g = ((DIMP2_g) this.dimp2_g).apply(iteration, y);
        double h = ((DIMP2_h) this.dimp2_h).apply(iteration, input);

        double value = g * h;

        return value;
    }
}
