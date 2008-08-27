/*
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
package net.sourceforge.cilib.entity.visitor;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.ManhattanDistanceMeasure;

/**
 * @author Edwin Peer
 */
public class ClosestParticleVisitor extends TopologyVisitor {
	
	private Entity closestEntity;
	private Entity target;
	private double distance;
	private DistanceMeasure distanceMeasure;
	
	public ClosestParticleVisitor() {
		distance = Double.MAX_VALUE;
		distanceMeasure = new ManhattanDistanceMeasure();
	}

	@Override
	public void visit(Topology<? extends Entity> topology) {
		for (Entity current : topology) {
			if (closestEntity == null) closestEntity = current;
			if (closestEntity.equals(current)) continue;
			
			double currentDistance = distanceMeasure.distance((Vector) target.getCandidateSolution(), (Vector) current.getCandidateSolution());
			
			if (currentDistance < distance) {
				this.closestEntity = current;
				this.distance = currentDistance;
			}
			
		}
	}
	
	public void setTarget(Entity entity) {
		this.target = entity;
	}
	
	public Entity getClosestEntity() {
		return this.closestEntity;
	}

	public DistanceMeasure getDistanceMeasure() {
		return distanceMeasure;
	}

	public void setDistanceMeasure(DistanceMeasure measure) {
		this.distanceMeasure = measure;
	}

}
