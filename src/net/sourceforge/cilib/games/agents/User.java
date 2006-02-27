/*
 * User.java
 * 
 * Created on Jul 24, 2004
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
package net.sourceforge.cilib.games.agents;

import java.io.*;
import net.sourceforge.cilib.games.*;
import net.sourceforge.cilib.games.states.*;

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
