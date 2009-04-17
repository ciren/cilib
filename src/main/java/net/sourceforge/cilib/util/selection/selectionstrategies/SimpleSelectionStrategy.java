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

/**
 * @author Wiehann Matthysen
 * @param <T>
 */
public class SimpleSelectionStrategy<T> extends SelectionStrategy<T> {
	private static final long serialVersionUID = 3335074169267315374L;
	
	public SimpleSelectionStrategy() { }
	
	public SimpleSelectionStrategy(SimpleSelectionStrategy<T> copy) { }

	@Override
	public SimpleSelectionStrategy<T> getClone() {
		return new SimpleSelectionStrategy<T>(this);
	}

	@Override
	public Collection<T> select(Collection<? extends T> objects, int numberOfObjects) {
		List<T> selectedObjects = new ArrayList<T>(numberOfObjects);
		selectedObjects.addAll(objects);
		return selectedObjects.subList(0, numberOfObjects);
	}
}
