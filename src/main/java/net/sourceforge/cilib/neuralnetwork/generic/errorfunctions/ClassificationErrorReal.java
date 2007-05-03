/**
 * 
 */
package net.sourceforge.cilib.neuralnetwork.generic.errorfunctions;

import net.sourceforge.cilib.neuralnetwork.foundation.NNError;
import net.sourceforge.cilib.neuralnetwork.foundation.NNPattern;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.type.types.MixedVector;
import net.sourceforge.cilib.type.types.Real;

/**
 * @author Stefan
 *
 */
public class ClassificationErrorReal implements NNError {
	
	protected int numberPatternsCorrect = 0;
	protected int numberPatternsIncorrect = 0;
	protected double outputSensitivityThreshold;
	protected double percentageCorrect = 9999999;
	
	
	
	public ClassificationErrorReal() {
		//Default behaviour is that output - target < 0.1
		outputSensitivityThreshold = 0.2;
	}
	
	
	public void computeIteration(MixedVector output, NNPattern input) {
		
		boolean isCorrect = true;
		
		for (int i = 0; i < output.size(); i++){
			
			if ( Math.abs(((Real)output.get(i)).getReal() - input.getTarget().getReal(i)) > this.outputSensitivityThreshold ){
				isCorrect = false;
				break;
			}			
		}
		
		if (isCorrect){
			this.numberPatternsCorrect++;
		}
		else this.numberPatternsIncorrect++;	
	}
	
	
	public void finaliseError() {
			
		this.percentageCorrect = (double)this.numberPatternsCorrect / ((double)this.numberPatternsIncorrect + (double)this.numberPatternsCorrect) * 100;
		numberPatternsCorrect = 0;
		numberPatternsIncorrect = 0;
	}
	
	
	public void setValue(Object val){
		throw new UnsupportedOperationException("Setting value not supported as an operation");
	}
	
	
	public Double getValue() {
		return new Double(this.percentageCorrect);
	}	
	
	
	public int compareTo(Fitness f) {
		
		if (!(f instanceof MSEErrorFunction)) {
			throw new IllegalArgumentException("Incorrect class instance passed");
		}
		
		return (Double.valueOf(this.percentageCorrect)).compareTo((Double) ((ClassificationErrorReal) f).getValue());
	}
	
	public NNError clone(){
		ClassificationErrorReal tmp = new ClassificationErrorReal();
		tmp.setOutputSensitivityThreshold(this.outputSensitivityThreshold);
		tmp.percentageCorrect = this.percentageCorrect;
		tmp.numberPatternsCorrect = this.numberPatternsCorrect;
		tmp.numberPatternsIncorrect = this.numberPatternsIncorrect;
		return tmp;
	}	
	
	
	public String getName() {
		return new String("Classification error for Real numbers");
	}
	
	
	
	public void setNoPatterns(int noPatterns) {
		//not callable via XML interface as information set by EvaluationStrategy
		//unsupported action, so do nothing.		
	}
	
	
	
	public void setNoOutputs(int nr) {
		//unsupported action, so do nothing.
	}
	
	
	
	public double getOutputSensitivityThreshold() {
		return outputSensitivityThreshold;
	}
	
	
	
	public void setOutputSensitivityThreshold(double outputSensitivityThreshold) {
		this.outputSensitivityThreshold = outputSensitivityThreshold;
	}
	
	
	
	public int getNumberPatternsCorrect() {
		return numberPatternsCorrect;
	}
	
	
	
	public int getNumberPatternsIncorrect() {
		return numberPatternsIncorrect;
	}
	
	public String toString(){
		return new String(Integer.valueOf(this.numberPatternsCorrect).toString());
	}
	
	
	public void initialize() {
		//empty method needed for Chain of Responsibility pattern.
	}
	
	
	
}
