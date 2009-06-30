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
package net.sourceforge.cilib.type.types;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Simple <tt>Blackboard</tt> implementation.
 *
 * @param <K> The key type.
 * @param <V> The value type.
 */
public class Blackboard<K, V extends Type> implements Type {
    private static final long serialVersionUID = -2589625146223946484L;
    private ConcurrentMap<K, V> board;

    /**
     * Create a new empty {@code Blackboard} container.
     */
    public Blackboard() {
        this.board = new ConcurrentHashMap<K, V>();
    }

    /**
     * Copy constructor. Create a copy of the provided instance.
     * @param copy The isntance to copy.
     */
    public Blackboard(Blackboard<K, V> copy) {
        for (Map.Entry<K, V> entry : copy.board.entrySet()) {
            K key = entry.getKey();
            @SuppressWarnings({"unchecked"}) V value = (V) entry.getValue().getClone();
            this.board.put(key, value);
        }
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public Blackboard<K, V> getClone() {
        return new Blackboard<K, V>(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if ((obj == null) || (this.getClass() != obj.getClass()))
            return false;

        Blackboard<?, ?> other = (Blackboard<?, ?>) obj;
        return this.board.equals(other.board);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (this.board == null ? 0 : this.board.hashCode());
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    public void reset() {
        this.board.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.board.toString();
    }

    /**
     * Put the provided key / value pair into the {@code Blackboard}.
     * @param key The key value for the pair.
     * @param value The value associated with the key.
     * @return The provided value.
     */
    public V put(K key, V value) {
        return this.board.put(key, value);
    }

    /**
     * Get the value associated with the provided key, {@code null} otherwise.
     * @param key The key to obtained the value of.
     * @return The associated value to the key.
     */
    public V get(K key) {
        return this.board.get(key);
    }

    /**
     * Obtain a {@code Set} of key / value pairs.
     * @return The set of values.
     */
    public Set<Map.Entry<K, V>> entrySet() {
        return this.board.entrySet();
    }

}
