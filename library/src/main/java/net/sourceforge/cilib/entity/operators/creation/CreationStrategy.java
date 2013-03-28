/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.operators.creation;

import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.operators.Operator;

/**
 * Creation operator definition. The manner in which new {@code Entity} instances
 * are created is specified.
 */
public interface CreationStrategy extends Operator {

    @Override
    CreationStrategy getClone();

    /**
     * Create an Entity, based on the provided parameters.
     *
     * TODO: this may need to be simplified in some way.
     *
     * @param targetEntity
     * @param current
     * @param topology
     * @return the created entity.
     */
    <T extends Entity> T create(T targetEntity, T current, Topology<T> topology);

    public void setScaleParameter(double scaleParameterValue);

    public ControlParameter getScaleParameter();
}
