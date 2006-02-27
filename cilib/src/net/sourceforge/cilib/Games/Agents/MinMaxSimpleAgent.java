/*
 * Created on 2004/04/29
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.Games.Agents;

import net.sourceforge.cilib.Games.States.*;
import net.sourceforge.cilib.Container.Tree.*;
import net.sourceforge.cilib.Games.*;

/**
 * @author Vangos
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MinMaxSimpleAgent extends MinMaxAgent{
	public MinMaxSimpleAgent(Game game_)
	{ super(game_); }
	
	protected double MinMax(Tree gameTree,int currentDepth,int currentPlayer)
	{
		theGame.SetStartState((State)gameTree.getNodeValue());
		theGame.ResetState();
		State[] stateArray = theGame.NextStates(currentPlayer+1);
		
		if ((currentDepth != depth)&&(!theGame.GameOver())&&(stateArray.length!=0))
		{
			for (int i=0; i<stateArray.length; i++)
			{
				evaluation.Evaluate(stateArray[i]);
				stateArray[i].SetScore(evaluation.GetEvaluation()[player-1]);
				//((GeneralTree)gameTree).add(new GeneralTree(stateArray[i]));
			}
			InsertionSort((GeneralTree)gameTree,stateArray);
			
			currentPlayer = theGame.NextPlayer(currentPlayer,totalPlayers);
			
			double[] minmax = new double[stateArray.length];
			for (int i=0; i<stateArray.length; i++)
				minmax[i] = MinMax(gameTree.getSubtree(i),currentDepth+1,currentPlayer);
			if ((currentDepth % 2) == 0)	
				return minmax[MaxIndex(minmax)];	
			else
				return minmax[MinIndex(minmax)];		
		}
		
		//((State)gameTree.getNodeValue()).Print();
		return ((State)gameTree.getNodeValue()).GetScore();
	}
}
