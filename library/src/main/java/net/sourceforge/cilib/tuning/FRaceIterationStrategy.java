/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.tuning;

import fj.F;
import static fj.Function.*;
import fj.P2;
import fj.data.List;
import static fj.data.List.*;
import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.math.StatsTests;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;
import net.sourceforge.cilib.type.types.container.Vector;
import static net.sourceforge.cilib.util.functions.Fitnesses.*;
import static net.sourceforge.cilib.util.functions.Solutions.*;
import net.sourceforge.cilib.util.functions.Utils;

public class FRaceIterationStrategy extends AbstractIterationStrategy<TuningAlgorithm> {
    
    private List<List<OptimisationSolution>> results;
    private ControlParameter minProblems;    
    
    public FRaceIterationStrategy() {
        this.minProblems = ConstantControlParameter.of(4.0);
        this.results = List.<List<OptimisationSolution>>nil();
    }
    
    public FRaceIterationStrategy(FRaceIterationStrategy copy) {
        this.minProblems = copy.minProblems.getClone();
        this.results = iterableList(copy.results);
    }

    public FRaceIterationStrategy getClone() {
        return new FRaceIterationStrategy(this);
    }

    public void performIteration(final TuningAlgorithm alg) {
        final List<Vector> parameterList = alg.getParameterList();
        
        //TODO: deal with maximisation problems
        results = results.snoc(parameterList.map(new F<Vector,OptimisationSolution>() {
            @Override
            public OptimisationSolution f(Vector a) {
                return new OptimisationSolution(a, alg.evaluate(a));
            }
        }));
        
        // (+1 because iterations start at 0)
        if (alg.getIterations() + 1 >= minProblems.getParameter() && parameterList.length() != 1) {            
            List<List<Double>> data = results
                .map(List.<OptimisationSolution,Double>map_().f(getFitness().andThen(getValue())));
            P2<Double, Double> friedman = StatsTests.friedman(0.05, data);

            if (friedman._1() > friedman._2()) {
                final List<Integer> indexes = StatsTests.postHoc(0.05, friedman._1(), data);
                alg.setParameterList(indexes.map(flip(Utils.<Vector>index()).f(parameterList)));

                results = results.map(new F<List<OptimisationSolution>,List<OptimisationSolution>>() {
                    @Override
                    public List<OptimisationSolution> f(final List<OptimisationSolution> a) {
                        return indexes.map(flip(Utils.<OptimisationSolution>index()).f(a));
                    }
                });
            }            
        }
    }

    public void setMinProblems(ControlParameter r) {
        this.minProblems = r;
    }

    public ControlParameter getMinProblems() {
        return minProblems;
    }
}
