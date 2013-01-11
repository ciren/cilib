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
 * <p>
 * Implementation of the Local Best Neighbourhood topology.
 * </p><p>
 * References:
 * </p><p><ul><li>
 * R.C. Eberhart, P. Simpson, and R. Drobbins, "Computational Intelligence PC Tools,"
 * chapter 6, pp. 212-226. Academic Press Professional, 1996.
 * </li></ul></p>
 *
 * @param <E> The {@linkplain Entity} type.
 */
public class LBestTopology<E extends Entity> extends AbstractTopology<E> {

    private static final long serialVersionUID = 93039445052676571L;

    /**
     * Default constructor. The default {@link #neighbourhoodSize} is 3.
     */
    public LBestTopology() {
        this.neighbourhoodSize = ConstantControlParameter.of(3);
    }

    /**
     * Copy constructor..
     */
    public LBestTopology(LBestTopology<E> copy) {
        super(copy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LBestTopology<E> getClone() {
        return new LBestTopology<E>(this);
    }

    /**
     * {@inheritDoc}
     */
	@Override
    protected Iterator<E> neighbourhoodOf(final E e) {
        return new UnmodifiableIterator<E>() {

            int count = 0;
            int ns = getNeighbourhoodSize();
            int ts = size();
            int index = (entities.indexOf(e) - (ns / 2) - 1 + ts) % ts;

            @Override
            public boolean hasNext() {
                return (count != ns);
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                return entities.get((index + ++count) % ts);
            }

        };
    }
}
