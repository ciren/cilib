package net.sourceforge.cilib.bioinf.sequencealignment;

import java.util.ArrayList;
import java.util.ListIterator;

//ONLY FOR GAPPED VERSION!

/**
 * @author fzablocki
 */
public class GapFourFour implements GapPenaltiesMethod {

	public double getPenalty(ArrayList<String> alignment) {
		
//	 	Now modify the fitness based on the formula to penalise gaps and gap groups
		int totalNumberGaps = 0;
		int gapGroups = 0;

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
			
			boolean flag = false;
			for (int i = 0; i < s.length()-1; i++)
			{
				if(s.charAt(i) == '-')
				{
					while(i < s.length()-1)
					{
						if ( s.charAt(++i) == '-') flag = true;
						else break;
					}
					if (flag) gapGroups++;
				}
				flag = false;
			}	
		}

		double gapPenalty = gapGroups*4 + (totalNumberGaps*0.4);
		//System.out.println("Penalty: " + gapPenalty);
		
		/*for (ListIterator j = alignment.listIterator(); j.hasNext(); )
		{
			String s = (String) j.next();
			System.out.println("'" + s + "'");
		}*/
		return gapPenalty;
	}
}
