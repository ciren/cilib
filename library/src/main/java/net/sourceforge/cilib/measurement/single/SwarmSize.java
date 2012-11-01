/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Int;

public class SwarmSize implements Measurement<Int> {

    @Override
    public Measurement<Int> getClone() {
        return this;
    }

    @Override
    public Int getValue(Algorithm algorithm) {
        return Int.valueOf(((PopulationBasedAlgorithm) algorithm).getTopology().size());
    }
}
