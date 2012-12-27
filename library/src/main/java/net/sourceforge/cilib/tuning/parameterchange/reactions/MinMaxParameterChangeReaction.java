/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.tuning.parameterchange.reactions;

import fj.*;
import fj.data.List;
import static fj.data.List.replicate;
import net.sourceforge.cilib.tuning.TuningAlgorithm;
import net.sourceforge.cilib.tuning.parameterlist.SobolParameterListProvider;
import net.sourceforge.cilib.tuning.parameters.TuningBounds;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Vector;
import static net.sourceforge.cilib.util.functions.Numerics.doubleValue;
import net.sourceforge.cilib.util.functions.Utils;
import static net.sourceforge.cilib.util.functions.Utils.pairwise;

public class MinMaxParameterChangeReaction extends ParameterChangeReaction {

    @Override
    public List<Vector> f(TuningAlgorithm a) {
        final SobolParameterListProvider sobol = new SobolParameterListProvider();
        sobol.setCount((int) count.getParameter());

        List<List<Double>> pars = a.getParameterList().map(Utils.<Numeric,Iterable>iterableList()
            .andThen(List.<Numeric,Double>map_().f(doubleValue())));
        int size = pars.head().length();
        List<Double> min = pars.foldLeft(pairwise(Ord.doubleOrd.min), replicate(size, Double.MAX_VALUE));
        List<Double> max = pars.foldLeft(pairwise(Ord.doubleOrd.max), replicate(size, Double.MIN_VALUE));

        min.zip(max).map(new F<P2<Double, Double>, TuningBounds>() {
            @Override
            public TuningBounds f(P2<Double, Double> a) {
                return new TuningBounds(a._1(), a._2());
            }            
        }).foreach(new F<TuningBounds, Unit>() {
            @Override
            public Unit f(TuningBounds a) {
                sobol.addParameterBounds(a);
                return Unit.unit();
            }
        });

        return sobol._1();
    }

}
