/*
 * Created on Jul 11, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.GamesTest;

import net.sourceforge.cilib.Games.*;
import net.sourceforge.cilib.Games.Agents.*;

/**
 * @author Vangos
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TestProbTicTacToe3D 
{
	public static void main(String[] args) 
	{
		ProbTicTacToe3DGame theGame = new ProbTicTacToe3DGame();
		theGame.SetStartPlayer(1);
		theGame.SetDimension(3);
		
		MinMaxAgent agent1 = new MinMaxSimpleAgent(new ProbTicTacToe3DGame(theGame));	
		agent1.SetDepth(1);
		//PlayOutAgent agent2 = new SimplePlayOutAgent(new ProbTicTacToe3DGame(theGame));
		//agent2.SetSamples(10);
		//PlayOutAgent agent3 = new Formula1PlayOutAgent(new ProbTicTacToe3DGame(theGame));
		//agent3.SetSamples(10);
		//PlayOutAgent agent4 = new Formula2PlayOutAgent(new ProbTicTacToe3DGame(theGame));
		//agent4.SetSamples(10);
		
		theGame.SetAgent(agent1,1);
		
		Information info = new Information(theGame);
		info.SetTotalGames(10000);
		
		for (int i=0; i<1; i++)
		{
			info.PlayGames(false,true);
			info.PrintDetails();
		}
	}
}
