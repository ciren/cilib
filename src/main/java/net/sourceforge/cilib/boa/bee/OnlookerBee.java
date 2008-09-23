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
package net.sourceforge.cilib.boa.bee;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.boa.ABC;


/**
 * Represents an onlooker bee in the hive.
 * @author Andrich
 *
 */
public class OnlookerBee extends AbstractBee implements HoneyBee {
	private static final long serialVersionUID = -4714791530850285930L;
	
	/**
	 * Default constructor.
	 */
	public OnlookerBee() {
	}
	
	/**
	 * Copy constructor. Creates a copy of the provided instance.
	 * @param copy reference that is deep copied.
	 */
	public OnlookerBee(AbstractBee copy) {
		super(copy);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OnlookerBee getClone() {
		return new OnlookerBee(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updatePosition() {
		ABC algorithm = (ABC) Algorithm.get();
		HoneyBee target = targetSelectionStrategy.select(algorithm.getWorkerTopology());
		
		while (target == this) {
			target = targetSelectionStrategy.select(algorithm.getWorkerTopology());
		}
		
		this.positionUpdateStrategy.updatePosition(this, target);
	}

}
