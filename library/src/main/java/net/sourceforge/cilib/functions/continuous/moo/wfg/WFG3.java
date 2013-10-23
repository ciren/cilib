/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.moo.wfg;

import java.util.List;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.type.types.container.Vector;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

/**
 *
 */
public class WFG3 extends WFGProblem {

    private static final long serialVersionUID = 3578276570298772239L;
    
    public WFG3() { }

    @Override
    protected void initialize() {
        clear();
        final int k = 2 * (this.m - 1);
        for (int i = 0; i < this.m; ++i) {
            final int index = i;
            ContinuousFunction function = new ContinuousFunction() {

                @Override
                public Double f(Vector input) {
                    Vector y = Problems.WFG3(input, k, m);
                    return y.doubleValueOf(index);
                }
            };
            FunctionOptimisationProblem wfg3_fm = new FunctionOptimisationProblem();
            wfg3_fm.setFunction(function);
            List<String> domain = Lists.newArrayList();
            for (int j = 0; j < k + this.l; ++j) {
                domain.add("R(0:" + 2 * (j + 1) + ")");
            }
            wfg3_fm.setDomain(Joiner.on(", ").join(domain));
            add(wfg3_fm);
        }
    }

    public WFG3(WFG3 copy) {
        super(copy);
    }

    @Override
    public WFG3 getClone() {
        return new WFG3(this);
    }
}
