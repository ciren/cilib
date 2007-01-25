/*
 * Coevolution.java
 * 
 * Created on 2005/08/17
 *
 * Copyright (C) 2003, 2005 - CIRG@UP 
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
 * 
 */
package net.sourceforge.cilib.coevolution;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import net.sourceforge.cilib.coevolution.inputmasks.TicTacToeMask;
import net.sourceforge.cilib.coevolution.scoreschemes.ScoreScheme;
import net.sourceforge.cilib.games.Game;
import net.sourceforge.cilib.games.agents.ParticleAgent;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.problem.dataset.DataSetBuilder;
import net.sourceforge.cilib.type.DomainRegistry;


/**
 * @author Vangos
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Coevolution extends MultiPopulationBasedAlgorithm implements OptimisationProblem
{
	public Coevolution()
	{
		population = 10;
		theGame = null;
		theScoreScheme = null;
	}
	
	public Coevolution(Coevolution copy) {
		this.population = copy.population;
		//this.theGame = copy.theGame.clone();
		//this.theScoreScheme = copy.theScoreScheme.clone();
	}
	
	public Coevolution clone() {
		return new Coevolution(this);
	}
	
	public void performInitialisation()
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
	
	public void performIteration() 
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

	public DomainRegistry getDomain() {
		throw new RuntimeException("Get domain on Coevolution still needs to be defined!");
	}

	public DomainRegistry getBehaviouralDomain() {
		// TODO Auto-generated method stub
		return null;
	}

	public DataSetBuilder getDataSetBuilder() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setDataSetBuilder(DataSetBuilder dataSet) {
		// TODO Auto-generated method stub
		
	}

	public OptimisationSolution getBestSolution() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<OptimisationSolution> getSolutions() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.Problem.OptimisationProblem#getDomain()
	 */
	/*public Domain getDomain() {
		return null;
	}*/
	
	//private PSO pso;
}
