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
public abstract class ScoreScheme {
	protected void Reset(ParticleAgent[] players)
	{
		for (int i=0; i<players.length; i++)
			players[i].GetParticleAdapter().SetScore(0);
	}
	
	public double GetFitness(ParticleAgent myPlayer, ParticleAgent[] allPlayers)
	{
		double score = 0;
		for (int i=0; i<allPlayers.length; i++)
		{
			if (myPlayer.GetParticleAdapter().GetScore() > 
					allPlayers[i].GetParticleAdapter().GetScore())
				score += 1.0;
		}
		return score;
	}
	
	public abstract void ScoreAgents(ParticleAgent[] players,Game theGame);
}
