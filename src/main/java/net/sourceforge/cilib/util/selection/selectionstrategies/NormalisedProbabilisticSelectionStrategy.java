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
import java.util.Collection;
import java.util.List;

import java.util.Random;
import net.sourceforge.cilib.container.Pair;

/**
 * @author Wiehann Matthysen
 * @param <T>
 */
public class NormalisedProbabilisticSelectionStrategy<T> extends ProbabilisticSelectionStrategy<T> {
	private static final long serialVersionUID = 379443471370708765L;
	
	public NormalisedProbabilisticSelectionStrategy(Random randomiser) {
        super(randomiser);
    }

    public NormalisedProbabilisticSelectionStrategy() {
        super();
    }
	
	public NormalisedProbabilisticSelectionStrategy(NormalisedProbabilisticSelectionStrategy<T> copy) {
		super(copy);
	}
	
	@Override
	public NormalisedProbabilisticSelectionStrategy<T> getClone() {
		return new NormalisedProbabilisticSelectionStrategy<T>(this);
	}
	
	@Override
	public Collection<T> select(List<Pair<Double, T>> weighedObjects, int numberOfObjects) {
		Pair<Double, Double> minMaxWeights = getMinMaxWeights(weighedObjects);
		double minWeight = minMaxWeights.getKey();
		double maxWeight = minMaxWeights.getValue();
		double minMaxDistance = maxWeight - minWeight;
		
		List<Pair<Double, T>> tempObjects = new ArrayList<Pair<Double, T>>(numberOfObjects);
		tempObjects.addAll(weighedObjects);
		for (Pair<Double, T> tempObject : tempObjects) {
			double normalisedWeight = (tempObject.getKey() - minWeight) / minMaxDistance;
			tempObject.setKey(normalisedWeight);
		}
		
		return super.select(tempObjects, numberOfObjects);
	}
}
