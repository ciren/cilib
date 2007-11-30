/*
 * Created on 2005/01/21
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.neuralnetwork.generic;

import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.util.Cloneable;

/**
 * @author stefanv
 *
 */
public class Weight implements Cloneable {
	
	Type weightValue = null;
	
	//a multi-purpose variable to track changes - value/menaingdepends on the using class
	Type previousChange = null;
	
	/**
	 * @param weightValue
	 */
	public Weight() {
		this.weightValue = null;
		this.previousChange = null;
	}
		
	
	public Weight(Type w){
		super();
		this.weightValue = w.getClone();
		this.previousChange = w.getClone();
		this.previousChange.reset();
	}

	
	public Weight getClone(){
		Weight clone = new Weight(this.weightValue);
		clone.previousChange = this.previousChange;
		return clone;
	}
	
	
	public Type getWeightValue() {
		return weightValue;
	}
	
	public void setWeightValue(Type weightValue) {
		this.weightValue = weightValue;
	}
		
	
	public Type getPreviousChange() {
		return previousChange;
	}
	
	
	public void setPreviousChange(Type previousChange) {
		this.previousChange = previousChange;
	}

	
}
