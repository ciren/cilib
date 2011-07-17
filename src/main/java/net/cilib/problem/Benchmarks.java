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
import fj.Ord;
import fj.P2;
import fj.data.List;
import fj.function.Doubles;

/**
 * @author gpampara
 */
public final class Benchmarks {

    private Benchmarks() {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Base helper functions
     */
    public static final F<Double, Double> pi = new F<Double, Double>() {
        @Override
        public Double f(Double a) {
            return a * Math.PI;
        }
    };
    
    public static final F<Integer, Integer> succ = new F<Integer, Integer>() {
        @Override
        public Integer f(Integer a) {
            return a + 1;
        }
    };
    
    public static final F<Integer, Integer> pred = new F<Integer, Integer>() {
        @Override
        public Integer f(Integer a) {
            return a - 1;
        }
    };
    
    /**
     * Lifted identity function.
     */
    public static final F<Double, Double> identity = new F<Double, Double>() {
        @Override
        public Double f(Double a) {
            return a;
        }
    };
    
    public static final F2<Double, Double, Double> pow = new F2<Double, Double, Double>() {
        @Override
        public Double f(Double a, Double b) {
            return Math.pow(a, b);
        }
    };
    
    public static final F<Double, Double> abs = new F<Double, Double>() {
        @Override
        public Double f(Double a) {
            return Math.abs(a);
        }
    };
    
    public static final F<Double, Double> square = new F<Double, Double>() {
        @Override
        public Double f(Double a) {
            return a * a;
        }
    };
    
    public static final F<Double, Double> sqrt = new F<Double, Double>() {
        @Override
        public Double f(Double a) {
            return Math.sqrt(a);
        }
    };
    
    public static final F<Double, Double> cos = new F<Double, Double>() {
        @Override
        public Double f(Double a) {
            return Math.cos(a);
        }
    };
    
    public static final F<Double, Double> sin = new F<Double, Double>() {
        @Override
        public Double f(Double a) {
            return Math.sin(a);
        }
    };
    
    public static final F<Double, Double> ceil = new F<Double, Double>() {
        @Override
        public Double f(Double a) {
            return Math.ceil(a);
        }
    };
    
    public static final F<Double, Double> floor = new F<Double, Double>() {
        @Override
        public Double f(Double a) {
            return Math.floor(a);
        }
    };
    
    /**
     * 1-Dimensional functions
     * More complex functions - methods or instances?
     */
    public static final F<Double, Double> alpine = new F<Double, Double>() {
        @Override
        public Double f(Double a) {
            return abs.f(a * (sin.f(a) + 0.1));
        }
    };
    
    public static final F<Double, Double> quartic = square.o(square);
    
    public static final F<Double, Double> spherical = square;
    
    public static final F<Double, Double> absoluteValue = abs;
    
    public static final F<Double, Double> rastrigin = new F<Double, Double>() {
        @Override
        public Double f(Double a) {
            return square.f(a) - 10.0 * cos.o(pi).f(2*a) + 10;
        }
    };
    
    public static final F<Double, Double> step = new F<Double, Double>() {
        @Override
        public Double f(Double a) {
            return square.o(floor).f(a + 0.5);
        }
    };
    
    /**
     * 2-Dimensional functions
     */
    //Not generalized
    public static final F2<Double, Double, Double> beale = new F2<Double, Double, Double>() {
        @Override
        public Double f(Double a, Double b) {
            return square.f(1.5 - a + a * b) + square.f(2.25 - a + a * square.f(b)) + square.f(2.625 - a + a * pow.f(b, 3.0));
        }
    };
    
    //Not generalized
    public static final F2<Double, Double, Double> bird = new F2<Double, Double, Double>() {
        @Override
        public Double f(Double a, Double b) {
            return sin.f(a) * Math.exp(square.f(1 - cos.f(b))) + cos.f(b) * Math.exp(square.f(1 - sin.f(a))) + square.f(a - b);
        }
    };
    
    //Not generalized
    public static final F2<Double, Double, Double> bohachevsky1 = new F2<Double, Double, Double>() {
        @Override
        public Double f(Double a, Double b) {
            return square.f(a) + 2 * square.f(b) - 0.3 * cos.o(pi).f(3 * a) - 0.4 * cos.o(pi).f(4 * b) + 0.7;
        }
    };
    
    //Not generalized
    public static final F2<Double, Double, Double> bohachevsky2 = new F2<Double, Double, Double>() {
        @Override
        public Double f(Double a, Double b) {
            return square.f(a) + 2 * square.f(b) - 0.3 * cos.o(pi).f(3 * a) * cos.o(pi).f(4 * b) + 0.3;
        }
    };
    
    //Not generalized
    public static final F2<Double, Double, Double> bohachevsky3 = new F2<Double, Double, Double>() {
        @Override
        public Double f(Double a, Double b) {
            return square.f(a) + 2 * square.f(b) - 0.3 * cos.f(pi.f(3 * a) + pi.f(4 * b)) + 0.3;
        }
    };
    
    //Not generalized
    public static final F2<Double, Double, Double> booth = new F2<Double, Double, Double>() {
        @Override
        public Double f(Double a, Double b) {
            return square.f(a + 2 * b - 7) + square.f(2 * a + b - 5);
        }
    };

    //Not generalized
    public static F2<Double, Double, Double> branin(final double a, final double b, final double c,
                                                    final double d, final double e, final double f) {
        return new F2<Double, Double, Double>() {
            @Override
            public Double f(Double x, Double y) {
                return a * square.f(y - b * a * a + c * a - d) + e * (1 - f) * cos.f(a) + e;
            }
        };
    }
    
    public static final F2<Double, Double, Double> branin = branin(1.0, 5.1 / square.o(pi).f(2.0), 5.0 / pi.f(1.0),
                                                                   6.0, 10.0, 1.0 / pi.f(8.0));
    
    //Not generalized
    public static final F2<Double, Double, Double> bukin4 = new F2<Double, Double, Double>() {
        @Override
        public Double f(Double a, Double b) {
            return 100 * square.f(b) + 0.01 * abs.f(a + 10);
        }
    };
    
    //Not generalized
    public static final F2<Double, Double, Double> bukin6 = new F2<Double, Double, Double>() {
        @Override
        public Double f(Double a, Double b) {
            return 100 * sqrt.o(abs).f(b - 0.01 * square.f(a)) + 0.01 * abs.f(a + 10);
        }
    };
    
    //Not generalized
    public static final F2<Double, Double, Double> damavandi = new F2<Double, Double, Double>() {
        @Override
        public Double f(Double a, Double b) {
            return (1 - pow.f(abs.f(sin.o(pi).f(a - 2) * sin.o(pi).f(b - 2) 
                              / square.o(pi).f(a - 2)), 5.0))
                    * (2 + square.f(a - 7) + 2 * square.f(b - 7));
        }
    };
    
    //Not generalized
    public static final F2<Double, Double, Double> easom = new F2<Double, Double, Double>() {
        @Override
        public Double f(Double a, Double b) {
            return - cos.f(a) * cos.f(b) * Math.exp(-square.f(a - pi.f(1.0)) - square.f(b - pi.f(1.0)));
        }
    };
    
    //Generalized
    public static final F2<Double, Double, Double> eggHolder = new F2<Double, Double, Double>() {
        @Override
        public Double f(Double a, Double b) {
            return -(b + 47) * sin.o(sqrt).o(abs).f(b + a/2 + 47)
                    + -a * sin.o(sqrt).o(abs).f(a - b + 47);
        }
    };
    
    //Not generalized
    public static final F2<Double, Double, Double> goldsteinPrice = new F2<Double, Double, Double>() {
        @Override
        public Double f(Double a, Double b) {
            return (1 + square.f(a + b + 1.0) * (19.0 - 14.0*a + 3*square.f(a) - 14*b + 6*a*b + 3*square.f(b)))
                    * (30 + square.f(2*a - 3*b) * (18 - 32*a + 12*square.f(a) + 48*b - 36*a*b + 27*square.f(b)));
        }
    };
    
    //Not generalized
    public static final F2<Double, Double, Double> himmelblau = new F2<Double, Double, Double>() {
        @Override
        public Double f(Double a, Double b) {
            return square.f(square.f(a) + b - 11) + square.f(a + square.f(b) - 7);
        }
    };
    
    //Generalized
    public static final F2<Double, Double, Double> modifiedSchaffer2 = new F2<Double, Double, Double>() {
        @Override
        public Double f(Double a, Double b) {
            return 0.5 + (square.o(sin).f(square.f(a) - square.f(b)) - 0.5) / square.f(1 + 0.001 * (square.f(a) + square.f(b)));
        }
    };
    
    //Generalized
    public static final F2<Double, Double, Double> modifiedSchaffer3 = new F2<Double, Double, Double>() {
        @Override
        public Double f(Double a, Double b) {
            return 0.5 + (square.o(sin).o(cos).o(abs).f(square.f(a) - square.f(b)) - 0.5) / square.f(1 + 0.001 * (square.f(a) + square.f(b)));
        }
    };
    
    //Generalized
    public static final F2<Double, Double, Double> modifiedSchaffer4 = new F2<Double, Double, Double>() {
        @Override
        public Double f(Double a, Double b) {
            return 0.5 + (square.o(cos).o(sin).o(abs).f(square.f(a) - square.f(b)) - 0.5) / square.f(1 + 0.001 * (square.f(a) + square.f(b)));
        }
    };
    
    //Generalized
    public static final F2<Double, Double, Double> rosenbrock = new F2<Double, Double, Double>() {
        @Override
        public Double f(Double a, Double b) {
            return 100 * square.f(b - square.f(a)) + square.f(a - 1.0);
        }
    };
    
    //Generalized
    public static final F2<Double, Double, Double> schaffer6 = new F2<Double, Double, Double>() {
        @Override
        public Double f(Double a, Double b) {
            return 0.5 + (square.o(sin).o(sqrt).f(100*square.f(a) + square.f(b)) - 0.5) / (1 + 0.001 * square.o(square).f(a-b));
        }
    };
    
    //Not generalized
    public static final F2<Double, Double, Double> sixHumpCamelBack = new F2<Double, Double, Double>() {
        @Override
        public Double f(Double a, Double b) {
            return (4 - 2.1*square.f(a) + pow.f(a, 4.0)/3.0) * square.f(a) + a*b + 4*(square.f(b) - 1)*square.f(b);
        }
    };
    
    /**
     * 4-Dimensional functions
     */
    //Not generalized
    public static final F4<Double, Double, Double, Double, Double> colville = new F4<Double, Double, Double, Double, Double>() {
        @Override
        public Double f(Double a, Double b, Double c, Double d) {
            return 100 * square.f(b - square.f(a)) + square.f(1 - a)
                    + 90 * square.f(d - square.f(c)) + square.f(1 - c)
                    + 10.1 * (square.f(c - 1) + square.f(d - 1))
                    + 19.8 * (b - 1) * (d - 1);
        }
    };
    
    /**
     * List functions
     */
    public static final F<List<Double>, Double> ackley = new F<List<Double>, Double>() {
        @Override
        public Double f(final List<Double> a) {
            final int size = a.length();
            return -20.0 * Math.exp(-0.2 * sqrt.f(Doubles.sum(a.map(square)) / size))
                    - Math.exp(Doubles.sum(a.map(cos.o(Doubles.multiply.f(2.0).o(pi)))) / size)
                    + 20 + Math.E;
        }
    };
    
    //TODO: de jong f4 is supposed to be quartic with noise but quartic is supposed to
    //have the index in it
    public static final F<List<Double>, Double> dejongf4 = new F<List<Double>, Double>() {
        @Override
        public Double f(final List<Double> a) {
            return Doubles.sum(a.zipIndex().map(new F<P2<Double, Integer>, Double>() {
                @Override
                public Double f(P2<Double, Integer> a) {
                    return quartic.f(a._1()) * succ.f(a._2());
                }
            }));
        }
    };
    
    public static F<List<Double>, Double> elliptic(final Double conditionNumber) {
        return new F<List<Double>, Double>() {
            @Override
            public Double f(final List<Double> a) {
                final int size = a.length();
                return Doubles.sum(a.zipIndex().map(new F<P2<Double, Integer>, Double>() {
                    @Override
                    public Double f(P2<Double, Integer> a) {
                        return square.f(a._1()) * pow.f(conditionNumber, a._2().doubleValue() / pred.f(size));
                    }
                }));
            };
        };
    }
    
    public static final F<List<Double>, Double> elliptic = elliptic(1000000.0);
    
    /**
     * Generalized Griewank function. Although the function is "generalized" the
     * resulting implementation is definitely not "general". It is very specific.
     */
    public static final F<List<Double>, Double> griewank = new F<List<Double>, Double>() {
        @Override
        public Double f(final List<Double> a) {
            return 1 + Doubles.sum(a.map(square)) * (1.0 / 4000)
                    - Doubles.product(a.zipIndex().map(new F<P2<Double, Integer>, Double>() {
                        @Override
                        public Double f(P2<Double, Integer> a) {
                            return cos.f(a._1() / sqrt.f(succ.f(a._2()).doubleValue()));
                        }
            }));
        }
    };
    
    public static final F<List<Double>, Double> hyperEllipsoid = new F<List<Double>, Double>() {
        @Override
        public Double f(final List<Double> a) {
            return Doubles.sum(a.zipIndex().map(new F<P2<Double, Integer>, Double>() {
                    @Override
                    public Double f(P2<Double, Integer> a) {
                        return square.f(a._1()) * succ.f(a._2());
                    }
            }));
        }
    };
    
    public static F<List<Double>, Double> michalewicz(final int m) {
        return new F<List<Double>, Double>() {
            @Override
            public Double f(final List<Double> a) {
                return Doubles.sum(a.zipIndex().map(new F<P2<Double, Integer>, Double>() {
                        @Override
                        public Double f(P2<Double, Integer> a) {
                            return - sin.f(a._1()) * pow.f(sin.f(succ.f(a._2()) * square.f(a._1()) / pi.f(1.0)), 2.0*m);
                        }
                }));
            }
        };
    }
    
    public static final F<List<Double>, Double> michalewicz = michalewicz(10);
    
    //TODO: there may be a simpler way, its here cos of the product
    public static final F<List<Double>, Double> norwegian = new F<List<Double>, Double>() {
            @Override
            public Double f(final List<Double> a) {
                return Doubles.product(a.map(new F<Double, Double>() {
                    @Override
                    public Double f(Double b) {
                        return cos.o(pi).f(pow.f(b, 3.0)) * (99 + b) / 100;
                    }
                    
                }));
            }
    };

    //This is Schwefel 1.2
    //TODO: check if constrained or not cos Schwefel 1.2 was under unconstrained
    public static final F<List<Double>, Double> quadric = new F<List<Double>, Double>() {
            @Override
            public Double f(final List<Double> a) {
                return Doubles.sum(a.reverse().tails().map(
                        square.o(new F<List<Double>, Double>(){
                            @Override
                            public Double f(List<Double> a) {
                                return Doubles.sum(a);
                            }
                        })
                ));
            }
    };
    
    public static final F<List<Double>, Double> salomon = new F<List<Double>, Double>() {
        @Override
        public Double f(final List<Double> a) {
            return -cos.o(pi).f(2 * sqrt.f(Doubles.sum(a.map(square)))) + 0.1 * sqrt.f(Doubles.sum(a.map(square))) + 1;
        }
    };
    
    public static final F<List<Double>, Double> schwefel2_21 = new F<List<Double>, Double>() {
        @Override
        public Double f(final List<Double> a) {
            return a.foldLeft(Ord.doubleOrd.max, Double.MIN_VALUE);
        }
    };
    
    public static final F<List<Double>, Double> schwefel2_22 = new F<List<Double>, Double>() {
        @Override
        public Double f(final List<Double> a) {
            return Doubles.sum(a.map(abs)) + Doubles.product(a.map(abs));
        }
    };
    
    //TODO check this, there may be a simpler way
    public static final F<List<Double>, Double> zakharov = new F<List<Double>, Double>() {
        @Override
        public Double f(final List<Double> a) {
            Double tmpSum = Doubles.sum(a.zipIndex().map(new F<P2<Double, Integer>, Double>() {
                @Override
                public Double f(P2<Double, Integer> b) {
                    return 0.5 * succ.f(b._2()) * b._1();
                }
            }));
            return Doubles.sum(a.map(square)) + square.f(tmpSum) + square.o(square).f(tmpSum);
        }
    };
}
