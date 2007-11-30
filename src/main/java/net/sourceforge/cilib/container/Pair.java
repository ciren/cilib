/*
 * Pair.java
 * 
 * Created on Nov 15, 2005
 *
 * Copyright (C) 2003 - 2006 
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
 *
 */
package net.sourceforge.cilib.container;

import java.io.Serializable;

import net.sourceforge.cilib.util.Cloneable;

/**
 * 
 * @author Gary Pampara
 *
 * @param <K>
 * @param <V>
 */
public class Pair<K extends Comparable<K>, V> implements Comparable<Pair<K,V>>, Cloneable, Serializable {
	private static final long serialVersionUID = -1557021513377872749L;
	private K key;
	private V value;

	public Pair() {
		this.key = null;
		this.value = null;
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public Pair(K key, V value) {
		this.key = key;
		this.value = value;
	}
	
	
	/**
	 * Copy constructor
	 * @param copy
	 */
	public Pair(Pair<K, V> copy) {
		this.key = copy.key;
		this.value = copy.value;
	}
	
	
	/**
	 * 
	 */
	public Pair<K, V> getClone() {
		return new Pair<K, V>(this);
	}

	
	/**
	 * @return Returns the key.
	 */
	public K getKey() {
		return key;
	}

	
	/**
	 * @param key The key to set.
	 */
	public void setKey(K key) {
		this.key = key;
	}

	
	/**
	 * @return Returns the value.
	 */
	public V getValue() {
		return value;
	}

	
	/**
	 * @param value The value to set.
	 */
	public void setValue(V value) {
		this.value = value;
	}

	
	/**
	 * 
	 * @param o
	 * @return
	 */
	public int compareTo(Pair<K, V> o) {
		Comparable<K> t1 = this.key;
		return t1.compareTo(o.key);
	}
	
	
	/**
	 * 
	 */
	public String toString() {
		return this.getKey().toString();
	}

}