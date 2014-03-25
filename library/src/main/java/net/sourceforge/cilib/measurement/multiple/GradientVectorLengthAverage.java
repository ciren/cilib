/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.multiple;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.functions.Gradient;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * @author florent
 */
public class GradientVectorLengthAverage implements Measurement<Real> {

    @Override
    public GradientVectorLengthAverage getClone() {
        return this;
    }

    @Override
    public Real getValue(Algorithm algorithm)
    {
        
        double res = 0.0;
        int i = 0;
        
        MultiPopulationBasedAlgorithm multi;
        if (algorithm instanceof SinglePopulationBasedAlgorithm)
        {
            multi = neighbourhood2populations((SinglePopulationBasedAlgorithm<Entity>) algorithm);
        }
        else
        {
            multi = (MultiPopulationBasedAlgorithm) algorithm;
        }
        for (SinglePopulationBasedAlgorithm single : multi.getPopulations())
        {
            ++i;
            OptimisationSolution best = single.getBestSolution();
            Problem d = single.getOptimisationProblem();
            FunctionOptimisationProblem fop = (FunctionOptimisationProblem) d;
            Gradient df = (Gradient) fop.getFunction();

            res += df.getGradientVectorLength((Vector) best.getPosition());
        }
        return Real.valueOf(res / ((double)i));
    }

private <E extends Entity> MultiPopulationBasedAlgorithm neighbourhood2populations(SinglePopulationBasedAlgorithm<E> s) {
        MultiPopulationBasedAlgorithm m = new MultiPopulationBasedAlgorithm() {
            @Override
        protected void algorithmIteration() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
        public AbstractAlgorithm getClone() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
        public OptimisationSolution getBestSolution() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
        public Iterable<OptimisationSolution> getSolutions() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        for (E e : s.getTopology()) {
            SinglePopulationBasedAlgorithm dummyPopulation = s.getClone();
            dummyPopulation.setTopology(s.getNeighbourhood().f(s.getTopology(), e));
            m.addPopulationBasedAlgorithm(dummyPopulation);
        }
        return m;
    }
}
