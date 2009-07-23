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

    private Map<E, List<Graph.Edge<E>>> adjacencyMap;

    public StandardGraph() {
        adjacencyMap = new LinkedHashMap<E, List<Graph.Edge<E>>>();
    }

    public StandardGraph(StandardGraph<E> copy) {
        this.adjacencyMap = new LinkedHashMap<E, List<Graph.Edge<E>>>();

        for (E element : copy.adjacencyMap.keySet()) {
            List<Graph.Edge<E>> connections = copy.adjacencyMap.get(element);
            List<Graph.Edge<E>> clonedconnections = new ArrayList<Graph.Edge<E>>();

            for (Graph.Edge<E> entry : connections) {
                Edge e = (Edge) entry;
                clonedconnections.add(new Edge<E>(e));
            }

            this.adjacencyMap.put(element, clonedconnections);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
    @Override
    public int edgeCount() {
        int count = 0;

        Collection<List<Graph.Edge<E>>> edgeLists = this.adjacencyMap.values();
        for (List<Graph.Edge<E>> list : edgeLists) {
            count += list.size();
        }

        return count;
    }

    /**
     * Get the number of verticies contained within the structure.
     * @return The number of vertexes within the structure.
     */
    @Override
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
    @Override
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
    @Override
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
    @Override
    public boolean addEdge(E a, E b, double cost, double weight) {
        if (!contains(a)) return false;
        if (!contains(b)) return false;
        if (a == b) return false;

        List<Graph.Edge<E>> connectedVerticies = this.adjacencyMap.get(a);
        connectedVerticies.add(new Edge<E>(b, cost, weight));

        return true;
    }

    /**
     * Determine if the provided vertex objects are connected.
     * @param a The first vertex.
     * @param b The second vertex.
     * @return <code>true</code> if vertex <code>a</code> and <code>b</code> are connected.
     *         <code>false</code> otherwise.
     */
    @Override
    public boolean isConnected(E a, E b) {
        if (!contains(a)) return false;
        if (!contains(b)) return false;
        if (a == b) return false;

        List<Graph.Edge<E>> connectedVerticies = this.adjacencyMap.get(a);

        for (Graph.Edge<E> pair : connectedVerticies) {
            if (pair.getConnectedVertex().equals(b))
                return true;
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(Visitor<E> visitor) {
        throw new UnsupportedOperationException("accept() for the class " + getClass().getName() + " still needs an implementation");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean add(E element) {
        if (this.adjacencyMap.containsKey(element))
            return false;

        this.adjacencyMap.put(element, new LinkedList<Graph.Edge<E>>());
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        this.adjacencyMap.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(E element) {
        return this.adjacencyMap.containsKey(element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return this.adjacencyMap.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<E> iterator() {
        return this.adjacencyMap.keySet().iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean remove(E element) {
        if (!this.adjacencyMap.containsKey(element))
            return false;

        this.adjacencyMap.remove(element);

        Collection<List<Graph.Edge<E>>> lists = this.adjacencyMap.values();

        for (List<Graph.Edge<E>> list : lists) {
            for (Graph.Edge<E> entry : list) {
                if (entry.getConnectedVertex().equals(element))
                    list.remove(entry);
            }
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E remove(int index) {
        int count = 0;
        for (Map.Entry<E, List<Graph.Edge<E>>> e : this.adjacencyMap.entrySet()) {
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
    @Override
    public int size() {
        return this.adjacencyMap.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addAll(StructuredType<? extends E> structure) {
        for (E element : structure)
            add(element);

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
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

        List<Graph.Edge<E>> distances = this.adjacencyMap.get(a);
        for (Graph.Edge<E> pair : distances) {
            if (pair.getConnectedVertex().equals(b))
                return pair.getCost();
        }

        throw new NoSuchElementException("The distance between Node(" + a + ") and Node(" + b + ") does not exist");
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
            List<Graph.Edge<E>> connections = adjacencyMap.get(vertex);
            for (Graph.Edge<E> edge : connections)
                edgeSet.add(new Pair<E, E>(vertex, edge.getConnectedVertex()));
        }

        return edgeSet;
    }

    @Override
    public void randomize(Random random) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Graph.Edge<E>> edgesOf(E vertex) {
        return this.adjacencyMap.get(vertex);
    }


    /**
     * Class to represent the element, cost and weight associated to the connection between
     * two distinct vertex objects.
     *
     * @param <E> The {@linkplain Comparable} type.
     */
    public static class Edge<E extends Comparable<E>> implements Graph.Edge<E> {
        private static final long serialVersionUID = 1697479517382450802L;
        private double weight;
        private double cost;
        private E vertex;

        /**
         * Create a new {@code Edge}.
         * @param element The vertex to connect to.
         * @param cost The cost of the edge.
         * @param weight The weight of the edge.
         */
        private Edge(E vertex, Double cost, Double weight) {
            this.vertex = vertex;
            this.cost = cost;
            this.weight = weight;
        }

        /**
         * Copy constructor. Create a copy of the provided instance.
         * @param copy The instance to copy.
         */
        private Edge(Edge<E> copy) {
            this.weight = copy.weight;
            this.cost = copy.cost;
            this.vertex = copy.vertex;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Double getWeight() {
            return weight;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setWeight(Double weight) {
            this.weight = weight;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Double getCost() {
            return cost;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setCost(Double cost) {
            this.cost = cost;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public E getConnectedVertex() {
            return vertex;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setConnectedVertex(E element) {
            this.vertex = element;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;

            if ((obj == null) || (this.getClass() != obj.getClass()))
                return false;

            Edge<?> other = (Edge<?>) obj;
            return this.vertex.equals(other) &&
                (this.cost == other.cost) &&
                (this.weight == other.weight);
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 31 * hash + (vertex == null ? 0 : vertex.hashCode());
            hash = 31 * hash + Double.valueOf(this.cost).hashCode();
            hash = 31 * hash + Double.valueOf(this.weight).hashCode();
            return hash;
        }
    }
}
