/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.dynamic.moo.fda2mod_deb;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This function is the h function of the FDA2_mod problem defined in the
 * following paper: K. Deb, U. Rao N and S. Karthik. Dynamic Multi-objective
 * optimization and decision making using modified NSGA-II: A case study on
 * hydro-thermal power scheduling, In Proceedings of the International
 * Conference on Evolutionary Multi-Criterion Optimization (EMO), Lecture Notes
 * in Computer Science, 4403:803-817, Springer-Verlag Berlin/Heidelberg, 2007.
 *
 */
public class FDA2_h implements ContinuousFunction {

    private static final long serialVersionUID = -637862405309737323L;
    //members
    ContinuousFunction fda2_f;
    ContinuousFunction fda2_g;
    FunctionOptimisationProblem fda2_f_problem;
    FunctionOptimisationProblem fda2_g_problem;
    //number of generations for which t remains fixed
    private int tau_t;
    //generation counter
    private int tau;
    //number of distinct steps in t
    private int n_t;
    //maximum number of iterations
    private int tau_max;

    /**
     * Creates a new instance of FDA2_h.
     */
    public FDA2_h() {
        //initialize the members
        this.tau_t = 5;
        this.tau = 1;
        this.n_t = 1;
        this.tau_max = 200;
    }

    /**
     * Sets the f1 function with a specified problem.
     * @param problem FunctionOptimisationProblem used for the f1 function.
     */
    public void setFDA2_f(FunctionOptimisationProblem problem) {
        this.fda2_f_problem = problem;
        this.fda2_f = (ContinuousFunction) problem.getFunction();
    }

    /**
     * Returns the problem used to set the f1 function.
     * @return fda2_f_problem FunctionOptimisationProblem used for the f1
     * function.
     */
    public FunctionOptimisationProblem getFDA2_f_problem() {
        return this.fda2_f_problem;
    }

    /**
     * Sets the f1 function that is used in the FDA2 problem without specifying
     * the problem.
     * @param fda2_f ContinuousFunction used for the f1 function.
     */
    public void setFDA2_f(ContinuousFunction fda2_f) {
        this.fda2_f = fda2_f;
    }

    /**
     * Returns the f1 function that is used in the FDA2 problem.
     * @return fda2_f ContinuousFunction used for the f1 function.
     */
    public ContinuousFunction getFDA2_f() {
        return this.fda2_f;
    }

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
     * Evaluates the function.
     */
    @Override
    public Double apply(Vector x) {
        this.tau = AbstractAlgorithm.get().getIterations();
        return this.apply(this.tau, x);
    }

    /**
     * Evaluates the function for a specific iteration.
     *
     */
    public Double apply(int iteration, Vector x) {
        double t_old = (1.0 / (double) n_t) * 2.0 * Math.floor((double) (iteration - 1.0) / (double) this.tau_t) * (this.tau_t / (this.tau_max - this.tau_t));
        double H = 2.0 * (Math.sin(0.5 * Math.PI * t_old));
        Vector xI = x;
        Vector xII = x;
        Vector xIII = x;
        if (x.size() > 1) {
            xI = x.copyOfRange(0, 1);
            xII = x.copyOfRange(1, 6);
            xIII = x.copyOfRange(6, x.size());
        }

        double f = this.fda2_f.apply(xI);
        double g = this.fda2_g.apply(xII);

        double value = 1.0;
        double power = H;

        for (int k = 0; k < xIII.size(); k++) {
            power += Math.pow(xIII.doubleValueOf(k) - (H / 4.0), 2);
        }

        double f_div_g = Math.pow((double) f / (double) g, 2.0);
        value -= Math.pow(f_div_g, power);

        return value;
    }
}
