/*
 * Created on Aug 5, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.Coevolution.InputMasks;

import net.sourceforge.cilib.Games.States.State;

/**
 * @author Vangos
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public abstract class InputMask {
	
	protected double Modify(int input)
	{
		switch (input)
		{
			case 1:return 0.5;
			case 2:return -0.5;
		}
		return 0.0;
	}
	
	public abstract double[] Mask(State state_);
}
