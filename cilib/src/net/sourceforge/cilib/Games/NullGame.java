/*
 * Created on Jul 12, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.Games;

import net.sourceforge.cilib.Games.States.*;
import net.sourceforge.cilib.Games.Agents.*;

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
