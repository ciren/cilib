/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.topologies;

import java.util.Iterator;
import java.util.NoSuchElementException;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.AbstractTopology;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.IndexedIterator;

/**
 * @param <E> The {@linkplain Entity} type.
 */
public class HypercubeTopology<E extends Entity> extends AbstractTopology<E> {
    private static final long serialVersionUID = -8328600903928335004L;

    /**
     * Default constructor.
     */
    public HypercubeTopology() {
        super();
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
    public Iterator<E> neighbourhood(Iterator<? extends Entity> iterator) {
        return new HypercubeNeighbourhoodIterator<E>(this, (IndexedIterator<E>) iterator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNeighbourhoodSize() {
        return (int) Math.round(neighbourhoodSize.getParameter());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNeighbourhoodSize(ControlParameter neighbourhoodSize) {
        this.neighbourhoodSize = neighbourhoodSize;
    }

    /**
     * Iterator to traverse the hypercube topology.
     */
    private class HypercubeNeighbourhoodIterator<T extends Entity> extends NeighbourhoodIterator<T> {
        private int count;

        public HypercubeNeighbourhoodIterator(AbstractTopology<T> topology, IndexedIterator<T> iterator) {
            super(topology, iterator);
            this.count = 0;
        }

        @Override
        public T next() {
            if (count >= topology.getNeighbourhoodSize()) {
                throw new NoSuchElementException();
            }
            
            int i = index ^ Double.valueOf(Math.pow(2, count)).intValue();
            count++;

            return topology.get(i);
        }

        @Override
        public void remove() {
            topology.remove(index);
            index = index ^ Double.valueOf(Math.pow(2, count)).intValue();
            
            if (index < 0) {
                index += topology.size();
            }
        }

        @Override
        public boolean hasNext() {
            return count < topology.getNeighbourhoodSize();
        }
    }
}
