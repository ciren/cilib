/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.visitor;

import net.sourceforge.cilib.util.Visitor;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;

/**
 * Interface for all visitor instances that visit an entire {@link Topology}
 * at once. These type of visitors are generally assocaited with the
 * calculation of topology related information, such as diameter and
 * radius calculations of the provided topologies.
 *
 */
public interface TopologyVisitor extends Visitor<Topology<? extends Entity>> {
    /**
     * Perfrom the visit operation on the provided {@link Topology}.
     * @param topology The {@link Topology} to visit.
     */
    @Override
    public void visit(Topology<? extends Entity> topology);

    /**
     * Get the result of the visitor after it has performed the visit()
     * action.
     * @return The result of the visit()
     */
    public Object getResult();
}
