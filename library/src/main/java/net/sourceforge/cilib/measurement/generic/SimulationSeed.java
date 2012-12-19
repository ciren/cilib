/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.generic;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.StringType;

public class SimulationSeed implements Measurement<StringType> {

    public Measurement<StringType> getClone() {
        return this;
    }

    public StringType getValue(Algorithm algorithm) {
        return new StringType(Long.toString(Rand.getSeed()));
    }

}
