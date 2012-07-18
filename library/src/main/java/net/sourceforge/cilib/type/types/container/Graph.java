/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.type.types.container;

import java.util.List;

/**
 * Definition of Graph operations.
 *
 * @param <E> The type of the {@linkplain Graph}.
 */
public interface Graph<E> extends StructuredType<E> {

    /**
     * Determine the number of vertices contained within the current <tt>Graph</tt>
     * structure.
     * @return The number of contained vertices.
     */
    int vertices();

    /**
     * Determine the number of edges contained within the current <tt>Graph</tt>
     * structure.
     * @return The number of contained edges.
     */
    int edgeCount();

    List<Graph.Edge<E>> edgesOf(E vertex);

    /**
     * Create an edge / link between the given two vertices.
     * @param a The vertex the edge should eminate from.
     * @param b The vertex the edge should be incident to.
     * @return <tt>true</tt> if the edge was created, <tt>false</tt> otherwise.
     */
    boolean addEdge(E a, E b);

    /**
     * Create an edge / link between the given two vertices.
     * @param a The vertex the edge should eminate from.
     * @param b The vertex the edge should be incident to.
     * @param cost The cost associated with this edge.
     * @return <tt>true</tt> if the edge was created, <tt>false</tt> otherwise.
     */
    boolean addEdge(E a, E b, double cost);

    /**
     * Create an edge / link between the given two vertices.
     * @param a The vertex the edge should eminate from.
     * @param b The vertex the edge should be incident to.
     * @param cost The cost associated with this edge.
     * @param weight The weight value associated with this edge.
     * @return <tt>true</tt> if the edge was created, <tt>false</tt> otherwise.
     */
    boolean addEdge(E a, E b, double cost, double weight);

    /**
     * Determine if <tt>a</tt> and <tt>b</tt> are connected by an edge.
     * @param a The vertex the edge should be emanating from.
     * @param b The vertex the edge should be incident to.
     * @return <tt>true</tt> if an edge is defined, <tt>false</tt> otherwise.
     */
    boolean isConnected(E a, E b);

    /**
     * Get the vertex at the provided <code>index</code>.
     * @param index The index of the vertex.
     * @return The value of the vertex at <code>index</code>.
     */
    E getVertex(int index);

    /**
     * Definition of an edge within a {@code Graph} structure.
     * @param <E> The edge type.
     */
    public interface Edge<E> {

        /**
         * Obtain the cost associated with the {@code Edge}.
         * @return The edge cost.
         */
        Double getCost();

        /**
         * Obtain the {@code Vertex} to which this edge is connected.
         * @return The "other" connected vertex.
         */
        E getConnectedVertex();

        /**
         * Obtain the weight value associated with this {@code Edge}.
         * @return The associated weight.
         */
        Double getWeight();

        /**
         * Set the cost value for this {@code Edge}.
         * @param cost The value to set.
         */
        void setCost(Double cost);

        /**
         * Set the vertex to which the current edge is connected.
         * @param element The element to set.
         */
        void setConnectedVertex(E element);

        /**
         * Set the weight value for this {@code Edge}.
         * @param weight The value to set.
         */
        void setWeight(Double weight);

    }

}
