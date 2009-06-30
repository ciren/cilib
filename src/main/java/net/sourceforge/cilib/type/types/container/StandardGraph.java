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
package net.sourceforge.cilib.type.types.container;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import net.sourceforge.cilib.container.Pair;
import net.sourceforge.cilib.container.visitor.Visitor;
import net.sourceforge.cilib.math.random.generator.Random;

/**
 * TODO: Complete this javadoc.
 *
 * @param <E> The {@code Comparable} type.
 */
public class StandardGraph<E extends Comparable<E>> implements Graph<E> {

    private static final long serialVersionUID = -5517089079342858152L;
    private Map<E, List<Entry<E>>> adjacencyMap;

    public StandardGraph() {
        adjacencyMap = new LinkedHashMap<E, List<Entry<E>>>();
    }

    public StandardGraph(StandardGraph<E> copy) {
        this.adjacencyMap = new LinkedHashMap<E, List<Entry<E>>>();

        for (E element : copy.adjacencyMap.keySet()) {
            List<Entry<E>> connections = copy.adjacencyMap.get(element);
            List<Entry<E>> clonedconnections = new ArrayList<Entry<E>>();
            
            for (Entry<E> entry : connections) {
                clonedconnections.add(entry.getClone());
            }

            this.adjacencyMap.put(element, clonedconnections);
        }
    }

    /**
     * {@inheritDoc}
     */
    public StandardGraph<E> getClone() {
        return new StandardGraph(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if ((obj == null) || (this.getClass() != obj.getClass()))
            return false;

        StandardGraph<E> graph = (StandardGraph<E>) obj;
        if (this.adjacencyMap.size() != graph.adjacencyMap.size()) return false;
        if (this.edgeCount() != graph.edgeCount()) return false;

        if (!adjacencyMap.keySet().containsAll(graph.adjacencyMap.keySet()))
                return false;

        // Set up the edge sets.
        Set<Pair<E, E>> currentEdgeSet = this.getEdgeSet();
        Set<Pair<E, E>> otherEdgeSet = graph.getEdgeSet();

        if (!otherEdgeSet.containsAll(currentEdgeSet))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (this.adjacencyMap == null ? 0 : this.adjacencyMap.hashCode());
        return hash;
    }

    /**
     * Get the number of edges contained within the {@linkplain Graph}. The number of edges is defined
     * as all edges emanating from any given vertex within the structure.
     * @return The number of edges contained within the structure.
     */
    public int edgeCount() {
        int count = 0;

        Collection<List<Entry<E>>> edgeLists = this.adjacencyMap.values();
        for (List<Entry<E>> list : edgeLists) {
            count += list.size();
        }

        return count;
    }

    /**
     * Get the number of verticies contained within the structure.
     * @return The number of vertexes within the structure.
     */
    public int vertices() {
        return this.adjacencyMap.size();
    }

    /**
     * Add a connecting edge between the provided two distinct vertexes. The cost for the connection
     * is defined to be a value of <code>1.0</code>.
     * @param a The first vertex.
     * @param b The second vertex.
     * @return <code>true</code> if successful, <code>false</code> otherwise.
     */
    public boolean addEdge(E a, E b) {
        return addEdge(a, b, 1.0);
    }

    /**
     * Add a connecting edge between the provided two distinct vertexes, given the provided
     * <code>cost</code> for the connection. The <code>weight</code> associated with this
     * connection is defined to be a value of <code>1.0</code>.
     * @param a The first vertex.
     * @param b The second vertex.
     * @param cost The cost associated with the connection.
     * @return <code>true</code> if successful, <code>false</code> otherwise.
     */
    public boolean addEdge(E a, E b, double cost) {
        return addEdge(a, b, cost, 1.0);
    }

    /**
     * Add a connecting edge between the provided two distinct vertexes, given the provided
     * <code>cost</code> and <code>weight</code> for the connection.
     * @param a The first vertex.
     * @param b The second vertex.
     * @param cost The cost associated with the connection.
     * @param weight The weight associted with the connection.
     * @return <code>true</code> if successful, <code>false</code> otherwise.
     */
    public boolean addEdge(E a, E b, double cost, double weight) {
        if (!contains(a)) return false;
        if (!contains(b)) return false;
        if (a == b) return false;

        List<Entry<E>> connectedVerticies = this.adjacencyMap.get(a);
        connectedVerticies.add(new Entry<E>(b, cost, weight));

        return true;
    }

    /**
     * Determine if the provided vertex objects are connected.
     * @param a The first vertex.
     * @param b The second vertex.
     * @return <code>true</code> if vertex <code>a</code> and <code>b</code> are connected.
     *         <code>false</code> otherwise.
     */
    public boolean isConnected(E a, E b) {
        if (!contains(a)) return false;
        if (!contains(b)) return false;
        if (a == b) return false;

        List<Entry<E>> connectedVerticies = this.adjacencyMap.get(a);

        for (Entry<E> pair : connectedVerticies) {
            if (pair.getElement().equals(b))
                return true;
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    public void accept(Visitor<E> visitor) {
        throw new UnsupportedOperationException("accept() for the class " + getClass().getName() + " still needs an implementation");
    }

    /**
     * {@inheritDoc}
     */
    public boolean add(E element) {
        if (this.adjacencyMap.containsKey(element))
            return false;

        this.adjacencyMap.put(element, new LinkedList<Entry<E>>());
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public void clear() {
        this.adjacencyMap.clear();
    }

    /**
     * {@inheritDoc}
     */
    public boolean contains(E element) {
        return this.adjacencyMap.containsKey(element);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isEmpty() {
        return this.adjacencyMap.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    public Iterator<E> iterator() {
        return this.adjacencyMap.keySet().iterator();
    }

    /**
     * {@inheritDoc}
     */
    public boolean remove(E element) {
        if (!this.adjacencyMap.containsKey(element))
            return false;

        this.adjacencyMap.remove(element);

        Collection<List<Entry<E>>> lists = this.adjacencyMap.values();

        for (List<Entry<E>> list : lists) {
            if (list.contains(element))
                list.remove(element);
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    public E remove(int index) {
        int count = 0;
        for (Map.Entry<E, List<Entry<E>>> e : this.adjacencyMap.entrySet()) {
            if (count == index) {
                this.adjacencyMap.remove(e.getKey());
                return e.getKey();
            }

            count++;
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    public int size() {
        return this.adjacencyMap.size();
    }

    /**
     * {@inheritDoc}
     */
    public boolean addAll(StructuredType<? extends E> structure) {
        for (E element : structure)
            add(element);

        return true;
    }

    /**
     * {@inheritDoc}
     */
    public boolean removeAll(StructuredType<E> structure) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Determine the distance between the provided vertex objects. The distance is defined to the be
     * total cost to get from vertex <code>a</code> to vertex <code>b</code>. The distance is only defined
     * for vertex objects that are directly connected. Indirect connections need to be traversed by
     * a traversal algorithm that determines the distance for the shortest tour or similar criterion.
     * @param a The first vertex.
     * @param b The second vertex.
     * @return The total cost from <code>a</code> to <code>b</code>.
     */
    public double distance(E a, E b) {
        if (!isConnected(a, b))
            throw new UnsupportedOperationException("Cannot determine the distance. Node(" + a + ") and Node(" + b + ") are not connected");

        List<Entry<E>> distances = this.adjacencyMap.get(a);
        for (Entry<E> pair : distances) {
            if (pair.getElement().equals(b))
                return pair.getCost();
        }

        throw new NoSuchElementException("The distance between Node(" + a + ") and Node(" + b + ") does not exist");
    }

    /**
     * {@inheritDoc}
     */
    public E getVertex(int index) {
        Set<E> keySet = this.adjacencyMap.keySet();
        int count  = 0;

        for (E element : keySet) {
            if (count == index) return element;
            count++;
        }

        return null;
    }


    private Set<Pair<E, E>> getEdgeSet() {
        Set<Pair<E, E>> edgeSet = new HashSet<Pair<E, E>>();

        for (E vertex : adjacencyMap.keySet()) {
            List<Entry<E>> connections = adjacencyMap.get(vertex);
            for (Entry<E> entry : connections)
                edgeSet.add(new Pair<E, E>(vertex, entry.getElement()));
        }

        return edgeSet;
    }

    @Override
    public void randomize(Random random) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    /**
     * Class to represent the element, cost and weight associated to the connection between
     * two distinct vertex objects.
     *
     * @param <T> The {@linkplain Comparable} type.
     */
    private class Entry<T extends Comparable<T>> implements net.sourceforge.cilib.util.Cloneable {
        private static final long serialVersionUID = 1697479517382450802L;
        private double weight;
        private double cost;
        private T element;

        public Entry(T element, Double cost, Double weight) {
            this.element = element;
            this.cost = cost;
            this.weight = weight;
        }

        public Entry(Entry<T> copy) {
            this.weight = copy.weight;
            this.cost = copy.cost;
            this.element = copy.element;
        }

        public Double getWeight() {
            return weight;
        }

        public void setWeight(Double weight) {
            this.weight = weight;
        }

        public Double getCost() {
            return cost;
        }

        public void setCost(Double cost) {
            this.cost = cost;
        }

        public T getElement() {
            return element;
        }

        public void setElement(T element) {
            this.element = element;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;

            if ((obj == null) || (this.getClass() != obj.getClass()))
                return false;

            Entry<?> other = (Entry<?>) obj;
            return this.element.equals(other) &&
                (this.cost == other.cost) &&
                (this.weight == other.weight);
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 31 * hash + (element == null ? 0 : element.hashCode());
            hash = 31 * hash + Double.valueOf(this.cost).hashCode();
            hash = 31 * hash + Double.valueOf(this.weight).hashCode();
            return hash;
        }

        @Override
        public Entry<T> getClone() {
            return new Entry(this);
        }
    }

}
