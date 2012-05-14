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
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;

/**
 *
 * @author Kristina
 */
public class ClusterCentroid implements StructuredType<Numeric>{
    private double[] dataItemDistances;
    private Numeric[] components;

    public ClusterCentroid() {
        dataItemDistances = new double[]{};
        components = new Numeric[]{};
    }
    
    public ClusterCentroid(int size) {
        dataItemDistances = new double[]{};
        components = new Numeric[size];
        for(int i = 0; i < size; i++) {
            components[i] = Int.valueOf(0);
        }
    }
    
    public static ClusterCentroid of(Number... numbers) {
        Numeric[] elements = new Numeric[numbers.length];
        int index = 0;
        for (Number number : numbers) {
            elements[index++] = Real.valueOf(number.doubleValue());
        }
        return new ClusterCentroid(elements);
    }
    
    private ClusterCentroid(Numeric[] elements) {
        this.components = elements;
        dataItemDistances = new double[]{};
    }
    
    public ClusterCentroid(ClusterCentroid copy) {
        dataItemDistances = copy.dataItemDistances.clone();
        components = copy.components.clone();
    }
    
    public void copy(Vector input) {
        components = new Numeric[input.size()];
        for(int i = 0; i < input.size(); i++) {
            components[i] = input.get(i);
        }
    }
    
    @Override
    public ClusterCentroid getClone() {
        return new ClusterCentroid(this);
    }

    @Override
    public void accept(Visitor<Numeric> visitor) {
        for (Numeric numeric : this.components) {
            if (!visitor.isDone()) {
                visitor.visit(numeric);
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
    public Iterator<Numeric> iterator() {
        return new UnmodifiableIterator<Numeric>() {

            private int index = 0;

            @Override
            public final boolean hasNext() {
                return index < components.length;
            }

            @Override
            public final Numeric next() {
                return components[index++];
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[components.length];
        
        int i = 0;
        for(Numeric n : components) {
            array[i] = n.doubleValue();
            i++;
        }
        
        return array;
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
    public boolean add(Numeric e) {
        Numeric[] array = new Numeric[components.length + 1];
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
        Numeric[] array = new Numeric[components.length - 1];
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
            if (!this.contains((Numeric) i.next())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean contains(Object o) {
        for(Numeric value : components) {
            if(o.equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends Numeric> c) {
        int size = components.length + c.size();
        Numeric[] array = new Numeric[size];
        System.arraycopy(components, 0, array, 0, components.length);
        int index = components.length;
        for (Numeric numeric : c) {
            array[index++] = numeric;
        }

        this.components = array;
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        components = new Numeric[components.length - 1];
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void clear() {
        this.components = new Numeric[]{};
    }

    @Override
    public void randomize(RandomProvider random) {
        for (int i = 0; i < components.length; i++) {
            this.components[i].randomize(random);
        }
    }
    
    public double[] getDataItemDistances() {
        return dataItemDistances;
    }
    
    public void setDataItemDistances(double[] newDataItemDistances) {
        dataItemDistances = newDataItemDistances;
    }
    
    public boolean addDataItemDistance(double distance) {
        double[] array = new double[dataItemDistances.length + 1];
        System.arraycopy(dataItemDistances, 0, array, 0, dataItemDistances.length);
        array[array.length - 1] = distance;
        dataItemDistances = array;
        return true;
    }
    
    public Vector toVector() {
        Vector.Builder builder = Vector.newBuilder();
        for(Numeric number : components) {
            builder.add(number);
        }
        
        return builder.build();
    }
    
    public void clearDataItemDistances() {
        dataItemDistances = new double[]{};
    }
    
    @Override
    public String toString() {
        String result = "[";
        for(Numeric number : components) {
            result += number + ", ";
        }
        
        return result + "]";
    }
}
