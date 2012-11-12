/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.functions;

import fj.F;
import fj.F2;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;

public final class Algorithms {
    public static <A extends Algorithm> F<A, OptimisationSolution> getBestSolution() {
        return new F<A, OptimisationSolution>() {
            @Override
            public OptimisationSolution f(A a) {
                return a.getBestSolution();
            }
        };
    }

    public static <A extends Algorithm> F<A, Iterable<OptimisationSolution>> getSolutions() {
        return new F<A, Iterable<OptimisationSolution>>() {
            @Override
            public Iterable<OptimisationSolution> f(A a) {
                return a.getSolutions();
            }
        };
    }

    public static <A extends Algorithm> F<A, A> performIteration() {
        return new F<A, A>() {
            @Override
            public A f(A a) {
                a.performIteration();
                return a;
            }
        };
    }

    public static <A extends Algorithm> F<A, A> iterateUnlessDone() {
        return new F<A, A>() {
            @Override
            public A f(A a) {
                if (!a.isFinished()) {
                    a.performIteration();
                }

                return a;
            }
        };
    }

    public static <A extends Algorithm> F<A, A> iterateUntilDone() {
        return new F<A, A>() {
            @Override
            public A f(A a) {
                while (!a.isFinished()) {
                    a.performIteration();
                }

                return a;
            }
        };
    }

    public static <A extends Algorithm> F<A, A> performInitialisation() {
        return new F<A, A>() {
            @Override
            public A f(A a) {
                a.performInitialisation();
                return a;
            }
        };
    }

    public static <A extends Algorithm> F<A, Integer> getIterations() {
        return new F<A, Integer>() {
            @Override
            public Integer f(A a) {
                return a.getIterations();
            }
        };
    }

    public static <A extends Algorithm> F2<Problem, A, A> setOptimisationProblem() {
        return new F2<Problem, A, A>() {
            @Override
            public A f(Problem a, A b) {
                b.setOptimisationProblem(a);
                return b;
            }
        };
    }

    public static <A extends Algorithm> F<A, Problem> getOptimisationProblem() {
        return new F<A, Problem>() {
            @Override
            public Problem f(A a) {
                return a.getOptimisationProblem();
            }
        };
    }

    public static <A extends Algorithm> F<A, Boolean> isFinished() {
        return new F<A, Boolean>() {
            @Override
            public Boolean f(A a) {
                return a.isFinished();
            }
        };
    }

    public static <A extends Algorithm> F<A, A> run() {
        return new F<A, A>() {
            @Override
            public A f(A a) {
                ((AbstractAlgorithm) a).run();
                return a;
            }
        };
    }

    public static <A extends Algorithm> F<A, A> initialise() {
        return new F<A, A>() {
            @Override
            public A f(A a) {
                ((AbstractAlgorithm) a).performInitialisation();
                return a;
            }
        };
    }

    public static <A extends PopulationBasedAlgorithm> F<A, Topology<? extends Entity>> getTopology() {
        return new F<A, Topology<? extends Entity>>() {
            @Override
            public Topology<? extends Entity> f(A a) {
                return a.getTopology();
            }
        };
    }

    public static <A extends SinglePopulationBasedAlgorithm> F2<Topology<? extends Entity>, ? extends A, A> setTopology() {
        return new F2<Topology<? extends Entity>, A, A>() {
            @Override
            public A f(Topology<? extends Entity> a, A b) {
                b.setTopology(a);
                return b;
            }
        };
    }
}
