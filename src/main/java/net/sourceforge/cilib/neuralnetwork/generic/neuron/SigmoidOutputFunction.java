/*
 * Copyright (C) 2003 - 2008
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
package net.sourceforge.cilib.neuralnetwork.generic.neuron;

import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;

/**
 * @author stefanv
 *
 */
public class SigmoidOutputFunction implements NeuronFunction{
	
	double lambda;
	
	
	public SigmoidOutputFunction() {
		lambda = 1;
	}
		
	
	
	public Type computeFunction(Type in) {
		return new Real(1.0 / (1.0 + Math.exp(-1.0 *lambda * ((Real) in).getReal())));
	}


	public Type computeDerivativeAtPos(Type pos) {
		return new Real(((Real) computeFunction(pos)).getReal() * (1 - ((Real) computeFunction(pos)).getReal()));
	}


	public Type computeDerivativeUsingLastOutput(Type lastOut) {
		return new Real(((Real) lastOut).getReal() * (1 - ((Real) lastOut).getReal()));
	}

}
