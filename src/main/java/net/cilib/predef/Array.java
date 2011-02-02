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
package net.cilib.predef;

import com.google.common.base.Function;

import java.util.Iterator;

import static net.cilib.predef.Predef.array;

/**
 * @param <A>
 */
public final class Array<A> implements Iterable<A> {
    private final Object[] a;

    Array(Object[] a) {
        this.a = a;
    }

    @Override
    public Iterator<A> iterator() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @SuppressWarnings("unchecked")
    public <B, C> Array<C> map(Function<B, C> function) {
        Object[] local = new Object[a.length];
        for (int i = 0; i < a.length; i++) {
            local[i] = function.apply((B) a[i]);
        }
        return (Array<C>) array(local);
    }
}
