/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.controlparameter.initialisation;

import net.cilib.controlparameter.SettableControlParameter;
import net.cilib.util.Cloneable;

/*
 * This is an interface for initialisation strategies specific to parameters.
 */
public interface ControlParameterInitialisationStrategy<E> extends Cloneable {

    @Override
    ControlParameterInitialisationStrategy getClone();

    void initialise(SettableControlParameter parameter);

}
