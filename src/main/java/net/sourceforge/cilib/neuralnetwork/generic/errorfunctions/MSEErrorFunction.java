/*
 * Created on 2004/12/01
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.neuralnetwork.generic.errorfunctions;

import net.sourceforge.cilib.neuralnetwork.foundation.Initializable;
import net.sourceforge.cilib.neuralnetwork.foundation.NNError;
import net.sourceforge.cilib.neuralnetwork.foundation.NNPattern;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkData;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.type.types.MixedVector;
import net.sourceforge.cilib.type.types.Real;

/**
 * @author stefanv
 * 
 */
public class MSEErrorFunction implements NNError, Initializable {
	
	protected double value = 0;
	protected double computationData = 0;
	protected int noOutputs = 0;
	protected int noPatterns = 0;

	
	public MSEErrorFunction() {
		
		this.value = 0;
		this.computationData = 0;
		noOutputs = 0;
		noPatterns = 0;
	}	
	
	
	public MSEErrorFunction(int noOutputs, int noPatterns, double value) {
		
		//this constructor is used by clone() to ensure fewer function calls are made during cloning, as NNErrors are cloned often.
		this.noOutputs = noOutputs;
		this.noPatterns = noPatterns;
		this.computationData = 0;
		this.value = value;
	}
	
	
	
	
	public void initialize(){
		if ( (this.noOutputs <= 0) ){
			throw new IllegalArgumentException("Incorrect noOutputs variable set for class");
		}
						
		if (this.noPatterns < 0){
			throw new IllegalArgumentException("Negative noPatterns variable set for class");
		}	
		
	}
	
	
	public Double getValue() {
		if (computationData == 0){
			return new Double(value);
		}
		else throw new IllegalStateException("Error not finalised by finaliseError() method.  No value to return");
	}
	
	
	public int compareTo(Fitness arg0) {
		
		if (!(arg0 instanceof MSEErrorFunction)) {
			throw new IllegalArgumentException("Incorrect class instance passed");
		}
		
		if (computationData == 0){
			return this.getValue().compareTo(((MSEErrorFunction) arg0).getValue());
		}
		else throw new IllegalStateException("Error not finalised by finaliseError() method.  No value to compare");
		
	}
		
	
	public void computeIteration(MixedVector output, NNPattern input) {
		
		if (input.getTargetLength() != output.size()){
			throw new IllegalArgumentException("Output and target lenghts don't match");
		} else {
			
			for (int i = 0; i < output.size(); i++){
				this.computationData += Math.pow( ((Real)input.getTarget().get(i)).getReal()
						- ((Real)output.get(i)).getReal() , 2);
			}			
			
		}		
	}	
	
	
	public void finaliseError() {
		
		if (this.noOutputs == 0){
			throw new IllegalStateException("noOutputs is zero - division by zero");
		}
		
		if (this.noPatterns == 0){
			throw new IllegalStateException("noPatterns is zero - division by zero");
		}
		
		if (this.computationData != 0){
			this.value = computationData / ((double)noOutputs * (double)noPatterns);
		}
		computationData = 0;
	}
	
	
	public NNError clone(){
		MSEErrorFunction tmp = new MSEErrorFunction(this.noOutputs, this.noPatterns, this.value);
		tmp.computationData = this.computationData;
		return tmp;
	}
	
	
	public int getNoOutputs() {
		return noOutputs;
	}
	
	public void setNoOutputs(int noOutputs) {
		
		this.noOutputs = noOutputs;
	}
	
	public int getNoPatterns() {
		return noPatterns;
	}
	
	public void setNoPatterns(int noPatterns) {
		//not callable via XML interface as information set by EvaluationStrategy initialize()
		this.noPatterns = noPatterns;
		if (this.noPatterns < 0){
			throw new IllegalArgumentException("Negative noPatterns variable not set for class");
		}
	}
	
	
	public void setValue(Object val) {
		value = ((Double)val).doubleValue();
		computationData = 0;
	}
	
	public String getName() {
		return new String("MSE Error Function");
	}
	
	
	public NeuralNetworkData getData() {
		throw new IllegalAccessError("Illegal operation on class - obtain data object from EvaluationStrategy object instead");
	}
	
	public String toString(){
		return new String(Double.valueOf(this.value).toString());
	}
	
	
}