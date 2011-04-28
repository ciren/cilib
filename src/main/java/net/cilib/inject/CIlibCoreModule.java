/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.cilib.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import net.cilib.entity.FitnessComparator;
import net.cilib.inject.annotation.Seed;
import net.cilib.inject.annotation.SimulationScoped;
import net.cilib.inject.annotation.Unique;
import net.cilib.main.MockProblem;
import net.cilib.problem.Problem;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;

/**
 * Module to define core related bindings for injection. The list of provided
 * bindings are:
 * <table>
 * <thead>
 *   <tr><td>Data type</td><td>Annotation</td><td>Default value</td><td>Description</td></tr>
 * </thead>
 * <tbody>
 *   <tr>
 *     <td>{@code int}</td>
 *     <td>{@link Seed}</td>
 *     <td>{@code System.currentTimeMillis()}</td>
 *     <td>Seed value for PRNGs</td>
 *   </tr>
 *   <tr>
 *     <td>{@code RandomProvider}</td>
 *     <td></td>
 *     <td>{@link MersenneTwister}</td>
 *     <td>Source for random numbers</td>
 *   </tr>
 * </tbody>
 * </table>
 * @since 0.8
 * @author gpampara
 */
public final class CIlibCoreModule extends AbstractModule {

    @Override
    protected void configure() {
        // Define the custom simulation scope
        SimulationScope scope = new SimulationScope();
        bindScope(SimulationScoped.class, scope);
        bind(SimulationScope.class).toInstance(scope);

        // CIlib constants
        bindConstant().annotatedWith(Seed.class).to(System.currentTimeMillis());

        // Define the required bindings, like the PRNG
        bind(RandomProvider.class).to(MersenneTwister.class);
        bind(RandomProvider.class).annotatedWith(Unique.class).toProvider(UniqueRandomProvider.class);

        bind(Problem.class).to(MockProblem.class);
        bind(FitnessComparator.class).toInstance(FitnessComparator.MIN);
    }

    static class UniqueRandomProvider implements Provider<RandomProvider> {

        private final Provider<RandomProvider> provider;

        @Inject
        UniqueRandomProvider(Provider<RandomProvider> provider) {
            this.provider = provider;
        }

        @Override
        public RandomProvider get() {
            return provider.get();
        }
    }
}
