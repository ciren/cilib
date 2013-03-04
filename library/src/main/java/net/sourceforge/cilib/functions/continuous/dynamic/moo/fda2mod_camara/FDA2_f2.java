/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.dynamic.moo.fda2mod_camara;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This function is the f2 function of the FDA2_mod problem defined in the
 * following paper: M. Camara, J. Ortega and F. de Toro. Approaching dynamic
 * multi-objective optimization problems by using parallel evolutionary
 * algorithms, Advances in Multi-Objective Nature Inspired Computing, Studies in
 * Computational Intelligence, vol. 272, pp. 63-86, Springer Berlin/Heidelberg,
 * 2010.
 *
 */
public class FDA2_f2 implements ContinuousFunction {

    private static final long serialVersionUID = 7814549850032093196L;
    //member
    ContinuousFunction fda2_g;
    ContinuousFunction fda2_h;
    FunctionOptimisationProblem fda2_g_problem;
    FunctionOptimisationProblem fda2_h_problem;

    //Domain("R(-1, 1)^31")

    /**
     * Sets the g function with a specified problem.
     * @param problem FunctionOptimisationProblem used for the g function.
     */
    public void setFDA2_g(FunctionOptimisationProblem problem) {
        this.fda2_g_problem = problem;
        this.fda2_g = (ContinuousFunction) problem.getFunction();
    }

    /**
     * Returns the problem used to set the g function.
     * @return fda2_g_problem FunctionOptimisationProblem used for the g
     * function.
     */
    public FunctionOptimisationProblem getFDA2_g_problem() {
        return this.fda2_g_problem;
    }

    /**
     * Sets the g function that is used in the FDA2 problem without specifying
     * the problem.
     * @param fda2_g ContinuousFunction used for the g function.
     */
    public void setFDA2_g(ContinuousFunction fda2_g) {
        this.fda2_g = fda2_g;
    }

    /**
     * Returns the g function that is used in the FDA2 problem.
     * @return fda2_g ContinuousFunction used for the g function.
     */
    public ContinuousFunction getFDA2_g() {
        return this.fda2_g;
    }

    /**
     * Sets the h function with a specified problem.
     * @param problem FunctionOptimisationProblem used for the h function.
     */
    public void setFDA2_h(FunctionOptimisationProblem problem) {
        this.fda2_h_problem = problem;
        this.fda2_h = (ContinuousFunction) problem.getFunction();
    }

    /**
     * Returns the problem used to set the h function.
     * @return fda2_h_problem FunctionOptimisationProblem used for the h
     * function.
     */
    public FunctionOptimisationProblem getFDA2_h_problem() {
        return this.fda2_h_problem;
    }

    /**
     * Sets the h function that is used in the FDA2 problem.
     * @param fda2_h ContinuousFunction used for the h function.
     */
    public void setFDA2_h(ContinuousFunction fda2_h) {
        this.fda2_h = fda2_h;
    }

    /**
     * Returns the h function that is used in the FDA2 problem.
     * @return fda2_h ContinuousFunction used for the h function.
     */
    public ContinuousFunction getFDA2_h() {
        return this.fda2_h;
    }

    /**
     * Evaluates the function.
     */
    @Override
    public Double apply(Vector x) {
        int iteration = AbstractAlgorithm.get().getIterations();
        return this.apply(iteration, x);
    }

    /**
     * Evaluates the function for a specific iteration.
     */
    public double apply(int iteration, Vector x) {
        Vector y = x;
        if (x.size() > 1) {
            y = x.copyOfRange(1, 16);
        }

        double g = this.fda2_g.apply(y);
        double h = ((FDA2_h) this.fda2_h).apply(iteration, x);

        double value = g * h;
        return value;
    }
}
