/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.dynamic.moo.fda1mod_zhou;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This function is the h function of the FDA1_mod problem defined in the
 * following paper: A. Zhou et al. Prediction-based population re-initialization
 * for evolutionary dynamic multi-objective optimization, In Proceedings of the
 * International Conference on Evolutionary Multi-Criterion Optimization (EMO),
 * Lecture * Notes in Computer Science, 4403:832-846, Springer-Verlag
 * Berlin/Heidelberg, 2007.
 *
 */
public class FDA1_h implements ContinuousFunction {

    private static final long serialVersionUID = -539665464941830813L;
    //members
    private ContinuousFunction fda1_g;
    private ContinuousFunction fda1_f1;
    private FunctionOptimisationProblem fda1_f1_problem;
    private FunctionOptimisationProblem fda1_g_problem;
    //number of generations for which t remains fixed
    private int tau_t;
    //generation counter
    private int tau;
    //number of distinct steps in t
    private int n_t;

    /**
     * Creates a new instance of FDA1_g.
     */
    public FDA1_h() {
        //initialize the members
        this.tau_t = 5;
        this.tau = 1;
        this.n_t = 10;
    }

    /**
     * Sets the iteration number.
     * @param tau Iteration number.
     */
    public void setTau(int tau) {
        this.tau = tau;
    }

    /**
     * Returns the iteration number.
     * @return tau Iteration number.
     */
    public int getTau() {
        return this.tau;
    }

    /**
     * Sets the frequency of change.
     * @param tau_t Change frequency.
     */
    public void setTau_t(int tau_t) {
        this.tau_t = tau_t;
    }

    /**
     * Returns the frequency of change.
     * @return tau_t Change frequency.
     */
    public int getTau_t() {
        return this.tau_t;
    }

    /**
     * Sets the severity of change.
     * @param n_t Change severity.
     */
    public void setN_t(int n_t) {
        this.n_t = n_t;
    }

    /**
     * Returns the severity of change.
     * @return n_t Change severity.
     */
    public int getN_t() {
        return this.n_t;
    }

    /**
     * Sets the g function with a specified problem.
     * @param problem FunctionOptimisationProblem used for the g function.
     */
    public void setFDA1_g(FunctionOptimisationProblem problem) {
        this.fda1_g_problem = problem;
        this.fda1_g = (ContinuousFunction) problem.getFunction();
    }

    /**
     * Returns the problem used to set the g function.
     * @return fda1_g_problem FunctionOptimisationProblem used for the g
     * function.
     */
    public FunctionOptimisationProblem getFDA1_g_problem() {
        return this.fda1_g_problem;
    }

    /**
     * Sets the g function that is used in the FDA1 problem without specifying
     * the problem.
     * @param fda1_g ContinuousFunction used for the g function.
     */
    public void setFDA1_g(ContinuousFunction fda1_g) {
        this.fda1_g = fda1_g;
    }

    /**
     * Returns the g function that is used in the FDA1 problem.
     * @return fda1_g ContinuousFunction used for the g function.
     */
    public ContinuousFunction getFDA1_g() {
        return this.fda1_g;
    }

    /**
     * Sets the f1 function with a specified problem.
     * @param problem FunctionOptimisationProblem used for the f1 function.
     */
    public void setFDA1_f(FunctionOptimisationProblem problem) {
        this.fda1_f1_problem = problem;
        this.fda1_f1 = (ContinuousFunction) problem.getFunction();
    }

    /**
     * Returns the problem used to set the f1 function.
     * @return fda1_f1_problem FunctionOptimisationProblem used for the f1
     * function.
     */
    public FunctionOptimisationProblem getFDA1_f_problem() {
        return this.fda1_f1_problem;
    }

    /**
     * Sets the f1 function that is used in the FDA1 problem without specifying
     * the problem.
     * @param fda1_f1 ContinuousFunction used for the f1 function.
     */
    public void setFDA1_f(ContinuousFunction fda1_f1) {
        this.fda1_f1 = fda1_f1;
    }

    /**
     * Returns the f1 function that is used in the FDA1 problem.
     * @return fda1_f1 ContinuousFunction used for the f1 function.
     */
    public ContinuousFunction getFDA1_f() {
        return this.fda1_f1;
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
        double t = (1.0 / (double) n_t) * Math.floor((double) iteration / (double) this.tau_t);
        double G = Math.sin(0.5 * Math.PI * t);
        double H = 1.5 + (double) G;

        //only the first element
        Vector y = x.copyOfRange(0, 1);

        //evaluate the fda1_g function
        double g = ((FDA1_g) this.fda1_g).apply(iteration, x);
        //evaluate the fda1_f1 function
        double f1 = this.fda1_f1.apply(y);

        double value = 1.0;
        value -= Math.pow((double) f1 / (double) g, H);

        return value;
    }
}
