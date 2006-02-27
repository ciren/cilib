/*
 * MinMaxAgent.java
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
package net.sourceforge.cilib.games.agents;

import net.sourceforge.cilib.container.tree.*;
import net.sourceforge.cilib.games.*;
import net.sourceforge.cilib.games.states.*;
import net.sourceforge.cilib.games.states.evaluations.*;

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
		
		GeneralTree<State> handle = new GeneralTree<State>();
		InsertionSort(handle,stateArray_);
		
		for (int i=0; i<handle.getDegree(); i++)
		{
			score[i] = MinMax(handle.getSubtree(i),1,theGame.NextPlayer(player-1,totalPlayers));
		}
		return (State)handle.getSubtree(MaxIndex(score)).getNodeValue();
	}
	
	protected void InsertionSort(GeneralTree<State> tree,State[] stateArray)
	{
		boolean found;
		for (int i=0; i<stateArray.length; i++)
		{
			found = false;
			for (int j=0; j<tree.getDegree(); j++)
			{
				if (((State)(tree.getSubtree(j)).getNodeValue()).GetScore() <= stateArray[i].GetScore())
				{
					tree.add(j, new GeneralTree<State>(stateArray[i]));
					found = true;
					break;
				}
			}
			if (!found)
				tree.add(new GeneralTree<State>(stateArray[i]));
		}
	}
	
	protected abstract double MinMax(Tree<State> gameTree,int currentDepth,int currentPlayer);
	
	protected int depth;
	protected Evaluations evaluation;
}
