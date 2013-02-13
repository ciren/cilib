/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.visitor;

import fj.F;
import fj.data.List;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.pso.dynamic.ChargedParticle;

/**
 * A topology visitor decorator that removes all charged particles from a topology
 * and then visits the topology with the decorated visitor.
 *
 */
public class ChargedTopologyVisitorDecorator<E extends Entity> extends TopologyVisitor<E, Double> {

    private TopologyVisitor<E, Double> visitor;

    /**
     * Default constructor.
     */
    public ChargedTopologyVisitorDecorator() {
        this.visitor = new DiameterVisitor<E>();
    }

    /**
     * Gets the decorated topology visitor.
     * @return the topology visitor.
     */
    public TopologyVisitor<E, Double> getVisitor() {
        return visitor;
    }

    /**
     * Sets the decorated topology visitor.
     * @param visitor the topology visitor to use.
     */
    public void setVisitor(TopologyVisitor<E, Double> visitor) {
        this.visitor = visitor;
    }

    @Override
    public Double f(List<E> topology) {
        return visitor.f(topology.removeAll(new F<E, Boolean>() {
            @Override
            public Boolean f(E e) {
                return e instanceof ChargedParticle && ((ChargedParticle) e).getCharge() != 0;
            }
        }));
    }
}
