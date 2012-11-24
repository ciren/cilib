/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.tuning.parameters;

import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Returns the values given to the generator. Used if the exact values are known.
 */
public class DiscreteParameterGenerator extends ParameterGenerator {
    
    private Vector parameters;
    
    public DiscreteParameterGenerator() {
        this.parameters = Vector.of();
    }

    @Override
    public Vector _1() {
        return parameters;
    }
    
    public void addParameter(double p) {
        parameters = Vector.newBuilder().copyOf(parameters).add(p).build();
    }

}
