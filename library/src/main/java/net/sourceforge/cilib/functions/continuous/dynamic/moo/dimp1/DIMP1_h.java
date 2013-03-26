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
 * This function is the h function of the DIMP1 problem defined in the following
 * paper: W.T. Koo and C.K. Goh and K.C. Tan. A predictive gradien strategy for
 * multiobjective evolutionary algorithms in a fast changing environment,
 * Memetic Computing, 2:87-110, 2010.
 *
 */
public class DIMP1_h implements ContinuousFunction {

    //members
    private ContinuousFunction dimp1_g;
    private ContinuousFunction dimp1_f1;
    private FunctionOptimisationProblem dimp1_f1_problem;
    private FunctionOptimisationProblem dimp1_g_problem;

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
     * Sets the f1 function with a specified problem.
     * @param problem FunctionOptimisationProblem used for the f1 function.
     */
    public void setDIMP1_f(FunctionOptimisationProblem problem) {
        this.dimp1_f1_problem = problem;
        this.dimp1_f1 = (ContinuousFunction) problem.getFunction();
    }

    /**
     * Returns the problem used to set the f1 function.
     * @return dimp1_f1_problem FunctionOptimisationProblem used for the f1
     * function.
     */
    public FunctionOptimisationProblem getDIMP1_f_problem() {
        return this.dimp1_f1_problem;
    }

    /**
     * Sets the f1 function that is used in the DIMP1 problem without specifying
     * the problem.
     * @param dimp1_f1 ContinuousFunction used for the f1 function.
     */
    public void setDIMP1_f(ContinuousFunction dimp1_f1) {
        this.dimp1_f1 = dimp1_f1;
    }

    /**
     * Returns the f1 function that is used in the DIMP1 problem.
     * @return dimp1_f1 ContinuousFunction used for the f1 function.
     */
    public ContinuousFunction getDIMP1_f() {
        return this.dimp1_f1;
    }

    /**
     * Evaluates the function.
     */
    @Override
    public Double apply(Vector x) {
        int iteration = AbstractAlgorithm.get().getIterations();
        return apply(iteration, x);
    }

    /**
     * Evaluates the function for a specific iteration.
     */
    public Double apply(int iteration, Vector x) {

        //only the first element
        Vector y = x.copyOfRange(0, 1);
        //all the elements except the first element
        Vector z = x.copyOfRange(1, x.size());
        //evaluate the dimp1_g function
        double g = ((DIMP1_g) this.dimp1_g).apply(iteration, z);
        //evaluate the dimp1_f1 function
        double f1 = this.dimp1_f1.apply(y);

        double value = 1.0;
        value -= Math.pow((double) f1 / (double) g, 2);

        return value;
    }
}
