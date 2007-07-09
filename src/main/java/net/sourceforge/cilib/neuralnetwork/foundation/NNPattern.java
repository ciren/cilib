/*
 * Created on 2004/11/13
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.neuralnetwork.foundation;

import net.sourceforge.cilib.type.types.container.MixedVector;


/**
 * @author stefanv
 *
 * 
 */
public interface NNPattern {

	public int getTargetLength();

	public int getInputLength();

	public MixedVector getInput();

	public MixedVector getTarget();
	
	public NNPattern clone();
	
	public void setInput(MixedVector v);
	
	public void setTarget(MixedVector t);
		
}
