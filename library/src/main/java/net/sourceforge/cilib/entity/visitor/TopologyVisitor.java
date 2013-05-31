/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.visitor;

import net.sourceforge.cilib.entity.Entity;
import fj.F;

/**
 * Interface for all visitor instances that visit an entire {@link Topology}
 * at once. These type of visitors are generally assocaited with the
 * calculation of topology related information, such as diameter and
 * radius calculations of the provided topologies.
 *
 */
public abstract class TopologyVisitor<E extends Entity, A> extends F<fj.data.List<E>, A> {
}
