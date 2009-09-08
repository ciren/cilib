/*
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.pso.multiswarm;

import java.util.Iterator;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.visitor.TopologyVisitor;
import net.sourceforge.cilib.pso.dynamic.ChargedParticle;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Visitor to determine the size of the diameter of the provided {@linkplain Topology}
 * within the <code>visit</code> method.
 * This visitor only takes into consideration neutral (non-charged) particles.
 */
public class MultiSwarmDiameterVisitor extends TopologyVisitor{

    private double distance = -Double.MAX_VALUE;

    public MultiSwarmDiameterVisitor() {
        super();
    }

    @Override
    public void visit(Topology<? extends Entity> topology) {
        double maxDistance = 0.0;

        Iterator<? extends Entity> k1 = topology.iterator();
        while (k1.hasNext()) {
            Entity p1 = (Entity) k1.next();
            if(p1 instanceof ChargedParticle && ((ChargedParticle)p1).getCharge() != 0)
                continue;
            Vector position1 = (Vector) p1.getCandidateSolution();

            Iterator<? extends Entity> k2 = topology.iterator();
            while (k2.hasNext()) {
                Entity p2 = (Entity) k2.next();
                if(p2 instanceof ChargedParticle && ((ChargedParticle)p2).getCharge() != 0)
                    continue;
                Vector position2 = (Vector) p2.getCandidateSolution();

                double actualDistance = distanceMeasure.distance(position1, position2);
                if (actualDistance > maxDistance)
                    maxDistance = actualDistance;
            }
        }

        //result = maxDistance;
        distance = maxDistance;

    }

    @Override
    public Object getResult() {
         return this.distance;
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public boolean isDone() {
        return false;
    }

}
