/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.tuning.parameterchange.reactions;

import static fj.data.List.replicate;
import static net.cilib.util.functions.Numerics.doubleValue;
import static net.cilib.util.functions.Utils.pairwise;
import net.cilib.math.random.GaussianDistribution;
import net.cilib.math.random.UniformDistribution;
import net.cilib.tuning.TuningAlgorithm;
import net.cilib.type.types.Numeric;
import net.cilib.type.types.container.Vector;
import net.cilib.util.functions.Utils;
import fj.F;
import fj.Ord;
import fj.P2;
import fj.data.List;
import fj.data.Stream;

public class GaussianParameterChangeReaction extends ParameterChangeReaction {

    @Override
    public List<Vector> f(final TuningAlgorithm alg) {
        final List<List<Double>> pars = alg.getParameterList().map(Utils.<Numeric,Iterable>iterableList()
            .andThen(List.<Numeric,Double>map_().f(doubleValue())));
        final int size = pars.length();
        final int pSize = pars.head().length();
        final UniformDistribution uniform = new UniformDistribution();
        final GaussianDistribution gaussian = new GaussianDistribution();
        final List<Double> min = pars.foldLeft(pairwise(Ord.doubleOrd.min), replicate(pSize, Double.MAX_VALUE));
        final List<Double> max = pars.foldLeft(pairwise(Ord.doubleOrd.max), replicate(pSize, Double.MIN_VALUE));

        return Stream.range(0, (int) count.getParameter()).map(new F<Integer, Vector>() {
            @Override
            public Vector f(Integer a) {
                return Vector.copyOf(pars.drop((int) (size * uniform.getRandomNumber())).head()
                    .zip(min)
                    .zip(max)
                    .map(new F<P2<P2<Double, Double>, Double>, Double>() {
                        @Override
                        public Double f(P2<P2<Double, Double>, Double> a) {
                            return gaussian.getRandomNumber(a._1()._1(), (a._2() - a._1()._2()) * (1.0 - alg.getPercentageComplete()));
                        }
                }));
            }
        }).toList();
    }

}
