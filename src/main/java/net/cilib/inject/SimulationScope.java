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

import static com.google.common.base.Preconditions.checkState;
import com.google.common.collect.Maps;
import com.google.inject.Key;
import com.google.inject.OutOfScopeException;
import com.google.inject.Provider;
import com.google.inject.Scope;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * <p>
 * This scope implements a {@link ThreadLocal} storage system, so that different
 * simulations may execute in different threads.
 * </p>
 * <p>
 * It is <b>very</b> important that {@link SimulationScope#exit()} is called
 * when using the scope to ensure that the data stored within the a {@link Thread}
 * is removed. If a number of simulations execute with a single {@link Executor}
 * and the thread global state is not removed, undesirable behavior will occur.
 * </p>
 * @author gpampara
 */
public class SimulationScope implements Scope {

    private final ThreadLocal<Map<Key<?>, Object>> values = new ThreadLocal<Map<Key<?>, Object>>();

    @Override
    public <T> Provider<T> scope(final Key<T> key, final Provider<T> provider) {
        return new Provider<T>() {

            @Override
            public T get() {
                T current = (T) values.get().get(key);
                if (current == null && !values.get().containsKey(key)) {
                    current = provider.get();
                    values.get().put(key, current);
                }
                return current;
            }
        };
    }

    public void enter() {
        checkState(values.get() == null, "A scoping block is already in progress");
        values.set(Maps.<Key<?>, Object>newConcurrentMap());
    }

    public void exit() {
        checkState(values.get() != null, "No scoping block in progress");
        values.remove();
    }

    public <T> void seed(Key<T> key, T value) {
        Map<Key<?>, Object> scopedObjects = getScopedObjectMap(key);
        checkState(!scopedObjects.containsKey(key), "A value for the key %s was "
                + "already seeded in this scope. Old value: %s New value: %s", key,
                scopedObjects.get(key), value);
        scopedObjects.put(key, value);
    }

    <T> T get(Key<T> key) {
        Map<Key<?>, Object> scopedObjects = getScopedObjectMap(key);
        return (T) scopedObjects.get(key);
    }

    private <T> Map<Key<?>, Object> getScopedObjectMap(Key<T> key) {
        Map<Key<?>, Object> scopedObjects = values.get();
        if (scopedObjects == null) {
            throw new OutOfScopeException("Cannot access " + key
                    + " outside of a scoping block");
        }
        return scopedObjects;
    }

    public void update(Key<?> key, Object value) {
        Map<Key<?>, Object> scopedObjects = values.get();
        checkState(scopedObjects.containsKey(key), "A pre-existing value for the key %s "
                + "does not exist. Updates require an existing object reference to replace.", key);
        scopedObjects.put(key, value);
    }
}
