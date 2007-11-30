/*
 * Created on 2004/12/09
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.neuralnetwork.generic.datacontainers;

import java.util.ArrayList;

import net.sourceforge.cilib.neuralnetwork.foundation.NNPattern;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkDataIterator;

/**
 * @author stefanv
 *
 */
public class LinearDataIterator implements NeuralNetworkDataIterator {
	
	
	GenericData data = null;
	int count;
	ArrayList<NNPattern> list = null;
	

	public LinearDataIterator(GenericData data, ArrayList<NNPattern> list) {
	
		this.data = data;
		this.list = list;
		this.count = 0;
		
	}

	
	public void next() {
		count++;
	}

	
	public boolean hasMore() {
		return (count < list.size()); 
	}

	
	public void reset() {
		count = 0;
	}

	
	public NNPattern value() {
		return list.get(count);
	}

	
	public int size() {
		return list.size();
	}

	
	public int currentPos() {
		return count;
	}
	
	public NeuralNetworkDataIterator getClone(){
		LinearDataIterator tmp = new LinearDataIterator(this.data, this.list);
		tmp.count = this.count;
		return tmp;
	}

}
