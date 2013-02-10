/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.type.types.container;

import com.google.common.collect.UnmodifiableIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import net.sourceforge.cilib.util.Visitor;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;

/**
 * This class serves the same purpose as a Vector, except with added functionality.
 * It was designed to be used for clustering problems.
 * It holds a numeric array which represents the position of the centroid.
 * It holds a double array to hold the distance value of each data item to the centroid
 * It holds an ArrayList of the data items assigned to the centroid
 */

public class ClusterCentroid implements StructuredType<Numeric>{
    private double[] dataItemDistances;
    private ArrayList<Vector> dataItems;
    private Numeric[] components;

    /*
     * Cluster Centroid default constructor
     */
    public ClusterCentroid() {
        dataItemDistances = new double[]{};
        components = new Real[]{};
        dataItems = new ArrayList<Vector>();
    }

    /*
     * Creates a ClusterCentroid of some size
     * @param size The size of the clusterCentroid
     */
    public ClusterCentroid(int size) {
        dataItemDistances = new double[]{};
        components = new Numeric[size];
        dataItems = new ArrayList<Vector>();
        for(int i = 0; i < size; i++) {
            components[i] = Real.valueOf(0);
        }
    }

    /*
     * Creates a ClusterCentroid containing the numbers received as parameters
     * @param numbers The values to be held by the ClusterCentroid
     * @return The new ClusterCentorid
     */
    public static ClusterCentroid of(Number... numbers) {
        Real[] elements = new Real[numbers.length];
        int index = 0;
        for (Number number : numbers) {
            elements[index++] = Real.valueOf(number.doubleValue());
        }
        return new ClusterCentroid(elements);
    }

    /*
     * Creates a ClusterCentroid containing the real numbers received as parameters
     * It takes into account the bounds set for each number
     * @param numbers The values to be held by the ClusterCentroid
     * @return The new ClusterCentorid
     */
    public static ClusterCentroid of(Real... numbers) {
        Real[] elements = new Real[numbers.length];
        int index = 0;
        for (Real number : numbers) {
            elements[index++] = Real.valueOf(number.doubleValue(), number.getBounds());
        }
        return new ClusterCentroid(elements);
    }

    /*
     * A constructor that creates a ClusterCentroid containing the elements received as parameters
     * @param elements The elements to be added to the ClusterCentroid
     */
    private ClusterCentroid(Numeric[] elements) {
        this.components = elements;
        dataItemDistances = new double[]{};
        dataItems = new ArrayList<Vector>();
    }

    /*
     * A constructor that creates a ClusterCentroid containing the elements received as parameters
     * It takes into account the bounds set for each number
     * @param elements The elements to be added to the ClusterCentroid
     */
    private ClusterCentroid(Real[] elements) {
        components = new Real[elements.length];
        for(int i = 0; i < elements.length; i++) {
            components[i] = Real.valueOf(elements[i].doubleValue(), elements[i].getBounds());
        }
        dataItemDistances = new double[]{};
        dataItems = new ArrayList<Vector>();
    }

    /*
     * The copy constructor of a ClusterCentroid
     * @param copy The ClusterCentroid to be copied
     */
    public ClusterCentroid(ClusterCentroid copy) {
        dataItemDistances = copy.dataItemDistances;
        components = copy.components.clone();
        dataItems = copy.dataItems;
    }

    /*
     * Creates a ClusterCentroid from the Vector given
     * @param input The vector to be converted to a ClusterCentroid
     */
    public void copy(Vector input) {
        components = new Real[input.size()];
        for(int i = 0; i < input.size(); i++) {
            components[i] = Real.valueOf(input.get(i).doubleValue());
        }
    }

    /*
     * The clone method for the ClusterCentroid
     * @return the new instance of the ClusterCentroid
     */
    @Override
    public ClusterCentroid getClone() {
        return new ClusterCentroid(this);
    }

    /*
     * The visitor method for the ClusterCentroid
     * @param visitor The visitor
     */
    @Override
    public void accept(Visitor<Numeric> visitor) {
        for (Numeric numeric : this.components) {
            if (!visitor.isDone()) {
                visitor.visit(numeric);
            }
        }
    }

    /*
     * Returns the size of the ClusterCentroid
     * @return size The size of the ClusterCentroid
     */
    @Override
    public int size() {
        return components.length;
    }

    /*
     * Checks if the ClusterCentroid has any components
     * @return true if the ClusterCentroid is empty, false if it contains some element
     */
    @Override
    public boolean isEmpty() {
        if(components.length == 0) {
            return true;
        }

        return false;
    }

    /*
     * Iterates through the components of the ClusterCentroid
     */
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

    /*
     * Converts the ClusterCentroid to an array
     * @return array The array representation of the ClusterCentroid
     */
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

    /*
     * Converts the ClusterCentroid to an array and saves it to the array received
     * @param array The array to which the array representation of the ClusterCentroid must be saved
     * @return array The array representation of the ClusterCentroid
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
     * Adds a component to the ClusterCentroid
     * @param element The element to be added
     * @return true if all commands execute
     */
    @Override
    public boolean add(Numeric e) {
        Numeric[] array = new Numeric[components.length + 1];
        System.arraycopy(components, 0, array, 0, components.length);
        array[array.length - 1] = e;
        components = array;
        return true;
    }

    /*
     * Removes an element from the ClusterCentroid
     * @param object The element to be found and removed
     * @return true if the element was found and removed, false otherwise
     */
    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < components.length; i++) {
            if (components[i].equals(o)) {
                return remove(i);
            }
        }
        return false;
    }

    /*
     * Removes the element located in some position from the ClusterCentroid
     * @param index The position of the element to be removed
     * @return true if all commands executed
     */
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

    /*
     * Checks if the ClusterCentroid contains the entire list of elements received as a parameter
     * @param collection The elements to be checked for
     * @return true if all elements were found within the ClusterCentroid, false otherwise
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        Iterator<?> i = c.iterator();
        while (i.hasNext()) {
            if (!this.contains((Real) i.next())) {
                return false;
            }
        }
        return true;
    }

    /*
     * Checks if the ClusterCentroid contains the element received as a parameter
     * @param object The element to be checked for
     * @return True if the element exists in the ClusterCentroid, false otherwise
     */
    @Override
    public boolean contains(Object o) {
        for(Numeric value : components) {
            if(o.equals(value)) {
                return true;
            }
        }
        return false;
    }

    /*
     * Adds all the elements received as parameters to the ClusterCentroid
     * @param collection The collection of elements to be added to the ClusterCentroid
     * @return true if all commands execute
     */
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

    /*
     * Removes all the elements received as parameters from the ClusterCentroid
     * @param collection The collection of elements to be removed
     * @return true if all commands execute
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        components = new Real[components.length - 1];
        return true;
    }

    /*
     * This method is not supported for the ClusterCentroid
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /*
     * Removes all elements from the ClusterCentroid
     */
    @Override
    public void clear() {
        this.components = new Real[]{};
    }

    /*
     * Randomizes the elements held by the ClusterCentroid
     * @param random The RandomProvider to be used
     */
    @Override
    public void randomise() {
        for (int i = 0; i < components.length; i++) {
            this.components[i].randomise();
        }
    }

    /*
     * Returns the dataItems assigned to this ClusterCentroid
     * @return dataItems The list of dataItems assigned to the CentroidHolder
     */
    public ArrayList<Vector> getDataItems() {
        return dataItems;
    }

    /*
     * Returns the list of distances between each data item and the ClusterCentroid
     * @return dataItemDistances The list of distances between each data item and the ClusterCentroid
     */
    public double[] getDataItemDistances() {
        return dataItemDistances;
    }

    /*
     * Sets dataItemDistances array to the one received as a parameter
     * @param newDataItemDistanes The new array of data-item distances
     */
    public void setDataItemDistances(double[] newDataItemDistances) {
        dataItemDistances = newDataItemDistances;
    }

    /*
     * Adds a data-item to the list of data items assigned to the ClusterCentroid
     * It also adds the distance between the ClusterCentroid and this data item to the
     * dataItemDistances array
     * @param distance The distance between the ClusterCentroid and the data-item
     * @param item The data-item to be added to the ClusterCentroid's collection of data-items
     * @return true if all commands have executed
     */
    public boolean addDataItem(double distance, Vector item) {
        double[] array = new double[dataItemDistances.length + 1];
        System.arraycopy(dataItemDistances, 0, array, 0, dataItemDistances.length);
        array[array.length - 1] = distance;
        dataItemDistances = array;
        dataItems.add(item);
        return true;
    }

    /*
     * Converts the ClusterCentroid to a Vector
     * @return vector The vector representation of the ClusterCentroid
     */
    public Vector toVector() {
        Vector.Builder builder = Vector.newBuilder();
        for(Numeric number : components) {
            builder.add(number);
        }

        return builder.build();
    }

    /*
     * Removes all elements from the list of data-items assigned to the ClusterCentroid
     */
    public void clearDataItems() {
        dataItemDistances = new double[]{};
        dataItems.clear();
    }

    /*
     * Returns the string representation of the ClusterCentroid
     * @return string The string representation of the ClusterCentroid
     */
    @Override
    public String toString() {
        String result = "[";
        for(Numeric number : components) {
            result += number + ", ";
        }

        return result + "]";
    }

    /*
     * Returns the element held at some index by the ClusterCentroid
     * @param index The index of the element to be retrieved
     * @return element The element at position "index"
     */
    public Numeric get(int index) {
        return components[index];
    }

    /*
     * Sets the element held by the ClusterCentroid at some index to the new value provided for it
     * @param index The index of the element to be replaced
     * @param value The new value of the element
     */
    public void set(int index, Real value) {
        components[index] = value;
    }
}
