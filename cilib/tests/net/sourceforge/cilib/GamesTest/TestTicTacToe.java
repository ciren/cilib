/*
 * Created on Apr 12, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.GamesTest;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

import net.sourceforge.cilib.Games.*;
import net.sourceforge.cilib.Games.Agents.*;

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
