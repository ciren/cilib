/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.tuning;

import static fj.Function.flip;
import fj.*;
import fj.data.List;
import static fj.data.List.*;
import static fj.data.List.iterableList;
import static fj.function.Doubles.add;
import static fj.Ord.*;
import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.math.Stats;
import net.sourceforge.cilib.math.StatsTests;
import net.sourceforge.cilib.problem.objective.Maximise;
import net.sourceforge.cilib.problem.objective.Minimise;
import net.sourceforge.cilib.problem.objective.Objective;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;
import net.sourceforge.cilib.type.types.container.Vector;
import static net.sourceforge.cilib.util.functions.Fitnesses.getValue;
import static net.sourceforge.cilib.util.functions.Solutions.getFitness;
import net.sourceforge.cilib.util.functions.Utils;

public class FRaceIterationStrategy extends AbstractIterationStrategy<TuningAlgorithm> {
    
    private List<List<OptimisationSolution>> results;
    private ControlParameter minProblems;
    private ControlParameter minSolutions;
    
    public FRaceIterationStrategy() {
        this.minProblems = ConstantControlParameter.of(4.0);
        this.minSolutions = ConstantControlParameter.of(2.0);
        this.results = List.<List<OptimisationSolution>>nil();
    }
    
    public FRaceIterationStrategy(FRaceIterationStrategy copy) {
        this.minProblems = copy.minProblems.getClone();
        this.minSolutions = copy.minSolutions.getClone();
        this.results = iterableList(copy.results);
    }

    @Override
    public FRaceIterationStrategy getClone() {
        return new FRaceIterationStrategy(this);
    }

    @Override
    public void performIteration(final TuningAlgorithm alg) {
        final List<Vector> parameterList = alg.getParameterList();
        final TuningProblem tuningProblem = (TuningProblem) alg.getOptimisationProblem();
        
        //TODO: deal with maximisation problems
        results = results.snoc(parameterList.map(new F<Vector,OptimisationSolution>() {
            @Override
            public OptimisationSolution f(Vector a) {
                return new OptimisationSolution(a, alg.evaluate(a));
            }
        }));
        
        // (+1 because iterations start at 0)
        if (alg.getIterations() + 1 >= minProblems.getParameter() && parameterList.length() > 1) {
            List<List<Double>> data = results
                .map(List.<OptimisationSolution,Double>map_().f(getFitness()
                    .andThen(getValue())
                    .andThen(negateIfMaximising(tuningProblem.getObjective()))));
            P2<Double, Double> friedman = StatsTests.friedman(0.05, data);

            if (friedman._1() > friedman._2()) {
                final List<Integer> indexes = StatsTests.postHoc(0.05, friedman._1(), data);
                
                if (indexes.isNotEmpty() && indexes.length() >= minSolutions.getParameter()) {
                    alg.setParameterList(indexes.map(flip(Utils.<Vector>index()).f(parameterList)));

                    results = results.map(new F<List<OptimisationSolution>,List<OptimisationSolution>>() {
                        @Override
                        public List<OptimisationSolution> f(final List<OptimisationSolution> a) {
                            return indexes.map(flip(Utils.<OptimisationSolution>index()).f(a));
                        }
                    });
                } else {
                    final List<List<Double>> ranks = iterableList(data)
                        .map(Stats.rank.andThen(Utils.<Double,Iterable>iterableList()));
                    final List<Integer> newIndexes = ranks.foldLeft(Utils.<Double>pairwise(add), replicate(data.head().length(), 0.0))
                        .zipIndex()
                        .sort(p2Ord(doubleOrd, intOrd))
                        .take((int)minSolutions.getParameter())
                        .map(P2.<Double,Integer>__2());
                    
                    alg.setParameterList(newIndexes.map(flip(Utils.<Vector>index()).f(parameterList)));
                    
                    results = results.map(new F<List<OptimisationSolution>,List<OptimisationSolution>>() {
                        @Override
                        public List<OptimisationSolution> f(final List<OptimisationSolution> a) {
                            return newIndexes.map(flip(Utils.<OptimisationSolution>index()).f(a));
                        }
                    });
                }   
            }
        }
        
        List<List<Double>> data = results
            .map(List.<OptimisationSolution,Double>map_().f(getFitness()
                .andThen(getValue())
                .andThen(negateIfMaximising(tuningProblem.getObjective()))));
        final List<List<Double>> ranks = iterableList(data)
            .map(Stats.rank.andThen(Utils.<Double,Iterable>iterableList()));
        final List<Integer> indexes = ranks.foldLeft(Utils.<Double>pairwise(add), replicate(data.head().length(), 0.0))
            .zipIndex()
            .sort(p2Ord(doubleOrd, intOrd))
            .map(P2.<Double,Integer>__2());
        alg.setParameterList(indexes.map(flip(Utils.<Vector>index()).f(alg.getParameterList())));
        results = results.map(new F<List<OptimisationSolution>,List<OptimisationSolution>>() {
            @Override
            public List<OptimisationSolution> f(final List<OptimisationSolution> a) {
                return indexes.map(flip(Utils.<OptimisationSolution>index()).f(a));
            }
        });
    }
    
    public static F<Double, Double> negateIfMaximising(final Objective obj) {
        return new F<Double, Double>() {
            @Override
            public Double f(Double a) {
                return obj.fold(Function.<Minimise,Double>constant(a), Function.<Maximise,Double>constant(-a));
            }
        };
    }

    public void setMinProblems(ControlParameter r) {
        this.minProblems = r;
    }

    public ControlParameter getMinProblems() {
        return minProblems;
    }
    
    public void setMinSolutions(ControlParameter r) {
        this.minSolutions = r;
    }

    public ControlParameter getMinSolutions() {
        return minSolutions;
    }
    
    public void resetResults() {
        results = List.nil();
    }
}
