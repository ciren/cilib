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
package net.sourceforge.cilib.Type.Types;

import java.util.ArrayList;


/**
 * @author Gary Pampara
 * @author Edwin Peer
 *
 */
public class MixedVector extends Vector implements Cloneable {
	
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1L;
	
	private ArrayList<Type> components;

	
	/**
	 * Create a clone (deep copy) of the <code>MixedVector</code>
	 * 
	 * @return The clone of the <code>MixedVector</code>
	 * @throws CloneNotSupportedException
	 */
	public Object clone() throws CloneNotSupportedException {
		MixedVector clone = (MixedVector) super.clone();
		clone.components = new ArrayList<Type>();//[components.length];
		for (int i = 0; i < components.size(); ++i) {
			//clone.components[i] =  (Type) components[i].clone();
			clone.components.add((Type) components.get(i).clone());
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
		try {
			if (component instanceof Vector) {
				Vector tmp = (Vector) component;
				int vectorDimension = tmp.getDimension();
				//components = new Type[dimension * vectorDimension];
				components = new ArrayList<Type>();
				for (int i = 0, offset = 0; i < dimension; ++i, offset += vectorDimension) {
					for (int j = 0; i < vectorDimension; ++j) {
						components.add(offset + j, (Type) tmp.get(j).clone());
					}
				}
			}
			else {
				//components = new Type[dimension];
				components = new ArrayList<Type>();
				for (int i = 0; i < dimension; ++i) {
					//components[i] = (Type) component.clone(); 
					components.add((Type) component.clone());
				}
			}	
		}
		catch (CloneNotSupportedException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	
	/**
	 * Create a default <code>MixedVector</code> with an initial size of 0
	 *
	 */
	public MixedVector() {
		//components = new Type[0];
		components = new ArrayList<Type>();
	}
	
	
	/**
	 * Create a <code>MixedVector</code> with a default size specified by <code>dimension</code>
	 * 
	 * @param dimension The size to initialise the <code>MixedVector</code>
	 */
	public MixedVector(int dimension) {
		//System.out.println("dimension: " + dimension);
		//components = new Type[dimension];
		components = new ArrayList<Type>(dimension);
		//System.out.println("size of arraylist: " + components.size());
	}
	
		
	/**
	 * Return the dimension of the <code>MixedVector</code>
	 * 
	 * @return The dimesion of the <code>MixedVector</code>
	 */
	public int getDimension() {
		//return components.length;
		return components.size();
	}
	
	
	/**
	 * This method returns the current <code>Type</code> at index <code>index</code>. The returned <code>Type</code>
	 * is a <b>clone</b> of the original to avoid issues with the references to the same objects.
	 * 
	 * @param index The index of the requested <code>Type</code>
	 */
	public Type get(int index) {
		Type result = null;
		
		try {
			result = (Type) components.get(index).clone();
		}
		catch (CloneNotSupportedException c) {
			throw new RuntimeException(c);
		}
		//return components.get(index).clone(); // FIXME::: This could cause a problem... return a clone?
		return result;
	}

	
	/**
	 * Set the element within the <code>MixedVector</code> at index <code>index</code> with
	 * the object defined by <code>value</code>
	 * 
	 * @param index The index within the <code>MixedVector</code>
	 * @param value The object replacing the original item in the <code>MixedVector</code>
	 */
	public void set(int index, Type value) {
		//System.out.println("class: " + value);
		/*if (value instanceof Vector) {
			Vector vector = (Vector) value;
			int dimension = vector.getDimension();
			//Type[] tmp = new Type[components.length + dimension - 1];
			ArrayList<Type> tmp = new ArrayList<Type>(components.size()+dimension-1);
			for (int i = 0; i < index; ++i) {
				//tmp[i] = components[i];
				tmp.set(i, components.get(i));
			}
			for (int i = index, j = 0; j < dimension; ++i, ++j) {
				//tmp[i] = vector.get(j);
				tmp.set(i, vector.get(j));
			}
			//for (int i = index + dimension, j = index + 1; i < tmp.length; ++i, ++j) {
			for (int i = index + dimension, j = index + 1; i < tmp.size(); ++i, ++j) {
				//tmp[i] = components[j];
				tmp.set(i, components.get(j));
			}
			components = tmp;
		}
		else {
			//components[index] = value;
			//try {
				//components.set(index, (Type) value.clone());
			//}
			//catch (CloneNotSupportedException c) {
				//throw new RuntimeException(c);
			//}
			components.set(index, value);
		}*/
		
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
		/*if (value instanceof Vector) {
			Vector vector = (Vector) value;
			int dimension = vector.getDimension();
			//Type[] tmp = new Type[components.length + dimension];
			ArrayList<Type> tmp = new ArrayList<Type>(components.size() + dimension);
			for (int i = 0; i < index; ++i) {
				//tmp[i] = components[i];
				tmp.set(i, components.get(i));
			}
			for (int i = index, j = 0; j < dimension; ++i, ++j) {
				//tmp[i] = vector.get(j);
				tmp.set(i, vector.get(j));
			}
			//for (int i = index + dimension, j = index + 1; i < tmp.length; ++i, ++j) {
			for (int i = index + dimension, j = index + 1; i < tmp.size(); ++i, ++j) {
				//tmp[i] = components[j];
				tmp.set(i, components.get(j));
			}
			components = tmp;
		}
		else {
			/*
			//Type[] tmp = new Type[components.length + 1];
			ArrayList<Type> tmp = new ArrayList<Type>(components.size()+1);
			for (int i = 0; i < index; ++i) {
				//tmp[i] = components[i];
				tmp.set(i, components.get(i));
			}
			//tmp[index] =  value;
			tmp.set(index, value);
			//for (int i = index + 1; i < tmp.length; ++i) {
			for (int i = index + 1; i < tmp.size(); ++i) {
				//tmp[i] = components[i - 1];
				tmp.set(i, components.get(i-1));
			}
			components = tmp;
			components.add(index, value);
		}*/
		
		components.add(index, value);
	}
	
	
	/**
	 * Remove the object at index <code>index</code> and adjust the <code>MixedVector</code>
	 * accordingly.
	 * @param index The index within the <code>MixedVector</code> at which the element is to
	 *              be removed.
	 */
	public void remove(int index) {
		/*Type[] tmp = new Type[components.length - 1];
		for (int i = 0; i < index; ++i) {
			tmp[i] = components[i];
		}
		for (int i = index; i < tmp.length; ) {
			tmp[i] = components[++i];
		}
		components = tmp;*/
		components.remove(index);
	}
	
	
	/**
	 * Add an object to the end of the <code>MixedVector</code>.
	 * 
	 * @param t The <code>Type</code> object to add.
	 */
	public void add(Type t) {
		//System.out.println("t: " + t);
		components.add(t);
	}

	
	/**
	 * Get the <code>Numeric</code> component from the <code>MixedVector</code> at index
	 * <code>index</code>
	 * 
	 * @param index The location of the requested object within the <code>MixedVector</code>
	 * @return A <code>Numeric</code> representing the object located at index <code>index</code>
	 */
	private Numeric getNumeric(int index) {
		//Type tmp = this.get(index);
		Type tmp = this.components.get(index);
		if (tmp instanceof Numeric) {
			return (Numeric) tmp;
		}
		else {
			throw new UnsupportedOperationException("Attempted to perform a numeric operation on non-numeric type");
		}
	}
	
	
	/**
	 * 
	 */
	public boolean getBit(int index) {
		return this.getNumeric(index).getBit();
	}

	
	/**
	 * 
	 */
	public void setBit(int index, boolean value) {
		this.getNumeric(index).setBit(value);
	}

	
	/**
	 * 
	 */
	public int getInt(int index) {
		return this.getNumeric(index).getInt();
	}

	
	/**
	 * 
	 */
	public void setInt(int index, int value) {
		this.getNumeric(index).setInt(value);
	}

	
	/**
	 * Get the real value of the object located at <code>index</code>
	 * @return A <code>double</code> value of the object at index <code>index</code>
	 */
	public double getReal(int index) {
		return this.getNumeric(index).getReal();
	}

	
	/**
	 * Set the value of the object located at index <code>index</code> to <code>value</code>
	 */
	public void setReal(int index, double value) {
		this.getNumeric(index).setReal(value);
	}
	
	
	/**
	 * Re-Randomize the contents of the structure based on the lower and uppper bounds
	 * enforced on the <code>Type</code> 
	 */
	public void randomize() {
		for (int i = 0; i < components.size(); i++) {
			components.get(i).randomize();
		}
	}
}
