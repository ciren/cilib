/*
 * Created on 2004/03/31
 *
 * Copyright (C) 2003, 2004 - CIRG@UP 
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
 
package net.sourceforge.cilib.Games.Agents;

import net.sourceforge.cilib.Games.States.*;
import java.util.Random;

/**
 * @author Vangos
 *
 * Selects a random state out of an array of states
 */

public class RandomAgent extends Agent
{
	public RandomAgent(Random random_)
	{
		super(null); 
		ran = random_; 
	}
	
	//Returns a random state
	public State ReturnMax(State[] stateArray_)
	{ return stateArray_[ran.nextInt(stateArray_.length)]; }
	
	//Returns a random state
	public State ReturnMin(State[] stateArray_)
	{ return stateArray_[ran.nextInt(stateArray_.length)]; }
	
	//The Random object used to randomly select a state
	private Random ran;
}
