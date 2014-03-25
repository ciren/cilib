/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
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
    void setTopology(List<? extends E> topology);

}
