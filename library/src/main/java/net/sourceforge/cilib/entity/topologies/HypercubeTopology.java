/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.topologies;

import com.google.common.collect.UnmodifiableIterator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.entity.Entity;

/**
 * @param <E> The {@linkplain Entity} type.
 */
public class HypercubeTopology<E extends Entity> extends AbstractTopology<E> {
    private static final long serialVersionUID = -8328600903928335004L;

    /**
     * Default constructor.
     */
    public HypercubeTopology() {
        this.neighbourhoodSize = ConstantControlParameter.of(5);
    }

    /**
     * Copy constructor.
     */
    public HypercubeTopology(HypercubeTopology<E> copy) {
        super(copy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HypercubeTopology<E> getClone() {
        return new HypercubeTopology<E>(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Iterator<E> neighbourhoodOf(final E e) {
        return new UnmodifiableIterator<E>() {

            private int count = 0;
            private int index = entities.indexOf(e);

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                return entities.get(index ^ Double.valueOf(Math.pow(2, count++)).intValue());
            }

            @Override
            public boolean hasNext() {
                return count < getNeighbourhoodSize();
            }
        };
    }

}
