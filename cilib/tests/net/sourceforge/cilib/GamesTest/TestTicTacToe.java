/*
 * Created on Apr 12, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.GamesTest;

import net.sourceforge.cilib.Games.*;
import net.sourceforge.cilib.Games.Agents.*;
import net.sourceforge.cilib.Random.*;

/**
 * @author Vangos
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TestTicTacToe 
{
	public static void main(String[] args) 
	{
		int gameDimensions = 3;
		int totalPlayers = 3;
		int player1 = 1;
		int player2 = 2;
		int player3 = 3;
		
		Agent[] evaluate = new Agent[totalPlayers];
		//evaluate[player1-1] = new Formula1PlayOutAgent(player1,totalPlayers,10,new TicTacToe(gameDimensions),new RandomAgent(new Tausworthe()));
		evaluate[player1-1] = new RandomAgent(new MersenneTwister());
		evaluate[player2-1] = new RandomAgent(new MersenneTwister());
		evaluate[player3-1] = new RandomAgent(new MersenneTwister());
		
		Information info = new Information(100000,new TicTacToe(gameDimensions),evaluate);
		
		for (int i=0; i<3; i++)
		
		{
			info.PlayGames(player3,false);
			info.PrintDetails();
		}	
	}
}
