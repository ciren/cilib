/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.controlparameter.adaptation;

import net.cilib.controlparameter.SettableControlParameter;
import net.cilib.entity.Entity;
import net.cilib.util.Cloneable;

/**
 * This is an interface for parameter adaptation strategies, that is, strategies
 * that will change the values of parameters.
 */
public interface ParameterAdaptationStrategy extends Cloneable {
    @Override
    ParameterAdaptationStrategy getClone();

    public void change(SettableControlParameter parameter);

    public void accepted(SettableControlParameter parameter, Entity entity, boolean accepted);

    public double recalculateAdaptiveVariables();
}
