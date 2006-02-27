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
 
package net.sourceforge.cilib.Games.GameTokens;


/**
 * @author Vangos
 *
 * An object that represents a game action. This is represented by binding a token
 * together with a position.
 */

public class Action 
{
	public Action(Token token_,int position_)
	{
		gameToken = token_;
		position = position_;
	}
	
	//Returns the Token of the current Action
	public Token GetGameToken()
	{ return gameToken; }
	
	//Returns the position of the current Action
	public int GetPosition()
	{ return position; }
	
	//The Token of the current Action
	private Token gameToken;
	
	//The position of the current Action
	private int position;
}
