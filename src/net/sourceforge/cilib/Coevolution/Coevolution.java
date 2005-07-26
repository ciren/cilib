/*
 * Created on Aug 5, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.Coevolution;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import net.sourceforge.cilib.Algorithm.Algorithm;
import net.sourceforge.cilib.Coevolution.InputMasks.TicTacToeMask;
import net.sourceforge.cilib.Coevolution.ScoreSchemes.ScoreScheme;
import net.sourceforge.cilib.Games.Game;
import net.sourceforge.cilib.Games.Agents.ParticleAgent;
import net.sourceforge.cilib.Problem.Fitness;
import net.sourceforge.cilib.Problem.OptimisationProblem;


/**
 * @author Vangos
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Coevolution extends Algorithm implements OptimisationProblem
{
	public Coevolution()
	{
		population = 10;
		theGame = null;
		theScoreScheme = null;
	}
	
	protected void performInitialisation()
	{
		players = new ParticleAgent[population];
		for (int i=0; i<population; i++)
		{
			ParticleAdapter adapter = new NNEvangelosParticle(10,10,1);
			adapter.SetInputMask(new TicTacToeMask());
			
			players[i] = new ParticleAgent();
			players[i].SetParticleAdapter(adapter);
		}
	}
	
	public void SetPopulation(int population_)
	{ population = population_; }
	
	public void SetGame (Game theGame_)
	{theGame = theGame_; }
	
	public void SetScoreScheme(ScoreScheme scoreScheme_)
	{ theScoreScheme = scoreScheme_; }
	
	protected void performIteration() 
	{
		theScoreScheme.ScoreAgents(players,theGame);
			
		int bestposi = 0;
		double bestscore = 0;
		double fitness = 0;
			
		for (int j=0; j<players.length; j++)
		{
			fitness += players[j].GetParticleAdapter().GetScore();
			double score = players[j].GetParticleAdapter().FindPersonalBest
				(players[j].GetParticleAdapter().GetScore());
			//System.out.println(score+"");
			if (score > bestscore)
			{
				bestscore = score;
				bestposi = j;
			}
			//System.out.println(j + " " + players[j].GetParticleAdapter().GetScore());
		}
			
		for (int j=0; j<players.length; j++)
		{
			players[j].GetParticleAdapter().UpdateParticle(players[bestposi].GetParticleAdapter());
		}
			
		System.out.println(fitness +"");
		//System.out.println(bestposi + " " + players[bestposi].GetParticleAdapter().GetScore());
		
		try
		{
			FileOutputStream fout = new FileOutputStream("Lucky3.dna");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(players[bestposi]);
			oos.close();
		} catch (Exception e) {}			
	}
	
	public void Start(int epochs_,ScoreScheme scoreScheme_)
	{
		for (int i=0; i<epochs_; i++)
		{
			System.out.print(i+": ");
		
			theScoreScheme.ScoreAgents(players,theGame);
			
			int bestposi = 0;
			double bestscore = 0;
			double fitness = 0;
			
			for (int j=0; j<players.length; j++)
			{
				fitness += players[j].GetParticleAdapter().GetScore();
				double score = players[j].GetParticleAdapter().FindPersonalBest
					(players[j].GetParticleAdapter().GetScore());
				//System.out.println(score+"");
				if (score > bestscore)
				{
					bestscore = score;
					bestposi = j;
				}
				//System.out.println(j + " " + players[j].GetParticleAdapter().GetScore());
			}
			
			for (int j=0; j<players.length; j++)
			{
				players[j].GetParticleAdapter().UpdateParticle(players[bestposi].GetParticleAdapter());
			}
			
			System.out.println(fitness +"");
			//System.out.println(bestposi + " " + players[bestposi].GetParticleAdapter().GetScore());
			
			try
			{
				FileOutputStream fout = new FileOutputStream("Lucky3.dna");
				ObjectOutputStream oos = new ObjectOutputStream(fout);
				oos.writeObject(players[bestposi]);
				oos.close();
			} catch (Exception e) {}
			
		}
	}
	
	private int population;
	private ParticleAgent[] players;
	private Game theGame;
	private ScoreScheme theScoreScheme;
	
	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.Problem.OptimisationProblem#getFitness(java.lang.Object, boolean)
	 */
	public Fitness getFitness(Object solution, boolean count) {
		//double[] weights = (double[]) solution;
		//Iterator i = pso.getTopology().particles();
		return null;
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.Problem.OptimisationProblem#getFitnessEvaluations()
	 */
	public int getFitnessEvaluations() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.Problem.OptimisationProblem#getDomain()
	 */
	/*public Domain getDomain() {
		// TODO Auto-generated method stub
		return null;
	}*/
	
	//private PSO pso;
}
