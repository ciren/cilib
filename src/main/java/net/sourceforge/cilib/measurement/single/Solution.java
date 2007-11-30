/*
 * Solution.java
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
package net.sourceforge.cilib.measurement.single;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.StringType;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * @author Edwin Peer
 */
public class Solution implements Measurement {
	private static final long serialVersionUID = -7485598441585703760L;

	public Solution() {
	}
	
	public Solution(Solution copy) {
	}
	
	public Solution getClone() {
		return new Solution(this);
	}
	
	public String getDomain() {
		return "T";
	}

	public Type getValue() {
		StringType s = new StringType();
		
		Vector solution = (Vector) Algorithm.get().getBestSolution().getPosition();
		
		//s.setString(buffer.toString());
		s.setString(solution.toString());
		
		return s;
	}

}
