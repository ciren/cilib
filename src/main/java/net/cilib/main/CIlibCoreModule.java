package net.cilib.main;

import com.google.inject.AbstractModule;
import net.cilib.annotation.Seed;

/**
 * Module to define core related bindings for injection.
 *
 * <p>
 * The main list of bindings are:
 * <ul>
 *   <li>{@code RandomProvider} - the source of random numbers</li>
 * </ul>
 * @since 0.8
 * @author gpampara
 */
public final class CIlibCoreModule extends AbstractModule {

    @Override
    protected void configure() {
        bindConstant().annotatedWith(Seed.class).to(System.currentTimeMillis());
        // Define the required bindings, like the PRNG
    }

//    @Provides // @SomeScope
//    RandomProvider randomProvider(@Seed long seed, RandomProviderFactory provider) {
//        return provider.create(seed);
//    }
}
