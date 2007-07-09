/*
 * Created on 2004/11/30
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.neuralnetwork.foundation;


import net.sourceforge.cilib.type.types.container.MixedVector;



/**
 * @author stefanv
 *  
 */
public abstract class EvaluationMediator implements Initializable{

	protected NNError[] prototypeError = null;
	protected NNError[] errorDg = null;
	protected NNError[] errorDt = null;
	protected NNError[] errorDv = null;
	protected int nrEvaluationsPerEpoch;
	
	protected NeuralNetworkTopology topology = null;
	protected NeuralNetworkData data = null;
	protected TrainingStrategy trainer = null;
	
	protected int totalEvaluations;
	
	
	
	public EvaluationMediator() {
		super();
		nrEvaluationsPerEpoch = 0;
		totalEvaluations = 0;
	}
	
	public void initialize(){
		
		if (this.data == null)  {			
			throw new IllegalArgumentException("Evaluation Strategy error: required data object was null");
		}
		if (this.topology == null)  {			
			throw new IllegalArgumentException("Evaluation Strategy error: required topology object was null");
		}
		if (this.prototypeError == null)  {			
			throw new IllegalArgumentException("Evaluation Strategy error: required prototypeError object was null");
		}
		if (this.trainer == null)  {			
			throw new IllegalArgumentException("Evaluation Strategy error: required trainer object was null");
		}
		
		//Initialize objects, depth first via Chain of Responsibility pattern.
		data.initialize();
		topology.initialize();
		trainer.setTopology(this.topology);
		trainer.initialize();
		
				
		this.errorDg = new NNError[prototypeError.length];
		this.errorDt = new NNError[prototypeError.length];
		this.errorDv = new NNError[prototypeError.length];
		
		for (int i=0; i < prototypeError.length; i++){
			
			this.errorDg[i] = prototypeError[i].clone();
			this.errorDg[i].setNoPatterns(this.data.getGeneralisationSetSize());
			this.errorDt[i] = prototypeError[i].clone();
			this.errorDt[i].setNoPatterns(this.data.getTrainingSetSize());
			this.errorDv[i] = prototypeError[i].clone();
			this.errorDv[i].setNoPatterns(this.data.getValidationSetSize());
		}
	}
	
	
	public void computeErrorIteration(NNError[] err, MixedVector output, NNPattern input){
		
		for (int e = 0; e < err.length; e++){
			err[e].computeIteration(output, input);
		}
		
	}
	
	public abstract MixedVector evaluate(NNPattern p);
	
	
	public NNError[] getErrorDg() {
		return errorDg;
	}

	
	public NNError[] getErrorDt() {
		return errorDt;
	}
	

	
	public NNError[] getErrorDv() {
		return errorDv;
	}


	
	public int getNrEvaluationsPerEpoch() {
		return nrEvaluationsPerEpoch;
	}
	
	
	
	public NeuralNetworkTopology getTopology() {
		return topology;
	}
	
	
	public int getTotalEvaluations() {
		return totalEvaluations;
	}
	
	
	protected abstract void learningEpoch();
	
	
	public void performLearning(){
		learningEpoch();
		totalEvaluations += nrEvaluationsPerEpoch;
		nrEvaluationsPerEpoch = 0;
		
	}
	public void finaliseErrors(NNError[] err){
		for (int e = 0; e < err.length; e++){
			err[e].finaliseError();
		}
	}
	public void resetError(NNError[] err){
		for (int e = 0; e < err.length; e++){
			err[e] = prototypeError[e].clone();
		}
	}
	
	public void setErrorDg(NNError[] errorDg) {
		throw new UnsupportedOperationException("Class member cannot be set directly - please use reset() method");
	}
	
	
	public void setErrorDt(NNError[] errorDt) {
		throw new UnsupportedOperationException("Class member cannot be set directly - please use reset() method");
	}
	
	
	public void setErrorDv(NNError[] errorDv) {
		throw new UnsupportedOperationException("Class member cannot be set directly - please use reset() method");
	}
	
	
	public void setErrorNoOutputs(NNError[] err, int nr){
		for (int e = 0; e < err.length; e++){
			err[e].setNoOutputs(nr);
		}
	}	
		
	public void setErrorNoPatterns(NNError[] err, int nr){
		for (int e = 0; e < err.length; e++){
			err[e].setNoPatterns(nr);
		}
	}
	
			
	public void setTopology(NeuralNetworkTopology topology_) {
		this.topology = topology_;
	}
	
	public NNError[] getPrototypeError() {
		return prototypeError;
	}
	
	public void addPrototypError(NNError proto){
		
		if (this.prototypeError == null){
			this.prototypeError = new NNError[1];
			this.prototypeError[0] = proto.clone();
		}
		else {
			NNError[] tmp = new NNError[this.prototypeError.length + 1];
			for (int i=0; i < this.prototypeError.length; i++){
				tmp[i] = this.prototypeError[i].clone();			
			}
			tmp[this.prototypeError.length] = proto.clone();
			this.prototypeError = tmp;
		}
	}

	public NeuralNetworkData getData() {
		return data;
	}

	public void setData(NeuralNetworkData data) {
		this.data = data;
	}

	public TrainingStrategy getTrainer() {
		return trainer;
	}

	public void setTrainer(TrainingStrategy trainer) {
		this.trainer = trainer;
	}
	
	

}
