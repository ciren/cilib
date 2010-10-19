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

import com.google.common.collect.Maps;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;
import java.util.Map;

/**
 *
 * @author gpampara
 */
public class SimulationScope implements Scope {
    private Map<Key<?>, Object> scopedObjects;

    @Override
    public <T> Provider<T> scope(final Key<T> key, final Provider<T> provider) {
        return new Provider<T>() {
            @Override
            public T get() {
                T current = (T) scopedObjects.get(key);
                if (current == null && !scopedObjects.containsKey(key)) {
                    current = provider.get();
                    scopedObjects.put(key, current);
                }
                return current;
            }
        };
    }

    public void enter() {
        scopedObjects = Maps.newConcurrentMap(); // Should be normal map?
    }

    public void exit() {
        scopedObjects.clear();
    }

    public <T> void seed(Key<T> key, T value) {
        scopedObjects.put(key, value);
    }
}
