/**
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

package net.sourceforge.cilib.entity.operators;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.cilib.entity.topologies.TopologyHolder;

public class CompositeOperator implements Operator {
	private static final long serialVersionUID = -807046469432710581L;
	private List<Operator> operators;
	
	public CompositeOperator() {
		this.operators = new ArrayList<Operator>();
	}
	
	public CompositeOperator(CompositeOperator copy) {
		this.operators = new ArrayList<Operator>();
		
		for (Operator operator : copy.operators)
			this.operators.add(operator.getClone());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CompositeOperator getClone() {
		return new CompositeOperator(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
//	public void performOperation(Topology<? extends Entity> topology, Topology<Entity> offspring) {
	public void performOperation(TopologyHolder holder) {
		for (Operator operator : operators)
//			operator.performOperation(topology, offspring);
			operator.performOperation(holder);
	}
	
	public void add(Operator operator) {
		this.operators.add(operator);
	}

	public void clear() {
		this.operators.clear();
	}

}
