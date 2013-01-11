/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.topologies;

import java.util.Iterator;
import net.sourceforge.cilib.entity.Entity;

/**
 * <p>
 * Implementation of the gbest neighbourhoodOf topology. This topology is a special
 * case of the LBestTopology where the neighbourhoodOf size is the swarm size.
 * </p><p>
 * References:
 * </p><p><ul><li>
 * R.C. Eberhart, P. Simpson, and R. Drobbins, "Computational Intelligence PC Tools,"
 * chapter 6, pp. 212-226. Academic Press Professional, 1996.
 * </li></ul></p>
 *
 * @param <E> The {@linkplain Entity} type.
 */
public class GBestTopology<E extends Entity> extends AbstractTopology<E> {
    private static final long serialVersionUID = 3190027340582769112L;

    /**
     * Default constructor.
     */
    public GBestTopology() {
        super();
    }

    /**
     * Copy constructor.
     */
    public GBestTopology(GBestTopology<E> copy) {
        super(copy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GBestTopology<E> getClone() {
        return new GBestTopology<E>(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNeighbourhoodSize() {
        return size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Iterator<E> neighbourhoodOf(E e) {
        return iterator();
    }
}

