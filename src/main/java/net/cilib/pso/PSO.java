package net.cilib.pso;

import com.google.inject.Inject;
import net.cilib.algorithm.PopulationBasedAlgorithm;
import net.cilib.annotation.Initialized;
import net.cilib.collection.Topology;

/**
 * @since 0.8
 * @author gpampara
 */
public class PSO implements PopulationBasedAlgorithm {
    private final Topology topology;

    @Inject
    public PSO(@Initialized Topology topology) {
        this.topology = topology;
    }

    @Override
    public PopulationBasedAlgorithm iterate() {
        System.out.println("performing an iteration");
        return this;
    }
}
