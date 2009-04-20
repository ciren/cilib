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
import java.util.Random;

import net.sourceforge.cilib.container.Pair;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;

/**
 * @author Wiehann Matthysen
 * @param <S>
 * @param <T>
 */
public class TournamentSelectionStrategy<S extends Comparable<S>, T> extends WeighedSelectionStrategy<S, T> {
	private static final long serialVersionUID = -6581331147767234266L;
	
	private int tournamentSize;
	
	public TournamentSelectionStrategy() {
		this.tournamentSize = 3;
	}
	
	public TournamentSelectionStrategy(TournamentSelectionStrategy<S, T> copy) {
		super(copy);
		this.tournamentSize = copy.tournamentSize;
	}

	@Override
	public TournamentSelectionStrategy<S, T> getClone() {
		return new TournamentSelectionStrategy<S, T>(this);
	}
	
	public void setTournamentSize(int tournamentSize) {
		this.tournamentSize = tournamentSize;
	}
	
	public int getTournamentSize() {
		return this.tournamentSize;
	}

	@Override
	public Collection<T> select(List<Pair<S, T>> weighedObjects, int numberOfObjects) {
		List<Pair<S, T>> tempObjects = new ArrayList<Pair<S, T>>(weighedObjects.size());
		tempObjects.addAll(weighedObjects);
		
		List<T> selectedObjects = new ArrayList<T>(numberOfObjects);
		Random randomiser = new MersenneTwister();
		while (selectedObjects.size() < numberOfObjects && tempObjects.size() > 0) {
			int tempTournamentSize = Math.min(this.tournamentSize, tempObjects.size());
			List<Pair<S, T>> competitors = new ArrayList<Pair<S, T>>(tempTournamentSize);
			while (competitors.size() < tempTournamentSize) {
				int randomIndex = randomiser.nextInt(tempObjects.size());
				competitors.add(tempObjects.remove(randomIndex));
			}
			Collections.sort(competitors);
			T selectedObject = competitors.remove(0).getValue();
			selectedObjects.add(selectedObject);
			tempObjects.addAll(competitors);
		}
		return selectedObjects;
	}
}
