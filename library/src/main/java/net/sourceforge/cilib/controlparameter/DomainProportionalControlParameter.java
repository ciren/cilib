/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.controlparameter;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.type.types.container.Vector;

public class DomainProportionalControlParameter implements ControlParameter {

    private double proportion;

    public DomainProportionalControlParameter() {
        this.proportion = 0.1;
    }

    public DomainProportionalControlParameter(DomainProportionalControlParameter copy) {
        this.proportion = copy.proportion;
    }

    @Override
    public double getParameter() {
        return proportion * ((Vector) AbstractAlgorithm.get().getOptimisationProblem()
                .getDomain().getBuiltRepresentation()).boundsOf(0).getRange();
    }

    @Override
    public double getParameter(double min, double max) {
        return getParameter();
    }

    @Override
    public DomainProportionalControlParameter getClone() {
        return new DomainProportionalControlParameter(this);
    }

    public double getProportion() {
        return proportion;
    }

    public void setProportion(double proportion) {
        this.proportion = proportion;
    }
}
