/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.tuning;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.controlparameter.ControlParameter;

public class TuningControlParameter implements ControlParameter {
    
    private int index;

    @Override
    public double getParameter() {
        TuningAlgorithm algorithm = (TuningAlgorithm) AbstractAlgorithm.getAlgorithmList().get(0);
        return algorithm.getCurrentParameters().doubleValueOf(index);
    }

    @Override
    public double getParameter(double min, double max) {
        return getParameter();
    }

    @Override
    public TuningControlParameter getClone() {
        return this;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
