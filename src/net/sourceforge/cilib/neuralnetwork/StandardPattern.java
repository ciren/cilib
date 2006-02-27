/*
 * StandardPattern.java
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
package net.sourceforge.cilib.neuralnetwork;

import java.util.ArrayList;

import net.sourceforge.cilib.neuralnetwork.foundation.NNPattern;




/**
 * @author stefanv
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class StandardPattern implements NNPattern {
	
	protected ArrayList input = null;
	protected ArrayList target = null;
	
	
	/**
	 * @param input
	 * @param target
	 */
	public StandardPattern(ArrayList input, ArrayList target) {
		super();
		this.input = input;
		this.target = target;
	}

	
	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Foundation.NNPattern#getInput()
	 */
	public ArrayList getInput() {
		return input;
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Foundation.NNPattern#getLengthInput()
	 */
	public int getInputLength() {
		return input.size();
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Foundation.NNPattern#getLengthTarget()
	 */
	public int getTargetLength() {
		return target.size();
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Foundation.NNPattern#getTarget()
	 */
	public ArrayList getTarget() {
		return target;
	}
	
	public NNPattern clone(){
		return new StandardPattern(input, target);
	}
	
	public String toString(){
		String tmp = new String();
				
		for (int i = 0; i < input.size(); i++){
			tmp+= (input.get(i) + " ");
		}
		tmp.concat(" | ");
		for (int i = 0; i < target.size(); i++){
			tmp += (target.get(i) + " ");
		}
		return tmp;
	}

		
}
