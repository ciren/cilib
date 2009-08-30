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

import net.sourceforge.cilib.util.Cloneable;

/**
 * Simple structure to associate a key to a value.
 *
 * @author Gary Pampara
 *
 * @param <K> The key value.
 * @param <V> The Object to associate to the key.
 */
public class Pair<K extends Comparable<? super K>, V> implements Comparable<Pair<K, V>>, Cloneable {
    private static final long serialVersionUID = -1557021513377872749L;
    private K key;
    private V value;

    /**
     * Default constructor. Assigns both the key and the value to {@code null}.
     */
    public Pair() {
        this.key = null;
        this.value = null;
    }

    /**
     * Construct a {@linkplain Pair} with the given key and value.
     * @param key The key value to set.
     * @param value The object to associate with this key.
     */
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }


    /**
     * Copy constructor.
     * @param copy The object to copy.
     */
    public Pair(Pair<K, V> copy) {
        this.key = copy.key;
        this.value = copy.value;
    }


    /**
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     * @param o The {@linkplain Pair} to perform the comparison with.
     * @return The result of the comparison.
     */
    @Override
    public int compareTo(Pair<K, V> o) {
        K t1 = this.key;
        return t1.compareTo(o.key);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.getKey().toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;

        if (getClass() != obj.getClass()) return false;
        final Pair other = (Pair) obj;

        if (key == null) {
            if (other.key != null)
                return false;
        }
        else if (!key.equals(other.key))
            return false;

        if (value == null) {
            if (other.value != null)
                return false;
        }
        else if (!value.equals(other.value))
            return false;

        return true;
    }

}
