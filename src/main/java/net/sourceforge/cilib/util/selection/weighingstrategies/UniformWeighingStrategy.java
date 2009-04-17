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

package net.sourceforge.cilib.util.selection.weighingstrategies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.cilib.container.Pair;

/**
 * @author Wiehann Matthysen
 * @param <S>
 * @param <T>
 */
public class UniformWeighingStrategy<S extends Comparable<S>, T> implements WeighingStrategy<S, T> {
	private static final long serialVersionUID = -8960834082878739829L;
	
	private S weight;

	public UniformWeighingStrategy() {
		this.weight = null;
	}
	
	public UniformWeighingStrategy(UniformWeighingStrategy<S, T> copy) {
		this.weight = copy.weight;
	}
	
	@Override
	public UniformWeighingStrategy<S, T> getClone() {
		return new UniformWeighingStrategy<S, T>(this);
	}
	
	public void setWeight(S weight) {
		this.weight = weight;
	}
	
	public S getWeight() {
		return this.weight;
	}

	@Override
	public List<Pair<S, T>> weigh(Collection<? extends T> objects) {
		List<Pair<S, T>> weighedObjects = new ArrayList<Pair<S, T>>();
		for (T object : objects) {
			weighedObjects.add(new Pair<S, T>(this.weight, object));
		}
		return weighedObjects;
	}
}
