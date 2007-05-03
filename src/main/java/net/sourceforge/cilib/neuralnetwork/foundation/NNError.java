/*
 * Created on 2004/11/13
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.neuralnetwork.foundation;

import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.type.types.MixedVector;

/**
 * @author stefanv
 *
 * 
 */
public interface NNError extends Fitness, Initializable {
	
	public void computeIteration(MixedVector output, NNPattern input);
	
	public void finaliseError();
	
	public NNError clone();
	
	public void setValue(Object val);
	
	public Double getValue();
	
	public String getName();
	
	public void setNoPatterns(int noPatterns);
	
	public void setNoOutputs(int nr);
	
}
