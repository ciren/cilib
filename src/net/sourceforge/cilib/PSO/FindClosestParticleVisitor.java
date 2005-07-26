/*
 * FindClosestNeighbourParticleVisitor.java
 * 
 * Created on Apr 11, 2004
 *
 * Copyright (C)  2004 - CIRG@UP 
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
 *
 */
package net.sourceforge.cilib.PSO;

import net.sourceforge.cilib.Util.DistanceMeasure;
import net.sourceforge.cilib.Util.AbsoluteDistanceMeasure;

/**
 * @author espeer
 */
public class FindClosestParticleVisitor implements ParticleVisitor {

    public FindClosestParticleVisitor(Particle target) {
        this.target = target;
        closest = null;
        minimum = Double.MAX_VALUE;
        distanceMeasure = new AbsoluteDistanceMeasure();
    }
    
    /* (non-Javadoc)
     * @see net.sourceforge.cilib.PSO.ParticleVisitor#visit(net.sourceforge.cilib.PSO.Particle)
     */
    public void visit(Particle particle) {
        if (closest == null && particle.getId() != target.getId()) {
            closest = particle;
            minimum = distanceMeasure.distance(closest.getPosition(), target.getPosition()); 
        }
        else {
            double tmp = distanceMeasure.distance(particle.getPosition(), target.getPosition());
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
