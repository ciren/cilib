/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.moo.wfg;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import java.util.List;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.problem.MOOptimisationProblem;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 */
public class WFG5 extends MOOptimisationProblem {

    private static final long serialVersionUID = -7464349678257326248L;

    private static final int M = 3;
    private static final int k = 2 * (M - 1);
    private static final int l = 20;

    public WFG5() {

        for (int i = 0; i < M; ++i) {
            final int index = i;
            ContinuousFunction function = new ContinuousFunction() {

                @Override
                public Double apply(Vector input) {
                    Vector y = Problems.WFG5(input, k, M);
                    return y.doubleValueOf(index);
                }
            };
            FunctionOptimisationProblem wfg5_fm = new FunctionOptimisationProblem();
            wfg5_fm.setFunction(function);
            List<String> domain = Lists.newArrayList();
            for (int j = 0; j < k + l; ++j) {
                domain.add("R(0:" + 2 * (j + 1) + ")");
            }
            wfg5_fm.setDomain(Joiner.on(", ").join(domain));
            add(wfg5_fm);
        }

    }

    public WFG5(WFG5 copy) {
        super(copy);
    }

    @Override
    public WFG5 getClone() {
        return new WFG5(this);
    }
}
