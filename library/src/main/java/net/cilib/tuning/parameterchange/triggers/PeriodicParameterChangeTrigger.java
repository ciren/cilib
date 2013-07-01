/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.tuning.parameterchange.triggers;

import net.cilib.controlparameter.ConstantControlParameter;
import net.cilib.controlparameter.ControlParameter;
import net.cilib.tuning.TuningAlgorithm;

public class PeriodicParameterChangeTrigger extends ParameterChangeTrigger {

    private ControlParameter period;

    public PeriodicParameterChangeTrigger() {
        this.period = ConstantControlParameter.of(1.0);
    }

    @Override
    public Boolean f(TuningAlgorithm a) {
        return (a.getIterations() + 1) % (int) period.getParameter() == 0;
    }

    public void setPeriod(ControlParameter period) {
        this.period = period;
    }

    public ControlParameter getPeriod() {
        return period;
    }

}
