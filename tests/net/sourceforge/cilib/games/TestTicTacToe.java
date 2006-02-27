/*
 * TestTicTacToe.java
 * 
 * Created on Apr 12, 2004
 *
 * Copyright (C) 2004 - CIRG@UP 
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
package net.sourceforge.cilib.games;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

import net.sourceforge.cilib.games.agents.Agent;
import net.sourceforge.cilib.games.agents.ParticleAgent;

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
		TicTacToeGame theGame = new TicTacToeGame();
		theGame.SetStartPlayer(1);
		theGame.SetDimension(3);
		
		//Agent user = new User(new TicTacToeGame(theGame));
		//MinMaxAgent agent1 = new MinMaxSimpleAgent(new TicTacToeGame(theGame));	
		//agent1.SetDepth(2);
		//PlayOutAgent agent2 = new SimplePlayOutAgent(new TicTacToeGame(theGame));
		//agent2.SetSamples(10);
		//PlayOutAgent agent3 = new Formula1PlayOutAgent(new TicTacToeGame(theGame));
		//agent3.SetSamples(10);
		//PlayOutAgent agent4 = new Formula2PlayOutAgent(new TicTacToeGame(theGame));
		//agent4.SetSamples(10);
		//ParticleAgent agent5 = new ParticleAgent();
		//agent5.SetParticleAdapter(theGame.GetParticleAdapter());

		Agent agent6 = null;
		try {
			FileInputStream fin = new FileInputStream("Lucky3.dna");
			ObjectInputStream ois = new ObjectInputStream(fin);
			agent6 = (ParticleAgent) ois.readObject();
		    ois.close();
		    }
		//catch (FileNotFoundException e){ System.out.println("File not found!");}
		catch (Exception e){ System.out.println("Error: " + e.getMessage()); }

		theGame.SetAgent(agent6,1);
		
		Information info = new Information(theGame);
		info.SetTotalGames(10000);
	
		for (int i=0; i<1; i++)
		{
			info.PlayGames(false,true);
			info.PrintDetails();
		}
	}
}
