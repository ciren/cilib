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

import com.google.common.collect.UnmodifiableIterator;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import net.sourceforge.cilib.container.visitor.Visitor;
import net.sourceforge.cilib.math.random.generator.RandomProvider;

/**
 *
 * @author Kristina
 */
public class CentroidHolder implements StructuredType<ClusterCentroid>{

    ClusterCentroid[] components;
    public CentroidHolder() {
        components = new ClusterCentroid[]{};
    }
    
    public CentroidHolder(int size, int clusterDimensions) {
        components = new ClusterCentroid[size];
        for(int i = 0; i < size; i++) {
            components[i] = new ClusterCentroid(clusterDimensions);
        }
    }
    
    public CentroidHolder(CentroidHolder copy) {
        components = copy.components.clone();
    }
    
    @Override
    public CentroidHolder getClone() {
        return new CentroidHolder(this);
    }

    @Override
    public void accept(Visitor<ClusterCentroid> visitor) {
        for (ClusterCentroid centroid : this.components) {
            if (!visitor.isDone()) {
                visitor.visit(centroid);
            }
        }
    }

    @Override
    public int size() {
        return components.length;
    }

    @Override
    public boolean isEmpty() {
        if(components.length == 0) {
            return true;
        }
        
        return false;
    }

    @Override
    public boolean contains(Object o) {
        for(ClusterCentroid value : components) {
            if(o.equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<ClusterCentroid> iterator() {
        return new UnmodifiableIterator<ClusterCentroid>() {

            private int index = 0;

            @Override
            public final boolean hasNext() {
                return index < components.length;
            }

            @Override
            public final ClusterCentroid next() {
                return components[index++];
            }

        };
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < components.length) {
            return (T[]) Arrays.copyOf(components, components.length, a.getClass());
        }
        System.arraycopy(components, 0, a, 0, components.length);
        if (a.length > components.length) {
            a[components.length] = null;
        }
        return a;
    }

    @Override
    public boolean add(ClusterCentroid e) {
        ClusterCentroid[] array = new ClusterCentroid[components.length + 1];
        System.arraycopy(components, 0, array, 0, components.length);
        array[array.length - 1] = e;
        components = array;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < components.length; i++) {
            if (components[i].equals(o)) {
                return remove(i);
            }
        }
        return false;
    }
    
    private boolean remove(final int index) {
        ClusterCentroid[] array = new ClusterCentroid[components.length - 1];
        int count = 0;
        for (int i = 0; i < index; i++) {
            array[count++] = components[i];
        }
        for (int i = index + 1; i < components.length; i++) {
            array[count++] = components[i];
        }
        components = array;
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        Iterator<?> i = c.iterator();
        while (i.hasNext()) {
            if (!this.contains((ClusterCentroid) i.next())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends ClusterCentroid> c) {
        int size = components.length + c.size();
        ClusterCentroid[] array = new ClusterCentroid[size];
        System.arraycopy(components, 0, array, 0, components.length);
        int index = components.length;
        for (ClusterCentroid numeric : c) {
            array[index++] = numeric;
        }

        this.components = array;
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        components = new ClusterCentroid[components.length - 1];
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void clear() {
        this.components = new ClusterCentroid[]{};
    }

    @Override
    public void randomize(RandomProvider random) {
        for (int i = 0; i < components.length; i++) {
            this.components[i].randomize(random);
        }
    }
    
    public ClusterCentroid get(int index) {
        return components[index];
    }
    
}
