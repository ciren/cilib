/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.dynamic.moo.fda1;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This function is the h function of the FDA1 problem defined on page 428 in the following paper:
 * M.Farina, K.Deb, P.Amato. Dynamic multiobjective optimization problems: test cases, approximations
 * and applications, IEEE Transactions on Evolutionary Computation, 8(5): 425-442, 2003
 *
 */
public class FDA1_h implements ContinuousFunction {

    private static final long serialVersionUID = -539665464941830813L;

    private ContinuousFunction fda1_g;
    private ContinuousFunction fda1_f1;

    /**
     * Default constructor
     */
    public FDA1_h() {
    }

    /**
     * Copy constructor
     */
    public FDA1_h(FDA1_h copy) {
        this.fda1_f1 = copy.fda1_f1;
        this.fda1_g = copy.fda1_g;
    }

    /**
     * Sets the g function that is used in the FDA1 problem
     * @param fda1_g
     */
    public void setFDA1_g(ContinuousFunction fda1_g) {
        this.fda1_g = fda1_g;
    }

    /**
     * Returns the g function that is used in the FDA1 problem
     * @return
     */
    public ContinuousFunction getFDA1_g() {
        return this.fda1_g;
    }

    /**
     * Sets the f1 function that is used in the FDA1 problem
     * @param fda1_f1
     */
    public void setFDA1_f(ContinuousFunction fda1_f1) {
        this.fda1_f1 = fda1_f1;
    }

    /**
     * Gets the f1 function that is used in the FDA1 problem
     * @return
     */
    public ContinuousFunction getFDA1_f() {
        return this.fda1_f1;
    }

    /**
     * Evaluates the function
     * h(f_1, g) = 1-sqrt(f_1/g)
     */
    @Override
    public Double apply(Vector input) {
        Vector y = input.copyOfRange(0, 1);
        Vector z = input.copyOfRange(1, input.size());
        double g = this.fda1_g.apply(z);
        double f1 = this.fda1_f1.apply(y);

        return 1.0 - Math.sqrt(f1 / g);
    }
}
