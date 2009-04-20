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
import java.util.List;

import net.sourceforge.cilib.container.Pair;
import net.sourceforge.cilib.util.selection.weighingstrategies.UniformWeighingStrategy;
import net.sourceforge.cilib.util.selection.weighingstrategies.WeighingStrategy;

/**
 * @author Wiehann Matthysen
 * @param <S>
 * @param <T>
 */
public abstract class WeighedSelectionStrategy<S extends Comparable<S>, T> extends SelectionStrategy<T> {
	private static final long serialVersionUID = -7144748104792291712L;
	
	public static <S extends Comparable<S>, T> Pair<S, S> getMinMaxWeights(List<Pair<S, T>> weighedObjects) {
		S minWeight = weighedObjects.iterator().next().getKey();
		S maxWeight = weighedObjects.iterator().next().getKey();
		for (Pair<S, T> weighedObject : weighedObjects) {
			S weight = weighedObject.getKey();
			if (weight.compareTo(minWeight) < 0) {
				minWeight = weight;
			}
			else if (weight.compareTo(maxWeight) > 0) {
				maxWeight = weight;
			}
		}
		return new Pair<S, S>(minWeight, maxWeight);
	}
	
	private WeighingStrategy<S, T> weighingStrategy;
	
	public WeighedSelectionStrategy() {
		this.weighingStrategy = new UniformWeighingStrategy<S, T>();
	}
	
	public WeighedSelectionStrategy(WeighedSelectionStrategy<S, T> copy) {
		this.weighingStrategy = copy.weighingStrategy.getClone();
	}
	
	@Override
	public abstract WeighedSelectionStrategy<S, T> getClone();
	
	public void setWeighingStrategy(WeighingStrategy<S, T> weighingStrategy) {
		this.weighingStrategy = weighingStrategy;
	}
	
	public WeighingStrategy<S, T> getWeighingStrategy() {
		return this.weighingStrategy;
	}
	
    @Override
	public Collection<T> select(Collection<? extends T> objects, int numberOfObjects) {
		List<Pair<S, T>> weighedObjects = this.weighingStrategy.weigh(objects);
		return select(weighedObjects, numberOfObjects);
	}
	
	public T select(List<Pair<S, T>> weighedObjects) {
		return select(weighedObjects, 1).iterator().next();
	}
	
	public abstract Collection<T> select(List<Pair<S, T>> weighedObjects, int numberOfObjects);
}
