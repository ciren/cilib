package net.sourceforge.cilib.algorithm.population;

import fj.data.List;

public interface HasTopology<E> {

    /**
     * {@inheritDoc}
     */
    List<E> getTopology();

    /**
     * Set the <tt>Topology</tt> for the population-based algorithm.
     * @param topology The {@linkplain Topology} to be set.
     */
    void setTopology(List<E> topology);

}
