/*
 * TestCoevolution.java
 * 
 * Created on Aug 5, 2004
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

import net.sourceforge.cilib.coevolution.*;
import net.sourceforge.cilib.coevolution.scoreschemes.AllVsRandomScoreScheme;
import net.sourceforge.cilib.games.TicTacToeGame;
import net.sourceforge.cilib.stoppingcondition.MaximumIterations;

/**
 * @author Vangos
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TestCoevolution {
	public static void main(String[] args) 
	{
		TicTacToeGame theGame = new TicTacToeGame();
		theGame.SetStartPlayer(1);
		theGame.SetDimension(3);
		
		Coevolution algo = new Coevolution();
		algo.SetGame(theGame);
		algo.SetScoreScheme(new AllVsRandomScoreScheme(1000));
		algo.addStoppingCondition(new MaximumIterations(20));
		algo.initialise();
		algo.run();
		
		
		/*
		TicTacToeGame theGame = new TicTacToeGame();
		theGame.SetStartPlayer(1);
		theGame.SetDimension(3);
		
		Coevolution population = new Coevolution(10,theGame);
		
		//ScoreScheme sc = new AllVsAllScoreScheme();
		ScoreScheme sc = new AllVsRandomScoreScheme(1000);
		
		population.Start(100,sc);
		*/
	}
}
