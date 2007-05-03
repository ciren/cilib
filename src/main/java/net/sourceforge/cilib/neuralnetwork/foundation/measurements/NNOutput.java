package net.sourceforge.cilib.neuralnetwork.foundation.measurements;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkController;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkDataIterator;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkProblem;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkTopology;
import net.sourceforge.cilib.neuralnetwork.generic.datacontainers.GenericData;
import net.sourceforge.cilib.neuralnetwork.generic.datacontainers.RandomDistributionStrategy;
import net.sourceforge.cilib.type.types.MixedVector;
import net.sourceforge.cilib.type.types.StringType;
import net.sourceforge.cilib.type.types.Type;

public class NNOutput implements Measurement {
	
	private String inputFile;
	private String outputFile;
	private int noInputs;
	private NeuralNetworkTopology topology;
	private BufferedWriter out;
	
	public NNOutput() {
		this.inputFile = null;
		this.outputFile = null;
		
	
	}

	public String getDomain() {
		return "T";
	}

	public Type getValue() {
		this.topology = ((NeuralNetworkProblem) ((NeuralNetworkController) Algorithm.get()).getOptimisationProblem()).getEvaluationStrategy().getTopology();
		
		GenericData data = new GenericData();
		RandomDistributionStrategy distributor = new RandomDistributionStrategy();
		distributor.setFile(this.inputFile);
		distributor.setNoInputs(this.noInputs);
		distributor.setPercentCan(1000);
		data.setDistributor(distributor);
		data.initialize();		
		
		int iter = Algorithm.get().getIterations();
		try {
			out = new BufferedWriter(new FileWriter(this.outputFile + "_" + String.valueOf(iter) + ".txt"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		NeuralNetworkDataIterator iteratorDc = data.getCandidateSetIterator();
		iteratorDc.reset();
		
		while(iteratorDc.hasMore()){
			MixedVector outputDg = topology.evaluate(iteratorDc.value());
							
			try {
				out.write(iteratorDc.value().getInput().toString() + " " + outputDg.toString());
				out.newLine();
			} catch (IOException e) {
				e.printStackTrace();
				throw new IllegalStateException("Problem writing measurement to file...");
			}
			
			
			iteratorDc.next();
		}
		
		try {
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalStateException("Problem writing measurement to file...");
		}
		
				
		return new StringType(this.outputFile + "_" + String.valueOf(iter) + ".txt");
	}

	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}

	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}

	public void setNoInputs(int noInputs) {
		this.noInputs = noInputs;
	}

	
}
