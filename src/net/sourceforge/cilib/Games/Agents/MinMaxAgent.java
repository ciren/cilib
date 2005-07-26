/*
 * Created on 2004/04/29
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.Games.Agents;

import net.sourceforge.cilib.Games.States.*;
import net.sourceforge.cilib.Games.States.Evaluations.*;
import net.sourceforge.cilib.Container.Tree.*;
import net.sourceforge.cilib.Games.*;

/**
 * @author Vangos
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public abstract class MinMaxAgent extends Agent{
	public MinMaxAgent(Game game_)
	{
		super(game_);
		depth = 1;
		evaluation = game_.GetEvaluation();
	}
	
	public void SetDepth(int depth_)
	{ depth = depth_; }
	
	public State ReturnBest(State[] stateArray_)
	{
		for (int i=0; i<stateArray_.length; i++)
		{
			evaluation.Evaluate(stateArray_[i]);
			stateArray_[i].SetScore(evaluation.GetEvaluation()[player-1]);
		}
		
		double[] score = new double[stateArray_.length];
		
		GeneralTree handle = new GeneralTree();
		InsertionSort(handle,stateArray_);
		
		for (int i=0; i<handle.getDegree(); i++)
		{
			score[i] = MinMax(handle.getSubtree(i),1,theGame.NextPlayer(player-1,totalPlayers));
		}
		return (State)handle.getSubtree(MaxIndex(score)).getNodeValue();
	}
	
	protected void InsertionSort(GeneralTree tree,State[] stateArray)
	{
		boolean found;
		for (int i=0; i<stateArray.length; i++)
		{
			found = false;
			for (int j=0; j<tree.getDegree(); j++)
			{
				if (((State)(tree.getSubtree(j)).getNodeValue()).GetScore() <= stateArray[i].GetScore())
				{
					tree.add(j, new GeneralTree(stateArray[i]));
					found = true;
					break;
				}
			}
			if (!found)
				tree.add(new GeneralTree(stateArray[i]));
		}
	}
	
	protected abstract double MinMax(Tree gameTree,int currentDepth,int currentPlayer);
	
	protected int depth;
	protected Evaluations evaluation;
}
