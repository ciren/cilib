/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.container.visitor;

/**
 * Extension of the default {@linkplain Visitor} to make pre and post visit operations
 * possible.
 * <p>
 * An example of use would be on {@linkplain Tree} containers, whereby the specific
 * traversal would alter the order in which tree nodes are used.
 *
 * @param <E> The element type.
 */
public class PrePostVisitor<E> implements Visitor<E> {

    /**
     * Create a new instance of {@linkplain PrePostVisitor}.
     */
    public PrePostVisitor() {
    }

    /**
     * Pre-visit the given element.
     * @param o The object to pre-visit.
     */
    public void preVisit(E o) {};

    /**
     * {@inheritDoc}
     */
    @Override
    public void visit(E o) {};

    /**
     * Post-visit the given element.
     * @param o The element to post visit.
     */
    public void postVisit(E o) {};

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDone() {
        return false;
    }
}
