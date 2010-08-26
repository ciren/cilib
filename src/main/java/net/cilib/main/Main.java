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

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import java.util.List;
import net.cilib.algorithm.AlgorithmExecutor;
import net.cilib.algorithm.DE;
import net.cilib.annotation.Initialized;
import net.cilib.entity.Entity;
import net.cilib.collection.Topology;
import net.cilib.measurement.Measurement;

/**
 * @since 0.8
 * @author gpampara
 */
public final class Main {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new CIlibCoreModule(), new PopulationBasedModule());

        Main main = injector.getInstance(Main.class);
        // Something here?

        Topology<Entity> topology = injector.getInstance(Key.get(Topology.class, Initialized.class));
        DE a = injector.getInstance(DE.class);
        List<Predicate<Measurement>> stoppingConditions = Lists.newArrayList();
        stoppingConditions.add(new Predicate<Measurement>() {
            @Override
            public boolean apply(Measurement input) {
                return false;
            }
        });
        AlgorithmExecutor executor = new AlgorithmExecutor(stoppingConditions);
        executor.execute(a, topology);
    }
}
