/*
 * MixedVector.java
 * 
 * Created on Oct 18, 2004
 *
 * Copyright (C)  2004 - CIRG@UP 
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
package net.sourceforge.cilib.type.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * Concrete implemetnation of the {@see net.sourceforge.cilib.type.types.Vector}
 * class. Any {@see net.sourceforge.cilib.type.types.Type} object may be contained
 * within this object.
 * 
 * @author Gary Pampara
 * @author Edwin Peer
 */
public class MixedVector extends Vector {
	private static Logger log = Logger.getLogger(MixedVector.class);
	private static final long serialVersionUID = 1L;
	private ArrayList<Type> components;

	
	/**
	 * Create a clone (deep copy) of the <code>MixedVector</code>
	 * 
	 * @return The clone of the <code>MixedVector</code>
	 */
	public MixedVector clone() {
		MixedVector clone = new MixedVector(this.components.size());
		
		for (int i = 0; i < components.size(); ++i) {
			clone.components.add(getType(i).clone());
		}
		
		return clone;
	}
	
	
	/**
	 * Create a <code>MixedVector</code> object with a default size of <code>dimension</code>
	 * and initialised with a the component <code>commponent</code> 
	 * 
	 * @param dimension The dimension of the <code>MixedVector</code> required
	 * @param component The component required for initial values of type <code>Type</code>
	 */
	public MixedVector(int dimension, Type component) {
		if (component instanceof Vector) {
			Vector tmp = (Vector) component;
			int vectorDimension = tmp.getDimension();

			components = new ArrayList<Type>();
			for (int i = 0, offset = 0; i < dimension; ++i, offset += vectorDimension) {
				for (int j = 0; i < vectorDimension; ++j) {
					components.add(offset + j, (Type) tmp.get(j).clone());
				}
			}
		}
		else {
			components = new ArrayList<Type>();
			for (int i = 0; i < dimension; ++i) { 
				components.add((Type) component.clone());
			}
		}	
	}
	
	
	/**
	 * Create a default <code>MixedVector</code> with an initial size of 0
	 *
	 */
	public MixedVector() {
		components = new ArrayList<Type>();
	}
	
	
	/**
	 * Create a <code>MixedVector</code> with a default size specified by <code>dimension</code>
	 * 
	 * @param dimension The size to initialise the <code>MixedVector</code>
	 */
	public MixedVector(int dimension) {
		components = new ArrayList<Type>(dimension);
		components.ensureCapacity(dimension);
	}

	/**
	 * Initialise this vector to contain 'size' 'element' elements
	 * @param size the number of elements this vector should contain
	 * @param element the element that will be cloned and used as each element of this vector
	 * @author Theuns Cloete
	 */
	public void initialise(int size, Type element) {
		for(int i = 0; i < size; i++)
			components.add(element.clone());
	}

	/**
	 * Add an object to the end of the <code>MixedVector</code>.
	 * 
	 * @param t The <code>Type</code> object to add.
	 * @return <tt>true</tt> (as per the general contract of Collection.add).
	 */
	public boolean add(Type t) {
		return components.add(t);
	}
	
	
	/**
	 * Appends all of the elements in the specified Collection to the end of
     * this list, in the order that they are returned by the
     * specified Collection's Iterator.  The behavior of this operation is
     * undefined if the specified Collection is modified while the operation
     * is in progress.  (This implies that the behavior of this call is
     * undefined if the specified Collection is this list, and this
     * list is nonempty.)
     * 
	 * @param collection The <tt>Collection</tt> to be added.
	 * @return <tt>true</tt> if this <tt>Vector</tt> changed as a result of the call.
	 * @throws NullPointerException if the specified collection is null.
	 */
	public boolean addAll(Collection<? extends Type> collection) {
		return components.addAll(collection);
	}
	
	/**
	 * Appends the supplied <tt>Vector</tt> contents to the current <tt>Vector</tt>.
	 * This method copies the references of the supplied <tt>Vector</tt>.
	 * @return true always
	 */
	public boolean append(Vector vector) {
		if (vector == null)
			throw new NullPointerException("Cannot append a null reference to Vector object");
		
		int capacity = this.components.size() + vector.size();
		this.components.ensureCapacity(capacity);
		
		for (Type t : vector) {
			this.components.add(t);
		}
		return true;
	}
	

	/**
	 * Append the supplied <tt>Vector</tt> to the beginning of the current 
	 * <tt>Vector</tt>.
	 * 
	 * @return true always
	 */
	public boolean prepend(Vector vector) {
		if (vector == null)
			throw new NullPointerException("Cannot prepend a null reference to Vector object");
		
		int capacity = this.components.size() + vector.size();
		this.components.ensureCapacity(capacity);
		
		for (int i = vector.size()-1; i >= 0; i--) {
			prepend(vector.getType(i));
		}
		return true;
	}
	
	
	/**
	 * Remove all the enties in this <tt>Vector</tt>.
	 */
	public void clear() {
		this.components.clear();
	}
	
	
	/**
	 * Deterime if the given object is contained within the <tt>MixedVector</tt>.
	 * 
	 * @param obj The object to examined for containment
	 * @return <tt>true</tt> if the object is contained<br><tt>false</tt> otherwise.
	 */
	public boolean contains(Object obj) {
		return components.contains(obj);
	}
	
	
	/**
	 * Determine if the entire given collection is contained within this
	 * <tt>MixedVector</tt>.
	 * 
	 * @param collection The {@see java.util.Collection} to be examined for containment.
	 * @return <tt>true</tt> if the collection is contained, <tt>false</tt> otherwise.
	 */
	public boolean containsAll(Collection<?> collection) {
		return this.components.containsAll(collection);
	}

	/**
     * Tests if this <tt>Vector</tt> has no elements.
     *
     * @return  <tt>true</tt> if this list has no elements;
     *          <tt>false</tt> otherwise.
     */
	public boolean isEmpty() {
		return this.components.isEmpty();
	}

	/**
	 * Returns the elements in this <tt>Vector</tt> in the proper sequence.
	 * 
	 * @return an iterator over the elements in this <tt>Vector</tt> in proper sequence.
	 */
	public Iterator<Type> iterator() {
		return this.components.iterator();
	}

	/**
	 * Remove the <tt>Object</tt> obj from this <tt>Vector</tt>
	 * 
	 * @param obj element to be removed from this <tt>Vector</tt>, if present.
	 * @return <tt>true</tt> if the list contained the specified element.
	 */
	public boolean remove(Object obj) {
		return this.components.remove(obj);		
	}

	/**
	 * Remove the <tt>Collection</tt> of objects from this <tt>Vector</tt>.
	 * 
	 * @return <tt>true</tt> if this collection changed as a result of the call.
	 * @throws UnsupportedOperationException - if the removeAll method is not supported by this collection. 
	 * @throws NullPointerException - if the specified collection is null.
	 */
	public boolean removeAll(Collection<?> c) {
		return this.components.removeAll(c);
	}

	/**
	 * Retains only the specified in the objects contained in the specified collection.
	 * 
	 * @param c elements to be retained in this collection.
	 * @return <tt>true</tt> if this collection changed as a result of the call.
	 * @throws UnsupportedOperationException - if the retainAll method is not supported by this Collection.
	 * @throws NullPointerException - if the specified collection is null.
	 */
	public boolean retainAll(Collection<?> c) {
		return this.components.retainAll(c);
	}

	/**
     * Returns the number of elements in this list.
     *
     * @return  the number of elements in this list.
     */
    public int size() {
    	return this.getDimension();
    }

    /**
     * Returns an array containing all of the elements in this <tt>Vector</tt>
     * in the correct order.
     *
     * @return An <tt>Object</tt> array containing all of the elements in this <tt>Vector</tt>
     * 	       in the correct order.
     */
	public Object [] toArray() {
		try {
			Object tmp [] = new Object[getDimension()];
		
			for (int i = 0; i < getDimension(); i++) {
				tmp[i] = getType(i).clone();
			}
		
			return tmp;
		}
		catch (Exception e) {
			throw new RuntimeException("Could not create a array");
		}
	}

	/**
     * Returns an array containing all of the elements in this <tt>Vector</tt> in the
     * correct order; the runtime type of the returned array is that of the
     * specified array.  If the <tt>Vector</tt> fits in the specified array, it is
     * returned therein.  Otherwise, a new array is allocated with the runtime
     * type of the specified array and the size of this <tt>Vector</tt>.<p>
     *
     * If the <tt>Vector</tt> fits in the specified array with room to spare (i.e., the
     * array has more elements than the <tt>Vector</tt>), the element in the array
     * immediately following the end of the collection is set to
     * <tt>null</tt>.  This is useful in determining the length of the list
     * <i>only</i> if the caller knows that the list does not contain any
     * <tt>null</tt> elements.
     *
     * @param a the array into which the elements of the <tt>Vector</tt> are to
     *		be stored, if it is big enough; otherwise, a new array of the
     * 		same runtime type is allocated for this purpose.
     * @return an array containing the elements of the list.
     * @throws ArrayStoreException if the runtime type of a is not a supertype
     *         of the runtime type of every element in this list.
     */
	public <T> T[] toArray(T[] a) {
		return this.components.toArray(a);
	}

	/**
	 * Return the dimension of the <code>MixedVector</code>
	 * 
	 * @return The dimesion of the <code>MixedVector</code>
	 */
	public int getDimension() {
		return components.size();
	}

	/**
	 * This method returns the current <code>Type</code> at index <code>index</code>. The returned <code>Type</code>
	 * is a <b>clone</b> of the original to avoid issues with the references to the same objects, provided that the
	 * requested object is an instance or sub class of <code>Numeric</code>. The use of this method is not regarded
	 * as correct in this instance and the use of <code>getReal(), getInt()</code> and <code>getBit()</code> 
	 * is preferred. 
	 * 
	 * If the returned type is expected to be something other than a <code>Numeric</code>, but still an object located
	 * with in the Types heirarchy, the <code>Type</code> will be returned <b><i>without</i></b> any cloning. 
	 * 
	 * @param index The index of the requested <code>Type</code>
	 * 
	 * @return A clone of the <tt>Type</tt> object specificed at the given index.
	 * 
	 * @throws IndexOutOfBoundsException if the <tt>index</tt> exceeds the bounds.
	 */
	public Type get(int index) {
		Type component = getType(index);
		
		if (component instanceof Numeric) {
			Type result = getType(index).clone();
			return result;
		}
		else 
			return component;
	}

	/**
	 * Set the element within the <code>MixedVector</code> at index <code>index</code> with
	 * the object defined by <code>value</code>
	 * 
	 * @param index The index within the <code>MixedVector</code>
	 * @param value The object replacing the original item in the <code>MixedVector</code>
	 */
	public void set(int index, Type value) {
		components.set(index, value);
	}

	/**
	 * Insert a new object within the <code>MixedVector</code> such that the <code>Type</code>
	 * object will be located between index-1 and index+1
	 * 
	 * @param index The desired index to insert a new <code>Type</code> object.
	 * @param value The object to be inserted.
	 */
	public void insert(int index, Type value) {
		components.add(index, value);
	}

	/**
	 * Remove the object at index <code>index</code> and adjust the <code>MixedVector</code>
	 * accordingly.
	 * @param index The index within the <code>MixedVector</code> at which the element is to
	 *              be removed.
	 */
	public void remove(int index) {
		components.remove(index);
	}

	/**
	 * Get the <code>Numeric</code> component from the <code>MixedVector</code> at index
	 * <code>index</code>
	 * 
	 * @param index The location of the requested object within the <code>MixedVector</code>
	 * @return A <code>Numeric</code> representing the object located at index <code>index</code>
	 * @throws IndexOutOfBoundsException if the <tt>index</tt> exceeds the bounds.
	 * @throws UnsupportedOperationException if the <tt>Vector</tt> does not contain a <tt>Numeric</tt> object at the given index
	 */
	private Numeric getNumeric(int index) {
		Type tmp = getType(index);
		if (tmp instanceof Numeric) {
			return (Numeric) tmp;
		}
		else {
			throw new UnsupportedOperationException("Attempted to perform a numeric operation on non-numeric type");
		}
	}

	/**
	 * Get the <tt>boolean</tt> represenation of the bit value represented
	 * at index <tt>index</tt> within this <tt>Vector</tt> object.
	 * 
	 * @param index The index of the object that the bit value must be returned.
	 * @return The boolean value represenation of the bit.
	 */
	public boolean getBit(int index) {
		return this.getNumeric(index).getBit();
	}

	/**
	 * Set the value located at the specified index to the given value.
	 * 
	 * @param index The index of the contained <tt>Type</tt>.
	 * @param value The value to be set.
	 */
	public void setBit(int index, boolean value) {
		this.getNumeric(index).setBit(value);
	}

	/**
	 * Get the integer value of the object located at the specified index.
	 * 
	 * @param index The location of the requested object.
	 * @return The int value representation the of the object located at the specified index. 
	 */
	public int getInt(int index) {
		return this.getNumeric(index).getInt();
	}

	/**
	 * Set the value located at the specified index to the given value.
	 * 
	 * @param index The index of the contained <tt>Type</tt>.
	 * @param value The value to be set.
	 */
	public void setInt(int index, int value) {
		this.getNumeric(index).setInt(value);
	}

	/**
	 * Get the real value of the object located at <code>index</code>
	 * 
	 * @param index The index of the real value to get.
	 * @return A <code>double</code> value of the object at index <code>index</code>
	 */
	public double getReal(int index) {
		return this.getNumeric(index).getReal();
	}

	/**
	 * Set the value of the object located at index <code>index</code> to <code>value</code>
	 * 
	 * @param index The index of the object to be altered.
	 * @param value The value to be set.
	 */
	public void setReal(int index, double value) {
		this.getNumeric(index).setReal(value);
	}

	/**
	 * Get the specified <tt>Type</tt> object at the given index.
	 * 
	 * @return The reference to the <tt>Type</tt> object at index <tt>index</tt>.
	 * @throws IndexOutOfBoundsException if the <tt>index</tt> exceeds the bounds.
	 */
	protected Type getType(int index) {
		if (index >= components.size())
			throw new IndexOutOfBoundsException("Cannot find object at index: " + index);
		
		return this.components.get(index);
	}

	/**
	 * Re-Randomise the contents of the structure based on the lower and uppper bounds
	 * enforced on the <code>Type</code>.
	 */
	public void randomise() {
		for (int i = 0; i < components.size(); i++) {
			this.getType(i).randomise();
		}
	}

	/**
	 * Reset all every component within this vector to it's standard default value
	 * as defined and accepted by developers everywhere. The value of the components 
	 * will be 0.0, 0, or false.
	 */
	public void reset() {
		for (int i = 0; i < components.size(); i++) {
			this.getType(i).reset();
		}
	}

	/**
	 * Get the subrange from the current <tt>Vector</tt> as a <tt>Vector</tt>.
	 * 
	 * @param fromIndex The starting index for the subrange
	 * @param toIndex The ending index for the subrange.
	 * @return A <tt>Vector</tt> object representing the sub range of the original <tt>Vector</tt>
	 */
	public Vector subVector(int fromIndex, int toIndex) {
		int length = toIndex - fromIndex + 1;
		MixedVector m = new MixedVector(length);
		
		for (int i = fromIndex; i <= toIndex; i++) {
			m.add(this.get(i));
		}
		return m;
	}

	/**
	 * Create a new <tt>Vector</tt> that is the resultant <tt>Vector</tt>
	 * calculated from the addition of two <tt>Vector</tt> objects.
	 * 
	 * @return The resultant <tt>Vector</tt> object.
	 * @throws UnsupportedOperationException if the current <tt>Vector</tt> and the given <tt>Vector</tt> do
	 * 			not have the same dimensions.
	 */
	public final Vector plus(Vector vector) {
		if (this.components.size() != vector.size())
			throw new UnsupportedOperationException("Cannot add vectors with differing dimensions");

		Vector result = this.clone();
		for(int i = 0; i < result.size(); i++) {
			Numeric numeric = (Numeric)result.getType(i);
			numeric.setReal(numeric.getReal() + vector.getReal(i));
		}
		return result;
	}

	public final Vector subtract(Vector vector) {
		if (this.components.size() != vector.size())
			throw new UnsupportedOperationException("Cannot subtract vectors with differing dimensions");

		Vector result = this.clone();
		for (int i = 0; i < result.size(); i++) {
			Numeric numeric = (Numeric)result.getType(i);
			numeric.setReal(numeric.getReal() - vector.getReal(i));
		}
		return result;
	}

	public final Vector divide(Vector vector) {
		if (this.components.size() != vector.size())
			throw new UnsupportedOperationException("Cannot divide vectors with differing dimensions");
		
		Vector result = this.clone();
		for(int i = 0; i < result.size(); i++) {
			if(vector.getReal(i) == 0.0)
				throw new ArithmeticException("Vector division by zero caused by element " + i);

			Numeric numeric = (Numeric)result.getType(i);
			numeric.setReal(numeric.getReal() / vector.getReal(i));
		}
		return result;
	}

	/**
	 * Scalar-vector multiplication
	 * @param scalar the scalar that should be multiplied with each element of this vector
	 * @return a vector of which the scalar has been multiplied with each element
	 * @author Theuns Cloete
	 */
	public Vector divide(double scalar) {
		if(scalar == 0.0)
			throw new ArithmeticException("Vector division by zero");

		Vector result = this.clone();
		for(int i = 0; i < result.size(); i++) {
			Numeric numeric = (Numeric)result.getType(i);
			numeric.setReal(numeric.getReal() / scalar);
		}
		return result;
	}

	public final Vector multiply(Vector vector) {
		if (this.components.size() != vector.size())
			throw new UnsupportedOperationException("Cannot multiply vectors with differing dimensions");
		
		Vector result = this.clone();
		for(int i = 0; i < result.size(); i++) {
			Numeric numeric = (Numeric)result.getType(i);
			numeric.setReal(numeric.getReal() * vector.getReal(i));
		}
		return result;		
	}

	/**
	 * Scalar-vector multiplication
	 * @param scalar the scalar that should be multiplied with each element of this vector
	 * @return a vector of which the scalar has been multiplied with each element
	 * @author Theuns Cloete
	 */
	public Vector multiply(double scalar) {
		Vector result = this.clone();
		for(int i = 0; i < result.size(); i++) {
			Numeric numeric = (Numeric)result.getType(i);
			numeric.setReal(numeric.getReal() * scalar);
		}
		return result;
	}

	/**
	 * Calculate the norm of this Vector object. All the elements must
	 * be of type {@see net.sourceforge.cilib.type.types.Numeric}.
	 * 
	 * @return The value of the vector norm 
	 */	
	public double norm() {
		double result = 0.0;
		
		for (Type element : this.components) {
			if (element instanceof Numeric) {
				Numeric n = (Numeric) element;
				result += n.getReal() * n.getReal();
			}
		}
		
		return Math.sqrt(result);
	}


	/**
	 * @throws ArithmeticException
	 */
	public final double dot(Vector vector) {
		if (this.size() != vector.size()) {
			throw new ArithmeticException("Cannot perform the dot product on vectors with differing dimensions");
		}
		
		double result = 0.0;
		
		for (int i = 0; i < size(); i++) {
			result += this.getReal(i) * vector.getReal(i);
		}
		
		return result;
	}


	public final Vector cross(Vector vector) {
		// TODO Auto-generated method stub
		return null;
	}

}
