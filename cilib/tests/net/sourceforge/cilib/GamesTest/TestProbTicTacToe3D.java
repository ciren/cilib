/*
 * Created on 2004/03/09
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
public class TestProbTicTacToe3D 
{
	public static void main(String[] args) 
	{
		
		int gameDimensions = 3;
		int totalPlayers = 2;
		int player1 = 1;
		int player2 = 2;		
		Agent[] evaluate = new Agent[totalPlayers];
		evaluate[player1-1] = new Formula2PlayOutAgent(player1,totalPlayers,100,new ProbTicTacToe3D(gameDimensions),new RandomAgent(new Tausworthe()));
		//evaluate[player1-1] = new RandomEvaluate(new Tausworthe());
		evaluate[player2-1] = new RandomAgent(new MersenneTwister());
		
		Information info = new Information(1000,new ProbTicTacToe3D(gameDimensions),evaluate);

		for (int i=0; i<3; i++)
		{
			info.PlayGames(player1,false);
			info.PrintDetails();
		}
		
	}
}
