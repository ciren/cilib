/*
 * LinearDataIterator.java
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
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkDataIterator;

/**
 * @author stefanv
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class LinearDataIterator implements
		NeuralNetworkDataIterator {
	
	
	DefaultData data = null;
	int count;
	ArrayList<NNPattern> list = null;
	

	/**
	 * @param data
	 */
	public LinearDataIterator(DefaultData data, ArrayList<NNPattern> list) {
	
		this.data = data;
		this.list = list;
		this.count = 0;
		
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Foundation.NeuralNetworkDataIterator#next()
	 */
	public void next() {
		count++;
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Foundation.NeuralNetworkDataIterator#hasMore()
	 */
	public boolean hasMore() {
		return (count < list.size()); 
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Foundation.NeuralNetworkDataIterator#reset()
	 */
	public void reset() {
		count = 0;
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Foundation.NeuralNetworkDataIterator#value()
	 */
	public NNPattern value() {
		return list.get(count);
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Foundation.NeuralNetworkDataIterator#getSize()
	 */
	public int getSize() {
		return list.size();
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Foundation.NeuralNetworkDataIterator#getCurrentPos()
	 */
	public int getCurrentPos() {
		return count;
	}

}
