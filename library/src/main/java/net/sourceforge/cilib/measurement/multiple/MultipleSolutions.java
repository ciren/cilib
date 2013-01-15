/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.multiple;


import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;
import net.sourceforge.cilib.type.types.container.TypeList;

/**
 *
 * @deprecated This class is no longer valid. A combination of the
 *             {@link CompositeMeasurement} and {@link net.sourceforge.cilib.measurement.single.Solution}
 *             should be used instead
 */
@Deprecated
public class MultipleSolutions implements Measurement<TypeList> {
    private static final long serialVersionUID = 1617755270627315980L;

    @Override
    public MultipleSolutions getClone() {
        return this;
    }

    @Override
    public TypeList getValue(Algorithm algorithm) {
        TypeList tl = new TypeList();

        for (OptimisationSolution solution : algorithm.getSolutions()) {
            tl.add(solution.getPosition());
        }

        return tl;
    }

}
