/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.tuning.parameterlist;

import fj.F;
import fj.P2;
import fj.data.List;
import fj.data.Stream;
import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.math.random.generator.quasi.Sobol;
import net.sourceforge.cilib.tuning.parameters.TuningBounds;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.functions.Utils;

public class SobolParameterListProvider extends ParameterListProvider {
    
    private List<TuningBounds> parameters;
    private int count;
    private int precision;
    
    public SobolParameterListProvider() {
        this.parameters = List.<TuningBounds>nil();
        this.count = 1000;
        this.precision = 4;
    }

    @Override
    public List<Vector> _1() {
        final Sobol sobol = new Sobol(Rand.nextLong());
        sobol.setDimensions(parameters.length());
        
        return Stream.range(0, count).map(new F<Integer, Vector>() {
            @Override
            public Vector f(Integer a) {
                final double[] p = sobol.nextPoint();
                return Vector.copyOf(parameters.zipIndex()
                    .map(new F<P2<TuningBounds, Integer>, Double>() {
                        @Override
                        public Double f(P2<TuningBounds, Integer> a) {
                            return p[a._2()] * a._1().getRange() + a._1().getLowerBound();
                        }                    
                    }.andThen(Utils.precision(precision))));
            }            
        }).toList();
    }
    
    public void addParameterBounds(TuningBounds p) {
        parameters = parameters.snoc(p);
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public int gtPrecision() {
        return precision;
    }
}
