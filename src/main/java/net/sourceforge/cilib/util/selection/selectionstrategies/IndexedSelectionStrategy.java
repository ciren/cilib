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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Wiehann Matthysen
 * @param <T>
 */
public class IndexedSelectionStrategy<T> extends SelectionStrategy<T> {
	private static final long serialVersionUID = -1795328406695719522L;
	
	private Set<Integer> indices;
	
	public IndexedSelectionStrategy() {
		this.indices = new HashSet<Integer>();
	}
	
	public IndexedSelectionStrategy(IndexedSelectionStrategy<T> copy) {
		this.indices = new HashSet<Integer>();
		this.indices.addAll(copy.indices);
	}

	@Override
	public IndexedSelectionStrategy<T> getClone() {
		return new IndexedSelectionStrategy<T>(this);
	}
	
	public void addIndex(int index) {
		this.indices.add(index);
	}

    public void addIndices(Integer ... indices) {
        this.indices.addAll(Arrays.asList(indices));
    }
	
	public void removeIndex(int index) {
		this.indices.remove(index);
	}

	@Override
	public Collection<T> select(Collection<? extends T> objects, int numberOfObjects) {
		List<T> selectedObjects = new ArrayList<T>(numberOfObjects);
		int index = 0;
		for (T object : objects) {
			if (this.indices.contains(index++)) {
				selectedObjects.add(object);
			}
		}
		return selectedObjects;
	}

}
