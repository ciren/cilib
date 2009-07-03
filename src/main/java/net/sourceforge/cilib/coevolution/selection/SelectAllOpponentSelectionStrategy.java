/**
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
package net.sourceforge.cilib.coevolution.selection;

import net.sourceforge.cilib.coevolution.competitors.CoevolutionCompetitorList;

/**
 * example of implementation of an OpponentSelectionStrategy, selects all the opponents in the pool
 * @author Julien Duhain
 * @author leo
 *
 */
public class SelectAllOpponentSelectionStrategy extends OpponentSelectionStrategy{

	private static final long serialVersionUID = -7695834817656232972L;

	public SelectAllOpponentSelectionStrategy(){
		super();
	}

	public SelectAllOpponentSelectionStrategy(SelectAllOpponentSelectionStrategy copy){

	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public SelectAllOpponentSelectionStrategy getClone() {
		return new SelectAllOpponentSelectionStrategy(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public  CoevolutionCompetitorList selectCompetitors(CoevolutionCompetitorList pool){
		pool.setNumberofEntitiesPerList();
		return pool;
	}
}
