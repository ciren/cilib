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
import java.util.Collections;
import java.util.List;

import net.sourceforge.cilib.container.Pair;

/**
 * @author Wiehann Matthysen
 * @param <S>
 * @param <T>
 */
public class RankBasedSelectionStrategy<S extends Comparable<S>, T> extends WeighedSelectionStrategy<S, T> {
	private static final long serialVersionUID = 4782682872298695967L;
	
	public RankBasedSelectionStrategy() {
		super();
	}
	
	public RankBasedSelectionStrategy(RankBasedSelectionStrategy<S, T> copy) {
		super(copy);
	}

	@Override
	public RankBasedSelectionStrategy<S, T> getClone() {
		return new RankBasedSelectionStrategy<S, T>(this);
	}

	@Override
	public Collection<T> select(List<Pair<S, T>> weighedObjects, int numberOfObjects) {
		List<Pair<S, T>> sortedObjects = new ArrayList<Pair<S, T>>(weighedObjects.size());
		sortedObjects.addAll(weighedObjects);
		Collections.sort(sortedObjects);
		List<Pair<S, T>> subList = sortedObjects.subList(0, numberOfObjects);
		List<T> selectedObjects = new ArrayList<T>(numberOfObjects);
		for (Pair<S, T> object : subList) {
			selectedObjects.add(object.getValue());
		}
		return selectedObjects;
	}

}
