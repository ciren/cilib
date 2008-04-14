/*
 * AveragePositionVisitor.java
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
package net.sourceforge.cilib.pso.niching;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.visitor.TopologyVisitor;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * A visitor that determines the average position vector for all {@linkplain Entity} objects
 * contained within the provided {@linkplain Topology}.
 */
public class AveragePositionVisitor extends TopologyVisitor {
	
	private Vector averageVector;
	
	public AveragePositionVisitor() {
		this.averageVector = new Vector();
	}

	/**
	 * Determine the average position of all {@linkplain Entity} objects within
	 * the current {@linkplain Topology}.
	 * 
	 * @param topology The {@linkplain Topology} maintaining the {@linkplain Entity} objects.
	 */
	public void visit(Topology<? extends Entity> topology) {
		averageVector.initialise(topology.get(0).getDimension(), new Real(0.0));
		
		for (Entity entity : topology)
			averageVector = averageVector.plus((Vector) entity.getContents());
		
		averageVector.divide(topology.get(0).getDimension());
	}

	/**
	 * Get the determined average position after the visit operation has completed.
	 * @return The average position {@linkplain Vector} if the visit has completed,
	 *         else return a empty {@linkplain Vector}.
	 */
	public Vector getAveragePosition() {
		return averageVector;
	}

	
}
