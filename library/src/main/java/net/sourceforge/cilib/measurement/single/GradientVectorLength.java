/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single;

import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.functions.Gradient;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;
import net.sourceforge.cilib.type.types.container.Vector;
/**
 *
 * @author florent
 */
public class GradientVectorLength implements Measurement<Real> {
    
    @Override
    public GradientVectorLength getClone() {
        return this;
    }
    
    @Override
    public Real getValue(Algorithm algorithm) {
       OptimisationSolution best = algorithm.getBestSolution();
        Problem d = algorithm.getOptimisationProblem();
        FunctionOptimisationProblem fop = (FunctionOptimisationProblem)d;
        Gradient df = (Gradient)fop.getFunction();
        
        return Real.valueOf(df.getGradientVectorLength((Vector)best.getPosition()));
    }
}
