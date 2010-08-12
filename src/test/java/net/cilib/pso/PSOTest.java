package net.cilib.pso;

import net.cilib.algorithm.PopulationBasedAlgorithm;
import net.cilib.collection.immutable.ImmutableGBestTopology;
import org.junit.Test;

/**
 *
 * @author gpampara
 */
public class PSOTest {

    @Test
    public void iteration() {
        PopulationBasedAlgorithm instance = new PSO(new ImmutableGBestTopology());
        instance.iterate();
    }
}
