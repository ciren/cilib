/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.dynamic.moo.dmop3;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This function is the f1 function of the DMOP3 problem defined on page 119 in
 * the following article: C-K. Goh and K.C. Tan. A competitive-cooperative
 * coevolutionary paradigm for dynamic multiobjective optimization, IEEE
 * Transactions on Evolutionary Computation, 13(1): 103-127, 2009
 *
 */
public class DMOP3_f1 implements ContinuousFunction {

    private static final long serialVersionUID = 4525717945098304120L;
    //members
    //index of member to select - index starts at 0
    private static int r;
    //number of generations for which t remains fixed
    private int tau_t;
    private static int rIteration;

    /**
     * Creates a new instance of DMOP3_f1.
     */
    public DMOP3_f1() {
        DMOP3_f1.r = -1;
        DMOP3_f1.rIteration = -1;
        tau_t = 5;
    }

    /**
     * Sets which x to set as f1's value.
     * @param r Index of x value to select.
     */
    public static void setR(int r) {
        DMOP3_f1.r = r;
    }

    /**
     * Returns which x to set as f1's value.
     * @return r Index of x value to select.
     */
    public static int getR() {
        return DMOP3_f1.r;
    }

    /**
     * Sets the iteration when r was set.
     * @param r the iteration when r was last randomly selected.
     */
    public static void setRIteration(int r) {
        DMOP3_f1.rIteration = r;
    }

    /**
     * Returns the iteration when r was set.
     * @return rIteration Iteration when r was set.
     */
    public static int getRIteration() {
        return DMOP3_f1.rIteration;
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
    public Double apply(int iteration, Vector x) {

        if (((iteration % this.tau_t) == 0) && (DMOP3_f1.rIteration != iteration)) {
            int indeks = Rand.nextInt(x.size() - 1);
            DMOP3_f1.setR(indeks);
            DMOP3_f1.setRIteration(iteration);
        } //sets r to a random value
        else if (DMOP3_f1.r == -1) {
            int indeks = Rand.nextInt(x.size() - 1);
            DMOP3_f1.setR(indeks);
            DMOP3_f1.setRIteration(iteration);
        }
        double value = Math.abs(x.doubleValueOf(DMOP3_f1.r));
        return value;

    }
}
