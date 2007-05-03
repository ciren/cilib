package net.sourceforge.cilib.neuralnetwork.generic.datacontainers;

import java.util.ArrayList;

import net.sourceforge.cilib.neuralnetwork.foundation.NNPattern;

public interface DataDistributionStrategy {
	
	public void populateData(ArrayList<NNPattern> Dc,
							 ArrayList<NNPattern> Dt,
							 ArrayList<NNPattern> Dg,
							 ArrayList<NNPattern> Dv);
	
	public void initialize();

}
