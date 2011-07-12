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
package net.cilib.predef;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import fj.P1;
import fj.data.List;
import junit.framework.Assert;
import org.junit.Test;

import static net.cilib.predef.Predef.*;

/**
 *
 */
public class PredefTest {

    @Test
    public void addition() {
        List<Double> solution = solution(1.0, 3.0);
        List<Double> result = plus(solution, solution); // z = x + y

        Assert.assertTrue(Iterables.elementsEqual(result, Lists.newArrayList(2.0, 6.0)));
    }

    @Test
    public void subtraction() {
        List<Double> solution = solution(1.0, 3.0);
        List<Double> result = subtract(solution, solution); // z = x - y

        Assert.assertTrue(Iterables.elementsEqual(result, Lists.newArrayList(0.0, 0.0)));
    }

    @Test
    public void multiplication() {
        List<Double> solution = solution(1.0, 3.0);
        List<Double> result = multiply(2.0, solution); // z = x * y

        Assert.assertTrue(Iterables.elementsEqual(Lists.newArrayList(2.0, 6.0), result));
    }

    @Test
    public void multiplyBind() {
        List<Double> solution = solution(1.0, 3.0, 5.0);
        Assert.assertTrue(Iterables.elementsEqual(Lists.newArrayList(2.0, 6.0, 10.0), multiply(2.0, solution)));
    }

    @Test
    public void multiplySupplier() {
        List<Double> solution = solution(1.0, 3.0, 5.0);
        P1<Double> supplier = new P1<Double>() {
            private double value = 1.0;

            @Override
            public Double _1() {
                value *= 2.0;
                return value;
            }
        };

        List<Double> result = multiply(supplier, solution);
        Assert.assertTrue(Iterables.elementsEqual(Lists.newArrayList(2.0, 12.0, 40.0), result));
    }

    @Test(expected = ArithmeticException.class)
    public void illegalDivide() {
        List<Double> solution = solution(1.0);
        divide(0.0, solution);
    }

    @Test
    public void division() {
        List<Double> solution = solution(1.0, 3.0);
        List<Double> result = divide(1.0, solution); // z = x / y

        Assert.assertTrue(Iterables.elementsEqual(Lists.newArrayList(1.0, 3.0), result));
    }

    @Test
    public void complexFunctionalOperation() {
        List<Double> solution = solution(1.0, 2.0);
        List<Double> result = plus(multiply(4.0, solution), solution);

        Assert.assertTrue(Iterables.elementsEqual(Lists.newArrayList(5.0, 10.0), result));
    }

//    private Stream<Double> randomStream(final F<Double, Double> twister) {
//        return Stream.cons(twister.f(0.0), new P1<Stream<Double>>() {
//                    public Stream<Double> _1() {
//                        return randomStream(twister);
//                    }
//                });
//    }
//
//    @Test
//    public void eitherTest() {
//        Stream<Double> s = randomStream(new F<Double, Double>() {
//            private final MersenneTwister t = new MersenneTwister();
//            @Override
//            public Double f(Double aDouble) {
//                return t.nextDouble();
//            }
//        });
//
//        for (Stream<Double> bs = s; bs.isNotEmpty(); bs = bs.tail()._1()) {
//            System.out.println(bs.head());
//        }
//    }

//    private static final long M_BIG = 0xffffffffL;
//    private static final long M_SEED = 161803398;
//
//    public P1<Stream<Double>> seed(long seed) {
//        final long[] buffer = new long[56];
//
//        if (seed == 0) {
//            seed = 1;
//        }
//
//        long j = (M_SEED - seed) % M_BIG;
//
//        buffer[0] = 0;
//        buffer[55] = j;
//
//        long k = 1;
//        for (int i = 1; i < 55; ++i) {
//            int n = (21 * i) % 55;
//            buffer[n] = k;
//            k = j - k;
//            if (k < 0) {
//                k += M_BIG;
//            }
//            j = buffer[n];
//        }
//
//        for (int i1 = 0; i1 < 4; ++i1) {
//            for (int i = 1; i < 56; ++i) {
//                long t = buffer[i] - buffer[1 + (i + 30) % 55];
//                if (t < 0) {
//                    t += M_BIG;
//                }
//                buffer[i] = t;
//            }
//        }
//
//        return new P1<Stream<Double>>() {
//            @Override
//            public Stream<Double> _1() {
//                return internal(0, 31, buffer);
//            }
//        };
//    }
//
//    private Stream<Double> internal(int x, int y, long[] buffer) {
//        ++x;
//        if (x == 56) {
//            x = 1;
//        }
//        ++y;
//        if (y == 56) {
//            y = 1;
//        }
//
//        long j = buffer[x] - buffer[y];
//        if (j < 0) {
//            j += M_BIG;
//        }
//        buffer[x] = j;
//
//        return (int) ((j & 0xffffffffL) >>> (32 - bits));
//    }
//
//    public void next() {
//
//    }
//
//    public P1<Stream<Double>> knuth(long seed) {
//        return seed(seed);
//    }
//
//    public Stream<Double> knuthSub() {
//        throw new UnsupportedOperationException();
//    }


//    /**
//     * for { i <- 1
//     *       j <- if (i == 1) 2 else 3 }
//     *   yield j
//     */
//    @Test
//    public void understandBind() {
//        Option<Integer> i = Option.some(1);
////        Option<Integer> i = Option.none();
//
//        Option<Integer> result = i.bind(new F<Integer, Option<Integer>>() {
//            @Override
//            public Option<Integer> f(Integer integer) {
//                System.out.println("inside here");
//                return integer == 1 ? Option.some(2) : Option.some(3);
//            }
//        });
//
//        Show.optionShow(Show.intShow).print(i);
//
//        Assert.assertTrue(result.isSome());
//        Assert.assertEquals(result.some(), Integer.valueOf(2));
//    }
//
//    interface Pair<A, B> {
//    A first();
//    B second();
//  }
//
//  static class Pairs {
//    public static <A, B> Pair<A, B> pair(final A a, final B b) {
//      return new Pair<A, B>() {
//        public A first() { return a; }
//        public B second() { return b; }
//        public String toString() {
//          return "(" + a + ":" + a.getClass().getName() + ", " + b + ":" + b.getClass().getName() + ")";
//        }
//      };
//    }
//  }
//
//  interface State<S, A> {
//    Pair<S, A> run(S s);
//  }
//
//  interface Function<X, Y> {
//    Y apply(X x);
//  }
//
//  static class States {
//
//    // State<S, _> is a covariant functor
//    public static <S, A, B> State<S, B> map(final State<S, A> s, final Function<A, B> f) {
//      return new State<S, B>() {
//        public Pair<S, B> run(final S k) {
//          final Pair<S, A> p = s.run(k);
//          return Pairs.pair(p.first(), f.apply(p.second()));
//        }
//      };
//    }
//
//    // // State<S, _> is a monad
//    public static <S, A, B> State<S, B> bind(final State<S, A> s, final Function<A, State<S, B>> f) {
//      return new State<S, B>() {
//        public Pair<S, B> run(final S k) {
//          final Pair<S, A> p = s.run(k);
//          return f.apply(p.second()).run(p.first());
//        }
//      };
//    }
//  }
//
//    @Test
//  public void states() {
//    State<Double, String> state0 = new State<Double, String>() {
//      public Pair<Double, String> run(Double s) {
//        return Pairs.pair(s, "9");
//      }
//    };
//    System.out.println("state0 = " + state0.run(1.0));
//
//    State<Double, Integer> state1 = States.map(state0, new Function<String, Integer>() {
//      public Integer apply(String x) {
//        return Integer.valueOf(x) - 5;
//      }
//    });
//    System.out.println("state1 = " + state1.run(1.0));
//
//    State<Double, String> state2 = States.bind(state1, new Function<Integer, State<Double, String>>() {
//      public State<Double, String> apply(final Integer a) {
//        return new State<Double, String>() {
//          public Pair<Double, String> run(Double s) {
//            return Pairs.pair(s + 0.1, "'" + a.toString() + "'");
//          }
//        };
//      }
//    });
//    System.out.println("state2 = " + state2.run(1.0));
//  }
}
