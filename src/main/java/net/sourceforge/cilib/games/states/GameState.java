/*
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
package net.sourceforge.cilib.games.states;

import net.sourceforge.cilib.games.items.GameItem;


import java.util.Vector;

import net.sourceforge.cilib.util.Cloneable;

/**
 * @author leo
 * This class represents a snapshot of the game at any given iteration.
 */
public class GameState implements Cloneable {
	/**
	 *
	 */
	private static final long serialVersionUID = 3668288597302493178L;
	protected Vector<GameItem> currentState;
	/**
	 *
	 */
	public GameState() {
		// TODO Auto-generated constructor stub
		currentState = new Vector<GameItem>();
	}

	public GameState(GameState other){
		currentState = new Vector<GameItem>(other.currentState);
	}

	public int getSize(){
		return currentState.size();
	}

	public void addGameItem(GameItem item){
		currentState.add(item);
	}

	public void clearState(){
		currentState.clear();
	}

	public GameItem getItem(int index) {
		if(index >= currentState.size())
			throw new RuntimeException("Index greater than vector size");
		return currentState.get(index);
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.util.Cloneable#getClone()
	 */
	public GameState getClone() {
		return new GameState(this);
	}

	public void Merge(GameState other){

	}

	@Override
	public boolean equals(Object other) {
		// TODO Auto-generated method stub
		if(!(other instanceof GameState))
			return false;
		return false;
	}




}
