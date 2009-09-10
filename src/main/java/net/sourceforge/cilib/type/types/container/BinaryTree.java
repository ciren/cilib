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
import net.sourceforge.cilib.util.Cloneable;

/**
 * Implementation of a {@literal BinaryTree}.
 *
 * @param <E> The {@linkplain Comparable} and {@linkplain Cloneable} type.
 */
public class BinaryTree<E extends Comparable<? super E> & Cloneable> extends AbstractTree<E> {
    private static final long serialVersionUID = 3537717751647961525L;

    private BinaryTree<E> left;
    private BinaryTree<E> right;

    /**
     * Create a new instance of {@literal BinaryTree}.
     */
    public BinaryTree() {
        this(null, null, null);
    }

    /**
     * Create a new instance of {@literal BinaryTree} with the provided parameter as the value of
     * the key.
     * @param element The value of the key that the {@literal BinaryTree} should maintain.
     */
    public BinaryTree(E element) {
        this(element, new BinaryTree<E>(), new BinaryTree<E>());
    }

    /**
     * Create an instance of {@linkplain BinaryTree} with the provided tree instances as the
     * {@literal left} and {@literal right} children, maintaining the provided {@literal key} value.
     * @param key The value of the tree to maintain.
     * @param left The left child.
     * @param right The right child.
     */
    public BinaryTree(E key, BinaryTree<E> left, BinaryTree<E> right) {
        this.key = key;
        this.left = left;
        this.right = right;
    }

    /**
     * Copy constructor. Create a copy of the provided instance. This copy is a deep copy of the
     * provided instance.
     * @param copy The instance to copy.
     */
    @SuppressWarnings("unchecked")
    public BinaryTree(BinaryTree<E> copy) {
        this.key = (E) copy.key.getClone();
        this.left = copy.left.getClone();
        this.right = copy.right.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BinaryTree<E> getClone() {
        return new BinaryTree<E>(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if ((obj == null) || (this.getClass() != obj.getClass()))
            return false;

        BinaryTree<?> other = (BinaryTree<?>) obj;

        return this.key.equals(other.key) &&
            this.left.equals(other.left) &&
            this.right.equals(other.right);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (this.key == null ? 0 : this.key.hashCode());
        hash = 31 * hash + (this.left == null ? 0 : this.left.hashCode());
        hash = 31 * hash + (this.right == null ? 0 : this.right.hashCode());
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Add the provided subtree to the current Tree.
     * Addition is by default first the left branch subtree. If the
     * left branch already has a defined tree attached, the right
     * subtree is assigned.
     *
     * @param subTree The {@linkplain} to add.
     * @return <tt>true</tt> if the addition was successful, <tt>false</tt> otherwise
     */
    @Override
    public boolean addSubTree(Tree<E> subTree) {
        if (isEmpty())
            throw new UnsupportedOperationException("Cannot add a subtree to an empty tree");

        BinaryTree<E> child = (BinaryTree<E>) subTree;

        if (left.isEmpty()) {
            left = child;
            return true;
        }
        if (right.isEmpty()) {
            right = child;
            return true;
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    public Tree<E> getSubTree(E element) {
        if (isEmpty())
            throw new UnsupportedOperationException("Cannot get a subtree from an empty tree");

        if (!left.isEmpty() && left.getKey().equals(element)) return left;
        if (!right.isEmpty() && right.getKey().equals(element)) return right;

        return new BinaryTree<E>();
    }

    /**
     * {@inheritDoc}
     */
    public Tree<E> getSubTree(int index) {
        if (index < 0 || index >= 2)
            throw new IndexOutOfBoundsException("BinaryTree subTree indexes of 0 or 1 are ony allowed.");

        if (index == 0) return left;

        return right;
    }

    /**
     * {@inheritDoc}
     */
    public Tree<E> removeSubTree(E element) {
        AbstractTree<E> subTreeFound = (AbstractTree<E>) this.getSubTree(element);

        if (subTreeFound == left) left = new BinaryTree<E>();
        if (subTreeFound == right) right = new BinaryTree<E>();

        return subTreeFound;
    }

    /**
     * {@inheritDoc}
     */
    public Tree<E> removeSubTree(int index) {
        AbstractTree<E> found = (AbstractTree<E>) this.getSubTree(index);
        return this.removeSubTree(found.getKey());
    }

    /**
     * Convenience method. Defers to {@see BinaryTree#addSubtree(Tree)}.
     * @param element The element to add to the current tree.
     * @return The result of the addition from {@see BinaryTree#addSubTree(Tree)}.
     */
    public boolean add(E element) {
        return this.addSubTree(new BinaryTree<E>(element));
    }

    /**
     * {@inheritDoc}
     */
    public void clear() {
        this.key = null;
        this.left = null;
        this.right = null;
    }

    /**
     * {@inheritDoc}
     */
    public boolean contains(E element) {
        return this.getSubTree(element).isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    public boolean remove(E element) {
        return !this.removeSubTree(element).isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    public E remove(int index) {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void depthFirstTraversal(PrePostVisitor<E> visitor) {
        if (!isEmpty()) {
            visitor.preVisit(getKey());
            left.depthFirstTraversal(visitor);
            visitor.visit(getKey());
            right.depthFirstTraversal(visitor);
            visitor.postVisit(getKey());
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean isLeaf() {
        return left.isEmpty() && right.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    public int getDegree() {
        return 2;
    }

}
