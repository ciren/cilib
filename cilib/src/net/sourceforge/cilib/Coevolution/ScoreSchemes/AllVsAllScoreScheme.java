/*
 * Created on Aug 17, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.Coevolution.ScoreSchemes;

import net.sourceforge.cilib.Games.Agents.*;
import net.sourceforge.cilib.Games.*;

/**
 * @author Vangos
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AllVsAllScoreScheme extends ScoreScheme{
	public AllVsAllScoreScheme()
	{}
	
	public void ScoreAgents(ParticleAgent[] players,Game theGame)
	{
		Reset(players);
		for (int j=0; j<players.length; j++)
			for (int k=0; k<players.length; k++)
			{
				if (j!=k)
				{
					theGame.SetAgent(players[j],1);
					theGame.SetAgent(players[k],2);
						
					int winner = theGame.PlayGame(false);
					switch(winner)
					{
						case 0:	players[j].GetParticleAdapter().IncrementScore(1);
								players[k].GetParticleAdapter().IncrementScore(1);
								break;
						case 1: players[j].GetParticleAdapter().IncrementScore(3);
								break;
						case 2: players[k].GetParticleAdapter().IncrementScore(3);
								break;
					}
				}	
			}
	}
}
