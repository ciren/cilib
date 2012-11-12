/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.dynamic.moo.fda1;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.functions.Function;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This function is the g*h function of the FDA1 problem defined on page 428 in the following paper:
 * M.Farina, K.Deb, P.Amato. Dynamic multiobjective optimization problems: test cases, approximations
 * and applications, IEEE Transactions on Evolutionary Computation, 8(5): 425-442, 2003
 *
 * R(-1, 1)^20
 *
 */
public class FDA1_f2 implements ContinuousFunction {

    private static final long serialVersionUID = 6369118486095689078L;

    private ContinuousFunction fda1_g;
    private ContinuousFunction fda1_h;

    /**
     * Default constructor
     */
    public FDA1_f2() {
    }

    /**
     * Copy constructor.
     * @param copy
     */
    public FDA1_f2(FDA1_f2 copy) {
        this.fda1_g = copy.fda1_g;
        this.fda1_h = copy.fda1_h;
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
    public Function getFDA1_g() {
        return this.fda1_g;
    }

    /**
     * Sets the f1 function that is used in the FDA1 problem
     * @param fda1_h
     */
    public void setFDA1_h(ContinuousFunction fda1_h) {
        this.fda1_h = fda1_h;
    }

    /**
     * Gets the f1 function that is used in the FDA1 problem
     * @return
     */
    public Function getFDA1_h() {
        return this.fda1_h;
    }

    /**
     * Evaluates the function
     * g*h
     */
    @Override
    public Double apply(Vector input) {
        Vector y = input.copyOfRange(1, input.size());
        double g = this.fda1_g.apply(y).doubleValue();
        double h = this.fda1_h.apply(input).doubleValue();
        return g * h;
    }
}
