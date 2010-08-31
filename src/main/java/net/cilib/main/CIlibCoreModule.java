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
package net.cilib.main;

import com.google.inject.AbstractModule;
import net.cilib.annotation.Seed;
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
        bindConstant().annotatedWith(Seed.class).to(System.currentTimeMillis());
        // Define the required bindings, like the PRNG

        bind(RandomProvider.class).to(MersenneTwister.class);
        bind(Problem.class).to(MockProblem.class);
    }

//    @Provides // @SomeScope
//    RandomProvider randomProvider(@Seed long seed, RandomProviderFactory provider) {
//        return provider.create(seed);
//    }
}
