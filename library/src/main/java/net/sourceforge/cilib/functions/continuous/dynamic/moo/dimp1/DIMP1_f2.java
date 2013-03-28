/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.dynamic.moo.dimp1;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This function is the f2 function of the DIMP1 problem defined in the
 * following paper: W.T. Koo and C.K. Goh and K.C. Tan. A predictive gradien
 * strategy for multiobjective evolutionary algorithms in a fast changing
 * environment, Memetic Computing, 2:87-110, 2010.
 *
 */
public class DIMP1_f2 implements ContinuousFunction {

    //member
    ContinuousFunction dimp1_g;
    ContinuousFunction dimp1_h;
    FunctionOptimisationProblem dimp1_g_problem;
    FunctionOptimisationProblem dimp1_h_problem;

    //Domain("R(-1, 1)^20");
    /**
     * Sets the g function with a specified problem.
     * @param problem FunctionOptimisationProblem used for the g function.
     */
    public void setDIMP1_g(FunctionOptimisationProblem problem) {
        this.dimp1_g_problem = problem;
        this.dimp1_g = (ContinuousFunction) problem.getFunction();
    }

    /**
     * Returns the problem used to set the g function.
     * @return dimp1_g_problem FunctionOptimisationProblem used for the g
     * function.
     */
    public FunctionOptimisationProblem getDIMP1_g_problem() {
        return this.dimp1_g_problem;
    }

    /**
     * Sets the g function that is used in the DIMP1 problem without specifying
     * the problem.
     * @param dimp1_g ContinuousFunction used for the g function.
     */
    public void setDIMP1_g(ContinuousFunction dimp1_g) {
        this.dimp1_g = dimp1_g;
    }

    /**
     * Returns the g function that is used in the DIMP1 problem.
     * @return dimp1_g ContinuousFunction used for the g function.
     */
    public ContinuousFunction getDIMP1_g() {
        return this.dimp1_g;
    }

    /**
     * Sets the h function with a specified problem.
     * @param problem FunctionOptimisationProblem used for the h function.
     */
    public void setDIMP1_h(FunctionOptimisationProblem problem) {
        this.dimp1_h_problem = problem;
        this.dimp1_h = (ContinuousFunction) problem.getFunction();
    }

    /**
     * Returns the problem used to set the h function.
     * @return dimp1_h_problem FunctionOptimisationProblem used for the h
     * function.
     */
    public FunctionOptimisationProblem getDIMP1_h_problem() {
        return this.dimp1_h_problem;
    }

    /**
     * Sets the h function that is used in the DIMP1 problem without specifying
     * the problem.
     * @param dimp1_h ContinuousFunction used for the h function.
     */
    public void setDIMP1_h(ContinuousFunction dimp1_h) {
        this.dimp1_h = dimp1_h;
    }

    /**
     * Sets the f1 hunction that is used in the DIMP1 problem without specifying
     * the problem.
     * @return dimp1_h ContinuousFunction used for the h function.
     */
    public ContinuousFunction getDIMP1_h() {
        return this.dimp1_h;
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
        double g = ((DIMP1_g) this.dimp1_g).apply(iteration, y);
        double h = ((DIMP1_h) this.dimp1_h).apply(iteration, input);

        double value = g * h;

        return value;
    }
}
