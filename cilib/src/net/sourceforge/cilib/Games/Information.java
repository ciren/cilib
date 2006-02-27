/*
 * Created on Apr 12, 2004
 *
 * Copyright (C) 2003, 2004 - CIRG@UP 
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
 
package net.sourceforge.cilib.Games;

import net.sourceforge.cilib.Games.Agents.*;

/**
 * @author Vangos
 *
 * The class enables a number of games to take place and statistics to be accumulated
 */

public class Information 
{
	public Information(int totalGames_,Game game_,Agent[] evaluateArray_)
	{
		totalGames = totalGames_;
		theGame = game_;
		theEvaluations = evaluateArray_;
		players = theEvaluations.length;
		data = new double[players+1][totalGames];
		score = new double[players];
		wins = new int[players];
	}
	
	//Plays a certain number of games collecting statistics
	public void PlayGames(int startPlayer_,boolean print_)
	{
		for (int i=0; i<players; i++)
		{
			score[i] = 0;
			wins[i] = 0;
		}
			
		moves = 0;
		
		for (int i=0; i<totalGames; i++)
		{
			theGame.PlayGame(theEvaluations,startPlayer_,print_);
			double[] gameScore = theGame.GetScores();
			int winner = -1;
			double maximum = Integer.MIN_VALUE;
			for (int j=0; j<players; j++)
			{
				data[j][i] = gameScore[j];
				score[j] += gameScore[j];
				if (gameScore[j] > maximum)
				{
					winner = j;
					maximum = gameScore[j];
				}
				else if (gameScore[j] == maximum)
					winner = -1;
			}
			data[players][i] = theGame.GetCounter();
			moves += data[players][i];
			if (winner!=-1)
				wins[winner]++;
		}
	}	
	
	//Returns the scores of the games that were played
	public double[] GetScore()
	{ return score; }
	
	//Returns the wins of the games that were played
	public int[] GetWins()
	{ return wins; }
	
	//The average number of moves for all the games played
	public double GetAverageMoves()
	{ return 1.0*moves/totalGames; }
	
	//Outputs the raw results of the games played
	public void PrintRaw()
	{
		for (int i=0; i<totalGames; i++)
		{
			for (int j=0; j<players+1; j++)
				System.out.print(data[j][i]+ " ");
			System.out.println();
		}
		System.out.println();
	}
	
	//Outputs results of the games played
	public void PrintDetails()
	{
		int totalWins = 0;

		for (int j=0; j<players; j++)
		{
			totalWins += wins[j];
			System.out.println("Player " + (j+1) + ":\t" + wins[j] + "\t" + (100.0*wins[j]/totalGames) + "\t\t" + score[j]/wins[j]); 
		}
		System.out.println("Draw    "  + ":\t" + (totalGames-totalWins) + "\t" + (100.0*(totalGames-totalWins)/totalGames));
		System.out.println("Moves   "  + ":\t" + (1.0*moves/totalGames));  
		System.out.println();
	}
	
	//Total games to be played
	private int totalGames;
	
	//The game on which to collect information
	private Game theGame;
	
	//The evaluators to be used for the game
	private Agent[] theEvaluations;
	
	//The total players used for the game
	private int players;
	
	//An array to store all the raw data
	private double[][] data;
	
	//The score each player has accumulated
	private double[] score;
	
	//How many wins the player has acheived
	private int[] wins;
	
	//The total moves used
	private int moves;
}
