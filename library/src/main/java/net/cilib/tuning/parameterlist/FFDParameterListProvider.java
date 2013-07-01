/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.tuning.parameterlist;

import fj.P1;
import fj.data.List;
import net.cilib.math.Maths;
import net.cilib.tuning.parameters.ParameterGenerator;
import net.cilib.type.types.container.Vector;

public class FFDParameterListProvider extends ParameterListProvider {

    private List<P1<Vector>> parameters;

    public FFDParameterListProvider() {
        this.parameters = List.<P1<Vector>>nil();
    }

    @Override
    public List<Vector> _1() {
        return Maths.combinations(parameters.map(P1.<Vector>__1()));
    }

    public void addParameter(ParameterGenerator p) {
        this.parameters = parameters.cons(p);
    }
}
