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

package net.sourceforge.cilib.util.selection.selectionstrategies;

import java.util.Collection;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;

/**
 * @author Wiehann Matthysen
 */
public abstract class PopulationSelectionStrategy extends SelectionStrategy<PopulationBasedAlgorithm> {
	private static final long serialVersionUID = 1277499103589095651L;

	@Override
	public abstract PopulationSelectionStrategy getClone();

	@Override
	public abstract Collection<PopulationBasedAlgorithm> select(Collection<? extends PopulationBasedAlgorithm> populations, int numberOfPopulations);
}
