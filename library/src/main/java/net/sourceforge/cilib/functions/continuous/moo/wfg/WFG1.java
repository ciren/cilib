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
public class WFG1 extends WFGProblem {

    private static final long serialVersionUID = -1933790309464225734L;

    public WFG1() { }
    
    @Override
    protected void initialize() {
        clear();
        final int k = 2 * (this.m - 1);
        for (int i = 0; i < this.m; ++i) {
            final int index = i;
            ContinuousFunction function = new ContinuousFunction() {

                @Override
                public Double f(Vector input) {
                    Vector y = Problems.WFG1(input, k, m);
                    return y.doubleValueOf(index);
                }
            };
            FunctionOptimisationProblem wfg1_fm = new FunctionOptimisationProblem();
            wfg1_fm.setFunction(function);
            List<String> domain = Lists.newArrayList();
            for (int j = 0; j < k + this.l; ++j) {
                domain.add("R(0:" + 2 * (j + 1) + ")");
            }
            wfg1_fm.setDomain(Joiner.on(", ").join(domain));
            add(wfg1_fm);
        }
    }

    public WFG1(WFG1 copy) {
        super(copy);
    }

    @Override
    public WFG1 getClone() {
        return new WFG1(this);
    }
}
