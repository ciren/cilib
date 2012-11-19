/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.math;

import static fj.Equal.*;
import fj.F;
import fj.Function;
import static fj.Function.*;
import static fj.Ord.*;
import fj.P;
import fj.P2;
import fj.data.List;
import static fj.data.List.*;
import fj.data.Stream;
import static fj.function.Doubles.*;
import fj.function.Integers;
import net.sourceforge.cilib.util.functions.Utils;

public final class Stats {
    
    private Stats() {}
    
    public static Double mean(final Iterable<Double> a) {
        return mean.f(a);
    }
    
    public static Double median(final Iterable<Double> a) {
        return median.f(a);
    }
    
    public static Double variance(final Iterable<Double> a) {
        return variance.f(a);
    }
    
    public static Double stdDev(final Iterable<Double> a) {
        return stdDev.f(a);
    }
    
    public static Iterable<Double> rank(final Iterable<Double> a) {
        return rank.f(a);
    }
    
    public static F<Iterable<Double>, Double> mean =
        new F<Iterable<Double>, Double>() {
            @Override
            public Double f(Iterable<Double> a) {
                List<Double> l = iterableList(a);
                return sum(l) / l.length();
            }
        };
    
    public static F<Iterable<Double>, Double> variance =
        new F<Iterable<Double>, Double>() {
            @Override
            public Double f(Iterable<Double> a) {
                List<Double> l = iterableList(a);
                double m = mean.f(a);
                int len = l.length();
                return sum(l.map(flip(subtract).f(m).andThen(flip(power).f(2.0)))) / len;
            }
        };
    
    public static F<Iterable<Double>, Double> stdDev =
        new F<Iterable<Double>, Double>() {
            @Override
            public Double f(Iterable<Double> a) {
                return Math.sqrt(variance.f(a));
            }
        };
    
    public static F<Iterable<Double>, Double> median =
        new F<Iterable<Double>, Double>() {
            @Override
            public Double f(Iterable<Double> a) {
                List<Double> l = iterableList(a).sort(doubleOrd);
                int len = l.length();
                P2<List<Double>, List<Double>> split = l.splitAt(len / 2);
                
                if (len % 2 == 0) {
                    return (split._1().last() + split._2().head()) / 2.0;
                }
                return split._2().head();
            }
        };
    
    public static F<Iterable<Double>, Iterable<Double>> rank =
        new F<Iterable<Double>, Iterable<Double>>() {
            @Override
            public Iterable<Double> f(Iterable<Double> a) {
                return join(Stream.iterableStream(a).zipIndex()
                .sort(p2Ord(doubleOrd, intOrd))
                .zipIndex()
                .map(P2.split_(P2.split_(Function.<Double>identity(), Function.<Integer>identity()), Integers.add.f(1)))
                .toList()
                .group(p2Equal(p2Equal(doubleEqual, Utils.<Integer>alwaysEqual()), Utils.<Integer>alwaysEqual()))
                .map(new F<List<P2<P2<Double, Integer>,Integer>>, List<P2<P2<Double,Integer>,Double>>>() {
                    @Override
                    public List<P2<P2<Double,Integer>,Double>> f(List<P2<P2<Double,Integer>,Integer>> a) {
                        final Double average = Integers.sum(a.map(P2.<P2<Double,Integer>,Integer>__2())) / (double) a.length();
                        return a.map(new F<P2<P2<Double,Integer>,Integer>,P2<P2<Double,Integer>,Double>>() {
                            @Override
                            public P2<P2<Double,Integer>,Double> f(P2<P2<Double,Integer>,Integer> b) {
                                return P.p(b._1(), average);
                            }
                        });
                    }
                }))
                .sort(p2Ord(p2Ord(Utils.<Double>equalOrd(), intOrd), Utils.<Double>equalOrd()))
                .map(P2.<P2<Double,Integer>,Double>__2());
            }
        };
   
}
