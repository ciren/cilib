package net.cilib.main;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Provides;
import net.cilib.annotation.Initialized;
import net.cilib.collection.Topology;
import net.cilib.collection.immutable.ImmutableGBestTopology;

/**
 * @since 0.8
 * @author gpampara
 */
public final class PopulationBasedAlgorithmModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Topology.class).to(ImmutableGBestTopology.class);
    }

    @Provides
    @Initialized
    Topology getTopology(Provider<Topology> plainTopology) {
        Topology topology = plainTopology.get();

        return topology;
    }
}
