/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.dynamic.moo.deceptive.fda5;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This function is the fk function of the FDA5 problem defined on page 430 in
 * the following article: M.Farina, K.Deb, P.Amato. Dynamic multiobjective
 * optimization problems: test cases, approximations and applications, IEEE
 * Transactions on Evolutionary Computation, 8(5): 425-442
 */
public class FDA5_fk implements ContinuousFunction {

    private static final long serialVersionUID = -7037264042756770881L;
    //members
    FunctionOptimisationProblem fda5_g_problem;
    ContinuousFunction fda5_g;
    int M;
    int n;
    int K;
    //number of generations for which t remains fixed
    private int tau_t;
    //generation counter
    private int tau;
    //number of distinct steps in t
    private int n_t;

    /**
     * Creates a new instance of FDA5_fk.
     */
    public FDA5_fk() {
        super();
        this.M = 3;
        this.n = this.M + 9;
        this.K = 2;
        //initialize the members
        this.tau_t = 5;
        this.tau = 1;
        this.n_t = 10;
    }

    /**
     * Sets the number of objectives.
     *
     * @param m Number of objectives.
     */
    public void setM(int m) {
        this.M = m;
        this.n = this.M + 9;
    }

    /**
     * Returns the number of objectives.
     *
     * @return M Number of objectives.
     */
    public int getM() {
        return this.M;
    }

    /**
     * Sets the number of variables.
     *
     * @param n Number of variables.
     */
    public void setN(int n) {
        this.n = n;
        this.M = this.n - 9;
    }

    /**
     * Returns the number of variables.
     *
     * @return n Number of variables.
     */
    public int getN() {
        return this.n;
    }

    /**
     * Sets the k value of the objective.
     *
     * @param k Objective number.
     */
    public void setK(int k) {
        this.K = k;
    }

    /**
     * Returns the k value of the objective.
     *
     * @return K Objective number.
     */
    public int getK() {
        return this.K;
    }

    /**
     * Sets the iteration number.
     *
     * @param tau Iteration number.
     */
    public void setTau(int tau) {
        this.tau = tau;
    }

    /**
     * Returns the iteration number.
     *
     * @return tau Iteration number.
     */
    public int getTau() {
        return this.tau;
    }

    /**
     * Sets the frequency of change.
     *
     * @param tau_t Change frequency.
     */
    public void setTau_t(int tau_t) {
        this.tau_t = tau_t;
    }

    /**
     * Returns the frequency of change.
     *
     * @return tau_t Change frequency.
     */
    public int getTau_t() {
        return this.tau_t;
    }

    /**
     * Sets the severity of change.
     *
     * @param n_t Change severity.
     */
    public void setN_t(int n_t) {
        this.n_t = n_t;
    }

    /**
     * Returns the severity of change.
     *
     * @return n_t Change severity.
     */
    public int getN_t() {
        return this.n_t;
    }

    /**
     * Sets the g function with a specified problem.
     *
     * @param problem FunctionOptimisationProblem used for the g function.
     */
    public void setFDA5_g(FunctionOptimisationProblem problem) {
        this.fda5_g_problem = problem;
        this.fda5_g = (ContinuousFunction) problem.getFunction();
    }

    /**
     * Returns the problem used to set the g function.
     *
     * @return fda5_g_problem FunctionOptimisationProblem used for the g
     * function.
     */
    public FunctionOptimisationProblem getFDA5_g_problem() {
        return this.fda5_g_problem;
    }

    /**
     * Sets the g function that is used in the FDA5 problem without specifying
     * the problem.
     *
     * @param fda5_g ContinuousFunction used for the g function.
     */
    public void setFDA5_g(ContinuousFunction fda5_g) {
        this.fda5_g = fda5_g;
    }

    /**
     * Returns the g function that is used in the FDA5 problem.
     *
     * @return fda5_g ContinuousFunction used for the g function.
     */
    public ContinuousFunction getFDA5_g() {
        return this.fda5_g;
    }

    /**
     * Evaluates the function.
     */
    @Override
    public Double apply(Vector x) {
        this.tau = AbstractAlgorithm.get().getIterations();
        return this.apply(this.tau, x);
    }

    /**
     * Evaluates the function for a specific iteration.
     */
    public Double apply(int iteration, Vector x) {
        double t = (1.0 / (double) n_t) * Math.floor((double) iteration / (double) this.tau_t);
        double F = 1 + 100.0 * Math.pow(Math.sin(0.5 * Math.PI * t), 4);
        Vector y = x;
        if (x.size() > 1) {
            y = x.copyOfRange(this.M - 1, this.n);
        }

        double g = ((FDA5_g) this.fda5_g).apply(iteration, y);

        double value = 1.0 + g;
        double mult = 1.0;


        for (int i = 1; i <= (this.M - this.K); i++) {
            double y_i = Math.pow(x.doubleValueOf(i - 1), F);
            mult *= Math.cos(y_i * Math.PI / 2.0);
        }

        double yy = Math.pow(x.doubleValueOf(this.M - this.K), F);
        mult *= Math.sin(yy * Math.PI / 2.0);


        mult *= value;

        return mult;
    }
}
