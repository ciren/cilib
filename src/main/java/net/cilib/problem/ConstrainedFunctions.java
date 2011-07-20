/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.cilib.problem;

import fj.F;
import fj.F2;
import fj.F4;
import fj.P2;
import fj.data.List;
import fj.function.Doubles;
import static net.cilib.problem.BasicFunctions.*;

/**
 *
 * @author filipe
 */
public class ConstrainedFunctions {

    private ConstrainedFunctions() {
        throw new UnsupportedOperationException();
    }
    private static final double[][] foxholes2dimA = {
        {-32.0, -16.0, 0.0, 16.0, 32.0},
        {-32.0, -16.0, 0.0, 16.0, 32.0},};
    private static final double[][] foxholes4dimA = {
        {4.0, 4.0, 4.0, 4.0},
        {1.0, 1.0, 1.0, 1.0},
        {8.0, 8.0, 8.0, 8.0},
        {6.0, 6.0, 6.0, 6.0},
        {3.0, 7.0, 3.0, 7.0},
        {2.0, 9.0, 2.0, 9.0},
        {5.0, 5.0, 3.0, 3.0},
        {8.0, 1.0, 8.0, 1.0},
        {6.0, 2.0, 6.0, 2.0},
        {7.0, 3.6, 7.0, 3.6},};
    private static final double[] foxholes4dimC = {0.1, 0.2, 0.2, 0.4, 0.4, 0.6, 0.3, 0.7, 0.5, 0.5};
    
    /**
     * 1-Dimensional functions
     */
    //Generalized
    public static final F<Double, Double> continuousStep = new F<Double, Double>() {

        @Override
        public Double f(Double a) {
            return square.f(a + 0.5);
        }
    };

    //Not generalized
    public static F<Double, Double> maximumDeratingFunction1(final double radius, final double alpha) {
        return new F<Double, Double>() {

            @Override
            public Double f(Double a) {
                return a >= radius ? 1.0 : pow.f(a / radius, alpha);
            }
        };
    }
    
    public static final F<Double, Double> maximumDeratingFunction1 = maximumDeratingFunction1(0.25, 2.0);
    
    //Generalized
    public static final F<Double, Double> multimodalFunction1 = new F<Double, Double>() {

        @Override
        public Double f(Double a) {
            return pow.f(sin.o(pi).f(5.0 * a), 6.0);
        }
    };
    
    //Generalized
    public static final F<Double, Double> multimodalFunction2 = new F<Double, Double>() {

        @Override
        public Double f(Double a) {
            return multimodalFunction1.f(a) * Math.exp(-2.0 * Math.log(2) * square.f((a - 0.1) / 0.8));
        }
    };
    
    //Generalized
    public static final F<Double, Double> multimodalFunction3 = new F<Double, Double>() {

        @Override
        public Double f(Double a) {
            return multimodalFunction1.f(pow.f(a, 0.75) - 0.05);
        }
    };
    
    //Generalized
    public static final F<Double, Double> multimodalFunction4 = new F<Double, Double>() {

        @Override
        public Double f(Double a) {
            return multimodalFunction3.f(a) * Math.exp(-2.0 * Math.log(2) * square.f((a - 0.08) / 0.854));
        }
    };
    
    //Generalized
    public static final F<Double, Double> schwefel = new F<Double, Double>() {

        @Override
        public Double f(final Double a) {
            return a * sin.o(sqrt).o(abs).f(a) + 4.18982887272434686131e+02;
        }
    };
    
    //Generalized
    public static final F<Double, Double> schwefelProblem2_26 = new F<Double, Double>() {

        @Override
        public Double f(final Double a) {
            return -a * sin.o(sqrt).o(abs).f(a);
        }
    };
    
    /**
     * 2-Dimensional functions
     */
    //Not generalized
    public static final F2<Double, Double, Double> shekelFoxholes2dim = new F2<Double, Double, Double>() {

        @Override
        public Double f(final Double a, final Double b) {
            return 1.0 / (0.002 + Doubles.sum(List.range(0, 25).map(new F<Integer, Double>() {

                @Override
                public Double f(Integer c) {
                    return 1.0 / (succ.f(c) + pow.f(a - foxholes2dimA[0][c % 5], 6.0) + pow.f(b - foxholes2dimA[1][c / 5], 6.0));
                }
            })));
        }
    };
    
    //Not generalized
    public static final F2<Double, Double, Double> multimodalFunction5 = new F2<Double, Double, Double>() {

        @Override
        public Double f(final Double a, final Double b) {
            return 200 - square.f(square.f(a) + b - 11) - square.f(a + square.f(b) - 7);
        }
    };
    
    /*research this function, not very common/popular
    public static final F2<Double, Double, Double> ripple = new F2<Double, Double, Double>() {

        @Override
        public Double f(final Double a, final Double b) {
            return Math.exp(-1.0 * Math.log(2) * square.f((a - 0.1) / 0.8))
                    * (multimodalFunction1.f(a) + 0.1 * square.o(cos).o(pi).o(Doubles.multiply.f(500.0)).f(a))
                    + Math.exp(-2.0 * Math.log(2) * square.f((b - 0.1) / 0.8))
                    * (sin.o(pi).f(5 * b) + 0.1 * 0.1 * square.o(cos).o(pi).o(Doubles.multiply.f(500.0)).f(b));
        }
    };*/
    
    //Not generalized
    public static final F2<Double, Double, Double> schaffer2 = new F2<Double, Double, Double>() {

        @Override
        public Double f(final Double a, final Double b) {
            return pow.f(square.f(a) + square.f(b), 0.25) * (square.o(Doubles.multiply.f(50.0)).f(pow.f(square.f(a) + square.f(b), 0.1)) + 1);
        }
    };
    
    //Not generalized
    public static final F2<Double, Double, Double> ursemF1 = new F2<Double, Double, Double>() {

        @Override
        public Double f(final Double a, final Double b) {
            return sin.f(2.0 * a - pi.f(0.5)) + 1.0 + 2.0 * cos.f(b) + 0.5 * a;
        }
    };
    
    //Not generalized
    public static final F2<Double, Double, Double> ursemF3 = new F2<Double, Double, Double>() {

        @Override
        public Double f(final Double a, final Double b) {
            return sin.f(pi.f(a * 2.2) + pi.f(0.5)) * ((2.0 - abs.f(b)) / 2.0) * ((3.0 - abs.f(a)) / 2.0)
                    + sin.f(pi.f(0.5 * square.f(b)) + pi.f(0.5)) * ((2.0 - abs.f(b)) / 2.0) * ((2.0 - abs.f(a)) / 2.0);
        }
    };
    
    //Not generalized
    public static final F2<Double, Double, Double> ursemF4 = new F2<Double, Double, Double>() {

        @Override
        public Double f(final Double a, final Double b) {
            return 3.0 * sin.o(pi).f(0.5 * (a + 1)) * (2.0 - sqrt.f(square.f(a) + square.f(b))) / 4.0;
        }
    };

    /**
     * 4-Dimensional functions
     */
    //Not generalized
    public static F4<Double, Double, Double, Double, Double> shekelFoxholes4dim(final int m) {
        return new F4<Double, Double, Double, Double, Double>() {

            @Override
            public Double f(final Double a, final Double b, final Double c, final Double d) {
                return Doubles.sum(List.range(0, m).map(new F<Integer, Double>() {

                    @Override
                    public Double f(Integer e) {
                        return -1.0 / (square.f(a - foxholes4dimA[e][0]) + square.f(b - foxholes4dimA[e][1])
                                + square.f(c - foxholes4dimA[e][2]) + square.f(d - foxholes4dimA[e][3]) + foxholes4dimC[e]);
                    }
                }));
            }
        };
    }
    
    //parameter should be 5, 7 or 10
    public static F4<Double, Double, Double, Double, Double> shekelFoxholes4dim = shekelFoxholes4dim(10);
    
    /**
     * List functions
     */
    public static final F<List<Double>, Double> nastyBenchmark = new F<List<Double>, Double>() {

        @Override
        public Double f(final List<Double> a) {
            return Doubles.sum(a.zipIndex().map(square.o(new F<P2<Double, Integer>, Double>() {

                @Override
                public Double f(P2<Double, Integer> b) {
                    return b._1() - succ.f(b._2());
                }
            })));
        }
    };
    
    //TODO: check this, could be wonky
    public static final F<List<Double>, Double> neumaier3 = new F<List<Double>, Double>() {

        @Override
        public Double f(final List<Double> a) {
            return Doubles.sum(a.map(square.o(Doubles.subtract.f(1.0))))
                    - Doubles.sum(a.zip(a.tail()).map(new F<P2<Double, Double>, Double>() {

                @Override
                public Double f(P2<Double, Double> b) {
                    return b._1() * b._2();
                }
            }));
        }
    };
    
    public static final F<List<Double>, Double> schwefelProblem1_2 = UnconstrainedFunctions.quadric;

    public static F<List<Double>, Double> shir(final double l1, final double l2, final double l3,
            final double l4, final double l5, final double sharpness) {
        return new F<List<Double>, Double>() {

            @Override
            public Double f(final List<Double> a) {
                return Doubles.product(a.map(new F<Double, Double>() {

                    @Override
                    public Double f(final Double b) {
                        return pow.f(sin.f(pi.f(l1 * b) + l2), sharpness)
                                * Math.exp(-l3 * square.f((b - l4) / l5));
                    }
                }));
            }
        };
    }
    
    public static final F<List<Double>, Double> shir = shir(1.0, 1.0, 1.0, 1.0, 1.0, 2.0);
    
    public static final F<List<Double>, Double> shubert = new F<List<Double>, Double>() {

        @Override
        public Double f(final List<Double> a) {
            return Doubles.product(a.map(new F<Double, Double>() {

                @Override
                public Double f(final Double b) {
                    return Doubles.sum(List.range(1, 6).map(new F<Integer, Double>() {

                        @Override
                        public Double f(Integer c) {
                            return c * cos.f(succ.f(c) * b + c);
                        }
                    }));
                }
            }));
        }
    };
}
