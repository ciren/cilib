/*
 * MultipleSolutions.java
 *
 * Created on January 17, 2003, 4:54 PM
 *
 * Copyright (C) 2003 - Clive Naicker
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
 *
 */
package net.sourceforge.cilib.measurement.multiple;

import java.util.Collection;
import java.util.Iterator;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.OptimisationAlgorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.type.types.MixedVector;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.Vector;

/**
 * 
 * @author Edwin Peer
 */
public class MultipleSolutions implements Measurement {

	public MultipleSolutions() {
	}

	public String getDomain() {
		//return "?^N";
		//return "R";
		return "T";
	}
	
	public Type getValue() {
		//return ((OptimisationAlgorithm) Algorithm.get()).getSolutions().toArray();
		
		Vector v = new MixedVector();
		Collection<OptimisationSolution> p = ((OptimisationAlgorithm) Algorithm.get()).getSolutions();
		for (Iterator<OptimisationSolution> i = p.iterator(); i.hasNext(); ) {
			Number n = (Number) i.next().getFitness().getValue();
			v.append(new Real(n.doubleValue()));
		}
		
		return v;
	}
	
}
