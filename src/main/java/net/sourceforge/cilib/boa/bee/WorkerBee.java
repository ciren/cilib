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
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;

/**
 * A worker bee that forages for food and updates the hive information by dancing.
 * @author Andrich
 */
public class WorkerBee extends AbstractBee implements HoneyBee {
	private static final long serialVersionUID = 3657591650621784765L;

	private ControlParameter forageLimit;
	private int failureCount;

	/**
	 * Create a new instance with reasonable defaults set.
	 */
	public WorkerBee() {
		failureCount = 0;
		this.forageLimit = new ConstantControlParameter(500);
	}

	/**
	 * Copy constructor. Create a copy of the provided instance.
	 * @param copy The isntance to copy.
	 */
	public WorkerBee(WorkerBee copy) {
		super(copy);
		failureCount = copy.failureCount;
		this.forageLimit = copy.forageLimit.getClone();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public WorkerBee getClone() {
		return new WorkerBee(this);
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

		boolean success = this.positionUpdateStrategy.updatePosition(this, target);
		if (!success) {
			failureCount++;
			if (failureCount >= forageLimit.getParameter())	{
				failureCount = 0;
				ExplorerBee explorerBee = algorithm.getExplorerBee();
				if (explorerBee.searchAllowed()) {
					this.setPosition(explorerBee.getNewPosition(this.getPosition()));
				}
			}
		}
	}

	/**
	 * Get the forage limit.
	 * @return The {@linkplain ControlParameter} representing the forage limit.
	 */
	public ControlParameter getForageLimit() {
		return forageLimit;
	}

	/**
	 * Set the forage limit.
	 * @param forageLimit The limit to set.
	 */
	public void setForageLimit(ControlParameter forageLimit) {
		this.forageLimit = forageLimit;
	}
}
