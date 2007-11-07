/*
 * Created on 2004/12/01
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.neuralnetwork.generic.datacontainers;

import net.sourceforge.cilib.neuralnetwork.foundation.Initializable;
import net.sourceforge.cilib.neuralnetwork.foundation.NNPattern;
import net.sourceforge.cilib.type.types.container.Vector;




/**
 * @author stefanv
 *
 */
public class StandardPattern implements NNPattern, Initializable {
	
	protected Vector input = null;
	protected Vector target = null;
	
	
	
	public StandardPattern() {
		super();
		this.input = null;
		this.target = null;
	}
	
	
	
	public StandardPattern(Vector input, Vector target) {
		super();
		this.input = input;
		this.target = target;
	}



	public void initialize(){
		if ((this.input == null)|| (this.target == null)){
			throw new IllegalArgumentException("Required object was null during initialization");
		}
	}

	
	
	public Vector getInput() {
		return input;
	}

	
	public int getInputLength() {
		return input.size();
	}

	
	public int getTargetLength() {
		return target.size();
	}

	
	public Vector getTarget() {
		return target;
	}
	
	public NNPattern clone(){
		StandardPattern tmp = new StandardPattern();
		tmp.setInput(this.input);
		tmp.setTarget(this.target);
		return tmp;
	}
	
	public String toString(){
		String tmp = new String();
		tmp += "{";		
		for (int i = 0; i < input.size(); i++){
			tmp+= (input.get(i) + " ");
		}
		tmp += " \t| ";
		for (int i = 0; i < target.size(); i++){
			tmp += (target.get(i) + " ");
		}
		tmp += "}";
		return tmp;
	}

	public void setInput(Vector input) {
		this.input = input;
	}

	public void setTarget(Vector target) {
		this.target = target;
	}
	
	

		
}
