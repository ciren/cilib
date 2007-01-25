/*
 * GapOpeningAndExtensionPenalty.java
 *
 * Created on Sep 21, 2004
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

package net.sourceforge.cilib.bioinf.sequencealignment;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * @author fzablocki
 */
public class GapOpeningAndExtensionPenalty implements GapPenaltiesMethod {

	double gapOpeningPenalty;
	double gapOpeningExtension;
	
	public void setGapOpeningExtension(double gapOpeningExtension) {
		this.gapOpeningExtension = gapOpeningExtension;
	}

	public void setGapOpeningPenalty(double gapOpeningPenalty) {
		this.gapOpeningPenalty = gapOpeningPenalty;
	}
	
	public double getPenalty(ArrayList<String> alignment) {
		
//	 	Now modify the fitness based on the formula to penalise gaps and gap groups
		int totalNumberGaps = 0;
		int extensionCounter = 0;

		for (ListIterator l = alignment.listIterator(); l.hasNext(); )
		{
			String s = (String) l.next();
			for (int i = 0; i < s.length(); i++)
			{
				if (s.charAt(i) == '-')
				{ 
					totalNumberGaps++; 
				}
			}
			
			for (int i = 0; i < s.length()-1; i++)
			{
				if(s.charAt(i) == '-')
				{
					while(i < s.length()-1)
					{
						if ( s.charAt(++i) == '-') extensionCounter++;

						else break;
					}
				}
			}	
		}

		double gapPenalty = (extensionCounter* gapOpeningExtension) + (totalNumberGaps*gapOpeningPenalty);
		//System.out.println("extensionCounter"+ extensionCounter);
		//System.out.println("totalNumberGaps"+ totalNumberGaps);
		//System.out.println("Penalty: " + gapPenalty);
		return gapPenalty;
	}
}
