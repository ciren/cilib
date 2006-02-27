/*
 * Created on 2004/04/29
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.Games.Agents;

import net.sourceforge.cilib.Container.Tree.GeneralTree;
import net.sourceforge.cilib.Games.Game;
import net.sourceforge.cilib.Games.States.State;

/**
 * @author Vangos
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MinMaxAgent extends Agent{
	public MinMaxAgent(Game game_,int depth_)
	{
		super(game_);
		depth = depth_;
	}
	
	public State ReturnMax(State[] stateArray_)
	{
		double[] score = new double[stateArray_.length];
		
		for (int i=0; i<stateArray_.length; i++)
		{
			GeneralTree gameTree = new GeneralTree(stateArray_[i]);
			score[i] = MinMax(gameTree);
		}
		return stateArray_[0];
	}
	
	public State ReturnMin(State[] stateArray_)
	{
		int[] score = new int[stateArray_.length];
		
		for (int i=0; i<stateArray_.length; i++)
		{
		}
		return stateArray_[0];
	}
	
	private double MinMax(GeneralTree gameTree)
	{
		theGame.SetStartState((State)gameTree.getNodeValue());
		theGame.ResetState();
		State[] stateArray = theGame.NextStates(0);
		for (int i=0; i<stateArray.length; i++)
			gameTree.add(new GeneralTree(stateArray[i]));
		return 1.0;
	}

	private int depth;
}
