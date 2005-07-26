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
package net.sourceforge.cilib.Type;


/**
 * @author espeer
 *
 */
public class OldMixedVector extends OldVector implements Cloneable {
	
	public Object clone() throws CloneNotSupportedException {
		OldMixedVector clone = (OldMixedVector) super.clone();
		clone.components = new OldType[components.length];
		for (int i = 0; i < components.length; ++i) {
			clone.components[i] =  (OldType) components[i].clone();
		}
		return clone;
	}
		
	public OldMixedVector(int dimension, OldType component) {
		try {
			if (component instanceof OldVector) {
				OldVector tmp = (OldVector) component;
				int vectorDimension = tmp.getDimension();
				components = new OldType[dimension * vectorDimension];
				for (int i = 0, offset = 0; i < dimension; ++i, offset += vectorDimension) {
					for (int j = 0; i < vectorDimension; ++j) {
						components[offset + j] = (OldType) tmp.get(j).clone();
					}
				}
			}
			else {
				components = new OldType[dimension];
				for (int i = 0; i < dimension; ++i) {
					components[i] = (OldType) component.clone(); 
				}
			}	
		}
		catch (CloneNotSupportedException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public OldMixedVector() {
		components = new OldType[0];
	}
	
	public OldMixedVector(int dimension) {
		components = new OldType[dimension];
	}
	
	public int getDimension() {
		return components.length;
	}
	
	public OldType get(int index) {
		return components[index];
	}

	public void set(int index, OldType value) {
		if (value instanceof OldVector) {
			OldVector vector = (OldVector) value;
			int dimension = vector.getDimension();
			OldType[] tmp = new OldType[components.length + dimension - 1];
			for (int i = 0; i < index; ++i) {
				tmp[i] = components[i];
			}
			for (int i = index, j = 0; j < dimension; ++i, ++j) {
				tmp[i] = vector.get(j);
			}
			for (int i = index + dimension, j = index + 1; i < tmp.length; ++i, ++j) {
				tmp[i] = components[j];
			}
			components = tmp;
		}
		else {
			components[index] = value;
		}
	}
	
	public void insert(int index, OldType value) {
		if (value instanceof OldVector) {
			OldVector vector = (OldVector) value;
			int dimension = vector.getDimension();
			OldType[] tmp = new OldType[components.length + dimension];
			for (int i = 0; i < index; ++i) {
				tmp[i] = components[i];
			}
			for (int i = index, j = 0; j < dimension; ++i, ++j) {
				tmp[i] = vector.get(j);
			}
			for (int i = index + dimension, j = index + 1; i < tmp.length; ++i, ++j) {
				tmp[i] = components[j];
			}
			components = tmp;
		}
		else {
			OldType[] tmp = new OldType[components.length + 1];
			for (int i = 0; i < index; ++i) {
				tmp[i] = components[i];
			}
			tmp[index] =  value;
			for (int i = index + 1; i < tmp.length; ++i) {
				tmp[i] = components[i - 1];
			}
			components = tmp;
		}
	}
		
	public void remove(int index) {
		OldType[] tmp = new OldType[components.length - 1];
		for (int i = 0; i < index; ++i) {
			tmp[i] = components[i];
		}
		for (int i = index; i < tmp.length; ) {
			tmp[i] = components[++i];
		}
		components = tmp;
	}

	private OldNumeric getNumeric(int index) {
		OldType tmp = this.get(index);
		if (tmp instanceof OldNumeric) {
			return (OldNumeric) tmp;
		}
		else {
			throw new UnsupportedOperationException("Attempted to perform a numeric operation on non-numeric type");
		}
	}
	
	public boolean getBit(int index) {
		return this.getNumeric(index).getBit();
	}

	public void setBit(int index, boolean value) {
		this.getNumeric(index).setBit(value);
	}

	public int getInt(int index) {
		return this.getNumeric(index).getInt();
	}

	public void setInt(int index, int value) {
		this.getNumeric(index).setInt(value);
	}

	public double getReal(int index) {
		return this.getNumeric(index).getReal();
	}

	public void setReal(int index, double value) {
		this.getNumeric(index).setReal(value);
	}
	
	private OldType[] components;

}
