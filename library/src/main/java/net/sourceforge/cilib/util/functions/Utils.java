/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.functions;

import com.google.common.base.Predicate;
import fj.Equal;
import fj.F;
import fj.F2;
import fj.Function;
import fj.Ord;
import fj.Ordering;
import fj.P2;
import fj.data.List;

public final class Utils {
    
    private Utils() {}
    
    public static <T, I extends Iterable<T>> F<I, List<T>> iterableList() {
        return new F<I, List<T>>() {
            @Override
            public List<T> f(I a) {
                return List.<T>iterableList(a);
            }
        };
    }
    
    public static <T> F<Integer, F<List<T>, T>> index() {
        return new F<Integer, F<List<T>, T>>() {
            @Override
            public F<List<T>, T> f(final Integer a) {
                return new F<List<T>, T>() {
                    @Override
                    public T f(List<T> b) {
                        return b.index(a);
                    }
                };
            }
        };
    }
    
    public static F<Double, Double> precision(final int p) {
        return new F<Double, Double>() {
            @Override
            public Double f(Double a) {
                return Math.round(Math.pow(10, p) * a) / Math.pow(10, p);
            }
        };
    }
    
    public static <A> F2<List<A>, List<A>, List<A>> pairwise(final F<A, F<A, A>> f) {
        return new F2<List<A>, List<A>, List<A>>() {
            @Override
            public List<A> f(List<A> a, List<A> b) {
                return a.zip(b).map(new F<P2<A, A>, A>() {
                    @Override
                    public A f(P2<A, A> a) {
                        return f.f(a._1()).f(a._2());
                    }                    
                });
            }
        };
    }
    
    public static <T> F<T, Boolean> predicate(final Predicate<T> pred) {
        return new F<T, Boolean>() {
            @Override
            public Boolean f(T a) {
                return pred.apply(a);
            }
        };
    }
    
    public static <T> Ord<T> equalOrd() {
        return Ord.ord(Function.curry(new F2<T, T, Ordering>() {
            @Override
            public Ordering f(T a, T b) {
                return Ordering.EQ;
            }
        }));
    }
    
    public static <T> Equal<T> alwaysEqual() {
        return Equal.equal(Function.curry(new F2<T, T, Boolean>() {
            @Override
            public Boolean f(T a, T b) {
                return true;
            }
        }));
    }

}
