/*
 * FindClosetParticleVisitor.java
 *
 * Copyright (C) 2003 - 2008
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
package net.sourceforge.cilib.pso;

import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.pso.particle.ParticleVisitor;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.ManhattanDistanceMeasure;

/**
 * @author Edwin Peer
 */
public class FindClosestParticleVisitor extends ParticleVisitor {

    public FindClosestParticleVisitor(Particle target) {
        this.target = target;
        closest = null;
        minimum = Double.MAX_VALUE;
        distanceMeasure = new ManhattanDistanceMeasure();
    }

    /**
     * {@inheritDoc}
     */
    public void visit(Particle particle) {
        if (closest == null && particle.getId() != target.getId()) {
            closest = particle;
            Vector closestPosition = (Vector) closest.getPosition();
            Vector targetPosition = (Vector) target.getPosition();
            minimum = distanceMeasure.distance(closestPosition, targetPosition); 
        }
        else {
        	Vector particlePosition = (Vector) particle.getPosition();
        	Vector targetPosition = (Vector) target.getPosition();
            double tmp = distanceMeasure.distance(particlePosition, targetPosition);
            if (tmp < minimum) {
                closest = particle;
                minimum = tmp;
            }
        }
    }

    public Particle getClosestParticle() {
        return closest;
    }
    
    /**
     * @return Returns the distanceMeasure.
     */
    public DistanceMeasure getDistanceMeasure() {
        return distanceMeasure;
    }

    /**
     * @param distanceMeasure The distanceMeasure to set.
     */
    public void setDistanceMeasure(DistanceMeasure distanceMeasure) {
        this.distanceMeasure = distanceMeasure;
    }

    private Particle closest;
    private Particle target;
    private double minimum;
    private DistanceMeasure distanceMeasure;
}
