/*
 * NullGame.java
 * 
 * Created on Jul 24, 2004
 *
 *
 * Copyright (C) 2004 - CIRG@UP 
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
package net.sourceforge.cilib.games;

import net.sourceforge.cilib.games.agents.*;
import net.sourceforge.cilib.games.states.*;

/**
 * @author Vangos
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class NullGame extends Game
{
	public NullGame()
	{ totalPlayers = 0;	}
	
	protected void AssignScore()
	{}
	
	public boolean GameOver()
	{ return false; }
	
	protected State GetBestState(Agent evaluate_,State[] stateArray_)
	{ return null; }
	
	protected void InitialState()
	{}
	
	public State[] NextStates(int player)
	{ return null; } 
	
	public void ResetState()
	{}
	
	public void SetStartState(State state_)
	{}
}
