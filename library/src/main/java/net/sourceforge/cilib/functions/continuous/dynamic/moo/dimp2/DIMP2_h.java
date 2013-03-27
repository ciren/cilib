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
 * This function is the h function of the DIMP2 problem defined in the following
 * paper: W.T. Koo and C.K. Goh and K.C. Tan. A predictive gradien strategy for
 * multiobjective evolutionary algorithms in a fast changing environment,
 * Memetic Computing, 2:87-110, 2010.
 *
 */
public class DIMP2_h implements ContinuousFunction {

    //members
    private ContinuousFunction dimp2_g;
    private ContinuousFunction dimp2_f1;
    private FunctionOptimisationProblem dimp2_f1_problem;
    private FunctionOptimisationProblem dimp2_g_problem;

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
     * Sets the f1 function with a specified problem.
     * @param problem FunctionOptimisationProblem used for the f1 function.
     */
    public void setDIMP2_f(FunctionOptimisationProblem problem) {
        this.dimp2_f1_problem = problem;
        this.dimp2_f1 = (ContinuousFunction) problem.getFunction();
    }

    /**
     * Returns the problem used to set the f1 function.
     * @return dimp2_f1_problem FunctionOptimisationProblem used for the f1
     * function.
     */
    public FunctionOptimisationProblem getDIMP2_f_problem() {
        return this.dimp2_f1_problem;
    }

    /**
     * Sets the f1 function that is used in the DIMP2 problem without specifying
     * the problem.
     * @param dimp2_f1 ContinuousFunction used for the f1 function.
     */
    public void setDIMP2_f(ContinuousFunction dimp2_f1) {
        this.dimp2_f1 = dimp2_f1;
    }

    /**
     * Returns the f1 function that is used in the DIMP2 problem.
     * @return dimp2_f1 ContinuousFunction used for the f1 function.
     */
    public ContinuousFunction getDIMP2_f() {
        return this.dimp2_f1;
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
        //evaluate the dimp2_g function
        double g = ((DIMP2_g) this.dimp2_g).apply(iteration, z);
        //evaluate the dimp2_f1 function
        double f1 = this.dimp2_f1.apply(y);

        double value = 1.0;
        value -= Math.pow((double) f1 / (double) g, 2);

        return value;
    }
}
