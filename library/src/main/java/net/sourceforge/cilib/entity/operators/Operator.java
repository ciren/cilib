/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.operators;

import net.sourceforge.cilib.util.Cloneable;

/**
 * Interface to define all operators within the general structure of CIlib. All
 * operator instances should implement this interface to ensure that generic
 * use of operators is possible between different types of algorithms.
 */
public interface Operator extends Cloneable {

    /**
     * {@inheritDoc}
     */
    @Override
    Operator getClone();

    /**
     * Perform the operator operation given the current {@link net.sourceforge.cilib.entity.topologies.TopologyHolder}.
     *
     * @param holder The {@link net.sourceforge.cilib.entity.topologies.TopologyHolder} representing the required
     *               {@link net.sourceforge.cilib.entity.Topology} instances.
     */
//    public void performOperation(Topology<? extends Entity> topology, Topology<Entity> offspring);
//    void performOperation(TopologyHolder holder);

}
