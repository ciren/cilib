/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.tuning.parameterchange.reactions;

import fj.F;
import fj.data.List;
import net.cilib.controlparameter.ConstantControlParameter;
import net.cilib.controlparameter.ControlParameter;
import net.cilib.tuning.TuningAlgorithm;
import net.cilib.type.types.container.Vector;

public abstract class ParameterChangeReaction extends F<TuningAlgorithm, List<Vector>> {

    protected ControlParameter count;

    public ParameterChangeReaction() {
        this.count = ConstantControlParameter.of(100);
    }

    public void setCount(ControlParameter count) {
        this.count = count;
    }

    public ControlParameter getCount() {
        return count;
    }

}
