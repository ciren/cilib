/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.tuning.parameterchange.reactions;

import fj.F;
import fj.Ord;
import fj.P2;
import fj.data.List;
import static fj.data.List.*;
import fj.data.Stream;
import net.sourceforge.cilib.math.random.GaussianDistribution;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.tuning.TuningAlgorithm;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Vector;
import static net.sourceforge.cilib.util.functions.Numerics.*;
import net.sourceforge.cilib.util.functions.Utils;
import static net.sourceforge.cilib.util.functions.Utils.*;

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
