/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.math;

import fj.*;
import static fj.Function.*;
import static fj.Ord.*;
import fj.data.List;
import static fj.data.List.*;
import fj.data.Stream;
import static fj.function.Doubles.*;
import net.sourceforge.cilib.util.functions.Utils;
import static net.sourceforge.cilib.util.functions.Utils.pairwise;

public final class StatsTests {

    private StatsTests() {}

    public static <T extends Iterable<? extends Iterable<Double>>> P2<Double,Double> friedman(final double alpha, T a) {
        return friedman(alpha).f(a);
    }

    public static <T extends Iterable<? extends Iterable<Double>>> List<Integer> postHoc(final double alpha, final double statistic, T a) {
        return postHoc(alpha, statistic).f(a);
    }

    public static F<Iterable<? extends Iterable<Double>>, P2<Double,Double>>
            friedman(final double alpha) {
        return new F<Iterable<? extends Iterable<Double>>, P2<Double, Double>>() {
            @Override
            public P2<Double, Double> f(Iterable<? extends Iterable<Double>> a) {
                final List<List<Double>> ranks = iterableList(a)
                    .map(Stats.rank.andThen(Utils.<Double,Iterable>iterableList()));

                final int k = ranks.length();
                final int m = ranks.isNotEmpty() ? iterableList(ranks.head()).length() : 0;

                final double numerator = sum(ranks.foldLeft(pairwise(add), replicate(m, 0.0))
                    .map(new F<Double, Double>() {
                        @Override
                        public Double f(Double a) {
                            return Math.pow(a - k * (m + 1) / 2, 2);
                        }
                    })) * (m - 1);

                final double denominator = sum(ranks.map(flip(power).f(2.0).mapList())
                    .foldLeft(pairwise(add), replicate(m, 0.0)))
                    - k * m * (m + 1) * (m + 1) / 4;

                return P.p(numerator / denominator, StatsTables.chisqrDistribution(m - 1, alpha));
            }
        };
    }

    public static F<Iterable<? extends Iterable<Double>>, List<Integer>>
            postHoc(final double alpha, final double statistic) {
        return new F<Iterable<? extends Iterable<Double>>, List<Integer>>() {
            @Override
            public List<Integer> f(Iterable<? extends Iterable<Double>> a) {
                final List<List<Double>> ranks = iterableList(a)
                    .map(Stats.rank.andThen(Utils.<Double,Iterable>iterableList()));

                final int k = ranks.length();
                final int m = ranks.isNotEmpty() ? iterableList(ranks.head()).length() : 0;

                Stream<P2<Double,Integer>> sumOfRanks = ranks.foldLeft(pairwise(add), replicate(m, 0.0)).toStream().zipIndex();
                final double denominator = sum(ranks.map(flip(power).f(2.0).mapList())
                    .foldLeft(pairwise(add), replicate(m, 0.0)))
                    - k * m * (m + 1) * (m + 1) / 4;

                final double posthocStatistic = StatsTables.tDistribution(m - 1, alpha / 2.0);
                final double posthocDenominator = Math.sqrt((2 * k)
                    * (1 - statistic / (k * (m - 1))) * denominator / ((k - 1) * (m - 1)));
                final P2<Double,Integer> best = sumOfRanks.sort(p2Ord(doubleOrd, intOrd)).head();

                return Stream.unzip(sumOfRanks.filter(new F<P2<Double, Integer>, Boolean>() {
                    @Override
                    public Boolean f(P2<Double, Integer> a) {
                        return Math.abs(best._1() - a._1()) / posthocDenominator <= posthocStatistic;
                    }
                }))._2().toList();
            }
        };
    }

}
