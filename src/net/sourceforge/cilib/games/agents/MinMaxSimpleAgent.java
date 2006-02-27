/*
 * MinMaxSimpleAgent.java
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

/**
 * @author Vangos
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MinMaxSimpleAgent extends MinMaxAgent{
	public MinMaxSimpleAgent(Game game_)
	{ super(game_); }
	
	protected double MinMax(Tree<State> gameTree,int currentDepth,int currentPlayer)
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
			InsertionSort((GeneralTree<State>)gameTree,stateArray);
			
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
