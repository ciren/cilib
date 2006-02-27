/*
 * Created on Aug 5, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.Coevolution.InputMasks;

import net.sourceforge.cilib.Games.States.*;

/**
 * @author Vangos
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TicTacToeMask extends InputMask {
	public TicTacToeMask()
	{}
	
	public double[] Mask(State state_)
	{
		double[] mask = new double[state_.GetSize()+2];
		
		for (int i=0; i<state_.GetSize(); i++)
			mask[i] = Modify(state_.GetGameState()[i].GetPlayer());
		mask[state_.GetSize()] = Modify(state_.GetCurrentPlayer());
		mask[state_.GetSize()+1] = -1;
		
		return mask;
	}
}
