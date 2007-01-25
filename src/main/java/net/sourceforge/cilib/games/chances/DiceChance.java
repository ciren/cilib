/*
 * DiceChance.java
 * 
 * Created on 2004/03/16
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
 
package net.sourceforge.cilib.games.chances;

import java.util.Random;

/**
 * @author Vangos
 *
 * Represents a chance and returns an intger between 0 and an upper bound
 */

public class DiceChance extends Chance{
	public DiceChance(Random random_,int sides_)
	{
		super(random_);
		sides = sides_;
	}

	public Chance Clone()
	{
		Chance clone = new DiceChance(ran,sides);
		return clone;
	}
	
	//Returns this integer
	public int NextChance()
	{ return ran.nextInt(sides); }
	
	//The number of different numbers that can be returned
	private int sides;
}
