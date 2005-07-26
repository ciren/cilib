/*
 * BestParticlePosition.java
 *
 * Created on August 24, 2004, 12:16 AM
 *
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
 *   
 */

package net.sourceforge.cilib.Measurement;

import net.sourceforge.cilib.Domain.DomainVisitor;

/**
 * Measurement to print the position of the best particle.  Either in vector
 * format, or in row/column format.
 * 
 * @author jkroon
 */
@SuppressWarnings("unused")
public class BestParticlePosition implements Measurement, DomainVisitor { 
	
	private static final int FORMAT_VECTOR = 1;
	private static final int FORMAT_ROWCOL = 2;
	
	private int format = FORMAT_VECTOR;
	private StringBuffer accumulator = null;
	private boolean hasfirst;
	
    public BestParticlePosition() {
    }

    public String getDomain() {
    	return "?";
    }
    
    synchronized public Object getValue() {
		/*OptimisationAlgorithm alg = (OptimisationAlgorithm)Algorithm.get();
        Object solution = alg.getBestSolution().getPosition();
		DomainComponent domain = alg.getOptimisationProblem().getDomain();
		Object instances[] = new Object[]{solution};
		
		accumulator = new StringBuffer();
		hasfirst = false;
		
		accumulator.append("[");
		domain.acceptDomainVisitor(this, instances);
		accumulator.append("]");
		
		String ret = accumulator.toString();
		accumulator = null;
		return ret;*/
    	return null;
    }

	public void visitBit(Byte []instance) {
		doSep();
		accumulator.append(instance[0].toString());
	}

	public void visitBitSet(java.util.BitSet []i) {
		doSep();
		java.util.BitSet bs = i[0];
		String repr = bs.toString();
		accumulator.append(repr.substring(1, bs.size() * 3 - 1));
	}
	
	public void visitReal(Double []instance) {
		doSep();
		accumulator.append(instance[0].toString());
	}

	public void visitRealArray(double [][]inst) {
		double []d = inst[0];

		for(int i = 0; i < d.length; i++) {
			doSep();
			accumulator.append("" + d[i]);
		}
	}

	public void visitInteger(Integer []i) {
		doSep();
		accumulator.append(i[0].toString());
	}

	public void visitIntegerArray(int [][]in) {
		int []i = in[0];

		for(int j = 0; j < i.length; j++) {
			doSep();
			accumulator.append("" + i[j]);
		}
	}

	public void visitText(String []instances) {
		doSep();
		accumulator.append("\"");
		accumulator.append(instances[0]);
		accumulator.append("\"");
	}

	public void visitSet(java.util.Set []inst) {
		throw new RuntimeException("Cannot handle java.util.Set");
	}

	public void visitUnknown(Object []inst) {
		doSep();
		accumulator.append(inst[0].toString());
	}

	private void doSep() {
		if(hasfirst)
			accumulator.append(", ");
		else
			hasfirst = true;
	}
}
