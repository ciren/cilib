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
package net.sourceforge.cilib.type.types.container;

import java.util.Collection;
import java.util.Iterator;
import net.sourceforge.cilib.container.visitor.Visitor;
import net.sourceforge.cilib.math.random.generator.RandomProvider;

/**
 * @author Theuns Cloete
 */
public abstract class ForwardingStructuredType<S> implements StructuredType<S> {
    private static final long serialVersionUID = -8569907896353754409L;

    public abstract StructuredType<S> delegate();

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ForwardingStructuredType<S> other = (ForwardingStructuredType<S>) obj;
        if (this.delegate() != other.delegate() && (this.delegate() == null || !this.delegate().equals(other.delegate()))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.delegate() != null ? this.delegate().hashCode() : 0);
        return hash;
    }

    @Override
    public void accept(Visitor<S> visitor) {
        this.delegate().accept(visitor);
    }

    @Override
    public boolean add(S e) {
        return this.delegate().add(e);
    }

    @Override
    public boolean addAll(Collection<? extends S> c) {
        return this.delegate().addAll(c);
    }

    @Override
    public void clear() {
        this.delegate().clear();
    }

    @Override
    public boolean contains(Object o) {
        return this.delegate().contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return this.delegate().containsAll(c);
    }
    @Override
    public boolean isEmpty() {
        return this.delegate().isEmpty();
    }

    @Override
    public Iterator<S> iterator() {
        return this.delegate().iterator();
    }

    @Override
    public void randomize(RandomProvider random) {
        this.delegate().randomize(random);
    }

    @Override
    public boolean remove(Object o) {
        return this.delegate().remove(o);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return this.delegate().removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return this.delegate().retainAll(c);
    }

    @Override
    public int size() {
        return this.delegate().size();
    }

    @Override
    public Object[] toArray() {
        return this.delegate().toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return this.delegate().toArray(a);
    }
}
