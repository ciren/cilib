/*
 * Created on Jul 14, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.Games.Agents;

import java.io.*;
import net.sourceforge.cilib.Games.*;
import net.sourceforge.cilib.Games.States.*;

/**
 * @author Vangos
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class User extends Agent{
	public User(Game game_)
	{ super(game_); }
	
	public State ReturnBest(State[] stateArray_)
	{
		int index = 0;
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		try
		{
			int press = 0;
			while (press != 2)
			{
				stateArray_[index].Print();
				press = in.read();
				System.out.print(press+"");
				switch(press)
				{
					case 97:
						if (++index == stateArray_.length)
							index = 0;
						break;
					case 115:
						if (--index == -1)
							index = stateArray_.length-1;
						break;
				}
			}
		}
		
		catch(IOException e)
		{}
			
		return stateArray_[index];
	}
}
