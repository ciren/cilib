/*
 * NNError.java
 * 
 * Created on Nov 13, 2004
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
package net.sourceforge.cilib.neuralnetwork.foundation;

import java.util.ArrayList;

import net.sourceforge.cilib.problem.Fitness;

/**
 * @author stefanv
 *
 * Any Neural network error function will at the very least require a pattern and an output.  This is the 
 * base from which all error functions are computed.  If any other fields are required, consider using a decorator
 * to add them to the class.
 * 
 * In theory the NNError class does not need to know about the topology info such as no. of output units, output etc.
 * as this can be derived from the parameters.
 * 
 */
public interface NNError extends Fitness {


	public void add(NNError e);
	
	public void addIteration(ArrayList output, NNPattern input);
	
	public void postEpochActions();
	
	public NNError clone();
	
	public void setValue(Object val);
	
	public Double getValue();

}
