/*
 * CardChance.java
 * 
 * Created on 2004/03/23
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
 * Incomplete class representing a deck of cards
 */
public class CardChance extends Chance{
	public CardChance(Random random_,int decks_,int jokers_)
	{
		super(random_);
		decks = decks_;
		jokers = jokers_;
	}
	
	public Chance Clone()
	{
		Chance clone = new CardChance(ran,decks,jokers);
		return clone;
	}
	
	public int NextChance()
	{
		/*
		 * Add code here to return a card from a deck
		 */
		return 0;
	}
	
	private int decks;
	private int jokers;
}
