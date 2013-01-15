/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.topologies;

import com.google.common.collect.ForwardingList;
import com.google.common.collect.Lists;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.visitor.TopologyVisitor;

/**
 * This an abstract class which extends from the abstract Topology class.
 * All {@linkplain net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm}
 * Topologies must inherit from this class.
 *
 * @param <E> The {@code Entity} type.
 */
public abstract class AbstractTopology<E extends Entity> extends ForwardingList<E> implements Topology<E> {

    private static final long serialVersionUID = -9117512234439769226L;

    protected List<E> entities;
    protected ControlParameter neighbourhoodSize;

    /**
     * Default constructor.
     */
    public AbstractTopology() {
        this.entities = Lists.<E>newArrayList();
        this.neighbourhoodSize = ConstantControlParameter.of(3.0);
    }

    /**
     * Copy constructor.
     */
    public AbstractTopology(AbstractTopology<E> copy) {
        this.neighbourhoodSize = copy.neighbourhoodSize.getClone();
        this.entities = Lists.<E>newArrayList();

        for (E entity : copy.entities) {
            this.entities.add((E) entity.getClone());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<E> iterator() {
        return entities.iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(TopologyVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final AbstractTopology<E> other = (AbstractTopology<E>) obj;
        if (this.entities != other.entities && (this.entities == null || !this.entities.equals(other.entities))) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + (this.entities != null ? this.entities.hashCode() : 0);
        return hash;
    }

    @Override
    protected List<E> delegate() {
        return this.entities;
    }

    public void setNeighbourhoodSize(ControlParameter neighbourhoodSize) {
        this.neighbourhoodSize = neighbourhoodSize;
    }

    public int getNeighbourhoodSize() {
        return Long.valueOf(Math.round(neighbourhoodSize.getParameter())).intValue();
    }

    protected abstract Iterator<E> neighbourhoodOf(E e);

    public final Collection<E> neighbourhood(final E e) {
        return new AbstractCollection<E>() {

            @Override
            public Iterator<E> iterator() {
                return neighbourhoodOf(e);
            }

            @Override
            public int size() {
                return getNeighbourhoodSize();
            }

        };
    }

}
