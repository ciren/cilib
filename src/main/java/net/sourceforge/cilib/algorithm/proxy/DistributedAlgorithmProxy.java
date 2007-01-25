package net.sourceforge.cilib.algorithm.proxy;

import net.sourceforge.cilib.algorithm.Algorithm;

public class DistributedAlgorithmProxy implements AlgorithmProxy {
	
	private static Algorithm localInstance = null;

	public Algorithm get() {
		return localInstance;
	}

	public void set(Algorithm algorithm) {
		localInstance = algorithm;
	}

}
