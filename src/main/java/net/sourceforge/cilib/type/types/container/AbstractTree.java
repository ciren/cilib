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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import net.sourceforge.cilib.container.visitor.PreOrderVisitorDecorator;
import net.sourceforge.cilib.container.visitor.PrePostVisitor;
import net.sourceforge.cilib.container.visitor.Visitor;
import net.sourceforge.cilib.math.random.generator.Random;
import net.sourceforge.cilib.util.Cloneable;

/**
 * Base class for all {@linkplain Tree} objects. Contains the methods common to
 * all {@linkplain Tree} objects.
 *
 * @param <E> The {@linkplain Cloneable} type.
 */
public abstract class AbstractTree<E extends Cloneable & Comparable<? super E>> implements Tree<E> {
    private static final long serialVersionUID = 9196740766045092902L;
    protected E key;

    /**
     * {@inheritDoc}
     */
    public abstract AbstractTree<E> getClone();

    /**
     * {@inheritDoc}
     */
    public abstract boolean equals(Object obj);

    /**
     * {@inheritDoc}
     */
    public abstract int hashCode();

    /**
     * {@inheritDoc}
     */
    public void breadthFirstTraversal(Visitor<Tree<E>> visitor) {
        Queue<Tree<E>> queue = new LinkedList<Tree<E>>();

        if (!isEmpty())
            queue.add(this);

        while (!queue.isEmpty() && !visitor.isDone()) {
            Tree<E> head = queue.remove();
            visitor.visit(head);
            for (int i = 0; i < head.getDegree(); i++) {
                Tree<E> child = head.getSubTree(i);
                if (!child.isEmpty())
                    queue.add(child);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void depthFirstTraversal(PrePostVisitor<E> visitor) {
        if (visitor.isDone())
            return;

        if (!isEmpty()) {
            visitor.preVisit(getKey());
            for (int i = 0; i < this.size(); i++)
                this.getSubTree(i).depthFirstTraversal(visitor);

            visitor.postVisit(getKey());
        }
    }

    /**
     * {@inheritDoc}
     */
    public void accept(Visitor<E> visitor) {
        depthFirstTraversal(new PreOrderVisitorDecorator<E>(visitor));
    }

    /**
     * {@inheritDoc}
     */
    public E getKey() {
        if (isEmpty())
            throw new UnsupportedOperationException("Empty trees do not have valid keys");

        return this.key;
    }

    /**
     * {@inheritDoc}
     */
    public void setKey(E element) {
        this.key = element;
    }

    /**
     * Determine if the current {@linkplain Tree} is empty. A {@linkplain Tree} is only
     * defined to be empty if the key value is <code>null</code>. ie: <code>getKey() == null</code>.
     * @return <code>true</code> if the {@linkplain Tree} is empty, else <code>false</code>.
     */
    public boolean isEmpty() {
        return this.key == null;
    }

    /**
     * {@inheritDoc}
     */
    public boolean addAll(StructuredType<? extends E> structure) {
        throw new UnsupportedOperationException("Implementation needed");
    }

    /**
     * {@inheritDoc}
     */
    public boolean removeAll(StructuredType<E> structure) {
        throw new UnsupportedOperationException("Implementation needed");
    }

    /**
     * {@inheritDoc}
     */
    public Iterator<E> iterator() {
        return (Iterator<E>) new TreeIterator();
    }

    /**
     * {@inheritDoc}
     */
    public int size() {
        return this.getDegree();
    }

    /**
     * {@inheritDoc}
     */
    public String getRepresentation() {
        StringBuilder buffer = new StringBuilder();
        PrintingVisitor<E> visitor = new PrintingVisitor<E>(buffer);
        this.accept(visitor);

        return buffer.toString();
    }

    @Override
    public void randomize(Random random) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * {@linkplain PrePostVisitor} to simply print {@linkplain Tree} instances out
     * in a flaw textual representation.
     * @param <E> The type of the node to be visited.
     */
    @SuppressWarnings("hiding")
    private class PrintingVisitor<E> extends PrePostVisitor<E> {
        private StringBuilder buffer;

        /**
         * Create a {@linkplain PrintingVisitor} with the specified {@linkplain StringBuilder}.
         * @param buffer the {@linkplain StringBuilder} to use.
         */
        public PrintingVisitor(StringBuilder buffer) {
            this.buffer = buffer;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void visit(E o) {
            if (buffer.length() != 0)
                buffer.append(",");

            buffer.append(o.toString());
        }
    }

    /**
     * Provides a simple Iterator for trees.
     */
    protected class TreeIterator implements Iterator<E> {

        private Stack<Tree<E>> stack;

        /**
         * Create a {@linkplain TreeIterator} instance.
         */
        public TreeIterator() {
            stack = new Stack<Tree<E>>();
            stack.push(AbstractTree.this);
        }

        /**
         * {@inheritDoc}
         */
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        /**
         * {@inheritDoc}
         */
        public E next() {
            if (stack.isEmpty())
                throw new UnsupportedOperationException();

            Tree<E> top = stack.pop();
            for (int i = top.size() - 1; i >= 0; i--) {
                Tree<E> subTree = top.getSubTree(i);
                if (!subTree.isEmpty())
                    stack.push(subTree);
            }

            return top.getKey();
        }

        /**
         * {@inheritDoc}
         */
        public void remove() {
            throw new UnsupportedOperationException("Cannot remove a tree using an iterator.");
        }
    }

}
