/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.container.visitor;

/**
 * Simple decorator class that wraps the needed <code>Visitor</code> to make it compatible with Pre-Order
 * Traversals in the Containers defined.
 *
 * @param <E> The type
 */
public class PreOrderVisitorDecorator<E> extends PrePostVisitor<E> {
    private Visitor<E> visitor;

    /**
     * Create a visitor that will pre-visit all nodes. Decorates the provided
     * decorator.
     * @param v The {@linkplain Visitor} to decorate.
     */
    public PreOrderVisitorDecorator(Visitor<E> v) {
        visitor = v;
    }

    /**
     * {@inheritDoc}
     */
    public void preVisit(E o) {
        visitor.visit(o);
    }
}
