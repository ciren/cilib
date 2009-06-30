/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.container;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class fills the need to create a arbitraty multi-dimensional array structure.
 *
 * @author gpampara
 */
public class MultiDimensionalArray<E> implements Array {

    private List<Array> data;

    public MultiDimensionalArray() {
        this.data = new ArrayList<Array>();
    }

    public MultiDimensionalArray(int... dimensions) {
        this.data = createStructure(dimensions);
    }

    public int size() {
        return data.size();
    }

    @Override
    public Array get(int index) {
        return this.data.get(index);
    }

    @Override
    public <T> void set(T array, int index) {
        Array arr = (Array) array;
        this.data.set(index, arr);
    }

    /**
     * Set the element at the provided index. The index is a list
     * defining the traversal path into the multi-dimensional array.
     * @param element The element to set at the given index position.
     * @param indicies The indicies to follow.
     */
    public void setElement(E element, int... indicies) {
        Array current = this.data.get(indicies[0]);

        // Get the position of the containing array object
        for (int i = 1; i < indicies.length-1; i++) {
            current = current.get(indicies[i]);
        }

        // Set the value
        current.set(element, indicies[indicies.length-1]);
    }

    /**
     * Get the element located at the provided indicies.
     * @param indicies The list of indecies to follow to obtain the desired element.
     * @return The target element at the provided index.
     * @throws IndexOutOfBoundsException if an index is larger that a sub-array.
     */
    public E getElement(int... indicies) {
        Array current = this.data.get(indicies[0]);

        // Get the position of the containing array object
        for (int i = 1; i < indicies.length-1; i++) {
            current = current.get(indicies[i]);
        }

        // Return and cast to the defined parameter type. The cast is needed.
        return (E) current.get(indicies[indicies.length-1]);
    }

    private List<Array> createStructure(int... dimensions) {
        List<Array> structure = new ArrayList<Array>();

        if (dimensions.length == 1) {
            structure.add(new StandardArray(dimensions[0]));
            return structure;
        }

        if (dimensions.length == 2) {
            int elementLength = dimensions[1];
            for (int i = 0; i < dimensions[0]; i++) {
                StandardArray<E> array = new StandardArray<E>(elementLength);
                structure.add(array);
            }

            return structure;
        }

        for (int i = 0; i < dimensions[0]; i++) {
            int[] subRange = Arrays.copyOfRange(dimensions, 1, dimensions.length);
            MultiDimensionalArray<E> multi = new MultiDimensionalArray<E>();
            List<Array> subElements = createStructure(subRange); // need to work on this

            for (Array a : subElements)
                multi.data.add(a);

            structure.add(multi);
        }

        return structure;
    }

}
