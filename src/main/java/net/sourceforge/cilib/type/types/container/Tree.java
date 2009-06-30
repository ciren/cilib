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

import net.sourceforge.cilib.container.visitor.PrePostVisitor;
import net.sourceforge.cilib.container.visitor.Visitor;
import net.sourceforge.cilib.util.Cloneable;

/**
 * Definition of the {@linkplain Tree} type.
 * @param <E> the {@linkplain Cloneable} type.
 */
public interface Tree<E extends Cloneable> extends StructuredType<E> {

    /**
     * {@inheritDoc}
     */
    public Tree<E> getClone();

    /**
     * Get the key value maintained by this {@linkplain Tree}.
     * @return The key of the current {@linkplain Tree}.
     */
    public E getKey();

    /**
     * Set the current key for the {@linkplain Tree}.
     * @param element The {@linkplain E} to be used as the key.
     */
    public void setKey(E element);

    /**
     * Add a subtree to the current {@linkplain Tree} instance.
     * @param subTree The {@linkplain Tree} instance to add as a child tree.
     * @return <code>true</code> if the subtree was added, <code>false</code> otherwise.
     */
    public boolean addSubTree(Tree<E> subTree);

    /**
     * Get the subtree with the specified key value.
     * @param element The value of the key to lookup.
     * @return A reference to the found {@linkplain Tree}, otherwise {@code null}.
     */
    public Tree<E> getSubTree(E element);

    /**
     * Return the subTree with the node value of <tt>element</tt>. If
     * such an subTree does not exist, an empty tree is returned.
     *
     * @param element The element of the subTree to search for.
     * @return The subtree if found else an empty <tt>Tree</tt> object.
     */
    public Tree<E> getSubTree(int index);

    /**
     * Remove the subtree with the given key value.
     * @param element The value of the key of the subtree to be removed.
     * @return The instance that was removed, otherwise {@code null}.
     */
    public Tree<E> removeSubTree(E element);

    /**
     * Remove the subtree at the specified index within the current {@linkplain Tree}.
     * @param index The index of the {@linkplain Tree} to be removed.
     * @return The instance that was removed, otherwise {@code null}.
     */
    public Tree<E> removeSubTree(int index);

    /**
     * Determine if the current {@linkplain Tree} node is a leaf node in the current
     * structure.
     * @return <code>true</code> if it is a leaf node, <code>false</code> otherwise.
     */
    public boolean isLeaf();

    /**
     * Get the degree of the current {@linkplain Tree}. The degree is an indication of
     * the {@linkplain Tree}s branching factor and therefore the maximum number of
     * children that it may contain.
     * @return The branching factor / maximum allowable children.
     */
    public int getDegree();

    /**
     * Perform a depth first traversal of the current {@linkplain Tree} node, executing
     * the operation stored within the provided {@linkplain Visitor} instance.
     * @param visitor The visitor operation to execute at each node.
     */
    public void depthFirstTraversal(PrePostVisitor<E> visitor);

    /**
     * Perform a breadth first traversal of the current {@linkplain Tree} node, executing
     * the operation stored within the provided {@linkplain Visitor} instance.
     * @param visitor The visitor operation to execute at each node.
     */
    public void breadthFirstTraversal(Visitor<Tree<E>> visitor);

}
