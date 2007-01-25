/*
 * Evaluations.java
 * 
 * Created on Apr 12, 2004
 *
 * Copyright (C) 2003 - 2006 
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
 
package net.sourceforge.cilib.games.states.evaluations;

import net.sourceforge.cilib.games.states.*;

/**
 * @author Vangos
 *
 * An abstratct class that is used to evaluate a State
 */


public abstract class Evaluations {
	public Evaluations(int totalPlayers_)
	{
		totalPlayers = totalPlayers_;
		evaluation = new double[totalPlayers];
	}
	
	//Returns a clone of the object
	public abstract Evaluations Clone();
	
	//Evaluates a certain given state
	public abstract void Evaluate(State state_);
	
	//Returns the evaluation of the state
	public abstract double[] GetEvaluation();
	
	//Returns the total number of players
	public int GetTotalPlayer()
	{ return totalPlayers; }
	
	//The total players that have to be evaluated for a given state
	protected int totalPlayers;
	
	//The array that stores the Evaluations
	protected double[] evaluation;
}
