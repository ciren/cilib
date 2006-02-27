/*
 * User.java
 * 
 * Created on Jan 01, 2005
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
package net.sourceforge.cilib.neuralnetwork.basicffnn;

import net.sourceforge.cilib.neuralnetwork.foundation.TrainingStrategy;

/**
 * @author stefanv
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class FFNNTrainingStrategy implements TrainingStrategy{
	
	FFNNTopology topology = null;
	
	

	/**
	 * @param topology
	 */
	public FFNNTrainingStrategy(FFNNTopology topology) {
		super();
		this.topology = topology;
	}
	
	
	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Foundation.TrainingStrategy#invokeTrainer(java.lang.Object)
	 */
	public void invokeTrainer(Object args) {
		topology.train();
		
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Foundation.TrainingStrategy#preEpochActions(java.lang.Object)
	 */
	public void preEpochActions(Object args) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Foundation.TrainingStrategy#postEpochActions(java.lang.Object)
	 */
	public void postEpochActions(Object args) {
		// TODO Auto-generated method stub
		
	}

}
