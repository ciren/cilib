/*
 * Created on 2004/11/13
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.neuralnetwork.foundation;

import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Cloneable;


/**
 * @author stefanv
 *
 * 
 */
public interface NNPattern extends Cloneable {

	public int getTargetLength();

	public int getInputLength();

	public Vector getInput();

	public Vector getTarget();
	
	public NNPattern getClone();
	
	public void setInput(Vector v);
	
	public void setTarget(Vector t);
		
}
