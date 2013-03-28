/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.visitor;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.pso.dynamic.ChargedParticle;

/**
 * A topology visitor decorator that removes all charged particles from a topology
 * and then visits the topology with the decorated visitor.
 *
 */
public class ChargedTopologyVisitorDecorator implements TopologyVisitor {

    private boolean done;
    private Object result;
    private TopologyVisitor visitor;

    /**
     * Default constructor.
     */
    public ChargedTopologyVisitorDecorator() {
        this.done = false;
        this.visitor = new DiameterVisitor();
        this.result = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visit(Topology<? extends Entity> topology) {
        done = false;
        result = null;

        Topology<? extends Entity> newTopology = topology.getClone();

        for(Entity e : newTopology) {
            if(e instanceof ChargedParticle && ((ChargedParticle) e).getCharge() != 0) {
                newTopology.remove(e);
            }
        }

        topology.accept(visitor);
        result = visitor.getResult();
        done = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getResult() {
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDone() {
        return done;
    }

    /**
     * Gets the decorated topology visitor.
     * @return the topology visitor.
     */
    public TopologyVisitor getVisitor() {
        return visitor;
    }

    /**
     * Sets the decorated topology visitor.
     * @param visitor the topology visitor to use.
     */
    public void setVisitor(TopologyVisitor visitor) {
        this.visitor = visitor;
    }
}
