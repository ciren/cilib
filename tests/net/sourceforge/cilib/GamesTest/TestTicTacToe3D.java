/*
 * Created on 2004/03/09
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
public class TestTicTacToe3D 
{
	public static void main(String[] args)
	{
		TicTacToe3DGame theGame = new TicTacToe3DGame();
		theGame.SetStartPlayer(1);
		theGame.SetDimension(3);
		
		//MinMaxAgent agent1 = new MinMaxSimpleAgent(new TicTacToe3DGame(theGame));
		//agent1.SetDepth(2);
		//PlayOutAgent agent2 = new SimplePlayOutAgent(new TicTacToe3DGame(theGame));
		//agent2.SetSamples(10);
		PlayOutAgent agent3 = new Formula1PlayOutAgent(new TicTacToe3DGame(theGame));
		agent3.SetSamples(10);
		//PlayOutAgent agent4 = new Formula2PlayOutAgent(new TicTacToe3DGame(theGame));
		//agent4.SetSamples(10);
		
		theGame.SetAgent(agent3,1);
		
		Information info = new Information(theGame);
		info.SetTotalGames(3000);
		
		for (int i=0; i<1; i++)
		{
			info.PlayGames(false,true);
			info.PrintDetails();
		}
	}
}
