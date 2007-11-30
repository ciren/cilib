/*
 * Created on 2004/11/13
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.neuralnetwork.foundation;

import net.sourceforge.cilib.util.Cloneable;

/**
 * @author stefanv
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface NeuralNetworkDataIterator extends Cloneable {
	
	public void next();
	
	public boolean hasMore();
	
	public void reset();
	
	public NNPattern value();
	
	public int size();
	
	public int currentPos();
	
	public NeuralNetworkDataIterator getClone();

}
