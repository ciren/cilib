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
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.ProvisionException;
import net.cilib.inject.annotation.SimulationScoped;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author gpampara
 */
public class SimulationScopeTest {

    /**
     * This test will ensure that something scoped in "simulation scope" is only
     * available when the scope is active.
     */
    @Test
    public void scoping() {
        Injector injector = Guice.createInjector(new AbstractModule() {

            @Override
            protected void configure() {
                SimulationScope scope = new SimulationScope();
                bindScope(SimulationScoped.class, scope);
                bind(SimulationScope.class).toInstance(scope);
                bind(A.class).to(B.class).in(scope);
            }
        });

        // Getting an instance from the within the scope should provide a valid instance
        SimulationScope scope = injector.getInstance(SimulationScope.class);

        scope.enter();
        A inside1 = injector.getInstance(A.class);
        A inside2 = injector.getInstance(A.class);
        scope.exit();

        Assert.assertSame(inside1, inside2);
    }

    @Test(expected = ProvisionException.class)
    public void outsideScope() {
        Injector injector = Guice.createInjector(new AbstractModule() {

            @Override
            protected void configure() {
                SimulationScope scope = new SimulationScope();
                bindScope(SimulationScoped.class, scope);
                bind(SimulationScope.class).toInstance(scope);
                bind(A.class).to(B.class).in(scope);
            }
        });

        injector.getInstance(A.class);
    }

    /**
     * Updates to a scoped reference that does not exist should result in an
     * error.
     */
    @Test(expected = IllegalStateException.class)
    public void invalidUpdate() {
        SimulationScope scope = new SimulationScope();
        scope.enter();
        try {
            scope.update(Key.get(Integer.class), Integer.valueOf(0));
        } finally {
            scope.exit();
        }
    }

    private static interface A {
    }

    private static class B implements A {
    }
}
