package net.sourceforge.cilib.neuralnetwork.generic.datacontainers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.StringTokenizer;

import net.sourceforge.cilib.neuralnetwork.foundation.NNPattern;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

//Draft class - not tested yet.
public class CrossValidationStrategy implements DataDistributionStrategy {
	
	protected BufferedReader inputReader;
	protected String file;
	protected Random shuffleRandomizer;
	
	protected int noInputs;
	
	//cross validation number of sets = K
	protected int K;
	protected int Koffset;
	protected int percentTrain;
	protected int percentGen;
	protected int percentCan;
	
		
	public CrossValidationStrategy() {
		
		this.noInputs = 999;
		this.K = 99999;
		this.Koffset = 999999;
		this.file= null;
		//Default choice, can be set explicitely.
		this.shuffleRandomizer = new Random(System.currentTimeMillis());
		
	}
	
	
	public void initialize(){
		
		if (this.file == null){
			throw new IllegalArgumentException("Required filename object was null during initialization");
		}
		
		if (this.noInputs == 999){
			throw new IllegalArgumentException("Required noInputs object was null during initialization");
		}
		
		if (this.percentCan + this.percentTrain + this.percentGen != 100){
			throw new IllegalArgumentException("Percentages for data sets do not add up to 100");
		}
		
		if (this.K == 99999) {
			throw new IllegalArgumentException("K-value is not set.");
		}
		
		if (this.Koffset >= this.K) {
			throw new IllegalArgumentException("K-offset is too large or not set correctly.");
		}
				
		
	}
	
	
	
	
	public void populateData(ArrayList<NNPattern> Dc, 
							 ArrayList<NNPattern> Dt,
							 ArrayList<NNPattern> Dg, 
							 ArrayList<NNPattern> Dv) {
		
		try {
			inputReader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Input data file not found...");
			
		}		
		try {
			while(inputReader.ready()) {
				String line = inputReader.readLine();
				
				StringTokenizer token = new StringTokenizer(line, " ");
				
				if(token.countTokens() <= this.noInputs)
					throw new IllegalStateException("IOException: Record lengths dont match or too many spaces in line.");
				
				Vector input = new Vector();		
				Vector target = new Vector();
				
				for (int i = 0; i < noInputs; i++){
					input.add(new Real(Double.parseDouble(token.nextToken())));
				}
				
				while(token.hasMoreTokens()){
					target.add(new Real(Double.parseDouble(token.nextToken())));
				}
				StandardPattern tmp = new StandardPattern();
				tmp.setInput(input);
				tmp.setTarget(target);
				Dc.add(tmp);
				
			}//end while
		}catch (IOException e){
			throw new IllegalStateException("IOException: Data not in correct format");
		}
		
		
		//==================================================================================
		//=   Distribute the patterns into Dc, Dt, Dg and Dv usinf K-fold crossvalidation  =
		//==================================================================================
		
		int KvalidationSize = (int)Math.floor(Dc.size() / this.K);
		int trainsetSize = (int)Math.floor( (Dc.size() - KvalidationSize) * this.percentTrain / 100);
		int gensetSize = (int)Math.floor( (Dc.size() - KvalidationSize) * this.percentGen / 100);
				
		if (Dc.size() % KvalidationSize > 0){
			throw new IllegalArgumentException("Invalid value for K - total patterns mod K is not zero.");
		}
		
		Collections.shuffle(Dc, this.shuffleRandomizer);
		
		//Validation set = Kth set of patterns
		for (int i = 0; i < KvalidationSize; i++){
			Dv.add(Dc.remove(this.Koffset * KvalidationSize));
		}
		
		//Generalization set
		for (int i = 0; i < gensetSize; i++){
			Dg.add(Dc.remove(0));
		}
		
		//Training set
		for (int i = 0; i < trainsetSize; i++){
			Dt.add(Dc.remove(0));
		}
	}


	public void setFile(String file) {
		this.file = file;
	}


	public void setK(int k) {
		K = k;
	}


	public void setKoffset(int koffset) {
		Koffset = koffset;
	}


	public void setNoInputs(int noInputs) {
		this.noInputs = noInputs;
	}


	public void setPercentCan(int percentCan) {
		this.percentCan = percentCan;
	}


	public void setPercentGen(int percentGen) {
		this.percentGen = percentGen;
	}


	public void setPercentTrain(int percentTrain) {
		this.percentTrain = percentTrain;
	}


	public void setShuffleRandomizer(Random shuffleRandomizer) {
		this.shuffleRandomizer = shuffleRandomizer;
	}
	
	
	
}
