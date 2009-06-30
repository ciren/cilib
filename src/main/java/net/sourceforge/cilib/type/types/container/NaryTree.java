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
import java.util.List;

import net.sourceforge.cilib.util.Cloneable;

/**
 * Implementation of a Nary Tree data structure.
 *
 * @param <E> The type that this {@linkplain Tree} instance is defined to contain.
 */
public class NaryTree<E extends Comparable<? super E> & Cloneable> extends AbstractTree<E> {
    private static final long serialVersionUID = -1136444941205621381L;

    private int degree;
    private List<NaryTree<E>> subTrees;

    /**
     * Create an empty {@linkplain NaryTree} instance.
     */
    public NaryTree() {
        this.key = null;
        this.subTrees = new ArrayList<NaryTree<E>>();
    }

    /**
     * Create an {@linkplain NaryTree} instance with the specified {@code degree}.
     * @param degree The specified degree for the {@linkplain NaryTree}.
     */
    public NaryTree(int degree) {
        this.key = null;
        this.degree = degree;
        this.subTrees = new ArrayList<NaryTree<E>>();
    }

    /**
     * Create a {@linkplain NaryTree} instance, with the given degree and key value.
     * @param degree The specified degree for the {@linkplain NaryTree}.
     * @param element The key to be maintained by this {@linkplain NaryTree}.
     */
    public NaryTree(int degree, E element) {
        this.key = element;
        this.degree = degree;
        this.subTrees = new ArrayList<NaryTree<E>>();
        for (int i = 0; i < degree; i++)
            this.subTrees.add(new NaryTree<E>(degree));
    }

    /**
     * Copy constructor. Make a copy of the provided instance. This copy is a deep copy
     * of the provided instance.
     * @param copy The instance to copy.
     */
    @SuppressWarnings("unchecked")
    public NaryTree(NaryTree<E> copy) {
        this(copy.degree);
        this.key = (E) copy.key.getClone();
        for (NaryTree<E> subTree : copy.subTrees) {
            this.subTrees.add(subTree.getClone());
        }
    }

    /**
     * {@inheritDoc}
     */
    public NaryTree<E> getClone() {
        return new NaryTree<E>(this);
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

        NaryTree<?> other = (NaryTree<?>) obj;

        return this.key.equals(other.key) && this.subTrees.equals(other.subTrees);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (this.key == null ? 0 : this.key.hashCode());
        hash = 31 * hash + (this.subTrees == null ? 0 : this.subTrees.hashCode());
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    public boolean addSubTree(Tree<E> subTree) {
        if (isEmpty())
            throw new UnsupportedOperationException();

        for (int i = 0; i < degree; i++) {
            if (!subTrees.get(i).isEmpty())
                continue;

            subTrees.set(i, (NaryTree<E>) subTree);
            return true;
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    public Tree<E> getSubTree(E element) {
        for (int i = 0; i < degree; i++) {
            NaryTree<E> subTree = subTrees.get(i);
            if (subTree.isEmpty())
                continue;

            if (subTree.getKey().equals(element))
                return subTree;
        }

        return new NaryTree<E>();
    }

    /**
     * {@inheritDoc}
     */
    public Tree<E> removeSubTree(E element) {
        if (isEmpty())
            throw new UnsupportedOperationException();

        Tree<E> subTree = getSubTree(element);
        int index = subTrees.indexOf(subTree);
        subTrees.remove(subTree);
        subTrees.add(index, new NaryTree<E>(degree));
        return subTree;
    }

    /**
     * {@inheritDoc}
     */
    public Tree<E> removeSubTree(int index) {
        Tree<E> subTree = getSubTree(index);
        return removeSubTree(subTree.getKey());
    }

    /**
     * {@inheritDoc}
     */
    public boolean add(E element) {
        NaryTree<E> tree = new NaryTree<E>(degree, element);
        return this.addSubTree(tree);
    }

    /**
     * {@inheritDoc}
     */
    public void clear() {
        throw new UnsupportedOperationException("Implementation needed");
    }

    /**
     * {@inheritDoc}
     */
    public boolean contains(E element) {
        for (int i = 0; i < degree; i++) {
            Tree<E> subTree = getSubTree(i);
            if (!subTree.isEmpty() && subTree.getKey().equals(element))
                return true;
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    public boolean remove(E element) {
        throw new UnsupportedOperationException("Implementation needed");
    }

    /**
     * {@inheritDoc}
     */
    public E remove(int index) {
        throw new UnsupportedOperationException("Implementation needed");
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        throw new UnsupportedOperationException("Implementation needed");
    }

    /**
     * {@inheritDoc}
     */
    public Tree<E> getSubTree(int index) {
        if (isEmpty())
            throw new UnsupportedOperationException();

        return this.subTrees.get(index);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isLeaf() {
        for (int i = 0; i < degree; i++)
            if (!subTrees.get(i).isEmpty())
                return false;

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setKey(E element) {
        if (!isEmpty())
            throw new UnsupportedOperationException();

        this.key = element;
        this.subTrees = new ArrayList<NaryTree<E>>();
        for (int i = 0; i < degree; i++) {
            this.subTrees.add(new NaryTree<E>(degree));
        }
    }

    /**
     * {@inheritDoc}
     */
    public int getDegree() {
        return this.degree;
    }

}
