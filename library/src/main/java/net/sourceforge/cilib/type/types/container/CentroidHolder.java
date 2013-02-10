/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.type.types.container;

import com.google.common.collect.UnmodifiableIterator;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import net.sourceforge.cilib.util.Visitor;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;

/**
 * This is a data structure that holds and manages a list of ClusterCentroids
 */
public class CentroidHolder implements StructuredType<ClusterCentroid>{

    ClusterCentroid[] components;

    /*
     * The CentroidHolder default constructor
     */
    public CentroidHolder() {
        components = new ClusterCentroid[]{};
    }

    /*
     * Creates a centroid holder of some size holding ClusterCentroids of some size
     * @param size The size of the CentroidHolder, i.i. how many ClusterCentroids it must hold
     * @param clusterDimension The size of the ClusterCentroid
     */
    public CentroidHolder(int size, int clusterDimensions) {
        components = new ClusterCentroid[size];
        for(int i = 0; i < size; i++) {
            components[i] = new ClusterCentroid(clusterDimensions);
        }
    }

    /*
     * The CentroidHolder copy constructor
     * @param The CentroidHolder to be copied
     */
    public CentroidHolder(CentroidHolder copy) {
        int index = 0;
        components = new ClusterCentroid[copy.size()];
        for(ClusterCentroid centroid : copy) {
            components[index] = centroid.getClone();
            index++;
        }
    }

    /*
     * The clone method of the CentroidHolder
     * @return the new instance of the CentroidHolder
     */
    @Override
    public CentroidHolder getClone() {
        return new CentroidHolder(this);
    }

    /*
     * The visitor method for the CentroidHolder
     * @param visitor The visitor
     */
    @Override
    public void accept(Visitor<ClusterCentroid> visitor) {
        for (ClusterCentroid centroid : this.components) {
            if (!visitor.isDone()) {
                visitor.visit(centroid);
            }
        }
    }

    /*
     * Reinitialises the values of all dimensions of all ClusterCentroids held by the CentroidHolder to zero
     */
    public void reinitialise() {
        for(ClusterCentroid centroid : components) {
            for(Numeric n : centroid) {
                n = Real.valueOf(0.0);
            }
        }
    }

    /*
     * Returns the total number of ClusterCentroids held by the CentroidHolder
     * @return size The size of the CentroidHolder
     */
    @Override
    public int size() {
        return components.length;
    }

    /*
     * Checks if the CentroidHolder has any elements
     * @return true if the CentroidHodler is empty, false if it contains elements
     */
    @Override
    public boolean isEmpty() {
        if(components.length == 0) {
            return true;
        }

        return false;
    }

    /*
     * Checks if the CentroidHolder contains the element received as a parameter
     * @param The element to be checked for
     * @return true if the element exists within the CentroidHolder, false otherwise
     */
    @Override
    public boolean contains(Object o) {
        ClusterCentroid other;
        for(ClusterCentroid value : components) {
            other = (ClusterCentroid) o;
            if(value.containsAll(other)) {
                return true;
            }
        }
        return false;
    }

    /*
     * Iterates through the elements of the CentroidHolder
     */
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

    /*
     * This method is not supported for CentroidHolders
     */
    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /*
     * Converts the CentroidHodler to an array of ClusterCentroids and stores it in the array provided as a parameter
     * @param array The array to which the array representation must be saved
     * @return array The array representation of the CentroidHolder
     */
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

    /*
     * Adds a new ClusterCentroid to the CentroidHolder
     * @param clusterCentroid The new ClusterCentroid to be added
     * @return true if all commands have executed
     */
    @Override
    public boolean add(ClusterCentroid e) {
        ClusterCentroid[] array = new ClusterCentroid[components.length + 1];
        System.arraycopy(components, 0, array, 0, components.length);
        array[array.length - 1] = e;
        components = array;
        return true;
    }

    /*
     * Removes the object received as a parameter from the CentroidHolder
     * @param object The element to be removed
     * @return true if the object has been found and removed, false otherwise
     */
    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < components.length; i++) {
            if (components[i].containsAll((ClusterCentroid) o)) {
                return remove(i);
            }
        }
        return false;
    }

    /*
     * Removes the ClusterCentroid held at some index by the CentroidHolder
     * @param index The index of the element to be removed
     * @return true if all commands execute, false otherwise
     */
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

    /*
     * Checks if all elements provided as parameters are held by the CentroidHolder
     * @param collection The list of elements to check for
     * @return true if all elements are in the CentroidHolder, false otherwise
     */
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

    /*
     * Adds all the elements in the list received as a parameter to the CentroidHodler
     * @param collection The list of elements to be added to the CentroidHolder
     * @return true if all commands execute
     */
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

    /*
     * This method is not supported by the CentroidHolder
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    /*
     * This method is not supported by the CentroidHolder
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /*
     * Clears all elements from the CentroidHolder
     */
    @Override
    public void clear() {
        this.components = new ClusterCentroid[]{};
    }

    /*
     * Randomizes the elements held by the CentroidHolder
     * @param random The RandomProvider to be used
     */
    @Override
    public void randomise() {
        for (int i = 0; i < components.length; i++) {
            this.components[i].randomise();
        }
    }

    /*
     * Gets the element held by the CentroidHolder at some index
     * @param index The index of the element to be retrieved
     * @return element The retrieved element
     */
    public ClusterCentroid get(int index) {
        return components[index];
    }

    /*
     * Returns the string representation of the CentroidHolder
     * @return string The string representation of the CentroidHolder
     */
    @Override
    public String toString() {
        String result = "{ ";
        for(ClusterCentroid centroid : components) {
            result += centroid.toString() + ", ";
        }
        return result + " }";
    }

    /*
     * Sets the element at some index of the CentroidHolder to the value received as a parameter
     * @param index The index of the element to be replaced
     * @param centroid The ClusterCentroid that will be replace the old one
     */
    public void set(int index, ClusterCentroid centroid) {
        components[index] = centroid.getClone();
    }

}
