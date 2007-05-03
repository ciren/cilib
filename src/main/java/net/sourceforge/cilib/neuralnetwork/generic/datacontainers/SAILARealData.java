/*
 * Created on 2005/07/06
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.neuralnetwork.generic.datacontainers;

import java.util.ArrayList;

import net.sourceforge.cilib.neuralnetwork.generic.neuron.NeuronConfig;
import net.sourceforge.cilib.neuralnetwork.foundation.Initializable;
import net.sourceforge.cilib.neuralnetwork.foundation.NNPattern;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkDataIterator;
import net.sourceforge.cilib.neuralnetwork.generic.GenericTopology;
import net.sourceforge.cilib.neuralnetwork.generic.StandardLayerIterator;
import net.sourceforge.cilib.type.types.Real;

/**
 * @author stefanv
 *
 */
public class SAILARealData extends GenericData implements Initializable{
	
	protected GenericTopology topology;
	private int nrUpdates;
	protected ArrayList<NNPattern> mostInformative;
	protected ArrayList<Double> informativeness;

	
	public SAILARealData() {		
		super();				
		topology = null;
		this.nrUpdates = 1;		
	}
	
	public void initialize(){
		
		super.initialize();
		
		if (this.topology == null){
			throw new IllegalArgumentException("Required object was null during initialization");
		}
		if (this.nrUpdates <= 0){
			throw new IllegalArgumentException("Illegal number of updates selected - must be larger than 0");
		}
		
		if (this.getTrainingSetSize() != 0){
			throw new IllegalArgumentException("In SAILA, the starting training set size must be 0");
		}
		
		this.mostInformative = new ArrayList<NNPattern>();
		this.informativeness = new ArrayList<Double>();
		for (int i = 0; i < nrUpdates; i++){
			mostInformative.add(null);
			informativeness.add(new Double(-9999999));
		}
		this.activeLearningUpdate(null);		
	}
	
	
		
	protected void prioritisePattern(NNPattern p, double inform){
		
		//search for input position, index 0 being the best ie highest informativeness value.
		for (int i = 0; i < informativeness.size(); i++){
			
			if (informativeness.get(i) < inform){
				informativeness.add(i, new Double(inform));
				mostInformative.add(i, p);
				break;
			}
		}
		
		//trim the list to size, if an insertion was made
		if (informativeness.size() > this.nrUpdates){
			informativeness.remove(this.nrUpdates);
			mostInformative.remove(this.nrUpdates);
		}
		
	}
	
	
	public void activeLearningUpdate(Object input) {
		//At every update interval, determine pattern informativeness of all 
		//patterns in Candidate set, select most informative one 
		//to remove from candidate set and add to training set.
		
				
			//Iterate over each pattern p in the Candidate set Dc
			NeuralNetworkDataIterator DcIter = this.getCandidateSetIterator();
			
			while(DcIter.hasMore()){
				
				NNPattern p = DcIter.value();
				
				//Evaluate p against current NN topology.
				this.topology.evaluate(p);
				
				//determine informativeness of p
				double pInformativeness = this.calculateInformativeness(p);
				
				this.prioritisePattern(p, pInformativeness);
				
				DcIter.next();
			}//end iterate Dc
			
			//remove bestInformative patterns from candidate set Dc and add to training set Dt.
			while( (candidateSet.size() != 0) && (this.mostInformative.size() > 0) ){
				this.candidateSet.remove(mostInformative.get(0));
				this.trainingSet.add(mostInformative.get(0));
				this.mostInformative.remove(0);
				this.informativeness.remove(0);
			}
			
			//reset list
			this.mostInformative = new ArrayList<NNPattern>();
			this.informativeness = new ArrayList<Double>();
			for (int i = 0; i < nrUpdates; i++){
				mostInformative.add(null);
				informativeness.add(new Double(-9999999));
			}
			
		
	}


	
	protected double calculateInformativeness(NNPattern p) {
		
		double bestOutputSo = -999999.0;
		
		//Iterate over output neurons Ok
		StandardLayerIterator oIter = topology.getLayerIterator(2);
		
		while(oIter.hasMore()){
			
			double normOSVec = 0;
			
			if(oIter.value().getInputWeights() != null){
				//calculate the normalised output sensitivity vector (sum-norm)
				normOSVec = this.normalisedOSVector(oIter.value());
			}
			
			//select the max output unit, equation 4.2
			if (normOSVec > bestOutputSo){
				bestOutputSo = normOSVec;
			}
			
			oIter.nextNeuron();
		}//end oIter
		
		return bestOutputSo;
	}


	
	protected double normalisedOSVector(NeuronConfig Ok) {
		
		int nrInputs = Ok.getInput()[0].getInput().length;
		double[] inputVector = new double[nrInputs];
		double outputDeriv = (1.0 - ((Real)Ok.getCurrentOutput()).getReal() )
							 * ((Real)Ok.getCurrentOutput()).getReal();
		
		//For each input unit
		for (int i = 0; i < nrInputs -1; i++){
			
			inputVector[i] = 0.0;
		    
			//Iterate over Hidden units
			for (int h = 0; h < Ok.getInput().length; h++){
								
				NeuronConfig hidden = Ok.getInput()[h];
				
				//check that no bias unit used.
				if (hidden.getInputWeights() != null){
					//Evaluate equation E6 from SAILA paper, adding result to the input unit vector.
					inputVector[i] += ((Real) Ok.getInputWeights()[h].getWeightValue()).getReal() 
						              * ( (1.0 - ((Real)hidden.getCurrentOutput()).getReal()) * 
							          ((Real)hidden.getCurrentOutput()).getReal() ) *
							          ((Real) hidden.getInputWeights()[i].getWeightValue()).getReal();
					
				}
			}
			
			inputVector[i] *= outputDeriv;
					
		} //end for each input
		
		//Normalise the inputVector (sum-norm), equation 4.4
		double sumNorm = 0.0;
		
		for (int i = 0; i < nrInputs; i++){
			sumNorm += Math.abs(inputVector[i]);
		}
		
		return sumNorm;
	}

	public void setTopology(GenericTopology topology) {
		this.topology = topology;
	}

	public int getNrUpdates() {
		return nrUpdates;
	}

	public void setNrUpdates(int nrUpdates) {
		this.nrUpdates = nrUpdates;
	}
	
		
	
	
	
}
