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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;

/**
 * @author Wiehann Matthysen
 */
public class RingBasedPopulationSelectionStrategy extends PopulationSelectionStrategy {
	private static final long serialVersionUID = -33553447581402053L;

	public RingBasedPopulationSelectionStrategy() { }
	
	public RingBasedPopulationSelectionStrategy(RingBasedPopulationSelectionStrategy copy) { }

	@Override
	public RingBasedPopulationSelectionStrategy getClone() {
		return new RingBasedPopulationSelectionStrategy(this);
	}

	@Override
	public Collection<PopulationBasedAlgorithm> select(Collection<? extends PopulationBasedAlgorithm> objects, int numberOfObjects) {
		PopulationBasedAlgorithm currentPopulation = (PopulationBasedAlgorithm)Algorithm.get();
		List<PopulationBasedAlgorithm> populationList = Arrays.asList(objects.toArray(new PopulationBasedAlgorithm[0]));
		int currentIndex = populationList.indexOf(currentPopulation);
		List<PopulationBasedAlgorithm> selectedPopulations = new ArrayList<PopulationBasedAlgorithm>(numberOfObjects);
		int numberOfObjectsRemaining = numberOfObjects;
		while (numberOfObjectsRemaining > 0) {
			PopulationBasedAlgorithm selectedPopulation = populationList.get((++currentIndex) % populationList.size());
			selectedPopulations.add(selectedPopulation);
			--numberOfObjectsRemaining;
		}
		return selectedPopulations;
	}
}
