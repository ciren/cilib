/*
 * Created on Aug 17, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.Coevolution.ScoreSchemes;

import net.sourceforge.cilib.Games.Agents.*;
import net.sourceforge.cilib.Games.*;
import net.sourceforge.cilib.Random.MersenneTwister;

/**
 * @author Vangos
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AllVsRandomScoreScheme extends ScoreScheme{
	public AllVsRandomScoreScheme(int totalGames_)
	{ totalGames = totalGames_; }
	
	public void ScoreAgents(ParticleAgent[] players,Game theGame)
	{
		Reset(players);
		
		for (int j=0; j<players.length; j++)
		{
			int current = 0;
			for (int k=0; k<totalGames; k++)
			{
				theGame.SetAgent(new RandomAgent(new MersenneTwister()),current+1);
				theGame.SetAgent(players[j],theGame.NextPlayer(current,2)+1);
				
				int winner = theGame.PlayGame(false);
				if (winner == current+1)
					players[j].GetParticleAdapter().IncrementScore(1);
				if (winner == 0)
					players[j].GetParticleAdapter().IncrementScore(1);
				
				current = theGame.NextPlayer(current,2);
			}
		}
	}
	private int totalGames;
}
