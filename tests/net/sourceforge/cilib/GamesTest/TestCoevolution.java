/*
 * Created on Aug 5, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.GamesTest;

import net.sourceforge.cilib.Coevolution.*;
import net.sourceforge.cilib.Coevolution.ScoreSchemes.AllVsRandomScoreScheme;
import net.sourceforge.cilib.Games.TicTacToeGame;
import net.sourceforge.cilib.StoppingCondition.MaximumIterations;

/**
 * @author Vangos
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TestCoevolution {
	public static void main(String[] args) 
	{
		TicTacToeGame theGame = new TicTacToeGame();
		theGame.SetStartPlayer(1);
		theGame.SetDimension(3);
		
		Coevolution algo = new Coevolution();
		algo.SetGame(theGame);
		algo.SetScoreScheme(new AllVsRandomScoreScheme(1000));
		algo.addStoppingCondition(new MaximumIterations(20));
		algo.initialise();
		algo.run();
		
		
		/*
		TicTacToeGame theGame = new TicTacToeGame();
		theGame.SetStartPlayer(1);
		theGame.SetDimension(3);
		
		Coevolution population = new Coevolution(10,theGame);
		
		//ScoreScheme sc = new AllVsAllScoreScheme();
		ScoreScheme sc = new AllVsRandomScoreScheme(1000);
		
		population.Start(100,sc);
		*/
	}
}
